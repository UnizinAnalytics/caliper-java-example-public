package org.imsglobal.caliper.test.cli;

import org.fusesource.jansi.AnsiConsole;
import org.imsglobal.caliper.Client;
import org.imsglobal.caliper.Options;
import org.imsglobal.caliper.Sensor;
import org.imsglobal.caliper.events.Event;
import org.imsglobal.caliper.test.EventSender;
import org.imsglobal.caliper.test.SequenceGenerator;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.ExampleMode;
import org.kohsuke.args4j.Option;

import static org.fusesource.jansi.Ansi.Color.*;
import static org.fusesource.jansi.Ansi.ansi;

/**
 * Created by pgray on 12/23/14.
 */
public class CaliperSequenceGenerator {

    @Option(name="-h", usage="hostname to send events to", metaVar="STORE_HOST")
    private String host;

    @Option(name="-k", usage="caliper api key", metaVar="API_KEY")
    private String apiKey;

    private static Client caliperStore = null;

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
        caliperStore = new Client("clientid?", opts);

        //TODO: flag by a cli param
        System.out.println(ansi().fg(YELLOW).a("Sending Assessment Sequence...").reset());
        SequenceGenerator.generateAndSendAAOSequence(TestCliEventSender, new StringBuffer());
        System.out.println(ansi().fg(YELLOW).a("Sending Media Sequence...").reset());
        SequenceGenerator.generateAndSendMediaSequence(TestCliEventSender, new StringBuffer());
        System.out.println(ansi().fg(YELLOW).a("Sending Reading Sequence...").reset());
        SequenceGenerator.generateAndSendReadingAnnotationSequence(TestCliEventSender, new StringBuffer());

    }

    public static EventSender TestCliEventSender = new EventSender(){
        @Override
        public void send(Event event) {
            printEventMessage("Sending " + event.getClass().getSimpleName());
            sendEvent(caliperStore, event);
        }
    };

    public static void sendEvent(Client client, Event event){
        //TODO: Remove the swallowing of exceptions in java-caliper so they can be handled here.
        client.send(new Sensor("sensorid?"), event);
    }

    public static void printEventMessage(String message){
        System.out.println(ansi().fg(MAGENTA).a(message).reset());
    }

}
