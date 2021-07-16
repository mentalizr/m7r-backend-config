package org.mentalizr.backend.config;

import org.junit.jupiter.api.Test;
import org.mentalizr.serviceObjects.frontend.patient.ApplicationConfigPatientSO;

import static org.junit.jupiter.api.Assertions.*;

class ProjectConfigurationTest {

    String resourcesDir = "src/test/resources/";

    @Test
    void getApplicationConfigSODefaultOnly() {
        ProjectConfiguration projectConfiguration
                = new ProjectConfiguration(resourcesDir + "m7r-application-defaultOnly.conf");
        ApplicationConfigPatientSO applicationConfigSO = projectConfiguration.getApplicationConfigPatientSO("bar");

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
        ProjectConfiguration projectConfiguration
                = new ProjectConfiguration(resourcesDir + "m7r-application-foo.conf");

        ApplicationConfigPatientSO applicationConfigSO = projectConfiguration.getApplicationConfigPatientSO("foo");
        assertEquals("program foo", applicationConfigSO.getName());
        assertEquals("foo_logo.png", applicationConfigSO.getLogo());
        assertFalse(applicationConfigSO.isProgram());
        assertFalse(applicationConfigSO.isActivityDiary());
        assertFalse(applicationConfigSO.isMoodDiary());
        assertFalse(applicationConfigSO.isMessages());
        assertFalse(applicationConfigSO.isQuestioning());
        assertFalse(applicationConfigSO.isVideoConference());
        assertFalse(applicationConfigSO.isTherapist());

        applicationConfigSO = projectConfiguration.getApplicationConfigPatientSO("bar");
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

}