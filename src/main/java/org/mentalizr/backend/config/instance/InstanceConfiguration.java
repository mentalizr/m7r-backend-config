package org.mentalizr.backend.config.instance;

import org.mentalizr.serviceObjects.frontend.application.ApplicationConfigGenericSO;
import org.mentalizr.serviceObjects.frontend.patient.ApplicationConfigPatientSO;
import org.mentalizr.serviceObjects.frontend.therapist.ApplicationConfigTherapistSO;

import static de.arthurpicht.utils.core.assertion.MethodPreconditions.assertArgumentNotNull;

public class InstanceConfiguration {

    public static final String DEFAULT = "default";
    public static final String GENERIC = "generic";

    private final InstanceConfigurationMap instanceConfigurationMap;
    private final ApplicationConfigGenericSO applicationConfigGenericSO;

    public InstanceConfiguration(InstanceConfigurationMap instanceConfigurationMap, ApplicationConfigGenericSO applicationConfigGenericSO) {
        assertArgumentNotNull("instanceConfigurationMap", instanceConfigurationMap);
        this.instanceConfigurationMap = instanceConfigurationMap;
        this.applicationConfigGenericSO = applicationConfigGenericSO;
    }

    public ApplicationConfigPatientSO getApplicationConfigPatientSO(String programId) {
        return this.instanceConfigurationMap.getApplicationConfigPatientSO(programId);
    }

    public ApplicationConfigGenericSO getApplicationConfigGenericSO() {
        return this.applicationConfigGenericSO;
    }

    public ApplicationConfigTherapistSO getApplicationConfigTherapistSO() {
        ApplicationConfigPatientSO applicationConfigPatientSO = getApplicationConfigPatientSO(DEFAULT);

        ApplicationConfigTherapistSO applicationConfigTherapistSO = new ApplicationConfigTherapistSO();
        applicationConfigTherapistSO.setName(applicationConfigPatientSO.getName());
        applicationConfigTherapistSO.setLogo(applicationConfigPatientSO.getLogo());

        return applicationConfigTherapistSO;
    }

}
