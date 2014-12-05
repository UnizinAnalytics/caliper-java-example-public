package com.pnayak.test;

import org.imsglobal.caliper.Options;
import org.imsglobal.caliper.Sensor;
import org.imsglobal.caliper.entities.LearningContext;
import org.imsglobal.caliper.entities.media.MediaLocation;
import org.imsglobal.caliper.entities.media.VideoObject;
import org.imsglobal.caliper.events.MediaEvent;
import org.imsglobal.caliper.events.NavigationEvent;
import org.imsglobal.caliper.profiles.MediaProfile;
import org.joda.time.DateTime;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Random;

import static com.pnayak.test.CaliperSampleAssets.*;

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

        // EVENT 01 Generate NavigationEvent once user launches media.
        LearningContext learningContext = buildSuperMediaToolLearningContext();
        VideoObject video = buildVideoWithLearningObjective();
        NavigationEvent navEvent = NavigationEvent.builder()
            .edApp(learningContext.getEdApp())
            .lisOrganization(learningContext.getLisOrganization())
            .actor(learningContext.getAgent())
            .object(video)
            .action(MediaProfile.Actions.NAVIGATED_TO.key())
            .fromResource(buildAmRev101LandingPage())
            .target(MediaLocation.builder()
                .id(video.getId()) // Don't forget to set the Id
                .currentTime(0).build())
            .startedAtTime(DateTime.now().minusSeconds(1000).getMillis())
            .build();

        output.append("Generated Media Tool learning context data\n");
        output.append("Generated Video activity context data\n");
        output.append("Navigated to video in Canvas LMS edApp... sent NavigateEvent\n");

        // Process Event
        Sensor.send(navEvent);

        output.append("Object : " + navEvent.getObject().getId() + "\n");
        output.append("Media Location : " + ((MediaLocation) navEvent.getTarget()).getCurrentTime() + "\n\n");

        // EVENT 02 - Start playing video
        learningContext = buildSuperMediaToolLearningContext();
        video = buildVideoWithLearningObjective();
        MediaEvent mediaEvent = MediaEvent.builder()
            .edApp(learningContext.getEdApp())
            .lisOrganization(learningContext.getLisOrganization())
            .actor(learningContext.getAgent())
            .object(video)
            .action(MediaProfile.MediaActions.STARTED.key())
            .mediaLocation(MediaLocation.builder()
                .id(video.getId()) // Don't forget to set the Id
                .currentTime(0)
                .build())
            .startedAtTime(DateTime.now().minusSeconds(2840).getMillis())
            .build();

        output.append("Generated Media Tool learning context data\n");
        output.append("Generated Video activity context data\n");
        output.append("Started playing Video in Super Media edApp... sent MediaEvent\n");

        Sensor.send(mediaEvent);

        output.append("Object : " + mediaEvent.getObject().getId() + "\n");
        output.append("Media Location : " + mediaEvent.getMediaLocation().getCurrentTime() + "\n\n");

        // EVENT 03 - Pause playing video Event
        learningContext = buildSuperMediaToolLearningContext();
        video = buildVideoWithLearningObjective();
        mediaEvent = MediaEvent.builder()
            .edApp(learningContext.getEdApp())
            .lisOrganization(learningContext.getLisOrganization())
            .actor(learningContext.getAgent())
            .object(video)
            .action(MediaProfile.MediaActions.PAUSED.key())
            .mediaLocation(MediaLocation.builder()
                .id(video.getId()) // Don't forget to set the Id
                .currentTime(710)
                .build())
            .startedAtTime(DateTime.now().minusSeconds(2130).getMillis())
            .build();

        output.append("Generated Media Tool learning context data\n");
        output.append("Generated Video activity context data\n");
        output.append("Paused playing video in Super Media edApp... sent MediaEvent\n");

        // Process Event
        Sensor.send(mediaEvent);

        output.append("Object : " + mediaEvent.getObject().getId() + "\n");
        output.append("Media Location : " + mediaEvent.getMediaLocation().getCurrentTime() + "\n\n");

        // EVENT 04 - Resume playing video Event
        learningContext = buildSuperMediaToolLearningContext();
        video = buildVideoWithLearningObjective();
        mediaEvent = MediaEvent.builder()
            .edApp(learningContext.getEdApp())
            .lisOrganization(learningContext.getLisOrganization())
            .actor(learningContext.getAgent())
            .object(video)
            .action(MediaProfile.MediaActions.RESUMED.key())
            .mediaLocation(MediaLocation.builder()
                .id(video.getId()) // Don't forget to set the Id
                .currentTime(710)
                .build())
            .startedAtTime(DateTime.now().minusSeconds(2100).getMillis())
            .build();

        output.append("Generated Media Tool learning context data\n");
        output.append("Generated Video activity context data\n");
        output.append("Resumed playing video in Super Media edApp... sent MediaEvent\n");

        // Process Event
        Sensor.send(mediaEvent);

        output.append("Object : " + mediaEvent.getObject().getId() + "\n");
        output.append("Media Location : " + mediaEvent.getMediaLocation().getCurrentTime() + "\n\n");

        // EVENT 05 - Completed playing video Event
        learningContext = buildSuperMediaToolLearningContext();
        video = buildVideoWithLearningObjective();
        mediaEvent = MediaEvent.builder()
            .edApp(learningContext.getEdApp())
            .lisOrganization(learningContext.getLisOrganization())
            .actor(learningContext.getAgent())
            .object(video)
            .action(MediaProfile.MediaActions.ENDED.key())
            .mediaLocation(MediaLocation.builder()
                .id(video.getId()) // Don't forget to set the Id
                .currentTime(1420)
                .build())
            .startedAtTime(DateTime.now().minusSeconds(1319).getMillis())
            .build();

        output.append("Generated Media Tool learning context data\n");
        output.append("Generated Video activity context data\n");
        output.append("Completed playing video in Super Media edApp... sent MediaEvent\n");

        // Process Event
        Sensor.send(mediaEvent);

        output.append("Object : " + mediaEvent.getObject().getId() + "\n");
        output.append("Media Location : " + mediaEvent.getMediaLocation().getCurrentTime() + "\n\n");
        output.append("FINIS\n\n");
    }
}