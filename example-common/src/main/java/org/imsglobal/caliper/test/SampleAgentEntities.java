package org.imsglobal.caliper.test;

import org.imsglobal.caliper.entities.agent.Person;
import org.imsglobal.caliper.entities.agent.SoftwareApplication;

public class SampleAgentEntities {

    /**
     * Sample Canvas LMS
     * @return canvas
     */
    public static final SoftwareApplication buildCanvas() {
        return SoftwareApplication.builder()
            .id("https://example.com/lms")
            .dateCreated(SampleTime.getDefaultDateCreated())
            .dateModified(SampleTime.getDefaultDateModified())
            .build();
    }

    public static final SoftwareApplication buildCourseSmartReader() {
        return SoftwareApplication.builder()
            .id("example.com/reader")
            .dateCreated(SampleTime.getDefaultDateCreated())
            .dateModified(SampleTime.getDefaultDateModified())
            .build();
    }

    /**
     * Sample media tool.
     * @return media tool.
     */
    public static final SoftwareApplication buildMediaTool() {
        return SoftwareApplication.builder()
            .id("https://example.com/super-media-tool")
            .dateCreated(SampleTime.getDefaultDateCreated())
            .dateModified(SampleTime.getDefaultDateModified())
            .build();
    }

    /**
     * Sample Readium ePub viewer.
     * @return readium viewer
     */
    public static final SoftwareApplication buildReadium() {
        return SoftwareApplication.builder()
            .id("https://example.com/viewer")
            .dateCreated(SampleTime.getDefaultDateCreated())
            .dateModified(SampleTime.getDefaultDateModified())
            .build();
    }

    /**
     * Sample Quiz engine
     * @return quiz engine
     */
    public static final SoftwareApplication buildQuizEngine() {
        return SoftwareApplication.builder()
            .id("https://example.com/quiz-engine-tool")
            .dateCreated(SampleTime.getDefaultDateCreated())
            .dateModified(SampleTime.getDefaultDateModified())
            .build();
    }

    /**
     * Sample student.
     * @return agent
     */
    public static final Person buildStudent554433() {
        return Person.builder()
            .id("https://example.edu/user/554433")
            .dateCreated(SampleTime.getDefaultDateCreated())
            .dateModified(SampleTime.getDefaultDateModified())
            .name("Student 1")
            .build();
    }
}
