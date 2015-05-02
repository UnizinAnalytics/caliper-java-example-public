package org.imsglobal.caliper.test;


import org.imsglobal.caliper.events.Event;

/**
 * This interface encapsulates the logic needed to send an event, since
 * the webapp & test-cli differ in that regard.
 */
public interface EventSender {
    public void send(Event event);
}
