package org.imsglobal.caliper.test.servlet;

import org.imsglobal.caliper.Client;
import org.imsglobal.caliper.Options;
import org.imsglobal.caliper.Sensor;
import org.imsglobal.caliper.actions.Action;
import org.imsglobal.caliper.entities.*;
import org.imsglobal.caliper.entities.agent.Person;
import org.imsglobal.caliper.entities.agent.SoftwareApplication;
import org.imsglobal.caliper.entities.assessment.Assessment;
import org.imsglobal.caliper.entities.assessment.AssessmentItem;
import org.imsglobal.caliper.entities.assignable.Attempt;
import org.imsglobal.caliper.entities.foaf.Agent;
import org.imsglobal.caliper.entities.outcome.Result;
import org.imsglobal.caliper.entities.reading.Frame;
import org.imsglobal.caliper.entities.reading.WebPage;
import org.imsglobal.caliper.entities.response.MultipleChoiceResponse;
import org.imsglobal.caliper.entities.session.Session;
import org.imsglobal.caliper.events.*;
import org.imsglobal.caliper.test.CaliperSampleAssets;
import org.joda.time.DateTime;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Random;

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

    Sensor<String> sensor = new Sensor();

    // Initialize the sensor - this needs to be done only once
    private void initialize() {
        Options options = new Options();
        options.setHost(HOST);
        options.setApiKey(API_KEY);

        sensor.registerClient("example", new Client(options));
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

        output.append(sensor.getStatistics().toString());

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
        output.append("Sending events  . . .\n\n");

        // Session Event: logged in to Canvas LMS
        LearningContext canvas = CaliperSampleAssets.buildCanvasLearningContext();
        DigitalResource reading = CaliperSampleAssets.buildEpubSubChap43();
        DateTime incrementTime = CaliperSampleAssets.getDefaultStartedAtTime();

        SessionEvent sessionEvent = SessionEvent.builder()
            .edApp(canvas.getEdApp())
            .actor(canvas.getAgent())
            .action(Action.LOGGED_IN)
            .object(canvas.getEdApp())
            .target(reading)
            .generated(CaliperSampleAssets.buildSessionStart())
            .startedAtTime(incrementTime)
            .build();

        sensor.send(sessionEvent);

        output.append("Generated SessionEvent \n");
        output.append("actor : " + ((Person) sessionEvent.getActor()).getId() + "\n");
        output.append("action : " + sessionEvent.getAction() + "\n");
        output.append("object : " + ((SoftwareApplication) sessionEvent.getObject()).getId() + "\n");
        output.append("target : " + ((DigitalResource) sessionEvent.getTarget()).getId() + "\n");
        output.append("generated : " + ((Session) sessionEvent.getGenerated()).getId() + "\n\n");

        //NavigationEvent: navigated to assessment
        LearningContext learningContext = CaliperSampleAssets.buildCanvasLearningContext();
        Assessment assessment = CaliperSampleAssets.buildAssessment();
        incrementTime = CaliperSampleAssets.getDefaultStartedAtTime().plusSeconds(200);
        NavigationEvent navEvent = NavigationEvent.builder()
            .edApp(learningContext.getEdApp())
            .actor((Person) learningContext.getAgent())
            .object(assessment)
            .action(Action.NAVIGATED_TO)
            .fromResource(CaliperSampleAssets.buildAmRev101LandingPage())
            .target(Frame.builder()
                    .id(assessment.getId())
                    .index(0)
                    .build())
            .startedAtTime(incrementTime)
            .build();

        // Process Event
        sensor.send(navEvent);

        output.append("Generated NavigationEvent \n");
        output.append("actor : " + ((Person) navEvent.getActor()).getId() + "\n");
        output.append("action : " + navEvent.getAction() + "\n");
        output.append("object : " + ((Assessment) navEvent.getObject()).getId() + "\n");
        output.append("fromResource : " + ((WebPage) navEvent.getFromResource()).getId() + "\n\n");
        output.append("target : " + ((Frame) navEvent.getTarget()).getId() + "\n\n");

        // AssessmentEvent: started assessment
        learningContext = CaliperSampleAssets.buildSuperAssessmentToolLearningContext();
        assessment = CaliperSampleAssets.buildAssessment();
        incrementTime = CaliperSampleAssets.getDefaultStartedAtTime().plusSeconds(210);
        AssessmentEvent assessmentEvent = AssessmentEvent.builder()
            .edApp(learningContext.getEdApp())
            .actor((Person) learningContext.getAgent())
            .object(assessment)
            .action(Action.STARTED)
            .generated(Attempt.builder()
                    .id(assessment.getId() + "/attempt1")
                    .assignable(assessment)
                    .actor(learningContext.getAgent())
                    .count(1) // First attempt
                    .startedAtTime(CaliperSampleAssets.getDefaultStartedAtTime())
                    .build())
            .startedAtTime(incrementTime)
            .build();

        // Process Event
        sensor.send(assessmentEvent);

        output.append("Generated AssessmentEvent \n");
        output.append("actor : " + ((Person) assessmentEvent.getActor()).getId() + "\n");
        output.append("action : " + assessmentEvent.getAction() + "\n");
        output.append("object : " + ((Assessment) assessmentEvent.getObject()).getId() + "\n");
        output.append("generated attempt count : " + Integer.toString(((Attempt) assessmentEvent.getGenerated()).getCount()) + "\n\n");

        // AssessmentItem Event: started item 01
        learningContext = CaliperSampleAssets.buildSuperAssessmentToolLearningContext();
        assessment = CaliperSampleAssets.buildAssessment();
        AssessmentItem item = assessment.getAssessmentItems().get(0);
        incrementTime = CaliperSampleAssets.getDefaultStartedAtTime().plusSeconds(220);
        AssessmentItemEvent itemEvent = AssessmentItemEvent.builder()
            .edApp(learningContext.getEdApp())
            .actor((Person) learningContext.getAgent())
            .object(item)
            .action(Action.STARTED)
            .generated(Attempt.builder()
                    .id(assessment.getId() + "/item1/attempt1")
                    .actor(learningContext.getAgent())
                    .assignable(item)
                    .count(1)
                    .startedAtTime(CaliperSampleAssets.getDefaultStartedAtTime())
                    .build())
            .startedAtTime(incrementTime)
            .build();

        // Process Event
        sensor.send(itemEvent);

        output.append("Generated AssessmentItemEvent \n");
        output.append("actor : " + ((Person) itemEvent.getActor()).getId() + "\n");
        output.append("action : " + itemEvent.getAction() + "\n");
        output.append("object : " + ((AssessmentItem) itemEvent.getObject()).getId() + "\n");
        output.append("generated attempt count : " + Integer.toString(((Attempt) itemEvent.getGenerated()).getCount()) + "\n\n");

        // AssessmentItemEvent: completed item 01
        learningContext = CaliperSampleAssets.buildSuperAssessmentToolLearningContext();
        assessment = CaliperSampleAssets.buildAssessment();
        item = assessment.getAssessmentItems().get(0);
        incrementTime = CaliperSampleAssets.getDefaultStartedAtTime().plusSeconds(225);
        itemEvent = AssessmentItemEvent.builder()
            .edApp(learningContext.getEdApp())
            .actor((Person) learningContext.getAgent())
            .object(item)
            .action(Action.COMPLETED)
            .generated(MultipleChoiceResponse.builder()
                .id(item.getId())
                .assignable(assessment)
                .actor(learningContext.getAgent())
                .attempt(Attempt.builder()
                    .id(assessment.getId() + "/item1/attempt1")
                    .actor(learningContext.getAgent())
                    .assignable(item)
                    .count(1)
                    .startedAtTime(CaliperSampleAssets.getDefaultStartedAtTime())
                    .build())
                .value("A")
                .startedAtTime(incrementTime)
                .build())
            .startedAtTime(incrementTime)
            .build();

        // Process Event
        sensor.send(itemEvent);

        output.append("Generated AssessmentItemEvent \n");
        output.append("actor : " + ((Person) itemEvent.getActor()).getId() + "\n");
        output.append("action : " + itemEvent.getAction() + "\n");
        output.append("object : " + ((AssessmentItem) itemEvent.getObject()).getId() + "\n");
        output.append("generated response : " + ((MultipleChoiceResponse) itemEvent.getGenerated()).getValue() + "\n\n");

        // AssessmentItemEvent: started item 02
        learningContext = CaliperSampleAssets.buildSuperAssessmentToolLearningContext();
        assessment = CaliperSampleAssets.buildAssessment();
        item = assessment.getAssessmentItems().get(1);
        incrementTime = CaliperSampleAssets.getDefaultStartedAtTime().plusSeconds(230);
        itemEvent = AssessmentItemEvent.builder()
            .edApp(learningContext.getEdApp())
            .actor((Person) learningContext.getAgent())
            .object(item)
            .action(Action.STARTED)
            .generated(Attempt.builder()
                .id(assessment.getId() + "/item2/attempt1")
                .actor((learningContext.getAgent()))
                .assignable(item)
                .count(1)
                .startedAtTime(CaliperSampleAssets.getDefaultStartedAtTime())
                .build())
            .startedAtTime(incrementTime)
            .build();

        // Process Event
        sensor.send(itemEvent);

        output.append("Generated AssessmentItemEvent \n");
        output.append("actor : " + ((Person) itemEvent.getActor()).getId() + "\n");
        output.append("action : " + itemEvent.getAction() + "\n");
        output.append("object : " + ((AssessmentItem) itemEvent.getObject()).getId() + "\n");
        output.append("generated attempt count : " + Integer.toString(((Attempt) itemEvent.getGenerated()).getCount()) + "\n\n");

        // AssessmentItemEvent: completed item 02
        learningContext = CaliperSampleAssets.buildSuperAssessmentToolLearningContext();
        assessment = CaliperSampleAssets.buildAssessment();
        item = assessment.getAssessmentItems().get(1);
        incrementTime = CaliperSampleAssets.getDefaultStartedAtTime().plusSeconds(240);
        itemEvent = AssessmentItemEvent.builder()
            .edApp(learningContext.getEdApp())
            .actor((Person) learningContext.getAgent())
            .object(item)
            .action(Action.COMPLETED)
            .generated(MultipleChoiceResponse.builder()
                .id(item.getId())
                .assignable(assessment)
                .actor(learningContext.getAgent())
                .attempt(Attempt.builder()
                        .id(assessment.getId() + "/item2/attempt1")
                        .actor(learningContext.getAgent())
                        .assignable(item)
                        .count(1)
                        .startedAtTime(CaliperSampleAssets.getDefaultStartedAtTime())
                        .build())
                .value("C")
                .startedAtTime(incrementTime)
                .build())
            .startedAtTime(incrementTime)
            .build();

        // Process Event
        sensor.send(itemEvent);

        output.append("Generated AssessmentItemEvent \n");
        output.append("actor : " + ((Person) itemEvent.getActor()).getId() + "\n");
        output.append("action : " + itemEvent.getAction() + "\n");
        output.append("object : " + ((AssessmentItem) itemEvent.getObject()).getId() + "\n");
        output.append("generated response : " + ((MultipleChoiceResponse) itemEvent.getGenerated()).getValue() + "\n\n");

        // AssessmentItemEvent: started item 03
        learningContext = CaliperSampleAssets.buildSuperAssessmentToolLearningContext();
        assessment = CaliperSampleAssets.buildAssessment();
        item = assessment.getAssessmentItems().get(2);
        incrementTime = CaliperSampleAssets.getDefaultStartedAtTime().plusSeconds(250);
        itemEvent = AssessmentItemEvent.builder()
            .edApp(learningContext.getEdApp())
            .actor((Person) learningContext.getAgent())
            .object(item)
            .action(Action.STARTED)
            .generated(Attempt.builder()
                    .id(assessment.getId() + "/item3/attempt1")
                    .actor(learningContext.getAgent())
                    .assignable(item)
                    .count(1)
                    .startedAtTime(CaliperSampleAssets.getDefaultStartedAtTime())
                    .build())
            .startedAtTime(incrementTime)
            .build();

        // Process Event
        sensor.send(itemEvent);

        output.append("Generated AssessmentItemEvent \n");
        output.append("actor : " + ((Person) itemEvent.getActor()).getId() + "\n");
        output.append("action : " + itemEvent.getAction() + "\n");
        output.append("object : " + ((AssessmentItem) itemEvent.getObject()).getId() + "\n");
        output.append("generated attempt count : " + Integer.toString(((Attempt) itemEvent.getGenerated()).getCount()) + "\n\n");

        // AssessmentItemEvent: completed item 03
        learningContext = CaliperSampleAssets.buildSuperAssessmentToolLearningContext();
        assessment = CaliperSampleAssets.buildAssessment();
        item = assessment.getAssessmentItems().get(2);
        incrementTime = CaliperSampleAssets.getDefaultStartedAtTime().plusSeconds(260);
        itemEvent = AssessmentItemEvent.builder()
            .edApp(learningContext.getEdApp())
            .actor((Person) learningContext.getAgent())
            .object(item)
            .action(Action.COMPLETED)
            .generated(MultipleChoiceResponse.builder()
                    .id(item.getId())
                    .assignable(assessment)
                    .actor(learningContext.getAgent())
                    .attempt(Attempt.builder()
                            .id(assessment.getId() + "/item3/attempt1")
                            .actor(learningContext.getAgent())
                            .assignable(item)
                            .count(1)
                            .startedAtTime(CaliperSampleAssets.getDefaultStartedAtTime())
                            .build())
                    .value("B")
                    .startedAtTime(incrementTime)
                    .build())
            .startedAtTime(incrementTime)
            .build();

        // Process Event
        sensor.send(navEvent);

        output.append("Generated AssessmentItemEvent \n");
        output.append("actor : " + ((Person) itemEvent.getActor()).getId() + "\n");
        output.append("action : " + itemEvent.getAction() + "\n");
        output.append("object : " + ((AssessmentItem) itemEvent.getObject()).getId() + "\n");
        output.append("generated response : " + ((MultipleChoiceResponse) itemEvent.getGenerated()).getValue() + "\n\n");

        // AssessmentEvent: submitted assessment
        learningContext = CaliperSampleAssets.buildSuperAssessmentToolLearningContext();
        assessment = CaliperSampleAssets.buildAssessment();
        incrementTime = CaliperSampleAssets.getDefaultStartedAtTime().plusSeconds(270);
        assessmentEvent = AssessmentEvent.builder()
            .edApp(learningContext.getEdApp())
            .actor((Person) learningContext.getAgent())
            .object(assessment)
            .action(Action.SUBMITTED)
            .generated(Attempt.builder()
                .id(assessment.getId() + "/attempt1")
                .assignable(assessment)
                .actor(learningContext.getAgent())
                .count(1) // First attempt
                .startedAtTime(CaliperSampleAssets.getDefaultStartedAtTime())
                .build())
            .startedAtTime(incrementTime)
            .build();

        // Process Event
        sensor.send(assessmentEvent);

        output.append("Generated AssessmentEvent \n");
        output.append("actor : " + ((Person) assessmentEvent.getActor()).getId() + "\n");
        output.append("action : " + assessmentEvent.getAction() + "\n");
        output.append("object : " + ((Assessment) assessmentEvent.getObject()).getId() + "\n");
        output.append("generated attempt : " + Integer.toString(((Attempt) assessmentEvent.getGenerated()).getCount()) + "\n\n");

        // Session Event: logged out of Canvas LMS
        canvas = CaliperSampleAssets.buildCanvasLearningContext();

        sessionEvent = SessionEvent.builder()
                .edApp(canvas.getEdApp())
                .actor((Person) canvas.getAgent())
                .action(Action.LOGGED_OUT)
                .object(canvas.getEdApp())
                .target(CaliperSampleAssets.buildSessionEnd())
                .startedAtTime(CaliperSampleAssets.getDefaultStartedAtTime())
                .endedAtTime(CaliperSampleAssets.getDefaultEndedAtTime())
                .duration("PT3000S")
            .build();

        sensor.send(sessionEvent);

        output.append("Generated SessionEvent \n");
        output.append("actor : " + ((Person) sessionEvent.getActor()).getId() + "\n");
        output.append("action : " + sessionEvent.getAction() + "\n");
        output.append("object : " + ((SoftwareApplication) sessionEvent.getObject()).getId() + "\n");
        output.append("target : " + ((Session) sessionEvent.getTarget()).getId() + "\n\n");

        // OutcomeEvent: generated result
        learningContext = CaliperSampleAssets.buildSuperAssessmentToolLearningContext();
        assessment = CaliperSampleAssets.buildAssessment();
        Agent gradingEngine = learningContext.getEdApp();
        incrementTime = CaliperSampleAssets.getDefaultStartedAtTime().plusSeconds(280);
        OutcomeEvent outcomeEvent = OutcomeEvent.builder()
            .edApp(learningContext.getEdApp())
            .actor(learningContext.getAgent())
            .object(Attempt.builder()
                    .id(assessment.getId() + "/attempt1")
                    .assignable(assessment)
                    .actor(learningContext.getAgent())
                    .count(1) // First attempt
                    .startedAtTime(CaliperSampleAssets.getDefaultStartedAtTime())
                    .build())
            .action(Action.GRADED)
            .generated(Result.builder()
                    .id("https://some-university.edu/politicalScience/2014/american-revolution-101/activityContext1/attempt1/result")
                    .totalScore(4.2d)
                    .normalScore(4.2d)
                    .scoredBy(gradingEngine)
                    .build())
            .startedAtTime(incrementTime)
            .build();

        // Process Event
        sensor.send(outcomeEvent);

        output.append("Generated OutcomeEvent \n");
        output.append("actor : " + ((Person) outcomeEvent.getActor()).getId() + "\n");
        output.append("action : " + outcomeEvent.getAction() + "\n");
        output.append("object : " + ((Attempt) outcomeEvent.getObject()).getId() + "\n");
        output.append("attempt count : " + Integer.toString(((Attempt) outcomeEvent.getObject()).getCount()) + "\n");
        output.append("generated outcome : " + String.valueOf(((Result) outcomeEvent.getGenerated()).getTotalScore()) + "\n");
        output.append("scored by : " + ((Result) outcomeEvent.getGenerated()).getScoredBy() + "\n\n");

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