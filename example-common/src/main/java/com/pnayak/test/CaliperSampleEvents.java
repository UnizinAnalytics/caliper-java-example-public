package com.pnayak.test;

import org.imsglobal.caliper.entities.foaf.Agent;
import org.imsglobal.caliper.entities.LearningContext;
import org.imsglobal.caliper.entities.WebPage;
import org.imsglobal.caliper.entities.assessment.Assessment;
import org.imsglobal.caliper.entities.assessment.AssessmentItem;
import org.imsglobal.caliper.entities.assignable.Attempt;
import org.imsglobal.caliper.entities.lis.Person;
import org.imsglobal.caliper.entities.outcome.Result;
import org.imsglobal.caliper.entities.reading.Frame;
import org.imsglobal.caliper.events.AssessmentEvent;
import org.imsglobal.caliper.events.AssessmentItemEvent;
import org.imsglobal.caliper.events.NavigationEvent;
import org.imsglobal.caliper.events.OutcomeEvent;
import org.imsglobal.caliper.profiles.AssessmentItemProfile;
import org.imsglobal.caliper.profiles.AssessmentProfile;
import org.imsglobal.caliper.profiles.NavigationProfile;
import org.imsglobal.caliper.profiles.OutcomeProfile;
import org.joda.time.DateTime;

import static com.pnayak.test.CaliperSampleAssets.getDefaultStartedAtTime;


/**
 * Created by pgray on 12/26/14.
 */
public class CaliperSampleEvents {

    public static NavigationEvent generateNavigationEvent(LearningContext learningContext, Assessment assessment){
        return NavigationEvent.builder()
                .edApp(learningContext.getEdApp())
                .lisOrganization(learningContext.getLisOrganization())
                .actor((Person) learningContext.getAgent())
                .object(assessment)
                .action(NavigationProfile.Actions.NAVIGATED_TO.key())
                .fromResource(WebPage.builder()
                        .id("AmRev-101-landingPage")
                        .name("American Revolution 101 Landing Page")
                        .isPartOf(learningContext.getLisOrganization())
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
                .lisOrganization(learningContext.getLisOrganization())
                .actor((Person) learningContext.getAgent())
                .object(assessment)
                .action(AssessmentProfile.Actions.STARTED.key())
                .generated(Attempt.builder()
                        .id(assessment.getId() + "/attempt1")
                        .assignable(assessment)
                        .actor((Person) learningContext.getAgent())
                        .count(1) // First attempt
                        .build())
                .startedAtTime(getDefaultStartedAtTime())
                .build();
    }

    public static AssessmentItemEvent generateStartedAssessmentItemEvent(LearningContext learningContext, Assessment assessment, AssessmentItem item){
        return AssessmentItemEvent.builder()
                .edApp(learningContext.getEdApp())
                .lisOrganization(learningContext.getLisOrganization())
                .actor((Person) learningContext.getAgent())
                .object(item)
                .action(AssessmentItemProfile.Actions.STARTED.key())
                .startedAtTime(getDefaultStartedAtTime())
                .build();
    }

    public static AssessmentItemEvent generateCompletedAssessmentItemEvent(LearningContext learningContext, Assessment assessment, AssessmentItem item){
        return AssessmentItemEvent.builder()
                .edApp(learningContext.getEdApp())
                .lisOrganization(learningContext.getLisOrganization())
                .actor((Person) learningContext.getAgent())
                .object(item)
                .action(AssessmentItemProfile.Actions.COMPLETED.key())
                .startedAtTime(getDefaultStartedAtTime())
                .build();
    }

    public static AssessmentEvent generateSubmittedAssessmentEvent(LearningContext learningContext, Assessment assessment){
        return AssessmentEvent.builder()
                .edApp(learningContext.getEdApp())
                .lisOrganization(learningContext.getLisOrganization())
                .actor((Person) learningContext.getAgent())
                .object(assessment)
                .action(AssessmentProfile.Actions.SUBMITTED.key())
                .generated(Attempt.builder()
                        .id(assessment.getId() + "/attempt1")
                        .assignable(assessment)
                        .actor((Person) learningContext.getAgent())
                        .count(1) // First attempt
                        .build())
                .startedAtTime(getDefaultStartedAtTime())
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
                        .actor((Person) learningContext.getAgent())
                        .count(1) // First attempt
                        .build())
                .action(OutcomeProfile.Actions.GRADED.key())
                .generated(Result.builder()
                        .id("https://some-university.edu/politicalScience/2014/american-revolution-101/activityContext1/attempt1/result")
                        .totalScore(4.2d)
                        .normalScore(4.2d)
                        .scoredBy(learningContext.getAgent())
                        .build())
                .startedAtTime(getDefaultStartedAtTime())
                .build();
    }

}
