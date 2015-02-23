package com.imsglobal.caliper.example;

import org.imsglobal.caliper.Options;
import org.imsglobal.caliper.Sensor;
import org.imsglobal.caliper.entities.DigitalResource;
import org.imsglobal.caliper.entities.LearningContext;
import org.imsglobal.caliper.entities.Session;
import org.imsglobal.caliper.entities.SoftwareApplication;
import org.imsglobal.caliper.entities.lis.Person;
import org.imsglobal.caliper.entities.media.MediaLocation;
import org.imsglobal.caliper.entities.media.VideoObject;
import org.imsglobal.caliper.events.MediaEvent;
import org.imsglobal.caliper.events.NavigationEvent;
import org.imsglobal.caliper.events.SessionEvent;
import org.imsglobal.caliper.profiles.MediaProfile;
import org.imsglobal.caliper.profiles.Profile;
import org.imsglobal.caliper.profiles.SessionProfile;
import org.joda.time.DateTime;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Random;

import static com.imsglobal.caliper.example.CaliperSampleAssets.*;

/**
 * Servlet implementation class CaliperReadingSequenceServlet
 */
public class CaliperMediaSequenceServlet extends HttpServlet {

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
    public CaliperMediaSequenceServlet() {
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

        output.append("=============================================================================\n");
        output.append("Caliper Event Generator: Generating Media interaction Sequence\n");
        output.append("=============================================================================\n");

        generateMediaSequence(output);

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

    /* Student in a course interacts with multi-media entities within
        // the course. The events and entities referenced here are defined in
        // the Caliper Media Metric Profile

     */

    /**
     * --------------------------------------------------------------------
     * ------------------ Media Interaction Sequence-----------------------
     * --------------------------------------------------------------------
     * Student in a course interacts with multi-media entities within
     * the course. The events and entities referenced here are defined in
     * the Caliper Media Metric Profile
     *
     * @param output
     */
    private void generateMediaSequence(StringBuffer output) {

        // For reference, the current time
        DateTime now = DateTime.now();
        output.append(now + "\n\n");
        output.append("Sending events . . .\n\n");

        // Session Event: logged in to Media tool
        LearningContext tool = buildSuperMediaToolLearningContext();
        DigitalResource reading = buildEpubSubChap43();
        DateTime incrementTime = getDefaultStartedAtTime();

        SessionEvent sessionEvent = SessionEvent.builder()
            .edApp(tool.getEdApp())
            .lisOrganization(tool.getLisOrganization())
            .actor((Person) tool.getAgent())
            .action(SessionProfile.Actions.LOGGEDIN.key())
            .object(tool.getEdApp())
            .target(reading)
            .generated(buildSessionStart())
            .startedAtTime(incrementTime)
            .build();

        // Process Event
        Sensor.send(sessionEvent);

        output.append("Generated SessionEvent \n");
        output.append("actor : " + ((Person) sessionEvent.getActor()).getId() + "\n");
        output.append("action : " + sessionEvent.getAction() + "\n");
        output.append("object : " + ((SoftwareApplication) sessionEvent.getObject()).getId() + "\n");
        output.append("target : " + ((DigitalResource) sessionEvent.getTarget()).getId() + "\n");
        output.append("generated : " + ((Session) sessionEvent.getGenerated()).getId() + "\n\n");

        // NavigationEvent: navigated to media content
        LearningContext learningContext = buildSuperMediaToolLearningContext();
        VideoObject video = buildVideoWithLearningObjective();
        incrementTime = getDefaultStartedAtTime().plusSeconds(10);
        NavigationEvent navEvent = NavigationEvent.builder()
            .edApp(learningContext.getEdApp())
            .lisOrganization(learningContext.getLisOrganization())
            .actor((Person) learningContext.getAgent())
            .object(video)
            .action(Profile.Actions.NAVIGATED_TO.key())
            .fromResource(buildAmRev101LandingPage())
            .target(MediaLocation.builder()
                .id(video.getId()) // Don't forget to set the Id
                .currentTime(0).build())
            .startedAtTime(incrementTime)
            .build();

        // Process Event
        Sensor.send(navEvent);

        output.append("Generated NavigationEvent \n");
        output.append("actor : " + ((Person) navEvent.getActor()).getId() + "\n");
        output.append("action : " + navEvent.getAction() + "\n");
        output.append("object : " + ((DigitalResource) navEvent.getObject()).getId() + "\n");
        output.append("target media location : " + ((MediaLocation) navEvent.getTarget()).getCurrentTime() + "\n\n");

        // MediaEvent: started video
        learningContext = buildSuperMediaToolLearningContext();
        video = buildVideoWithLearningObjective();
        incrementTime = getDefaultStartedAtTime().plusSeconds(20);
        MediaEvent mediaEvent = MediaEvent.builder()
            .edApp(learningContext.getEdApp())
            .lisOrganization(learningContext.getLisOrganization())
            .actor((Person) learningContext.getAgent())
            .object(video)
            .action(MediaProfile.Actions.STARTED.key())
            .target(MediaLocation.builder()
                .id(video.getId()) // Don't forget to set the Id
                .currentTime(0)
                .build())
            .startedAtTime(incrementTime)
            .build();

        Sensor.send(mediaEvent);

        output.append("Generated MediaEvent \n");
        output.append("actor : " + ((Person) mediaEvent.getActor()).getId() + "\n");
        output.append("action : " + mediaEvent.getAction() + "\n");
        output.append("object : " + ((DigitalResource) mediaEvent.getObject()).getId() + "\n");
        output.append("target media location : " + ((MediaLocation) mediaEvent.getTarget()).getCurrentTime() + "\n\n");

        // MediaEvent: paused video
        learningContext = buildSuperMediaToolLearningContext();
        video = buildVideoWithLearningObjective();
        incrementTime = getDefaultStartedAtTime().plusSeconds(730);
        mediaEvent = MediaEvent.builder()
            .edApp(learningContext.getEdApp())
            .lisOrganization(learningContext.getLisOrganization())
            .actor((Person) learningContext.getAgent())
            .object(video)
            .action(MediaProfile.Actions.PAUSED.key())
            .target(MediaLocation.builder()
                .id(video.getId()) // Don't forget to set the Id
                .currentTime(710)
                .build())
            .startedAtTime(incrementTime)
            .build();

        // Process Event
        Sensor.send(mediaEvent);

        output.append("Generated MediaEvent \n");
        output.append("actor : " + ((Person) mediaEvent.getActor()).getId() + "\n");
        output.append("action : " + mediaEvent.getAction() + "\n");
        output.append("object : " + ((DigitalResource) mediaEvent.getObject()).getId() + "\n");
        output.append("target media location : " + ((MediaLocation) mediaEvent.getTarget()).getCurrentTime() + "\n\n");

        // MediaEvent: resume video
        learningContext = buildSuperMediaToolLearningContext();
        video = buildVideoWithLearningObjective();
        incrementTime = getDefaultStartedAtTime().plusSeconds(750);
        mediaEvent = MediaEvent.builder()
            .edApp(learningContext.getEdApp())
            .lisOrganization(learningContext.getLisOrganization())
            .actor((Person) learningContext.getAgent())
            .object(video)
            .action(MediaProfile.Actions.RESUMED.key())
            .target(MediaLocation.builder()
                .id(video.getId()) // Don't forget to set the Id
                .currentTime(710)
                .build())
            .startedAtTime(incrementTime)
            .build();

        // Process Event
        Sensor.send(mediaEvent);

        output.append("Generated MediaEvent \n");
        output.append("actor : " + ((Person) mediaEvent.getActor()).getId() + "\n");
        output.append("action : " + mediaEvent.getAction() + "\n");
        output.append("object : " + ((DigitalResource) mediaEvent.getObject()).getId() + "\n");
        output.append("target media location : " + ((MediaLocation) mediaEvent.getTarget()).getCurrentTime() + "\n\n");

        // MediaEvent: completed video
        learningContext = buildSuperMediaToolLearningContext();
        video = buildVideoWithLearningObjective();
        incrementTime = getDefaultStartedAtTime().plusSeconds(2170);
        mediaEvent = MediaEvent.builder()
            .edApp(learningContext.getEdApp())
            .lisOrganization(learningContext.getLisOrganization())
            .actor((Person) learningContext.getAgent())
            .object(video)
            .action(MediaProfile.Actions.ENDED.key())
            .target(MediaLocation.builder()
                .id(video.getId()) // Don't forget to set the Id
                .currentTime(1420)
                .build())
            .startedAtTime(incrementTime)
            .build();

        // Process Event
        Sensor.send(mediaEvent);

        output.append("Generated MediaEvent \n");
        output.append("actor : " + ((Person) mediaEvent.getActor()).getId() + "\n");
        output.append("action : " + mediaEvent.getAction() + "\n");
        output.append("object : " + ((DigitalResource) mediaEvent.getObject()).getId() + "\n");
        output.append("target media location : " + ((MediaLocation) mediaEvent.getTarget()).getCurrentTime() + "\n\n");

        // Session Event: logged out of Canvas LMS
        tool = buildSuperMediaToolLearningContext();

        sessionEvent = SessionEvent.builder()
            .edApp(tool.getEdApp())
            .lisOrganization(tool.getLisOrganization())
            .actor((Person) tool.getAgent())
            .action(SessionProfile.Actions.LOGGEDOUT.key())
            .object(tool.getEdApp())
            .target(buildSessionEnd())
            .startedAtTime(getDefaultStartedAtTime())
            .endedAtTime(getDefaultEndedAtTime())
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
}