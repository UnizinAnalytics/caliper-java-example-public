package com.imsglobal.caliper.example;

import com.google.common.collect.Lists;
import org.imsglobal.caliper.Options;
import org.imsglobal.caliper.Sensor;
import org.imsglobal.caliper.entities.DigitalResource;
import org.imsglobal.caliper.entities.LearningContext;
import org.imsglobal.caliper.entities.Session;
import org.imsglobal.caliper.entities.SoftwareApplication;
import org.imsglobal.caliper.entities.annotation.BookmarkAnnotation;
import org.imsglobal.caliper.entities.annotation.HighlightAnnotation;
import org.imsglobal.caliper.entities.annotation.SharedAnnotation;
import org.imsglobal.caliper.entities.annotation.TagAnnotation;
import org.imsglobal.caliper.entities.foaf.Agent;
import org.imsglobal.caliper.entities.lis.Person;
import org.imsglobal.caliper.entities.reading.EpubSubChapter;
import org.imsglobal.caliper.entities.reading.EpubVolume;
import org.imsglobal.caliper.entities.reading.Frame;
import org.imsglobal.caliper.events.AnnotationEvent;
import org.imsglobal.caliper.events.NavigationEvent;
import org.imsglobal.caliper.events.ReadingEvent;
import org.imsglobal.caliper.events.SessionEvent;
import org.imsglobal.caliper.profiles.AnnotationProfile;
import org.imsglobal.caliper.profiles.Profile;
import org.imsglobal.caliper.profiles.ReadingProfile;
import org.imsglobal.caliper.profiles.SessionProfile;
import org.joda.time.DateTime;

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
    StringBuilder output = new StringBuilder();

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
    private void generateReadingAnnotationEventSequence(StringBuilder output) {

        DateTime now = DateTime.now();
        output.append(now + "\n\n");
        output.append("Sending events . . .\n\n");

        // Session Event: logged in to Canvas LMS
        LearningContext canvas = CaliperSampleAssets.buildCanvasLearningContext();
        DigitalResource reading = CaliperSampleAssets.buildEpubSubChap43();
        DateTime incrementTime = CaliperSampleAssets.getDefaultStartedAtTime();

        SessionEvent sessionEvent = SessionEvent.builder()
            .edApp(canvas.getEdApp())
            .lisOrganization(canvas.getLisOrganization())
            .actor((Person) canvas.getAgent())
            .action(SessionProfile.Actions.LOGGEDIN.key())
            .object(canvas.getEdApp())
            .target(reading)
            .generated(CaliperSampleAssets.buildSessionStart())
            .startedAtTime(incrementTime)
            .build();

        Sensor.send(sessionEvent);

        output.append("Generated SessionEvent \n");
        output.append("actor : " + ((Person) sessionEvent.getActor()).getId() + "\n");
        output.append("action : " + sessionEvent.getAction() + "\n");
        output.append("object : " + ((SoftwareApplication) sessionEvent.getObject()).getId() + "\n");
        output.append("target : " + ((DigitalResource) sessionEvent.getTarget()).getId() + "\n");
        output.append("generated : " + ((Session) sessionEvent.getGenerated()).getId() + "\n\n");

        //TODO ADD LAUNCH EVENT

        // Navigation Event: navigated to #epubcfi(/4/3)
        LearningContext learningContext = CaliperSampleAssets.buildCanvasLearningContext();
        reading = CaliperSampleAssets.buildEpubSubChap43();
        DigitalResource fromResource = CaliperSampleAssets.buildAmRev101LandingPage();
        incrementTime = CaliperSampleAssets.getDefaultStartedAtTime().plusSeconds(10);
        NavigationEvent navEvent = NavigationEvent.builder()
            .edApp(learningContext.getEdApp())
            .lisOrganization(learningContext.getLisOrganization())
            .actor((Person) learningContext.getAgent())
            .object(reading)
            .action(Profile.Actions.NAVIGATED_TO.key())
            .fromResource(fromResource)
            .target(Frame.builder()
                .id(reading.getId())
                .index(0)
                .build())
            .startedAtTime(incrementTime)
            .build();

        Sensor.send(navEvent);

        output.append("Generated NavigationEvent \n");
        output.append("actor : " + ((Person) navEvent.getActor()).getId() + "\n");
        output.append("action : " + navEvent.getAction() + "\n");
        output.append("object : " + ((DigitalResource) navEvent.getObject()).getId() + "\n");
        output.append("fromResource : " + navEvent.getFromResource().getId() + "\n");
        output.append("target : " + ((Frame) navEvent.getTarget()).getId() + "\n\n");

        // Navigation Event: navigated to #epubcfi(/4/3/1) (George Washington)
        learningContext = CaliperSampleAssets.buildReadiumLearningContext();
        reading = CaliperSampleAssets.buildEpubSubChap43();
        DigitalResource target = CaliperSampleAssets.buildEpubSubChap431();
        incrementTime = CaliperSampleAssets.getDefaultStartedAtTime().plusSeconds(15);
        navEvent = NavigationEvent.builder()
            .edApp(learningContext.getEdApp())
            .lisOrganization(learningContext.getLisOrganization())
            .actor((Person) learningContext.getAgent())
            .object(reading)
            .action(Profile.Actions.NAVIGATED_TO.key())
            .fromResource(fromResource)
            .target(Frame.builder()
                .id(target.getId())
                .index(1)
                .build())
            .startedAtTime(incrementTime)
            .build();

        Sensor.send(navEvent);

        output.append("Generated NavigationEvent \n");
        output.append("actor : " + ((Person) navEvent.getActor()).getId() + "\n");
        output.append("action : " + navEvent.getAction() + "\n");
        output.append("object : " + ((EpubSubChapter) navEvent.getObject()).getId() + "\n");
        output.append("fromResource : " + navEvent.getFromResource().getId() + "\n");
        output.append("target : " + ((Frame) navEvent.getTarget()).getId() + "\n\n");

        // View Event: viewed #epubcfi(/4/3/1) (George Washington)
        // TODO viewed = object or target?
        learningContext = CaliperSampleAssets.buildReadiumLearningContext();
        reading = CaliperSampleAssets.buildEpubSubChap43();
        target = CaliperSampleAssets.buildEpubSubChap431();
        incrementTime = CaliperSampleAssets.getDefaultStartedAtTime().plusSeconds(60);
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

        Sensor.send(readEvent);

        output.append("Generated ViewEvent \n");
        output.append("actor : " + ((Person) readEvent.getActor()).getId() + "\n");
        output.append("action : " + readEvent.getAction() + "\n");
        output.append("object : " + ((EpubSubChapter) readEvent.getObject()).getId() + "\n");
        output.append("target : " + ((Frame) readEvent.getTarget()).getId() + "\n\n");

        // Navigation Event: navigated to #epubcfi(/4/3/2) (Lord Cornwallis)
        learningContext = CaliperSampleAssets.buildReadiumLearningContext();
        reading = CaliperSampleAssets.buildEpubSubChap43();
        fromResource = CaliperSampleAssets.buildEpubSubChap431();
        target = CaliperSampleAssets.buildEpubSubChap432();
        incrementTime = CaliperSampleAssets.getDefaultStartedAtTime().plusSeconds(65);
        navEvent = NavigationEvent.builder()
            .edApp(learningContext.getEdApp())
            .lisOrganization(learningContext.getLisOrganization())
            .actor((Person) learningContext.getAgent())
            .object(reading)
            .action(Profile.Actions.NAVIGATED_TO.key())
            .fromResource(fromResource)
            .target(Frame.builder()
                .id(target.getId())
                .index(2)
                .build())
            .startedAtTime(incrementTime)
                .build();

        Sensor.send(navEvent);

        output.append("Generated NavigationEvent \n");
        output.append("actor : " + ((Person) navEvent.getActor()).getId() + "\n");
        output.append("action : " + navEvent.getAction() + "\n");
        output.append("object : " + ((EpubSubChapter) navEvent.getObject()).getId() + "\n");
        output.append("fromResource : " + navEvent.getFromResource().getId() + "\n");
        output.append("target : " + ((Frame) navEvent.getTarget()).getId() + "\n\n");

        // View Event: viewed #epubcfi(/4/3/2) (Lord Cornwallis)
        // TODO viewed = object or target?
        learningContext = CaliperSampleAssets.buildReadiumLearningContext();
        reading = CaliperSampleAssets.buildEpubSubChap43();
        target = CaliperSampleAssets.buildEpubSubChap432();
        incrementTime = CaliperSampleAssets.getDefaultStartedAtTime().plusSeconds(230);
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

        Sensor.send(readEvent);

        output.append("Generated ViewEvent \n");
        output.append("actor : " + ((Person) readEvent.getActor()).getId() + "\n");
        output.append("action : " + readEvent.getAction() + "\n");
        output.append("object : " + ((EpubSubChapter) readEvent.getObject()).getId() + "\n");
        output.append("target : " + ((Frame) readEvent.getTarget()).getId() + "\n\n");

        // Annotation Event: highlighted text
        learningContext = CaliperSampleAssets.buildReadiumLearningContext();
        reading = CaliperSampleAssets.buildEpubSubChap432();
        incrementTime = CaliperSampleAssets.getDefaultStartedAtTime().plusSeconds(240);
        AnnotationEvent annoEvent = AnnotationEvent.builder()
            .edApp(learningContext.getEdApp())
            .lisOrganization(learningContext.getLisOrganization())
            .actor((Person) learningContext.getAgent())
            .object(Frame.builder()
                .id(reading.getId())
                .index(2)
                .build())
            .action(AnnotationProfile.Actions.HIGHLIGHTED.key())
            .generated(HighlightAnnotation.builder()
                .id("https://someEduApp.edu/highlights/" + UUID.randomUUID().toString())
                .annotatedId(reading.getId())
                .selectionStart(Integer.toString(455))
                .selectionEnd(Integer.toString(489))
                .selectionText("Life, Liberty and the pursuit of Happiness")
                .build())
            .startedAtTime(incrementTime)
            .build();

        Sensor.send(annoEvent);

        output.append("Generated Highlight AnnotationEvent \n");
        output.append("actor : " + ((Person) annoEvent.getActor()).getId() + "\n");
        output.append("action : " + annoEvent.getAction() + "\n");
        output.append("object : " + ((Frame) annoEvent.getObject()).getId() + "\n");
        output.append("generated : " + ((HighlightAnnotation) annoEvent.getGenerated()).getId()  + "\n");
        output.append("highlighted : " + ((HighlightAnnotation) annoEvent.getGenerated()).getSelectionText() + "\n\n");

        // Navigation Event: navigated to #epubcfi(/4/3/3) (Paul Revere)
        learningContext = CaliperSampleAssets.buildReadiumLearningContext();
        reading = CaliperSampleAssets.buildEpubSubChap43();
        fromResource = CaliperSampleAssets.buildEpubSubChap432();
        target = CaliperSampleAssets.buildEpubSubChap433();
        incrementTime = CaliperSampleAssets.getDefaultStartedAtTime().plusSeconds(250);
        navEvent = NavigationEvent.builder()
            .edApp(learningContext.getEdApp())
            .lisOrganization(learningContext.getLisOrganization())
            .actor((Person) learningContext.getAgent())
            .object(reading)
            .action(Profile.Actions.NAVIGATED_TO.key())
            .fromResource(fromResource)
            .target(Frame.builder()
                .id(target.getId())
                .index(3)
                .build())
            .startedAtTime(incrementTime)
            .build();

        Sensor.send(navEvent);

        output.append("Generated NavigationEvent \n");
        output.append("actor : " + ((Person) navEvent.getActor()).getId() + "\n");
        output.append("action : " + navEvent.getAction() + "\n");
        output.append("object : " + ((EpubSubChapter) navEvent.getObject()).getId() + "\n");
        output.append("fromResource : " + navEvent.getFromResource().getId() + "\n");
        output.append("target : " + ((Frame) navEvent.getTarget()).getId() + "\n\n");

        // ViewEvent: viewed #epubcfi(/4/3/3) (Paul Revere)
        learningContext = CaliperSampleAssets.buildReadiumLearningContext();
        reading = CaliperSampleAssets.buildEpubSubChap43();
        target = CaliperSampleAssets.buildEpubSubChap433();
        incrementTime = CaliperSampleAssets.getDefaultStartedAtTime().plusSeconds(320);
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

        Sensor.send(readEvent);

        output.append("Generated ViewEvent \n");
        output.append("actor : " + ((Person) navEvent.getActor()).getId() + "\n");
        output.append("action : " + navEvent.getAction() + "\n");
        output.append("object : " + ((EpubSubChapter) readEvent.getObject()).getId() + "\n");
        output.append("target : " + ((Frame) readEvent.getTarget()).getId() + "\n\n");

        // BookmarkAnnotationEvent: bookmarked the reading with a note
        learningContext = CaliperSampleAssets.buildReadiumLearningContext();
        reading = CaliperSampleAssets.buildEpubSubChap433();
        incrementTime = CaliperSampleAssets.getDefaultStartedAtTime().plusSeconds(325);
        annoEvent = AnnotationEvent.builder()
            .edApp(learningContext.getEdApp())
            .lisOrganization(learningContext.getLisOrganization())
            .actor((Person) learningContext.getAgent())
            .action(AnnotationProfile.Actions.BOOKMARKED.key())
            .object(Frame.builder()
                .id(reading.getId())
                .index(3)
                .build())
            .generated(BookmarkAnnotation.builder()
                .id("https://someEduApp.edu/bookmarks/" + UUID.randomUUID().toString())
                .annotatedId(reading.getId())
                .bookmarkNotes("The Intolerable Acts (1774)--bad idea Lord North")
                .build())
            .startedAtTime(incrementTime)
            .build();

        Sensor.send(annoEvent);

        output.append("Generated Bookmark AnnotationEvent \n");
        output.append("actor : " + ((Person) annoEvent.getActor()).getId() + "\n");
        output.append("action : " + annoEvent.getAction() + "\n");
        output.append("object : " + ((Frame) annoEvent.getObject()).getId() + "\n");
        output.append("generated : " + ((BookmarkAnnotation) annoEvent.getGenerated()).getId()  + "\n");
        output.append("bookmark notes : " + ((BookmarkAnnotation) annoEvent.getGenerated()).getBookmarkNotes() + "\n\n");

        // TODO ADD LAUNCH EVENT

        // NavigationEvent: navigated to CourseSmart content
        learningContext = CaliperSampleAssets.buildCourseSmartLearningContext();
        reading = CaliperSampleAssets.buildAllisonAmRevEpubVolume();
        fromResource = CaliperSampleAssets.buildAmRev101LandingPage();
        incrementTime = CaliperSampleAssets.getDefaultStartedAtTime().plusSeconds(330);
        navEvent = NavigationEvent.builder()
            .edApp(learningContext.getEdApp())
            .lisOrganization(learningContext.getLisOrganization())
            .actor((Person) learningContext.getAgent())
            .object(reading)
            .action(Profile.Actions.NAVIGATED_TO.key())
            .fromResource(fromResource)
            .target(Frame.builder()
                .id(reading.getId())
                .index(0)
                .build())
            .startedAtTime(incrementTime)
            .build();

        Sensor.send(navEvent);

        output.append("Generated NavigationEvent \n");
        output.append("actor : " + ((Person) navEvent.getActor()).getId() + "\n");
        output.append("action : " + navEvent.getAction() + "\n");
        output.append("object : " + ((EpubVolume) navEvent.getObject()).getId() + "\n");
        output.append("fromResource : " + navEvent.getFromResource().getId() + "\n");
        output.append("target : " + ((Frame) navEvent.getTarget()).getId() + "\n\n");

        // NavigationEvent: navigated to aXfsadf12
        learningContext = CaliperSampleAssets.buildCourseSmartLearningContext();
        reading = CaliperSampleAssets.buildAllisonAmRevEpubVolume();
        fromResource = CaliperSampleAssets.buildAllisonAmRevEpubVolume();
        target = CaliperSampleAssets.buildAllisonAmRevEpubSubChap();
        incrementTime = CaliperSampleAssets.getDefaultStartedAtTime().plusSeconds(340);
        navEvent = NavigationEvent.builder()
            .edApp(learningContext.getEdApp())
            .lisOrganization(learningContext.getLisOrganization())
            .actor((Person) learningContext.getAgent())
            .object(reading)
            .action(Profile.Actions.NAVIGATED_TO.key())
            .fromResource(fromResource)
            .target(Frame.builder()
                .id(target.getId())
                .name(target.getName())
                .index(1)
                .build())
            .startedAtTime(incrementTime)
            .build();

        Sensor.send(navEvent);

        output.append("Generated NavigationEvent \n");
        output.append("actor : " + ((Person) navEvent.getActor()).getId() + "\n");
        output.append("action : " + navEvent.getAction() + "\n");
        output.append("object : " + ((EpubVolume) navEvent.getObject()).getId() + "\n");
        output.append("fromResource : " + navEvent.getFromResource().getId() + "\n");
        output.append("target : " + ((Frame) navEvent.getTarget()).getId() + "\n\n");

        // ViewEvent: viewed aXfsadf12
        learningContext = CaliperSampleAssets.buildCourseSmartLearningContext();
        reading = CaliperSampleAssets.buildAllisonAmRevEpubVolume();
        target = CaliperSampleAssets.buildAllisonAmRevEpubSubChap();
        incrementTime = CaliperSampleAssets.getDefaultStartedAtTime().plusSeconds(400);
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

        Sensor.send(readEvent);

        output.append("Generated ViewEvent \n");
        output.append("actor : " + ((Person) readEvent.getActor()).getId() + "\n");
        output.append("action : " + readEvent.getAction() + "\n");
        output.append("object : " + ((EpubVolume) readEvent.getObject()).getId() + "\n");
        output.append("target : " + ((Frame) readEvent.getTarget()).getId() + "\n\n");

        // Tag AnnotationEvent: tagged reading
        learningContext = CaliperSampleAssets.buildCourseSmartLearningContext();
        reading = CaliperSampleAssets.buildAllisonAmRevEpubSubChap();
        incrementTime = CaliperSampleAssets.getDefaultStartedAtTime().plusSeconds(420);
        annoEvent = AnnotationEvent.builder()
            .edApp(learningContext.getEdApp())
            .lisOrganization(learningContext.getLisOrganization())
            .actor((Person) learningContext.getAgent())
            .action(AnnotationProfile.Actions.TAGGED.key())
            .object(Frame.builder()
                .id(target.getId())
                .index(1)
                .build())
            .generated(TagAnnotation.builder()
                .id("https://someEduApp.edu/tags/" + UUID.randomUUID().toString())
                .annotatedId(reading.getId())
                .tags(Lists.newArrayList("to-read", "1776", "shared-with-project-team"))
                .build())
            .startedAtTime(incrementTime)
            .build();

        Sensor.send(annoEvent);

        output.append("Generated Tag AnnotationEvent \n");
        output.append("actor : " + ((Person) annoEvent.getActor()).getId() + "\n");
        output.append("action : " + annoEvent.getAction() + "\n");
        output.append("object : " + ((Frame) annoEvent.getObject()).getId() + "\n");
        output.append("generated : " + ((TagAnnotation) annoEvent.getGenerated()).getId()  + "\n");
        output.append("tags : " + ((TagAnnotation) annoEvent.getGenerated()).getTags().toString() + "\n\n");

        // Shared AnnotationEvent: shared reading with other students
        learningContext = CaliperSampleAssets.buildCourseSmartLearningContext();
        reading = CaliperSampleAssets.buildAllisonAmRevEpubSubChap();
        incrementTime = CaliperSampleAssets.getDefaultStartedAtTime().plusSeconds(440);
        annoEvent = AnnotationEvent.builder()
            .edApp(learningContext.getEdApp())
            .lisOrganization(learningContext.getLisOrganization())
            .actor((Person) learningContext.getAgent())
            .action(AnnotationProfile.Actions.SHARED.key())
            .object(Frame.builder()
                .id(target.getId())
                .index(1)
                .build())
            .generated(SharedAnnotation.builder()
                .id("https://someEduApp.edu/shared/" + UUID.randomUUID().toString())
                .annotatedId(reading.getId())
                .withAgents(Lists.<Agent>newArrayList(
                    Person.builder()
                        .id("https://some-university.edu/students/657585")
                        .dateCreated(CaliperSampleAssets.getDefaultDateCreated())
                        .dateModified(CaliperSampleAssets.getDefaultDateModified())
                        .build(),
                    Person.builder()
                        .id("https://some-university.edu/students/667788")
                        .dateCreated(CaliperSampleAssets.getDefaultDateCreated())
                        .dateModified(CaliperSampleAssets.getDefaultDateModified())
                        .build()))
                .build())
            .startedAtTime(incrementTime)
            .build();

        Sensor.send(annoEvent);

        output.append("Generated Shared AnnotationEvent \n");
        output.append("actor : " + ((Person) annoEvent.getActor()).getId() + "\n");
        output.append("action : " + annoEvent.getAction() + "\n");
        output.append("object : " + ((Frame) annoEvent.getObject()).getId() + "\n");
        output.append("generated : " + ((SharedAnnotation) annoEvent.getGenerated()).getId()  + "\n");

        // Retrieve agents
        SharedAnnotation shared = (SharedAnnotation) annoEvent.getGenerated();
        for (Agent agent: shared.getWithAgents()) {
            output.append("Shared with: " + ((Person) agent).getId() + "\n");
        }
        output.append("\n");

        // Session Event: logged out of Canvas LMS
        canvas = CaliperSampleAssets.buildCanvasLearningContext();

        sessionEvent = SessionEvent.builder()
            .edApp(canvas.getEdApp())
            .lisOrganization(canvas.getLisOrganization())
            .actor((Person) canvas.getAgent())
            .action(SessionProfile.Actions.LOGGEDOUT.key())
            .object(canvas.getEdApp())
            .target(CaliperSampleAssets.buildSessionEnd())
            .startedAtTime(CaliperSampleAssets.getDefaultStartedAtTime())
            .endedAtTime(CaliperSampleAssets.getDefaultEndedAtTime())
            .duration("PT3000S")
            .build();

        Sensor.send(sessionEvent);

        output.append("Generated SessionEvent \n");
        output.append("actor : " + ((Person) sessionEvent.getActor()).getId() + "\n");
        output.append("action : " + sessionEvent.getAction() + "\n");
        output.append("object : " + ((SoftwareApplication) sessionEvent.getObject()).getId() + "\n");
        output.append("target : " + ((Session) sessionEvent.getTarget()).getId() + "\n\n");

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