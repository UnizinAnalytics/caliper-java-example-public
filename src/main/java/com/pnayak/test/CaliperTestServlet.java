package com.pnayak.test;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.imsglobal.caliper.Caliper;
import org.imsglobal.caliper.Options;
import org.imsglobal.caliper.entities.CaliperDigitalResource;
import org.imsglobal.caliper.entities.SoftwareApplication;
import org.imsglobal.caliper.entities.annotation.BookmarkAnnotation;
import org.imsglobal.caliper.entities.annotation.HighlightAnnotation;
import org.imsglobal.caliper.entities.annotation.ShareAnnotation;
import org.imsglobal.caliper.entities.annotation.TagAnnotation;
import org.imsglobal.caliper.entities.lis.LISCourseSection;
import org.imsglobal.caliper.entities.lis.LISOrganization;
import org.imsglobal.caliper.entities.lis.LISPerson;
import org.imsglobal.caliper.entities.reading.EPubSubChapter;
import org.imsglobal.caliper.entities.reading.EPubVolume;
import org.imsglobal.caliper.entities.schemadotorg.WebPage;
import org.imsglobal.caliper.events.annotation.BookmarkedEvent;
import org.imsglobal.caliper.events.annotation.HighlightedEvent;
import org.imsglobal.caliper.events.annotation.SharedEvent;
import org.imsglobal.caliper.events.annotation.TaggedEvent;
import org.imsglobal.caliper.events.reading.NavigationEvent;
import org.imsglobal.caliper.events.reading.ViewedEvent;
import org.joda.time.DateTime;
import org.joda.time.Weeks;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * Servlet implementation class CaliperTestServlet
 */
