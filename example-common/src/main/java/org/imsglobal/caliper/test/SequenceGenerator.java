package org.imsglobal.caliper.test;

import com.google.common.collect.Lists;
import org.imsglobal.caliper.actions.Action;
import org.imsglobal.caliper.entities.DigitalResource;
import org.imsglobal.caliper.entities.LearningContext;
import org.imsglobal.caliper.entities.agent.Person;
import org.imsglobal.caliper.entities.agent.SoftwareApplication;
import org.imsglobal.caliper.entities.annotation.BookmarkAnnotation;
import org.imsglobal.caliper.entities.annotation.HighlightAnnotation;
import org.imsglobal.caliper.entities.annotation.SharedAnnotation;
import org.imsglobal.caliper.entities.annotation.TagAnnotation;
import org.imsglobal.caliper.entities.assessment.Assessment;
import org.imsglobal.caliper.entities.assessment.AssessmentItem;
import org.imsglobal.caliper.entities.assignable.Attempt;
import org.imsglobal.caliper.entities.foaf.Agent;
import org.imsglobal.caliper.entities.media.MediaLocation;
import org.imsglobal.caliper.entities.media.VideoObject;
import org.imsglobal.caliper.entities.outcome.Result;
import org.imsglobal.caliper.entities.reading.EpubSubChapter;
import org.imsglobal.caliper.entities.reading.EpubVolume;
import org.imsglobal.caliper.entities.reading.Frame;
import org.imsglobal.caliper.entities.reading.WebPage;
import org.imsglobal.caliper.entities.response.MultipleChoiceResponse;
import org.imsglobal.caliper.entities.session.Session;
import org.imsglobal.caliper.events.*;
import org.joda.time.DateTime;

import java.util.UUID;
import java.util.logging.Logger;

/**
 * Builds & sends sequences of events
 */
public class SequenceGenerator {

