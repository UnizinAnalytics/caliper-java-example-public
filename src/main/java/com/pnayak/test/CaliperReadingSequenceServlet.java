package com.pnayak.test;

import com.google.common.collect.Lists;
import org.imsglobal.caliper.CaliperSensor;
import org.imsglobal.caliper.Options;
import org.imsglobal.caliper.actions.AnnotationActions;
import org.imsglobal.caliper.actions.ReadingActions;
import org.imsglobal.caliper.entities.CaliperDigitalResource;
import org.imsglobal.caliper.entities.LearningContext;
import org.imsglobal.caliper.entities.SoftwareApplication;
import org.imsglobal.caliper.entities.annotation.BookmarkAnnotation;
import org.imsglobal.caliper.entities.annotation.HighlightAnnotation;
import org.imsglobal.caliper.entities.annotation.SharedAnnotation;
import org.imsglobal.caliper.entities.annotation.TagAnnotation;
import org.imsglobal.caliper.entities.lis.LISCourseSection;
import org.imsglobal.caliper.entities.lis.LISPerson;
import org.imsglobal.caliper.entities.reading.EPubSubChapter;
import org.imsglobal.caliper.entities.reading.EPubVolume;
import org.imsglobal.caliper.entities.schemadotorg.WebPage;
import org.imsglobal.caliper.events.annotation.AnnotationEvent;
import org.imsglobal.caliper.events.reading.NavigationEvent;
import org.imsglobal.caliper.events.reading.ViewedEvent;
import org.imsglobal.caliper.profiles.AnnotationProfile;
import org.imsglobal.caliper.profiles.ReadingProfile;
import org.joda.time.DateTime;
import org.joda.time.Weeks;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Random;
import java.util.UUID;

/**
 * Servlet implementation class CaliperReadingSequenceServlet
 */
