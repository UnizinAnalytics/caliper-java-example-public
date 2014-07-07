package com.pnayak.test;

import java.io.IOException;
import java.util.HashMap;

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
import org.imsglobal.caliper.events.reading.NavigationEvent;
import org.joda.time.DateTime;
import org.joda.time.Weeks;

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

		generateReadingAnnotationEventSeqeuence();

		response.getWriter().write(Caliper.getStatistics().toString());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	private void generateReadingAnnotationEventSeqeuence() {

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

		// ----------------------------------------------------------------
		// Step 3: Set up activity context elements (i.e. the two Readings)
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

		// ----------------------------------------------------------------
		// Step 3: Populate Global App State for Event Generator
		// ----------------------------------------------------------------
		// Note - global app state - this is a simulation of the "state" of the
		// application that the Java sensor is installed into. In this case, it
		// is this Tomcat application. It is a utility data structure that is
		// used by subsequent event generation code
		HashMap<String, Object> globalAppState = Maps.newHashMap();
		globalAppState.put("currentCourse", americanHistoryCourse);
		globalAppState.put("readiumEdApp", readium);
		globalAppState.put("readiumReading", readiumReading);
		globalAppState.put("coursesmartEdApp", courseSmart);
		globalAppState.put("courseSmartReading", courseSmartReading);
		globalAppState.put("student", alice);

		// ----------------------------------------------------------------
		// Step 3: Execute reading sequence
		// ----------------------------------------------------------------
		navigateToFirstReading(globalAppState);
		viewPageInReading(globalAppState, 1);
		viewPageInReading(globalAppState, 2);
		usePageInReading(globalAppState, 3);

	}

	private void navigateToFirstReading(HashMap<String, Object> globalAppState) {

		NavigationEvent navEvent = new NavigationEvent();
		// action is set in navEvent constructor... now set agent and object
		navEvent.setAgent((Agent) globalAppState.get("student"));
		navEvent.setObject((DigitalResource) globalAppState
				.get("readiumReading"));
		// add (learning) context for event
		navEvent.setEdApp((SoftwareApplication) globalAppState
				.get("readiumEdApp"));
		navEvent.setOrganization((Organization) globalAppState
				.get("currentCourse"));
		// set time and any event specific properties
		navEvent.setStartedAt(DateTime.now().getMillis());
		navEvent.setOperationType("link"); // TODO - should this be part of
											// Metric Profile?

		Caliper.measure(navEvent);

	}

	private void viewPageInReading(HashMap<String, Object> globalAppState, int i) {
		// TODO Auto-generated method stub

	}

	private void usePageInReading(HashMap<String, Object> globalAppState, int i) {
		// TODO Auto-generated method stub

	}
}