    public static void generateAndSendMediaSequence(EventSender eventSender, StringBuffer output){

        // For reference, the current time
        DateTime now = DateTime.now();
        output.append(now + "\n\n");
        output.append("Sending events . . .\n\n");

        // Session Event: logged in to Media tool
        LearningContext tool = CaliperSampleAssets.buildSuperMediaToolLearningContext();
        DigitalResource reading = CaliperSampleAssets.buildEpubSubChap43();
        DateTime incrementTime = CaliperSampleAssets.getDefaultStartedAtTime();

        SessionEvent sessionEvent = SessionEvent.builder()
                .edApp(tool.getEdApp())
                .actor((Person) tool.getAgent())
                .action(Action.LOGGED_IN)
                .object(tool.getEdApp())
                .target(reading)
                .generated(CaliperSampleAssets.buildSessionStart())
                .startedAtTime(incrementTime)
                .build();

        // Process Event
        eventSender.send(sessionEvent);

        output.append("Generated SessionEvent \n");
        output.append("actor : " + ((Person) sessionEvent.getActor()).getId() + "\n");
        output.append("action : " + sessionEvent.getAction() + "\n");
        output.append("object : " + ((SoftwareApplication) sessionEvent.getObject()).getId() + "\n");
        output.append("target : " + ((DigitalResource) sessionEvent.getTarget()).getId() + "\n");
        output.append("generated : " + ((Session) sessionEvent.getGenerated()).getId() + "\n\n");

        // NavigationEvent: navigated to media content
        LearningContext learningContext = CaliperSampleAssets.buildSuperMediaToolLearningContext();
        VideoObject video = CaliperSampleAssets.buildVideoWithLearningObjective();
        incrementTime = CaliperSampleAssets.getDefaultStartedAtTime().plusSeconds(10);
        NavigationEvent navEvent = NavigationEvent.builder()
                .edApp(learningContext.getEdApp())
                .actor((Person) learningContext.getAgent())
                .object(video)
                .action(Action.NAVIGATED_TO)
                .fromResource(CaliperSampleAssets.buildAmRev101LandingPage())
                .target(MediaLocation.builder()
                        .id(video.getId()) // Don't forget to set the Id
                        .currentTime(0).build())
                .startedAtTime(incrementTime)
                .build();

        // Process Event
        eventSender.send(navEvent);

        output.append("Generated NavigationEvent \n");
        output.append("actor : " + ((Person) navEvent.getActor()).getId() + "\n");
        output.append("action : " + navEvent.getAction() + "\n");
        output.append("object : " + ((DigitalResource) navEvent.getObject()).getId() + "\n");
        output.append("target media location : " + ((MediaLocation) navEvent.getTarget()).getCurrentTime() + "\n\n");

        // MediaEvent: started video
        learningContext = CaliperSampleAssets.buildSuperMediaToolLearningContext();
        video = CaliperSampleAssets.buildVideoWithLearningObjective();
        incrementTime = CaliperSampleAssets.getDefaultStartedAtTime().plusSeconds(20);
        MediaEvent mediaEvent = MediaEvent.builder()
                .edApp(learningContext.getEdApp())
                .actor((Person) learningContext.getAgent())
                .object(video)
                .action(Action.STARTED)
                .target(MediaLocation.builder()
                        .id(video.getId()) // Don't forget to set the Id
                        .currentTime(0)
                        .build())
                .startedAtTime(incrementTime)
                .build();

        eventSender.send(mediaEvent);

        output.append("Generated MediaEvent \n");
        output.append("actor : " + ((Person) mediaEvent.getActor()).getId() + "\n");
        output.append("action : " + mediaEvent.getAction() + "\n");
        output.append("object : " + ((DigitalResource) mediaEvent.getObject()).getId() + "\n");
        output.append("target media location : " + ((MediaLocation) mediaEvent.getTarget()).getCurrentTime() + "\n\n");

        // MediaEvent: paused video
        learningContext = CaliperSampleAssets.buildSuperMediaToolLearningContext();
        video = CaliperSampleAssets.buildVideoWithLearningObjective();
        incrementTime = CaliperSampleAssets.getDefaultStartedAtTime().plusSeconds(730);
        mediaEvent = MediaEvent.builder()
                .edApp(learningContext.getEdApp())
                .actor((Person) learningContext.getAgent())
                .object(video)
                .action(Action.PAUSED)
                .target(MediaLocation.builder()
                        .id(video.getId()) // Don't forget to set the Id
                        .currentTime(710)
                        .build())
                .startedAtTime(incrementTime)
                .build();

        // Process Event
        eventSender.send(mediaEvent);

        output.append("Generated MediaEvent \n");
        output.append("actor : " + ((Person) mediaEvent.getActor()).getId() + "\n");
        output.append("action : " + mediaEvent.getAction() + "\n");
        output.append("object : " + ((DigitalResource) mediaEvent.getObject()).getId() + "\n");
        output.append("target media location : " + ((MediaLocation) mediaEvent.getTarget()).getCurrentTime() + "\n\n");

        // MediaEvent: resume video
        learningContext = CaliperSampleAssets.buildSuperMediaToolLearningContext();
        video = CaliperSampleAssets.buildVideoWithLearningObjective();
        incrementTime = CaliperSampleAssets.getDefaultStartedAtTime().plusSeconds(750);
        mediaEvent = MediaEvent.builder()
                .edApp(learningContext.getEdApp())
                .actor((Person) learningContext.getAgent())
                .object(video)
                .action(Action.RESUMED)
                .target(MediaLocation.builder()
                        .id(video.getId()) // Don't forget to set the Id
                        .currentTime(710)
                        .build())
                .startedAtTime(incrementTime)
                .build();

        // Process Event
        eventSender.send(mediaEvent);

        output.append("Generated MediaEvent \n");
        output.append("actor : " + ((Person) mediaEvent.getActor()).getId() + "\n");
        output.append("action : " + mediaEvent.getAction() + "\n");
        output.append("object : " + ((DigitalResource) mediaEvent.getObject()).getId() + "\n");
        output.append("target media location : " + ((MediaLocation) mediaEvent.getTarget()).getCurrentTime() + "\n\n");

        // MediaEvent: completed video
        learningContext = CaliperSampleAssets.buildSuperMediaToolLearningContext();
        video = CaliperSampleAssets.buildVideoWithLearningObjective();
        incrementTime = CaliperSampleAssets.getDefaultStartedAtTime().plusSeconds(2170);
        mediaEvent = MediaEvent.builder()
                .edApp(learningContext.getEdApp())
                .actor((Person) learningContext.getAgent())
                .object(video)
                .action(Action.ENDED)
                .target(MediaLocation.builder()
                        .id(video.getId()) // Don't forget to set the Id
                        .currentTime(1420)
                        .build())
                .startedAtTime(incrementTime)
                .build();

        // Process Event
        eventSender.send(mediaEvent);

        output.append("Generated MediaEvent \n");
        output.append("actor : " + ((Person) mediaEvent.getActor()).getId() + "\n");
        output.append("action : " + mediaEvent.getAction() + "\n");
        output.append("object : " + ((DigitalResource) mediaEvent.getObject()).getId() + "\n");
        output.append("target media location : " + ((MediaLocation) mediaEvent.getTarget()).getCurrentTime() + "\n\n");

        // Session Event: logged out of Canvas LMS
        tool = CaliperSampleAssets.buildSuperMediaToolLearningContext();

        sessionEvent = SessionEvent.builder()
                .edApp(tool.getEdApp())

                .actor((Person) tool.getAgent())
                .action(Action.LOGGED_OUT)
                .object(tool.getEdApp())
                .target(CaliperSampleAssets.buildSessionEnd())
                .startedAtTime(CaliperSampleAssets.getDefaultStartedAtTime())
                .endedAtTime(CaliperSampleAssets.getDefaultEndedAtTime())
                .duration("PT3000S")
                .build();

        eventSender.send(sessionEvent);

        output.append("Generated SessionEvent \n");
        output.append("actor : " + ((Person) sessionEvent.getActor()).getId() + "\n");
        output.append("action : " + sessionEvent.getAction() + "\n");
        output.append("object : " + ((SoftwareApplication) sessionEvent.getObject()).getId() + "\n");
        output.append("target : " + ((Session) sessionEvent.getTarget()).getId() + "\n\n");

        output.append("FINIS\n\n");
    }

