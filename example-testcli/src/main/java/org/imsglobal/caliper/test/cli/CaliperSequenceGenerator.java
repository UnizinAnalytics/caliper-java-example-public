package org.imsglobal.caliper.test.cli;

import org.imsglobal.caliper.test.CaliperSampleAssets;
import org.imsglobal.caliper.test.CaliperSampleEvents;
import org.imsglobal.caliper.Client;
import org.imsglobal.caliper.Options;
import org.imsglobal.caliper.entities.LearningContext;
import org.imsglobal.caliper.entities.assessment.Assessment;
import org.imsglobal.caliper.entities.assessment.AssessmentItem;
import org.imsglobal.caliper.events.*;
import org.fusesource.jansi.AnsiConsole;
import org.kohsuke.args4j.*;

import static org.fusesource.jansi.Ansi.*;
import static org.fusesource.jansi.Ansi.Color.*;

/**
 * Created by pgray on 12/23/14.
 */
public class CaliperSequenceGenerator {

    @Option(name="-h", usage="hostname to send events to", metaVar="STORE_HOST")
    private String host;

    @Option(name="-k", usage="caliper api key", metaVar="API_KEY")
    private String apiKey;

    private static String DEFAULT_HOST  = "http://localhost:1080";
    private static String DEFAULT_API_KEY = "FEFNtMyXRZqwAH4svMakTw";

    public void initialize(String[] args){
        AnsiConsole.systemInstall();
        CmdLineParser parser = new CmdLineParser(this);
        try {
            parser.parseArgument(args);
        } catch(CmdLineException e) {
            System.err.println(e.getMessage());
            System.err.println("java SampleMain [options...] arguments...");
            parser.printUsage(System.err);
            System.err.println();
            System.err.println("  Example: java SampleMain"+parser.printExample(ExampleMode.ALL));
            return;
        }
        host = host == null ? DEFAULT_HOST : host;
        apiKey = apiKey == null ? DEFAULT_API_KEY : apiKey;
    }

    public static void main(String[] args){
        new CaliperSequenceGenerator().doMain(args);
    }

    public void doMain(String[] args) {
        initialize(args);
        System.out.println(ansi().fg(YELLOW).a("Caliper Sequence Generator...").reset());
        System.out.println(ansi().fg(CYAN).a("Using:").reset());
        System.out.println(ansi().fg(CYAN).a("    host:    " + host).reset());
        System.out.println(ansi().fg(CYAN).a("    api key: " + apiKey).reset());

        Options opts = new Options();
        opts.setApiKey(apiKey);
        opts.setHost(host);
        Client caliperStore = new Client(opts);

        //TODO: add other Sequences, flagged by a cli param
        generateAndSendAssessmentSequence(caliperStore);
    }

    private void generateAndSendAssessmentSequence(Client caliperStore) {
        LearningContext learningContext = CaliperSampleAssets.buildCanvasLearningContext();
        Assessment assessment = CaliperSampleAssets.buildAssessment();

        NavigationEvent navEvent = CaliperSampleEvents.generateNavigationEvent(learningContext, assessment);
        sendEvent(caliperStore, navEvent);
        printEventMessage("Sent Navigation Event");

        AssessmentEvent assessmentEvent = CaliperSampleEvents.generateStartedAssessmentEvent(learningContext, assessment);
        sendEvent(caliperStore, assessmentEvent);
        printEventMessage("Sent Started Assessment Event");

        for(AssessmentItem assessmentItem: assessment.getAssessmentItems()) {
            sendEvent(caliperStore, CaliperSampleEvents.generateStartedAssessmentItemEvent(learningContext, assessment, assessmentItem));
            printEventMessage("Sent Started Assessment Item Event");
            sendEvent(caliperStore, CaliperSampleEvents.generateCompletedAssessmentItemEvent(learningContext, assessment, assessmentItem));
            printEventMessage("Sent Completed Assessment Item Event");
        }

        assessmentEvent = CaliperSampleEvents.generateSubmittedAssessmentEvent(learningContext, assessment);
        sendEvent(caliperStore, assessmentEvent);
        printEventMessage("Sent Submitted Assessment Event");

        OutcomeEvent outcomeEvent = CaliperSampleEvents.generateOutcomeEvent(learningContext, assessment);
        sendEvent(caliperStore, outcomeEvent);
        printEventMessage("Sent Outcome Event");

    }

    public void sendEvent(Client client, Event event){
        //TODO: Remove the swallowing of exceptions in java-caliper so they can be handled here.
        client.send(event);
    }

    public void printEventMessage(String message){
        System.out.println(ansi().fg(MAGENTA).a(message).reset());
    }

}