package com.pnayak.test;

import org.imsglobal.caliper.CaliperSensor;
import org.imsglobal.caliper.Options;
import org.imsglobal.caliper.actions.*;
import org.imsglobal.caliper.entities.CaliperAgent;
import org.imsglobal.caliper.entities.CaliperDigitalResource;
import org.imsglobal.caliper.entities.LearningContext;
import org.imsglobal.caliper.entities.SoftwareApplication;
import org.imsglobal.caliper.entities.assessment.Attempt;
import org.imsglobal.caliper.entities.assessment.CaliperAssessment;
import org.imsglobal.caliper.entities.assessment.CaliperAssessmentItem;
import org.imsglobal.caliper.entities.assignable.CaliperAssignableDigitalResource;
import org.imsglobal.caliper.entities.lis.LISCourseSection;
import org.imsglobal.caliper.entities.lis.LISPerson;
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
        //
        // Invoke the Caliper sensor, send a set of Caliper Events
        //
        StringBuffer output = new StringBuffer();

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

        // ================================================================
        // ------------Assignable, Assessment, Outcome Sequence------------
        // ================================================================
        // Student in a course interacts with assignable entities within
        // the course. The assignable here is an Assessment.
        // In the process of interacting, she performs various assignable
        // and assessment related interactions. These are defined in
        // the Caliper Assignable, Assessment and Outcomes profiles respectively

        // For reference, the current time
        DateTime now = DateTime.now();

        // ----------------------------------------------------------------
        // Step 1: Set up (Learning) context elements
        // ----------------------------------------------------------------

        // LISCourseSection context. NOTE - we would want to associate it with a
        // parent Department or Institution at some point
        /**
        LISCourseSection americanHistoryCourse = new LISCourseSection(
                "https://some-university.edu/politicalScience/2014/american-revolution-101",
                null);
        americanHistoryCourse.setCourseNumber("AmRev-101");
        americanHistoryCourse.setLabel("American Revolution 101");
        americanHistoryCourse.setTitle("American Revolution 101");
        americanHistoryCourse.setSemester("Spring-2014");
        americanHistoryCourse.setLastModifiedAt(now.minus(Weeks.weeks(4))
                .getMillis());

        WebPage courseWebPage = new WebPage("AmRev-101-landingPage");
        courseWebPage.setName("American Revolution 101 Landing Page");
        courseWebPage.setParentRef(americanHistoryCourse);

        // edApp that provides the course (depicted as Canvas LMS)
        SoftwareApplication canvasLMS = new SoftwareApplication(
                "https://canvas.instructure.com");
        canvasLMS.setType("http://purl.imsglobal.org/ctx/caliper/v1/edApp/lms");
        canvasLMS.setLastModifiedAt(now.minus(Weeks.weeks(8)).getMillis());
        */

        // edApp that provides the assessment (likely an LTI based tool
        // provider)
        /**
        SoftwareApplication superAssessmentTool = new SoftwareApplication(
                "https://com.sat/super-assessment-tool");
        superAssessmentTool
                .setType("http://purl.imsglobal.org/ctx/caliper/v1/edApp/assessment");
        superAssessmentTool.setLastModifiedAt(now.minus(Weeks.weeks(8))
                .getMillis());

         // Student - performs interaction with reading activities
         LISPerson alice = new LISPerson(
         "https://some-university.edu/students/jones-alice-554433");
         alice.setLastModifiedAt(now.minus(Weeks.weeks(3)).getMillis());
        */

        // TODO LISCourseSection does not define a section property.  Oversight?
        // INFO: a default type value will be returned.
        LISCourseSection americanHistoryCourse = LISCourseSection.builder()
            .id("https://some-university.edu/politicalScience/2014/american-revolution-101")
            .semester("Spring-2014")
            .courseNumber("AmRev-101")
            .label("Am Rev 101")
            .title("American Revolution 101")
            .lastModifiedAt(now.minus(Weeks.weeks(4)).getMillis())
            .build();

        SoftwareApplication assessmentTool = SoftwareApplication.builder()
            .id("https://com.sat/super-assessment-tool")
            //.context("http://purl.imsglobal.org/ctx/caliper/v1/edApp/Assessment") // WARN: CaliperEntity prop
            .type("http://purl.imsglobal.org/caliper/v1/SoftwareApplication") // INFO: builder constructor will set this
            .lastModifiedAt(now.minus(Weeks.weeks(8)).getMillis())
            .build();

        LearningContext lmsContext = LearningContext.builder()
            .edApp(SoftwareApplication.builder()
                .id("https://canvas.instructure.com")
                //.context("http://purl.imsglobal.org/ctx/caliper/v1/edApp/lms") // WARN CaliperEntity prop
                .type("http://purl.imsglobal.org/caliper/v1/SoftwareApplication") // INFO: builder constructor will set this
                .lastModifiedAt(now.minus(Weeks.weeks(8)).getMillis())
                .build())
            .lisOrganization(americanHistoryCourse) // lisCourseSection?
            .agent(LISPerson.builder()
                .id("https://some-university.edu/students/jones-alice-554433")
                .lastModifiedAt(now.minus(Weeks.weeks(3)).getMillis())
                .build())
            .build();

        // The entity that automatically grades our assessment submissions
        // CaliperAgent superAssessmentToolGradingEngine = superAssessmentTool;
        CaliperAgent gradingEngine = assessmentTool;

        output.append(">> generated learning context data\n");

        // -------------------------------------------------------------------------
        // Step 2: Set up activity context elements (i.e. the Assignable
        // Assessment)
        // -------------------------------------------------------------------------

        /**
        CaliperAssessment assessment = new CaliperAssessment(
                "https://some-university.edu/politicalScience/2014/american-revolution-101/assessment1");
        assessment.setName("American Revolution - Key Figures Assessment");
        assessment.setDateCreated(now.minus(Weeks.weeks(1)).getMillis());
        assessment.setDatePublished(now.minus(Weeks.weeks(1)).getMillis());
        assessment.setDateToActivate(now.minus(Days.days(1)).getMillis());
        assessment.setDateToShow(now.minus(Days.days(1)).getMillis());
        assessment.setDateToSubmit(now.minus(Days.days(10)).getMillis());
        assessment.setMaxAttempts(2);
        assessment.setMaxSubmits(2);
        assessment.setMaxScore(5.0d);
        assessment.setParentRef(americanHistoryCourse);
        */

        CaliperAssessment assessment = CaliperAssessment.builder()
            .id("https://some-university.edu/politicalScience/2014/american-revolution-101/assessment1")
            //.type("http://purl.imsglobal.org/caliper/v1/Assessment") builder instantiates type value
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
            .build();

        /**
        CaliperAssessmentItem assessmentItem = new CaliperAssessmentItem(
                "https://some-university.edu/politicalScience/2014/american-revolution-101/assessment1/item1");
        assessmentItem.setName("Assessment Item 1");
        assessmentItem.setParentRef(assessment);
        */

        /**
         * Assignment activity context
         */
        AssignableProfile assignableProfile = AssignableProfile.builder()
            .learningContext(lmsContext)
            //.name()
            //.partOf()
            //.objectType()
            //.alignedLearningObjective()
            //.keyword()
            .assignable(assessment)
            //.target() // WARN: better as a CaliperEvent prop?
            //.generated() // WARN: better as a CaliperEvent prop?
            .build();

        /**
         * Quiz launch page activity context
         */
        ReadingProfile readingProfile = ReadingProfile.builder()
            .learningContext(lmsContext)
            .frame(assessment)
            .navigatedFrom(WebPage.builder()
                .id("AmRev-101-landingPage")
                .name("American Revolution 101 Landing Page")
                //.parentRef() WARN: currently no parentRef prop (use partOf)
                .partOf(americanHistoryCourse)
                .build())
            //.target() // WARN: better as a CaliperEvent prop?
            //.generated() // WARN: better as a CaliperEvent prop?
            .build();

        /**
         * Quiz activity context
         */

        List <CaliperAssessmentItem> assessmentItems = new ArrayList<CaliperAssessmentItem>();
        assessmentItems.add(CaliperAssessmentItem.builder()
                .id("https://some-university.edu/politicalScience/2014/american-revolution-101/assessment1/item1")
                .name("Assessment Item 1")
                .partOf("https://some-university.edu/politicalScience/2014/american-revolution-101/assessment1")
                .build());

        AssessmentProfile assessmentProfile = AssessmentProfile.builder()
            .learningContext(LearningContext.builder()
                    .edApp(assessmentTool)
                    .lisOrganization(americanHistoryCourse) // lisCourseSection?
                    .agent(LISPerson.builder()
                            .id("https://some-university.edu/students/jones-alice-554433")
                            .lastModifiedAt(now.minus(Weeks.weeks(3)).getMillis())
                            .build())
                    .build())
            .assessment(assessment)
            .assessmentItem(assessmentItems)
            //.target() // WARN: better as a CaliperEvent prop?
            //.generated() // WARN: better as a CaliperEvent prop or add attempt now?
            /**
            .generated(Attempt.builder()
                .id("https://some-university.edu/politicalScience/2014/american-revolution-101/assessment1/attempt1")
                .assignable((CaliperAssignableDigitalResource) assessment)
                .actor(profile.getLearningContext().getAgent())
                .count(1)
                .build())
            */
            .build();

        /**
         * Outcome activity context
         */
        OutcomeProfile outcomeProfile = OutcomeProfile.builder()
            .learningContext(LearningContext.builder()
                .edApp(assessmentTool)
                .lisOrganization(americanHistoryCourse) // lisCourseSection?
                .agent(gradingEngine)
                .build())
            //.name()
            //.partOf()
            //.objectType()
            //.alignedLearningObjective()
            //.keyword()
            .assignable(assessment)
            //.result() //WARN CAN'T PRE-POPULATE RESULT; BETTER AS A OutcomeEvent prop/
            //.target() // WARN: better as a CaliperEvent prop?
            //.generated() // WARN: better as a CaliperEvent prop or add attempt now?  Add attempt seems premature.
            .build();

        output.append(">> generated activity context data\n");

        // ----------------------------------------------------------------
        // Step 3: Populate Global App State for Event Generator
        // ----------------------------------------------------------------
        // Note - global app state - this is a simulation of the "state" of the
        // application that the Java sensor is installed into. In this case, it
        // is this Tomcat application. It is a utility data structure that is
        // used by subsequent event generation code and NOT part of the Caliper
        // standard
        // HashMap<String, Object> globalAppState = Maps.newHashMap();
        //globalAppState.put("canvas", canvasLMS);
        //globalAppState.put("currentCourse", americanHistoryCourse);
        // globalAppState.put("courseWebPage", courseWebPage);
        // globalAppState.put("assessmentEdApp", superAssessmentTool);
        // globalAppState.put("assessment1", assessment);
        // globalAppState.put("assessment1item1", assessmentItem);
        // globalAppState.put("gradingEngine", superAssessmentToolGradingEngine);
        // globalAppState.put("readiumReadingPage1", readiumReadingPage1);
        // globalAppState.put("readiumReadingPage2", readiumReadingPage2);
        // globalAppState.put("readiumReadingPage3", readiumReadingPage3);
        // globalAppState.put("coursesmartEdApp", courseSmart);
        // globalAppState.put("coursesmartReading", courseSmartReading);
        // globalAppState.put("coursesmartReadingPageaXfsadf12",
        // courseSmartReadingPageaXfsadf12);
        // globalAppState.put("student", alice);

        output.append(">> populated Event Generator\'s global state\n");

        // ----------------------------------------------------------------
        // Step 4: Execute reading sequence
        // ----------------------------------------------------------------
        output.append(">> sending events\n");

        // Event # 1 - NavigationEvent
        navigateToAssignable(readingProfile);
        //navigateToAssignable(globalAppState);
        output.append(">>>>>> Navigated to Assignable in Canvas LMS edApp... sent NavigateEvent\n");

        // Event # 2 - ViewedEvent
        viewAssignable(readingProfile);
        //viewAssignable(globalAppState);
        output.append(">>>>>> Viewed Assignable in Canvas LMS edApp... sent ViewedEvent\n");

        // Event # 3 - Start Assignable Event
        startAssignment(assignableProfile);
        //startAssignment(globalAppState);
        output.append(">>>>>> Started Assignable in Canvas LMS edApp... sent AssignableEvent[started]\n");

        // Event # 4 - Start Assessment Event
        startAssessment(assessmentProfile);
        //startAssessment(globalAppState);
        output.append(">>>>>> Started Assessment in Super Assessment edApp... sent AssessmentEvent[started]\n");

        // Event # 5 - Start AssessmentItem Event
        startAssessmentItem(assessmentProfile);
        //startAssessmentItem(globalAppState);
        output.append(">>>>>> Started AssessmentItem in Super Assessment edApp... sent AssessmentItemEvent[started]\n");

        // Event # 6 - Completed AssessmentItem Event
        completeAssessmentItem(assessmentProfile);
        //completeAssessmentItem(globalAppState);
        output.append(">>>>>> Completed AssessmentItem in Super Assessment edApp... sent AssessmentItemEvent[completed]\n");

        // Event # 7 - Submitted Assessment Event
        submitAssessment(assessmentProfile);
        //submitAssessment(globalAppState);
        output.append(">>>>>> Submitted Assessment in Super Assessment edApp... sent AssessmentEvent[submitted]\n");

        // WARN submit action removed from assignable
        // Event # 8 - Start Assignable Event
        //submitAssignment(assignableProfile);
        //submitAssignment(globalAppState);
        //output.append(">>>>>> Submitted Assignable in Canvas LMS edApp... sent AssignableEvent[submitted]\n");

        // Event # 9 - Outcome Event (grade)
        autoGradeAssessmentSubmission(outcomeProfile);
        //autoGradeAssessmentSubmission(globalAppState);
        output.append(">>>>>> Attempt auto-graded in Super Assessment edApp... sent OutcomeEvent[graded]\n");
    }

    // Methods below are utility methods for generating events... These are NOT
    // part of Caliper standards work and are here only as a utility in this
    // sample App

    private void startAssignment(AssignableProfile profile) {
    // private void startAssignment(HashMap<String, Object> globalAppState) {

        /**
        AssignableEvent assignableStartEvent = AssignableEvent
                .forAction(Action.started);

        Attempt attempt = new Attempt(
                "https://some-university.edu/politicalScience/2014/american-revolution-101/assessment1/attempt1");
        attempt.setActor((LISPerson) globalAppState.get("student"));
        attempt.setCount(1);
        attempt.setAssignable((CaliperAssignableDigitalResource) globalAppState
                .get("assessment1"));
        globalAppState.put("assignment1attempt1", attempt);
        */


        // action is set in navEvent constructor... now set actor and object
        /*
        assignableStartEvent
                .setActor((LISPerson) globalAppState.get("student"));
        assignableStartEvent
                .setObject((CaliperAssignableDigitalResource) globalAppState
                        .get("assessment1"));
        assignableStartEvent.setGenerated(attempt);

        // add (learning) context for event
        assignableStartEvent.setEdApp((SoftwareApplication) globalAppState
                .get("canvas"));

        // INFO: Event group property
        assignableStartEvent
                .setLisOrganization((LISOrganization) globalAppState
                        .get("currentCourse"));

        // set time and any event specific properties
        assignableStartEvent.setStartedAt(DateTime.now().getMillis());

        */

        /**
         * Type & context properties set by AssignableEvent constructor
         */
        AssignableEvent event = AssignableEvent.builder()
            .edApp(profile.getLearningContext().getEdApp())
            .lisOrganization(profile.getLearningContext().getLisOrganization())
            .actor(profile.getLearningContext().getAgent())
            .action(AssignableActions.STARTED.key())
            .object(profile.getAssignable())
            .startedAtTime(DateTime.now().getMillis())
            .build();

        // Send event to EventStore
        CaliperSensor.send(event);
    }

    private void startAssessment(AssessmentProfile profile) {
    // private void startAssessment(HashMap<String, Object> globalAppState) {

        /**
        AssessmentEvent assessmentStartEvent = AssessmentEvent
                .forAction(org.imsglobal.caliper.events.assessment.AssessmentEvent.Action.started);

        // action is set in navEvent constructor... now set actor and object
        assessmentStartEvent
                .setActor((LISPerson) globalAppState.get("student"));
        assessmentStartEvent
                .setObject((CaliperAssignableDigitalResource) globalAppState
                        .get("assessment1"));

        // add (learning) context for event
        assessmentStartEvent.setEdApp((SoftwareApplication) globalAppState
                .get("assessmentEdApp"));
        assessmentStartEvent
                .setLisOrganization((LISOrganization) globalAppState
                        .get("currentCourse"));

        // set time and any event specific properties
        assessmentStartEvent.setStartedAt(DateTime.now().getMillis());
        */

        /**
         * Type & context properties set by AssignableEvent constructor
         */
        AssessmentEvent event = AssessmentEvent.builder()
            .edApp(profile.getLearningContext().getEdApp())
            .lisOrganization(profile.getLearningContext().getLisOrganization())
            .actor(profile.getLearningContext().getAgent())
            .action(AssessmentActions.STARTED.key())
            .object((CaliperAssignableDigitalResource) profile.getAssessment())
            .startedAtTime(DateTime.now().getMillis())
            .build();

        // Send event to EventStore
        CaliperSensor.send(event);

        // TODO RETURN ATTEMPT OBJECT?
        /**
         Attempt attempt = Attempt.builder()
         .id("https://some-university.edu/politicalScience/2014/american-revolution-101/assessment1/attempt1")
         .assignable((CaliperAssignableDigitalResource) profile.getAssessment())
         .actor(profile.getLearningContext().getAgent())
         .count(1)
         .build())

        return attempt;
        */
    }

    private void startAssessmentItem(AssessmentProfile profile) {
    // private void startAssessmentItem(HashMap<String, Object> globalAppState) {

        /**
        AssessmentItemEvent assessmentItemStartEvent = AssessmentItemEvent
                .forAction(org.imsglobal.caliper.events.assessment.AssessmentItemEvent.Action.started);

        // action is set in navEvent constructor... now set actor and object
        assessmentItemStartEvent.setActor((LISPerson) globalAppState
                .get("student"));
        assessmentItemStartEvent
                .setObject((CaliperAssignableDigitalResource) globalAppState
                        .get("assessment1item1"));

        // add (learning) context for event
        assessmentItemStartEvent.setEdApp((SoftwareApplication) globalAppState
                .get("assessmentEdApp"));
        assessmentItemStartEvent
                .setLisOrganization((LISOrganization) globalAppState
                        .get("currentCourse"));

        // set time and any event specific properties
        assessmentItemStartEvent.setStartedAt(DateTime.now().getMillis());
        */

        /**
         * Type & context properties set by AssignableEvent constructor
         */
        AssessmentItemEvent event = AssessmentItemEvent.builder()
            .edApp(profile.getLearningContext().getEdApp())
            .lisOrganization(profile.getLearningContext().getLisOrganization())
            .actor(profile.getLearningContext().getAgent())
            .action(AssessmentItemActions.STARTED.key())
            .object((CaliperAssignableDigitalResource) profile.getAssessmentItem().get(0))
            .startedAtTime(DateTime.now().getMillis())
            .build();

        // Send event to EventStore
        CaliperSensor.send(event);
    }

    private void completeAssessmentItem(AssessmentProfile profile) {
    // private void completeAssessmentItem(HashMap<String, Object> globalAppState) {

        /**
        AssessmentItemEvent assessmentItemCompletedEvent = AssessmentItemEvent
                .forAction(org.imsglobal.caliper.events.assessment.AssessmentItemEvent.Action.completed);

        // action is set in navEvent constructor... now set actor and object
        assessmentItemCompletedEvent.setActor((LISPerson) globalAppState
                .get("student"));
        assessmentItemCompletedEvent
                .setObject((CaliperAssignableDigitalResource) globalAppState
                        .get("assessment1item1"));

        // add (learning) context for event
        assessmentItemCompletedEvent
                .setEdApp((SoftwareApplication) globalAppState
                        .get("assessmentEdApp"));
        assessmentItemCompletedEvent
                .setLisOrganization((LISOrganization) globalAppState
                        .get("currentCourse"));

        // set time and any event specific properties
        assessmentItemCompletedEvent.setStartedAt(DateTime.now().getMillis());
        */

        /**
         * Type & context properties set by AssignableEvent constructor
         */
        AssessmentItemEvent event = AssessmentItemEvent.builder()
            .edApp(profile.getLearningContext().getEdApp())
            .lisOrganization(profile.getLearningContext().getLisOrganization())
            .actor(profile.getLearningContext().getAgent())
            .action(AssessmentItemActions.COMPLETED.key())
            .object((CaliperAssignableDigitalResource) profile.getAssessmentItem().get(0))
            .startedAtTime(DateTime.now().getMillis())
            .build();

        // Send event to EventStore
        CaliperSensor.send(event);
    }

    private void submitAssessment(AssessmentProfile profile) {
    // private void submitAssessment(HashMap<String, Object> globalAppState) {

        /**
        AssessmentEvent assessmentStartEvent = AssessmentEvent
                .forAction(org.imsglobal.caliper.events.assessment.AssessmentEvent.Action.submitted);

        // action is set in navEvent constructor... now set actor and object
        assessmentStartEvent
                .setActor((LISPerson) globalAppState.get("student"));
        assessmentStartEvent
                .setObject((CaliperAssignableDigitalResource) globalAppState
                        .get("assessment1"));

        // add (learning) context for event
        assessmentStartEvent.setEdApp((SoftwareApplication) globalAppState
                .get("assessmentEdApp"));
        assessmentStartEvent
                .setLisOrganization((LISOrganization) globalAppState
                        .get("currentCourse"));

        // set time and any event specific properties
        assessmentStartEvent.setStartedAt(DateTime.now().getMillis());
        */

        /**
         * Type & context properties set by AssignableEvent constructor
         */
        AssessmentEvent event = AssessmentEvent.builder()
            .edApp(profile.getLearningContext().getEdApp())
            .lisOrganization(profile.getLearningContext().getLisOrganization())
            .actor(profile.getLearningContext().getAgent())
            .action(AssessmentActions.SUBMITTED.key())
            .object((CaliperAssignableDigitalResource) profile.getAssessment())
            .startedAtTime(DateTime.now().getMillis())
            .build();

        // Send event to EventStore
        CaliperSensor.send(event);
    }

    /**
    private void submitAssignment(AssignableProfile profile) {
    // private void submitAssignment(AssignableProfile profile, Attempt attempt) {

    // private void submitAssignment(HashMap<String, Object> globalAppState) {

        AssignableEvent assignableStartEvent = AssignableEvent.forAction(Action.submitted);

        // action is set in navEvent constructor... now set actor and object
        assignableStartEvent
                .setActor((LISPerson) globalAppState.get("student"));
        assignableStartEvent.setObject((Attempt) globalAppState
                .get("assignment1attempt1"));

        // add (learning) context for event
        assignableStartEvent.setEdApp((SoftwareApplication) globalAppState
                .get("canvas"));
        assignableStartEvent
                .setLisOrganization((LISOrganization) globalAppState
                        .get("currentCourse"));

        // set time and any event specific properties
        assignableStartEvent.setStartedAt(DateTime.now().getMillis());

        AssignableEvent event = AssignableEvent.builder()
            .edApp(profile.getLearningContext().getEdApp())
            .lisOrganization(profile.getLearningContext().getLisOrganization())
            .actor(profile.getLearningContext().getAgent())
            .action(AssignableActions.SUBMITTED.key())
            .object(profile.getAssignable())
            //.generated(attempt)
            .generated(Attempt.builder()
                .id("https://some-university.edu/politicalScience/2014/american-revolution-101/assessment1/attempt1")
                .assignable((CaliperAssignableDigitalResource) profile.getAssignable())
                .actor(profile.getLearningContext().getAgent())
                .count(1)
                .build())
            .startedAtTime(DateTime.now().getMillis())
            .build();

        // Send event to EventStore
        CaliperSensor.send(event);
    }
    */

    private void autoGradeAssessmentSubmission(OutcomeProfile profile) {
    // private void autoGradeAssessmentSubmission(OutcomeProfile profile, Attempt attempt) {
    // private void autoGradeAssessmentSubmission(HashMap<String, Object> globalAppState) {

        /**
        OutcomeEvent gradeAttemptEvent = OutcomeEvent
                .forAction(org.imsglobal.caliper.events.outcome.OutcomeEvent.Action.graded);

        // action is set in navEvent constructor... now set actor and object
        gradeAttemptEvent.setActor((CaliperAgent) globalAppState
                .get("gradingEngine"));
        gradeAttemptEvent.setObject((Attempt) globalAppState
                .get("assignment1attempt1"));

        Result result = new Result(
                "https://some-university.edu/politicalScience/2014/american-revolution-101/assessment1/attempt1/result");
        result.setTotalScore(4.2d);
        result.setNormalScore(4.2d);

        gradeAttemptEvent.setGenerated(result);

        // add (learning) context for event
        gradeAttemptEvent.setEdApp((SoftwareApplication) globalAppState
                .get("assessmentEdApp"));
        gradeAttemptEvent.setLisOrganization((LISOrganization) globalAppState
                .get("currentCourse"));

        // set time and any event specific properties
        gradeAttemptEvent.setStartedAt(DateTime.now().getMillis());
        */

        OutcomeEvent event = OutcomeEvent.builder()
            .edApp(profile.getLearningContext().getEdApp())
            .lisOrganization(profile.getLearningContext().getLisOrganization())
            .actor(profile.getLearningContext().getAgent())
            .action(OutcomeActions.GRADED.key())
            .object((Attempt) Attempt.builder()
                    .id("https://some-university.edu/politicalScience/2014/american-revolution-101/assessment1/attempt1")
                    .assignable((CaliperAssignableDigitalResource) profile.getAssignable())
                    .actor(profile.getLearningContext().getAgent())
                    .count(1)
                    .build())
            .generated((Result) Result.builder()
                    .id("https://some-university.edu/politicalScience/2014/american-revolution-101/assessment1/attempt1/result")
                    .totalScore(4.2d)
                    .normalScore(4.2d)
                    .build())
            .startedAtTime(DateTime.now().getMillis())
            .build();

        // Send event to EventStore
        CaliperSensor.send(event);
    }

    private void navigateToAssignable(ReadingProfile profile) {
    // private void navigateToAssignable(HashMap<String, Object> globalAppState) {

        /**
        NavigationEvent navTo   AssignableEvent = new NavigationEvent();

        // action is set in navEvent constructor... now set actor and object
        navToAssignableEvent
                .setActor((LISPerson) globalAppState.get("student"));
        navToAssignableEvent
                .setObject((CaliperAssignableDigitalResource) globalAppState
                        .get("assessment1"));
        navToAssignableEvent
                .setFromResource((CaliperDigitalResource) globalAppState
                        .get("courseWebPage"));

        // add (learning) context for event
        navToAssignableEvent.setEdApp((SoftwareApplication) globalAppState
                .get("canvas"));
        navToAssignableEvent
                .setLisOrganization((LISOrganization) globalAppState
                        .get("currentCourse"));

        // set time and any event specific properties
        navToAssignableEvent.setStartedAt(DateTime.now().getMillis());
        */

        /**
         * Type & context properties set by AssignableEvent constructor
         */
        NavigationEvent event = NavigationEvent.builder()
                .edApp(profile.getLearningContext().getEdApp())
                .lisOrganization(profile.getLearningContext().getLisOrganization())
                .actor(profile.getLearningContext().getAgent())
                .action(ReadingActions.NAVIGATEDTO.key())
                .object(profile.getFrame())
                .fromResource((CaliperDigitalResource) profile.getNavigatedFrom())
                .startedAtTime(DateTime.now().getMillis())
                .build();

        // Send event to EventStore
        CaliperSensor.send(event);
    }

    private void viewAssignable(ReadingProfile profile) {
    // private void viewAssignable(HashMap<String, Object> globalAppState) {
        /**
        ViewedEvent viewAssignableEvent = new ViewedEvent();

        // action is set in navEvent constructor... now set actor and object
        viewAssignableEvent.setActor((LISPerson) globalAppState.get("student"));
        viewAssignableEvent
                .setObject((CaliperAssignableDigitalResource) globalAppState
                        .get("assessment1"));

        // add (learning) context for event
        viewAssignableEvent.setEdApp((SoftwareApplication) globalAppState
                .get("canvas"));
        viewAssignableEvent.setLisOrganization((LISOrganization) globalAppState
                .get("currentCourse"));

        // set time and any event specific properties
        viewAssignableEvent.setStartedAt(DateTime.now().getMillis());
        int duration = randomSecsDurationBetween(5, 120);
        viewAssignableEvent.setDuration("PT" + duration + "S");
        */

        /**
         * Type & context properties set by AssignableEvent constructor
         */
        ViewedEvent event = ViewedEvent.builder()
            .edApp(profile.getLearningContext().getEdApp())
            .lisOrganization(profile.getLearningContext().getLisOrganization())
            .actor(profile.getLearningContext().getAgent())
            .action(ReadingActions.VIEWED.key())
            .object(profile.getFrame())
            .startedAtTime(DateTime.now().getMillis())
            .duration("PT" + randomSecsDurationBetween(5, 120) + "S")
            .build();

        // Send event to EventStore
        CaliperSensor.send(event);
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