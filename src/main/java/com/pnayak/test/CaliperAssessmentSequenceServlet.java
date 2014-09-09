package com.pnayak.test;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.imsglobal.caliper.CaliperSensor;
import org.imsglobal.caliper.Options;
import org.imsglobal.caliper.entities.CaliperDigitalResource;
import org.imsglobal.caliper.entities.SoftwareApplication;
import org.imsglobal.caliper.entities.annotation.BookmarkAnnotation;
import org.imsglobal.caliper.entities.annotation.HighlightAnnotation;
import org.imsglobal.caliper.entities.annotation.SharedAnnotation;
import org.imsglobal.caliper.entities.annotation.TagAnnotation;
import org.imsglobal.caliper.entities.assessment.CaliperAssessment;
import org.imsglobal.caliper.entities.assessment.CaliperAssessmentItem;
import org.imsglobal.caliper.entities.assignable.Attempt;
import org.imsglobal.caliper.entities.assignable.CaliperAssignableDigitalResource;
import org.imsglobal.caliper.entities.lis.LISCourseSection;
import org.imsglobal.caliper.entities.lis.LISOrganization;
import org.imsglobal.caliper.entities.lis.LISPerson;
import org.imsglobal.caliper.entities.schemadotorg.WebPage;
import org.imsglobal.caliper.events.annotation.AnnotationEvent;
import org.imsglobal.caliper.events.assessment.AssessmentEvent;
import org.imsglobal.caliper.events.assessment.AssessmentItemEvent;
import org.imsglobal.caliper.events.assignable.AssignableEvent;
import org.imsglobal.caliper.events.assignable.AssignableEvent.Action;
import org.imsglobal.caliper.events.reading.NavigationEvent;
import org.imsglobal.caliper.events.reading.ViewedEvent;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Weeks;

import com.google.common.collect.Maps;

