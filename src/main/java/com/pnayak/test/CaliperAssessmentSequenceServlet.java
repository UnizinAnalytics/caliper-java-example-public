package com.pnayak.test;

import com.google.common.collect.Iterables;
import org.imsglobal.caliper.Sensor;
import org.imsglobal.caliper.Options;
import org.imsglobal.caliper.actions.AssessmentActions;
import org.imsglobal.caliper.actions.AssessmentItemActions;
import org.imsglobal.caliper.actions.AssignableActions;
import org.imsglobal.caliper.actions.OutcomeActions;
import org.imsglobal.caliper.entities.Agent;
import org.imsglobal.caliper.entities.DigitalResource;
import org.imsglobal.caliper.entities.LearningContext;
import org.imsglobal.caliper.entities.SoftwareApplication;
import org.imsglobal.caliper.entities.WebPage;
import org.imsglobal.caliper.entities.assessment.Assessment;
import org.imsglobal.caliper.entities.assessment.AssessmentItem;
import org.imsglobal.caliper.entities.assignable.Attempt;
import org.imsglobal.caliper.entities.assignable.AssignableDigitalResource;
import org.imsglobal.caliper.entities.lis.CourseSection;
import org.imsglobal.caliper.entities.lis.Person;
import org.imsglobal.caliper.entities.outcome.Outcome;
import org.imsglobal.caliper.entities.outcome.Result;
import org.imsglobal.caliper.events.AssessmentEvent;
import org.imsglobal.caliper.events.AssessmentItemEvent;
import org.imsglobal.caliper.events.AssignableEvent;
import org.imsglobal.caliper.events.NavigationEvent;
import org.imsglobal.caliper.events.OutcomeEvent;
import org.imsglobal.caliper.profiles.AssessmentProfile;
import org.imsglobal.caliper.profiles.AssignableProfile;
import org.imsglobal.caliper.profiles.OutcomeProfile;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Weeks;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Servlet implementation class CaliperAssessmentServlet
 */
public class CaliperAssessmentSequenceServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static String HOST = "http://localhost:1080/1.0/event/put";

    // RequestBin HOST for testing/recording
    // private static String HOST = "http://requestb.in/uc7mt9uct";

    private static String API_KEY = "FEFNtMyXRZqwAH4svMakTw";
    private Random r;
    StringBuffer output = new StringBuffer();

    // Initialize the sensor - this needs to be done only once
    private void initialize() {
        Options options = new Options();
        options.setHost(HOST);
        options.setApiKey(API_KEY);
        Sensor.initialize(options);

        r = new Random();
    }

    /**
     * Default constructor.
     */
    public CaliperAssessmentSequenceServlet() {
        initialize();
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
     *      response)
     */
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response) throws ServletException, IOException {

        // Invoke the Caliper sensor, send a set of Caliper Events
        //StringBuffer output = new StringBuffer();

        output.append("=============================================================================\n");
        output.append("Caliper Event Generator: Generating Assignable, Assessment, Outcome Sequence\n");
        output.append("=============================================================================\n");

        generateAAOSequence(output);

        output.append(Sensor.getStatistics().toString());

        response.getWriter().write(output.toString());

        //Clear the buffer
        output.setLength(0);
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
     *      response)
     */
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub
    }

    /**
     * -----------------------------------------------------------------
     * ------------Assignable, Assessment, Outcome Sequence-------------
     * -----------------------------------------------------------------
     * Student in a course interacts with assignable entities within
     * the course. The assignable here is an Assessment.
     * In the process of interacting, she performs various assignable
     * and assessment related interactions. These are defined in
     * the Caliper Assignable, Assessment and Outcomes profiles respectively
     *
     * @param output
     */
    private void generateAAOSequence(StringBuffer output) {

        // For reference, the current time
        DateTime now = DateTime.now();

        /*
         * -----------------------------------------------------------------
         * Step 01.  Set the learning and activity context for the two readings.
         * -----------------------------------------------------------------
         */

        // LMS learning context
        // TODO LISCourseSection does not define a section property.  Oversight?
        LearningContext lmsContext = LearningContext.builder()
            .edApp(SoftwareApplication.builder()
                .id("https://canvas.instructure.com")
                //.context("http://purl.imsglobal.org/ctx/caliper/v1/edApp/lms") // WARN CaliperEntity prop
                .lastModifiedAt(now.minus(Weeks.weeks(8)).getMillis())
                .build())
            .lisOrganization(CourseSection.builder()
                .id("https://some-university.edu/politicalScience/2014/american-revolution-101")
                .semester("Spring-2014")
                .courseNumber("AmRev-101")
                .label("Am Rev 101")
                .title("American Revolution 101")
                .lastModifiedAt(now.minus(Weeks.weeks(4)).getMillis())
                .build()) // lisCourseSection?
            .agent(Person.builder()
                .id("https://some-university.edu/students/jones-alice-554433")
                .lastModifiedAt(now.minus(Weeks.weeks(3)).getMillis())
                .build())
            .build();

        //Tool learning context
        LearningContext toolContext = LearningContext.builder()
            .edApp(SoftwareApplication.builder()
                .id("https://com.sat/super-assessment-tool")
                //.context("http://purl.imsglobal.org/ctx/caliper/v1/edApp/Assessment") // WARN: CaliperEntity prop
                .lastModifiedAt(now.minus(Weeks.weeks(8)).getMillis())
                .build())
            .lisOrganization(lmsContext.getLisOrganization())
            .agent(lmsContext.getAgent())
            .build();

        // Grading engine
        Agent gradingEngine = toolContext.getEdApp();

        output.append(">> Generated learning context data\n");

        /*
         * -----------------------------------------------------------------
         * Step 02.  Set up activity context elements (i.e. the Assignable Assessment)
         * -----------------------------------------------------------------
         */

        // Assessment items
        List <AssessmentItem> assessmentItems = new ArrayList<AssessmentItem>();
        assessmentItems.add(AssessmentItem.builder()
            .id("https://some-university.edu/politicalScience/2014/american-revolution-101/assessment1/item1")
            .name("Assessment Item 1")
            .partOf("https://some-university.edu/politicalScience/2014/american-revolution-101/assessment1")
            .build());
        assessmentItems.add(AssessmentItem.builder()
            .id("https://some-university.edu/politicalScience/2014/american-revolution-101/assessment1/item2")
            .name("Assessment Item 2")
            .partOf("https://some-university.edu/politicalScience/2014/american-revolution-101/assessment1")
            .build());
        assessmentItems.add(AssessmentItem.builder()
            .id("https://some-university.edu/politicalScience/2014/american-revolution-101/assessment1/item3")
            .name("Assessment Item 3")
            .partOf("https://some-university.edu/politicalScience/2014/american-revolution-101/assessment1")
            .build());

        // Assessment
        Assessment assessment = Assessment.builder()
            .id("https://some-university.edu/politicalScience/2014/american-revolution-101/assessment1")
            .name("American Revolution - Key Figures Assessment")
            .dateCreated(now.minus(Weeks.weeks(1)).getMillis())
            .datePublished(now.minus(Weeks.weeks(1)).getMillis())
            .dateToActivate(now.minus(Days.days(1)).getMillis())
            .dateToShow(now.minus(Days.days(1)).getMillis())
            .dateToSubmit(now.minus(Days.days(10)).getMillis())
            .maxAttempts(2)
            .maxSubmits(2)
            .maxScore(5) // WARN original value "5.0d"
            .partOf(lmsContext.getLisOrganization())
            .assessmentItems(assessmentItems)
            .build();

        // Assessment profile (Tool context)
        AssessmentProfile assessmentProfile = AssessmentProfile.builder()
            .learningContext(toolContext)
            .assessment(assessment)
            .build();

        output.append("Generated activity context data\n");

        /*
         * -----------------------------------------------------------------
         * Step 4: Execute assessment sequence
         * -----------------------------------------------------------------
         */

        output.append("Sending events  . . .\n\n");

        // EVENT # 01 - Generate Assignable profile (LMS context) triggered by Navigation Event

        AssignableProfile assignableProfile = AssignableProfile.builder()
            .learningContext(lmsContext)
            .assignable(assessment)
            .action(AssignableActions.NAVIGATED_TO.key())
            .fromResource(WebPage.builder()
                .id("AmRev-101-landingPage")
                .name("American Revolution 101 Landing Page")
                .partOf(lmsContext.getLisOrganization())
                .build())
            .target(assessment)
            .build();

        output.append("Navigated to Assignable in Canvas LMS edApp . . . sent NavigationEvent\n");

        // Process Event
        navigate(assignableProfile);

        output.append("Object : " + ((Assessment) assignableProfile.getAssignable()).getId() + "\n\n");

        // EVENT # 02 - Started Assignable Event
        assignableProfile.getActions().add(AssignableActions.STARTED.key());
        assignableProfile.getAttempts().add(Attempt.builder()
            .id(assignableProfile.getAssignable().getId() + "/attempt1")
            .assignable((AssignableDigitalResource) assignableProfile.getAssignable())
            .actor(assignableProfile.getLearningContext().getAgent())
            .count(1)
            .build());

        output.append("Started Assignable in Canvas LMS edApp . . . sent AssignableEvent.\n");

        // Process Event
        startAssignment(assignableProfile);

        output.append("Attempt : " + ((Attempt) Iterables.getFirst(assignableProfile.getAttempts(), 1)).getCount() + "\n");
        output.append("Object : " + ((Assessment) assignableProfile.getAssignable()).getId() + "\n\n");

        // EVENT # 03 - Started Assessment Event
        assessmentProfile.getActions().add(AssessmentActions.STARTED.key());
        output.append("Started Assessment in Super Assessment edApp . . . sent AssessmentEvent.\n");

        // Process Event
        assess(assessmentProfile, assignableProfile);

        output.append("Object : " + assessmentProfile.getAssessment().getId() + "\n\n");

        // EVENT # 04 - Started AssessmentItem 01
        assessmentProfile.getActions().add(AssessmentItemActions.STARTED.key());
        output.append("Started AssessmentItem 01 in Super Assessment edApp . . . sent AssessmentItemEvent.\n");

        // Process Event
        itemAssess(assessmentProfile, 0);

        output.append("Object : " + assessmentProfile.getAssessment().getAssessmentItems().get(0).getId() + "\n\n");

        // EVENT # 05 - Completed AssessmentItem 01
        assessmentProfile.getActions().add(AssessmentItemActions.COMPLETED.key());
        output.append("Completed AssessmentItem 01 in Super Assessment edApp . . . sent AssessmentItemEvent.\n");

        // Process Event
        itemAssess(assessmentProfile, 0);

        output.append("Object : " + assessmentProfile.getAssessment().getAssessmentItems().get(0).getId() + "\n\n");

        // EVENT # 06 - Started AssessmentItem 02
        assessmentProfile.getActions().add(AssessmentItemActions.STARTED.key());
        output.append("Started AssessmentItem 02 in Super Assessment edApp . . . sent AssessmentItemEvent.\n");

        // Process Event
        itemAssess(assessmentProfile, 1);

        output.append("Object : " + assessmentProfile.getAssessment().getAssessmentItems().get(1).getId() + "\n\n");

        // EVENT # 07 - Completed AssessmentItem 02
        assessmentProfile.getActions().add(AssessmentItemActions.COMPLETED.key());
        output.append("Completed AssessmentItem 02 in Super Assessment edApp . . . sent AssessmentItemEvent.\n");

        // Process Event
        itemAssess(assessmentProfile, 1);

        output.append("Object : " + assessmentProfile.getAssessment().getAssessmentItems().get(1).getId() + "\n\n");

        // EVENT # 08 - Started AssessmentItem 03
        assessmentProfile.getActions().add(AssessmentItemActions.STARTED.key());
        output.append("Started AssessmentItem 03 in Super Assessment edApp . . . sent AssessmentItemEvent.\n");

        // Process Event
        itemAssess(assessmentProfile, 2);

        output.append("Object : " + assessmentProfile.getAssessment().getAssessmentItems().get(2).getId() + "\n\n");

        // EVENT # 09 - Completed AssessmentItem 03
        assessmentProfile.getActions().add(AssessmentItemActions.COMPLETED.key());
        output.append("Completed AssessmentItem 03 in Super Assessment edApp . . . sent AssessmentItemEvent.\n");

        // Process Event
        itemAssess(assessmentProfile, 2);

        output.append("Object : " + assessmentProfile.getAssessment().getAssessmentItems().get(2).getId() + "\n\n");

        // EVENT # 10 - Submitted Assessment Event
        assessmentProfile.getActions().add(AssessmentActions.SUBMITTED.key());
        output.append("Submitted Assessment in Super Assessment edApp . . . sent AssessmentEvent.\n");

        // Process Event
        assess(assessmentProfile, assignableProfile);

        output.append("Object : " + assessmentProfile.getAssessment().getId() + "\n\n");

        // EVENT # 11 Generate OutcomeProfile triggered by Outcome Event (attempt, result pairing)
        Result result = Result.builder()
            .id("https://some-university.edu/politicalScience/2014/american-revolution-101/assessment1/attempt1/result")
            .totalScore(4.2d)
            .normalScore(4.2d)
            .build();

        OutcomeProfile outcomeProfile = OutcomeProfile.builder()
            .learningContext(toolContext)
            .assignable(assessment)
            .action(OutcomeActions.GRADED.key())
            .outcome(new Outcome(Iterables.getLast(assignableProfile.getAttempts()), result))
            .build();

        output.append("Submitted assessment auto-graded by Super Assessment edApp . . . sent OutcomeEvent.\n");

        // Process Event
        autoGrade(outcomeProfile);

        output.append("Object : " + Iterables.getLast(outcomeProfile.getOutcomes()).getAttempt().getId() + "\n");
        output.append("Attempt : " + Iterables.getLast(outcomeProfile.getOutcomes()).getAttempt().getCount() + "\n");
        output.append("Generated (Score) : " + Iterables.getLast(outcomeProfile.getOutcomes()).getResult().getTotalScore() + "\n\n");
        output.append("FINIS\n\n");
    }

    /*
      --------------------------------------------------------------------------------
      Methods below are utility methods for generating events. These are NOT
      part of Caliper standards work and are here only as a utility in this sample App.
      ---------------------------------------------------------------------------------
     */

    private void startAssignment(AssignableProfile profile) {

        AssignableEvent event = AssignableEvent.builder()
            .edApp(profile.getLearningContext().getEdApp())
            .lisOrganization(profile.getLearningContext().getLisOrganization())
            .actor(profile.getLearningContext().getAgent())
            .action(Iterables.getLast(profile.getActions()))
            .object((AssignableDigitalResource) profile.getAssignable())
            .generated((Attempt) Iterables.getLast(profile.getAttempts()))
            .startedAtTime(DateTime.now().getMillis())
            .build();

        // Send event to EventStore
        Sensor.send(event);

        // Output i18n action text
        output.append("Action : " + event.getAction() + "\n");
    }

    private void assess(AssessmentProfile assessment, AssignableProfile assignable) {

        /*
         * Include the latest attempt (generated)
         */
        AssessmentEvent event = AssessmentEvent.builder()
            .edApp(assessment.getLearningContext().getEdApp())
            .lisOrganization(assessment.getLearningContext().getLisOrganization())
            .actor(assessment.getLearningContext().getAgent())
            .action(Iterables.getLast(assessment.getActions()))
            .object((AssignableDigitalResource) assessment.getAssessment())
            .generated(Iterables.getLast(assignable.getAttempts()))
            .startedAtTime(DateTime.now().getMillis())
            .build();

        // Send event to EventStore
        Sensor.send(event);

        // Output i18n action text
        output.append("Action : " + event.getAction() + "\n");
    }

    private void itemAssess(AssessmentProfile profile, int index) {

        AssessmentItemEvent event = AssessmentItemEvent.builder()
            .edApp(profile.getLearningContext().getEdApp())
            .lisOrganization(profile.getLearningContext().getLisOrganization())
            .actor(profile.getLearningContext().getAgent())
            .action(Iterables.getLast(profile.getActions()))
            .object((AssignableDigitalResource) profile.getAssessment().getAssessmentItems().get(index))
            //.generated(RESPONSE) TODO: Do we need to capture the item response, if any?
            .startedAtTime(DateTime.now().getMillis())
            .build();

        // Send event to EventStore
        Sensor.send(event);

        // Output i18n action text
        output.append("Action : " + event.getAction() + "\n");
    }

    private void autoGrade(OutcomeProfile profile) {

        OutcomeEvent event = OutcomeEvent.builder()
            .edApp(profile.getLearningContext().getEdApp())
            .lisOrganization(profile.getLearningContext().getLisOrganization())
            .actor(profile.getLearningContext().getAgent())
            .action(Iterables.getLast(profile.getActions()))
            .object((Attempt) Iterables.getLast(profile.getOutcomes()).getAttempt())
            .generated((Result) Iterables.getLast(profile.getOutcomes()).getResult())
            .startedAtTime(DateTime.now().getMillis())
            .build();

        // Send event to EventStore
        Sensor.send(event);

        // Output i18n action text
        output.append("Action : " + event.getAction() + "\n");
    }

    private void navigate(AssignableProfile profile) {

        NavigationEvent event = NavigationEvent.builder()
            .edApp(profile.getLearningContext().getEdApp())
            .lisOrganization(profile.getLearningContext().getLisOrganization())
            .actor(profile.getLearningContext().getAgent())
            .action(Iterables.getLast(profile.getActions()))
            .object((AssignableDigitalResource) profile.getAssignable())
            .fromResource((DigitalResource) Iterables.getLast(profile.getFromResources()))
            .startedAtTime(DateTime.now().getMillis())
            .build();

        // Send event to EventStore
        Sensor.send(event);

        // Output i18n action text
        output.append("Action : " + event.getAction() + "\n");
    }

    private void pauseFor(int time) {

        try {
            Thread.sleep(time * 1000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private int randomSecsDurationBetween(int start, int end) {
        return r.nextInt((end - start) + start);
    }
}