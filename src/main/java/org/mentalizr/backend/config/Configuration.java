package org.mentalizr.backend.config;

import de.arthurpicht.configuration.ConfigurationFactory;
import de.arthurpicht.configuration.ConfigurationFileNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Configuration {

//    public enum DefaultLogin {ACCESS_KEY, LOGIN}

    private static final Logger logger = LoggerFactory.getLogger(Configuration.class);

    private static final String M7R_CONFIG_FILE = "m7r.conf";
    private static final String SYS_PROP = "m7r.config";

    private static final String SECTION_NAME__USER_DB = "user-db";
    private static final String SECTION_NAME__DOCUMENT_DB = "document-db";
    private static final String SECTION_NAME__DIR = "dir";
    private static final String SECTION_NAME__APPLICATION = "application";

    private static final String PROPERTY__USER_DB_NAME = "db_name";
    private static final String PROPERTY__USER_DB_USER = "db_user";
    private static final String PROPERTY__USER_DB_PASSWORD = "db_password";
    private static final String PROPERTY__USER_DB_HOST = "db_host";

    private static final String PROPERTY__DOCUMENT_DB_HOST = "db_host";
    private static final String PROPERTY__DOCUMENT_DB_NAME = "db_name";
    private static final String PROPERTY__DOCUMENT_DB_USER = "db_user";
    private static final String PROPERTY__DOCUMENT_DB_PASSWORD = "db_password";

    private static final String PROPERTY__DIR_PROGRAM_CONTENT_ROOT = "program_content_root";
    private static final String PROPERTY__DIR_IMAGE_ROOT = "image_root";

    private static final String PROPERTY__APPLICATION_DEFAULT_LOGIN = "default_login";

    private static de.arthurpicht.configuration.Configuration configurationUserDB;
    private static de.arthurpicht.configuration.Configuration configurationDocumentDB;
    private static de.arthurpicht.configuration.Configuration configurationDir;

    static {
        init();
        doChecks();
    }

    private static void init() {

        logger.info("Start initializing configuration.");

        ConfigurationFactory configurationFactory = new ConfigurationFactory();

        String m7rConfig = System.getProperty(SYS_PROP);

        if (m7rConfig != null && !m7rConfig.equals("")) {
            File configFile = new File(m7rConfig);
            bindConfigurationFromFilesystem(configurationFactory, configFile);
        } else {
            bindConfigurationFromClasspath(configurationFactory);
        }

        configurationUserDB = configurationFactory.getConfiguration(SECTION_NAME__USER_DB);
        configurationDocumentDB = configurationFactory.getConfiguration(SECTION_NAME__DOCUMENT_DB);
        configurationDir = configurationFactory.getConfiguration(SECTION_NAME__DIR);
    }

    private static void bindConfigurationFromFilesystem(ConfigurationFactory configurationFactory, File configFile) {
        try {
            configurationFactory.addConfigurationFileFromFilesystem(configFile);
            logger.info("Configuration file passed by System Property: " + configFile.getAbsolutePath());
        } catch (ConfigurationFileNotFoundException | IOException e) {
            String message = "Configuration file " + configFile.getAbsolutePath() + " not found. Passed by system property " + SYS_PROP + ".";
            logger.error(message);
            throw new RuntimeException(message);
        }
    }

    private static void bindConfigurationFromClasspath(ConfigurationFactory configurationFactory) {
        try {
            configurationFactory.addConfigurationFileFromClasspath(M7R_CONFIG_FILE);
            logger.info("Configuration file on classpath: " + M7R_CONFIG_FILE);
        } catch (ConfigurationFileNotFoundException | IOException e) {
            String message = "Configuration file " + M7R_CONFIG_FILE + " not found on classpath.";
            throw new RuntimeException(message);
        }
    }

    private static void doChecks() {

        checkForKeyExistance(SECTION_NAME__USER_DB, PROPERTY__USER_DB_NAME);
        checkForKeyExistance(SECTION_NAME__USER_DB, PROPERTY__USER_DB_USER);
        checkForKeyExistance(SECTION_NAME__USER_DB, PROPERTY__USER_DB_PASSWORD);
        checkForKeyExistance(SECTION_NAME__USER_DB, PROPERTY__USER_DB_HOST);

        checkForKeyExistance(SECTION_NAME__DOCUMENT_DB, PROPERTY__DOCUMENT_DB_HOST);
        checkForKeyExistance(SECTION_NAME__DOCUMENT_DB, PROPERTY__DOCUMENT_DB_NAME);
        checkForKeyExistance(SECTION_NAME__DOCUMENT_DB, PROPERTY__DOCUMENT_DB_USER);
        checkForKeyExistance(SECTION_NAME__DOCUMENT_DB, PROPERTY__DOCUMENT_DB_PASSWORD);

        checkForKeyExistance(SECTION_NAME__DIR, PROPERTY__DIR_PROGRAM_CONTENT_ROOT);
        checkForDirExistence(PROPERTY__DIR_PROGRAM_CONTENT_ROOT);
        checkForKeyExistance(SECTION_NAME__DIR, PROPERTY__DIR_IMAGE_ROOT);
        checkForDirExistence(PROPERTY__DIR_IMAGE_ROOT);

        checkForKeyExistance(SECTION_NAME__APPLICATION, PROPERTY__APPLICATION_DEFAULT_LOGIN);
    }

    private static void checkForKeyExistance(String sectionName, String key) {

        boolean exists = true;

        if (sectionName.equals(SECTION_NAME__USER_DB) && !configurationUserDB.containsKey(key)) {
            exists = false;
        } else if (sectionName.equals(SECTION_NAME__DOCUMENT_DB) && !configurationDocumentDB.containsKey(key)) {
            exists = false;
        } else if (sectionName.equals(SECTION_NAME__DIR) && !configurationDir.containsKey(key)) {
            exists = false;
        }

        if (exists) return;

        String message = "Missing declaration in Configuration [" + M7R_CONFIG_FILE + "] for key [" + key + "] in section [" + SECTION_NAME__USER_DB + "].";
        logger.error(message);
        throw new RuntimeException(message);
    }

    private static void checkForDirExistence(String key) {

        String dirName = configurationDir.getString(key);
        File file = new File(dirName);
        if (!file.exists() && !file.isDirectory()) {
            String message = "Configured directory (" + key + ") not existing: " + dirName;
            logger.error(message);
            throw new RuntimeException(message);
        }
    }

    public static String getUserDbName() {
        return configurationUserDB.getString(PROPERTY__USER_DB_NAME);
    }

    public static String getUserDbUser() {
        return configurationUserDB.getString(PROPERTY__USER_DB_USER);
    }

    public static String getUserDbPassword() {
        return configurationUserDB.getString(PROPERTY__USER_DB_PASSWORD);
    }

    public static String getUserDbHost() {
        return configurationUserDB.getString(PROPERTY__USER_DB_HOST);
    }

    public static String getDocumentDbHost() {
        return configurationDocumentDB.getString(PROPERTY__DOCUMENT_DB_HOST);
    }

    public static String getDocumentDbName() {
        return configurationDocumentDB.getString(PROPERTY__DOCUMENT_DB_NAME);
    }

    public static String getDocumentDbUser() {
        return configurationDocumentDB.getString(PROPERTY__DOCUMENT_DB_USER);
    }

    public static String getDocumentDbPassword() {
        return configurationDocumentDB.getString(PROPERTY__DOCUMENT_DB_PASSWORD);
    }

    public static Path getDirProgramContentRoot() {
        return Paths.get(configurationDir.getString(PROPERTY__DIR_PROGRAM_CONTENT_ROOT));
    }

    public static File getDirImageRoot() {
        return new File(configurationDir.getString(PROPERTY__DIR_IMAGE_ROOT));
    }

}
