package org.mentalizr.backend.config;

import de.arthurpicht.configuration.Configuration;
import de.arthurpicht.configuration.ConfigurationFactory;
import de.arthurpicht.configuration.ConfigurationFileNotFoundException;
import org.mentalizr.serviceObjects.frontend.patient.ApplicationConfigPatientSO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class BrandingConfigurationFactory {

    private static final Logger logger = LoggerFactory.getLogger(BrandingConfigurationFactory.class);

    private static final String M7R_CONFIG_FILE = "m7r-branding.conf";

    private static final String GENERIC_TITLE = "title";
    private static final String GENERIC_LOGO = "logo";

    private static final String NAME = "name";
    private static final String LOGO = "logo";
    private static final String PROGRAM = "program";
    private static final String ACTIVITY_DIARY = "activityDiary";
    private static final String MOOD_DIARY = "moodDiary";
    private static final String MESSAGES = "messages";
    private static final String QUESTIONING = "questioning";
    private static final String VIDEO_CONFERENCE = "videoConference";
    private static final String THERAPIST = "therapist";

    private final Map<String, ApplicationConfigPatientSO> applicationConfigMap;
    private final BrandingConfigurationGeneric brandingConfigurationGeneric;

    public static BrandingConfiguration createProjectConfiguration() {
        BrandingConfigurationFactory brandingConfigurationFactory = new BrandingConfigurationFactory();
        return new BrandingConfiguration(
                brandingConfigurationFactory.getApplicationConfigMap(),
                brandingConfigurationFactory.getBrandingConfigurationGeneric()
        );
    }

    public static BrandingConfiguration createProjectConfiguration(String configurationFilePath) {
        BrandingConfigurationFactory brandingConfigurationFactory = new BrandingConfigurationFactory(configurationFilePath);
        return new BrandingConfiguration(
                brandingConfigurationFactory.getApplicationConfigMap(),
                brandingConfigurationFactory.brandingConfigurationGeneric);
    }

    private BrandingConfigurationFactory() {
        logger.info("Start initializing application configuration. Bind [" + M7R_CONFIG_FILE + "] from classpath." );

        ConfigurationFactory configurationFactory = new ConfigurationFactory();
        bindConfigurationFromClasspath(configurationFactory);

        this.applicationConfigMap = obtainApplicationConfigMap(configurationFactory);
        this.brandingConfigurationGeneric = obtainBrandingConfigurationGeneric(configurationFactory);
    }


    private BrandingConfigurationFactory(String configurationFilePath) {
        logger.info("Start initializing application configuration. Bind [" + configurationFilePath + "]." );

        ConfigurationFactory configurationFactory = new ConfigurationFactory();
        bindConfigurationFromFilesystem(configurationFactory, new File(configurationFilePath));

        this.applicationConfigMap = obtainApplicationConfigMap(configurationFactory);
        this.brandingConfigurationGeneric = obtainBrandingConfigurationGeneric(configurationFactory);
    }

    public Map<String, ApplicationConfigPatientSO> getApplicationConfigMap() {
        return this.applicationConfigMap;
    }

    public BrandingConfigurationGeneric getBrandingConfigurationGeneric() {
        return this.brandingConfigurationGeneric;
    }

    private Map<String, ApplicationConfigPatientSO> obtainApplicationConfigMap(ConfigurationFactory configurationFactory) {
        if (!configurationFactory.hasSection(BrandingConfiguration.DEFAULT))
            throw new RuntimeException("No section [default] found in configuration file [" + M7R_CONFIG_FILE + "].");

        Map<String, ApplicationConfigPatientSO> projectConfigSOMap = new HashMap<>();
        for (String sectionName : configurationFactory.getSectionNames()) {
            if (sectionName.equalsIgnoreCase(BrandingConfiguration.GENERIC)) continue;
            Configuration projectConfiguration = configurationFactory.getConfiguration(sectionName);
            ApplicationConfigPatientSO applicationConfigSO = getApplicationConfigPatientSO(projectConfiguration);
            projectConfigSOMap.put(sectionName, applicationConfigSO);
        }

        return projectConfigSOMap;
    }

    private BrandingConfigurationGeneric obtainBrandingConfigurationGeneric(ConfigurationFactory configurationFactory) {
        if (!configurationFactory.hasSection(BrandingConfiguration.GENERIC))
            throw new RuntimeException("No section [generic] found in configuration file [" + M7R_CONFIG_FILE + "].");

        Configuration configuration = configurationFactory.getConfiguration(BrandingConfiguration.GENERIC);

        if (!configuration.containsKey(GENERIC_TITLE))
            throw new RuntimeException("Configuration key [" + GENERIC_TITLE + "] missing in section " +
                    "[" + BrandingConfiguration.GENERIC + "] of configuration file [" + M7R_CONFIG_FILE + "].");
        String title = configuration.getString(GENERIC_TITLE);

        if (!configuration.containsKey(GENERIC_LOGO))
            throw new RuntimeException("Configuration key [" + GENERIC_LOGO + "] missing in section " +
                    "[" + BrandingConfiguration.GENERIC + "] of configuration file [" + M7R_CONFIG_FILE + "].");
        String logo = configuration.getString(GENERIC_LOGO);

        return new BrandingConfigurationGeneric(title, logo);
    }

    private void bindConfigurationFromClasspath(ConfigurationFactory configurationFactory) {
        try {
            configurationFactory.addConfigurationFileFromClasspath(M7R_CONFIG_FILE);
        } catch (ConfigurationFileNotFoundException | IOException e) {
            String message = "Configuration file " + M7R_CONFIG_FILE + " not found on classpath.";
            throw new RuntimeException(message);
        }
    }

    private void bindConfigurationFromFilesystem(ConfigurationFactory configurationFactory, File configFile) {
        try {
            configurationFactory.addConfigurationFileFromFilesystem(configFile);
        } catch (ConfigurationFileNotFoundException | IOException e) {
            String message = "Configuration file [" + configFile.getAbsolutePath() + "] not found.";
            logger.error(message);
            throw new RuntimeException(message);
        }
    }

    private ApplicationConfigPatientSO getApplicationConfigPatientSO(Configuration configuration) {
        String name = configuration.getString(NAME);
        String logo = configuration.getString(LOGO);
        ApplicationConfigPatientSO applicationConfigPatientSO = new ApplicationConfigPatientSO();
        applicationConfigPatientSO.setName(name);
        applicationConfigPatientSO.setLogo(logo);

        boolean program = configuration.getBoolean(PROGRAM, false);
        applicationConfigPatientSO.setProgram(program);

        boolean activityDiary = configuration.getBoolean(ACTIVITY_DIARY, false);
        applicationConfigPatientSO.setActivityDiary(activityDiary);

        boolean moodDiary = configuration.getBoolean(MOOD_DIARY, false);
        applicationConfigPatientSO.setMoodDiary(moodDiary);

        boolean messages = configuration.getBoolean(MESSAGES, false);
        applicationConfigPatientSO.setMessages(messages);

        boolean questioning = configuration.getBoolean(QUESTIONING, false);
        applicationConfigPatientSO.setQuestioning(questioning);

        boolean videoConference = configuration.getBoolean(VIDEO_CONFERENCE, false);
        applicationConfigPatientSO.setVideoConference(videoConference);

        boolean therapist = configuration.getBoolean(THERAPIST, false);
        applicationConfigPatientSO.setTherapist(therapist);

        return applicationConfigPatientSO;
    }

}
