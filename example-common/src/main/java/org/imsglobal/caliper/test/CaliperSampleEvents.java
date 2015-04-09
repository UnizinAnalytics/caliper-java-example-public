package org.imsglobal.caliper.test;

import org.imsglobal.caliper.actions.Action;
import org.imsglobal.caliper.entities.LearningContext;
import org.imsglobal.caliper.entities.reading.WebPage;
import org.imsglobal.caliper.entities.assessment.Assessment;
import org.imsglobal.caliper.entities.assessment.AssessmentItem;
import org.imsglobal.caliper.entities.assignable.Attempt;
import org.imsglobal.caliper.entities.outcome.Result;
import org.imsglobal.caliper.entities.reading.Frame;
import org.imsglobal.caliper.events.AssessmentEvent;
import org.imsglobal.caliper.events.AssessmentItemEvent;
import org.imsglobal.caliper.events.NavigationEvent;
import org.imsglobal.caliper.events.OutcomeEvent;

import static org.imsglobal.caliper.test.CaliperSampleAssets.*;

/**
 * Created by pgray on 12/26/14.
 */
public class CaliperSampleEvents {

    public static NavigationEvent generateNavigationEvent(LearningContext learningContext, Assessment assessment){
        return NavigationEvent.builder()
                .edApp(learningContext.getEdApp())
                .actor(learningContext.getAgent())
                .object(assessment)
                .action(Action.NAVIGATED_TO)
                .fromResource(WebPage.builder()
                        .id("AmRev-101-landingPage")
                        .name("American Revolution 101 Landing Page")
                        .build())
                .target(Frame.builder()
                        .id(assessment.getId())
                        .index(0)
                        .build())
                .startedAtTime(getDefaultStartedAtTime())
                .build();
    }

    public static AssessmentEvent generateStartedAssessmentEvent(LearningContext learningContext, Assessment assessment){
        return AssessmentEvent.builder()
                .edApp(learningContext.getEdApp())
                .actor(learningContext.getAgent())
                .object(assessment)
                .action(Action.STARTED)
                .generated(Attempt.builder()
                        .id(assessment.getId() + "/attempt1")
                        .assignableId(assessment.getId())
                        .actorId("actorId")
                        .count(1) // First attempt
                        .startedAtTime(getDefaultStartedAtTime())
                        .build())
                .startedAtTime(getDefaultStartedAtTime())
                .build();
    }

    public static AssessmentItemEvent generateStartedAssessmentItemEvent(LearningContext learningContext, Assessment assessment, AssessmentItem item){
        return AssessmentItemEvent.builder()
                .edApp(learningContext.getEdApp())
                .actor(learningContext.getAgent())
                .object(item)
                .generated(buildAttempt())
                .action(Action.STARTED)
                .startedAtTime(getDefaultStartedAtTime())
                .build();
    }

    public static AssessmentItemEvent generateCompletedAssessmentItemEvent(LearningContext learningContext, Assessment assessment, AssessmentItem item){
        return AssessmentItemEvent.builder()
                .edApp(learningContext.getEdApp())
                .actor(learningContext.getAgent())
                .object(item)
                .action(Action.COMPLETED)
                .startedAtTime(getDefaultStartedAtTime())
                .generated(buildResponse())
                .build();
    }

    public static AssessmentEvent generateSubmittedAssessmentEvent(LearningContext learningContext, Assessment assessment){
        return AssessmentEvent.builder()
                .edApp(learningContext.getEdApp())
                .actor(learningContext.getAgent())
                .object(assessment)
                .action(Action.SUBMITTED)
                .generated(Attempt.builder()
                        .id(assessment.getId() + "/attempt1")
                        .assignableId(assessment.getId())
                        .actorId("actorId")
                        .count(1) // First attempt
                        .startedAtTime(getDefaultStartedAtTime())
                        .build())
                .startedAtTime(getDefaultStartedAtTime())
                .build();
    }

    public static OutcomeEvent generateOutcomeEvent(LearningContext learningContext, Assessment assessment) {
        return OutcomeEvent.builder()
                .edApp(learningContext.getEdApp())
                .actor(learningContext.getAgent())
                .object(Attempt.builder()
                        .id(assessment.getId() + "/attempt1")
                        .assignableId(assessment.getId())
                        .actorId("actorId")
                        .count(1) // First attempt
                        .startedAtTime(getDefaultStartedAtTime())
                        .build())
                .action(Action.GRADED)
                .generated(Result.builder()
                        .id("https://some-university.edu/politicalScience/2014/american-revolution-101/activityContext1/attempt1/result")
                        .totalScore(4.2d)
                        .normalScore(4.2d)
                        .scoredBy(learningContext.getAgent())
                        .assignableId("assignableId")
                        .actorId("actorId")
                        .build())
                .startedAtTime(getDefaultStartedAtTime())
                .build();
    }

}
