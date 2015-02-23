package com.imsglobal.caliper.example;

import org.imsglobal.caliper.Options;
import org.imsglobal.caliper.Sensor;
import org.imsglobal.caliper.entities.*;
import org.imsglobal.caliper.entities.assessment.Assessment;
import org.imsglobal.caliper.entities.assessment.AssessmentItem;
import org.imsglobal.caliper.entities.assignable.Attempt;
import org.imsglobal.caliper.entities.foaf.Agent;
import org.imsglobal.caliper.entities.lis.Person;
import org.imsglobal.caliper.entities.outcome.Result;
import org.imsglobal.caliper.entities.reading.Frame;
import org.imsglobal.caliper.events.*;
import org.imsglobal.caliper.profiles.*;
import org.imsglobal.caliper.response.MultipleChoiceResponse;
import org.joda.time.DateTime;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Random;

import static com.imsglobal.caliper.example.CaliperSampleAssets.*;

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
        output.append("Sending events  . . .\n\n");

        // Session Event: logged in to Canvas LMS
        LearningContext canvas = buildCanvasLearningContext();
        DigitalResource reading = buildEpubSubChap43();
        DateTime incrementTime = getDefaultStartedAtTime();

        SessionEvent sessionEvent = SessionEvent.builder()
            .edApp(canvas.getEdApp())
            .lisOrganization(canvas.getLisOrganization())
            .actor((Person) canvas.getAgent())
            .action(SessionProfile.Actions.LOGGEDIN.key())
            .object(canvas.getEdApp())
            .target(reading)
            .generated(buildSessionStart())
            .startedAtTime(incrementTime)
            .build();

        Sensor.send(sessionEvent);

        output.append("Generated SessionEvent \n");
        output.append("actor : " + ((Person) sessionEvent.getActor()).getId() + "\n");
        output.append("action : " + sessionEvent.getAction() + "\n");
        output.append("object : " + ((SoftwareApplication) sessionEvent.getObject()).getId() + "\n");
        output.append("target : " + ((DigitalResource) sessionEvent.getTarget()).getId() + "\n");
        output.append("generated : " + ((Session) sessionEvent.getGenerated()).getId() + "\n\n");

        //NavigationEvent: navigated to assessment
        LearningContext learningContext = buildCanvasLearningContext();
        Assessment assessment = buildAssessment();
        incrementTime = getDefaultStartedAtTime().plusSeconds(200);
        NavigationEvent navEvent = NavigationEvent.builder()
            .edApp(learningContext.getEdApp())
            .lisOrganization(learningContext.getLisOrganization())
            .actor((Person) learningContext.getAgent())
            .object(assessment)
            .action(Profile.Actions.NAVIGATED_TO.key())
            .fromResource(buildAmRev101LandingPage())
            .target(Frame.builder()
                .id(assessment.getId())
                .index(0)
                .build())
            .startedAtTime(incrementTime)
            .build();

        // Process Event
        Sensor.send(navEvent);

        output.append("Generated NavigationEvent \n");
        output.append("actor : " + ((Person) navEvent.getActor()).getId() + "\n");
        output.append("action : " + navEvent.getAction() + "\n");
        output.append("object : " + ((Assessment) navEvent.getObject()).getId() + "\n");
        output.append("fromResource : " + ((WebPage) navEvent.getFromResource()).getId() + "\n\n");
        output.append("target : " + ((Frame) navEvent.getTarget()).getId() + "\n\n");

        // AssessmentEvent: started assessment
        learningContext = buildSuperAssessmentToolLearningContext();
        assessment = buildAssessment();
        incrementTime = getDefaultStartedAtTime().plusSeconds(210);
        AssessmentEvent assessmentEvent = AssessmentEvent.builder()
            .edApp(learningContext.getEdApp())
            .lisOrganization(learningContext.getLisOrganization())
            .actor((Person) learningContext.getAgent())
            .object(assessment)
            .action(AssessmentProfile.Actions.STARTED.key())
            .generated(Attempt.builder()
                .id(assessment.getId() + "/attempt1")
                .assignableId(assessment.getId())
                .actorId(((Person) learningContext.getAgent()).getId())
                .count(1) // First attempt
                .startedAtTime(getDefaultStartedAtTime())
                .build())
            .startedAtTime(incrementTime)
            .build();

        // Process Event
        Sensor.send(assessmentEvent);

        output.append("Generated AssessmentEvent \n");
        output.append("actor : " + ((Person) assessmentEvent.getActor()).getId() + "\n");
        output.append("action : " + assessmentEvent.getAction() + "\n");
        output.append("object : " + ((Assessment) assessmentEvent.getObject()).getId() + "\n");
        output.append("generated attempt count : " + Integer.toString(((Attempt) assessmentEvent.getGenerated()).getCount()) + "\n\n");

        // AssessmentItem Event: started item 01
        learningContext = buildSuperAssessmentToolLearningContext();
        assessment = buildAssessment();
        AssessmentItem item = assessment.getAssessmentItems().get(0);
        incrementTime = getDefaultStartedAtTime().plusSeconds(220);
        AssessmentItemEvent itemEvent = AssessmentItemEvent.builder()
            .edApp(learningContext.getEdApp())
            .lisOrganization(learningContext.getLisOrganization())
            .actor((Person) learningContext.getAgent())
            .object(item)
            .action(AssessmentItemProfile.Actions.STARTED.key())
            .generated(Attempt.builder()
                .id(assessment.getId() + "/item1/attempt1")
                .actorId(((Person) learningContext.getAgent()).getId())
                .assignableId(item.getId())
                .count(1)
                .startedAtTime(getDefaultStartedAtTime())
                .build())
            .startedAtTime(incrementTime)
            .build();

        // Process Event
        Sensor.send(itemEvent);

        output.append("Generated AssessmentItemEvent \n");
        output.append("actor : " + ((Person) itemEvent.getActor()).getId() + "\n");
        output.append("action : " + itemEvent.getAction() + "\n");
        output.append("object : " + ((AssessmentItem) itemEvent.getObject()).getId() + "\n");
        output.append("generated attempt count : " + Integer.toString(((Attempt) itemEvent.getGenerated()).getCount()) + "\n\n");

        // AssessmentItemEvent: completed item 01
        learningContext = buildSuperAssessmentToolLearningContext();
        assessment = buildAssessment();
        item = assessment.getAssessmentItems().get(0);
        incrementTime = getDefaultStartedAtTime().plusSeconds(225);
        itemEvent = AssessmentItemEvent.builder()
            .edApp(learningContext.getEdApp())
            .lisOrganization(learningContext.getLisOrganization())
            .actor((Person) learningContext.getAgent())
            .object(item)
            .action(AssessmentItemProfile.Actions.COMPLETED.key())
            .generated(MultipleChoiceResponse.builder()
                .id(item.getId())
                .assignableId(assessment.getId())
                .actorId(((Person) learningContext.getAgent()).getId())
                .attempt(Attempt.builder()
                    .id(assessment.getId() + "/item1/attempt1")
                    .actorId(((Person) learningContext.getAgent()).getId())
                    .assignableId(item.getId())
                    .count(1)
                    .startedAtTime(getDefaultStartedAtTime())
                    .build())
                .value("A")
                .startedAtTime(incrementTime)
                .build())
            .startedAtTime(incrementTime)
            .build();

        // Process Event
        Sensor.send(itemEvent);

        output.append("Generated AssessmentItemEvent \n");
        output.append("actor : " + ((Person) itemEvent.getActor()).getId() + "\n");
        output.append("action : " + itemEvent.getAction() + "\n");
        output.append("object : " + ((AssessmentItem) itemEvent.getObject()).getId() + "\n");
        output.append("generated response : " + ((MultipleChoiceResponse) itemEvent.getGenerated()).getValue() + "\n\n");

        // AssessmentItemEvent: started item 02
        learningContext = buildSuperAssessmentToolLearningContext();
        assessment = buildAssessment();
        item = assessment.getAssessmentItems().get(1);
        incrementTime = getDefaultStartedAtTime().plusSeconds(230);
        itemEvent = AssessmentItemEvent.builder()
            .edApp(learningContext.getEdApp())
            .lisOrganization(learningContext.getLisOrganization())
            .actor((Person) learningContext.getAgent())
            .object(item)
            .action(AssessmentItemProfile.Actions.STARTED.key())
            .generated(Attempt.builder()
                .id(assessment.getId() + "/item2/attempt1")
                .actorId(((Person) learningContext.getAgent()).getId())
                .assignableId(item.getId())
                .count(1)
                .startedAtTime(getDefaultStartedAtTime())
                .build())
            .startedAtTime(incrementTime)
            .build();

        // Process Event
        Sensor.send(itemEvent);

        output.append("Generated AssessmentItemEvent \n");
        output.append("actor : " + ((Person) itemEvent.getActor()).getId() + "\n");
        output.append("action : " + itemEvent.getAction() + "\n");
        output.append("object : " + ((AssessmentItem) itemEvent.getObject()).getId() + "\n");
        output.append("generated attempt count : " + Integer.toString(((Attempt) itemEvent.getGenerated()).getCount()) + "\n\n");

        // AssessmentItemEvent: completed item 02
        learningContext = buildSuperAssessmentToolLearningContext();
        assessment = buildAssessment();
        item = assessment.getAssessmentItems().get(1);
        incrementTime = getDefaultStartedAtTime().plusSeconds(240);
        itemEvent = AssessmentItemEvent.builder()
            .edApp(learningContext.getEdApp())
            .lisOrganization(learningContext.getLisOrganization())
            .actor((Person) learningContext.getAgent())
            .object(item)
            .action(AssessmentItemProfile.Actions.COMPLETED.key())
            .generated(MultipleChoiceResponse.builder()
                .id(item.getId())
                .assignableId(assessment.getId())
                .actorId(((Person) learningContext.getAgent()).getId())
                .attempt(Attempt.builder()
                        .id(assessment.getId() + "/item2/attempt1")
                        .actorId(((Person) learningContext.getAgent()).getId())
                        .assignableId(item.getId())
                        .count(1)
                        .startedAtTime(getDefaultStartedAtTime())
                        .build())
                .value("C")
                .startedAtTime(incrementTime)
                .build())
            .startedAtTime(incrementTime)
            .build();

        // Process Event
        Sensor.send(itemEvent);

        output.append("Generated AssessmentItemEvent \n");
        output.append("actor : " + ((Person) itemEvent.getActor()).getId() + "\n");
        output.append("action : " + itemEvent.getAction() + "\n");
        output.append("object : " + ((AssessmentItem) itemEvent.getObject()).getId() + "\n");
        output.append("generated response : " + ((MultipleChoiceResponse) itemEvent.getGenerated()).getValue() + "\n\n");

        // AssessmentItemEvent: started item 03
        learningContext = buildSuperAssessmentToolLearningContext();
        assessment = buildAssessment();
        item = assessment.getAssessmentItems().get(2);
        incrementTime = getDefaultStartedAtTime().plusSeconds(250);
        itemEvent = AssessmentItemEvent.builder()
            .edApp(learningContext.getEdApp())
            .lisOrganization(learningContext.getLisOrganization())
            .actor((Person) learningContext.getAgent())
            .object(item)
            .action(AssessmentItemProfile.Actions.STARTED.key())
            .generated(Attempt.builder()
                    .id(assessment.getId() + "/item3/attempt1")
                    .actorId(((Person) learningContext.getAgent()).getId())
                    .assignableId(item.getId())
                    .count(1)
                    .startedAtTime(getDefaultStartedAtTime())
                    .build())
            .startedAtTime(incrementTime)
            .build();

        // Process Event
        Sensor.send(itemEvent);

        output.append("Generated AssessmentItemEvent \n");
        output.append("actor : " + ((Person) itemEvent.getActor()).getId() + "\n");
        output.append("action : " + itemEvent.getAction() + "\n");
        output.append("object : " + ((AssessmentItem) itemEvent.getObject()).getId() + "\n");
        output.append("generated attempt count : " + Integer.toString(((Attempt) itemEvent.getGenerated()).getCount()) + "\n\n");

        // AssessmentItemEvent: completed item 03
        learningContext = buildSuperAssessmentToolLearningContext();
        assessment = buildAssessment();
        item = assessment.getAssessmentItems().get(2);
        incrementTime = getDefaultStartedAtTime().plusSeconds(260);
        itemEvent = AssessmentItemEvent.builder()
            .edApp(learningContext.getEdApp())
            .lisOrganization(learningContext.getLisOrganization())
            .actor((Person) learningContext.getAgent())
            .object(item)
            .action(AssessmentItemProfile.Actions.COMPLETED.key())
            .generated(MultipleChoiceResponse.builder()
                .id(item.getId())
                .assignableId(assessment.getId())
                .actorId(((Person) learningContext.getAgent()).getId())
                    .attempt(Attempt.builder()
                            .id(assessment.getId() + "/item3/attempt1")
                            .actorId(((Person) learningContext.getAgent()).getId())
                            .assignableId(item.getId())
                            .count(1)
                            .startedAtTime(getDefaultStartedAtTime())
                            .build())
                .value("B")
                .startedAtTime(incrementTime)
                .build())
            .startedAtTime(incrementTime)
            .build();

        // Process Event
        Sensor.send(navEvent);

        output.append("Generated AssessmentItemEvent \n");
        output.append("actor : " + ((Person) itemEvent.getActor()).getId() + "\n");
        output.append("action : " + itemEvent.getAction() + "\n");
        output.append("object : " + ((AssessmentItem) itemEvent.getObject()).getId() + "\n");
        output.append("generated response : " + ((MultipleChoiceResponse) itemEvent.getGenerated()).getValue() + "\n\n");

        // AssessmentEvent: submitted assessment
        learningContext = buildSuperAssessmentToolLearningContext();
        assessment = buildAssessment();
        incrementTime = getDefaultStartedAtTime().plusSeconds(270);
        assessmentEvent = AssessmentEvent.builder()
            .edApp(learningContext.getEdApp())
            .lisOrganization(learningContext.getLisOrganization())
            .actor((Person) learningContext.getAgent())
            .object(assessment)
            .action(AssessmentProfile.Actions.SUBMITTED.key())
            .generated(Attempt.builder()
                .id(assessment.getId() + "/attempt1")
                .assignableId(assessment.getId())
                .actorId(((Person) learningContext.getAgent()).getId())
                .count(1) // First attempt
                .startedAtTime(getDefaultStartedAtTime())
                .build())
            .startedAtTime(incrementTime)
            .build();

        // Process Event
        Sensor.send(assessmentEvent);

        output.append("Generated AssessmentEvent \n");
        output.append("actor : " + ((Person) assessmentEvent.getActor()).getId() + "\n");
        output.append("action : " + assessmentEvent.getAction() + "\n");
        output.append("object : " + ((Assessment) assessmentEvent.getObject()).getId() + "\n");
        output.append("generated attempt : " + Integer.toString(((Attempt) assessmentEvent.getGenerated()).getCount()) + "\n\n");

        // Session Event: logged out of Canvas LMS
        canvas = buildCanvasLearningContext();

        sessionEvent = SessionEvent.builder()
                .edApp(canvas.getEdApp())
                .lisOrganization(canvas.getLisOrganization())
                .actor((Person) canvas.getAgent())
                .action(SessionProfile.Actions.LOGGEDOUT.key())
                .object(canvas.getEdApp())
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

        // OutcomeEvent: generated result
        learningContext = buildSuperAssessmentToolLearningContext();
        assessment = buildAssessment();
        Agent gradingEngine = learningContext.getEdApp();
        incrementTime = getDefaultStartedAtTime().plusSeconds(280);
        OutcomeEvent outcomeEvent = OutcomeEvent.builder()
            .edApp(learningContext.getEdApp())
            .lisOrganization(learningContext.getLisOrganization())
            .actor(learningContext.getAgent())
            .object(Attempt.builder()
                    .id(assessment.getId() + "/attempt1")
                    .assignableId(assessment.getId())
                    .actorId(((Person) learningContext.getAgent()).getId())
                    .count(1) // First attempt
                    .startedAtTime(getDefaultStartedAtTime())
                    .build())
            .action(OutcomeProfile.Actions.GRADED.key())
            .generated(Result.builder()
                    .id("https://some-university.edu/politicalScience/2014/american-revolution-101/activityContext1/attempt1/result")
                    .totalScore(4.2d)
                    .normalScore(4.2d)
                    .scoredBy(gradingEngine)
                    .build())
            .startedAtTime(incrementTime)
            .build();

        // Process Event
        Sensor.send(outcomeEvent);

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