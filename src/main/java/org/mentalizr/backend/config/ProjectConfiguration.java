package org.mentalizr.backend.config;

import de.arthurpicht.configuration.ConfigurationFactory;
import de.arthurpicht.configuration.ConfigurationFileNotFoundException;
import org.mentalizr.serviceObjects.frontend.application.ApplicationConfigSO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ProjectConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(ProjectConfiguration.class);

    private static final String M7R_CONFIG_FILE = "m7r-project.conf";

    private static final String NAME = "name";
    private static final String LOGO = "logo";
    private static final String PROGRAM = "program";
    private static final String ACTIVITY_DIARY = "activityDiary";
    private static final String MOOD_DIARY = "moodDiary";
    private static final String MESSAGES = "messages";
    private static final String QUESTIONING = "questioning";
    private static final String VIDEO_CONFERENCE = "videoConference";
    private static final String THERAPIST = "therapist";

    private static final String DEFAULT = "default";

    private Map<String, ApplicationConfigSO> projectConfigSOMap;

    public ProjectConfiguration() {
        logger.info("Start initializing application configuration. Bind [" + M7R_CONFIG_FILE + "] from classpath." );

        ConfigurationFactory configurationFactory = new ConfigurationFactory();
        bindConfigurationFromClasspath(configurationFactory);

        initCommon(configurationFactory);
    }

    public ProjectConfiguration(String configurationFilePath) {
        logger.info("Start initializing application configuration. Bind [" + configurationFilePath + "]." );

        ConfigurationFactory configurationFactory = new ConfigurationFactory();
        bindConfigurationFromFilesystem(configurationFactory, new File(configurationFilePath));

        initCommon(configurationFactory);
    }

    private void initCommon(ConfigurationFactory configurationFactory) {
        if (!configurationFactory.hasSection(DEFAULT))
            throw new RuntimeException("No section [default] found in configuration file [" + M7R_CONFIG_FILE + "].");

        projectConfigSOMap = new HashMap<>();
        for (String programId : configurationFactory.getSectionNames()) {
            de.arthurpicht.configuration.Configuration projectConfiguration
                    = configurationFactory.getConfiguration(programId);
            ApplicationConfigSO applicationConfigSO = getApplicationConfigSO(projectConfiguration);
            projectConfigSOMap.put(programId, applicationConfigSO);
        }
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

    private ApplicationConfigSO getApplicationConfigSO(de.arthurpicht.configuration.Configuration configuration) {
        String name = configuration.getString(NAME);
        String logo = configuration.getString(LOGO);
        ApplicationConfigSO applicationConfigSO = new ApplicationConfigSO(name, logo);

        boolean program = configuration.getBoolean(PROGRAM, false);
        applicationConfigSO.setProgram(program);

        boolean activityDiary = configuration.getBoolean(ACTIVITY_DIARY, false);
        applicationConfigSO.setActivityDiary(activityDiary);

        boolean moodDiary = configuration.getBoolean(MOOD_DIARY, false);
        applicationConfigSO.setMoodDiary(moodDiary);

        boolean messages = configuration.getBoolean(MESSAGES, false);
        applicationConfigSO.setMessages(messages);

        boolean questioning = configuration.getBoolean(QUESTIONING, false);
        applicationConfigSO.setQuestioning(questioning);

        boolean videoConference = configuration.getBoolean(VIDEO_CONFERENCE, false);
        applicationConfigSO.setVideoConference(videoConference);

        boolean therapist = configuration.getBoolean(THERAPIST, false);
        applicationConfigSO.setTherapist(therapist);

        return applicationConfigSO;
    }

    public ApplicationConfigSO getApplicationConfigSO(String projectName) {
        if (projectConfigSOMap.containsKey(projectName))
            return projectConfigSOMap.get(projectName);

        return projectConfigSOMap.get(DEFAULT);
    }

}
