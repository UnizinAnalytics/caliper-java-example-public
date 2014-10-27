package com.pnayak.test;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import org.imsglobal.caliper.Sensor;
import org.imsglobal.caliper.Options;
import org.imsglobal.caliper.actions.AnnotationActions;
import org.imsglobal.caliper.actions.ReadingActions;
import org.imsglobal.caliper.entities.DigitalResource;
import org.imsglobal.caliper.entities.reading.Frame;
import org.imsglobal.caliper.entities.LearningContext;
import org.imsglobal.caliper.entities.SoftwareApplication;
import org.imsglobal.caliper.entities.WebPage;
import org.imsglobal.caliper.entities.annotation.BookmarkAnnotation;
import org.imsglobal.caliper.entities.annotation.HighlightAnnotation;
import org.imsglobal.caliper.entities.annotation.SharedAnnotation;
import org.imsglobal.caliper.entities.annotation.TagAnnotation;
import org.imsglobal.caliper.entities.lis.CourseSection;
import org.imsglobal.caliper.entities.lis.Person;
import org.imsglobal.caliper.entities.reading.EpubVolume;
import org.imsglobal.caliper.entities.reading.View;
import org.imsglobal.caliper.events.AnnotationEvent;
import org.imsglobal.caliper.events.NavigationEvent;
import org.imsglobal.caliper.events.ViewEvent;
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

    // Initialize the sensor - this needs to be done only once
    private void initialize() {
        Options options = new Options();
        options.setHost(HOST);
        options.setApiKey(API_KEY);
        Sensor.initialize(options);

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

        output.append(Sensor.getStatistics().toString());

        response.getWriter().write(output.toString());

        //Clear the buffer
        output.setLength(0);
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
     * --------------------------------------------------------------------
     * -------------------------Reading Sequence---------------------------
     * --------------------------------------------------------------------
     * Student in a course interacts with two readings from two edApps.
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
         * Step 01.  Set the learning and activity context for the two readings.
         * -----------------------------------------------------------------
         */

        // TODO LISCourseSection does not define a section property.  Oversight?
        LearningContext lmsContext = LearningContext.builder()
            .edApp(SoftwareApplication.builder()
                .id("https://canvas.instructure.com")
                //.context("http://purl.imsglobal.org/ctx/caliper/v1/edApp/lms") // WARN CaliperEntity prop
                .lastModifiedAt(now.minus(Weeks.weeks(8)).getMillis())
                .build())
            .lisOrganization(CourseSection.builder()
                .id("https://some-university.edu/politicalScience/2014/american-revolution-101")
                .semester("Spring-2014")
                .courseNumber("AmRev-101")
                .label("Am Rev 101")
                .title("American Revolution 101")
                .lastModifiedAt(now.minus(Weeks.weeks(4)).getMillis())
                .build()) // lisCourseSection?
            .agent(Person.builder()
                .id("https://some-university.edu/students/jones-alice-554433")
                .lastModifiedAt(now.minus(Weeks.weeks(3)).getMillis())
                .build())
            .build();

        output.append("Generated activity context data.\n\n");

        /*
         * -----------------------------------------------------------------
         * Step 02.  Execute reading and annotation sequence.
         * -----------------------------------------------------------------
         */

        output.append("Sending events . . .\n\n");

        // EVENT 01 - Generate profile triggered by Navigation Event to #epubcfi(/4/3)

        // Readium Profile
        ReadingProfile readiumProfile = ReadingProfile.builder()
            .learningContext(LearningContext.builder()
                .edApp(SoftwareApplication.builder()
                    .id("https://github.com/readium/readium-js-viewer")
                    .lastModifiedAt(now.minus(Weeks.weeks(8)).getMillis())
                    .build())
                .lisOrganization(lmsContext.getLisOrganization())
                .agent(lmsContext.getAgent())
                .build())
            .reading(EpubVolume.builder()
                .id("https://github.com/readium/readium-js-viewer/book/34843#epubcfi(/4/3)")
                .name("The Glorious Cause: The American Revolution, 1763-1789 (Oxford History of the United States)")
                .lastModifiedAt(now.minus(Weeks.weeks(53)).getMillis())
                .build())
            .action(ReadingActions.NAVIGATED_TO.key())
            .fromResource(WebPage.builder()
                .id("AmRev-101-landingPage")
                .name("American Revolution 101 Landing Page")
                .partOf(lmsContext.getLisOrganization())
                .build())
            .target(Frame.builder()
                .id("https://github.com/readium/readium-js-viewer/book/34843#epubcfi(/4/3)")
                .index(0)
                .build())
            .build();

        output.append("Navigated to volume in Readium Reader . . . sent NavigatedEvent\n");

        // Process Event
        navigate(readiumProfile);

        output.append("Object : " + readiumProfile.getReading().getId() + "\n");
        output.append("From : " + Iterables.getLast(readiumProfile.getFromResources()).getId() + "\n");
        output.append("Target : " + ((Frame) Iterables.getLast(readiumProfile.getTargets())).getId() + "\n\n");

        // EVENT 02 - Add NavigationEvent to #epubcfi(/4/3)/1 (George Washington)
        readiumProfile.getActions().add(ReadingActions.NAVIGATED_TO.key());
        readiumProfile.getFromResources().add((DigitalResource) Iterables.getLast(readiumProfile.getTargets()));
        readiumProfile.getTargets().add(Frame.builder()
            .id(readiumProfile.getReading().getId() + "/1")
            .name("Key Figures: George Washington")
            .lastModifiedAt(readiumProfile.getReading().getLastModifiedAt())
            .index(1)
            .partOf(readiumProfile.getReading())
            .build());

        output.append("Navigated to Page 1 in Readium Reader . . . sent NavigatedEvent\n");

        // Process Event
        navigate(readiumProfile);

        output.append("Object : " + readiumProfile.getReading().getId() + "\n");
        output.append("From : " + Iterables.getLast(readiumProfile.getFromResources()).getId() + "\n");
        output.append("Target : " + ((Frame) Iterables.getLast(readiumProfile.getTargets())).getId() + "\n\n");

        // EVENT # 03 - Add ViewedEvent #epubcfi(/4/3)/1 (George Washington)

        readiumProfile.getActions().add(ReadingActions.VIEWED.key());
        readiumProfile.getViews().add(View.builder()
            .frame((Frame) Iterables.getLast(readiumProfile.getTargets()))
            .actor(lmsContext.getAgent())
            .startedAtTime(DateTime.now().minusSeconds(240).getMillis())
            .endedAtTime(DateTime.now().getMillis())
            .duration("PT" + randomSecsDurationBetween(5, 120) + "S")
            .build());
        readiumProfile.getFromResources().add(Iterables.getLast(readiumProfile.getFromResources()));
        readiumProfile.getTargets().add(Iterables.getLast(readiumProfile.getTargets()));

        output.append("Viewed Page 1 in Readium Reader . . . sent ViewedEvent\n");

        // Process event
        view(readiumProfile);

        output.append("Object : " + readiumProfile.getReading().getId() + "\n");
        output.append("From : " + Iterables.getLast(readiumProfile.getFromResources()).getId() + "\n");
        output.append("Target : " + ((Frame) Iterables.getLast(readiumProfile.getTargets())).getId() + "\n\n");

        // EVENT 04 - Add NavigationEvent to #epubcfi(/4/3)/1 (George Washington)
        readiumProfile.getActions().add(ReadingActions.NAVIGATED_TO.key());
        readiumProfile.getFromResources().add((DigitalResource) Iterables.getLast(readiumProfile.getTargets()));
        readiumProfile.getTargets().add(Frame.builder()
            .id(readiumProfile.getReading().getId() + "/2")
            .name("Key Figures: Lord Cornwallis")
            .lastModifiedAt(readiumProfile.getReading().getLastModifiedAt())
            .index(2)
            .partOf(readiumProfile.getReading())
            .build());

        output.append("Navigated to page 2 in Readium Reader . . . sent NavigatedEvent\n");

        // Process Event
        navigate(readiumProfile);

        output.append("Object : " + readiumProfile.getReading().getId() + "\n");
        output.append("From : " + Iterables.getLast(readiumProfile.getFromResources()).getId() + "\n");
        output.append("Target : " + ((Frame) Iterables.getLast(readiumProfile.getTargets())).getId() + "\n\n");

        // EVENT # 05 - Add ViewedEvent #epubcfi(/4/3)/2 (Lord Cornwallis)
        readiumProfile.getActions().add(ReadingActions.VIEWED.key());
        readiumProfile.getViews().add(View.builder()
            .frame((Frame) Iterables.getLast(readiumProfile.getTargets()))
            .actor(lmsContext.getAgent())
            .startedAtTime(DateTime.now().minusSeconds(360).getMillis())
            .endedAtTime(DateTime.now().getMillis())
            .duration("PT" + randomSecsDurationBetween(5, 120) + "S")
            .build());
        readiumProfile.getFromResources().add(Iterables.getLast(readiumProfile.getFromResources()));
        readiumProfile.getTargets().add(Iterables.getLast(readiumProfile.getTargets()));

        output.append("Viewed Page 2 in Readium Reader . . . sent ViewedEvent\n");

        // Process Event
        view(readiumProfile);

        output.append("Object : " + readiumProfile.getReading().getId() + "\n");
        output.append("From : " + Iterables.getLast(readiumProfile.getFromResources()).getId() + "\n");
        output.append("Target : " + ((Frame) Iterables.getLast(readiumProfile.getTargets())).getId() + "\n\n");

        // EVENT # 06 - HighlightedEvent
        AnnotationProfile readiumAnnotationProfile = AnnotationProfile.builder()
            .learningContext(readiumProfile.getLearningContext())
            .action(AnnotationActions.HIGHLIGHTED.key())
            .annotation(HighlightAnnotation.builder()
                .id("https://someEduApp.edu/highlights/" + UUID.randomUUID().toString())
                .selectionStart(Integer.toString(455))
                .selectionEnd(Integer.toString(489))
                .selectionText("Life, Liberty and the pursuit of Happiness")
                .target(Iterables.getLast(readiumProfile.getTargets()))  // TODO: REDUNDANT?
                .build())
            .target(Iterables.getLast(readiumProfile.getTargets()))
            .build();

        output.append("Highlighted fragment in Page 2 from index 455 to 489 in Readium Reading . . . sent HighlightedEvent\n");

        // Process Event
        annotate(readiumAnnotationProfile);

        HighlightAnnotation highlight = (HighlightAnnotation) Iterables.getLast(
            readiumAnnotationProfile.getAnnotations());
        output.append("Object : " + highlight.getId()  + "\n");
        output.append("From : " + Iterables.getLast(readiumProfile.getFromResources()).getId() + "\n");
        output.append("Target : " + ((DigitalResource) highlight.getTarget()).getId() + "\n\n");
        //output.append("Target : " + ((Frame) Iterables.getLast(readiumProfile.getTargets())).getId() + "\n\n");

        // EVENT # 07 - Add NavigationEvent #epubcfi(/4/3)/3 (Paul Revere)
        readiumProfile.getActions().add(ReadingActions.NAVIGATED_TO.key());
        readiumProfile.getFromResources().add((DigitalResource) Iterables.getLast(readiumProfile.getTargets()));
        readiumProfile.getTargets().add(Frame.builder()
            .id(readiumProfile.getReading().getId() + "/3")
            .name("Key Figures: Paul Revere")
            .lastModifiedAt(readiumProfile.getReading().getLastModifiedAt())
            .index(2)
            .partOf(readiumProfile.getReading())
            .build());

        output.append("Navigated to Page 3 in Readium Reader . . . sent NavigatedEvent\n");

        // Process Event
        navigate(readiumProfile);

        output.append("Object : " + readiumProfile.getReading().getId() + "\n");
        output.append("From : " + Iterables.getLast(readiumProfile.getFromResources()).getId() + "\n");
        output.append("Target : " + ((Frame) Iterables.getLast(readiumProfile.getTargets())).getId() + "\n\n");

        // EVENT # 07 - Add Viewed Event #epubcfi(/4/3)/3 (Paul Revere)
        readiumProfile.getActions().add(ReadingActions.VIEWED.key());
        readiumProfile.getViews().add(View.builder()
            .frame((Frame) Iterables.getLast(readiumProfile.getTargets()))
            .actor(lmsContext.getAgent())
            .startedAtTime(DateTime.now().minusSeconds(180).getMillis())
            .endedAtTime(DateTime.now().getMillis())
            .duration("PT" + randomSecsDurationBetween(5, 120) + "S")
            .build());
        readiumProfile.getFromResources().add(Iterables.getLast(readiumProfile.getFromResources()));
        readiumProfile.getTargets().add(Iterables.getLast(readiumProfile.getTargets()));

        output.append("Viewed Page 3 in Readium Reading . . . sent ViewedEvent\n");

        // Process Event
        view(readiumProfile);

        output.append("Object : " + readiumProfile.getReading().getId() + "\n");
        output.append("From : " + Iterables.getLast(readiumProfile.getFromResources()).getId() + "\n");
        output.append("Target : " + ((Frame) Iterables.getLast(readiumProfile.getTargets())).getId() + "\n\n");

        // EVENT # 08 - Add BookmarkedEvent
        readiumAnnotationProfile.getActions().add(AnnotationActions.BOOKMARKED.key());
        readiumAnnotationProfile.getAnnotations().add(BookmarkAnnotation.builder()
            .id("https://someEduApp.edu/bookmarks/" + UUID.randomUUID().toString())
            .bookmarkNotes("The Intolerable Acts (1774)--bad idea Lord North")
            .target(Iterables.getLast(readiumProfile.getTargets())) // TODO: REDUNDANT?
            .build());
        readiumAnnotationProfile.getTargets().add(Iterables.getLast(readiumProfile.getTargets()));

        output.append("Bookmarked Page with pageId 3 in Readium Reading . . . sent BookmarkedEvent\n");

        // Process Event
        annotate(readiumAnnotationProfile);

        BookmarkAnnotation bookmark = (BookmarkAnnotation) Iterables.getLast(readiumAnnotationProfile.getAnnotations());
        output.append("Object : " + bookmark.getId()  + "\n");
        output.append("From : " + Iterables.getLast(readiumProfile.getFromResources()).getId() + "\n");
        output.append("Target : " + ((DigitalResource) bookmark.getTarget()).getId() + "\n\n");
        //output.append("Target : " + ((Frame) Iterables.getLast(readiumProfile.getTargets())).getId() + "\n\n");

        // EVENT # 09 - Generate CourseSmart Profile triggered by NavigationEvent

        // CourseSmart Profile
        ReadingProfile courseSmartProfile = ReadingProfile.builder()
            .learningContext(LearningContext.builder()
                .edApp(SoftwareApplication.builder()
                    .id("http://www.coursesmart.com/reader")
                    .lastModifiedAt(now.minus(Weeks.weeks(6)).getMillis())
                    .build())
                .lisOrganization(lmsContext.getLisOrganization())
                .agent(lmsContext.getAgent())
                .build())
            .reading(EpubVolume.builder()
                .id("http://www.coursesmart.com/the-american-revolution-a-concise-history/robert-j-allison/dp/9780199347322")
                .name("The American Revolution: A Concise History | 978-0-19-531295-9")
                .lastModifiedAt(now.minus(Weeks.weeks(22)).getMillis())
                .build())
            .action(ReadingActions.NAVIGATED_TO.key())
            .fromResource(WebPage.builder()
                .id("AmRev-101-landingPage")
                .name("American Revolution 101 Landing Page")
                .partOf(readiumProfile.getLearningContext().getLisOrganization())
                .build())
            .target(Frame.builder()
                .id("http://www.coursesmart.com/the-american-revolution-a-concise-history/robert-j-allison/dp/9780199347322")
                .index(0)
                .build())
            .build();

        output.append("Navigated to Reading provided by CourseSmart . . . sent NavigateEvent\n");

        // Process event
        navigate(courseSmartProfile);

        output.append("Object : " + courseSmartProfile.getReading().getId() + "\n");
        output.append("From : " + Iterables.getLast(readiumProfile.getFromResources()).getId() + "\n");
        output.append("Target : " + ((Frame) Iterables.getLast(courseSmartProfile.getTargets())).getId() + "\n\n");

        // EVENT # 10 - Add NavigationEvent aXfsadf12
        courseSmartProfile.getActions().add(ReadingActions.NAVIGATED_TO.key());
        courseSmartProfile.getFromResources().add((DigitalResource) Iterables.getLast(courseSmartProfile.getTargets()));
        courseSmartProfile.getTargets().add(Frame.builder()
            .id(courseSmartProfile.getReading().getId() + "/aXfsadf12")
            .name("The Boston Tea Party")
            .lastModifiedAt(courseSmartProfile.getReading().getLastModifiedAt())
            .index(1)
            .build());

        output.append("Navigated to Reading provided by CourseSmart . . . sent NavigateEvent\n");

        // Process event
        navigate(courseSmartProfile);

        output.append("Object : " + courseSmartProfile.getReading().getId() + "\n");
        output.append("From : " + Iterables.getLast(readiumProfile.getFromResources()).getId() + "\n");
        output.append("Target : " + ((Frame) Iterables.getLast(courseSmartProfile.getTargets())).getId() + "\n\n");


        // EVENT # 11 - Add ViewedEvent aXfsadf12
        courseSmartProfile.getActions().add(ReadingActions.VIEWED.key());
        courseSmartProfile.getViews().add(View.builder()
            .frame((Frame) Iterables.getLast(courseSmartProfile.getTargets()))
            .actor(lmsContext.getAgent())
            .startedAtTime(DateTime.now().minusSeconds(180).getMillis())
            .endedAtTime(DateTime.now().getMillis())
            .duration("PT" + randomSecsDurationBetween(5, 120) + "S")
            .build());
        courseSmartProfile.getFromResources().add(Iterables.getLast(courseSmartProfile.getFromResources()));
        courseSmartProfile.getTargets().add(Iterables.getLast(courseSmartProfile.getTargets()));

        output.append("Viewed CourseSmart Reading Page with pageId aXfsadf12 ... sent ViewedEvent\n");

        // Process event
        view(courseSmartProfile);

        output.append("Object : " + courseSmartProfile.getReading().getId() + "\n");
        output.append("From : " + Iterables.getLast(readiumProfile.getFromResources()).getId() + "\n");
        output.append("Target : " + ((Frame) Iterables.getLast(courseSmartProfile.getTargets())).getId() + "\n\n");

        // EVENT # 12 - Add TaggedEvent
        AnnotationProfile courseSmartAnnotationProfile = AnnotationProfile.builder()
            .learningContext(courseSmartProfile.getLearningContext())
            .action(AnnotationActions.TAGGED.key())
            .annotation((TagAnnotation) TagAnnotation.builder()
                .id("https://someEduApp.edu/tags/" + UUID.randomUUID().toString())
                .tags(Lists.newArrayList("to-read", "1776", "shared-with-project-team"))
                .target(Iterables.getLast(courseSmartProfile.getTargets()))
                .build())
            .target(Iterables.getLast(courseSmartProfile.getTargets())) // WARN: DO REALLY NEED target in BASE PROFILE?
            .build();

        output.append("Tagged CourseSmart Reading Page with pageId aXfsadf12 . . . sent TaggedEvent\n");

        // Process event
        annotate(courseSmartAnnotationProfile);

        TagAnnotation tag = (TagAnnotation) Iterables.getLast(courseSmartAnnotationProfile.getAnnotations());
        output.append("Object : " + tag.getId()  + "\n");
        output.append("From : " + Iterables.getLast(readiumProfile.getFromResources()).getId() + "\n");
        output.append("Target : " + ((DigitalResource) tag.getTarget()).getId() + "\n\n");
        output.append("Tags : " + tag.getTags().toString() + "\n");
        //output.append("Target : " + ((Frame) Iterables.getLast(courseSmartProfile.getTargets())).getId() + "\n\n");

        // EVENT # 13 - Add SharedEvent
        courseSmartAnnotationProfile.getActions().add(AnnotationActions.SHARED.key());
        courseSmartAnnotationProfile.getAnnotations().add((SharedAnnotation) SharedAnnotation.builder()
            .id("https://someEduApp.edu/tags/" + UUID.randomUUID().toString())
            .withAgents(Lists.newArrayList(
                "https://some-university.edu/students/smith-bob-554433",
                "https://some-university.edu/students/lam-eve-554433"))
            .target(Iterables.getLast(courseSmartProfile.getTargets()))
            .build());
        courseSmartAnnotationProfile.getTargets().add(Iterables.getLast(courseSmartProfile.getTargets()));

        output.append("Shared CourseSmart Reading Page with pageId aXfsadf12 with students . . . sent SharedEvent\n");

        // Process event
        annotate(courseSmartAnnotationProfile);

        SharedAnnotation shared = (SharedAnnotation) Iterables.getLast(
            courseSmartAnnotationProfile.getAnnotations());
        output.append("Object : " + shared.getId() + "\n");
        output.append("From : " + Iterables.getLast(readiumProfile.getFromResources()).getId() + "\n");
        output.append("Target : " + ((DigitalResource) shared.getTarget()).getId() + "\n\n");
        //output.append("Target : " + ((Frame) Iterables.getLast(courseSmartProfile.getTargets())).getId() + "\n\n");
        output.append("Shared : " + shared.getWithAgents().toString() + "\n");
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
        Sensor.send(event);

        // Output i18n action text
        output.append("Action : " + event.getAction() + "\n");
    }

    private void navigate(ReadingProfile profile) {

        NavigationEvent event = NavigationEvent.builder()
            .edApp(profile.getLearningContext().getEdApp())
            .lisOrganization(profile.getLearningContext().getLisOrganization())
            .actor(profile.getLearningContext().getAgent())
            .action(Iterables.getLast(profile.getActions()))
            .object(profile.getReading())
            .target(Iterables.getLast(profile.getTargets()))
            .fromResource(Iterables.getLast(profile.getFromResources()))
            .startedAtTime(DateTime.now().getMillis())  // Pass this value in?
            .build();

        // Send event to EventStore
        Sensor.send(event);

        // Output i18n action text
        output.append("Action : " + event.getAction() + "\n");
    }

    private void view(ReadingProfile profile) {

        ViewEvent event = ViewEvent.builder()
            .edApp(profile.getLearningContext().getEdApp())
            .lisOrganization(profile.getLearningContext().getLisOrganization())
            .actor(profile.getLearningContext().getAgent())
            .action(Iterables.getLast(profile.getActions()))
            .object(profile.getReading())
            .target(Iterables.getLast(profile.getTargets()))
            .startedAtTime(Iterables.getLast(profile.getViews()).getStartedAtTime())
            .endedAtTime(Iterables.getLast(profile.getViews()).getEndedAtTime())
            .duration(Iterables.getLast(profile.getViews()).getDuration())
            .build();

        // Send event to EventStore
        Sensor.send(event);

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