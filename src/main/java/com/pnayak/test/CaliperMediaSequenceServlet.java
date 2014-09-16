package com.pnayak.test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.imsglobal.caliper.CaliperSensor;
import org.imsglobal.caliper.Options;
import org.imsglobal.caliper.entities.CaliperAgent;
import org.imsglobal.caliper.entities.CaliperDigitalResource;
import org.imsglobal.caliper.entities.LearningObjective;
import org.imsglobal.caliper.entities.SoftwareApplication;
import org.imsglobal.caliper.entities.assessment.Attempt;
import org.imsglobal.caliper.entities.assessment.CaliperAssessment;
import org.imsglobal.caliper.entities.assessment.CaliperAssessmentItem;
import org.imsglobal.caliper.entities.assignable.CaliperAssignableDigitalResource;
import org.imsglobal.caliper.entities.lis.LISCourseSection;
import org.imsglobal.caliper.entities.lis.LISOrganization;
import org.imsglobal.caliper.entities.lis.LISPerson;
import org.imsglobal.caliper.entities.media.CaliperVideoObject;
import org.imsglobal.caliper.entities.media.MediaLocation;
import org.imsglobal.caliper.entities.outcome.Result;
import org.imsglobal.caliper.entities.schemadotorg.WebPage;
import org.imsglobal.caliper.events.assessment.AssessmentEvent;
import org.imsglobal.caliper.events.assessment.AssessmentItemEvent;
import org.imsglobal.caliper.events.assignable.AssignableEvent;
import org.imsglobal.caliper.events.assignable.AssignableEvent.Action;
import org.imsglobal.caliper.events.media.MediaEvent;
import org.imsglobal.caliper.events.outcome.OutcomeEvent;
import org.imsglobal.caliper.events.reading.NavigationEvent;
import org.imsglobal.caliper.events.reading.ViewedEvent;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Weeks;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * Servlet implementation class CaliperReadingSequenceServlet
 */
