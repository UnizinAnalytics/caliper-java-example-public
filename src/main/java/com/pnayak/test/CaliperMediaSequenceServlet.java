package com.pnayak.test;

import com.google.common.collect.Iterables;
import org.imsglobal.caliper.CaliperSensor;
import org.imsglobal.caliper.CaliperOptions;
import org.imsglobal.caliper.actions.CaliperMediaActions;
import org.imsglobal.caliper.entities.CaliperDigitalResource;
import org.imsglobal.caliper.entities.LearningContext;
import org.imsglobal.caliper.entities.CaliperLearningObjective;
import org.imsglobal.caliper.entities.lis.LisCourseSection;
import org.imsglobal.caliper.entities.lis.LisPerson;
import org.imsglobal.caliper.entities.media.CaliperVideoObject;
import org.imsglobal.caliper.entities.media.CaliperMediaLocation;
import org.imsglobal.caliper.entities.CaliperSoftwareApplication;
import org.imsglobal.caliper.entities.CaliperWebPage;
import org.imsglobal.caliper.events.CaliperMediaEvent;
import org.imsglobal.caliper.events.CaliperNavigationEvent;
import org.imsglobal.caliper.profiles.CaliperMediaProfile;
import org.joda.time.DateTime;
import org.joda.time.Weeks;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Random;

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
        CaliperOptions options = new CaliperOptions();
        options.setHost(HOST);
        options.setApiKey(API_KEY);
        CaliperSensor.initialize(options);

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

        output.append(CaliperSensor.getStatistics().toString());

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

        /*
         * -----------------------------------------------------------------
         * Step 01. Set the learning and activity context (i.e. Video provided by the
         * LTI tool). We also assign a single learning objective to the video.
         * -----------------------------------------------------------------
         */

        // NOTE - we would want to associate the LISCourseSection with a
        // parent Department or Institution at some point

        // Learning Context
        LearningContext learningContext = LearningContext.builder()
            .edApp(CaliperSoftwareApplication.builder()
                .id("https://com.sat/super-media-tool")
                //.setType("http://purl.imsglobal.org/ctx/caliper/v1/edApp/media"); Set by default
                .lastModifiedAt(now.minus(Weeks.weeks(8)).getMillis())
                .build())
            .lisOrganization(LisCourseSection.builder()
                .id("https://some-university.edu/politicalScience/2014/american-revolution-101")
                .semester("Spring-2014")
                .courseNumber("AmRev-101")
                .label("Am Rev 101")
                .title("American Revolution 101")
                .lastModifiedAt(now.minus(Weeks.weeks(4)).getMillis())
                .build())
            .agent(LisPerson.builder()
                .id("https://some-university.edu/students/jones-alice-554433")
                .lastModifiedAt(now.minus(Weeks.weeks(3)).getMillis())
                .build())
            .build();

        // Video
        CaliperVideoObject video = CaliperVideoObject.builder()
            .id("https://com.sat/super-media-tool/video/video1")
            .name("American Revolution - Key Figures Video")
            .partOf(learningContext.getLisOrganization())
            .learningObjective(CaliperLearningObjective.builder()
                .id("http://americanrevolution.com/personalities/learn")
                .build())
            .duration(1420)
            .build();

        output.append("Generated activity context data\n\n");

        /*
         * -----------------------------------------------------------------
         * Step 02.  Execute media play sequence.
         * -----------------------------------------------------------------
         */

        output.append("Sending events . . .\n\n");

        // EVENT # 01 Generate MediaProfile triggered by NavigationEvent
        CaliperMediaProfile mediaProfile = CaliperMediaProfile.builder()
            .learningContext(learningContext)
            .mediaObject(video)
            .action(CaliperMediaActions.NAVIGATED_TO.key())
            .fromResource(CaliperWebPage.builder()
                .id("AmRev-101-landingPage")
                .name("American Revolution 101 Landing Page")
                .partOf(learningContext.getLisOrganization())
                .build())
            .target(video) // WARN: Expects a frame coordinate (none provided)
            .mediaLocation(CaliperMediaLocation.builder()
                .id(video.getId()) // Don't forget to set the Id
                .currentTime(0).build())
            .build();

        output.append("Navigated to video in Canvas LMS edApp... sent NavigateEvent\n");

        // Process Event
        navigate(mediaProfile);

        output.append("Object : " + ((CaliperVideoObject) mediaProfile.getMediaObject()).getId() + "\n");
        output.append("Media Location : " + Iterables.getLast(mediaProfile.getMediaLocations()).getCurrentTime() + "\n\n");

        // EVENT  # 02 - Start playing video
        mediaProfile.getActions().add(CaliperMediaActions.STARTED.key());
        mediaProfile.getMediaLocations().add(CaliperMediaLocation.builder()
            .id(((CaliperVideoObject) mediaProfile.getMediaObject()).getId())
            .currentTime(0).build());

        output.append("Started playing Video in Super Media edApp... sent MediaEvent\n");

        // Process Event
        media(mediaProfile);

        output.append("Object : " + ((CaliperVideoObject) mediaProfile.getMediaObject()).getId() + "\n");
        output.append("Media Location : " + Iterables.getLast(mediaProfile.getMediaLocations()).getCurrentTime() + "\n\n");

        // EVENT  # 03 - Pause playing video Event
        mediaProfile.getActions().add(CaliperMediaActions.PAUSED.key());
        mediaProfile.getMediaLocations().add(CaliperMediaLocation.builder()
            .id(((CaliperVideoObject) mediaProfile.getMediaObject()).getId())
            .currentTime(710).build());

        output.append("Paused playing video in Super Media edApp... sent MediaEvent\n");

        // Process Event
        media(mediaProfile);

        output.append("Object : " + ((CaliperVideoObject) mediaProfile.getMediaObject()).getId() + "\n");
        output.append("Media Location : " + Iterables.getLast(mediaProfile.getMediaLocations()).getCurrentTime() + "\n\n");

        // EVENT  # 04 - Resume playing video Event
        mediaProfile.getActions().add(CaliperMediaActions.RESUMED.key());
        mediaProfile.getMediaLocations().add(CaliperMediaLocation.builder()
            .id(((CaliperVideoObject) mediaProfile.getMediaObject()).getId())
            .currentTime(710).build());

        output.append("Resumed playing video in Super Media edApp... sent MediaEvent\n");

        // Process Event
        media(mediaProfile);

        output.append("Object : " + ((CaliperVideoObject) mediaProfile.getMediaObject()).getId() + "\n");
        output.append("Media Location : " + Iterables.getLast(mediaProfile.getMediaLocations()).getCurrentTime() + "\n\n");

        // EVENT  # 05 - Completed playing video Event
        mediaProfile.getActions().add(CaliperMediaActions.ENDED.key());
        mediaProfile.getMediaLocations().add(CaliperMediaLocation.builder()
            .id(((CaliperVideoObject) mediaProfile.getMediaObject()).getId())
            .currentTime(1420).build());

        output.append("Completed playing video in Super Media edApp... sent MediaEvent\n");

        // Process Event
        media(mediaProfile);

        output.append("Object : " + ((CaliperVideoObject) mediaProfile.getMediaObject()).getId() + "\n");
        output.append("Media Location : " + Iterables.getLast(mediaProfile.getMediaLocations()).getCurrentTime() + "\n\n");
        output.append("FINIS\n\n");
    }

    /*
      --------------------------------------------------------------------------------
      Methods below are utility methods for generating events. These are NOT
      part of Caliper standards work and are here only as a utility in this sample App.
      ---------------------------------------------------------------------------------
     */

    private void navigate(CaliperMediaProfile profile) {

        CaliperNavigationEvent event = CaliperNavigationEvent.builder()
            .edApp(profile.getLearningContext().getEdApp())
            .lisOrganization(profile.getLearningContext().getLisOrganization())
            .actor(profile.getLearningContext().getAgent())
            .action(Iterables.getLast(profile.getActions()))
            .object(profile.getMediaObject())
            .fromResource((CaliperDigitalResource) Iterables.getLast(profile.getFromResources()))
            .startedAtTime(DateTime.now().getMillis())
            .build();

        // Send event to EventStore
        CaliperSensor.send(event);

        // Output i18n action text
        output.append("Action : " + event.getAction() + "\n");
    }

    private void media(CaliperMediaProfile profile) {

        CaliperMediaEvent event = CaliperMediaEvent.builder()
            .edApp(profile.getLearningContext().getEdApp())
            .lisOrganization(profile.getLearningContext().getLisOrganization())
            .actor(profile.getLearningContext().getAgent())
            .action(Iterables.getLast(profile.getActions()))
            .object((CaliperVideoObject) profile.getMediaObject())
            .mediaLocation(Iterables.getLast(profile.getMediaLocations()))
            .startedAtTime(DateTime.now().getMillis())
            .build();

        // Send event to EventStore
        CaliperSensor.send(event);

        // Output i18n action text
        output.append("Action : " + event.getAction() + "\n");
    }
}