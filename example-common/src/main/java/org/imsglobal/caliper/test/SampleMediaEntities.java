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
            .id("https://com.sat/super-media-tool/video/video1")
            .name("American Revolution - Key Figures Video")
            .learningObjective(LearningObjective.builder()
                .id("http://americanrevolution.com/personalities/learn")
                .build())
            .duration(1420)
            .build();
    }
}