public class CaliperReadingSequenceServlet extends HttpServlet {

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
    public CaliperReadingSequenceServlet() {
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

        generateReadingAnnotationEventSequence(output);

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

    private void generateReadingAnnotationEventSequence(StringBuffer output) {

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
        /*
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

        LearningContext readiumContext = LearningContext.builder()
            .edApp(SoftwareApplication.builder()
                .id("https://github.com/readium/readium-js-viewer")
                //.context("http://purl.imsglobal.org/ctx/caliper/v1/edApp/epub-reader") // WARN: CaliperEntity prop
                .type("http://purl.imsglobal.org/caliper/v1/SoftwareApplication") // INFO: builder constructor will set this
                .lastModifiedAt(now.minus(Weeks.weeks(8)).getMillis())
                .build())
            .lisOrganization(americanHistoryCourse) // lisCourseSection?
            .agent(LISPerson.builder()
                .id("https://some-university.edu/students/jones-alice-554433")
                .lastModifiedAt(now.minus(Weeks.weeks(3)).getMillis())
                .build())
            .build();

        LearningContext courseSmartContext = LearningContext.builder()
            .edApp(SoftwareApplication.builder()
                .id("http://www.coursesmart.com/reader")
                //.context("http://purl.imsglobal.org/ctx/caliper/v1/edApp/epub-reader") // WARN: CaliperEntity prop
                .type("http://purl.imsglobal.org/caliper/v1/SoftwareApplication") // INFO: builder constructor will set this
                .lastModifiedAt(now.minus(Weeks.weeks(6)).getMillis())
                .build())
            .lisOrganization(americanHistoryCourse) // lisCourseSection?
            .agent(LISPerson.builder()
                .id("https://some-university.edu/students/jones-alice-554433")
                .lastModifiedAt(now.minus(Weeks.weeks(3)).getMillis())
                .build())
            .build();

        output.append(">> generated learning context data\n");

        // ---------------------------------------------------------------------
        // Step 2: Set up activity context elements (i.e. the two EPub Readings)
        // ---------------------------------------------------------------------
        /**
        EPubVolume readiumReading = new EPubVolume(
                "https://github.com/readium/readium-js-viewer/book/34843#epubcfi(/4/3)");
        readiumReading
                .setName("The Glorious Cause: The American Revolution, 1763-1789 (Oxford History of the United States)");
        readiumReading
                .setLastModifiedAt(now.minus(Weeks.weeks(53)).getMillis());
        */

        EPubVolume readiumVolume = EPubVolume.builder()
            .id("https://github.com/readium/readium-js-viewer/book/34843#epubcfi(/4/3)")
            .name("The Glorious Cause: The American Revolution, 1763-1789 (Oxford History of the United States)")
            .lastModifiedAt(now.minus(Weeks.weeks(53)).getMillis())
            .build();

        /**
         * Readium Profile
         */
        ReadingProfile readiumVolumeProfile = ReadingProfile.builder()
            .learningContext(readiumContext)
            .name("readium")
            .frame(readiumVolume)
            .navigatedFrom(WebPage.builder()
                    .id("AmRev-101-landingPage")
                    .name("American Revolution 101 Landing Page")
                    .partOf(americanHistoryCourse)
                    .build())
            //.target() // WARN: better as a CaliperEvent prop?
            //.generated() // WARN: better as a CaliperEvent prop?
            .build();

        /**
        EPubSubChapter readiumReadingPage1 = new EPubSubChapter(
                "https://github.com/readium/readium-js-viewer/book/34843#epubcfi(/4/3)/1");
        readiumReadingPage1.setName("Key Figures: George Washington)");
        readiumReadingPage1.setLastModifiedAt(now.minus(Weeks.weeks(53))
                .getMillis());
        readiumReadingPage1.setParentRef(readiumReading);
        */

        /**
        EPubSubChapter readiumReadingPage1 = EPubSubChapter.builder()
            .id("https://github.com/readium/readium-js-viewer/book/34843#epubcfi(/4/3)/1")
            .name("Key Figures: George Washington")
            .lastModifiedAt(now.minus(Weeks.weeks(53)).getMillis())
            .partOf((EPubVolume) readiumVolume)
            .build();
        */

        /**
         * Readium Page 01 Profile
         */
        ReadingProfile readiumPage01Profile = ReadingProfile.builder()
            .learningContext(readiumContext)
            .name("readium")
            .partOf(readiumVolume.getId())
            .frame(EPubSubChapter.builder()
                    .id("https://github.com/readium/readium-js-viewer/book/34843#epubcfi(/4/3)/1")
                    .name("Key Figures: George Washington")
                    .lastModifiedAt(now.minus(Weeks.weeks(53)).getMillis())
                    .partOf((EPubVolume) readiumVolume)
                    .build())
            .navigatedFrom(WebPage.builder()
                    .id("AmRev-101-landingPage")
                    .name("American Revolution 101 Landing Page")
                    .partOf(americanHistoryCourse)
                    .build())
            //.target() // WARN: better as a CaliperEvent prop?
            //.generated() // WARN: better as a CaliperEvent prop?
            .build();

        /**
        EPubSubChapter readiumReadingPage2 = new EPubSubChapter(
                "https://github.com/readium/readium-js-viewer/book/34843#epubcfi(/4/3)/2");
        readiumReadingPage2.setName("Key Figures: Lord Cornwalis)");
        readiumReadingPage2.setLastModifiedAt(now.minus(Weeks.weeks(53))
                .getMillis());
        readiumReadingPage2.setParentRef(readiumReading);
        */

        /**
        EPubSubChapter readiumReadingPage2 = EPubSubChapter.builder()
            .id("https://github.com/readium/readium-js-viewer/book/34843#epubcfi(/4/3)/2")
            .name("Key Figures: Lord Cornwallis")
            .lastModifiedAt(now.minus(Weeks.weeks(53)).getMillis())
            .partOf((EPubVolume) readiumVolume)
            .build();
        */

        /**
         * Readium Page 02 Profile
         */
        ReadingProfile readiumPage02Profile = ReadingProfile.builder()
            .learningContext(readiumContext)
            .name("readium")
            .partOf(readiumVolume.getId())
            .frame(EPubSubChapter.builder()
                    .id("https://github.com/readium/readium-js-viewer/book/34843#epubcfi(/4/3)/2")
                    .name("Key Figures: Lord Cornwallis")
                    .lastModifiedAt(now.minus(Weeks.weeks(53)).getMillis())
                    .partOf((EPubVolume) readiumVolume)
                    .build())
            .navigatedFrom(WebPage.builder()
                    .id("AmRev-101-landingPage")
                    .name("American Revolution 101 Landing Page")
                    .partOf(americanHistoryCourse)
                    .build())
            //.target() // WARN: better as a CaliperEvent prop?
            //.generated() // WARN: better as a CaliperEvent prop?
            .build();

        /**
         * Readium Page 02 Annotation Profile
         */
        AnnotationProfile readiumPage02AnnotationProfile = AnnotationProfile.builder()
            .learningContext(readiumContext)
            .target(readiumPage02Profile.getFrame())
                    //.generated() // WARN: better as a CaliperEvent prop?
            .build();

        /**
        EPubSubChapter readiumReadingPage3 = new EPubSubChapter(
                "https://github.com/readium/readium-js-viewer/book/34843#epubcfi(/4/3)/3");
        readiumReadingPage3.setName("Key Figures: Paul Revere");
        readiumReadingPage3.setLastModifiedAt(now.minus(Weeks.weeks(53))
                .getMillis());
        readiumReadingPage3.setParentRef(readiumReading);
        */

        /**
        EPubSubChapter readiumReadingPage3 = EPubSubChapter.builder()
            .id("https://github.com/readium/readium-js-viewer/book/34843#epubcfi(/4/3)/3")
            .name("Key Figures: Paul Revere")
            .lastModifiedAt(now.minus(Weeks.weeks(53)).getMillis())
            .partOf((EPubVolume) readiumVolume)
            .build();
        */

        /**
         * Readium Page 03 Profile
         */
        ReadingProfile readiumPage03Profile = ReadingProfile.builder()
            .learningContext(readiumContext)
            .name("readium")
            .partOf(readiumVolume.getId())
            .frame(EPubSubChapter.builder()
                    .id("https://github.com/readium/readium-js-viewer/book/34843#epubcfi(/4/3)/3")
                    .name("Key Figures: Paul Revere")
                    .lastModifiedAt(now.minus(Weeks.weeks(53)).getMillis())
                    .partOf((EPubVolume) readiumVolume)
                    .build())
            .navigatedFrom(WebPage.builder()
                    .id("AmRev-101-landingPage")
                    .name("American Revolution 101 Landing Page")
                    .partOf(americanHistoryCourse)
                    .build())
            //.target() // WARN: better as a CaliperEvent prop?
            //.generated() // WARN: better as a CaliperEvent prop?
            .build();

        /**
         * Readium Page 03 Annotation Profile
         */
        AnnotationProfile readiumPage03AnnotationProfile = AnnotationProfile.builder()
            .learningContext(readiumContext)
            .target(readiumPage03Profile.getFrame())
            //.generated() // WARN: better as a CaliperEvent prop?
            .build();

        /**
        EPubVolume courseSmartReading = new EPubVolume(
                "http://www.coursesmart.com/the-american-revolution-a-concise-history/robert-j-allison/dp/9780199347322");
        courseSmartReading
                .setName("The American Revolution: A Concise History | 978-0-19-531295-9");
        courseSmartReading.setLastModifiedAt(now.minus(Weeks.weeks(22))
                .getMillis());
        */

        EPubVolume courseSmartVolume = EPubVolume.builder()
            .id("http://www.coursesmart.com/the-american-revolution-a-concise-history/robert-j-allison/dp/9780199347322")
            .name("The American Revolution: A Concise History | 978-0-19-531295-9")
            .lastModifiedAt(now.minus(Weeks.weeks(22)).getMillis())
            .build();

        /**
         * CourseSmart Volume Profile
         */
        ReadingProfile courseSmartVolumeProfile = ReadingProfile.builder()
            .learningContext(courseSmartContext)
            .name("CourseSmart")
            //.partOf()
            //.objectType()
            //.alignedLearningObjective()
            //.keyword()
            .frame(courseSmartVolume)
            .navigatedFrom(WebPage.builder()
                    .id("AmRev-101-landingPage")
                    .name("American Revolution 101 Landing Page")
                    .partOf(americanHistoryCourse)
                    .build())
            //.target() // WARN: better as a CaliperEvent prop?
            //.generated() // WARN: better as a CaliperEvent prop?
            .build();

        /**
         EPubSubChapter courseSmartReadingPageaXfsadf12 = new EPubSubChapter(
         "http://www.coursesmart.com/the-american-revolution-a-concise-history/robert-j-allison/dp/9780199347322/aXfsadf12");
         courseSmartReadingPageaXfsadf12.setName("The Boston Tea Party");
         courseSmartReading.setLastModifiedAt(now.minus(Weeks.weeks(22))
         .getMillis());
         courseSmartReadingPageaXfsadf12.setParentRef(courseSmartReading);
         */

        /**
         EPubSubChapter courseSmartReadingPageaXfsadf12 = EPubSubChapter.builder()
         .id("http://www.coursesmart.com/the-american-revolution-a-concise-history/robert-j-allison/dp/9780199347322/aXfsadf12")
         .name("The Boston Tea Party")
         .lastModifiedAt(now.minus(Weeks.weeks(22)).getMillis())
         .partOf((EPubVolume) courseSmartReading)
         .build();
         */

        /**
         * CourseSmart Chapter Profile
         */
        ReadingProfile courseSmartSubChapterProfile = ReadingProfile.builder()
            .learningContext(courseSmartContext)
            .name("CourseSmart")
            //.partOf(courseSmartReading.getId())
            .frame(EPubSubChapter.builder()
                .id("http://www.coursesmart.com/the-american-revolution-a-concise-history/robert-j-allison/dp/9780199347322/aXfsadf12")
                .name("The Boston Tea Party")
                .lastModifiedAt(now.minus(Weeks.weeks(22)).getMillis())
                .partOf((EPubVolume) courseSmartVolume)
                .build())
            .navigatedFrom(WebPage.builder()
                .id("AmRev-101-landingPage")
                .name("American Revolution 101 Landing Page")
                .partOf(americanHistoryCourse)
                .build())
            //.target() // WARN: better as a CaliperEvent prop?
            //.generated() // WARN: better as a CaliperEvent prop?
            .build();


        /**
         * CourseSmart Annotation Profile
         */
        AnnotationProfile courseSmartAnnotationProfile = AnnotationProfile.builder()
            .learningContext(courseSmartContext)
            .target(courseSmartSubChapterProfile.getFrame()) // WARN: better as a CaliperEvent prop?
            //.generated() // WARN: better as a CaliperEvent prop?
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
        /**
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
        globalAppState.put("coursesmartReadingPageaXfsadf12", courseSmartReadingPageaXfsadf12);
        globalAppState.put("student", alice);
        */

        output.append(">> populated Event Generator\'s global state\n");

        // ----------------------------------------------------------------
        // Step 4: Execute reading sequence
        // ----------------------------------------------------------------
        output.append(">> sending events\n");

        // Event # 1 - NavigationEvent
        navigateToReading(readiumVolumeProfile);
        //navigateToReading(globalAppState, "readium");
        output.append(">>>>>> Navigated to Reading provided by Readium... sent NavigateEvent\n");

        // Event # 2 - ViewedEvent
        viewPageInReading(readiumPage01Profile);
        //viewPageInReading(globalAppState, "readium", "1");
        output.append(">>>>>> Viewed Page with pageId 1 in Readium Reading... sent ViewedEvent\n");

        // Event # 3 - ViewedEvent
        viewPageInReading(readiumPage02Profile);
        //viewPageInReading(globalAppState, "readium", "2");
        output.append(">>>>>> Viewed Page with pageId 2 in Readium Reading... sent ViewedEvent\n");

        // Event # 4 - HighlightedEvent
        highlightTermsInReading(readiumPage02AnnotationProfile, 455, 489, "Life, Liberty and the pursuit of Happiness");
        //highlightTermsInReading(globalAppState, "readium", "2", 455, 489);
        output.append(">>>>>> Highlighted fragment in pageId 2 from index 455 to 489 in Readium Reading... sent HighlightedEvent\n");

        // Event # 5 - Viewed Event
        viewPageInReading(readiumPage03Profile);
        //viewPageInReading(globalAppState, "readium", "3");
        output.append(">>>>>> Viewed Page with pageId 3 in Readium Reading... sent ViewedEvent\n");

        // Event # 6 - BookmarkedEvent
        bookmarkPageInReading(readiumPage03AnnotationProfile, "The Intolerable Acts (1774)--bad idea Lord North");
        //bookmarkPageInReading(globalAppState, "readium", "3");
        output.append(">>>>>> Bookmarked Page with pageId 3 in Readium Reading... sent BookmarkedEvent\n");

        // Event # 7 - NavigationEvent
        navigateToReading(courseSmartVolumeProfile);
        //navigateToReading(globalAppState, "coursesmart");
        output.append(">>>>>> Navigated to Reading provided by CourseSmart... sent NavigateEvent\n");

        // Event # 8 - ViewedEvent
        viewPageInReading(courseSmartSubChapterProfile);
        //viewPageInReading(globalAppState, "coursesmart", "aXfsadf12");
        output.append(">>>>>> Viewed Page with pageId aXfsadf12 in CourseSmart Reading... sent ViewedEvent\n");

        // Event # 9 - TaggedEvent
        tagPageInReading(courseSmartAnnotationProfile, Lists.newArrayList("to-read", "1776", "shared-with-project-team"));
        //tagPageInReading(courseSmartReadingProfile, "aXfsadf12", Lists.newArrayList("to-read", "1776", "shared-with-project-team"));
        output.append(">>>>>> Tagged Page with pageId aXfsadf12 with tags [to-read, 1776, shared-with-project-team] in CourseSmart Reading... sent TaggedEvent\n");

        // Event # 10 - SharedEvent
        sharePageInReading(courseSmartAnnotationProfile, Lists.newArrayList(
            "https://some-university.edu/students/smith-bob-554433",
            "https://some-university.edu/students/lam-eve-554433"));
        output.append(">>>>>> Shared Page with pageId aXfsadf12 with students [bob, eve] in CourseSmart Reading... sent SharedEvent\n");
    }

    // Methods below are utility methods for generating events... These are NOT
    // part of Caliper standards work and are here only as a utility in this
    // sample App

    private void navigateToReading(ReadingProfile profile) {
    // private void navigateToReading(HashMap<String, Object> globalAppState, String edApp) {

        /**
        NavigationEvent navEvent = new NavigationEvent();

        // INFO: readium app
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

    private void viewPageInReading(ReadingProfile profile) {
    // private void viewPageInReading(HashMap<String, Object> globalAppState, String edApp, String pageId) {

        /**
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

    private void highlightTermsInReading(AnnotationProfile profile, int startIndex, int endIndex, String selectionText) {
    // private void highlightTermsInReading(HashMap<String, Object> globalAppState, String edApp,
    //            String pageId, int startIndex, int endIndex) {

        /**
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
                (CaliperDigitalResource) globalAppState.get(edApp + "ReadingPage" + pageId)));

        // add (learning) context for event
        highlightTermsEvent.setEdApp((SoftwareApplication) globalAppState
                .get(edApp + "EdApp"));
        highlightTermsEvent.setLisOrganization((LISOrganization) globalAppState
                .get("currentCourse"));

        // set time and any event specific properties
        highlightTermsEvent.setStartedAt(DateTime.now().getMillis());
        */

        String baseUrl = "https://someEduApp.edu/highlights/";

        AnnotationEvent event = AnnotationEvent.builder()
            .edApp(profile.getLearningContext().getEdApp())
            .lisOrganization(profile.getLearningContext().getLisOrganization())
            .actor(profile.getLearningContext().getAgent())
            .action(AnnotationActions.HIGHLIGHTED.key())
            .object(profile.getTarget())
            .generated(HighlightAnnotation.builder()
                    .id(baseUrl + UUID.randomUUID().toString())
                    .selectionStart(Integer.toString(startIndex))
                    .selectionEnd(Integer.toString(endIndex))
                    .selectionText(selectionText)
                    .target(profile.getTarget())
                    .build())
            .startedAtTime(DateTime.now().getMillis())
            .build();

        // Send event to EventStore
        CaliperSensor.send(event);
    }

    /**
    private HighlightAnnotation getHighlight(int startIndex, int endIndex, String selectionText,
        CaliperDigitalResource target) {

        String baseUrl = "https://someEduApp.edu/highlights/";

        // HighlightAnnotation highlightAnnotation = new HighlightAnnotation(
        //         baseUrl + UUID.randomUUID().toString());
        // highlightAnnotation.getSelection().setStart(Integer.toString(startIndex));
        // highlightAnnotation.getSelection().setEnd(Integer.toString(endIndex));
        // highlightAnnotation.setSelectionText(selectionText);
        // highlightAnnotation.setTarget(target);

        HighlightAnnotation annotation = HighlightAnnotation.builder()
            .id(baseUrl + UUID.randomUUID().toString())
            .selectionStart(Integer.toString(startIndex))
            .selectionEnd(Integer.toString(endIndex))
            .selectionText(selectionText)
            .target(target)
            .build();

        return annotation;
    }
    */

    private void bookmarkPageInReading(AnnotationProfile profile, String bookmarkNotes) {

        /**
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
        */

        String baseUrl = "https://someEduApp.edu/bookmarks/";

        AnnotationEvent event = AnnotationEvent.builder()
            .edApp(profile.getLearningContext().getEdApp())
            .lisOrganization(profile.getLearningContext().getLisOrganization())
            .actor(profile.getLearningContext().getAgent())
            .action(AnnotationActions.BOOKMARKED.key())
            .object(profile.getTarget())
            .generated(BookmarkAnnotation.builder()
                    .id(baseUrl + UUID.randomUUID().toString())
                    .bookmarkNotes(bookmarkNotes)
                    .target(profile.getTarget())
                    .build())
            .startedAtTime(DateTime.now().getMillis())
            .build();

        // Send event to EventStore
        CaliperSensor.send(event);
    }

    /**
    private BookmarkAnnotation getBookmark(String bookmarkNotes, CaliperDigitalResource target) {

        String baseUrl = "https://someEduApp.edu/bookmarks/";

        // BookmarkAnnotation bookmarkAnnotation = new BookmarkAnnotation(baseUrl + UUID.randomUUID().toString());
        // bookmarkAnnotation.setTarget(target);

        BookmarkAnnotation annotation = BookmarkAnnotation.builder()
            .id(baseUrl + UUID.randomUUID().toString())
            .bookmarkNotes(bookmarkNotes)
            .target(target)
            .build();

        return bookmarkAnnotation;
    }
    */

    private void tagPageInReading(AnnotationProfile profile, List<String> tags) {
    // private void tagPageInReading(HashMap<String, Object> globalAppState,
    //            String edApp, String pageId, List<String> tags) {

        /**
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
        */

        String baseUrl = "https://someEduApp.edu/tags/";

        AnnotationEvent event = AnnotationEvent.builder()
            .edApp(profile.getLearningContext().getEdApp())
            .lisOrganization(profile.getLearningContext().getLisOrganization())
            .actor(profile.getLearningContext().getAgent())
            .action(AnnotationActions.TAGGED.key())
            .object(profile.getTarget())
            .generated(TagAnnotation.builder()
                .id(baseUrl + UUID.randomUUID().toString())
                .tags(tags)
                .target(profile.getTarget())
                .build())
            .startedAtTime(DateTime.now().getMillis())
            .build();

        // Send event to EventStore
        CaliperSensor.send(event);
    }

    /**
    private TagAnnotation getTag(List<String> tags, CaliperDigitalResource target) {

        String baseUrl = "https://someEduApp.edu/tags/";

        // TagAnnotation tagAnnotation = new TagAnnotation(baseUrl + UUID.randomUUID().toString());
        // tagAnnotation.setTags(tags);
        // tagAnnotation.setTarget(target);

        TagAnnotation annotation = TagAnnotation.builder()
            .id(baseUrl + UUID.randomUUID().toString())
            .tags(tags)
            .target(target)
            .build();

        return annotation;
    }
    */

    private void sharePageInReading(AnnotationProfile profile, List<String> sharedWithIds) {

        /**
        AnnotationEvent sharePageEvent = AnnotationEvent.forAction("shared");

        // action is set in navEvent constructor... now set actor and object
        sharePageEvent.setActor((LISPerson) globalAppState.get("student"));
        sharePageEvent.setObject((CaliperDigitalResource) globalAppState
                .get(edApp + "ReadingPage" + pageId));

        // tag create action generates a SharedAnnotation
        sharePageEvent.setGenerated(getShareAnnotation(
                sharedWithIds,
                (CaliperDigitalResource) globalAppState.get(edApp + "ReadingPage" + pageId)));

        // add (learning) context for event
        sharePageEvent.setEdApp((SoftwareApplication) globalAppState.get(edApp
                + "EdApp"));
        sharePageEvent.setLisOrganization((LISOrganization) globalAppState
                .get("currentCourse"));

        // set time and any event specific properties
        sharePageEvent.setStartedAt(DateTime.now().getMillis());
        */

        String baseUrl = "https://someBookmarkingApp.edu/shares/";

        AnnotationEvent event = AnnotationEvent.builder()
            .edApp(profile.getLearningContext().getEdApp())
            .lisOrganization(profile.getLearningContext().getLisOrganization())
            .actor(profile.getLearningContext().getAgent())
            .action(AnnotationActions.SHARED.key())
            .object(profile.getTarget())
            .generated(SharedAnnotation.builder()
                    .id(baseUrl + UUID.randomUUID().toString())
                    .withAgents(sharedWithIds)
                    .target(profile.getTarget())
                    .build())
            .startedAtTime(DateTime.now().getMillis())
            .build();

        // Send event to EventStore
        CaliperSensor.send(event);
    }

    /**
    private SharedAnnotation getShareAnnotation(List<String> sharedWithIds, CaliperDigitalResource target) {

        String baseUrl = "https://someBookmarkingApp.edu/shares/";

        // SharedAnnotation sharedAnnotation = new SharedAnnotation(baseUrl + UUID.randomUUID().toString());
        // sharedAnnotation.withAgents(sharedWithIds);
        // sharedAnnotation.setTarget(target);

        SharedAnnotation annotation = SharedAnnotation.builder()
            .id(baseUrl + UUID.randomUUID().toString())
            .withAgents(sharedWithIds)
            .target(target)
            .build();

        return annotation;
    }
    */

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