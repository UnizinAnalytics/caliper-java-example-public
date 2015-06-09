package org.imsglobal.caliper.test;

import com.google.common.collect.ImmutableList;
import org.imsglobal.caliper.entities.agent.Person;
import org.imsglobal.caliper.entities.assessment.Assessment;
import org.imsglobal.caliper.entities.assessment.AssessmentItem;
import org.imsglobal.caliper.entities.assignable.AssignableDigitalResource;
import org.imsglobal.caliper.entities.assignable.Attempt;
import org.imsglobal.caliper.entities.response.Response;
import org.imsglobal.caliper.entities.response.SelectTextResponse;

public class SampleAssessmentEntities {

    /**
     * Sample Assessment.  Pass a slim version of the parent assessment to .assessmentItems(Assessment parent)
     * in order to avoid generating an infinite loop during instance construction.
     *
     * @return assessment
     */
    public static final Assessment buildAssessment() {
        return Assessment.builder()
            .id("https://example.edu/politicalScience/2014/american-revolution-101/assessment/001")
            .name("American Revolution - Key Figures Assessment")
            .dateCreated(SampleTime.getDefaultDateCreated())
            .datePublished(SampleTime.getDefaultDatePublished())
            .dateToActivate(SampleTime.getDefaultDateToActivate())
            .dateToShow(SampleTime.getDefaultDateToShow())
            .dateToSubmit(SampleTime.getDefaultDateToSubmit())
            .maxAttempts(2)
            .maxSubmits(2)
            .maxScore(5)
            .build();
    }

    /**
     * Sample assessment items
     * @return immutable list
     */
    public static final ImmutableList<AssessmentItem> buildAssessmentItems(Assessment parent) {

        return ImmutableList.<AssessmentItem>builder()
            .add(AssessmentItem.builder()
                .id("https://example.edu/politicalScience/2014/american-revolution-101/assessment/001/item/001")
                .name("Assessment Item 1")
                .isPartOf(parent)
                .build())
            .add(AssessmentItem.builder()
                .id("https://example.edu/politicalScience/2014/american-revolution-101/assessment/001//item/002")
                .name("Assessment Item 2")
                .isPartOf(parent)
                .build())
            .add(AssessmentItem.builder()
                .id("https://example.edu/politicalScience/2014/american-revolution-101/assessment/001/item/003")
                .name("Assessment Item 3")
                .isPartOf(parent)
                .build())
            .build();
    }

    public static final Attempt buildAttempt(Person actor, AssignableDigitalResource assignable) {
        return Attempt.builder()
            .id("https://example.edu/politicalScience/2014/american-revolution-101/attempt/001")
            .name("American Revolution - Key Figures Assessment")
            .dateCreated(SampleTime.getDefaultDateCreated())
            .startedAtTime(SampleTime.getDefaultStartedAtTime())
            .assignable(assignable)
            .actor(actor)
            .count(1)
            .build();
    }

    public static final Response buildResponse(Person actor, AssignableDigitalResource assignable, Attempt attempt) {
        return SelectTextResponse.builder()
            .id("https://example.edu/politicalScience/2014/american-revolution-101/response/789")
            .name("American Revolution - Key Figures Assessment")
            .dateCreated(SampleTime.getDefaultDateCreated())
            .startedAtTime(SampleTime.getDefaultStartedAtTime())
            .assignable(assignable)
            .actor(actor)
            .value("sdafgdfas")
            .attempt(attempt)
            .build();
    }
}
