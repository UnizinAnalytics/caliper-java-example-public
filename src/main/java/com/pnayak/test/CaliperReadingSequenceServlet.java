package com.pnayak.test;

import com.google.common.collect.Iterables;
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
    public CaliperReadingSequenceServlet() {
        initialize();
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
     *      response)
     */
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response) throws ServletException, IOException {

        // Invoke the Caliper sensor, send a set of Caliper Events
        // StringBuffer output = new StringBuffer();

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

    /**
     * Reading Sequence. Student in a course interacts with two readings from two edApps.
     * In the process of interacting with the apps, she performs various reading and
     * annotation activities. These are defined in the Caliper Reading and Annotation profiles respectively.
     *
     * TODO LISCourseSection context. Associate it with a parent Department or Institution at some point.
     * TODO LISCourseSection does not define a section property.  Oversight?
     *
     * @param output
     */
    private void generateReadingAnnotationEventSequence(StringBuffer output) {

        // For reference, the current time
        DateTime now = DateTime.now();

        /*
         * -----------------------------------------------------------------
         * Step 01.  Set the activity context for the two readings.
         * -----------------------------------------------------------------
         */

        // Course Section (part of the learning context)
        LISCourseSection americanHistoryCourse = LISCourseSection.builder()
            .id("https://some-university.edu/politicalScience/2014/american-revolution-101")
            .semester("Spring-2014")
            .courseNumber("AmRev-101")
            .label("Am Rev 101")
            .title("American Revolution 101")
            .lastModifiedAt(now.minus(Weeks.weeks(4)).getMillis())
            .build();

        // Readium Profile
        ReadingProfile readiumProfile = ReadingProfile.builder()
            .learningContext(LearningContext.builder()
                .edApp(SoftwareApplication.builder()
                    .id("https://github.com/readium/readium-js-viewer")
                    .lastModifiedAt(now.minus(Weeks.weeks(8)).getMillis())
                    .build())
                .lisOrganization(americanHistoryCourse)
                .agent(LISPerson.builder()
                    .id("https://some-university.edu/students/jones-alice-554433")
                    .lastModifiedAt(now.minus(Weeks.weeks(3)).getMillis())
                    .build())
                .build())
            //.target() // WARN: better as a CaliperEvent prop?
            //.generated() // WARN: better as a CaliperEvent prop?
            .build();

        // CourseSmart Profile
        ReadingProfile courseSmartProfile = ReadingProfile.builder()
            .learningContext(LearningContext.builder()
                .edApp(SoftwareApplication.builder()
                    .id("http://www.coursesmart.com/reader")
                    .lastModifiedAt(now.minus(Weeks.weeks(6)).getMillis())
                    .build())
                .lisOrganization(americanHistoryCourse)
                .agent(LISPerson.builder()
                    .id("https://some-university.edu/students/jones-alice-554433")
                    .lastModifiedAt(now.minus(Weeks.weeks(3)).getMillis())
                    .build())
                .build())
            //.target() // WARN: better as a CaliperEvent prop?
            //.generated() // WARN: better as a CaliperEvent prop?
            .build();

        output.append("Generated activity context data.\n\n");

        /*
         * -----------------------------------------------------------------
         * Step 02.  Execute reading and annotation sequence.
         * -----------------------------------------------------------------
         */

        output.append("Sending events . . .\n\n");

        // EVENT 01 - Add NavigationEvent to #epubcfi(/4/3)
        readiumProfile.getActions().add(ReadingActions.NAVIGATED_TO.key());
        readiumProfile.getFrames().add(EPubVolume.builder()
            .id("https://github.com/readium/readium-js-viewer/book/34843#epubcfi(/4/3)")
            .name("The Glorious Cause: The American Revolution, 1763-1789 (Oxford History of the United States)")
            .lastModifiedAt(now.minus(Weeks.weeks(53)).getMillis())
            .build());
        readiumProfile.getNavigatedFroms().add(WebPage.builder()
            .id("AmRev-101-landingPage")
            .name("American Revolution 101 Landing Page")
            .partOf(americanHistoryCourse)
            .build());

        output.append("Navigated to chapter in Readium Reader . . . sent NavigatedEvent\n");

        // Process Event
        navigate(readiumProfile);

        output.append("Object : " + ((EPubVolume) Iterables.getLast(readiumProfile.getFrames())).getId() + "\n\n");

        // EVENT # 2 - Add ViewedEvent #epubcfi(/4/3)/1 (George Washington)
        readiumProfile.getActions().add(ReadingActions.VIEWED.key());
        readiumProfile.getFrames().add(EPubSubChapter.builder()
            .id("https://github.com/readium/readium-js-viewer/book/34843#epubcfi(/4/3)/1")
            .name("Key Figures: George Washington")
            .lastModifiedAt(now.minus(Weeks.weeks(53)).getMillis())
            .partOf((EPubVolume) Iterables.getFirst(readiumProfile.getFrames(), null))
            .build());
        readiumProfile.getNavigatedFroms().add(Iterables.getFirst(readiumProfile.getFrames(), null));

        output.append("Viewed Page with pageId 1 in Readium Reader . . . sent ViewedEvent\n");

        // Process event
        view(readiumProfile);

        output.append("Object : " + ((EPubSubChapter) Iterables.getLast(readiumProfile.getFrames())).getId() + "\n\n");

        // EVENT # 3 - Add ViewedEvent #epubcfi(/4/3)/2 (Lord Cornwallis)
        readiumProfile.getActions().add(ReadingActions.VIEWED.key());
        readiumProfile.getFrames().add(EPubSubChapter.builder()
            .id("https://github.com/readium/readium-js-viewer/book/34843#epubcfi(/4/3)/2")
            .name("Key Figures: Lord Cornwallis")
            .lastModifiedAt(now.minus(Weeks.weeks(53)).getMillis())
            .partOf((EPubVolume) Iterables.getFirst(readiumProfile.getFrames(), null))
            .build());
        readiumProfile.getNavigatedFroms()
            .add(readiumProfile.getFrames().get(readiumProfile.getFrames().size() - 2));

        output.append("Viewed Page with pageId 2 in Readium Reader . . . sent ViewedEvent\n");

        // Process Event
        view(readiumProfile);

        output.append("Object : " + ((EPubSubChapter) Iterables.getLast(readiumProfile.getFrames())).getId() + "\n\n");

        // EVENT # 4 - HighlightedEvent
        AnnotationProfile readiumAnnotationProfile = AnnotationProfile.builder()
            .learningContext(readiumProfile.getLearningContext())
            .action(AnnotationActions.HIGHLIGHTED.key())
            .annotation(HighlightAnnotation.builder()
                .id("https://someEduApp.edu/highlights/" + UUID.randomUUID().toString())
                .selectionStart(Integer.toString(455))
                .selectionEnd(Integer.toString(489))
                .selectionText("Life, Liberty and the pursuit of Happiness")
                .target(Iterables.getLast(readiumProfile.getFrames()))
                .build())
            .target(Iterables.getLast(readiumProfile.getFrames()))
            .build();

        output.append("Highlighted fragment in pageId 2 from index 455 to 489 in Readium Reading . . . sent HighlightedEvent\n");

        // Process Event
        annotate(readiumAnnotationProfile);

        HighlightAnnotation highlight = (HighlightAnnotation) Iterables.getLast(readiumAnnotationProfile.getAnnotations());
        output.append("Object : " + highlight.getId()  + "\n");
        output.append("Target : " + ((EPubSubChapter) highlight.getTarget()).getId() + "\n\n");

        // EVENT # 5 - Add Viewed Event #epubcfi(/4/3)/3 (Paul Revere)
        readiumProfile.getActions().add(ReadingActions.VIEWED.key());
        readiumProfile.getFrames().add(EPubSubChapter.builder()
            .id("https://github.com/readium/readium-js-viewer/book/34843#epubcfi(/4/3)/3")
            .name("Key Figures: Paul Revere")
            .lastModifiedAt(now.minus(Weeks.weeks(53)).getMillis())
            .partOf((EPubVolume) Iterables.getFirst(readiumProfile.getFrames(), null))
            .build());
        readiumProfile.getNavigatedFroms()
            .add(readiumProfile.getFrames().get(readiumProfile.getFrames().size() - 2));

        output.append("Viewed Page with pageId 3 in Readium Reading . . . sent ViewedEvent\n");

        // Process Event
        view(readiumProfile);

        output.append("Object : " + ((EPubSubChapter) Iterables.getLast(readiumProfile.getFrames())).getId() + "\n\n");

        // EVENT # 6 - Add BookmarkedEvent
        readiumAnnotationProfile.getActions().add(AnnotationActions.BOOKMARKED.key());
        readiumAnnotationProfile.getAnnotations().add(BookmarkAnnotation.builder()
            .id("https://someEduApp.edu/bookmarks/" + UUID.randomUUID().toString())
            .bookmarkNotes("The Intolerable Acts (1774)--bad idea Lord North")
            .target(Iterables.getLast(readiumProfile.getFrames()))
            .build());
        readiumAnnotationProfile.getTargets()
            .add(Iterables.getLast(readiumProfile.getFrames())); // WARN: DO REALLY NEED target in BASE PROFILE?

        output.append("Bookmarked Page with pageId 3 in Readium Reading . . . sent BookmarkedEvent\n");

        // Process Event
        annotate(readiumAnnotationProfile);

        BookmarkAnnotation bookmark = (BookmarkAnnotation) Iterables.getLast(readiumAnnotationProfile.getAnnotations());
        output.append("Object : " + bookmark.getId()  + "\n");
        output.append("Target : " + ((EPubSubChapter) highlight.getTarget()).getId() + "\n\n");

        // EVENT # 7 - Add NavigationEvent
        courseSmartProfile.getActions().add(ReadingActions.NAVIGATED_TO.key());
        courseSmartProfile.getFrames().add(EPubSubChapter.builder()
            .id("http://www.coursesmart.com/the-american-revolution-a-concise-history/robert-j-allison/dp/9780199347322/aXfsadf12")
            .name("The Boston Tea Party")
            .lastModifiedAt(now.minus(Weeks.weeks(22)).getMillis())
            .partOf((EPubVolume) EPubVolume.builder()
                .id("http://www.coursesmart.com/the-american-revolution-a-concise-history/robert-j-allison/dp/9780199347322")
                .name("The American Revolution: A Concise History | 978-0-19-531295-9")
                .lastModifiedAt(now.minus(Weeks.weeks(22)).getMillis())
                .build())
            .build());
        courseSmartProfile.getNavigatedFroms().add(WebPage.builder()
            .id("AmRev-101-landingPage")
            .name("American Revolution 101 Landing Page")
            .partOf(americanHistoryCourse)
            .build());

        output.append("Navigated to Reading provided by CourseSmart . . . sent NavigateEvent\n");

        // Process event
        navigate(courseSmartProfile);

        output.append("Object : " + ((EPubSubChapter) Iterables.getLast(readiumProfile.getFrames())).getId() + "\n\n");

        // EVENT # 8 - Add ViewedEvent aXfsadf12
        courseSmartProfile.getActions().add(ReadingActions.VIEWED.key());

        output.append("Viewed CourseSmart Reading Page with pageId aXfsadf12 ... sent ViewedEvent\n");

        // Process event
        view(courseSmartProfile);

        output.append("Object : " + ((EPubSubChapter) Iterables.getLast(readiumProfile.getFrames())).getId() + "\n\n");

        // EVENT # 9 - Add TaggedEvent
        AnnotationProfile courseSmartAnnotationProfile = AnnotationProfile.builder()
            .learningContext(courseSmartProfile.getLearningContext())
            .action(AnnotationActions.TAGGED.key())
            .annotation((TagAnnotation) TagAnnotation.builder()
                .id("https://someEduApp.edu/tags/" + UUID.randomUUID().toString())
                .tags(Lists.newArrayList("to-read", "1776", "shared-with-project-team"))
                .target(Iterables.getLast(courseSmartProfile.getFrames()))
                .build())
            .target(Iterables.getLast(courseSmartProfile.getFrames())) // WARN: DO REALLY NEED target in BASE PROFILE?
            .build();

        output.append("Tagged CourseSmart Reading Page with pageId aXfsadf12 . . . sent TaggedEvent\n");

        // Process event
        annotate(courseSmartAnnotationProfile);

        TagAnnotation tag = (TagAnnotation) Iterables.getLast(courseSmartAnnotationProfile.getAnnotations());
        output.append("Object : " + tag.getId()  + "\n");
        output.append("Tags : " + tag.getTags().toString() + "\n");
        output.append("Target : " + ((EPubSubChapter) tag.getTarget()).getId() + "\n\n");

        // EVENT # 10 - Add SharedEvent
        courseSmartAnnotationProfile.getActions().add(AnnotationActions.SHARED.key());
        courseSmartAnnotationProfile.getAnnotations().add((SharedAnnotation) SharedAnnotation.builder()
            .id("https://someEduApp.edu/tags/" + UUID.randomUUID().toString())
            .withAgents(Lists.newArrayList(
                "https://some-university.edu/students/smith-bob-554433",
                "https://some-university.edu/students/lam-eve-554433"))
            .target(Iterables.getLast(courseSmartProfile.getFrames()))
            .build());

        output.append("Shared CourseSmart Reading Page with pageId aXfsadf12 with students . . . sent SharedEvent\n");

        // Process event
        annotate(courseSmartAnnotationProfile);

        SharedAnnotation shared = (SharedAnnotation) Iterables.getLast(courseSmartAnnotationProfile.getAnnotations());
        output.append("Object : " + shared.getId() + "\n");
        output.append("Shared : " + shared.getWithAgents().toString() + "\n");
        output.append("Target : " + ((EPubSubChapter) shared.getTarget()).getId() + "\n\n");
        output.append("FINIS\n\n");
    }

    /*
      --------------------------------------------------------------------------------
      Methods below are utility methods for generating events. These are NOT
      part of Caliper standards work and are here only as a utility in this sample App.
      ---------------------------------------------------------------------------------
     */

    private void annotate(AnnotationProfile profile) {

        // Note: may need to re-implement separate annotate methods
        // to allow for casting generated to the various object and annotation types,
        // e.g., .generated((TagAnnotation) profile.getAnnotations().get(profile.getAnnotations().size() - 1))
        AnnotationEvent event = AnnotationEvent.builder()
            .edApp(profile.getLearningContext().getEdApp())
            .lisOrganization(profile.getLearningContext().getLisOrganization())
            .actor(profile.getLearningContext().getAgent())
            .action(Iterables.getLast(profile.getActions()))
            .object(Iterables.getLast(profile.getTargets()))
            .generated(Iterables.getLast(profile.getAnnotations()))
            .startedAtTime(DateTime.now().getMillis()) // Pass this value in?
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
            .object(Iterables.getLast(profile.getFrames()))
            .fromResource((CaliperDigitalResource) Iterables.getLast(profile.getNavigatedFroms()))
            .startedAtTime(DateTime.now().getMillis())  // Pass this value in?
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
            .object(Iterables.getLast(profile.getFrames()))
            .startedAtTime(DateTime.now().getMillis()) // Pass this value in?
            .duration("PT" + randomSecsDurationBetween(5, 120) + "S") // Pass this value in?
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
        return r.nextInt((end-start) + start);
    }
}