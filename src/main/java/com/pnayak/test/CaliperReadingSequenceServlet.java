package com.pnayak.test;

import com.google.common.collect.Lists;
import org.imsglobal.caliper.Options;
import org.imsglobal.caliper.Sensor;
import org.imsglobal.caliper.entities.DigitalResource;
import org.imsglobal.caliper.entities.LearningContext;
import org.imsglobal.caliper.entities.annotation.BookmarkAnnotation;
import org.imsglobal.caliper.entities.annotation.HighlightAnnotation;
import org.imsglobal.caliper.entities.annotation.SharedAnnotation;
import org.imsglobal.caliper.entities.annotation.TagAnnotation;
import org.imsglobal.caliper.entities.foaf.Agent;
import org.imsglobal.caliper.entities.lis.Person;
import org.imsglobal.caliper.entities.reading.Frame;
import org.imsglobal.caliper.events.AnnotationEvent;
import org.imsglobal.caliper.events.NavigationEvent;
import org.imsglobal.caliper.events.ReadingEvent;
import org.imsglobal.caliper.profiles.AnnotationProfile;
import org.imsglobal.caliper.profiles.NavigationProfile;
import org.imsglobal.caliper.profiles.ReadingProfile;
import org.joda.time.DateTime;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

import static com.pnayak.test.CaliperSampleAssets.*;

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

        DateTime now = DateTime.now();
        output.append(now + "\n\n");

        /*
         * -----------------------------------------------------------------
         * Execute reading and annotation sequence.
         * -----------------------------------------------------------------
         */

        output.append("Sending events . . .\n\n");

        // EVENT 01 - Add Navigation Event to #epubcfi(/4/3)
        LearningContext learningContext = buildCanvasLearningContext();
        DigitalResource reading = buildEpubSubChap43();
        DigitalResource fromResource = buildAmRev101LandingPage();
        Date incrementTime = getDefaultStartedAtTime();
        NavigationEvent navEvent = NavigationEvent.builder()
            .edApp(learningContext.getEdApp())
            .lisOrganization(learningContext.getLisOrganization())
            .actor((Person) learningContext.getAgent())
            .object(reading)
            .action(NavigationProfile.Actions.NAVIGATED_TO.key())
            .fromResource(fromResource)
            .target(Frame.builder()
                .id(reading.getId())
                .index(0)
                .build())
            .startedAtTime(incrementTime)
            .build();

        output.append("Generated LMS learning context.\n");
        output.append("Generated EPUB activity context data\n");
        output.append("Navigated to EPUB reading #epubcfi(/4/3) from LMS.  Sent NavigationEvent \n");

        Sensor.send(navEvent);

        output.append("Object : " + navEvent.getObject().getId() + "\n");
        output.append("From : " + navEvent.getFromResource().getId() + "\n");
        output.append("Target : " + ((Frame) navEvent.getTarget()).getId() + "\n\n");

        // EVENT 02 - Add NavigationEvent to #epubcfi(/4/3/1) (George Washington)
        learningContext = buildReadiumLearningContext();
        reading = buildEpubSubChap43();
        DigitalResource target = buildEpubSubChap431();
        incrementTime = new Date(getDefaultStartedAtTime().getTime() + 20000);
        navEvent = NavigationEvent.builder()
            .edApp(learningContext.getEdApp())
            .lisOrganization(learningContext.getLisOrganization())
            .actor((Person) learningContext.getAgent())
            .object(reading)
            .action(NavigationProfile.Actions.NAVIGATED_TO.key())
            .fromResource(fromResource)
            .target(Frame.builder()
                .id(target.getId())
                .index(1)
                .build())
            .startedAtTime(incrementTime)
            .build();

        output.append("Generated Readium Viewer learning context.\n");
        output.append("Generated EPUB activity context data\n");

        Sensor.send(navEvent);

        output.append("Navigated to EPUB reading #epubcfi(/4/3/1) from #epubcfi(/4/3).  Sent NavigationEvent \n");
        output.append("Object : " + navEvent.getObject().getId() + "\n");
        output.append("From : " + navEvent.getFromResource().getId() + "\n");
        output.append("Target : " + ((Frame) navEvent.getTarget()).getId() + "\n\n");

        // EVENT 03 - Add ViewedEvent #epubcfi(/4/3/1) (George Washington)
        // TODO viewed = object or target?
        learningContext = buildReadiumLearningContext();
        reading = buildEpubSubChap43();
        target = buildEpubSubChap431();
        incrementTime = new Date(incrementTime.getTime() + 20000);
        ReadingEvent readEvent = ReadingEvent.builder()
            .edApp(learningContext.getEdApp())
            .lisOrganization(learningContext.getLisOrganization())
            .actor((Person) learningContext.getAgent())
            .object(reading)
            .action(ReadingProfile.Actions.VIEWED.key())
            .target(Frame.builder()
                    .id(target.getId())
                    .index(1)
                    .build())
            .startedAtTime(incrementTime)
            .build();

        output.append("Generated Readium Viewer learning context.\n");
        output.append("Generated EPUB activity context data\n");

        Sensor.send(readEvent);

        output.append("Viewed #epubcfi(/4/3/1).  Sent ViewedEvent \n");
        output.append("Object : " + ((DigitalResource) readEvent.getObject()).getId() + "\n");
        output.append("Target : " + ((Frame) readEvent.getTarget()).getId() + "\n\n");

        // EVENT 04 - Add NavigationEvent to #epubcfi(/4/3/2) (Lord Cornwallis)
        learningContext = buildReadiumLearningContext();
        reading = buildEpubSubChap43();
        fromResource = buildEpubSubChap431();
        target = buildEpubSubChap432();
        incrementTime = new Date(incrementTime.getTime() + 20000);
        navEvent = NavigationEvent.builder()
            .edApp(learningContext.getEdApp())
            .lisOrganization(learningContext.getLisOrganization())
            .actor((Person) learningContext.getAgent())
            .object(reading)
            .action(NavigationProfile.Actions.NAVIGATED_TO.key())
            .fromResource(fromResource)
            .target(Frame.builder()
                .id(target.getId())
                .index(2)
                .build())
            .startedAtTime(incrementTime)
                .build();

        output.append("Generated Readium Viewer learning context.\n");
        output.append("Generated EPUB activity context data\n");

        Sensor.send(navEvent);

        output.append("Navigated to EPUB reading #epubcfi(/4/3/2) from #epubcfi(/4/3/1).  Sent NavigationEvent \n");
        output.append("Object : " + navEvent.getObject().getId() + "\n");
        output.append("From : " + navEvent.getFromResource().getId() + "\n");
        output.append("Target : " + ((Frame) navEvent.getTarget()).getId() + "\n\n");

        // EVENT 05 - Add ViewedEvent #epubcfi(/4/3/2) (Lord Cornwallis)
        // TODO viewed = object or target?
        learningContext = buildReadiumLearningContext();
        reading = buildEpubSubChap43();
        target = buildEpubSubChap432();
        incrementTime = new Date(incrementTime.getTime() + 20000);
        readEvent = ReadingEvent.builder()
            .edApp(learningContext.getEdApp())
            .lisOrganization(learningContext.getLisOrganization())
            .actor((Person) learningContext.getAgent())
            .object(reading)
            .action(ReadingProfile.Actions.VIEWED.key())
            .target(Frame.builder()
                .id(target.getId())
                .index(2)
                .build())
            .startedAtTime(incrementTime)
            .build();

        output.append("Generated Readium Viewer learning context.\n");
        output.append("Generated EPUB activity context data\n");

        Sensor.send(readEvent);

        output.append("Viewed #epubcfi(/4/3/2).  Sent ViewedEvent \n");
        output.append("Object : " + ((DigitalResource) readEvent.getObject()).getId() + "\n");
        output.append("Target : " + ((Frame) readEvent.getTarget()).getId() + "\n\n");

        // EVENT 06 - HighlightedEvent
        learningContext = buildReadiumLearningContext();
        target = buildEpubSubChap432();
        incrementTime = new Date(incrementTime.getTime() + 20000);
        AnnotationEvent annoEvent = AnnotationEvent.builder()
            .edApp(learningContext.getEdApp())
            .lisOrganization(learningContext.getLisOrganization())
            .actor((Person) learningContext.getAgent())
            .object(HighlightAnnotation.builder()
                .id("https://someEduApp.edu/highlights/" + UUID.randomUUID().toString())
                .selectionStart(Integer.toString(455))
                .selectionEnd(Integer.toString(489))
                .selectionText("Life, Liberty and the pursuit of Happiness")
                .target(Frame.builder()
                    .id(target.getId())
                    .index(2)
                    .build())
                .build())
            .action(AnnotationProfile.Actions.HIGHLIGHTED.key())
            .target(Frame.builder()
                .id(target.getId())
                .index(2)
                .build())
            .startedAtTime(incrementTime)
            .build();

        output.append("Generated Readium Viewer learning context.\n");
        output.append("Generated Annotation activity context data\n");

        Sensor.send(annoEvent);

        output.append("Highlighted fragment in #epubcfi(/4/3/2) between index 455-489.  Sent HighlightedEvent\n");
        output.append("Object : " + annoEvent.getObject().getId()  + "\n");
        output.append("Target : " + ((Frame) annoEvent.getTarget()).getId() + "\n");
        output.append("Highlighted text : " + ((HighlightAnnotation) annoEvent.getObject()).getSelectionText() + "\n\n");

        // EVENT 07 - Add NavigationEvent #epubcfi(/4/3/3) (Paul Revere)
        learningContext = buildReadiumLearningContext();
        reading = buildEpubSubChap43();
        fromResource = buildEpubSubChap432();
        target = buildEpubSubChap433();
        incrementTime = new Date(incrementTime.getTime() + 20000);
        navEvent = NavigationEvent.builder()
            .edApp(learningContext.getEdApp())
            .lisOrganization(learningContext.getLisOrganization())
            .actor((Person) learningContext.getAgent())
            .object(reading)
            .action(NavigationProfile.Actions.NAVIGATED_TO.key())
            .fromResource(fromResource)
            .target(Frame.builder()
                    .id(target.getId())
                    .index(3)
                    .build())
            .startedAtTime(incrementTime)
            .build();

        output.append("Generated Readium Viewer learning context.\n");
        output.append("Generated EPUB activity context data\n");

        Sensor.send(navEvent);

        output.append("Navigated to EPUB reading #epubcfi(/4/3/3) from #epubcfi(/4/3/2).  Sent NavigationEvent \n");
        output.append("Object : " + navEvent.getObject().getId() + "\n");
        output.append("From : " + navEvent.getFromResource().getId() + "\n");
        output.append("Target : " + ((Frame) navEvent.getTarget()).getId() + "\n\n");

        // EVENT 08 - Add Viewed Event #epubcfi(/4/3/3) (Paul Revere)
        learningContext = buildReadiumLearningContext();
        reading = buildEpubSubChap43();
        target = buildEpubSubChap433();
        incrementTime = new Date(incrementTime.getTime() + 20000);
        readEvent = ReadingEvent.builder()
            .edApp(learningContext.getEdApp())
            .lisOrganization(learningContext.getLisOrganization())
            .actor((Person) learningContext.getAgent())
            .object(reading)
            .action(ReadingProfile.Actions.VIEWED.key())
            .target(Frame.builder()
                    .id(target.getId())
                    .index(3)
                    .build())
            .startedAtTime(incrementTime)
            .build();

        output.append("Generated Readium Viewer learning context.\n");
        output.append("Generated EPUB activity context data\n");

        Sensor.send(readEvent);

        output.append("Viewed #epubcfi(/4/3/3).  Sent ViewedEvent \n");
        output.append("Object : " + ((DigitalResource) readEvent.getObject()).getId() + "\n");
        output.append("Target : " + ((Frame) readEvent.getTarget()).getId() + "\n\n");

        // EVENT 09 - Add BookmarkedEvent
        learningContext = buildReadiumLearningContext();
        target = buildEpubSubChap433();
        incrementTime = new Date(incrementTime.getTime() + 20000);
        annoEvent = AnnotationEvent.builder()
            .edApp(learningContext.getEdApp())
            .lisOrganization(learningContext.getLisOrganization())
            .actor((Person) learningContext.getAgent())
            .object(BookmarkAnnotation.builder()
                .id("https://someEduApp.edu/bookmarks/" + UUID.randomUUID().toString())
                .bookmarkNotes("The Intolerable Acts (1774)--bad idea Lord North")
                .target(target)
                .build())
            .action(AnnotationProfile.Actions.BOOKMARKED.key())
            .target(Frame.builder()
                .id(target.getId())
                .index(3)
                .build())
            .startedAtTime(incrementTime)
            .build();

        output.append("Generated Readium Viewer learning context.\n");
        output.append("Generated Annotation activity context data\n");

        Sensor.send(annoEvent);

        output.append("Bookmarked #epubcfi(/4/3/2).  Sent BookmarkedEvent\n");
        output.append("Object : " + annoEvent.getObject().getId()  + "\n");
        output.append("Target : " + ((Frame) annoEvent.getTarget()).getId() + "\n");
        output.append("Bookmark notes : " + ((BookmarkAnnotation) annoEvent.getObject())
            .getBookmarkNotes() + "\n\n");

        // EVENT 10 - Generate CourseSmart Profile triggered by NavigationEvent
        learningContext = buildCourseSmartLearningContext();
        reading = buildAllisonAmRevEpubVolume();
        fromResource = buildAmRev101LandingPage();
        incrementTime = new Date(incrementTime.getTime() + 20000);
        navEvent = NavigationEvent.builder()
            .edApp(learningContext.getEdApp())
            .lisOrganization(learningContext.getLisOrganization())
            .actor((Person) learningContext.getAgent())
            .object(reading)
            .action(NavigationProfile.Actions.NAVIGATED_TO.key())
            .fromResource(fromResource)
            .target(Frame.builder()
                    .id(reading.getId())
                    .index(0)
                    .build())
            .startedAtTime(incrementTime)
            .build();

        output.append("Generated CourseSmart learning context.\n");
        output.append("Generated EPUB activity context data\n");

        Sensor.send(navEvent);

        output.append("Navigated to CourseSmart Reading.  Sent NavigateEvent\n");
        output.append("Object : " + navEvent.getObject().getId() + "\n");
        output.append("From : " + navEvent.getFromResource().getId() + "\n");
        output.append("Target : " + ((Frame) navEvent.getTarget()).getId() + "\n\n");

        // EVENT 11 - Add NavigationEvent aXfsadf12
        learningContext = buildCourseSmartLearningContext();
        reading = buildAllisonAmRevEpubVolume();
        fromResource = buildAllisonAmRevEpubVolume();
        target = buildAllisonAmRevEpubSubChap();
        incrementTime = new Date(incrementTime.getTime() + 20000);
        navEvent = NavigationEvent.builder()
            .edApp(learningContext.getEdApp())
            .lisOrganization(learningContext.getLisOrganization())
            .actor((Person) learningContext.getAgent())
            .object(reading)
            .action(NavigationProfile.Actions.NAVIGATED_TO.key())
            .fromResource(fromResource)
            .target(Frame.builder()
                    .id(target.getId())
                    .name(target.getName())
                    .index(1)
                    .build())
            .startedAtTime(incrementTime)
            .build();

        output.append("Generated CourseSmart learning context.\n");
        output.append("Generated EPUB activity context data\n");

        Sensor.send(navEvent);

        output.append("Navigated to page aXfsadf12.  Sent NavigateEvent\n");
        output.append("Object : " + navEvent.getObject().getId() + "\n");
        output.append("From : " + navEvent.getFromResource().getId() + "\n");
        output.append("Target : " + ((Frame) navEvent.getTarget()).getId() + "\n\n");

        // EVENT 12 - Add ViewedEvent aXfsadf12
        learningContext = buildCourseSmartLearningContext();
        reading = buildAllisonAmRevEpubVolume();
        target = buildAllisonAmRevEpubSubChap();
        incrementTime = new Date(incrementTime.getTime() + 20000);
        readEvent = ReadingEvent.builder()
            .edApp(learningContext.getEdApp())
            .lisOrganization(learningContext.getLisOrganization())
            .actor((Person) learningContext.getAgent())
            .object(reading)
            .action(ReadingProfile.Actions.VIEWED.key())
            .target(Frame.builder()
                .id(target.getId())
                .index(1)
                .build())
            .startedAtTime(incrementTime)
            .build();

        output.append("Generated CourseSmart learning context.\n");
        output.append("Generated EPUB activity context data\n");

        Sensor.send(readEvent);

        output.append("Viewed CourseSmart page aXfsadf12.  Sent ViewedEvent \n");
        output.append("Object : " + ((DigitalResource) readEvent.getObject()).getId() + "\n");
        output.append("Target : " + ((Frame) readEvent.getTarget()).getId() + "\n\n");

        // EVENT 13 - Add TaggedEvent
        learningContext = buildCourseSmartLearningContext();
        target = buildAllisonAmRevEpubSubChap();
        incrementTime = new Date(incrementTime.getTime() + 20000);
        annoEvent = AnnotationEvent.builder()
            .edApp(learningContext.getEdApp())
            .lisOrganization(learningContext.getLisOrganization())
            .actor((Person) learningContext.getAgent())
            .object(TagAnnotation.builder()
                .id("https://someEduApp.edu/tags/" + UUID.randomUUID().toString())
                .tags(Lists.newArrayList("to-read", "1776", "shared-with-project-team"))
                .target(target)
                .build())
            .action(AnnotationProfile.Actions.TAGGED.key())
            .target(Frame.builder()
                .id(target.getId())
                .index(1)
                .build())
            .startedAtTime(incrementTime)
            .build();

        output.append("Generated CourseSmart learning context.\n");
        output.append("Generated Annotation activity context data\n");

        Sensor.send(annoEvent);

        output.append("Tagged CourseSmart page aXfsadf12.  Sent TaggedEvent\n");
        output.append("Object : " + annoEvent.getObject().getId()  + "\n");
        output.append("Target : " + ((Frame) annoEvent.getTarget()).getId() + "\n");
        output.append("Tags : " + ((TagAnnotation) annoEvent.getObject()).getTags().toString() + "\n\n");

        // EVENT 14 - Add SharedEvent
        learningContext = buildCourseSmartLearningContext();
        target = buildAllisonAmRevEpubSubChap();
        incrementTime = new Date(incrementTime.getTime() + 20000);
        annoEvent = AnnotationEvent.builder()
            .edApp(learningContext.getEdApp())
            .lisOrganization(learningContext.getLisOrganization())
            .actor((Person) learningContext.getAgent())
            .object(SharedAnnotation.builder()
                .id("https://someEduApp.edu/shared/" + UUID.randomUUID().toString())
                .withAgents(Lists.<Agent>newArrayList(
                    Person.builder()
                        .id("https://some-university.edu/students/657585")
                        .dateCreated(getDefaultDateCreated())
                        .dateModified(getDefaultDateModified())
                        .build(),
                    Person.builder()
                        .id("https://some-university.edu/students/667788")
                        .dateCreated(getDefaultDateCreated())
                        .dateModified(getDefaultDateModified())
                        .build()))
                .build())
            .action(AnnotationProfile.Actions.SHARED.key())
            .target(Frame.builder()
                    .id(target.getId())
                    .index(1)
                    .build())
            .startedAtTime(incrementTime)
            .build();

        output.append("Generated CourseSmart learning context.\n");
        output.append("Generated Annotation activity context data\n");

        Sensor.send(annoEvent);

        output.append("Shared CourseSmart page aXfsadf12.  Sent SharedEvent\n");
        output.append("Object : " + annoEvent.getObject().getId()  + "\n");
        output.append("Target : " + annoEvent.getTarget().getId() + "\n");

        // Retrieve agents
        SharedAnnotation shared = (SharedAnnotation) annoEvent.getObject();
        for (Agent agent: shared.getWithAgents()) {
            output.append("Shared with: " + ((Person) agent).getId() + "\n");
        }

        output.append("FINIS\n\n");
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