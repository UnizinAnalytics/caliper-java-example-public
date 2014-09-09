package com.pnayak.test;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.imsglobal.caliper.CaliperSensor;
import org.imsglobal.caliper.Options;
import org.imsglobal.caliper.entities.CaliperDigitalResource;
import org.imsglobal.caliper.entities.SoftwareApplication;
import org.imsglobal.caliper.entities.annotation.BookmarkAnnotation;
import org.imsglobal.caliper.entities.annotation.HighlightAnnotation;
import org.imsglobal.caliper.entities.annotation.SharedAnnotation;
import org.imsglobal.caliper.entities.annotation.TagAnnotation;
import org.imsglobal.caliper.entities.lis.LISCourseSection;
import org.imsglobal.caliper.entities.lis.LISOrganization;
import org.imsglobal.caliper.entities.lis.LISPerson;
import org.imsglobal.caliper.entities.reading.EPubSubChapter;
import org.imsglobal.caliper.entities.reading.EPubVolume;
import org.imsglobal.caliper.entities.schemadotorg.WebPage;
import org.imsglobal.caliper.events.annotation.AnnotationEvent;
import org.imsglobal.caliper.events.reading.NavigationEvent;
import org.imsglobal.caliper.events.reading.ViewedEvent;
import org.joda.time.DateTime;
import org.joda.time.Weeks;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.UUID;

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
		// the course.  The assignable here is an Assessment.
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
		americanHistoryCourse.setSemester("Spring-2014");
		americanHistoryCourse.setLastModifiedAt(now.minus(Weeks.weeks(4))
				.getMillis());

		WebPage courseWebPage = new WebPage("AmRev-101-landingPage");
		courseWebPage.setName("American Revolution 101 Landing Page");
		courseWebPage.setParentRef(americanHistoryCourse);

		// edApp that provides the assessment
		SoftwareApplication superAssessmentTool = new SoftwareApplication(
				"https://com.sat/super-assessment-tool");
		superAssessmentTool.setType("http://purl.imsglobal.org/ctx/caliper/v1/edApp/assessment");
		superAssessmentTool.setLastModifiedAt(now.minus(Weeks.weeks(8)).getMillis());

		// Student - performs interaction with reading activities
		LISPerson alice = new LISPerson(
				"https://some-university.edu/students/jones-alice-554433");
		alice.setLastModifiedAt(now.minus(Weeks.weeks(3)).getMillis());

		output.append(">> generated learning context data\n");

		// -------------------------------------------------------------------------
		// Step 2: Set up activity context elements (i.e. the Assignable Assessment)
		// -------------------------------------------------------------------------


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
		globalAppState.put("currentCourse", americanHistoryCourse);
		globalAppState.put("courseWebPage", courseWebPage);
		globalAppState.put("assessmentEdApp", superAssessmentTool);
//		globalAppState.put("readiumReading", readiumReading);
//		globalAppState.put("readiumReadingPage1", readiumReadingPage1);
//		globalAppState.put("readiumReadingPage2", readiumReadingPage2);
//		globalAppState.put("readiumReadingPage3", readiumReadingPage3);
//		globalAppState.put("coursesmartEdApp", courseSmart);
//		globalAppState.put("coursesmartReading", courseSmartReading);
//		globalAppState.put("coursesmartReadingPageaXfsadf12",
//				courseSmartReadingPageaXfsadf12);
		globalAppState.put("student", alice);

		output.append(">> populated Event Generator\'s global state\n");

		// ----------------------------------------------------------------
		// Step 4: Execute reading sequence
		// ----------------------------------------------------------------
		output.append(">> sending events\n");

//		// Event # 1 - NavigationEvent
//		navigateToReading(globalAppState, "readium");
//		output.append(">>>>>> Navigated to Reading provided by Readium... sent NavigateEvent\n");
//
//		// Event # 2 - ViewedEvent
//		viewPageInReading(globalAppState, "readium", "1");
//		output.append(">>>>>> Viewed Page with pageId 1 in Readium Reading... sent ViewedEvent\n");
//
//		// Event # 3 - ViewedEvent
//		viewPageInReading(globalAppState, "readium", "2");
//		output.append(">>>>>> Viewed Page with pageId 2 in Readium Reading... sent ViewedEvent\n");
//
//		// Event # 4 - HighlitedEvent
//		highlightTermsInReading(globalAppState, "readium", "2", 455, 489);
//		output.append(">>>>>> Highlighted fragment in pageId 2 from index 455 to 489 in Readium Reading... sent HighlightedEvent\n");
//
//		// Event # 5 - Viewed Event
//		viewPageInReading(globalAppState, "readium", "3");
//		output.append(">>>>>> Viewed Page with pageId 3 in Readium Reading... sent ViewedEvent\n");
//
//		// Event # 6 - BookmarkedEvent
//		bookmarkPageInReading(globalAppState, "readium", "3");
//		output.append(">>>>>> Bookmarked Page with pageId 3 in Readium Reading... sent BookmarkedEvent\n");
//
//		// Event # 7 - NavigationEvent
//		navigateToReading(globalAppState, "coursesmart");
//		output.append(">>>>>> Navigated to Reading provided by CourseSmart... sent NavigateEvent\n");
//
//		// Event # 8 - ViewedEvent
//		viewPageInReading(globalAppState, "coursesmart", "aXfsadf12");
//		output.append(">>>>>> Viewed Page with pageId aXfsadf12 in CourseSmart Reading... sent ViewedEvent\n");
//
//		// Event # 9 - TaggedEvent
//		tagPageInReading(globalAppState, "coursesmart", "aXfsadf12",
//				Lists.newArrayList("to-read", "1776",
//						"shared-with-project-team"));
//		output.append(">>>>>> Tagged Page with pageId aXfsadf12 with tags [to-read, 1776, shared-with-project-team] in CourseSmart Reading... sent TaggedEvent\n");
//
//		// Event # 10 - SharedEvent
//		sharePageInReading(
//				globalAppState,
//				"coursesmart",
//				"aXfsadf12",
//				Lists.newArrayList(
//						"https://some-university.edu/students/smith-bob-554433",
//						"https://some-university.edu/students/lam-eve-554433"));
//		output.append(">>>>>> Shared Page with pageId aXfsadf12 with students [bob, eve] in CourseSmart Reading... sent SharedEvent\n");
	}

	// Methods below are utility methods for generating events... These are NOT
	// part of Caliper standards work and are here only as a utility in this
	// sample App

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

	private void highlightTermsInReading(HashMap<String, Object> globalAppState,
			String edApp, String pageId, int startIndex, int endIndex) {

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
		return r.nextInt((end-start) + start);
	}
}
