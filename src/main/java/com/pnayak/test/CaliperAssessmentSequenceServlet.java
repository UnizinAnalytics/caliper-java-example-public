package com.pnayak.test;

import org.imsglobal.caliper.Options;
import org.imsglobal.caliper.Sensor;
import org.imsglobal.caliper.entities.Agent;
import org.imsglobal.caliper.entities.LearningContext;
import org.imsglobal.caliper.entities.WebPage;
import org.imsglobal.caliper.entities.assessment.Assessment;
import org.imsglobal.caliper.entities.assessment.AssessmentItem;
import org.imsglobal.caliper.entities.assignable.Attempt;
import org.imsglobal.caliper.entities.outcome.Result;
import org.imsglobal.caliper.entities.reading.Frame;
import org.imsglobal.caliper.events.AssessmentEvent;
import org.imsglobal.caliper.events.AssessmentItemEvent;
import org.imsglobal.caliper.events.NavigationEvent;
import org.imsglobal.caliper.events.OutcomeEvent;
import org.imsglobal.caliper.profiles.AssessmentItemProfile;
import org.imsglobal.caliper.profiles.AssessmentProfile;
import org.joda.time.DateTime;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Random;

import static com.pnayak.test.CaliperSampleAssets.*;

/**
 * Servlet implementation class CaliperAssessmentServlet
 */
