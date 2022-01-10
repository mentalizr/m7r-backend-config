package org.mentalizr.backend.config;

import de.arthurpicht.utils.core.assertion.AssertMethodPrecondition;
import de.arthurpicht.utils.core.collection.Maps;
import de.arthurpicht.utils.core.strings.Strings;
import org.mentalizr.serviceObjects.frontend.patient.ApplicationConfigPatientSO;
import org.mentalizr.serviceObjects.frontend.therapist.ApplicationConfigTherapistSO;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BrandingConfiguration {

    public static final String DEFAULT = "default";
    public static final String GENERIC = "generic";

    private final Map<String, ApplicationConfigPatientSO> applicationConfigPatientSOMap;
    private final BrandingConfigurationGeneric brandingConfigurationGeneric;

    public BrandingConfiguration(Map<String, ApplicationConfigPatientSO> applicationConfigPatientSOMap, BrandingConfigurationGeneric brandingConfigurationGeneric) {
        AssertMethodPrecondition.parameterNotNull("applicationConfigPatientSOMap", applicationConfigPatientSOMap);
        this.applicationConfigPatientSOMap = Maps.immutableMap(applicationConfigPatientSOMap);
        this.brandingConfigurationGeneric = brandingConfigurationGeneric;
    }

    public ApplicationConfigPatientSO getApplicationConfigPatientSO(String programId) {
        List<String> keyList = new ArrayList<>(this.applicationConfigPatientSOMap.keySet());
        for (String key : keyList) {
            if (key.equals(programId)) return this.applicationConfigPatientSOMap.get(key);
            if (matchesAsteriskPattern(key, programId)) return this.applicationConfigPatientSOMap.get(key);
        }
        return this.applicationConfigPatientSOMap.get(DEFAULT);
    }

    public BrandingConfigurationGeneric getBrandingConfigurationGeneric() {
        return this.brandingConfigurationGeneric;
    }

    private boolean matchesAsteriskPattern(String key, String programId) {
        if (!key.endsWith("*")) return false;
        String pattern = Strings.cutEnd(key, 1);
        return (programId.startsWith(pattern));
    }

    public ApplicationConfigTherapistSO getApplicationConfigTherapistSO() {
        ApplicationConfigPatientSO applicationConfigPatientSO = getApplicationConfigPatientSO(DEFAULT);

        ApplicationConfigTherapistSO applicationConfigTherapistSO = new ApplicationConfigTherapistSO();
        applicationConfigTherapistSO.setName(applicationConfigPatientSO.getName());
        applicationConfigTherapistSO.setLogo(applicationConfigPatientSO.getLogo());

        return applicationConfigTherapistSO;
    }

}
