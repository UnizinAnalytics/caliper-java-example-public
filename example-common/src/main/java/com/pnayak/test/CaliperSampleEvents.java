package com.pnayak.test;

import org.imsglobal.caliper.entities.Agent;
import org.imsglobal.caliper.entities.LearningContext;
import org.imsglobal.caliper.entities.WebPage;
import org.imsglobal.caliper.entities.assessment.Assessment;
import org.imsglobal.caliper.entities.assessment.AssessmentItem;
import org.imsglobal.caliper.entities.assignable.Attempt;
import org.imsglobal.caliper.entities.outcome.Result;
import org.imsglobal.caliper.entities.reading.Frame;
import org.imsglobal.caliper.events.AssessmentEvent;
import org.imsglobal.caliper.events.AssessmentItemEvent;
import org.imsglobal.caliper.events.NavigationEvent;
import org.imsglobal.caliper.events.OutcomeEvent;
import org.imsglobal.caliper.profiles.AssessmentItemProfile;
import org.imsglobal.caliper.profiles.AssessmentProfile;
import org.joda.time.DateTime;


/**
 * Created by pgray on 12/26/14.
 */
public class CaliperSampleEvents {

    public static NavigationEvent generateNavigationEvent(LearningContext learningContext, Assessment assessment){
        return NavigationEvent.builder()
                .edApp(learningContext.getEdApp())
                .lisOrganization(learningContext.getLisOrganization())
                .actor(learningContext.getAgent())
                .object(assessment)
                .action(AssessmentProfile.Actions.NAVIGATED_TO.key())
                .fromResource(WebPage.builder()
                        .id("AmRev-101-landingPage")
                        .name("American Revolution 101 Landing Page")
                        .partOf(learningContext.getLisOrganization())
                        .build())
                .target(Frame.builder()
                        .id(assessment.getId())
                        .index(0)
                        .build())
                .startedAtTime(DateTime.now().getMillis())
                .build();
    }

    public static AssessmentEvent generateStartedAssessmentEvent(LearningContext learningContext, Assessment assessment){
        return AssessmentEvent.builder()
                .edApp(learningContext.getEdApp())
                .lisOrganization(learningContext.getLisOrganization())
                .actor(learningContext.getAgent())
                .object(assessment)
                .action(AssessmentProfile.Actions.NAVIGATED_TO.key())
                .generated(Attempt.builder()
                        .id(assessment.getId() + "/attempt1")
                        .assignable(assessment)
                        .actor(learningContext.getAgent())
                        .count(1) // First attempt
                        .build())
                .startedAtTime(DateTime.now().getMillis())
                .build();
    }

    public static AssessmentItemEvent generateStartedAssessmentItemEvent(LearningContext learningContext, Assessment assessment, AssessmentItem item){
        return AssessmentItemEvent.builder()
                .edApp(learningContext.getEdApp())
                .lisOrganization(learningContext.getLisOrganization())
                .actor(learningContext.getAgent())
                .object(item)
                .action(AssessmentItemProfile.AssessmentItemActions.STARTED.key())
                .startedAtTime(DateTime.now().getMillis())
                .build();
    }

    public static AssessmentItemEvent generateCompletedAssessmentItemEvent(LearningContext learningContext, Assessment assessment, AssessmentItem item){
        return AssessmentItemEvent.builder()
                .edApp(learningContext.getEdApp())
                .lisOrganization(learningContext.getLisOrganization())
                .actor(learningContext.getAgent())
                .object(item)
                .action(AssessmentItemProfile.AssessmentItemActions.COMPLETED.key())
                .startedAtTime(DateTime.now().getMillis())
                .build();
    }

    public static AssessmentEvent generateSubmittedAssessmentEvent(LearningContext learningContext, Assessment assessment){
        return AssessmentEvent.builder()
                .edApp(learningContext.getEdApp())
                .lisOrganization(learningContext.getLisOrganization())
                .actor(learningContext.getAgent())
                .object(assessment)
                .action(AssessmentProfile.AssessmentActions.SUBMITTED.key())
                .generated(Attempt.builder()
                        .id(assessment.getId() + "/attempt1")
                        .assignable(assessment)
                        .actor(learningContext.getAgent())
                        .count(1) // First attempt
                        .build())
                .startedAtTime(DateTime.now().getMillis())
                .build();
    }

    public static OutcomeEvent generateOutcomeEvent(LearningContext learningContext, Assessment assessment) {
        return OutcomeEvent.builder()
                .edApp(learningContext.getEdApp())
                .lisOrganization(learningContext.getLisOrganization())
                .actor(learningContext.getAgent())
                .object(Attempt.builder()
                        .id(assessment.getId() + "/attempt1")
                        .assignable(assessment)
                        .actor(learningContext.getAgent())
                        .count(1) // First attempt
                        .build())
                .action(AssessmentProfile.AssessmentActions.SUBMITTED.key())
                .generated(Result.builder()
                        .id("https://some-university.edu/politicalScience/2014/american-revolution-101/activityContext1/attempt1/result")
                        .totalScore(4.2d)
                        .normalScore(4.2d)
                        .scoredBy(learningContext.getEdApp())
                        .build())
                .startedAtTime(DateTime.now().getMillis())
                .build();
    }

}
