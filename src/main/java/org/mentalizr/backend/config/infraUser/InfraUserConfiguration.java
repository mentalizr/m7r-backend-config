package org.mentalizr.backend.config.infraUser;

import de.arthurpicht.configuration.Configuration;
import de.arthurpicht.configuration.ConfigurationFactory;
import de.arthurpicht.configuration.ConfigurationFileNotFoundException;
import org.mentalizr.commons.paths.FileNames;
import org.mentalizr.commons.paths.M7rFile;
import org.mentalizr.commons.paths.host.hostDir.M7rInfraUserConfigFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Path;

public class InfraUserConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(InfraUserConfiguration.class);

    private static final String SECTION_NAME__USER_DB = "user-db";
    private static final String SECTION_NAME__DOCUMENT_DB = "document-db";
    private static final String SECTION_NAME__M7R = "m7r";

    private static final String PROPERTY__USER_DB_ROOT_PASSWORD = "db_root_password";
    private static final String PROPERTY__USER_DB_NAME = "db_name";
    private static final String PROPERTY__USER_DB_USER = "db_user";
    private static final String PROPERTY__USER_DB_PASSWORD = "db_password";
    private static final String PROPERTY__USER_DB_HOST = "db_host";

    private static final String PROPERTY__DOCUMENT_DB_ADMIN_NAME = "db_admin_user";
    private static final String PROPERTY__DOCUMENT_DB_ADMIN_PASSWORD = "db_admin_password";
    private static final String PROPERTY__DOCUMENT_DB_HOST = "db_host";
    private static final String PROPERTY__DOCUMENT_DB_NAME = "db_name";
    private static final String PROPERTY__DOCUMENT_DB_USER = "db_user";
    private static final String PROPERTY__DOCUMENT_DB_PASSWORD = "db_password";

    private static final String PROPERTY__M7R_ADMIN_USER = "admin_user";
    private static final String PROPERTY__M7R_ADMIN_PASSWORD = "admin_password";

    private final M7rFile m7rInfraUserConfigFile;
    private Configuration configurationUserDB;
    private Configuration configurationDocumentDB;
    private Configuration configurationM7r;

    public InfraUserConfiguration() {
        this.m7rInfraUserConfigFile = new M7rInfraUserConfigFile();
        load();
        doChecks();
    }

    private void load() {
        Path path = this.m7rInfraUserConfigFile.asPath();
        ConfigurationFactory configurationFactory = new ConfigurationFactory();
        try {
            configurationFactory.addConfigurationFileFromFilesystem(path.toFile());
            logger.info("Load configuration file [" + FileNames.M7R_INFRA_USER_CONF + "] from " +
                    "[" + this.m7rInfraUserConfigFile.toAbsolutePathString() + "].");
        } catch (ConfigurationFileNotFoundException | IOException e) {
            String message = "Configuration file [" + FileNames.M7R_INFRA_USER_CONF + "] " +
                    "not found: [" + path.toAbsolutePath() + "].";
            logger.error(message);
            throw new RuntimeException(message);
        }

        configurationUserDB = configurationFactory.getConfiguration(SECTION_NAME__USER_DB);
        configurationDocumentDB = configurationFactory.getConfiguration(SECTION_NAME__DOCUMENT_DB);
        configurationM7r = configurationFactory.getConfiguration(SECTION_NAME__M7R);
    }

    private void doChecks() {

        checkForKeyExistence(SECTION_NAME__USER_DB, PROPERTY__USER_DB_ROOT_PASSWORD);
        checkForKeyExistence(SECTION_NAME__USER_DB, PROPERTY__USER_DB_NAME);
        checkForKeyExistence(SECTION_NAME__USER_DB, PROPERTY__USER_DB_USER);
        checkForKeyExistence(SECTION_NAME__USER_DB, PROPERTY__USER_DB_PASSWORD);
        checkForKeyExistence(SECTION_NAME__USER_DB, PROPERTY__USER_DB_HOST);

        checkForKeyExistence(SECTION_NAME__DOCUMENT_DB, PROPERTY__DOCUMENT_DB_ADMIN_NAME);
        checkForKeyExistence(SECTION_NAME__DOCUMENT_DB, PROPERTY__DOCUMENT_DB_ADMIN_PASSWORD);
        checkForKeyExistence(SECTION_NAME__DOCUMENT_DB, PROPERTY__DOCUMENT_DB_HOST);
        checkForKeyExistence(SECTION_NAME__DOCUMENT_DB, PROPERTY__DOCUMENT_DB_NAME);
        checkForKeyExistence(SECTION_NAME__DOCUMENT_DB, PROPERTY__DOCUMENT_DB_USER);
        checkForKeyExistence(SECTION_NAME__DOCUMENT_DB, PROPERTY__DOCUMENT_DB_PASSWORD);
    }

    private void checkForKeyExistence(String sectionName, String key) {

        boolean exists = true;

        if (sectionName.equals(SECTION_NAME__USER_DB) && !this.configurationUserDB.containsKey(key)) {
            exists = false;
        } else if (sectionName.equals(SECTION_NAME__DOCUMENT_DB) && !this.configurationDocumentDB.containsKey(key)) {
            exists = false;
        }

        if (exists) return;

        String message =
                "Missing declaration in Configuration [" + this.m7rInfraUserConfigFile.getFileName() + "] " +
                        "for key [" + key + "] in section [" + SECTION_NAME__USER_DB + "].";
        logger.error(message);
        throw new RuntimeException(message);
    }

    public String getUserDbRootPassword() {
        return configurationUserDB.getString(PROPERTY__USER_DB_ROOT_PASSWORD);
    }

    public String getUserDbName() {
        return configurationUserDB.getString(PROPERTY__USER_DB_NAME);
    }

    public String getUserDbUser() {
        return configurationUserDB.getString(PROPERTY__USER_DB_USER);
    }

    public String getUserDbPassword() {
        return configurationUserDB.getString(PROPERTY__USER_DB_PASSWORD);
    }

    public String getUserDbHost() {
        return configurationUserDB.getString(PROPERTY__USER_DB_HOST);
    }

    public String getDocumentDbAdminName() {
        return configurationDocumentDB.getString(PROPERTY__DOCUMENT_DB_ADMIN_NAME);
    }

    public String getDocumentDbAdminPassword() {
        return configurationDocumentDB.getString(PROPERTY__DOCUMENT_DB_ADMIN_PASSWORD);
    }

    public String getDocumentDbHost() {
        return configurationDocumentDB.getString(PROPERTY__DOCUMENT_DB_HOST);
    }

    public String getDocumentDbName() {
        return configurationDocumentDB.getString(PROPERTY__DOCUMENT_DB_NAME);
    }

    public String getDocumentDbUser() {
        return configurationDocumentDB.getString(PROPERTY__DOCUMENT_DB_USER);
    }

    public String getDocumentDbPassword() {
        return configurationDocumentDB.getString(PROPERTY__DOCUMENT_DB_PASSWORD);
    }

    public String getM7rAdminUser() {
        return configurationM7r.getString(PROPERTY__M7R_ADMIN_USER);
    }

    public String getM7rAdminPassword() {
        return configurationM7r.getString(PROPERTY__M7R_ADMIN_PASSWORD);
    }

}