    public static void generateAndSendReadingAnnotationSequence(EventSender eventSender, StringBuffer output){

        DateTime now = DateTime.now();
        output.append(now + "\n\n");
        output.append("Sending events . . .\n\n");

        // Session Event: logged in to Canvas LMS
        LearningContext canvas = CaliperSampleAssets.buildCanvasLearningContext();
        DigitalResource reading = CaliperSampleAssets.buildEpubSubChap43();
        DateTime incrementTime = CaliperSampleAssets.getDefaultStartedAtTime();

        SessionEvent sessionEvent = SessionEvent.builder()
                .edApp(canvas.getEdApp())
                .actor((Person) canvas.getAgent())
                .action(Action.LOGGED_IN)
                .object(canvas.getEdApp())
                .target(reading)
                .generated(CaliperSampleAssets.buildSessionStart())
                .startedAtTime(incrementTime)
                .build();

        eventSender.send(sessionEvent);

        output.append("Generated SessionEvent \n");
        output.append("actor : " + ((Person) sessionEvent.getActor()).getId() + "\n");
        output.append("action : " + sessionEvent.getAction() + "\n");
        output.append("object : " + ((SoftwareApplication) sessionEvent.getObject()).getId() + "\n");
        output.append("target : " + ((DigitalResource) sessionEvent.getTarget()).getId() + "\n");
        output.append("generated : " + ((Session) sessionEvent.getGenerated()).getId() + "\n\n");

        //TODO ADD LAUNCH EVENT

        // Navigation Event: navigated to #epubcfi(/4/3)
        LearningContext learningContext = CaliperSampleAssets.buildCanvasLearningContext();
        reading = CaliperSampleAssets.buildEpubSubChap43();
        DigitalResource fromResource = CaliperSampleAssets.buildAmRev101LandingPage();
        incrementTime = CaliperSampleAssets.getDefaultStartedAtTime().plusSeconds(10);
        NavigationEvent navEvent = NavigationEvent.builder()
                .edApp(learningContext.getEdApp())
                .actor((Person) learningContext.getAgent())
                .object(reading)
                .action(Action.NAVIGATED_TO)
                .fromResource(fromResource)
                .target(Frame.builder()
                        .id(reading.getId())
                        .index(0)
                        .build())
                .startedAtTime(incrementTime)
                .build();

        eventSender.send(navEvent);

        output.append("Generated NavigationEvent \n");
        output.append("actor : " + ((Person) navEvent.getActor()).getId() + "\n");
        output.append("action : " + navEvent.getAction() + "\n");
        output.append("object : " + ((DigitalResource) navEvent.getObject()).getId() + "\n");
        output.append("fromResource : " + navEvent.getFromResource().getId() + "\n");
        output.append("target : " + ((Frame) navEvent.getTarget()).getId() + "\n\n");

        // Navigation Event: navigated to #epubcfi(/4/3/1) (George Washington)
        learningContext = CaliperSampleAssets.buildReadiumLearningContext();
        reading = CaliperSampleAssets.buildEpubSubChap43();
        DigitalResource target = CaliperSampleAssets.buildEpubSubChap431();
        incrementTime = CaliperSampleAssets.getDefaultStartedAtTime().plusSeconds(15);
        navEvent = NavigationEvent.builder()
                .edApp(learningContext.getEdApp())
                .actor((Person) learningContext.getAgent())
                .object(reading)
                .action(Action.NAVIGATED_TO)
                .fromResource(fromResource)
                .target(Frame.builder()
                        .id(target.getId())
                        .index(1)
                        .build())
                .startedAtTime(incrementTime)
                .build();

        eventSender.send(navEvent);

        output.append("Generated NavigationEvent \n");
        output.append("actor : " + ((Person) navEvent.getActor()).getId() + "\n");
        output.append("action : " + navEvent.getAction() + "\n");
        output.append("object : " + ((EpubSubChapter) navEvent.getObject()).getId() + "\n");
        output.append("fromResource : " + navEvent.getFromResource().getId() + "\n");
        output.append("target : " + ((Frame) navEvent.getTarget()).getId() + "\n\n");

        // View Event: viewed #epubcfi(/4/3/1) (George Washington)
        // TODO viewed = object or target?
        learningContext = CaliperSampleAssets.buildReadiumLearningContext();
        reading = CaliperSampleAssets.buildEpubSubChap43();
        target = CaliperSampleAssets.buildEpubSubChap431();
        incrementTime = CaliperSampleAssets.getDefaultStartedAtTime().plusSeconds(60);
        ReadingEvent readEvent = ReadingEvent.builder()
                .edApp(learningContext.getEdApp())
                .actor((Person) learningContext.getAgent())
                .object(reading)
                .action(Action.VIEWED)
                .target(Frame.builder()
                        .id(target.getId())
                        .index(1)
                        .build())
                .startedAtTime(incrementTime)
                .build();

        eventSender.send(readEvent);

        output.append("Generated ViewEvent \n");
        output.append("actor : " + ((Person) readEvent.getActor()).getId() + "\n");
        output.append("action : " + readEvent.getAction() + "\n");
        output.append("object : " + ((EpubSubChapter) readEvent.getObject()).getId() + "\n");
        output.append("target : " + ((Frame) readEvent.getTarget()).getId() + "\n\n");

        // Navigation Event: navigated to #epubcfi(/4/3/2) (Lord Cornwallis)
        learningContext = CaliperSampleAssets.buildReadiumLearningContext();
        reading = CaliperSampleAssets.buildEpubSubChap43();
        fromResource = CaliperSampleAssets.buildEpubSubChap431();
        target = CaliperSampleAssets.buildEpubSubChap432();
        incrementTime = CaliperSampleAssets.getDefaultStartedAtTime().plusSeconds(65);
        navEvent = NavigationEvent.builder()
                .edApp(learningContext.getEdApp())
                .actor((Person) learningContext.getAgent())
                .object(reading)
                .action(Action.NAVIGATED_TO)
                .fromResource(fromResource)
                .target(Frame.builder()
                        .id(target.getId())
                        .index(2)
                        .build())
                .startedAtTime(incrementTime)
                .build();

        eventSender.send(navEvent);

        output.append("Generated NavigationEvent \n");
        output.append("actor : " + ((Person) navEvent.getActor()).getId() + "\n");
        output.append("action : " + navEvent.getAction() + "\n");
        output.append("object : " + ((EpubSubChapter) navEvent.getObject()).getId() + "\n");
        output.append("fromResource : " + navEvent.getFromResource().getId() + "\n");
        output.append("target : " + ((Frame) navEvent.getTarget()).getId() + "\n\n");

        // View Event: viewed #epubcfi(/4/3/2) (Lord Cornwallis)
        // TODO viewed = object or target?
        learningContext = CaliperSampleAssets.buildReadiumLearningContext();
        reading = CaliperSampleAssets.buildEpubSubChap43();
        target = CaliperSampleAssets.buildEpubSubChap432();
        incrementTime = CaliperSampleAssets.getDefaultStartedAtTime().plusSeconds(230);
        readEvent = ReadingEvent.builder()
                .edApp(learningContext.getEdApp())
                .actor((Person) learningContext.getAgent())
                .object(reading)
                .action(Action.VIEWED)
                .target(Frame.builder()
                        .id(target.getId())
                        .index(2)
                        .build())
                .startedAtTime(incrementTime)
                .build();

        eventSender.send(readEvent);

        output.append("Generated ViewEvent \n");
        output.append("actor : " + ((Person) readEvent.getActor()).getId() + "\n");
        output.append("action : " + readEvent.getAction() + "\n");
        output.append("object : " + ((EpubSubChapter) readEvent.getObject()).getId() + "\n");
        output.append("target : " + ((Frame) readEvent.getTarget()).getId() + "\n\n");

        // Annotation Event: highlighted text
        learningContext = CaliperSampleAssets.buildReadiumLearningContext();
        reading = CaliperSampleAssets.buildEpubSubChap432();
        incrementTime = CaliperSampleAssets.getDefaultStartedAtTime().plusSeconds(240);
        AnnotationEvent annoEvent = AnnotationEvent.builder()
                .edApp(learningContext.getEdApp())
                .actor((Person) learningContext.getAgent())
                .object(Frame.builder()
                        .id(reading.getId())
                        .index(2)
                        .build())
                .action(Action.HIGHLIGHTED)
                .generated(HighlightAnnotation.builder()
                        .id("https://someEduApp.edu/highlights/" + UUID.randomUUID().toString())
                        .annotated(reading)
                        .selectionStart(Integer.toString(455))
                        .selectionEnd(Integer.toString(489))
                        .selectionText("Life, Liberty and the pursuit of Happiness")
                        .build())
                .startedAtTime(incrementTime)
                .build();

        eventSender.send(annoEvent);

        output.append("Generated Highlight AnnotationEvent \n");
        output.append("actor : " + ((Person) annoEvent.getActor()).getId() + "\n");
        output.append("action : " + annoEvent.getAction() + "\n");
        output.append("object : " + ((Frame) annoEvent.getObject()).getId() + "\n");
        output.append("generated : " + ((HighlightAnnotation) annoEvent.getGenerated()).getId()  + "\n");
        output.append("highlighted : " + ((HighlightAnnotation) annoEvent.getGenerated()).getSelectionText() + "\n\n");

        // Navigation Event: navigated to #epubcfi(/4/3/3) (Paul Revere)
        learningContext = CaliperSampleAssets.buildReadiumLearningContext();
        reading = CaliperSampleAssets.buildEpubSubChap43();
        fromResource = CaliperSampleAssets.buildEpubSubChap432();
        target = CaliperSampleAssets.buildEpubSubChap433();
        incrementTime = CaliperSampleAssets.getDefaultStartedAtTime().plusSeconds(250);
        navEvent = NavigationEvent.builder()
                .edApp(learningContext.getEdApp())
                .actor((Person) learningContext.getAgent())
                .object(reading)
                .action(Action.NAVIGATED_TO)
                .fromResource(fromResource)
                .target(Frame.builder()
                        .id(target.getId())
                        .index(3)
                        .build())
                .startedAtTime(incrementTime)
                .build();

        eventSender.send(navEvent);

        output.append("Generated NavigationEvent \n");
        output.append("actor : " + ((Person) navEvent.getActor()).getId() + "\n");
        output.append("action : " + navEvent.getAction() + "\n");
        output.append("object : " + ((EpubSubChapter) navEvent.getObject()).getId() + "\n");
        output.append("fromResource : " + navEvent.getFromResource().getId() + "\n");
        output.append("target : " + ((Frame) navEvent.getTarget()).getId() + "\n\n");

        // ViewEvent: viewed #epubcfi(/4/3/3) (Paul Revere)
        learningContext = CaliperSampleAssets.buildReadiumLearningContext();
        reading = CaliperSampleAssets.buildEpubSubChap43();
        target = CaliperSampleAssets.buildEpubSubChap433();
        incrementTime = CaliperSampleAssets.getDefaultStartedAtTime().plusSeconds(320);
        readEvent = ReadingEvent.builder()
                .edApp(learningContext.getEdApp())
                .actor((Person) learningContext.getAgent())
                .object(reading)
                .action(Action.VIEWED)
                .target(Frame.builder()
                        .id(target.getId())
                        .index(3)
                        .build())
                .startedAtTime(incrementTime)
                .build();

        eventSender.send(readEvent);

        output.append("Generated ViewEvent \n");
        output.append("actor : " + ((Person) navEvent.getActor()).getId() + "\n");
        output.append("action : " + navEvent.getAction() + "\n");
        output.append("object : " + ((EpubSubChapter) readEvent.getObject()).getId() + "\n");
        output.append("target : " + ((Frame) readEvent.getTarget()).getId() + "\n\n");

        // BookmarkAnnotationEvent: bookmarked the reading with a note
        learningContext = CaliperSampleAssets.buildReadiumLearningContext();
        reading = CaliperSampleAssets.buildEpubSubChap433();
        incrementTime = CaliperSampleAssets.getDefaultStartedAtTime().plusSeconds(325);
        annoEvent = AnnotationEvent.builder()
                .edApp(learningContext.getEdApp())
                .actor((Person) learningContext.getAgent())
                .action(Action.BOOKMARKED)
                .object(Frame.builder()
                        .id(reading.getId())
                        .index(3)
                        .build())
                .generated(BookmarkAnnotation.builder()
                        .id("https://someEduApp.edu/bookmarks/" + UUID.randomUUID().toString())
                        .annotated(reading)
                        .bookmarkNotes("The Intolerable Acts (1774)--bad idea Lord North")
                        .build())
                .startedAtTime(incrementTime)
                .build();

        eventSender.send(annoEvent);

        output.append("Generated Bookmark AnnotationEvent \n");
        output.append("actor : " + ((Person) annoEvent.getActor()).getId() + "\n");
        output.append("action : " + annoEvent.getAction() + "\n");
        output.append("object : " + ((Frame) annoEvent.getObject()).getId() + "\n");
        output.append("generated : " + ((BookmarkAnnotation) annoEvent.getGenerated()).getId()  + "\n");
        output.append("bookmark notes : " + ((BookmarkAnnotation) annoEvent.getGenerated()).getBookmarkNotes() + "\n\n");

        // TODO ADD LAUNCH EVENT

        // NavigationEvent: navigated to CourseSmart content
        learningContext = CaliperSampleAssets.buildCourseSmartLearningContext();
        reading = CaliperSampleAssets.buildAllisonAmRevEpubVolume();
        fromResource = CaliperSampleAssets.buildAmRev101LandingPage();
        incrementTime = CaliperSampleAssets.getDefaultStartedAtTime().plusSeconds(330);
        navEvent = NavigationEvent.builder()
                .edApp(learningContext.getEdApp())
                .actor((Person) learningContext.getAgent())
                .object(reading)
                .action(Action.NAVIGATED_TO)
                .fromResource(fromResource)
                .target(Frame.builder()
                        .id(reading.getId())
                        .index(0)
                        .build())
                .startedAtTime(incrementTime)
                .build();

        eventSender.send(navEvent);

        output.append("Generated NavigationEvent \n");
        output.append("actor : " + ((Person) navEvent.getActor()).getId() + "\n");
        output.append("action : " + navEvent.getAction() + "\n");
        output.append("object : " + ((EpubVolume) navEvent.getObject()).getId() + "\n");
        output.append("fromResource : " + navEvent.getFromResource().getId() + "\n");
        output.append("target : " + ((Frame) navEvent.getTarget()).getId() + "\n\n");

        // NavigationEvent: navigated to aXfsadf12
        learningContext = CaliperSampleAssets.buildCourseSmartLearningContext();
        reading = CaliperSampleAssets.buildAllisonAmRevEpubVolume();
        fromResource = CaliperSampleAssets.buildAllisonAmRevEpubVolume();
        target = CaliperSampleAssets.buildAllisonAmRevEpubSubChap();
        incrementTime = CaliperSampleAssets.getDefaultStartedAtTime().plusSeconds(340);
        navEvent = NavigationEvent.builder()
                .edApp(learningContext.getEdApp())
                .actor((Person) learningContext.getAgent())
                .object(reading)
                .action(Action.NAVIGATED_TO)
                .fromResource(fromResource)
                .target(Frame.builder()
                        .id(target.getId())
                        .name(target.getName())
                        .index(1)
                        .build())
                .startedAtTime(incrementTime)
                .build();

        eventSender.send(navEvent);

        output.append("Generated NavigationEvent \n");
        output.append("actor : " + ((Person) navEvent.getActor()).getId() + "\n");
        output.append("action : " + navEvent.getAction() + "\n");
        output.append("object : " + ((EpubVolume) navEvent.getObject()).getId() + "\n");
        output.append("fromResource : " + navEvent.getFromResource().getId() + "\n");
        output.append("target : " + ((Frame) navEvent.getTarget()).getId() + "\n\n");

        // ViewEvent: viewed aXfsadf12
        learningContext = CaliperSampleAssets.buildCourseSmartLearningContext();
        reading = CaliperSampleAssets.buildAllisonAmRevEpubVolume();
        target = CaliperSampleAssets.buildAllisonAmRevEpubSubChap();
        incrementTime = CaliperSampleAssets.getDefaultStartedAtTime().plusSeconds(400);
        readEvent = ReadingEvent.builder()
                .edApp(learningContext.getEdApp())
                .actor((Person) learningContext.getAgent())
                .object(reading)
                .action(Action.VIEWED)
                .target(Frame.builder()
                        .id(target.getId())
                        .index(1)
                        .build())
                .startedAtTime(incrementTime)
                .build();

        eventSender.send(readEvent);

        output.append("Generated ViewEvent \n");
        output.append("actor : " + ((Person) readEvent.getActor()).getId() + "\n");
        output.append("action : " + readEvent.getAction() + "\n");
        output.append("object : " + ((EpubVolume) readEvent.getObject()).getId() + "\n");
        output.append("target : " + ((Frame) readEvent.getTarget()).getId() + "\n\n");

        // Tag AnnotationEvent: tagged reading
        learningContext = CaliperSampleAssets.buildCourseSmartLearningContext();
        reading = CaliperSampleAssets.buildAllisonAmRevEpubSubChap();
        incrementTime = CaliperSampleAssets.getDefaultStartedAtTime().plusSeconds(420);
        annoEvent = AnnotationEvent.builder()
                .edApp(learningContext.getEdApp())
                .actor((Person) learningContext.getAgent())
                .action(Action.TAGGED)
                .object(Frame.builder()
                        .id(target.getId())
                        .index(1)
                        .build())
                .generated(TagAnnotation.builder()
                        .id("https://someEduApp.edu/tags/" + UUID.randomUUID().toString())
                        .annotated(reading)
                        .tags(Lists.newArrayList("to-read", "1776", "shared-with-project-team"))
                        .build())
                .startedAtTime(incrementTime)
                .build();

        eventSender.send(annoEvent);

        output.append("Generated Tag AnnotationEvent \n");
        output.append("actor : " + ((Person) annoEvent.getActor()).getId() + "\n");
        output.append("action : " + annoEvent.getAction() + "\n");
        output.append("object : " + ((Frame) annoEvent.getObject()).getId() + "\n");
        output.append("generated : " + ((TagAnnotation) annoEvent.getGenerated()).getId()  + "\n");
        output.append("tags : " + ((TagAnnotation) annoEvent.getGenerated()).getTags().toString() + "\n\n");

        // Shared AnnotationEvent: shared reading with other students
        learningContext = CaliperSampleAssets.buildCourseSmartLearningContext();
        reading = CaliperSampleAssets.buildAllisonAmRevEpubSubChap();
        incrementTime = CaliperSampleAssets.getDefaultStartedAtTime().plusSeconds(440);
        annoEvent = AnnotationEvent.builder()
                .edApp(learningContext.getEdApp())
                .actor((Person) learningContext.getAgent())
                .action(Action.SHARED)
                .object(Frame.builder()
                        .id(target.getId())
                        .index(1)
                        .build())
                .generated(SharedAnnotation.builder()
                        .id("https://someEduApp.edu/shared/" + UUID.randomUUID().toString())
                        .annotated(reading)
                        .withAgents(Lists.<Agent>newArrayList(
                                Person.builder()
                                        .id("https://some-university.edu/students/657585")
                                        .dateCreated(CaliperSampleAssets.getDefaultDateCreated())
                                        .dateModified(CaliperSampleAssets.getDefaultDateModified())
                                        .build(),
                                Person.builder()
                                        .id("https://some-university.edu/students/667788")
                                        .dateCreated(CaliperSampleAssets.getDefaultDateCreated())
                                        .dateModified(CaliperSampleAssets.getDefaultDateModified())
                                        .build()))
                        .build())
                .startedAtTime(incrementTime)
                .build();

        eventSender.send(annoEvent);

        output.append("Generated Shared AnnotationEvent \n");
        output.append("actor : " + ((Person) annoEvent.getActor()).getId() + "\n");
        output.append("action : " + annoEvent.getAction() + "\n");
        output.append("object : " + ((Frame) annoEvent.getObject()).getId() + "\n");
        output.append("generated : " + ((SharedAnnotation) annoEvent.getGenerated()).getId()  + "\n");

        // Retrieve agents
        SharedAnnotation shared = (SharedAnnotation) annoEvent.getGenerated();
        for (Agent agent: shared.getWithAgents()) {
            output.append("Shared with: " + ((Person) agent).getId() + "\n");
        }
        output.append("\n");

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

        eventSender.send(sessionEvent);

        output.append("Generated SessionEvent \n");
        output.append("actor : " + ((Person) sessionEvent.getActor()).getId() + "\n");
        output.append("action : " + sessionEvent.getAction() + "\n");
        output.append("object : " + ((SoftwareApplication) sessionEvent.getObject()).getId() + "\n");
        output.append("target : " + ((Session) sessionEvent.getTarget()).getId() + "\n\n");

        output.append("FINIS\n\n");
    }

