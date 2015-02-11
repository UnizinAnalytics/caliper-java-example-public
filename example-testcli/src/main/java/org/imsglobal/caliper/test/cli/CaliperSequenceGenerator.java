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
import org.kohsuke.args4j.spi.OptionHandler;

import static org.fusesource.jansi.Ansi.*;
import static org.fusesource.jansi.Ansi.Color.*;

import static com.pnayak.test.CaliperSampleAssets.buildAssessment;
import static com.pnayak.test.CaliperSampleAssets.buildCanvasLearningContext;

/**
 * Created by pgray on 12/23/14.
 */
public class CaliperSequenceGenerator {

    @Option(name="-h", usage="hostname to send events to", metaVar="STORE_HOST")
    private String host;

    @Option(name="-k", usage="caliper api key", metaVar="API_KEY")
    private String apiKey;

//    @Option(name="-m",usage="mode of events to send", metaVar = "EVENT_MODE", handler = EventModeOptionHandler.class)
//    private EventMode mode;

    private static String DEFAULT_HOST  = "http://localhost:1080";
    private static String DEFAULT_API_KEY = "FEFNtMyXRZqwAH4svMakTw";
//    private static EventMode DEFAULT_MODE = EventMode.assessment;

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
        AssessmentEvent assessmentEvent = CaliperSampleEvents.generateStartedAssessmentEvent(learningContext, assessment);
        sendEvent(caliperStore, assessmentEvent);

        for(AssessmentItem assessmentItem: assessment.getAssessmentItems()) {
            sendEvent(caliperStore, CaliperSampleEvents.generateStartedAssessmentItemEvent(learningContext, assessment, assessmentItem));
            sendEvent(caliperStore, CaliperSampleEvents.generateCompletedAssessmentItemEvent(learningContext, assessment, assessmentItem));
        }

        assessmentEvent = CaliperSampleEvents.generateSubmittedAssessmentEvent(learningContext, assessment);
        sendEvent(caliperStore, assessmentEvent);
        OutcomeEvent outcomeEvent = CaliperSampleEvents.generateOutcomeEvent(learningContext, assessment);
        sendEvent(caliperStore, outcomeEvent);
    }

    public void sendEvent(Client client, Event event){
        //TODO: Remove the swallowing of exceptions in java-caliper so they can be handled here.
        client.send(event);
    }

}
