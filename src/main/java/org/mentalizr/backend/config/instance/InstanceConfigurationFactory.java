package org.mentalizr.backend.config.instance;

import de.arthurpicht.configuration.Configuration;
import de.arthurpicht.configuration.ConfigurationFactory;
import de.arthurpicht.configuration.ConfigurationFileNotFoundException;
import org.mentalizr.commons.paths.FileNames;
import org.mentalizr.serviceObjects.frontend.application.ApplicationConfigGenericSO;
import org.mentalizr.serviceObjects.frontend.patient.ApplicationConfigPatientSO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public class InstanceConfigurationFactory {

    private static final Logger logger = LoggerFactory.getLogger(InstanceConfigurationFactory.class);

//    private static final String DEFAULT_M7R_CONFIG_FILE = "m7r-instance.conf";

    private static final String GENERIC_TITLE = "title";
    private static final String GENERIC_LOGO = "logo";
    private static final String GENERIC__DEFAULT_LOGIN_SCREEN = "defaultLoginScreen";
    private static final String GENERIC__DEFAULT_LOGIN_SCREEN__LOGIN = "login";
    private static final String GENERIC__DEFAULT_LOGIN_SCREEN__ACCESS_KEY = "accessKey";
    private static final String GENERIC_POLICY_VERSION = "policyVersion";
    private static final String NAME = "name";
    private static final String LOGO = "logo";
    private static final String PROGRAM = "program";
    private static final String ACTIVITY_DIARY = "activityDiary";
    private static final String MOOD_DIARY = "moodDiary";
    private static final String MESSAGES = "messages";
    private static final String QUESTIONING = "questioning";
    private static final String VIDEO_CONFERENCE = "videoConference";
    private static final String THERAPIST = "therapist";

    private final String m7rInstanceFileLabel;

    private final InstanceConfigurationMap instanceConfigurationMap;
    private final ApplicationConfigGenericSO applicationConfigGenericSO;

//    public static InstanceConfiguration createProjectConfigurationFromClasspath() {
//        InstanceConfigurationFactory instanceConfigurationFactory = new InstanceConfigurationFactory();
//        return new InstanceConfiguration(
//                instanceConfigurationFactory.getInstanceConfigurationMap(),
//                instanceConfigurationFactory.getApplicationConfigGenericSO()
//        );
//    }

    public static InstanceConfiguration createProjectConfigurationByPath(Path configurationFilePath) {
        InstanceConfigurationFactory instanceConfigurationFactory = new InstanceConfigurationFactory(configurationFilePath);
        return new InstanceConfiguration(
                instanceConfigurationFactory.getInstanceConfigurationMap(),
                instanceConfigurationFactory.applicationConfigGenericSO);
    }

//    private InstanceConfigurationFactory() {
//        logger.info("Start initializing m7r instance configuration. Bind [" + DEFAULT_M7R_CONFIG_FILE + "] from classpath." );
//        this.m7rInstanceFileLabel = DEFAULT_M7R_CONFIG_FILE + " (from classpath)";
//
//        ConfigurationFactory configurationFactory = new ConfigurationFactory();
//        bindConfigurationFromClasspath(configurationFactory);
//
//        this.instanceConfigurationMap = obtainApplicationConfigMap(configurationFactory);
//        this.applicationConfigGenericSO = obtainBrandingConfigurationGeneric(configurationFactory);
//    }


    private InstanceConfigurationFactory(Path configurationFilePath) {
        logger.info("Load configuration file [" + FileNames.M7R_INSTANCE_CONF + "] from [" + configurationFilePath + "]." );
        this.m7rInstanceFileLabel = configurationFilePath.toString();

        ConfigurationFactory configurationFactory = new ConfigurationFactory();
        bindConfigurationFromFilesystem(configurationFactory, configurationFilePath.toFile());

        this.instanceConfigurationMap = obtainApplicationConfigMap(configurationFactory);
        this.applicationConfigGenericSO = obtainBrandingConfigurationGeneric(configurationFactory);
    }

    public InstanceConfigurationMap getInstanceConfigurationMap() {
        return this.instanceConfigurationMap;
    }

    public ApplicationConfigGenericSO getApplicationConfigGenericSO() {
        return this.applicationConfigGenericSO;
    }

    private InstanceConfigurationMap obtainApplicationConfigMap(ConfigurationFactory configurationFactory) {
        if (!configurationFactory.hasSection(InstanceConfiguration.DEFAULT))
            throw new RuntimeException("No section [default] found in configuration file [" + this.m7rInstanceFileLabel + "].");

        InstanceConfigurationMap.Builder configurationMapBuilder = new InstanceConfigurationMap.Builder();
        for (String sectionName : configurationFactory.getSectionNames()) {
            if (sectionName.equalsIgnoreCase(InstanceConfiguration.GENERIC)) continue;
            Configuration projectConfiguration = configurationFactory.getConfiguration(sectionName);
            ApplicationConfigPatientSO applicationConfigSO = getApplicationConfigPatientSO(projectConfiguration);
            configurationMapBuilder.put(sectionName, applicationConfigSO);
        }

        return configurationMapBuilder.build();
    }

    private ApplicationConfigGenericSO obtainBrandingConfigurationGeneric(ConfigurationFactory configurationFactory) {
        if (!configurationFactory.hasSection(InstanceConfiguration.GENERIC))
            throw new RuntimeException("No section [generic] found in configuration file [" + this.m7rInstanceFileLabel + "].");

        Configuration configuration = configurationFactory.getConfiguration(InstanceConfiguration.GENERIC);

        if (!configuration.containsKey(GENERIC_TITLE))
            throw new RuntimeException("Configuration key [" + GENERIC_TITLE + "] missing in section " +
                    "[" + InstanceConfiguration.GENERIC + "] of configuration file [" + this.m7rInstanceFileLabel + "].");
        String title = configuration.getString(GENERIC_TITLE);

        if (!configuration.containsKey(GENERIC_LOGO))
            throw new RuntimeException("Configuration key [" + GENERIC_LOGO + "] missing in section " +
                    "[" + InstanceConfiguration.GENERIC + "] of configuration file [" + this.m7rInstanceFileLabel + "].");
        String logo = configuration.getString(GENERIC_LOGO);

        if (!configuration.containsKey(GENERIC__DEFAULT_LOGIN_SCREEN))
            throw new RuntimeException("Configuration key [" + GENERIC__DEFAULT_LOGIN_SCREEN + "] missing in section " +
                    "[" + InstanceConfiguration.GENERIC + "] of configuration file [" + this.m7rInstanceFileLabel + "].");
        String defaultLoginScreen = configuration.getString(GENERIC__DEFAULT_LOGIN_SCREEN);
        if (!defaultLoginScreen.equals(GENERIC__DEFAULT_LOGIN_SCREEN__LOGIN) && !defaultLoginScreen.equals(GENERIC__DEFAULT_LOGIN_SCREEN__ACCESS_KEY))
            throw new RuntimeException("Configuration key [" + GENERIC__DEFAULT_LOGIN_SCREEN + "] has illegal value " +
                    "[" + defaultLoginScreen + "]. Must be [" + GENERIC__DEFAULT_LOGIN_SCREEN__LOGIN + "] or " +
                    "[" + GENERIC__DEFAULT_LOGIN_SCREEN__ACCESS_KEY + "].");

        if (!configuration.containsKey(GENERIC_POLICY_VERSION))
            throw new RuntimeException("Configuration key [" + GENERIC_POLICY_VERSION + "] missing in section " +
                    "[" + InstanceConfiguration.GENERIC + "] of configuration file [" + this.m7rInstanceFileLabel + "].");
        String policyVersion = configuration.getString(GENERIC_POLICY_VERSION);

        return new ApplicationConfigGenericSO(title, logo, defaultLoginScreen, policyVersion);
    }

//    private void bindConfigurationFromClasspath(ConfigurationFactory configurationFactory) {
//        try {
//            configurationFactory.addConfigurationFileFromClasspath(DEFAULT_M7R_CONFIG_FILE);
//        } catch (ConfigurationFileNotFoundException | IOException e) {
//            String message = "Configuration file [" + DEFAULT_M7R_CONFIG_FILE + "] not found on classpath.";
//            throw new RuntimeException(message);
//        }
//    }

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
