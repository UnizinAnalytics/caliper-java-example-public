package com.pnayak.test;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.imsglobal.caliper.Caliper;
import org.imsglobal.caliper.Options;
import org.imsglobal.caliper.entities.Agent;
import org.imsglobal.caliper.entities.Course;
import org.imsglobal.caliper.entities.DigitalResource;
import org.imsglobal.caliper.entities.Organization;
import org.imsglobal.caliper.entities.SoftwareApplication;
import org.imsglobal.caliper.events.annotation.BookmarkedEvent;
import org.imsglobal.caliper.events.annotation.HilightedEvent;
import org.imsglobal.caliper.events.annotation.SharedEvent;
import org.imsglobal.caliper.events.annotation.TaggedEvent;
import org.imsglobal.caliper.events.reading.NavigationEvent;
import org.imsglobal.caliper.events.reading.UsedEvent;
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
		// Student in a course interacts with two readings.
		// In the process of interacting, she performs various reading
		// activities as well as annotation activities. These are defined in
		// the Caliper Reading and Annotation profiles respectively

		// For reference, the current time
		DateTime now = DateTime.now();

		// ----------------------------------------------------------------
		// Step 1: Set up contextual elements
		// ----------------------------------------------------------------

		// Course context. NOTE - we would want to associate it with a parent
		// Department or Institution at some point
		Course americanHistoryCourse = new Course(
				"https://some-university.edu/politicalScience/2014/american-revolution-101",
				null);
		americanHistoryCourse.setCourseNumber("AmRev-101");
		americanHistoryCourse.setLabel("American Revolution 101");
		americanHistoryCourse.setSemester("Spring-2014");
		americanHistoryCourse.setLastModifiedAt(now.minus(Weeks.weeks(4))
				.getMillis());

		// edApp that provides the first reading
		SoftwareApplication readium = new SoftwareApplication();
		readium.setType("http://purl.imsglobal.org/ctx/caliper/v1/edApp/epub-reader");
		readium.setId("https://github.com/readium/readium-js-viewer");
		readium.setLastModifiedAt(now.minus(Weeks.weeks(8)).getMillis());

		// edApp that provides the second reading
		SoftwareApplication courseSmart = new SoftwareApplication();
		courseSmart
				.setType("http://purl.imsglobal.org/ctx/caliper/v1/edApp/Reading");
		courseSmart.setId("http://www.coursesmart.com/reader");
		courseSmart.setLastModifiedAt(now.minus(Weeks.weeks(6)).getMillis());

		// Student - performs interaction with reading activities
		Agent alice = new Agent(
				"https://some-university.edu/students/jones-alice-554433");
		alice.setLastModifiedAt(now.minus(Weeks.weeks(3)).getMillis());

		output.append(">> generated learning context data\n");

		// ----------------------------------------------------------------
		// Step 2: Set up activity context elements (i.e. the two Readings)
		// ----------------------------------------------------------------
		DigitalResource readiumReading = new DigitalResource();
		readiumReading
				.setId("https://github.com/readium/readium-js-viewer/book/34843#epubcfi(/4/3)");
		readiumReading.setType("EpubFragment");
		readiumReading
				.setName("The Glorious Cause: The American Revolution, 1763-1789 (Oxford History of the United States)");
		readiumReading
				.setLastModifiedAt(now.minus(Weeks.weeks(53)).getMillis());

		DigitalResource courseSmartReading = new DigitalResource();
		courseSmartReading
				.setId("http://www.coursesmart.com/the-american-revolution-a-concise-history/robert-j-allison/dp/9780199347322");
		courseSmartReading.setType("EpubFragment");
		courseSmartReading
				.setName("The American Revolution: A Concise History | 978-0-19-531295-9");
		courseSmartReading.setLastModifiedAt(now.minus(Weeks.weeks(22))
				.getMillis());

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
		globalAppState.put("readiumEdApp", readium);
		globalAppState.put("readiumReading", readiumReading);
		globalAppState.put("coursesmartEdApp", courseSmart);
		globalAppState.put("coursesmartReading", courseSmartReading);
		globalAppState.put("student", alice);

		output.append(">> populated Event Generator\'s global state\n");

		// ----------------------------------------------------------------
		// Step 4: Execute reading sequence
		// ----------------------------------------------------------------
		output.append(">> sending events\n");

		navigateToReading(globalAppState, "readium");
		output.append(">>>>>> Navigated to Reading provided by Readium... sent NavigateEvent\n");

		viewPageInReading(globalAppState, "readium", "1");
		output.append(">>>>>> Viewed Page with pageId 1 in Readium Reading... sent ViewedEvent\n");

		viewPageInReading(globalAppState, "readium", "2");
		output.append(">>>>>> Viewed Page with pageId 2 in Readium Reading... sent ViewedEvent\n");

		usePageInReading(globalAppState, "readium", "3");
		output.append(">>>>>> Used Page with pageId 3 in Readium Reading... sent UsedEvent\n");

		hilightTermsInReading(globalAppState, "readium", "3", 455, 489);
		output.append(">>>>>> Hilighted fragment in pageId 3 from index 455 to 489 in Readium Reading... sent HilightedEvent\n");

		viewPageInReading(globalAppState, "readium", "2");
		output.append(">>>>>> Viewed Page with pageId 2 in Readium Reading... sent ViewedEvent\n");

		usePageInReading(globalAppState, "readium", "2");
		output.append(">>>>>> Used Page with pageId 2 in Readium Reading... sent UsedEvent\n");

		bookmarkPageInReading(globalAppState, "readium", "2");
		output.append(">>>>>> Bookmarked Page with pageId 2 in Readium Reading... sent BookmarkedEvent\n");

		navigateToReading(globalAppState, "coursesmart");
		output.append(">>>>>> Navigated to Reading provided by CourseSmart... sent NavigateEvent\n");

		viewPageInReading(globalAppState, "coursesmart", "aXfsadf12");
		output.append(">>>>>> Viewed Page with pageId aXfsadf12 in CourseSmart Reading... sent ViewedEvent\n");

		tagPageInReading(globalAppState, "coursesmart", "aXfsadf12",
				Lists.newArrayList("to-read", "1776",
						"shared-with-project-team"));
		output.append(">>>>>> Tagged Page with pageId aXfsadf12 with tags [to-read, 1776, shared-with-project-team] in CourseSmart Reading... sent TaggedEvent\n");
		
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

		// action is set in navEvent constructor... now set agent and object
		navEvent.setAgent((Agent) globalAppState.get("student"));
		navEvent.setObject((DigitalResource) globalAppState.get(edApp
				+ "Reading"));

		// add (learning) context for event
		navEvent.setEdApp((SoftwareApplication) globalAppState.get(edApp
				+ "EdApp"));
		navEvent.setOrganization((Organization) globalAppState
				.get("currentCourse"));

		// set time and any event specific properties
		navEvent.setStartedAt(DateTime.now().getMillis());
		navEvent.setOperationType("link"); // TODO - should this be part of
											// Metric Profile?
		// Send event to EventStore
		Caliper.measure(navEvent);

	}

	private void viewPageInReading(HashMap<String, Object> globalAppState,
			String edApp, String pageId) {

		ViewedEvent viewPageEvent = new ViewedEvent();

		// action is set in navEvent constructor... now set agent and object
		viewPageEvent.setAgent((Agent) globalAppState.get("student"));
		DigitalResource reading = (DigitalResource) globalAppState.get(edApp
				+ "Reading");
		reading.getProperties().put("pageId", pageId);
		viewPageEvent.setObject((DigitalResource) globalAppState.get(edApp
				+ "Reading"));

		// add (learning) context for event
		viewPageEvent.setEdApp((SoftwareApplication) globalAppState.get(edApp
				+ "EdApp"));
		viewPageEvent.setOrganization((Organization) globalAppState
				.get("currentCourse"));

		// set time and any event specific properties
		viewPageEvent.setStartedAt(DateTime.now().getMillis());

		// Send event to EventStore
		Caliper.measure(viewPageEvent);

		// clean up
		reading.getProperties().remove("pageId");

	}

	private void usePageInReading(HashMap<String, Object> globalAppState,
			String edApp, String pageId) {

		UsedEvent usePageEvent = new UsedEvent();

		// action is set in navEvent constructor... now set agent and object
		usePageEvent.setAgent((Agent) globalAppState.get("student"));
		DigitalResource reading = (DigitalResource) globalAppState.get(edApp
				+ "Reading");
		reading.getProperties().put("pageId", pageId);
		usePageEvent.setObject((DigitalResource) globalAppState.get(edApp
				+ "Reading"));

		// add (learning) context for event
		usePageEvent.setEdApp((SoftwareApplication) globalAppState.get(edApp
				+ "EdApp"));
		usePageEvent.setOrganization((Organization) globalAppState
				.get("currentCourse"));

		// set time and any event specific properties
		usePageEvent.setStartedAt(DateTime.now().getMillis());
		usePageEvent.setEndedAt(DateTime.now().plusMinutes(3).getMillis());

		// Send event to EventStore
		Caliper.measure(usePageEvent);

		// clean up
		reading.getProperties().remove("pageId");

	}

	private void hilightTermsInReading(HashMap<String, Object> globalAppState,
			String edApp, String pageId, int startIndex, int endIndex) {

		HilightedEvent hilightTermsEvent = HilightedEvent.forHilight();

		// action is set in navEvent constructor... now set agent and object
		hilightTermsEvent.setAgent((Agent) globalAppState.get("student"));
		DigitalResource reading = (DigitalResource) globalAppState.get(edApp
				+ "Reading");
		reading.getProperties().put("pageId", pageId);
		reading.getProperties().put("hilightStartIndex", startIndex);
		reading.getProperties().put("hilightEndIndex", endIndex);
		hilightTermsEvent.setObject((DigitalResource) globalAppState.get(edApp
				+ "Reading"));

		// add (learning) context for event
		hilightTermsEvent.setEdApp((SoftwareApplication) globalAppState
				.get(edApp + "EdApp"));
		hilightTermsEvent.setOrganization((Organization) globalAppState
				.get("currentCourse"));

		// set time and any event specific properties
		hilightTermsEvent.setStartedAt(DateTime.now().getMillis());

		// Send event to EventStore
		Caliper.measure(hilightTermsEvent);

		// clean up
		reading.getProperties().remove("pageId");
		reading.getProperties().remove("hilightStartIndex");
		reading.getProperties().remove("hilightEndIndex");
	}

	private void bookmarkPageInReading(HashMap<String, Object> globalAppState,
			String edApp, String pageId) {

		BookmarkedEvent bookmarkPageEvent = BookmarkedEvent.forMark();

		// action is set in navEvent constructor... now set agent and object
		bookmarkPageEvent.setAgent((Agent) globalAppState.get("student"));
		DigitalResource reading = (DigitalResource) globalAppState.get(edApp
				+ "Reading");
		reading.getProperties().put("pageId", pageId);
		bookmarkPageEvent.setObject((DigitalResource) globalAppState.get(edApp
				+ "Reading"));

		// add (learning) context for event
		bookmarkPageEvent.setEdApp((SoftwareApplication) globalAppState
				.get(edApp + "EdApp"));
		bookmarkPageEvent.setOrganization((Organization) globalAppState
				.get("currentCourse"));

		// set time and any event specific properties
		bookmarkPageEvent.setStartedAt(DateTime.now().getMillis());

		// Send event to EventStore
		Caliper.measure(bookmarkPageEvent);

		// clean up
		reading.getProperties().remove("pageId");
	}

	private void tagPageInReading(HashMap<String, Object> globalAppState,
			String edApp, String pageId, List<String> tags) {

		TaggedEvent tagPageEvent = TaggedEvent.forTag();

		// action is set in navEvent constructor... now set agent and object
		tagPageEvent.setAgent((Agent) globalAppState.get("student"));
		DigitalResource reading = (DigitalResource) globalAppState.get(edApp
				+ "Reading");
		reading.getProperties().put("pageId", pageId);
		reading.getProperties().put("tags", tags);
		tagPageEvent.setObject((DigitalResource) globalAppState.get(edApp
				+ "Reading"));

		// add (learning) context for event
		tagPageEvent.setEdApp((SoftwareApplication) globalAppState.get(edApp
				+ "EdApp"));
		tagPageEvent.setOrganization((Organization) globalAppState
				.get("currentCourse"));

		// set time and any event specific properties
		tagPageEvent.setStartedAt(DateTime.now().getMillis());

		// Send event to EventStore
		Caliper.measure(tagPageEvent);

		// clean up
		reading.getProperties().remove("pageId");
		reading.getProperties().remove("tags");
	}

	private void sharePageInReading(HashMap<String, Object> globalAppState,
			String edApp, String pageId, List<String> sharedWithIds) {

		SharedEvent sharePageEvent = SharedEvent.forShare();

		// action is set in navEvent constructor... now set agent and object
		sharePageEvent.setAgent((Agent) globalAppState.get("student"));
		DigitalResource reading = (DigitalResource) globalAppState.get(edApp
				+ "Reading");
		reading.getProperties().put("pageId", pageId);
		reading.getProperties().put("sharedWith", sharedWithIds);
		sharePageEvent.setObject((DigitalResource) globalAppState.get(edApp
				+ "Reading"));

		// add (learning) context for event
		sharePageEvent.setEdApp((SoftwareApplication) globalAppState.get(edApp
				+ "EdApp"));
		sharePageEvent.setOrganization((Organization) globalAppState
				.get("currentCourse"));

		// set time and any event specific properties
		sharePageEvent.setStartedAt(DateTime.now().getMillis());

		// Send event to EventStore
		Caliper.measure(sharePageEvent);

		// clean up
		reading.getProperties().remove("pageId");
		reading.getProperties().remove("sharedWith");
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
