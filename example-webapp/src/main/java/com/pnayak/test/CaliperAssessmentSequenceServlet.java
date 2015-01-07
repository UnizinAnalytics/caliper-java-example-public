package com.pnayak.test;

import org.imsglobal.caliper.Options;
import org.imsglobal.caliper.Sensor;
import org.imsglobal.caliper.entities.foaf.Agent;
import org.imsglobal.caliper.entities.LearningContext;
import org.imsglobal.caliper.entities.WebPage;
import org.imsglobal.caliper.entities.assessment.Assessment;
import org.imsglobal.caliper.entities.assessment.AssessmentItem;
import org.imsglobal.caliper.entities.assignable.Attempt;
import org.imsglobal.caliper.entities.outcome.Result;
import org.imsglobal.caliper.entities.reading.Frame;
import org.imsglobal.caliper.events.*;
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

        LearningContext learningContext = CaliperSampleAssets.buildCanvasLearningContext();
        Assessment assessment = CaliperSampleAssets.buildAssessment();

        output.append("Generated LMS learning context data\n");
        output.append("Generated activity context data\n");
        output.append("Sending events  . . .\n\n");

        // EVENT 01 -  Generate navigation event when user launches assessment
        NavigationEvent navEvent = CaliperSampleEvents.generateNavigationEvent(learningContext, assessment);

        output.append("Generated LMS learning context data\n");
        output.append("Generated Assessment activity context data\n");
        output.append("Navigated to Assessment.  Sent NavigationEvent\n");

        // Process Event
        Sensor.send(navEvent);

        output.append("Object : " + navEvent.getObject().getId() + "\n\n");

        // EVENT 02 - Started Assessment Event
        AssessmentEvent assessmentEvent = CaliperSampleEvents.generateStartedAssessmentEvent(learningContext, assessment);

        output.append("Generated Tool learning context data\n");
        output.append("Generated Assessment activity context data\n");
        output.append("Started Assessment.  Sent AssessmentEvent.\n");

        // Process Event
        Sensor.send(assessmentEvent);

        output.append("Object : " + assessmentEvent.getObject().getId() + "\n");
        output.append("Attempt : " + Integer.toString(assessmentEvent.getGenerated().getCount()) + "\n\n");

        // EVENT 03 - Started AssessmentItem 01
        AssessmentItemEvent itemEvent = CaliperSampleEvents.generateStartedAssessmentItemEvent(learningContext, assessment, assessment.getAssessmentItems().get(0));

        output.append("Generated Tool learning context data\n");
        output.append("Generated Assessment activity context data\n");
        output.append("Started AssessmentItem 01.  Sent AssessmentItemEvent.\n");

        // Process Event
        Sensor.send(itemEvent);

        output.append("Object : " + itemEvent.getObject().getId() + "\n\n");

        // EVENT 04 - Completed AssessmentItem 01
        itemEvent = CaliperSampleEvents.generateCompletedAssessmentItemEvent(learningContext, assessment, assessment.getAssessmentItems().get(0));

        output.append("Generated Tool learning context data\n");
        output.append("Generated Assessment activity context data\n");
        output.append("Completed AssessmentItem 01.  Sent AssessmentItemEvent.\n");

        // Process Event
        Sensor.send(itemEvent);

        output.append("Object : " + itemEvent.getObject().getId() + "\n");
        // output.append("Response : " + itemEvent.getGenerated().getResponse() + "\n\n");

        // EVENT 05 - Started AssessmentItem 02
        itemEvent = CaliperSampleEvents.generateStartedAssessmentItemEvent(learningContext, assessment, assessment.getAssessmentItems().get(1));

        output.append("Generated Tool learning context data\n");
        output.append("Generated Assessment activity context data\n");
        output.append("Started AssessmentItem 02.  Sent AssessmentItemEvent.\n");

        // Process Event
        Sensor.send(itemEvent);

        output.append("Object : " + itemEvent.getObject().getId() + "\n\n");

        // EVENT 06 - Completed AssessmentItem 02
        itemEvent = CaliperSampleEvents.generateCompletedAssessmentItemEvent(learningContext, assessment, assessment.getAssessmentItems().get(1));

        output.append("Generated Tool learning context data\n");
        output.append("Generated Assessment activity context data\n");
        output.append("Completed AssessmentItem 02.  Sent AssessmentItemEvent.\n");

        // Process Event
        Sensor.send(itemEvent);

        output.append("Object : " + itemEvent.getObject().getId() + "\n");
        // output.append("Response : " + itemEvent.getGenerated().getResponse() + "\n\n");

        // EVENT 07 - Started AssessmentItem 03
        itemEvent = CaliperSampleEvents.generateStartedAssessmentItemEvent(learningContext, assessment, assessment.getAssessmentItems().get(2));

        output.append("Generated Tool learning context data\n");
        output.append("Generated Assessment activity context data\n");
        output.append("Started AssessmentItem 03.  Sent AssessmentItemEvent.\n");

        // Process Event
        Sensor.send(itemEvent);

        output.append("Object : " + itemEvent.getObject().getId() + "\n\n");

        // EVENT 08 - Completed AssessmentItem 03
        itemEvent = CaliperSampleEvents.generateCompletedAssessmentItemEvent(learningContext, assessment, assessment.getAssessmentItems().get(1));

        output.append("Generated Tool learning context data\n");
        output.append("Generated Assessment activity context data\n");
        output.append("Completed AssessmentItem 03 in Super Assessment edApp . . . sent AssessmentItemEvent.\n");

        // Process Event
        Sensor.send(navEvent);

        output.append("Object : " + itemEvent.getObject().getId() + "\n");
        // output.append("Response : " + itemEvent.getGenerated().getResponse() + "\n\n");

        // EVENT # 09 - Submitted Assessment Event
        assessmentEvent = CaliperSampleEvents.generateSubmittedAssessmentEvent(learningContext, assessment);

        output.append("Generated Tool learning context data\n");
        output.append("Generated Assessment activity context data\n");
        output.append("Submitted Assessment.  Sent AssessmentEvent.\n");

        // Process Event
        Sensor.send(assessmentEvent);

        output.append("Object : " + assessmentEvent.getObject().getId() + "\n\n");

        // EVENT # 10 Generate OutcomeProfile triggered by Outcome Event
        OutcomeEvent outcomeEvent = CaliperSampleEvents.generateOutcomeEvent(learningContext, assessment);

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