public class CaliperTestServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private static String HOST = "http://localhost:1080/1.0/event/put";
	private static String API_KEY = "FEFNtMyXRZqwAH4svMakTw";

	private void initialize() {

		// Initialize the sensor - this needs to be done only once
		Options options = new Options();
		options.setHost(HOST);
		options.setApiKey(API_KEY);
		Caliper.initialize(options);
	}

	/**
	 * Default constructor.
	 */
	public CaliperTestServlet() {
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

		output.append("=========================================================================\n");
		output.append("Caliper Event Generator: Generating Reading and Annotation Sequence\n");
		output.append("=========================================================================\n");

		generateReadingAnnotationEventSeqeuence(output);

		output.append(Caliper.getStatistics().toString());

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

	private void generateReadingAnnotationEventSeqeuence(StringBuffer output) {

		// ================================================================
		// ------------------------Reading sequence------------------------
		// ================================================================
		// Student in a course interacts with two readings from two edApps.
		// In the process of interacting, she performs various reading
		// activities as well as annotation activities. These are defined in
		// the Caliper Reading and Annotation profiles respectively

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

		// edApp that provides the first reading
		SoftwareApplication readium = new SoftwareApplication(
				"https://github.com/readium/readium-js-viewer");
		readium.setType("http://purl.imsglobal.org/ctx/caliper/v1/edApp/epub-reader");
		readium.setLastModifiedAt(now.minus(Weeks.weeks(8)).getMillis());

		// edApp that provides the second reading
		SoftwareApplication courseSmart = new SoftwareApplication(
				"http://www.coursesmart.com/reader");
		courseSmart
				.setType("http://purl.imsglobal.org/ctx/caliper/v1/edApp/epub-reader");
		courseSmart.setLastModifiedAt(now.minus(Weeks.weeks(6)).getMillis());

		// Student - performs interaction with reading activities
		LISPerson alice = new LISPerson(
				"https://some-university.edu/students/jones-alice-554433");
		alice.setLastModifiedAt(now.minus(Weeks.weeks(3)).getMillis());

		output.append(">> generated learning context data\n");

		// ---------------------------------------------------------------------
		// Step 2: Set up activity context elements (i.e. the two EPub Readings)
		// ---------------------------------------------------------------------
		EPubVolume readiumReading = new EPubVolume(
				"https://github.com/readium/readium-js-viewer/book/34843#epubcfi(/4/3)");
		readiumReading
				.setName("The Glorious Cause: The American Revolution, 1763-1789 (Oxford History of the United States)");
		readiumReading
				.setLastModifiedAt(now.minus(Weeks.weeks(53)).getMillis());
		readiumReading.setLanguage("English");

		EPubSubChapter readiumReadingPage1 = new EPubSubChapter(
				"https://github.com/readium/readium-js-viewer/book/34843#epubcfi(/4/3)/1");
		readiumReadingPage1.setName("Key Figures: George Washington)");
		readiumReadingPage1.setLastModifiedAt(now.minus(Weeks.weeks(53))
				.getMillis());
		readiumReading.setLanguage("English");
		readiumReadingPage1.setParentRef(readiumReading);

		EPubSubChapter readiumReadingPage2 = new EPubSubChapter(
				"https://github.com/readium/readium-js-viewer/book/34843#epubcfi(/4/3)/2");
		readiumReadingPage2.setName("Key Figures: Lord Cornwalis)");
		readiumReadingPage2.setLastModifiedAt(now.minus(Weeks.weeks(53))
				.getMillis());
		readiumReading.setLanguage("English");
		readiumReadingPage2.setParentRef(readiumReading);

		EPubSubChapter readiumReadingPage3 = new EPubSubChapter(
				"https://github.com/readium/readium-js-viewer/book/34843#epubcfi(/4/3)/3");
		readiumReadingPage3.setName("Key Figures: Paul Revere)");
		readiumReadingPage3.setLastModifiedAt(now.minus(Weeks.weeks(53))
				.getMillis());
		readiumReading.setLanguage("English");
		readiumReadingPage3.setParentRef(readiumReading);

		// ........................................................................

		EPubVolume courseSmartReading = new EPubVolume(
				"http://www.coursesmart.com/the-american-revolution-a-concise-history/robert-j-allison/dp/9780199347322");
		courseSmartReading
				.setName("The American Revolution: A Concise History | 978-0-19-531295-9");
		courseSmartReading.setLastModifiedAt(now.minus(Weeks.weeks(22))
				.getMillis());
		courseSmartReading.setLanguage("English");

		EPubSubChapter courseSmartReadingPageaXfsadf12 = new EPubSubChapter(
				"http://www.coursesmart.com/the-american-revolution-a-concise-history/robert-j-allison/dp/9780199347322/aXfsadf12");
		courseSmartReadingPageaXfsadf12.setName("The Boston Tea Party");
		courseSmartReading.setLastModifiedAt(now.minus(Weeks.weeks(22))
				.getMillis());
		courseSmartReadingPageaXfsadf12.setLanguage("English");
		courseSmartReadingPageaXfsadf12.setParentRef(courseSmartReading);

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
		globalAppState.put("readiumEdApp", readium);
		globalAppState.put("readiumReading", readiumReading);
		globalAppState.put("readiumReadingPage1", readiumReadingPage1);
		globalAppState.put("readiumReadingPage2", readiumReadingPage2);
		globalAppState.put("readiumReadingPage3", readiumReadingPage3);
		globalAppState.put("coursesmartEdApp", courseSmart);
		globalAppState.put("coursesmartReading", courseSmartReading);
		globalAppState.put("coursesmartReadingPageaXfsadf12",
				courseSmartReadingPageaXfsadf12);
		globalAppState.put("student", alice);

		output.append(">> populated Event Generator\'s global state\n");

		// ----------------------------------------------------------------
		// Step 4: Execute reading sequence
		// ----------------------------------------------------------------
		output.append(">> sending events\n");
		
		// Event # 1 - NavigationEvent
		navigateToReading(globalAppState, "readium");
		output.append(">>>>>> Navigated to Reading provided by Readium... sent NavigateEvent\n");

		// Event # 2 - ViewedEvent
		viewPageInReading(globalAppState, "readium", "1");
		output.append(">>>>>> Viewed Page with pageId 1 in Readium Reading... sent ViewedEvent\n");
		
		// Event # 3 - ViewedEvent
		viewPageInReading(globalAppState, "readium", "2");
		output.append(">>>>>> Viewed Page with pageId 2 in Readium Reading... sent ViewedEvent\n");

		// Event # 4 - HighlitedEvent
		hilightTermsInReading(globalAppState, "readium", "2", 455, 489);
		output.append(">>>>>> Hilighted fragment in pageId 2 from index 455 to 489 in Readium Reading... sent HilightedEvent\n");

		// Event # 5 - Viewed Event
		viewPageInReading(globalAppState, "readium", "3");
		output.append(">>>>>> Viewed Page with pageId 3 in Readium Reading... sent ViewedEvent\n");

		// Event # 6 - BookmarkedEvent
		bookmarkPageInReading(globalAppState, "readium", "3");
		output.append(">>>>>> Bookmarked Page with pageId 3 in Readium Reading... sent BookmarkedEvent\n");

		// Event # 7 - NavigationEvent
		navigateToReading(globalAppState, "coursesmart");
		output.append(">>>>>> Navigated to Reading provided by CourseSmart... sent NavigateEvent\n");

		// Event # 8 - ViewedEvent
		viewPageInReading(globalAppState, "coursesmart", "aXfsadf12");
		output.append(">>>>>> Viewed Page with pageId aXfsadf12 in CourseSmart Reading... sent ViewedEvent\n");

		// Event # 9 - TaggedEvent
		tagPageInReading(globalAppState, "coursesmart", "aXfsadf12",
				Lists.newArrayList("to-read", "1776",
						"shared-with-project-team"));
		output.append(">>>>>> Tagged Page with pageId aXfsadf12 with tags [to-read, 1776, shared-with-project-team] in CourseSmart Reading... sent TaggedEvent\n");

		// Event # 10 - SharedEvent
		sharePageInReading(
				globalAppState,
				"coursesmart",
				"aXfsadf12",
				Lists.newArrayList(
						"https://some-university.edu/students/smith-bob-554433",
						"https://some-university.edu/students/lam-eve-554433"));
		output.append(">>>>>> Shared Page with pageId aXfsadf12 with students [bob, eve] in CourseSmart Reading... sent SharedEvent\n");
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
		Caliper.measure(navEvent);

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

		// Send event to EventStore
		Caliper.measure(viewPageEvent);

	}

	private void hilightTermsInReading(HashMap<String, Object> globalAppState,
			String edApp, String pageId, int startIndex, int endIndex) {

		HighlightedEvent hilightTermsEvent = HighlightedEvent
				.forAction("created");

		// action is set in navEvent constructor... now set actor and object
		hilightTermsEvent.setActor((LISPerson) globalAppState.get("student"));
		hilightTermsEvent.setObject(getHighlight(
				startIndex,
				endIndex,
				"Life, Liberty and the pursuit of Happiness",
				(CaliperDigitalResource) globalAppState.get(edApp
						+ "ReadingPage" + pageId)));

		// set target of highlight create action
		hilightTermsEvent.setTarget((CaliperDigitalResource) globalAppState
				.get(edApp + "ReadingPage" + pageId));

		// add (learning) context for event
		hilightTermsEvent.setEdApp((SoftwareApplication) globalAppState
				.get(edApp + "EdApp"));
		hilightTermsEvent.setLisOrganization((LISOrganization) globalAppState
				.get("currentCourse"));

		// set time and any event specific properties
		hilightTermsEvent.setStartedAt(DateTime.now().getMillis());

		// Send event to EventStore
		Caliper.measure(hilightTermsEvent);
	}

	/**
	 * @param endIndex
	 * @param startIndex
	 * @return
	 */
	private HighlightAnnotation getHighlight(int startIndex, int endIndex,
			String selectionText, CaliperDigitalResource target) {
		
		String baseUrl = "https://someEduApp.edu/highlights/";
		
		HighlightAnnotation highlightAnnotation = new HighlightAnnotation(baseUrl + UUID
				.randomUUID().toString());
		highlightAnnotation.getSelection().setStart(
				Integer.toString(startIndex));
		highlightAnnotation.getSelection().setEnd(Integer.toString(endIndex));
		highlightAnnotation.setSelectionText(selectionText);
		highlightAnnotation.setTarget(target);
		return highlightAnnotation;
	}

	private void bookmarkPageInReading(HashMap<String, Object> globalAppState,
			String edApp, String pageId) {

		BookmarkedEvent bookmarkPageEvent = BookmarkedEvent
				.forAction("created");

		// action is set in navEvent constructor... now set actor, object
		bookmarkPageEvent.setActor((LISPerson) globalAppState.get("student"));
		bookmarkPageEvent
				.setObject(getBookmark((CaliperDigitalResource) globalAppState
						.get(edApp + "ReadingPage" + pageId)));

		// set target of bookmark create action
		bookmarkPageEvent.setTarget((CaliperDigitalResource) globalAppState
				.get(edApp + "ReadingPage" + pageId));

		// add (learning) context for event
		bookmarkPageEvent.setEdApp((SoftwareApplication) globalAppState
				.get(edApp + "EdApp"));
		bookmarkPageEvent.setLisOrganization((LISOrganization) globalAppState
				.get("currentCourse"));

		// set time and any event specific properties
		bookmarkPageEvent.setStartedAt(DateTime.now().getMillis());

		// Send event to EventStore
		Caliper.measure(bookmarkPageEvent);
	}

	private Object getBookmark(CaliperDigitalResource target) {
		
		String baseUrl = "https://someEduApp.edu/bookmarks/";
		
		BookmarkAnnotation bookmarkAnnotation = new BookmarkAnnotation(baseUrl + UUID
				.randomUUID().toString());
		bookmarkAnnotation.setTarget(target);
		return bookmarkAnnotation;
	}

	private void tagPageInReading(HashMap<String, Object> globalAppState,
			String edApp, String pageId, List<String> tags) {

		TaggedEvent tagPageEvent = TaggedEvent.forAction("created");

		// action is set in navEvent constructor... now set actor and object
		tagPageEvent.setActor((LISPerson) globalAppState.get("student"));
		tagPageEvent.setObject(getTag(
				tags,
				(CaliperDigitalResource) globalAppState.get(edApp
						+ "ReadingPage" + pageId)));

		// set target of tag create action
		tagPageEvent.setTarget((CaliperDigitalResource) globalAppState
				.get(edApp + "ReadingPage" + pageId));

		// add (learning) context for event
		tagPageEvent.setEdApp((SoftwareApplication) globalAppState.get(edApp
				+ "EdApp"));
		tagPageEvent.setLisOrganization((LISOrganization) globalAppState
				.get("currentCourse"));

		// set time and any event specific properties
		tagPageEvent.setStartedAt(DateTime.now().getMillis());

		// Send event to EventStore
		Caliper.measure(tagPageEvent);
	}

	private Object getTag(List<String> tags, CaliperDigitalResource target) {
		
		String baseUrl = "https://someEduApp.edu/tags/";
		
		TagAnnotation tagAnnotation = new TagAnnotation(baseUrl + UUID.randomUUID()
				.toString());
		tagAnnotation.setTags(tags);
		tagAnnotation.setTarget(target);
		return tagAnnotation;
	}

	private void sharePageInReading(HashMap<String, Object> globalAppState,
			String edApp, String pageId, List<String> sharedWithIds) {

		SharedEvent sharePageEvent = SharedEvent.forAction("created");

		// action is set in navEvent constructor... now set actor and object
		sharePageEvent.setActor((LISPerson) globalAppState.get("student"));
		sharePageEvent.setObject(getShareAnnotation(
				sharedWithIds,
				(CaliperDigitalResource) globalAppState.get(edApp
						+ "ReadingPage" + pageId)));

		// set target of tag create action
		sharePageEvent.setTarget((CaliperDigitalResource) globalAppState
				.get(edApp + "ReadingPage" + pageId));

		// add (learning) context for event
		sharePageEvent.setEdApp((SoftwareApplication) globalAppState.get(edApp
				+ "EdApp"));
		sharePageEvent.setLisOrganization((LISOrganization) globalAppState
				.get("currentCourse"));

		// set time and any event specific properties
		sharePageEvent.setStartedAt(DateTime.now().getMillis());

		// Send event to EventStore
		Caliper.measure(sharePageEvent);
	}

	private Object getShareAnnotation(List<String> sharedWithIds,
			CaliperDigitalResource target) {
		
		String baseUrl = "https://someBookmarkingApp.edu/shares/";
		
		ShareAnnotation shareAnnotation = new ShareAnnotation(baseUrl + UUID.randomUUID()
				.toString());
		shareAnnotation.setUsers(sharedWithIds);
		shareAnnotation.setTarget(target);
		return shareAnnotation;
	}

	private void pauseFor(int time) {

		try {
			Thread.sleep(time * 1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
