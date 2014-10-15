package com.pnayak.test;

import com.google.common.collect.Iterables;
import org.imsglobal.caliper.CaliperSensor;
import org.imsglobal.caliper.Options;
import org.imsglobal.caliper.actions.*;
import org.imsglobal.caliper.entities.CaliperAgent;
import org.imsglobal.caliper.entities.CaliperDigitalResource;
import org.imsglobal.caliper.entities.LearningContext;
import org.imsglobal.caliper.entities.SoftwareApplication;
import org.imsglobal.caliper.entities.assessment.CaliperAssessment;
import org.imsglobal.caliper.entities.assessment.CaliperAssessmentItem;
import org.imsglobal.caliper.entities.assignable.Attempt;
import org.imsglobal.caliper.entities.assignable.CaliperAssignableDigitalResource;
import org.imsglobal.caliper.entities.lis.LISCourseSection;
import org.imsglobal.caliper.entities.lis.LISPerson;
import org.imsglobal.caliper.entities.outcome.Outcome;
import org.imsglobal.caliper.entities.outcome.Result;
import org.imsglobal.caliper.entities.schemadotorg.WebPage;
import org.imsglobal.caliper.events.assessment.AssessmentEvent;
import org.imsglobal.caliper.events.assessment.AssessmentItemEvent;
import org.imsglobal.caliper.events.assignable.AssignableEvent;
import org.imsglobal.caliper.events.outcome.OutcomeEvent;
import org.imsglobal.caliper.events.reading.NavigationEvent;
import org.imsglobal.caliper.events.reading.ViewedEvent;
import org.imsglobal.caliper.profiles.AssessmentProfile;
import org.imsglobal.caliper.profiles.AssignableProfile;
import org.imsglobal.caliper.profiles.OutcomeProfile;
import org.imsglobal.caliper.profiles.ReadingProfile;
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

    private void initialize() {

        // Initialize the sensor - this needs to be done only once
        Options options = new Options();
        options.setHost(HOST);
        options.setApiKey(API_KEY);
        CaliperSensor.initialize(options);

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

        output.append(CaliperSensor.getStatistics().toString());

        response.getWriter().write(output.toString());
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
     *      response)
     */
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub
    }

    private void generateAAOSequence(StringBuffer output) {

        /* -----------------------------------------------------------------
         * ------------Assignable, Assessment, Outcome Sequence-------------
         * -----------------------------------------------------------------
         * Student in a course interacts with assignable entities within
         * the course. The assignable here is an Assessment.
         * In the process of interacting, she performs various assignable
         * and assessment related interactions. These are defined in
         * the Caliper Assignable, Assessment and Outcomes profiles respectively
         */

        // For reference, the current time
        DateTime now = DateTime.now();

        /*
         * -----------------------------------------------------------------
         * Step 01.  Set the activity context for the two readings.
         * -----------------------------------------------------------------
         */

        // TODO LISCourseSection does not define a section property.  Oversight?
        LISCourseSection americanHistoryCourse = LISCourseSection.builder()
            .id("https://some-university.edu/politicalScience/2014/american-revolution-101")
            .semester("Spring-2014")
            .courseNumber("AmRev-101")
            .label("Am Rev 101")
            .title("American Revolution 101")
            .lastModifiedAt(now.minus(Weeks.weeks(4)).getMillis())
            .build();

        LearningContext lmsContext = LearningContext.builder()
            .edApp(SoftwareApplication.builder()
                .id("https://canvas.instructure.com")
                //.context("http://purl.imsglobal.org/ctx/caliper/v1/edApp/lms") // WARN CaliperEntity prop
                .lastModifiedAt(now.minus(Weeks.weeks(8)).getMillis())
                .build())
            .lisOrganization(americanHistoryCourse) // lisCourseSection?
            .agent(LISPerson.builder()
                .id("https://some-university.edu/students/jones-alice-554433")
                .lastModifiedAt(now.minus(Weeks.weeks(3)).getMillis())
                .build())
            .build();

        // Assessment engine
        SoftwareApplication assessmentTool = SoftwareApplication.builder()
            .id("https://com.sat/super-assessment-tool")
            //.context("http://purl.imsglobal.org/ctx/caliper/v1/edApp/Assessment") // WARN: CaliperEntity prop
            .lastModifiedAt(now.minus(Weeks.weeks(8)).getMillis())
            .build();

        // Grading engine
        CaliperAgent gradingEngine = assessmentTool;

        output.append(">> generated learning context data\n");

        /*
         * -----------------------------------------------------------------
         * Step 02.  Set up activity context elements (i.e. the Assignable Assessment)
         * -----------------------------------------------------------------
         */

        // Assessment items
        List <CaliperAssessmentItem> assessmentItems = new ArrayList<CaliperAssessmentItem>();
        assessmentItems.add(CaliperAssessmentItem.builder()
            .id("https://some-university.edu/politicalScience/2014/american-revolution-101/assessment1/item1")
            .name("Assessment Item 1")
            .partOf("https://some-university.edu/politicalScience/2014/american-revolution-101/assessment1")
            .build());
        assessmentItems.add(CaliperAssessmentItem.builder()
            .id("https://some-university.edu/politicalScience/2014/american-revolution-101/assessment1/item2")
            .name("Assessment Item 2")
            .partOf("https://some-university.edu/politicalScience/2014/american-revolution-101/assessment1")
            .build());
        assessmentItems.add(CaliperAssessmentItem.builder()
            .id("https://some-university.edu/politicalScience/2014/american-revolution-101/assessment1/item3")
            .name("Assessment Item 3")
            .partOf("https://some-university.edu/politicalScience/2014/american-revolution-101/assessment1")
            .build());

        // Assessment
        CaliperAssessment assessment = CaliperAssessment.builder()
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
            .partOf(americanHistoryCourse)
            .assessmentItems(assessmentItems)
            .build();

        // Assignable profile (LMS context)
        AssignableProfile assignableProfile = AssignableProfile.builder()
            .learningContext(lmsContext)
            .assignable(assessment)
            //.target() // WARN: better as a CaliperEvent prop?
            //.generated() // WARN: better as a CaliperEvent prop?
            .build();

        // Navigation profile (LMS context)
        ReadingProfile readingProfile = ReadingProfile.builder()
            .learningContext(lmsContext)
            .frame(assessment)
            .navigatedFrom(WebPage.builder()
                .id("AmRev-101-landingPage")
                .name("American Revolution 101 Landing Page")
                .partOf(americanHistoryCourse)
                .build())
                //.target() // WARN: better as a CaliperEvent prop?
                //.generated() // WARN: better as a CaliperEvent prop?
            .build();

        // Assessment profile (Assessment tool context)
        AssessmentProfile assessmentProfile = AssessmentProfile.builder()
            .learningContext(LearningContext.builder()
                .edApp(assessmentTool)
                .lisOrganization(americanHistoryCourse)
                .agent(LISPerson.builder()
                    .id("https://some-university.edu/students/jones-alice-554433")
                    .lastModifiedAt(now.minus(Weeks.weeks(3)).getMillis())
                    .build())
                .build())
            .assessment(assessment)
            //.target() // WARN: better as a CaliperEvent prop?
            //.generated() // WARN: better as a CaliperEvent prop or add attempt now?
            .build();

        //Outcome profile (Assessment tool context)
        OutcomeProfile outcomeProfile = OutcomeProfile.builder()
            .learningContext(LearningContext.builder()
                .edApp(assessmentTool)
                .lisOrganization(americanHistoryCourse) // lisCourseSection?
                .agent(gradingEngine)
                .build())
            .assignable(assessment)
            //.result() //WARN CAN'T PRE-POPULATE RESULT; BETTER AS A OutcomeEvent prop/
            //.target() // WARN: better as a CaliperEvent prop?
            //.generated() // WARN: better as a CaliperEvent prop or add attempt now?  Add attempt seems premature.
            .build();

        output.append("Generated activity context data\n");

        /*
         * -----------------------------------------------------------------
         * Step 4: Execute assessment sequence
         * -----------------------------------------------------------------
         */

        output.append("Sending events  . . .\n\n");

        // EVENT # 01 - NavigationEvent
        readingProfile.getActions().add(ReadingActions.NAVIGATED_TO.key());
        output.append("Navigated to Assignable in Canvas LMS edApp . . . sent NavigationEvent\n");

        // Process Event
        navigate(readingProfile);

        output.append("Object : " + ((CaliperAssessment) Iterables.getLast(readingProfile.getFrames())).getId() + "\n\n");

        // EVENT # 02 - ViewedEvent
        readingProfile.getActions().add(ReadingActions.VIEWED.key());
        output.append("Viewed Assignable in Canvas LMS edApp . . . sent ViewedEvent\n");

        // Process Event
        view(readingProfile);

        output.append("Object : " + ((CaliperAssessment) Iterables.getLast(readingProfile.getFrames())).getId() + "\n\n");

        // EVENT # 03 - Started Assignable Event
        assignableProfile.getActions().add(AssignableActions.STARTED.key());
        assignableProfile.getAttempts().add(Attempt.builder()
            .id("https://some-university.edu/politicalScience/2014/american-revolution-101/assessment1/attempt1")
            .assignable((CaliperAssignableDigitalResource) assignableProfile.getAssignable())
            .actor(assignableProfile.getLearningContext().getAgent())
            .count(1)
            .build());

        output.append("Started Assignable in Canvas LMS edApp . . . sent AssignableEvent.\n");

        // Process Event
        startAssignment(assignableProfile);

        output.append("Attempt : " + ((Attempt) Iterables.getFirst(assignableProfile.getAttempts(), 1)).getCount() + "\n");
        output.append("Object : " + ((CaliperAssessment) Iterables.getLast(readingProfile.getFrames())).getId() + "\n\n");

        // EVENT # 04 - Started Assessment Event
        assessmentProfile.getActions().add(AssessmentActions.STARTED.key());
        output.append("Started Assessment in Super Assessment edApp . . . sent AssessmentEvent.\n");

        // Process Event
        startAssessment(assessmentProfile);

        output.append("Object : " + ((CaliperAssessment) Iterables.getLast(readingProfile.getFrames())).getId() + "\n\n");

        // EVENT # 05 - Started AssessmentItem 01
        assessmentProfile.getActions().add(AssessmentItemActions.STARTED.key());
        output.append("Started AssessmentItem 01 in Super Assessment edApp . . . sent AssessmentItemEvent.\n");

        // Process Event
        startAssessmentItem(assessmentProfile, 0);

        output.append("Object : " + ((CaliperAssessment) Iterables.getLast(readingProfile.getFrames()))
            .getAssessmentItems().get(0).getId() + "\n\n");

        // EVENT # 06 - Completed AssessmentItem 01
        assessmentProfile.getActions().add(AssessmentItemActions.COMPLETED.key());
        output.append("Completed AssessmentItem 01 in Super Assessment edApp . . . sent AssessmentItemEvent.\n");

        // Process Event
        completeAssessmentItem(assessmentProfile, 0);

        output.append("Object : " + ((CaliperAssessment) Iterables.getLast(readingProfile.getFrames()))
            .getAssessmentItems().get(0).getId() + "\n\n");

        // EVENT # 07 - Started AssessmentItem 02
        assessmentProfile.getActions().add(AssessmentItemActions.STARTED.key());
        output.append("Started AssessmentItem 02 in Super Assessment edApp . . . sent AssessmentItemEvent.\n");

        // Process Event
        startAssessmentItem(assessmentProfile, 1);

        output.append("Object : " + ((CaliperAssessment) Iterables.getLast(readingProfile.getFrames()))
            .getAssessmentItems().get(1).getId() + "\n\n");

        // EVENT # 08 - Completed AssessmentItem 02
        assessmentProfile.getActions().add(AssessmentItemActions.COMPLETED.key());
        output.append("Completed AssessmentItem 02 in Super Assessment edApp . . . sent AssessmentItemEvent.\n");

        // Process Event
        completeAssessmentItem(assessmentProfile, 1);

        output.append("Object : " + ((CaliperAssessment) Iterables.getLast(readingProfile.getFrames()))
            .getAssessmentItems().get(1).getId() + "\n\n");

        // EVENT # 09 - Started AssessmentItem 03
        assessmentProfile.getActions().add(AssessmentItemActions.STARTED.key());
        output.append("Started AssessmentItem 03 in Super Assessment edApp . . . sent AssessmentItemEvent.\n");

        // Process Event
        startAssessmentItem(assessmentProfile, 2);

        output.append("Object : " + ((CaliperAssessment) Iterables.getLast(readingProfile.getFrames()))
            .getAssessmentItems().get(2).getId() + "\n\n");

        // EVENT # 10 - Completed AssessmentItem 03
        assessmentProfile.getActions().add(AssessmentItemActions.COMPLETED.key());
        output.append("Completed AssessmentItem 03 in Super Assessment edApp . . . sent AssessmentItemEvent.\n");

        // Process Event
        completeAssessmentItem(assessmentProfile, 2);

        output.append("Object : " + ((CaliperAssessment) Iterables.getLast(readingProfile.getFrames()))
            .getAssessmentItems().get(2).getId() + "\n\n");

        // EVENT # 11 - Submitted Assessment Event
        assessmentProfile.getActions().add(AssessmentActions.SUBMITTED.key());
        output.append("Submitted Assessment in Super Assessment edApp . . . sent AssessmentEvent.\n");

        // Process Event
        submitAssessment(assessmentProfile);

        output.append("Object : " + ((CaliperAssessment) Iterables.getLast(readingProfile.getFrames())).getId() + "\n\n");


        // EVENT # 12 - Outcome Event (attempt, result pairing)
        outcomeProfile.getActions().add(OutcomeActions.GRADED.key());
        Outcome outcome = new Outcome();
        outcome.setAttempt(Iterables.getLast(assignableProfile.getAttempts()));
        outcome.setResult(Result.builder()
            .id("https://some-university.edu/politicalScience/2014/american-revolution-101/assessment1/attempt1/result")
            .totalScore(4.2d)
            .normalScore(4.2d)
            .build());
        outcomeProfile.getOutcomes().add(outcome);

        output.append("Submitted assessment auto-graded by Super Assessment edApp . . . sent OutcomeEvent.\n");

        // Process Event
        autoGradeAssessmentSubmission(outcomeProfile);

        output.append("Object : " + ((CaliperAssessment) Iterables.getLast(readingProfile.getFrames())).getId() + "\n");
        output.append("Attempt : " + Iterables.getLast(outcomeProfile.getOutcomes()).getAttempt().getCount() + "\n");
        output.append("Total Score : " + Iterables.getLast(outcomeProfile.getOutcomes()).getResult().getTotalScore() + "\n\n");
        output.append("FINIS\n\n");
    }

    /*
      --------------------------------------------------------------------------------
      Methods below are utility methods for generating events. These are NOT
      part of Caliper standards work and are here only as a utility in this sample App.
      ---------------------------------------------------------------------------------
     */

    private void startAssignment(AssignableProfile profile) {

        // TODO Assignable Event should include the attempt object?
        AssignableEvent event = AssignableEvent.builder()
            .edApp(profile.getLearningContext().getEdApp())
            .lisOrganization(profile.getLearningContext().getLisOrganization())
            .actor(profile.getLearningContext().getAgent())
            .action(Iterables.getLast(profile.getActions()))
            .object((CaliperAssignableDigitalResource) profile.getAssignable())
            .startedAtTime(DateTime.now().getMillis())
            .build();

        // Send event to EventStore
        CaliperSensor.send(event);

        // Output i18n action text
        output.append("Action : " + event.getAction() + "\n");
    }

    private void startAssessment(AssessmentProfile profile) {

        // TODO CaliperAssignableDigitalResource - include the attempt object?
        AssessmentEvent event = AssessmentEvent.builder()
            .edApp(profile.getLearningContext().getEdApp())
            .lisOrganization(profile.getLearningContext().getLisOrganization())
            .actor(profile.getLearningContext().getAgent())
            .action(Iterables.getLast(profile.getActions()))
            .object((CaliperAssignableDigitalResource) profile.getAssessment())
            .startedAtTime(DateTime.now().getMillis())
            .build();

        // Send event to EventStore
        CaliperSensor.send(event);

        // Output i18n action text
        output.append("Action : " + event.getAction() + "\n");
    }

    private void startAssessmentItem(AssessmentProfile profile, int index) {

        AssessmentItemEvent event = AssessmentItemEvent.builder()
            .edApp(profile.getLearningContext().getEdApp())
            .lisOrganization(profile.getLearningContext().getLisOrganization())
            .actor(profile.getLearningContext().getAgent())
            .action(Iterables.getLast(profile.getActions()))
            .object((CaliperAssignableDigitalResource) profile.getAssessment().getAssessmentItems().get(index))
            .startedAtTime(DateTime.now().getMillis())
            .build();

        // Send event to EventStore
        CaliperSensor.send(event);

        // Output i18n action text
        output.append("Action : " + event.getAction() + "\n");
    }

    private void completeAssessmentItem(AssessmentProfile profile, int index) {

        AssessmentItemEvent event = AssessmentItemEvent.builder()
            .edApp(profile.getLearningContext().getEdApp())
            .lisOrganization(profile.getLearningContext().getLisOrganization())
            .actor(profile.getLearningContext().getAgent())
            .action(Iterables.getLast(profile.getActions()))
            .object((CaliperAssignableDigitalResource) profile.getAssessment().getAssessmentItems().get(index))
            .startedAtTime(DateTime.now().getMillis())
            .build();

        // Send event to EventStore
        CaliperSensor.send(event);

        // Output i18n action text
        output.append("Action : " + event.getAction() + "\n");
    }

    private void submitAssessment(AssessmentProfile profile) {

        AssessmentEvent event = AssessmentEvent.builder()
            .edApp(profile.getLearningContext().getEdApp())
            .lisOrganization(profile.getLearningContext().getLisOrganization())
            .actor(profile.getLearningContext().getAgent())
            .action(Iterables.getLast(profile.getActions()))
            .object((CaliperAssignableDigitalResource) profile.getAssessment())
            .startedAtTime(DateTime.now().getMillis())
            .build();

        // Send event to EventStore
        CaliperSensor.send(event);

        // Output i18n action text
        output.append("Action : " + event.getAction() + "\n");
    }

    private void autoGradeAssessmentSubmission(OutcomeProfile profile) {

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
        CaliperSensor.send(event);

        // Output i18n action text
        output.append("Action : " + event.getAction() + "\n");
    }

    private void navigate(ReadingProfile profile) {

        NavigationEvent event = NavigationEvent.builder()
            .edApp(profile.getLearningContext().getEdApp())
            .lisOrganization(profile.getLearningContext().getLisOrganization())
            .actor(profile.getLearningContext().getAgent())
            .action(Iterables.getLast(profile.getActions()))
            .object((CaliperAssignableDigitalResource) Iterables.getLast(profile.getFrames()))
            .fromResource((CaliperDigitalResource) Iterables.getLast(profile.getNavigatedFroms()))
            .startedAtTime(DateTime.now().getMillis())
            .build();

        // Send event to EventStore
        CaliperSensor.send(event);

        // Output i18n action text
        output.append("Action : " + event.getAction() + "\n");
    }

    private void view(ReadingProfile profile) {

        ViewedEvent event = ViewedEvent.builder()
            .edApp(profile.getLearningContext().getEdApp())
            .lisOrganization(profile.getLearningContext().getLisOrganization())
            .actor(profile.getLearningContext().getAgent())
            .action(Iterables.getLast(profile.getActions()))
            .object((CaliperAssignableDigitalResource) Iterables.getLast(profile.getFrames()))
            .startedAtTime(DateTime.now().getMillis())
            .duration("PT" + randomSecsDurationBetween(5, 120) + "S")
            .build();

        // Send event to EventStore
        CaliperSensor.send(event);

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