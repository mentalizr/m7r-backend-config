package org.mentalizr.backend.config;

import org.junit.jupiter.api.Test;
import org.mentalizr.serviceObjects.frontend.application.ApplicationConfigGenericSO;
import org.mentalizr.serviceObjects.frontend.patient.ApplicationConfigPatientSO;

import static org.junit.jupiter.api.Assertions.*;

class BrandingConfigurationFactoryTest {

    String resourcesDir = "src/test/resources/";

    @Test
    void brandingConfigurationGeneric() {
        BrandingConfiguration brandingConfiguration
                = BrandingConfigurationFactory.createProjectConfiguration(
                resourcesDir + "m7r-application-foo.conf");

        ApplicationConfigGenericSO applicationConfigGenericSO =
                brandingConfiguration.getApplicationConfigGenericSO();

        assertEquals("generic title", applicationConfigGenericSO.getTitle());
        assertEquals("generic_logo.png", applicationConfigGenericSO.getLogo());
    }

    @Test
    void getApplicationConfigSODefaultOnly() {
        BrandingConfiguration brandingConfiguration
                = BrandingConfigurationFactory.createProjectConfiguration(
                resourcesDir + "m7r-application-defaultOnly.conf");

        ApplicationConfigPatientSO applicationConfigSO
                = brandingConfiguration.getApplicationConfigPatientSO("bar");

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
        BrandingConfiguration brandingConfiguration
                = BrandingConfigurationFactory.createProjectConfiguration(
                resourcesDir + "m7r-application-foo.conf");

        ApplicationConfigPatientSO applicationConfigPatientSO
                = brandingConfiguration.getApplicationConfigPatientSO("foo");

        assertEquals("program foo", applicationConfigPatientSO.getName());
        assertEquals("foo_logo.png", applicationConfigPatientSO.getLogo());
        assertFalse(applicationConfigPatientSO.isProgram());
        assertFalse(applicationConfigPatientSO.isActivityDiary());
        assertFalse(applicationConfigPatientSO.isMoodDiary());
        assertFalse(applicationConfigPatientSO.isMessages());
        assertFalse(applicationConfigPatientSO.isQuestioning());
        assertFalse(applicationConfigPatientSO.isVideoConference());
        assertFalse(applicationConfigPatientSO.isTherapist());

        applicationConfigPatientSO = brandingConfiguration.getApplicationConfigPatientSO("bar");

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
        BrandingConfiguration brandingConfiguration
                = BrandingConfigurationFactory.createProjectConfiguration(
                resourcesDir + "m7r-application-asterisk.conf");

        ApplicationConfigPatientSO applicationConfigPatientSO
                = brandingConfiguration.getApplicationConfigPatientSO("bar-test");

        assertEquals("program bar", applicationConfigPatientSO.getName());
        assertEquals("bar_logo.png", applicationConfigPatientSO.getLogo());

        ApplicationConfigPatientSO applicationConfigPatientSOFoo
                = brandingConfiguration.getApplicationConfigPatientSO("foo");

        assertEquals("program foo", applicationConfigPatientSOFoo.getName());
        assertEquals("foo_logo.png", applicationConfigPatientSOFoo.getLogo());

        ApplicationConfigPatientSO applicationConfigPatientSOUnknown
                = brandingConfiguration.getApplicationConfigPatientSO("unknown");

        assertEquals("dummy name", applicationConfigPatientSOUnknown.getName());
        assertEquals("dummy_logo.png", applicationConfigPatientSOUnknown.getLogo());
    }

}