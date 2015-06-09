package org.imsglobal.caliper.test;

import org.imsglobal.caliper.entities.LearningObjective;
import org.imsglobal.caliper.entities.media.VideoObject;

public class SampleMediaEntities {

    /**
     * Sample Video with learning objective.
     * @return video
     */
    public static final VideoObject buildVideoWithLearningObjective() {
        return VideoObject.builder()
            .id("https://example.com/super-media-tool/video/1225")
            .name("American Revolution - Key Figures Video")
            .learningObjective(LearningObjective.builder()
                .id("https://example.edu/american-revolution-101/personalities/learn")
                .build())
            .duration(1420)
            .build();
    }
}
