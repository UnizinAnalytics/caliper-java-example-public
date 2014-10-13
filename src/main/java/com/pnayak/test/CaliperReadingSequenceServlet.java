package com.pnayak.test;

import com.google.common.collect.Lists;
import org.imsglobal.caliper.CaliperSensor;
import org.imsglobal.caliper.Options;
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
        // Step 1: Set up contextual elements (i.e. the two EPub Readings)
        // ----------------------------------------------------------------

        // LISCourseSection context. NOTE - we would want to associate it with a
        // parent Department or Institution at some point.

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

        // Assemble the Activity Context.
        ReadingProfile readiumProfile = ReadingProfile.builder()
            .learningContext(LearningContext.builder()
                .edApp(SoftwareApplication.builder()
                    .id("https://github.com/readium/readium-js-viewer")
                    //.context("http://purl.imsglobal.org/ctx/caliper/v1/edApp/epub-reader") // WARN: CaliperEntity prop
                    //.type("http://purl.imsglobal.org/caliper/v1/SoftwareApplication") // INFO:
                    .lastModifiedAt(now.minus(Weeks.weeks(8)).getMillis())
                    .build())
                .lisOrganization(americanHistoryCourse)
                .agent(LISPerson.builder()
                    .id("https://some-university.edu/students/jones-alice-554433")
                    .lastModifiedAt(now.minus(Weeks.weeks(3)).getMillis())
                    .build())
                .build())
            .name("readium")
            .frame(EPubVolume.builder()
                .id("https://github.com/readium/readium-js-viewer/book/34843#epubcfi(/4/3)")
                .name("The Glorious Cause: The American Revolution, 1763-1789 (Oxford History of the United States)")
                .lastModifiedAt(now.minus(Weeks.weeks(53)).getMillis())
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
         * CourseSmart Volume Profile
         */
        ReadingProfile courseSmartProfile = ReadingProfile.builder()
            .learningContext(LearningContext.builder()
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
                .build())
            .name("CourseSmart")
            .frame(EPubSubChapter.builder()
                .id("http://www.coursesmart.com/the-american-revolution-a-concise-history/robert-j-allison/dp/9780199347322/aXfsadf12")
                .name("The Boston Tea Party")
                .lastModifiedAt(now.minus(Weeks.weeks(22)).getMillis())
                .partOf((EPubVolume) EPubVolume.builder()
                    .id("http://www.coursesmart.com/the-american-revolution-a-concise-history/robert-j-allison/dp/9780199347322")
                    .name("The American Revolution: A Concise History | 978-0-19-531295-9")
                    .lastModifiedAt(now.minus(Weeks.weeks(22)).getMillis())
                    .build())
                .build())
            .navigatedFrom(WebPage.builder()
                .id("AmRev-101-landingPage")
                .name("American Revolution 101 Landing Page")
                .partOf(americanHistoryCourse)
                .build())
            .build();

        output.append(">> generated learning context and activity context data\n");

        // ----------------------------------------------------------------
        // Step 3: Execute reading sequence
        // ----------------------------------------------------------------
        output.append(">> sending events\n");

        // Event # 1 - NavigationEvent to #epubcfi(/4/3)
        navigateToReading(readiumProfile);
        //navigateToReading(globalAppState, "readium");
        output.append(">>>>>> Navigated to Reading provided by Readium... sent NavigateEvent\n");

        // Event # 2 - ViewedEvent #epubcfi(/4/3)/1 (George Washington)

        /**
         EPubSubChapter readiumReadingPage1 = new EPubSubChapter(
         "https://github.com/readium/readium-js-viewer/book/34843#epubcfi(/4/3)/1");
         readiumReadingPage1.setName("Key Figures: George Washington)");
         readiumReadingPage1.setLastModifiedAt(now.minus(Weeks.weeks(53))
         .getMillis());
         readiumReadingPage1.setParentRef(readiumReading);
         */

        // Add new frame / navigatedFrom
        readiumProfile.getFrames().add(EPubSubChapter.builder()
            .id("https://github.com/readium/readium-js-viewer/book/34843#epubcfi(/4/3)/1")
            .name("Key Figures: George Washington")
            .lastModifiedAt(now.minus(Weeks.weeks(53)).getMillis())
            .partOf((EPubVolume) readiumProfile.getFrames().get(0))
            .build());
        readiumProfile.getNavigatedFroms().add(readiumProfile.getFrames().get(0));

        viewPageInReading(readiumProfile);
        //viewPageInReading(globalAppState, "readium", "1");
        output.append(">>>>>> Viewed Page with pageId 1 in Readium Reading... sent ViewedEvent\n");

        // Event # 3 - ViewedEvent #epubcfi(/4/3)/2 (Lord Cornwallis)

        /**
         EPubSubChapter readiumReadingPage2 = new EPubSubChapter(
         "https://github.com/readium/readium-js-viewer/book/34843#epubcfi(/4/3)/2");
         readiumReadingPage2.setName("Key Figures: Lord Cornwallis)");
         readiumReadingPage2.setLastModifiedAt(now.minus(Weeks.weeks(53))
         .getMillis());
         readiumReadingPage2.setParentRef(readiumReading);
         */

        // Add new frame / navigatedFrom
        readiumProfile.getFrames().add(EPubSubChapter.builder()
            .id("https://github.com/readium/readium-js-viewer/book/34843#epubcfi(/4/3)/2")
            .name("Key Figures: Lord Cornwallis")
            .lastModifiedAt(now.minus(Weeks.weeks(53)).getMillis())
            .partOf((EPubVolume) readiumProfile.getFrames().get(0))
            .build());
        readiumProfile.getNavigatedFroms()
            .add(readiumProfile.getFrames().get(readiumProfile.getFrames().size() - 2));

        viewPageInReading(readiumProfile);
        //viewPageInReading(globalAppState, "readium", "2");
        output.append(">>>>>> Viewed Page with pageId 2 in Readium Reading... sent ViewedEvent\n");

        // Event # 4 - HighlightedEvent
        AnnotationProfile readiumAnnotationProfile = AnnotationProfile.builder()
            .learningContext(readiumProfile.getLearningContext())
            .annotation(HighlightAnnotation.builder()
                .id("https://someEduApp.edu/highlights/" + UUID.randomUUID().toString())
                .selectionStart(Integer.toString(455))
                .selectionEnd(Integer.toString(489))
                .selectionText("Life, Liberty and the pursuit of Happiness")
                .target(readiumProfile.getFrames().get(readiumProfile.getFrames().size() - 1))
                .build())
            .target(readiumProfile.getFrames().get(readiumProfile.getFrames().size() - 1))
            .build();

        highlightTermsInReading(readiumAnnotationProfile);
        //highlightTermsInReading(readiumAnnotationProfile, 455, 489, "Life, Liberty and the pursuit of Happiness");
        //highlightTermsInReading(globalAppState, "readium", "2", 455, 489);
        output.append(">>>>>> Highlighted fragment in pageId 2 from index 455 to 489 in Readium Reading... sent HighlightedEvent\n");

        // Event # 5 - Viewed Event #epubcfi(/4/3)/3 (Paul Revere)

        /**
         EPubSubChapter readiumReadingPage3 = new EPubSubChapter(
         "https://github.com/readium/readium-js-viewer/book/34843#epubcfi(/4/3)/3");
         readiumReadingPage3.setName("Key Figures: Paul Revere");
         readiumReadingPage3.setLastModifiedAt(now.minus(Weeks.weeks(53)).getMillis());
         readiumReadingPage3.setParentRef(readiumReading);
         */

        // Add new frame / navigatedFrom
        readiumProfile.getFrames().add(EPubSubChapter.builder()
            .id("https://github.com/readium/readium-js-viewer/book/34843#epubcfi(/4/3)/3")
            .name("Key Figures: Paul Revere")
            .lastModifiedAt(now.minus(Weeks.weeks(53)).getMillis())
            .partOf((EPubVolume) readiumProfile.getFrames().get(0))
            .build());
        readiumProfile.getNavigatedFroms()
            .add(readiumProfile.getFrames().get(readiumProfile.getFrames().size() - 2));

        viewPageInReading(readiumProfile);
        //viewPageInReading(globalAppState, "readium", "3");
        output.append(">>>>>> Viewed Page with pageId 3 in Readium Reading... sent ViewedEvent\n");

        // Event # 6 - BookmarkedEvent
        readiumAnnotationProfile.getAnnotations().add(BookmarkAnnotation.builder()
            .id("https://someEduApp.edu/bookmarks/" + UUID.randomUUID().toString())
            .bookmarkNotes("The Intolerable Acts (1774)--bad idea Lord North")
            .target(readiumProfile.getFrames().get(readiumProfile.getFrames().size() - 1))
            .build());
        readiumAnnotationProfile.getTargets()
            .add(readiumProfile.getFrames().get(readiumProfile.getFrames().size() - 1));

        bookmarkPageInReading(readiumAnnotationProfile);
        //bookmarkPageInReading(globalAppState, "readium", "3");
        output.append(">>>>>> Bookmarked Page with pageId 3 in Readium Reading... sent BookmarkedEvent\n");

        // Event # 7 - NavigationEvent
        navigateToReading(courseSmartProfile);
        //navigateToReading(globalAppState, "coursesmart");
        output.append(">>>>>> Navigated to Reading provided by CourseSmart... sent NavigateEvent\n");

        // Event # 8 - ViewedEvent aXfsadf12
        viewPageInReading(courseSmartProfile);
        //viewPageInReading(globalAppState, "coursesmart", "aXfsadf12");
        output.append(">>>>>> Viewed Page with pageId aXfsadf12 in CourseSmart Reading... sent ViewedEvent\n");

        // Event # 9 - TaggedEvent
        AnnotationProfile courseSmartAnnotationProfile = AnnotationProfile.builder()
            .learningContext(courseSmartProfile.getLearningContext())
            .annotation((TagAnnotation) TagAnnotation.builder()
                .id("https://someEduApp.edu/tags/" + UUID.randomUUID().toString())
                .tags(Lists.newArrayList("to-read", "1776", "shared-with-project-team"))
                .target(courseSmartProfile.getFrames().get(courseSmartProfile.getFrames().size() - 1))
                .build())
            .target(courseSmartProfile.getFrames().get(courseSmartProfile.getFrames().size() - 1))
            .build();

        tagPageInReading(courseSmartAnnotationProfile);
        //tagPageInReading(courseSmartReadingProfile, "aXfsadf12", Lists.newArrayList("to-read", "1776", "shared-with-project-team"));
        output.append(">>>>>> Tagged Page with pageId aXfsadf12 with tags [to-read, 1776, shared-with-project-team] in CourseSmart Reading... sent TaggedEvent\n");

        // Event # 10 - SharedEvent
        courseSmartAnnotationProfile.getAnnotations().add((SharedAnnotation) SharedAnnotation.builder()
            .id("https://someEduApp.edu/tags/" + UUID.randomUUID().toString())
            .withAgents(Lists.newArrayList(
                "https://some-university.edu/students/smith-bob-554433",
                "https://some-university.edu/students/lam-eve-554433"))
            .target(courseSmartProfile.getFrames().get(courseSmartProfile.getFrames().size() - 1))
            .build());

        sharePageInReading(courseSmartAnnotationProfile);
        output.append(">>>>>> Shared Page with pageId aXfsadf12 with students [bob, eve] in CourseSmart Reading... sent SharedEvent\n");
    }

    /*
      --------------------------------------------------------------------------------
      Methods below are utility methods for generating events. These are NOT
      part of Caliper standards work and are here only as a utility in this sample App.
      ---------------------------------------------------------------------------------
     */

    private void navigateToReading(ReadingProfile profile) {

        NavigationEvent event = NavigationEvent.builder()
            .edApp(profile.getLearningContext().getEdApp())
            .lisOrganization(profile.getLearningContext().getLisOrganization())
            .actor(profile.getLearningContext().getAgent())
            .action(ReadingProfile.ReadingAction.NAVIGATEDTO.key())
            .object(profile.getFrames().get(profile.getFrames().size() - 1))
            .fromResource((CaliperDigitalResource) profile.getNavigatedFroms().get(profile.getFrames().size() - 1))
            .startedAtTime(DateTime.now().getMillis())
            .build();

        // Send event to EventStore
        CaliperSensor.send(event);
    }

    private void viewPageInReading(ReadingProfile profile) {

        ViewedEvent event = ViewedEvent.builder()
            .edApp(profile.getLearningContext().getEdApp())
            .lisOrganization(profile.getLearningContext().getLisOrganization())
            .actor(profile.getLearningContext().getAgent())
            .action(ReadingProfile.ReadingAction.VIEWED.key())
            .object(profile.getFrames().get(profile.getFrames().size() - 1))
            .startedAtTime(DateTime.now().getMillis())
            .duration("PT" + randomSecsDurationBetween(5, 120) + "S")
            .build();

        // Send event to EventStore
        CaliperSensor.send(event);
    }

    private void highlightTermsInReading(AnnotationProfile profile) {

        AnnotationEvent event = AnnotationEvent.builder()
            .edApp(profile.getLearningContext().getEdApp())
            .lisOrganization(profile.getLearningContext().getLisOrganization())
            .actor(profile.getLearningContext().getAgent())
            .action(AnnotationProfile.AnnotationAction.HIGHLIGHTED.key())
            .object(profile.getTargets().get(profile.getTargets().size() - 1))
            .generated((HighlightAnnotation) profile.getAnnotations().get(profile.getAnnotations().size() - 1))
            .startedAtTime(DateTime.now().getMillis())
            .build();

        // Send event to EventStore
        CaliperSensor.send(event);
    }

    private void bookmarkPageInReading(AnnotationProfile profile) {

        AnnotationEvent event = AnnotationEvent.builder()
            .edApp(profile.getLearningContext().getEdApp())
            .lisOrganization(profile.getLearningContext().getLisOrganization())
            .actor(profile.getLearningContext().getAgent())
            .action(AnnotationProfile.AnnotationAction.BOOKMARKED.key())
            .object(profile.getTargets().get(profile.getTargets().size() - 1))
            .generated(profile.getAnnotations().get(profile.getAnnotations().size() - 1))
            .startedAtTime(DateTime.now().getMillis())
            .build();

        // Send event to EventStore
        CaliperSensor.send(event);
    }

    private void tagPageInReading(AnnotationProfile profile) {

        AnnotationEvent event = AnnotationEvent.builder()
            .edApp(profile.getLearningContext().getEdApp())
            .lisOrganization(profile.getLearningContext().getLisOrganization())
            .actor(profile.getLearningContext().getAgent())
            .action(AnnotationProfile.AnnotationAction.TAGGED.key())
            .object(profile.getTargets().get(profile.getTargets().size() - 1))
            .generated(profile.getAnnotations().get(profile.getAnnotations().size() - 1))
            .startedAtTime(DateTime.now().getMillis())
            .build();

        // Send event to EventStore
        CaliperSensor.send(event);
    }

    private void sharePageInReading(AnnotationProfile profile) {

        AnnotationEvent event = AnnotationEvent.builder()
            .edApp(profile.getLearningContext().getEdApp())
            .lisOrganization(profile.getLearningContext().getLisOrganization())
            .actor(profile.getLearningContext().getAgent())
            .action(AnnotationProfile.AnnotationAction.SHARED.key())
            .object(profile.getTargets().get(profile.getTargets().size() - 1))
            .generated(profile.getAnnotations().get(profile.getAnnotations().size() - 1))
            .startedAtTime(DateTime.now().getMillis())
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
        return r.nextInt((end-start) + start);
    }
}