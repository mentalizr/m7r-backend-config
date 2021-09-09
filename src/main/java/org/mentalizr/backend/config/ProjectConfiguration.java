package org.mentalizr.backend.config;

import de.arthurpicht.configuration.ConfigurationFactory;
import de.arthurpicht.configuration.ConfigurationFileNotFoundException;
import org.mentalizr.serviceObjects.frontend.patient.ApplicationConfigPatientSO;
import org.mentalizr.serviceObjects.frontend.therapist.ApplicationConfigTherapistSO;
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

    private Map<String, ApplicationConfigPatientSO> projectConfigSOMap;

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
            ApplicationConfigPatientSO applicationConfigSO = getApplicationConfigPatientSO(projectConfiguration);
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

    private ApplicationConfigPatientSO getApplicationConfigPatientSO(de.arthurpicht.configuration.Configuration configuration) {
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

    public ApplicationConfigPatientSO getApplicationConfigPatientSO(String projectName) {
        if (projectConfigSOMap.containsKey(projectName))
            return projectConfigSOMap.get(projectName);

        return projectConfigSOMap.get(DEFAULT);
    }

    public ApplicationConfigTherapistSO getApplicationConfigTherapistSO(String projectName) {
        ApplicationConfigPatientSO applicationConfigPatientSO = getApplicationConfigPatientSO(projectName);

        ApplicationConfigTherapistSO applicationConfigTherapistSO = new ApplicationConfigTherapistSO();
        applicationConfigTherapistSO.setName(applicationConfigPatientSO.getName());
        applicationConfigTherapistSO.setLogo(applicationConfigPatientSO.getLogo());

        return applicationConfigTherapistSO;
    }

}