/**
 * Servlet implementation class CaliperReadingSequenceServlet
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
		// and assessement related interactions. These are defined in
		// the Caliper Assignable, Assessment and Outcomes profiles respectively

		// For reference, the current time
		DateTime now = DateTime.now();

		// ----------------------------------------------------------------
		// Step 1: Set up contextual elements
		// ----------------------------------------------------------------

		// LISCourseSection context. NOTE - we would want to associate it with a
		// parent
		// Department or Institution at some point
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
		
		// edApp that provides the course
		SoftwareApplication canvasLMS = new SoftwareApplication(
				"https://canvas.instructure.com");
		canvasLMS
				.setType("http://purl.imsglobal.org/ctx/caliper/v1/edApp/lms");
		canvasLMS.setLastModifiedAt(now.minus(Weeks.weeks(8))
				.getMillis());

		// edApp that provides the assessment
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

		output.append(">> generated learning context data\n");

		// -------------------------------------------------------------------------
		// Step 2: Set up activity context elements (i.e. the Assignable
		// Assessment)
		// -------------------------------------------------------------------------
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
		assessment.setParentRef(americanHistoryCourse);

		CaliperAssessmentItem assessmentItem = new CaliperAssessmentItem(
				"https://some-university.edu/politicalScience/2014/american-revolution-101/assessment1/item1");
		assessmentItem.setName("Assessment Item 1");
		assessmentItem.setParentRef(assessment);

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
		globalAppState.put("assessmentEdApp", superAssessmentTool);
		globalAppState.put("assessment1", assessment);
		globalAppState.put("assessment1item1", assessmentItem);
		// globalAppState.put("readiumReadingPage1", readiumReadingPage1);
		// globalAppState.put("readiumReadingPage2", readiumReadingPage2);
		// globalAppState.put("readiumReadingPage3", readiumReadingPage3);
		// globalAppState.put("coursesmartEdApp", courseSmart);
		// globalAppState.put("coursesmartReading", courseSmartReading);
		// globalAppState.put("coursesmartReadingPageaXfsadf12",
		// courseSmartReadingPageaXfsadf12);
		globalAppState.put("student", alice);

		output.append(">> populated Event Generator\'s global state\n");

		// ----------------------------------------------------------------
		// Step 4: Execute reading sequence
		// ----------------------------------------------------------------
		output.append(">> sending events\n");

		// // Event # 1 - NavigationEvent
		// navigateToReading(globalAppState, "readium");
		// output.append(">>>>>> Navigated to Reading provided by Readium... sent NavigateEvent\n");
		//
		// // Event # 2 - ViewedEvent
		// viewPageInReading(globalAppState, "readium", "1");
		// output.append(">>>>>> Viewed Page with pageId 1 in Readium Reading... sent ViewedEvent\n");
		//
		// Event # 3 - Start Assignable Event
		startAssignment(globalAppState);
		output.append(">>>>>> Started Assigned Assessment in Super Assessment App... sent AssignableEvent[started]\n");

		// Event # 4 - Start Assessment Event
		startAssessment(globalAppState);
		output.append(">>>>>> Started Assessment in Super Assessment App... sent AssessmentEvent[started]\n");

		// Event # 5 - Start AssessmentItem Event
		startAssessmentItem(globalAppState);
		output.append(">>>>>> Started AssessmentItem in Super Assessment App... sent AssessmentItemEvent[started]\n");

		// Event # 6 - Completed AssessmentItem Event
		completeAssessmentItem(globalAppState);
		output.append(">>>>>> Completed AssessmentItem in Super Assessment App... sent AssessmentItemEvent[completed]\n");

		// Event # 7 - Submitted Assessment Event
		submitAssessment(globalAppState);
		output.append(">>>>>> Submitted Assessment in Super Assessment App... sent AssessmentEvent[submitted]\n");
		
		// Event # 8 - Start Assignable Event
		submitAssignment(globalAppState);
		output.append(">>>>>> Submitted Assigned Assessment in Super Assessment App... sent AssignableEvent[submitted]\n");
	}

	// Methods below are utility methods for generating events... These are NOT
	// part of Caliper standards work and are here only as a utility in this
	// sample App

	private void startAssignment(HashMap<String, Object> globalAppState) {

		AssignableEvent assignableStartEvent = AssignableEvent
				.forAction(Action.started);

		Attempt attempt = new Attempt(
				"https://some-university.edu/politicalScience/2014/american-revolution-101/assessment1/attempt1");
		attempt.setActor((LISPerson) globalAppState.get("student"));
		attempt.setCount(1);
		attempt.setAssignable((CaliperAssignableDigitalResource) globalAppState
				.get("assessment1"));
		globalAppState.put("assignment1attempt1", attempt);

		// action is set in navEvent constructor... now set actor and object
		assignableStartEvent
				.setActor((LISPerson) globalAppState.get("student"));
		assignableStartEvent
				.setObject((CaliperAssignableDigitalResource) globalAppState
						.get("assessment1"));
		assignableStartEvent.setGenerated(attempt);

		// add (learning) context for event
		assignableStartEvent.setEdApp((SoftwareApplication) globalAppState
				.get("canvas"));
		assignableStartEvent
				.setLisOrganization((LISOrganization) globalAppState
						.get("currentCourse"));

		// set time and any event specific properties
		assignableStartEvent.setStartedAt(DateTime.now().getMillis());

		// Send event to EventStore
		CaliperSensor.send(assignableStartEvent);

	}

	private void startAssessment(HashMap<String, Object> globalAppState) {

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

		// Send event to EventStore
		CaliperSensor.send(assessmentStartEvent);

	}

	private void startAssessmentItem(HashMap<String, Object> globalAppState) {

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

		// Send event to EventStore
		CaliperSensor.send(assessmentItemStartEvent);

	}

	private void completeAssessmentItem(HashMap<String, Object> globalAppState) {

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

		// Send event to EventStore
		CaliperSensor.send(assessmentItemCompletedEvent);

	}

	private void submitAssessment(HashMap<String, Object> globalAppState) {

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

		// Send event to EventStore
		CaliperSensor.send(assessmentStartEvent);

	}

	private void submitAssignment(HashMap<String, Object> globalAppState) {

		AssignableEvent assignableStartEvent = AssignableEvent
				.forAction(Action.submitted);

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

		// Send event to EventStore
		CaliperSensor.send(assignableStartEvent);

	}

	private void navigateToReading(HashMap<String, Object> globalAppState,
			String edApp) {

		NavigationEvent navEvent = new NavigationEvent();

		// action is set in navEvent constructor... now set actor and object
		navEvent.setActor((LISPerson) globalAppState.get("student"));
		navEvent.setObject((CaliperDigitalResource) globalAppState.get(edApp
				+ "Reading"));
		navEvent.setFromResource((CaliperDigitalResource) globalAppState
				.get("courseWebPage"));

		// add (learning) context for event
		navEvent.setEdApp((SoftwareApplication) globalAppState.get(edApp
				+ "EdApp"));
		navEvent.setLisOrganization((LISOrganization) globalAppState
				.get("currentCourse"));

		// set time and any event specific properties
		navEvent.setStartedAt(DateTime.now().getMillis());

		// Send event to EventStore
		CaliperSensor.send(navEvent);

	}

	private void viewPageInReading(HashMap<String, Object> globalAppState,
			String edApp, String pageId) {

		ViewedEvent viewPageEvent = new ViewedEvent();

		// action is set in navEvent constructor... now set actor and object
		viewPageEvent.setActor((LISPerson) globalAppState.get("student"));
		viewPageEvent.setObject((CaliperDigitalResource) globalAppState
				.get(edApp + "ReadingPage" + pageId));

		// add (learning) context for event
		viewPageEvent.setEdApp((SoftwareApplication) globalAppState.get(edApp
				+ "EdApp"));
		viewPageEvent.setLisOrganization((LISOrganization) globalAppState
				.get("currentCourse"));

		// set time and any event specific properties
		viewPageEvent.setStartedAt(DateTime.now().getMillis());
		int duration = randomSecsDurationBetween(5, 120);
		viewPageEvent.setDuration("PT" + duration + "S");

		// Send event to EventStore
		CaliperSensor.send(viewPageEvent);

	}

	private void highlightTermsInReading(
			HashMap<String, Object> globalAppState, String edApp,
			String pageId, int startIndex, int endIndex) {

		AnnotationEvent highlightTermsEvent = AnnotationEvent
				.forAction("highlighted");

		// action is set in navEvent constructor... now set actor and object
		highlightTermsEvent.setActor((LISPerson) globalAppState.get("student"));
		highlightTermsEvent.setObject((CaliperDigitalResource) globalAppState
				.get(edApp + "ReadingPage" + pageId));

		// highlight create action generates a HighlightAnnotation
		highlightTermsEvent.setGenerated(getHighlight(
				startIndex,
				endIndex,
				"Life, Liberty and the pursuit of Happiness",
				(CaliperDigitalResource) globalAppState.get(edApp
						+ "ReadingPage" + pageId)));

		// add (learning) context for event
		highlightTermsEvent.setEdApp((SoftwareApplication) globalAppState
				.get(edApp + "EdApp"));
		highlightTermsEvent.setLisOrganization((LISOrganization) globalAppState
				.get("currentCourse"));

		// set time and any event specific properties
		highlightTermsEvent.setStartedAt(DateTime.now().getMillis());

		// Send event to EventStore
		CaliperSensor.send(highlightTermsEvent);
	}

	/**
	 * @param endIndex
	 * @param startIndex
	 * @return
	 */
	private HighlightAnnotation getHighlight(int startIndex, int endIndex,
			String selectionText, CaliperDigitalResource target) {

		String baseUrl = "https://someEduApp.edu/highlights/";

		HighlightAnnotation highlightAnnotation = new HighlightAnnotation(
				baseUrl + UUID.randomUUID().toString());
		highlightAnnotation.getSelection().setStart(
				Integer.toString(startIndex));
		highlightAnnotation.getSelection().setEnd(Integer.toString(endIndex));
		highlightAnnotation.setSelectionText(selectionText);
		highlightAnnotation.setTarget(target);
		return highlightAnnotation;
	}

	private void bookmarkPageInReading(HashMap<String, Object> globalAppState,
			String edApp, String pageId) {

		AnnotationEvent bookmarkPageEvent = AnnotationEvent
				.forAction("bookmarked");

		// action is set in navEvent constructor... now set actor, object
		bookmarkPageEvent.setActor((LISPerson) globalAppState.get("student"));
		bookmarkPageEvent.setObject((CaliperDigitalResource) globalAppState
				.get(edApp + "ReadingPage" + pageId));

		// bookmark create action generates a BookmarkAnnotation
		bookmarkPageEvent
				.setGenerated(getBookmark((CaliperDigitalResource) globalAppState
						.get(edApp + "ReadingPage" + pageId)));

		// add (learning) context for event
		bookmarkPageEvent.setEdApp((SoftwareApplication) globalAppState
				.get(edApp + "EdApp"));
		bookmarkPageEvent.setLisOrganization((LISOrganization) globalAppState
				.get("currentCourse"));

		// set time and any event specific properties
		bookmarkPageEvent.setStartedAt(DateTime.now().getMillis());

		// Send event to EventStore
		CaliperSensor.send(bookmarkPageEvent);
	}

	private Object getBookmark(CaliperDigitalResource target) {

		String baseUrl = "https://someEduApp.edu/bookmarks/";

		BookmarkAnnotation bookmarkAnnotation = new BookmarkAnnotation(baseUrl
				+ UUID.randomUUID().toString());
		bookmarkAnnotation.setTarget(target);
		return bookmarkAnnotation;
	}

	private void tagPageInReading(HashMap<String, Object> globalAppState,
			String edApp, String pageId, List<String> tags) {

		AnnotationEvent tagPageEvent = AnnotationEvent.forAction("tagged");

		// action is set in navEvent constructor... now set actor and object
		tagPageEvent.setActor((LISPerson) globalAppState.get("student"));
		tagPageEvent.setObject((CaliperDigitalResource) globalAppState
				.get(edApp + "ReadingPage" + pageId));

		// tag create action generates a TagAnnotation
		tagPageEvent.setGenerated(getTag(
				tags,
				(CaliperDigitalResource) globalAppState.get(edApp
						+ "ReadingPage" + pageId)));

		// add (learning) context for event
		tagPageEvent.setEdApp((SoftwareApplication) globalAppState.get(edApp
				+ "EdApp"));
		tagPageEvent.setLisOrganization((LISOrganization) globalAppState
				.get("currentCourse"));

		// set time and any event specific properties
		tagPageEvent.setStartedAt(DateTime.now().getMillis());

		// Send event to EventStore
		CaliperSensor.send(tagPageEvent);
	}

	private Object getTag(List<String> tags, CaliperDigitalResource target) {

		String baseUrl = "https://someEduApp.edu/tags/";

		TagAnnotation tagAnnotation = new TagAnnotation(baseUrl
				+ UUID.randomUUID().toString());
		tagAnnotation.setTags(tags);
		tagAnnotation.setTarget(target);
		return tagAnnotation;
	}

	private void sharePageInReading(HashMap<String, Object> globalAppState,
			String edApp, String pageId, List<String> sharedWithIds) {

		AnnotationEvent sharePageEvent = AnnotationEvent.forAction("shared");

		// action is set in navEvent constructor... now set actor and object
		sharePageEvent.setActor((LISPerson) globalAppState.get("student"));
		sharePageEvent.setObject((CaliperDigitalResource) globalAppState
				.get(edApp + "ReadingPage" + pageId));

		// tag create action generates a SharedAnnotation
		sharePageEvent.setGenerated(getShareAnnotation(
				sharedWithIds,
				(CaliperDigitalResource) globalAppState.get(edApp
						+ "ReadingPage" + pageId)));

		// add (learning) context for event
		sharePageEvent.setEdApp((SoftwareApplication) globalAppState.get(edApp
				+ "EdApp"));
		sharePageEvent.setLisOrganization((LISOrganization) globalAppState
				.get("currentCourse"));

		// set time and any event specific properties
		sharePageEvent.setStartedAt(DateTime.now().getMillis());

		// Send event to EventStore
		CaliperSensor.send(sharePageEvent);
	}

	private Object getShareAnnotation(List<String> sharedWithIds,
			CaliperDigitalResource target) {

		String baseUrl = "https://someBookmarkingApp.edu/shares/";

		SharedAnnotation sharedAnnotation = new SharedAnnotation(baseUrl
				+ UUID.randomUUID().toString());
		sharedAnnotation.setUsers(sharedWithIds);
		sharedAnnotation.setTarget(target);
		return sharedAnnotation;
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
