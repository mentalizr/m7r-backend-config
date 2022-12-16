package org.mentalizr.backend.config.instance;

import de.arthurpicht.utils.core.collection.Maps;
import de.arthurpicht.utils.core.strings.SimplifiedGlob;
import org.mentalizr.backend.config.BackendConfigException;
import org.mentalizr.serviceObjects.frontend.patient.ApplicationConfigPatientSO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InstanceConfigurationMap {

    public static class Builder {

        private final Map<String, ApplicationConfigPatientSO> configurationMap;
        private final Map<String, Pattern> patternMap;

        public Builder() {
            this.configurationMap = new HashMap<>();
            this.patternMap = new HashMap<>();
        }

        public void put(String programIdGlobEx, ApplicationConfigPatientSO applicationConfigPatientSO) {
            if (this.configurationMap.containsKey(programIdGlobEx))
                throw new BackendConfigException("Multiple configurations for program [" + programIdGlobEx + "]" +
                        " in mentalizr instance configuration.");
            this.configurationMap.put(programIdGlobEx, applicationConfigPatientSO);
            Pattern pattern = SimplifiedGlob.compile(programIdGlobEx);
            this.patternMap.put(programIdGlobEx, pattern);
        }

        public InstanceConfigurationMap build() {
            return new InstanceConfigurationMap(this.configurationMap, this.patternMap);
        }
    }

    private final Map<String, ApplicationConfigPatientSO> configurationMap;
    private final Map<String, Pattern> patternMap;

    private InstanceConfigurationMap(Map<String, ApplicationConfigPatientSO> configurationMap,
                                     Map<String, Pattern> patternMap) {
        this.configurationMap = Maps.immutableMap(configurationMap);
        this.patternMap = Maps.immutableMap(patternMap);
    }

    public ApplicationConfigPatientSO getApplicationConfigPatientSO(String programId) {
        List<String> programIdGlobExList = new ArrayList<>(this.configurationMap.keySet());
        for (String programIdGlobEx : programIdGlobExList) {
            Pattern pattern = this.patternMap.get(programIdGlobEx);
            Matcher matcher = pattern.matcher(programId);
            if (matcher.matches()) return this.configurationMap.get(programIdGlobEx);
        }
        return this.configurationMap.get(InstanceConfiguration.DEFAULT);
    }

}
