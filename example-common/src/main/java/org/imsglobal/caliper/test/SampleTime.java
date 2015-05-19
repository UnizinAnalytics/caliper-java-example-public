package org.imsglobal.caliper.test;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

public class SampleTime {

    /**
     * January 1, 2015, 06:00:00.000 GMT
     * @return return date created
     */
    public static DateTime getDefaultDateCreated() {
        return new DateTime(2015, 1, 1, 6, 0, 0, 0, DateTimeZone.UTC);
    }

    /**
     * February 2, 2015, 11:30:00.000 GMT
     * @return return date modified
     */
    public static DateTime getDefaultDateModified() {
        return new DateTime(2015, 2, 2, 11, 30, 0, 0, DateTimeZone.UTC);
    }

    /**
     * January 15, 2015, 09:30:00.000 GMT
     * @return return date published
     */
    public static DateTime getDefaultDatePublished(){
        return new DateTime(2015, 1, 15, 9, 30, 0, 0, DateTimeZone.UTC);
    }

    /**
     * January 16, 2015, 05:00:00.000 GMT
     * @return date to activate
     */
    public static DateTime getDefaultDateToActivate(){
        return new DateTime(2015, 1, 16, 5, 0, 0, 0, DateTimeZone.UTC);
    }

    /**
     * Same date as activate date
     * @return date to show
     */
    public static DateTime getDefaultDateToShow() {
        return getDefaultDateToActivate();
    }

    /**
     * Same date as activate date
     * @return date to start on
     */
    public static DateTime getDefaultDateToStartOn() {
        return getDefaultDateToActivate();
    }

    /**
     * February 28, 2015, 11:59:59.000 GMT
     * @return date to submit
     */
    public static DateTime getDefaultDateToSubmit() {
        return new DateTime(2015, 2, 28, 11, 59, 59, 0, DateTimeZone.UTC);
    }

    /**
     * February 15, 2015, 10:15:00.000 GMT
     * @return started at time
     */
    public static DateTime getDefaultStartedAtTime() {
        return new DateTime(2015, 2, 15, 10, 15, 0, 0, DateTimeZone.UTC);
    }

    /**
     * February 15, 2015, 11:05:00.000 GMT
     * @return ended at time
     */
    public static DateTime getDefaultEndedAtTime() {
        return new DateTime(2015, 2, 15, 11, 05, 0, 0, DateTimeZone.UTC);
    }
}
