package org.imsglobal.caliper.test;

import org.imsglobal.caliper.entities.agent.Person;
import org.imsglobal.caliper.entities.session.Session;

public class SampleSessionEntities {
    /**
     * Sample Session start
     * @return Session
     */
    public static Session buildSessionStart(Person actor) {
        return Session.builder()
            .id("https://univ.instructure.com/session-123456789")
            .name("session-123456789")
            .actor(actor)
            .dateCreated(SampleTime.getDefaultDateCreated())
            .startedAtTime(SampleTime.getDefaultStartedAtTime())
            .build();
    }

    /**
     * Sample Session end
     * @return Session
     */
    public static Session buildSessionEnd(Person actor) {
        return Session.builder()
            .id("https://univ.instructure.com/session-123456789")
            .name("session-123456789")
            .actor(actor)
            .dateCreated(SampleTime.getDefaultDateCreated())
            .startedAtTime(SampleTime.getDefaultStartedAtTime())
            .endedAtTime(SampleTime.getDefaultEndedAtTime())
            .duration("PT3000S")
            .build();
    }
}
