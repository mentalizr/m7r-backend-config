package org.mentalizr.backend.config.instance;

import de.arthurpicht.utils.io.nio2.FileUtils;
import org.junit.jupiter.api.Test;
import org.mentalizr.serviceObjects.frontend.application.ApplicationConfigGenericSO;
import org.mentalizr.serviceObjects.frontend.patient.ApplicationConfigPatientSO;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class InstanceConfigurationFactoryTest {

    final Path resourcesDir = FileUtils.getWorkingDir().resolve("src/test/resources/");

    @Test
    void brandingConfigurationGeneric() {
        InstanceConfiguration instanceConfiguration
                = InstanceConfigurationFactory.createProjectConfigurationByPath(
                resourcesDir.resolve("m7r-application-foo.conf"));
        ApplicationConfigGenericSO applicationConfigGenericSO =
                instanceConfiguration.getApplicationConfigGenericSO();

        assertEquals("generic title", applicationConfigGenericSO.getTitle());
        assertEquals("generic_logo.png", applicationConfigGenericSO.getLogo());
        assertEquals("login", applicationConfigGenericSO.getDefaultLoginScreen());
    }

    @Test
    void getApplicationConfigSODefaultOnly() {
        InstanceConfiguration instanceConfiguration
                = InstanceConfigurationFactory.createProjectConfigurationByPath(
                resourcesDir.resolve("m7r-application-defaultOnly.conf"));

        ApplicationConfigPatientSO applicationConfigSO
                = instanceConfiguration.getApplicationConfigPatientSO("bar");

        assertEquals("dummy name", applicationConfigSO.getName());
        assertEquals("dummy_logo.png", applicationConfigSO.getLogo());
        assertTrue(applicationConfigSO.isProgram());
        assertTrue(applicationConfigSO.isActivityDiary());
        assertTrue(applicationConfigSO.isMoodDiary());
        assertTrue(applicationConfigSO.isMessages());
        assertTrue(applicationConfigSO.isQuestioning());
        assertTrue(applicationConfigSO.isVideoConference());
        assertTrue(applicationConfigSO.isTherapist());
    }

    @Test
    void getApplicationConfigSOFoo() {
        InstanceConfiguration instanceConfiguration
                = InstanceConfigurationFactory.createProjectConfigurationByPath(
                resourcesDir.resolve("m7r-application-foo.conf"));

        ApplicationConfigPatientSO applicationConfigPatientSO
                = instanceConfiguration.getApplicationConfigPatientSO("foo");

        assertEquals("program foo", applicationConfigPatientSO.getName());
        assertEquals("foo_logo.png", applicationConfigPatientSO.getLogo());
        assertFalse(applicationConfigPatientSO.isProgram());
        assertFalse(applicationConfigPatientSO.isActivityDiary());
        assertFalse(applicationConfigPatientSO.isMoodDiary());
        assertFalse(applicationConfigPatientSO.isMessages());
        assertFalse(applicationConfigPatientSO.isQuestioning());
        assertFalse(applicationConfigPatientSO.isVideoConference());
        assertFalse(applicationConfigPatientSO.isTherapist());

        applicationConfigPatientSO = instanceConfiguration.getApplicationConfigPatientSO("bar");

        assertEquals("dummy name", applicationConfigPatientSO.getName());
        assertEquals("dummy_logo.png", applicationConfigPatientSO.getLogo());
        assertTrue(applicationConfigPatientSO.isProgram());
        assertTrue(applicationConfigPatientSO.isActivityDiary());
        assertTrue(applicationConfigPatientSO.isMoodDiary());
        assertTrue(applicationConfigPatientSO.isMessages());
        assertTrue(applicationConfigPatientSO.isQuestioning());
        assertTrue(applicationConfigPatientSO.isVideoConference());
        assertTrue(applicationConfigPatientSO.isTherapist());
    }

    @Test
    void getApplicationConfigByAsteriskPattern() {
        InstanceConfiguration instanceConfiguration
                = InstanceConfigurationFactory.createProjectConfigurationByPath(
                resourcesDir.resolve("m7r-application-asterisk.conf"));

        ApplicationConfigPatientSO applicationConfigPatientSO
                = instanceConfiguration.getApplicationConfigPatientSO("bar-test");

        assertEquals("program bar", applicationConfigPatientSO.getName());
        assertEquals("bar_logo.png", applicationConfigPatientSO.getLogo());

        ApplicationConfigPatientSO applicationConfigPatientSOFoo
                = instanceConfiguration.getApplicationConfigPatientSO("foo");

        assertEquals("program foo", applicationConfigPatientSOFoo.getName());
        assertEquals("foo_logo.png", applicationConfigPatientSOFoo.getLogo());

        ApplicationConfigPatientSO applicationConfigPatientSOUnknown
                = instanceConfiguration.getApplicationConfigPatientSO("unknown");

        assertEquals("dummy name", applicationConfigPatientSOUnknown.getName());
        assertEquals("dummy_logo.png", applicationConfigPatientSOUnknown.getLogo());
    }

}