public class CaliperMediaSequenceServlet extends HttpServlet {

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
	public CaliperMediaSequenceServlet() {
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
		output.append("Caliper Event Generator: Generating Media interaction Sequence\n");
		output.append("=============================================================================\n");

		generateMediaSequence(output);

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

	private void generateMediaSequence(StringBuffer output) {

		// ===============================================================
		// ------------------ Media Interaction Sequence------------------
		// ===============================================================
		// Student in a course interacts with multi-media entities within
		// the course. The events and entities referenced here are defined in
		// the Caliper Media Metric Profile

		// For reference, the current time
		DateTime now = DateTime.now();

		// ----------------------------------------------------------------
		// Step 1: Set up (Learning) context elements
		// ----------------------------------------------------------------

		// LISCourseSection context. NOTE - we would want to associate it with a
		// parent Department or Institution at some point
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

		// edApp that provides the Media (likely an LTI based tool
		// provider)
		SoftwareApplication superMediaTool = new SoftwareApplication(
				"https://com.sat/super-media-tool");
		superMediaTool
				.setType("http://purl.imsglobal.org/ctx/caliper/v1/edApp/media");
		superMediaTool.setLastModifiedAt(now.minus(Weeks.weeks(8)).getMillis());

		// Student - performs interaction with media activities
		LISPerson alice = new LISPerson(
				"https://some-university.edu/students/jones-alice-554433");
		alice.setLastModifiedAt(now.minus(Weeks.weeks(3)).getMillis());

		output.append(">> generated learning context data\n");

		// -------------------------------------------------------------------------
		// Step 2: Set up activity context elements (i.e. Video provided by the
		// LTI tool). We also assign a single learning objective to the video
		// -------------------------------------------------------------------------

		LearningObjective comprehendVideoObjective = new LearningObjective(
				"http://blooms-sucks.com/lo1");

		CaliperVideoObject video = new CaliperVideoObject(
				"https://com.sat/super-media-tool/video/video1");
		video.setName("American Revolution - Key Figures Video");
		video.setDuration(1420);
		video.setAlignedLearningObjectives(Lists
				.newArrayList(comprehendVideoObjective));
		video.setParentRef(americanHistoryCourse);

		output.append(">> generated activity context data\n");

		// ----------------------------------------------------------------
		// Step 3: Populate Global App State for Event Generator
		// ----------------------------------------------------------------
		// Note - global app state - this is a simulation of the "state" of the
		// application that the Java sensor is installed into. In this case, it
		// is this Tomcat application. It is a utility data structure that is
		// used by subsequent event generation code and NOT part of the Caliper
		// standard
		HashMap<String, Object> globalAppState = Maps.newHashMap();
		globalAppState.put("canvas", canvasLMS);
		globalAppState.put("currentCourse", americanHistoryCourse);
		globalAppState.put("courseWebPage", courseWebPage);
		globalAppState.put("mediaEdApp", superMediaTool);
		globalAppState.put("video1", video);
		globalAppState.put("student", alice);

		output.append(">> populated Event Generator\'s global state\n");

		// ----------------------------------------------------------------
		// Step 4: Execute reading sequence
		// ----------------------------------------------------------------
		output.append(">> sending events\n");

		// Event # 1 - NavigationEvent
		navigateToVideo(globalAppState);
		output.append(">>>>>> Navigated to video in Canvas LMS edApp... sent NavigateEvent\n");

		// Event # 2 - ViewedEvent
		viewVideo(globalAppState);
		output.append(">>>>>> Viewed video in Super Media edApp... sent ViewedEvent\n");

		// Event # 3 - Start playing video
		startPlayingVideo(globalAppState);
		output.append(">>>>>> Started playing Video in Super Media edApp... sent AssignableEvent[started]\n");

		// Event # 4 - Pause playing video Event
		pausePlayingVideo(globalAppState);
		output.append(">>>>>> Paused playing video in Super Media edApp... sent AssessmentEvent[started]\n");

		// Event # 5 - Resume playing video Event
		resumePlayingVideo(globalAppState);
		output.append(">>>>>> Resumed playing video in Super Media edApp... sent AssessmentItemEvent[started]\n");

		// Event # 6 - Completed playing video Event
		completePlayingVideo(globalAppState);
		output.append(">>>>>> Completed playing video in Super Media edApp... sent AssessmentItemEvent[completed]\n");
	}

	// Methods below are utility methods for generating events... These are NOT
	// part of Caliper standards work and are here only as a utility in this
	// sample App

	private void navigateToVideo(HashMap<String, Object> globalAppState) {

		NavigationEvent navToVideoEvent = new NavigationEvent();

		// action is set in navEvent constructor... now set actor and object
		navToVideoEvent.setActor((LISPerson) globalAppState.get("student"));
		navToVideoEvent.setObject((CaliperVideoObject) globalAppState
				.get("video1"));
		navToVideoEvent.setFromResource((CaliperDigitalResource) globalAppState
				.get("courseWebPage"));

		// add (learning) context for event
		navToVideoEvent.setEdApp((SoftwareApplication) globalAppState
				.get("canvas"));
		navToVideoEvent.setLisOrganization((LISOrganization) globalAppState
				.get("currentCourse"));

		// set time and any event specific properties
		navToVideoEvent.setStartedAt(DateTime.now().getMillis());

		// Send event to EventStore
		CaliperSensor.send(navToVideoEvent);

	}

	private void viewVideo(HashMap<String, Object> globalAppState) {

		ViewedEvent viewVideoEvent = new ViewedEvent();

		// action is set in viewed event constructor... now set actor and object
		viewVideoEvent.setActor((LISPerson) globalAppState.get("student"));
		viewVideoEvent.setObject((CaliperVideoObject) globalAppState
				.get("video1"));

		// add (learning) context for event
		viewVideoEvent.setEdApp((SoftwareApplication) globalAppState
				.get("mediaEdApp"));
		viewVideoEvent.setLisOrganization((LISOrganization) globalAppState
				.get("currentCourse"));

		// set time and any event specific properties
		viewVideoEvent.setStartedAt(DateTime.now().getMillis());

		// Send event to EventStore
		CaliperSensor.send(viewVideoEvent);

	}

	private void startPlayingVideo(HashMap<String, Object> globalAppState) {

		MediaEvent startPlayingVideoEvent = MediaEvent
				.forAction(org.imsglobal.caliper.events.media.MediaEvent.Action.started);

		// action is set in media event constructor... now set actor and object
		startPlayingVideoEvent.setActor((LISPerson) globalAppState
				.get("student"));
		startPlayingVideoEvent.setObject((CaliperVideoObject) globalAppState
				.get("video1"));
		
		startPlayingVideoEvent.setMediaLocation(new MediaLocation(0));

		// add (learning) context for event
		startPlayingVideoEvent.setEdApp((SoftwareApplication) globalAppState
				.get("mediaEdApp"));
		startPlayingVideoEvent
				.setLisOrganization((LISOrganization) globalAppState
						.get("currentCourse"));

		// set time and any event specific properties
		startPlayingVideoEvent.setStartedAt(DateTime.now().getMillis());

		// Send event to EventStore
		CaliperSensor.send(startPlayingVideoEvent);

	}

	private void pausePlayingVideo(HashMap<String, Object> globalAppState) {

		MediaEvent pausePlayingVideoEvent = MediaEvent
				.forAction(org.imsglobal.caliper.events.media.MediaEvent.Action.paused);

		// action is set in media event constructor... now set actor and object
		pausePlayingVideoEvent.setActor((LISPerson) globalAppState
				.get("student"));
		pausePlayingVideoEvent.setObject((CaliperVideoObject) globalAppState
				.get("video1"));
		
		pausePlayingVideoEvent.setMediaLocation(new MediaLocation(42));

		// add (learning) context for event
		pausePlayingVideoEvent.setEdApp((SoftwareApplication) globalAppState
				.get("mediaEdApp"));
		pausePlayingVideoEvent
				.setLisOrganization((LISOrganization) globalAppState
						.get("currentCourse"));

		// set time and any event specific properties
		pausePlayingVideoEvent.setStartedAt(DateTime.now().getMillis());

		// Send event to EventStore
		CaliperSensor.send(pausePlayingVideoEvent);

	}

	private void resumePlayingVideo(HashMap<String, Object> globalAppState) {

		MediaEvent resumePlayingVideoEvent = MediaEvent
				.forAction(org.imsglobal.caliper.events.media.MediaEvent.Action.started);

		// action is set in media event constructor... now set actor and object
		resumePlayingVideoEvent.setActor((LISPerson) globalAppState
				.get("student"));
		resumePlayingVideoEvent.setObject((CaliperVideoObject) globalAppState
				.get("video1"));
		
		resumePlayingVideoEvent.setMediaLocation(new MediaLocation(42));

		// add (learning) context for event
		resumePlayingVideoEvent.setEdApp((SoftwareApplication) globalAppState
				.get("mediaEdApp"));
		resumePlayingVideoEvent
				.setLisOrganization((LISOrganization) globalAppState
						.get("currentCourse"));

		// set time and any event specific properties
		resumePlayingVideoEvent.setStartedAt(DateTime.now().getMillis());

		// Send event to EventStore
		CaliperSensor.send(resumePlayingVideoEvent);

	}

	private void completePlayingVideo(HashMap<String, Object> globalAppState) {

		MediaEvent completePlayingVideoEvent = MediaEvent
				.forAction(org.imsglobal.caliper.events.media.MediaEvent.Action.ended);

		// action is set in media event constructor... now set actor and object
		completePlayingVideoEvent.setActor((LISPerson) globalAppState
				.get("student"));
		completePlayingVideoEvent.setObject((CaliperVideoObject) globalAppState
				.get("video1"));
		
		completePlayingVideoEvent.setMediaLocation(new MediaLocation(1420));

		// add (learning) context for event
		completePlayingVideoEvent.setEdApp((SoftwareApplication) globalAppState
				.get("mediaEdApp"));
		completePlayingVideoEvent
				.setLisOrganization((LISOrganization) globalAppState
						.get("currentCourse"));

		// set time and any event specific properties
		completePlayingVideoEvent.setStartedAt(DateTime.now().getMillis());

		// Send event to EventStore
		CaliperSensor.send(completePlayingVideoEvent);

	}

	private void submitAssessment(HashMap<String, Object> globalAppState) {

		AssessmentEvent assessmentSubmitEvent = AssessmentEvent
				.forAction(org.imsglobal.caliper.events.assessment.AssessmentEvent.Action.submitted);

		// action is set in navEvent constructor... now set actor and object
		assessmentSubmitEvent.setActor((LISPerson) globalAppState
				.get("student"));
		// What is completed is the attempt itself (which was generated in
		// assessmentStartEvent
		// and in turn references the assignment via assignable)
		assessmentSubmitEvent.setObject((Attempt) globalAppState
				.get("assignment1attempt1"));

		// add (learning) context for event
		assessmentSubmitEvent.setEdApp((SoftwareApplication) globalAppState
				.get("assessmentEdApp"));
		assessmentSubmitEvent
				.setLisOrganization((LISOrganization) globalAppState
						.get("currentCourse"));

		// set time and any event specific properties
		assessmentSubmitEvent.setStartedAt(DateTime.now().getMillis());

		// Send event to EventStore
		CaliperSensor.send(assessmentSubmitEvent);

	}

	private void completeAssignment(HashMap<String, Object> globalAppState) {

		AssignableEvent assignableCompletedEvent = AssignableEvent
				.forAction(Action.completed);

		// action is set in navEvent constructor... now set actor and object
		assignableCompletedEvent.setActor((LISPerson) globalAppState
				.get("student"));
		assignableCompletedEvent.setObject((Attempt) globalAppState
				.get("assignment1"));

		// add (learning) context for event
		assignableCompletedEvent.setEdApp((SoftwareApplication) globalAppState
				.get("canvas"));
		assignableCompletedEvent
				.setLisOrganization((LISOrganization) globalAppState
						.get("currentCourse"));

		// set time and any event specific properties
		assignableCompletedEvent.setStartedAt(DateTime.now().getMillis());

		// Send event to EventStore
		CaliperSensor.send(assignableCompletedEvent);

	}

	private void autoGradeAssessmentSubmission(
			HashMap<String, Object> globalAppState) {

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

		// Send event to EventStore
		CaliperSensor.send(gradeAttemptEvent);

	}

	private int randomSecsDurationBetween(int start, int end) {
		return r.nextInt((end - start) + start);
	}
}
