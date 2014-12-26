package org.imsglobal.caliper.test.cli;

import com.pnayak.test.CaliperSampleAssets;
import com.pnayak.test.CaliperSampleEvents;
import org.apache.http.conn.HttpHostConnectException;
import org.imsglobal.caliper.Client;
import org.imsglobal.caliper.Options;
import org.imsglobal.caliper.Sensor;
import org.imsglobal.caliper.entities.LearningContext;
import org.imsglobal.caliper.entities.WebPage;
import org.imsglobal.caliper.entities.assessment.Assessment;
import org.imsglobal.caliper.entities.assessment.AssessmentItem;
import org.imsglobal.caliper.entities.reading.Frame;
import org.imsglobal.caliper.events.*;
import org.imsglobal.caliper.profiles.AssessmentProfile;
import org.joda.time.DateTime;
import org.fusesource.jansi.AnsiConsole;
import org.kohsuke.args4j.*;

import static org.fusesource.jansi.Ansi.*;
import static org.fusesource.jansi.Ansi.Color.*;

import static com.pnayak.test.CaliperSampleAssets.buildAssessment;
import static com.pnayak.test.CaliperSampleAssets.buildCanvasLearningContext;

/**
 * Created by pgray on 12/23/14.
 */
public class CaliperSequenceGenerator {

    @Option(name="-h",usage="hostname to send events to", metaVar = "STORE_HOST")
    private String host;

    @Option(name="-p",usage="port of host connecting to", metaVar = "STORE_PORT")
    private Integer port;

//    @Option(name="-m",usage="mode of events to send", metaVar = "EVENT_MODE", handler = EventModeOptionHandler.class)
//    private EventMode mode;

    @Option(name="-k",usage="caliper api key", metaVar = "API_KEY")
    private String apiKey;

    private static String DEFAULT_HOST  = "localhost";
    private static Integer DEFAULT_PORT = 1080;
//    private static EventMode DEFAULT_MODE = EventMode.assessment;
    private static String DEFAULT_API_KEY = "FEFNtMyXRZqwAH4svMakTw";

//    public static enum EventMode{
//        assessment,
//        media,
//        reading
//    }
//
//    public class EventModeOptionHandler extends OptionHandler<EventMode>{
//        public EventModeOptionHandler(CmdLineParser parser, OptionDef option, Setter<? super EventMode> setter) {
//            super(parser, option, setter);
//        }
//
//        @Override
//        public int parseArguments(Parameters parameters) throws CmdLineException {
//            if(EventMode.valueOf(parameters.getParameter(0)) != null){
//                setter.addValue(EventMode.valueOf(parameters.getParameter(0)));
//                return 0;
//            } else {
//                return 1;
//            }
//        }
//
//        @Override
//        public String getDefaultMetaVariable() {
//            return "EVENT_MODE";
//        }
//    }

    public void initialize(){
        AnsiConsole.systemInstall();

    }

    public static void main(String[] args){
        new CaliperSequenceGenerator().doMain(args);
    }

    public void doMain(String[] args) {
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

        //System.setProperty(org.slf4j.impl.SimpleLogger.DEFAULT_LOG_LEVEL_KEY, "ERROR");

        initialize();
        System.out.println(ansi().fg(YELLOW).a("Caliper Sequence Generator...").reset());
        System.out.println(ansi().fg(CYAN).a("  Got args:").reset());
        for(int i=0; i<args.length; i++){
            System.out.println(ansi().fg(CYAN).a("    " + (i + 1) + ". " + args[i]).reset());
        }

        Options opts = new Options();
        opts.setApiKey(apiKey == null ? DEFAULT_API_KEY : apiKey);
        opts.setHost("http://" + (host == null ? DEFAULT_HOST : host) + ":" + (port == null ? DEFAULT_PORT : port));
        Client caliperStore = new Client(opts);

        //TODO: add other Sequences, flagged by a cli param
        generateAndSendAssessmentSequence(caliperStore);
    }

    private void generateAndSendAssessmentSequence(Client caliperStore) {
        LearningContext learningContext = CaliperSampleAssets.buildCanvasLearningContext();
        Assessment assessment = CaliperSampleAssets.buildAssessment();

        // EVENT 01 -  Generate navigation event when user launches assessment
        NavigationEvent navEvent = CaliperSampleEvents.generateNavigationEvent(learningContext, assessment);

        // Process Event
        sendEvent(caliperStore, navEvent);

        // EVENT 02 - Started Assessment Event
        AssessmentEvent assessmentEvent = CaliperSampleEvents.generateStartedAssessmentEvent(learningContext, assessment);

        // Process Event
        sendEvent(caliperStore, assessmentEvent);

        for(AssessmentItem assessmentItem: assessment.getAssessmentItems()) {
            sendEvent(caliperStore, CaliperSampleEvents.generateStartedAssessmentItemEvent(learningContext, assessment, assessmentItem));
            sendEvent(caliperStore, CaliperSampleEvents.generateCompletedAssessmentItemEvent(learningContext, assessment, assessmentItem));
        }

        // Process Event
        sendEvent(caliperStore, navEvent);

        // EVENT # 09 - Submitted Assessment Event
        assessmentEvent = CaliperSampleEvents.generateSubmittedAssessmentEvent(learningContext, assessment);

        // Process Event
        sendEvent(caliperStore, assessmentEvent);

        // EVENT # 10 Generate OutcomeProfile triggered by Outcome Event
        OutcomeEvent outcomeEvent = CaliperSampleEvents.generateOutcomeEvent(learningContext, assessment);

        // Process Event
        sendEvent(caliperStore, outcomeEvent);

    }

    public void sendEvent(Client client, Event event){
        //TODO: Remove the swallowing of exceptions in java-caliper so they can be handled here.
        // Process Event
        client.send(event);
    }

}