public class CaliperAssessmentSequenceServlet extends HttpServlet {

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
    public CaliperAssessmentSequenceServlet() {
        initialize();
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
     *      response)
     */
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response) throws ServletException, IOException {

        // Invoke the Caliper sensor, send a set of Caliper Events
        //StringBuffer output = new StringBuffer();

        output.append("=============================================================================\n");
        output.append("Caliper Event Generator: Generating Assignable, Assessment, Outcome Sequence\n");
        output.append("=============================================================================\n");

        generateAAOSequence(output);

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
     * -----------------------------------------------------------------
     * ------------Assignable, Assessment, Outcome Sequence-------------
     * -----------------------------------------------------------------
     * Student in a course interacts with assignable entities within
     * the course. The assignable here is an Assessment.
     * In the process of interacting, she performs various assignable
     * and activityContext related interactions. These are defined in
     * the Caliper Assignable, Assessment and Outcomes profiles respectively
     *
     * @param output
     */
    private void generateAAOSequence(StringBuffer output) {

        // For reference, the current time
        DateTime now = DateTime.now();
        output.append(now + "\n\n");

        output.append("Generated LMS learning context data\n");
        output.append("Generated activity context data\n");
        output.append("Sending events  . . .\n\n");

        // EVENT 01 -  Generate navigation event when user launches assessment
        LearningContext learningContext = buildCanvasLearningContext();
        Assessment assessment = buildAssessment();
        NavigationEvent navEvent = NavigationEvent.builder()
            .edApp(learningContext.getEdApp())
            .lisOrganization(learningContext.getLisOrganization())
            .actor(learningContext.getAgent())
            .object(assessment)
            .action(AssessmentProfile.Actions.NAVIGATED_TO.key())
            .fromResource(WebPage.builder()
                .id("AmRev-101-landingPage")
                .name("American Revolution 101 Landing Page")
                .partOf(learningContext.getLisOrganization())
                .build())
            .target(Frame.builder()
                .id(assessment.getId())
                .index(0)
                .build())
            .startedAtTime(DateTime.now().minusSeconds(1000).getMillis())
            .build();

        output.append("Generated LMS learning context data\n");
        output.append("Generated Assessment activity context data\n");
        output.append("Navigated to Assessment.  Sent NavigationEvent\n");

        // Process Event
        Sensor.send(navEvent);

        output.append("Object : " + navEvent.getObject().getId() + "\n\n");

        // EVENT 02 - Started Assessment Event
        learningContext = buildSuperAssessmentToolLearningContext();
        assessment = buildAssessment();
        AssessmentEvent assessmentEvent = AssessmentEvent.builder()
            .edApp(learningContext.getEdApp())
            .lisOrganization(learningContext.getLisOrganization())
            .actor(learningContext.getAgent())
            .object(assessment)
            .action(AssessmentProfile.Actions.NAVIGATED_TO.key())
            .generated(Attempt.builder()
                .id(assessment.getId() + "/attempt1")
                .assignable(assessment)
                .actor(learningContext.getAgent())
                .count(1) // First attempt
                .build())
            .startedAtTime(DateTime.now().minusSeconds(980).getMillis())
            .build();

        output.append("Generated Tool learning context data\n");
        output.append("Generated Assessment activity context data\n");
        output.append("Started Assessment.  Sent AssessmentEvent.\n");

        // Process Event
        Sensor.send(assessmentEvent);

        output.append("Object : " + assessmentEvent.getObject().getId() + "\n");
        output.append("Attempt : " + Integer.toString(assessmentEvent.getGenerated().getCount()) + "\n\n");

        // EVENT 03 - Started AssessmentItem 01
        learningContext = buildSuperAssessmentToolLearningContext();
        assessment = buildAssessment();
        AssessmentItem item = assessment.getAssessmentItems().get(0);
        AssessmentItemEvent itemEvent = AssessmentItemEvent.builder()
            .edApp(learningContext.getEdApp())
            .lisOrganization(learningContext.getLisOrganization())
            .actor(learningContext.getAgent())
            .object(item)
            .action(AssessmentItemProfile.AssessmentItemActions.STARTED.key())
            .startedAtTime(DateTime.now().minusSeconds(960).getMillis())
            .build();

        output.append("Generated Tool learning context data\n");
        output.append("Generated Assessment activity context data\n");
        output.append("Started AssessmentItem 01.  Sent AssessmentItemEvent.\n");

        // Process Event
        Sensor.send(itemEvent);

        output.append("Object : " + itemEvent.getObject().getId() + "\n\n");

        // EVENT 04 - Completed AssessmentItem 01
        learningContext = buildSuperAssessmentToolLearningContext();
        assessment = buildAssessment();
        item = assessment.getAssessmentItems().get(0);
        itemEvent = AssessmentItemEvent.builder()
            .edApp(learningContext.getEdApp())
            .lisOrganization(learningContext.getLisOrganization())
            .actor(learningContext.getAgent())
            .object(item)
            .action(AssessmentItemProfile.AssessmentItemActions.COMPLETED.key())
            /**
            .generated(Response.builder()
                .id(item.getId())
                .assignable(assessment)
                .actor(learningContext.getAgent())
                .response("A")
                .build())
             */
            .startedAtTime(DateTime.now().minusSeconds(940).getMillis())
            .build();

        output.append("Generated Tool learning context data\n");
        output.append("Generated Assessment activity context data\n");
        output.append("Completed AssessmentItem 01.  Sent AssessmentItemEvent.\n");

        // Process Event
        Sensor.send(itemEvent);

        output.append("Object : " + itemEvent.getObject().getId() + "\n");
        // output.append("Response : " + itemEvent.getGenerated().getResponse() + "\n\n");

        // EVENT 05 - Started AssessmentItem 02
        learningContext = buildSuperAssessmentToolLearningContext();
        assessment = buildAssessment();
        item = assessment.getAssessmentItems().get(1);
        itemEvent = AssessmentItemEvent.builder()
            .edApp(learningContext.getEdApp())
            .lisOrganization(learningContext.getLisOrganization())
            .actor(learningContext.getAgent())
            .object(item)
            .action(AssessmentItemProfile.AssessmentItemActions.STARTED.key())
            .startedAtTime(DateTime.now().minusSeconds(920).getMillis())
            .build();

        output.append("Generated Tool learning context data\n");
        output.append("Generated Assessment activity context data\n");
        output.append("Started AssessmentItem 02.  Sent AssessmentItemEvent.\n");

        // Process Event
        Sensor.send(itemEvent);

        output.append("Object : " + itemEvent.getObject().getId() + "\n\n");

        // EVENT 06 - Completed AssessmentItem 02
        learningContext = buildSuperAssessmentToolLearningContext();
        assessment = buildAssessment();
        item = assessment.getAssessmentItems().get(1);
        itemEvent = AssessmentItemEvent.builder()
            .edApp(learningContext.getEdApp())
            .lisOrganization(learningContext.getLisOrganization())
            .actor(learningContext.getAgent())
            .object(item)
            .action(AssessmentItemProfile.AssessmentItemActions.COMPLETED.key())
            /**
            .generated(Response.builder()
                .id(item.getId())
                .assignable(assessment)
                .actor(learningContext.getAgent())
                .response("C")
                .build())
             */
            .startedAtTime(DateTime.now().minusSeconds(900).getMillis())
            .build();

        output.append("Generated Tool learning context data\n");
        output.append("Generated Assessment activity context data\n");
        output.append("Completed AssessmentItem 02.  Sent AssessmentItemEvent.\n");

        // Process Event
        Sensor.send(itemEvent);

        output.append("Object : " + itemEvent.getObject().getId() + "\n");
        // output.append("Response : " + itemEvent.getGenerated().getResponse() + "\n\n");

        // EVENT 07 - Started AssessmentItem 03
        learningContext = buildSuperAssessmentToolLearningContext();
        assessment = buildAssessment();
        item = assessment.getAssessmentItems().get(2);
        itemEvent = AssessmentItemEvent.builder()
            .edApp(learningContext.getEdApp())
            .lisOrganization(learningContext.getLisOrganization())
            .actor(learningContext.getAgent())
            .object(item)
            .action(AssessmentItemProfile.AssessmentItemActions.STARTED.key())
            .startedAtTime(DateTime.now().minusSeconds(880).getMillis())
            .build();

        output.append("Generated Tool learning context data\n");
        output.append("Generated Assessment activity context data\n");
        output.append("Started AssessmentItem 03.  Sent AssessmentItemEvent.\n");

        // Process Event
        Sensor.send(itemEvent);

        output.append("Object : " + itemEvent.getObject().getId() + "\n\n");

        // EVENT 08 - Completed AssessmentItem 03
        learningContext = buildSuperAssessmentToolLearningContext();
        assessment = buildAssessment();
        item = assessment.getAssessmentItems().get(2);
        itemEvent = AssessmentItemEvent.builder()
            .edApp(learningContext.getEdApp())
            .lisOrganization(learningContext.getLisOrganization())
            .actor(learningContext.getAgent())
            .object(item)
            .action(AssessmentItemProfile.AssessmentItemActions.COMPLETED.key())
            /**
            .generated(Response.builder()
                .id(item.getId())
                .assignable(assessment)
                .actor(learningContext.getAgent())
                .response("B")
                .build())
            */
            .startedAtTime(DateTime.now().minusSeconds(860).getMillis())
            .build();

        output.append("Generated Tool learning context data\n");
        output.append("Generated Assessment activity context data\n");
        output.append("Completed AssessmentItem 03 in Super Assessment edApp . . . sent AssessmentItemEvent.\n");

        // Process Event
        Sensor.send(navEvent);

        output.append("Object : " + itemEvent.getObject().getId() + "\n");
        // output.append("Response : " + itemEvent.getGenerated().getResponse() + "\n\n");

        // EVENT # 09 - Submitted Assessment Event
        learningContext = buildSuperAssessmentToolLearningContext();
        assessment = buildAssessment();
        assessmentEvent = AssessmentEvent.builder()
            .edApp(learningContext.getEdApp())
            .lisOrganization(learningContext.getLisOrganization())
            .actor(learningContext.getAgent())
            .object(assessment)
            .action(AssessmentProfile.AssessmentActions.SUBMITTED.key())
            .generated(Attempt.builder()
                .id(assessment.getId() + "/attempt1")
                .assignable(assessment)
                .actor(learningContext.getAgent())
                .count(1) // First attempt
                .build())
            .startedAtTime(DateTime.now().minusSeconds(840).getMillis())
            .build();

        output.append("Generated Tool learning context data\n");
        output.append("Generated Assessment activity context data\n");
        output.append("Submitted Assessment.  Sent AssessmentEvent.\n");

        // Process Event
        Sensor.send(assessmentEvent);

        output.append("Object : " + assessmentEvent.getObject().getId() + "\n\n");

        // EVENT # 10 Generate OutcomeProfile triggered by Outcome Event
        learningContext = buildSuperAssessmentToolLearningContext();
        assessment = buildAssessment();
        Agent gradingEngine = learningContext.getEdApp();
        OutcomeEvent outcomeEvent = OutcomeEvent.builder()
            .edApp(learningContext.getEdApp())
            .lisOrganization(learningContext.getLisOrganization())
            .actor(learningContext.getAgent())
            .object(Attempt.builder()
                .id(assessment.getId() + "/attempt1")
                .assignable(assessment)
                .actor(learningContext.getAgent())
                .count(1) // First attempt
                .build())
            .action(AssessmentProfile.AssessmentActions.SUBMITTED.key())
            .generated(Result.builder()
                .id("https://some-university.edu/politicalScience/2014/american-revolution-101/activityContext1/attempt1/result")
                .totalScore(4.2d)
                .normalScore(4.2d)
                .scoredBy(gradingEngine)
                .build())
            .startedAtTime(DateTime.now().minusSeconds(840).getMillis())
            .build();

        output.append("Generated Tool learning context data\n");
        output.append("Generated Outcome context data\n");
        output.append("Submitted activityContext auto-graded by Super Assessment edApp . . . sent OutcomeEvent.\n");

        // Process Event
        Sensor.send(outcomeEvent);

        output.append("Object : " + outcomeEvent.getObject().getId() + "\n");
        output.append("Attempt : " + Integer.toString(outcomeEvent.getObject().getCount()) + "\n");
        output.append("Generated (Score) : " + String.valueOf(outcomeEvent.getGenerated().getTotalScore()) + "\n\n");
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
        return r.nextInt((end - start) + start);
    }
}