    public static void generateAndSendAAOSequence(EventSender eventSender, StringBuffer output){

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

        eventSender.send(sessionEvent);

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
        eventSender.send(navEvent);

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
        eventSender.send(assessmentEvent);

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
        eventSender.send(itemEvent);

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
        eventSender.send(itemEvent);

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
        eventSender.send(itemEvent);

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
        eventSender.send(itemEvent);

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
        eventSender.send(itemEvent);

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
        eventSender.send(navEvent);

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
        eventSender.send(assessmentEvent);

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

        eventSender.send(sessionEvent);

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
                        .assignable(CaliperSampleAssets.buildDigitalResource())
                        .actor(CaliperSampleAssets.buildStudent554433())
                        .build())
                .startedAtTime(incrementTime)
                .build();

        // Process Event
        eventSender.send(outcomeEvent);

        output.append("Generated OutcomeEvent \n");
        output.append("actor : " + ((Person) outcomeEvent.getActor()).getId() + "\n");
        output.append("action : " + outcomeEvent.getAction() + "\n");
        output.append("object : " + ((Attempt) outcomeEvent.getObject()).getId() + "\n");
        output.append("attempt count : " + Integer.toString(((Attempt) outcomeEvent.getObject()).getCount()) + "\n");
        output.append("generated outcome : " + String.valueOf(((Result) outcomeEvent.getGenerated()).getTotalScore()) + "\n");
        output.append("scored by : " + ((Result) outcomeEvent.getGenerated()).getScoredBy() + "\n\n");

        output.append("FINIS\n\n");
    }

}
