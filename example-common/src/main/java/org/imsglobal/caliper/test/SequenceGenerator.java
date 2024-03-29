package org.imsglobal.caliper.test;

import com.google.common.collect.Lists;
import org.imsglobal.caliper.actions.Action;
import org.imsglobal.caliper.entities.DigitalResource;
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
import org.imsglobal.caliper.entities.lis.Membership;
import org.imsglobal.caliper.entities.lis.Role;
import org.imsglobal.caliper.entities.lis.Status;
import org.imsglobal.caliper.entities.media.MediaLocation;
import org.imsglobal.caliper.entities.media.VideoObject;
import org.imsglobal.caliper.entities.outcome.Result;
import org.imsglobal.caliper.entities.reading.EpubPart;
import org.imsglobal.caliper.entities.reading.EpubSubChapter;
import org.imsglobal.caliper.entities.reading.EpubVolume;
import org.imsglobal.caliper.entities.reading.Frame;
import org.imsglobal.caliper.entities.response.MultipleChoiceResponse;
import org.imsglobal.caliper.entities.session.Session;
import org.imsglobal.caliper.entities.w3c.Organization;
import org.imsglobal.caliper.events.AnnotationEvent;
import org.imsglobal.caliper.events.AssessmentEvent;
import org.imsglobal.caliper.events.AssessmentItemEvent;
import org.imsglobal.caliper.events.MediaEvent;
import org.imsglobal.caliper.events.NavigationEvent;
import org.imsglobal.caliper.events.OutcomeEvent;
import org.imsglobal.caliper.events.ReadingEvent;
import org.imsglobal.caliper.events.SessionEvent;
import org.joda.time.DateTime;

import java.util.UUID;

/**
 * Builds & sends sequences of events
 */
public class SequenceGenerator {

    public static void generateAndSendMediaSequence(EventSender eventSender, StringBuffer output){

        // For reference, the current time
        DateTime now = DateTime.now();
        output.append(now + "\n\n");
        output.append("Sending events . . .\n\n");

        // EdApp, Agent, Organization (CourseSection), Membership, Video
        SoftwareApplication edApp = SampleAgentEntities.buildMediaTool();
        Person actor = SampleAgentEntities.buildStudent554433();
        Organization organization = SampleLISEntities.buildAmRev101CourseSection();
        Membership membership = SampleLISEntities.buildAmRev101Membership(actor, organization, Role.LEARNER, Status.ACTIVE);
        VideoObject video = SampleMediaEntities.buildVideoWithLearningObjective();

        // SessionEvent: logged in to Media tool
        SessionEvent sessionEvent = SessionEvent.builder()
            .actor(actor)
            .action(Action.LOGGED_IN.getValue())
            .object(edApp)
            .target(video)
            .generated(Session.builder()
                .id("https://example.edu/session-123456789")
                .name("session-123456789")
                .actor(actor)
                .dateCreated(SampleTime.getDefaultDateCreated())
                .startedAtTime(SampleTime.getDefaultStartedAtTime())
                .build())
            .eventTime(SampleTime.getDefaultEventTime())
            .edApp(edApp)
            .group(organization)
            .membership(membership)
            .build();

        // Process event
        eventSender.send(sessionEvent);

        output.append("Generated SessionEvent \n");
        output.append("actor : " + sessionEvent.getActor().getId() + "\n");
        output.append("action : " + sessionEvent.getAction() + "\n");
        output.append("object : " + ((SoftwareApplication) sessionEvent.getObject()).getId() + "\n");
        output.append("target : " + ((DigitalResource) sessionEvent.getTarget()).getId() + "\n");
        output.append("generated : " + ((Session) sessionEvent.getGenerated()).getId() + "\n\n");

        // NavigationEvent: navigated to media content
        NavigationEvent navEvent = NavigationEvent.builder()
            .actor(actor)
            .action(Action.NAVIGATED_TO.getValue())
            .object(video)
            .fromResource(SampleReadingEntities.buildAmRev101LandingPage())
            .target(MediaLocation.builder()
                .id(video.getId()) // Don't forget to set the Id
                .currentTime(0).build())
            .eventTime(SampleTime.getDefaultEventTime().plusSeconds(10))
            .edApp(edApp)
            .group(organization)
            .membership(membership)
            .build();

        // Process event
        eventSender.send(navEvent);

        output.append("Generated NavigationEvent \n");
        output.append("actor : " + navEvent.getActor().getId() + "\n");
        output.append("action : " + navEvent.getAction() + "\n");
        output.append("object : " + ((DigitalResource) navEvent.getObject()).getId() + "\n");
        output.append("target media location : " + ((MediaLocation) navEvent.getTarget()).getCurrentTime() + "\n\n");

        // MediaEvent: started video
        MediaEvent mediaEvent = MediaEvent.builder()
            .actor(actor)
            .action(Action.STARTED.getValue())
            .object(video)
            .target(MediaLocation.builder()
                .id(video.getId())
                .currentTime(0)
                .build())
            .eventTime(SampleTime.getDefaultEventTime().plusSeconds(20))
            .edApp(edApp)
            .group(organization)
            .membership(membership)
            .build();

        eventSender.send(mediaEvent);

        output.append("Generated MediaEvent \n");
        output.append("actor : " + mediaEvent.getActor().getId() + "\n");
        output.append("action : " + mediaEvent.getAction() + "\n");
        output.append("object : " + ((DigitalResource) mediaEvent.getObject()).getId() + "\n");
        output.append("target media location : " + ((MediaLocation) mediaEvent.getTarget()).getCurrentTime() + "\n\n");

        // MediaEvent: paused video
        mediaEvent = MediaEvent.builder()
            .actor(actor)
            .action(Action.PAUSED.getValue())
            .object(video)
            .target(MediaLocation.builder()
                .id(video.getId())
                .currentTime(710)
                .build())
            .eventTime(SampleTime.getDefaultEventTime().plusSeconds(730))
            .edApp(edApp)
            .group(organization)
            .membership(membership)
            .build();

        // Process event
        eventSender.send(mediaEvent);

        output.append("Generated MediaEvent \n");
        output.append("actor : " + mediaEvent.getActor().getId() + "\n");
        output.append("action : " + mediaEvent.getAction() + "\n");
        output.append("object : " + ((DigitalResource) mediaEvent.getObject()).getId() + "\n");
        output.append("target media location : " + ((MediaLocation) mediaEvent.getTarget()).getCurrentTime() + "\n\n");

        // MediaEvent: resume video
        mediaEvent = MediaEvent.builder()
            .actor(actor)
            .action(Action.RESUMED.getValue())
            .object(video)
            .target(MediaLocation.builder()
                .id(video.getId()) // Don't forget to set the Id
                .currentTime(710)
                .build())
            .eventTime(SampleTime.getDefaultEventTime().plusSeconds(750))
            .edApp(edApp)
            .group(organization)
            .membership(membership)
            .build();

        // Process event
        eventSender.send(mediaEvent);

        output.append("Generated MediaEvent \n");
        output.append("actor : " + mediaEvent.getActor().getId() + "\n");
        output.append("action : " + mediaEvent.getAction() + "\n");
        output.append("object : " + ((DigitalResource) mediaEvent.getObject()).getId() + "\n");
        output.append("target media location : " + ((MediaLocation) mediaEvent.getTarget()).getCurrentTime() + "\n\n");

        // MediaEvent: completed video
        mediaEvent = MediaEvent.builder()
            .actor(actor)
            .action(Action.ENDED.getValue())
            .object(video)
            .target(MediaLocation.builder()
                .id(video.getId()) // Don't forget to set the Id
                .currentTime(1420)
                .build())
            .eventTime(SampleTime.getDefaultEventTime().plusSeconds(2170))
            .edApp(edApp)
            .group(organization)
            .membership(membership)
            .build();

        // Process event
        eventSender.send(mediaEvent);

        output.append("Generated MediaEvent \n");
        output.append("actor : " + mediaEvent.getActor().getId() + "\n");
        output.append("action : " + mediaEvent.getAction() + "\n");
        output.append("object : " + ((DigitalResource) mediaEvent.getObject()).getId() + "\n");
        output.append("target media location : " + ((MediaLocation) mediaEvent.getTarget()).getCurrentTime() + "\n\n");

        // SessionEvent: logged out
        sessionEvent = SessionEvent.builder()
            .actor(actor)
            .action(Action.LOGGED_OUT.getValue())
            .object(edApp)
            .target(Session.builder()
                .id("https://example.edu/session-123456789")
                .name("session-123456789")
                .actor(actor)
                .dateCreated(SampleTime.getDefaultDateCreated())
                .startedAtTime(SampleTime.getDefaultStartedAtTime())
                .endedAtTime(SampleTime.getDefaultEventTime().plusSeconds(2175))
                .duration("PT2175S")
                .build())
            .eventTime(SampleTime.getDefaultEventTime().plusSeconds(2175))
            .edApp(edApp)
            .group(organization)
            .membership(membership)
            .build();

        // Process event
        eventSender.send(sessionEvent);

        output.append("Generated SessionEvent \n");
        output.append("actor : " + sessionEvent.getActor().getId() + "\n");
        output.append("action : " + sessionEvent.getAction() + "\n");
        output.append("object : " + ((SoftwareApplication) sessionEvent.getObject()).getId() + "\n");
        output.append("target : " + ((Session) sessionEvent.getTarget()).getId() + "\n\n");

        output.append("FINIS\n\n");
    }

    public static void generateAndSendReadingAnnotationSequence(EventSender eventSender, StringBuffer output){

        DateTime now = DateTime.now();
        output.append(now + "\n\n");
        output.append("Sending events . . .\n\n");

        // EdApp, Agent, Organization (CourseSection), Membership
        SoftwareApplication canvas = SampleAgentEntities.buildCanvas();
        Person actor = SampleAgentEntities.buildStudent554433();
        Organization organization = SampleLISEntities.buildAmRev101CourseSection();
        Membership membership = SampleLISEntities.buildAmRev101Membership(actor, organization, Role.LEARNER, Status.ACTIVE);

        // SessionEvent: logged in to Canvas
        SessionEvent sessionEvent = SessionEvent.builder()
            .actor(actor)
            .action(Action.LOGGED_IN.getValue())
            .object(canvas)
            .target(SampleReadingEntities.buildAmRev101LandingPage())
            .generated(Session.builder()
                .id("https://example.edu/session-123456789")
                .name("session-123456789")
                .actor(actor)
                .dateCreated(SampleTime.getDefaultDateCreated())
                .startedAtTime(SampleTime.getDefaultStartedAtTime())
                .build())
            .eventTime(SampleTime.getDefaultEventTime())
            .edApp(canvas)
            .group(organization)
            .membership(membership)
            .build();

        // Process event
        eventSender.send(sessionEvent);

        output.append("Generated SessionEvent \n");
        output.append("actor : " + sessionEvent.getActor().getId() + "\n");
        output.append("action : " + sessionEvent.getAction() + "\n");
        output.append("object : " + ((SoftwareApplication) sessionEvent.getObject()).getId() + "\n");
        output.append("target : " + ((DigitalResource) sessionEvent.getTarget()).getId() + "\n");
        output.append("generated : " + ((Session) sessionEvent.getGenerated()).getId() + "\n\n");

        // TODO add LTI launch event

        // Navigation Event: navigated to ePub chapter #epubcfi(/4/3)
        SoftwareApplication readium = SampleAgentEntities.buildReadium();
        DigitalResource chapter = SampleReadingEntities.buildEpubGloriousCauseSubChap43();
        NavigationEvent navEvent = NavigationEvent.builder()
            .actor(actor)
            .action(Action.NAVIGATED_TO.getValue())
            .object(chapter)
            .fromResource(SampleReadingEntities.buildAmRev101LandingPage())
            .target(Frame.builder()
                .id(chapter.getId())
                .name(chapter.getName())
                .index(0)
                .build())
            .eventTime(SampleTime.getDefaultEventTime().plusSeconds(10))
            .edApp(readium)
            .group(organization)
            .membership(membership)
            .build();

        // Process event
        eventSender.send(navEvent);

        output.append("Generated NavigationEvent \n");
        output.append("actor : " + navEvent.getActor().getId() + "\n");
        output.append("action : " + navEvent.getAction() + "\n");
        output.append("object : " + ((DigitalResource) navEvent.getObject()).getId() + "\n");
        output.append("fromResource : " + navEvent.getFromResource().getId() + "\n");
        output.append("target : " + ((Frame) navEvent.getTarget()).getId() + "\n\n");

        // ViewEvent: viewed #epubcfi(/4/3)
        ReadingEvent readEvent = ReadingEvent.builder()
            .actor(actor)
            .action(Action.VIEWED.getValue())
            .object(chapter)
            .target(Frame.builder()
                .id(chapter.getId())
                .name(chapter.getName())
                .isPartOf(chapter.getIsPartOf())
                .index(0)
                .build())
            .eventTime(SampleTime.getDefaultEventTime().plusSeconds(60))
            .edApp(readium)
            .group(organization)
            .membership(membership)
            .build();

        // Process event
        eventSender.send(readEvent);

        output.append("Generated ViewEvent \n");
        output.append("actor : " + readEvent.getActor().getId() + "\n");
        output.append("action : " + readEvent.getAction() + "\n");
        output.append("object : " + ((EpubSubChapter) readEvent.getObject()).getId() + "\n");
        output.append("target : " + ((Frame) readEvent.getTarget()).getId() + "\n\n");

        // NavigationEvent: navigated to #epubcfi(/4/3/1) (George Washington)
        DigitalResource part431 = SampleReadingEntities.buildEpubGloriousCausePart431();
        navEvent = NavigationEvent.builder()
            .actor(actor)
            .action(Action.NAVIGATED_TO.getValue())
            .object(part431)
            .fromResource(Frame.builder()
                .id(chapter.getId())
                .name(chapter.getName())
                .isPartOf(chapter.getIsPartOf())
                .index(0)
                .build())
            .target(Frame.builder()
                .id(part431.getId())
                .name(part431.getName())
                .isPartOf(part431.getIsPartOf())
                .index(1)
                .build())
            .eventTime(SampleTime.getDefaultEventTime().plusSeconds(15))
            .edApp(readium)
            .group(organization)
            .membership(membership)
            .build();

        // Process event
        eventSender.send(navEvent);

        output.append("Generated NavigationEvent \n");
        output.append("actor : " + navEvent.getActor().getId() + "\n");
        output.append("action : " + navEvent.getAction() + "\n");
        output.append("object : " + ((EpubPart) navEvent.getObject()).getId() + "\n");
        output.append("fromResource : " + navEvent.getFromResource().getId() + "\n");
        output.append("target : " + ((Frame) navEvent.getTarget()).getId() + "\n\n");

        // View Event: viewed #epubcfi(/4/3/1) (George Washington)
        readEvent = ReadingEvent.builder()
            .actor(actor)
            .action(Action.VIEWED.getValue())
            .object(part431)
            .target(Frame.builder()
                .id(part431.getId())
                .name(part431.getName())
                .isPartOf(part431.getIsPartOf())
                .index(1)
                .build())
            .eventTime(SampleTime.getDefaultEventTime().plusSeconds(60))
            .edApp(readium)
            .group(organization)
            .membership(membership)
            .build();

        // Process event
        eventSender.send(readEvent);

        output.append("Generated ViewEvent \n");
        output.append("actor : " + readEvent.getActor().getId() + "\n");
        output.append("action : " + readEvent.getAction() + "\n");
        output.append("object : " + ((EpubPart) readEvent.getObject()).getId() + "\n");
        output.append("target : " + ((Frame) readEvent.getTarget()).getId() + "\n\n");

        // Navigation Event: navigated to #epubcfi(/4/3/2) (Lord Cornwallis)
        DigitalResource part432 = SampleReadingEntities.buildEpubGloriousCausePart432();
        navEvent = NavigationEvent.builder()
            .actor(actor)
            .action(Action.NAVIGATED_TO.getValue())
            .object(part432)
            .fromResource(Frame.builder()
                .id(part431.getId())
                .name(part431.getName())
                .isPartOf(part431.getIsPartOf())
                .index(1)
                .build())
            .target(Frame.builder()
                .id(part432.getId())
                .name(part432.getName())
                .isPartOf(part432.getIsPartOf())
                .index(2)
                .build())
            .eventTime(SampleTime.getDefaultEventTime().plusSeconds(65))
            .edApp(readium)
            .group(organization)
            .membership(membership)
            .build();

        // Process event
        eventSender.send(navEvent);

        output.append("Generated NavigationEvent \n");
        output.append("actor : " + navEvent.getActor().getId() + "\n");
        output.append("action : " + navEvent.getAction() + "\n");
        output.append("object : " + ((EpubPart) navEvent.getObject()).getId() + "\n");
        output.append("fromResource : " + navEvent.getFromResource().getId() + "\n");
        output.append("target : " + ((Frame) navEvent.getTarget()).getId() + "\n\n");

        // ViewEvent: viewed #epubcfi(/4/3/2) (Lord Cornwallis)
        readEvent = ReadingEvent.builder()
            .actor(actor)
            .action(Action.VIEWED.getValue())
            .object(part432)
            .target(Frame.builder()
                .id(part432.getId())
                .name(part432.getName())
                .isPartOf(part432.getIsPartOf())
                .index(2)
                .build())
            .eventTime(SampleTime.getDefaultEventTime().plusSeconds(230))
            .edApp(readium)
            .group(organization)
            .membership(membership)
            .build();

        // Process event
        eventSender.send(readEvent);

        output.append("Generated ViewEvent \n");
        output.append("actor : " + readEvent.getActor().getId() + "\n");
        output.append("action : " + readEvent.getAction() + "\n");
        output.append("object : " + ((EpubPart) readEvent.getObject()).getId() + "\n");
        output.append("target : " + ((Frame) readEvent.getTarget()).getId() + "\n\n");

        // AnnotationEvent: highlighted text
        AnnotationEvent annoEvent = AnnotationEvent.builder()
            .actor(actor)
            .action(Action.HIGHLIGHTED.getValue())
            .object(Frame.builder()
                .id(part432.getId())
                .name(part432.getName())
                .isPartOf(part432.getIsPartOf())
                .index(2)
                .build())
            .generated(HighlightAnnotation.builder()
                .id("https://someEduApp.edu/highlights/" + UUID.randomUUID().toString())
                .annotated(Frame.builder()
                    .id(part432.getId())
                    .name(part432.getName())
                    .isPartOf(part432.getIsPartOf())
                    .index(2)
                    .build())
                .selectionStart(Integer.toString(455))
                .selectionEnd(Integer.toString(489))
                .selectionText("Life, Liberty and the pursuit of Happiness")
                .build())
            .eventTime(SampleTime.getDefaultEventTime().plusSeconds(240))
            .edApp(readium)
            .group(organization)
            .membership(membership)
            .build();

        // Process event
        eventSender.send(annoEvent);

        output.append("Generated Highlight AnnotationEvent \n");
        output.append("actor : " + annoEvent.getActor().getId() + "\n");
        output.append("action : " + annoEvent.getAction() + "\n");
        output.append("object : " + ((Frame) annoEvent.getObject()).getId() + "\n");
        output.append("generated : " + ((HighlightAnnotation) annoEvent.getGenerated()).getId()  + "\n");
        output.append("highlighted : " + ((HighlightAnnotation) annoEvent.getGenerated()).getSelectionText() + "\n\n");

        // Navigation Event: navigated to #epubcfi(/4/3/3) (Paul Revere)
        DigitalResource part433 = SampleReadingEntities.buildEpubGloriousCausePart433();
        navEvent = NavigationEvent.builder()
            .actor(actor)
            .action(Action.NAVIGATED_TO.getValue())
            .object(part433)
            .fromResource(Frame.builder()
                .id(part432.getId())
                .name(part432.getName())
                .isPartOf(part432.getIsPartOf())
                .index(2)
                .build())
            .target(Frame.builder()
                .id(part433.getId())
                .name(part433.getName())
                .isPartOf(part433.getIsPartOf())
                .index(3)
                .build())
            .eventTime(SampleTime.getDefaultEventTime().plusSeconds(250))
            .edApp(readium)
            .group(organization)
            .membership(membership)
            .build();

        // Process event
        eventSender.send(navEvent);

        output.append("Generated NavigationEvent \n");
        output.append("actor : " + navEvent.getActor().getId() + "\n");
        output.append("action : " + navEvent.getAction() + "\n");
        output.append("object : " + ((EpubPart) navEvent.getObject()).getId() + "\n");
        output.append("fromResource : " + navEvent.getFromResource().getId() + "\n");
        output.append("target : " + ((Frame) navEvent.getTarget()).getId() + "\n\n");

        // ViewEvent: viewed #epubcfi(/4/3/3) (Paul Revere)
        readEvent = ReadingEvent.builder()
            .actor(actor)
            .action(Action.VIEWED.getValue())
            .object(part433)
            .target(Frame.builder()
                .id(part433.getId())
                .name(part433.getName())
                .isPartOf(part433.getIsPartOf())
                .index(3)
                .build())
            .eventTime(SampleTime.getDefaultEventTime().plusSeconds(320))
            .edApp(readium)
            .group(organization)
            .membership(membership)
            .build();

        // Process event
        eventSender.send(readEvent);

        output.append("Generated ViewEvent \n");
        output.append("actor : " + navEvent.getActor().getId() + "\n");
        output.append("action : " + navEvent.getAction() + "\n");
        output.append("object : " + ((EpubPart) readEvent.getObject()).getId() + "\n");
        output.append("target : " + ((Frame) readEvent.getTarget()).getId() + "\n\n");

        // BookmarkAnnotationEvent: bookmarked the reading with a note
        annoEvent = AnnotationEvent.builder()
            .actor(actor)
            .action(Action.BOOKMARKED.getValue())
            .object(Frame.builder()
                .id(part433.getId())
                .name(part433.getName())
                .isPartOf(part433.getIsPartOf())
                .index(3)
                .build())
            .generated(BookmarkAnnotation.builder()
                .id("https://someEduApp.edu/bookmarks/" + UUID.randomUUID().toString())
                .annotated(Frame.builder()
                    .id(part433.getId())
                    .name(part433.getName())
                    .isPartOf(part433.getIsPartOf())
                    .index(3)
                    .build())
                .bookmarkNotes("The Intolerable Acts (1774)--bad idea Lord North")
                .build())
            .eventTime(SampleTime.getDefaultEventTime().plusSeconds(325))
            .edApp(readium)
            .group(organization)
            .membership(membership)
            .build();

        eventSender.send(annoEvent);

        output.append("Generated Bookmark AnnotationEvent \n");
        output.append("actor : " + annoEvent.getActor().getId() + "\n");
        output.append("action : " + annoEvent.getAction() + "\n");
        output.append("object : " + ((Frame) annoEvent.getObject()).getId() + "\n");
        output.append("generated : " + ((BookmarkAnnotation) annoEvent.getGenerated()).getId()  + "\n");
        output.append("bookmark notes : " + ((BookmarkAnnotation) annoEvent.getGenerated()).getBookmarkNotes() + "\n\n");

        // NavigationEvent: navigated to CourseSmart content
        SoftwareApplication courseSmart = SampleAgentEntities.buildCourseSmartReader();
        DigitalResource volume = SampleReadingEntities.buildEpubAllisonAmRevVolume();
        DigitalResource fromResource = SampleReadingEntities.buildAmRev101LandingPage();

        navEvent = NavigationEvent.builder()
            .actor(actor)
            .action(Action.NAVIGATED_TO.getValue())
            .object(volume)
            .fromResource(fromResource)
            .target(Frame.builder()
                .id(volume.getId())
                .name(volume.getName())
                .index(0)
                .build())
            .eventTime(SampleTime.getDefaultEventTime().plusSeconds(330))
            .edApp(courseSmart)
            .group(organization)
            .membership(membership)
            .build();

        // Process event
        eventSender.send(navEvent);

        output.append("Generated NavigationEvent \n");
        output.append("actor : " + navEvent.getActor().getId() + "\n");
        output.append("action : " + navEvent.getAction() + "\n");
        output.append("object : " + ((EpubVolume) navEvent.getObject()).getId() + "\n");
        output.append("fromResource : " + navEvent.getFromResource().getId() + "\n");
        output.append("target : " + ((Frame) navEvent.getTarget()).getId() + "\n\n");

        // NavigationEvent: navigated to aXfsadf12
        chapter = SampleReadingEntities.buildEpubAllisonAmRevSubChapter();
        navEvent = NavigationEvent.builder()
            .actor(actor)
            .action(Action.NAVIGATED_TO.getValue())
            .object(chapter)
            .fromResource(Frame.builder()
                .id(volume.getId())
                .index(0)
                .build())
            .target(Frame.builder()
                .id(chapter.getId())
                .name(chapter.getName())
                .isPartOf(chapter.getIsPartOf())
                .index(1)
                .build())
            .eventTime(SampleTime.getDefaultEventTime().plusSeconds(340))
            .edApp(courseSmart)
            .group(organization)
            .membership(membership)
                .build();

        eventSender.send(navEvent);

        output.append("Generated NavigationEvent \n");
        output.append("actor : " + navEvent.getActor().getId() + "\n");
        output.append("action : " + navEvent.getAction() + "\n");
        output.append("object : " + ((EpubSubChapter) navEvent.getObject()).getId() + "\n");
        output.append("fromResource : " + navEvent.getFromResource().getId() + "\n");
        output.append("target : " + ((Frame) navEvent.getTarget()).getId() + "\n\n");

        // ViewEvent: viewed aXfsadf12
        readEvent = ReadingEvent.builder()
            .actor(actor)
            .action(Action.VIEWED.getValue())
            .object(chapter)
            .target(Frame.builder()
                .id(chapter.getId())
                .name(chapter.getName())
                .isPartOf(chapter.getIsPartOf())
                .version(chapter.getVersion())
                .index(1)
                .build())
            .eventTime(SampleTime.getDefaultEventTime().plusSeconds(400))
            .edApp(courseSmart)
            .group(organization)
            .membership(membership)
            .build();

        eventSender.send(readEvent);

        output.append("Generated ViewEvent \n");
        output.append("actor : " + readEvent.getActor().getId() + "\n");
        output.append("action : " + readEvent.getAction() + "\n");
        output.append("object : " + ((EpubSubChapter) readEvent.getObject()).getId() + "\n");
        output.append("target : " + ((Frame) readEvent.getTarget()).getId() + "\n\n");

        // Tag AnnotationEvent: tagged reading
        annoEvent = AnnotationEvent.builder()
            .actor(actor)
            .action(Action.TAGGED.getValue())
            .object(Frame.builder()
                .id(chapter.getId())
                .name(chapter.getName())
                .isPartOf(chapter.getIsPartOf())
                .version(chapter.getVersion())
                .index(1)
                .build())
            .generated(TagAnnotation.builder()
                .id("https://someEduApp.edu/tags/" + UUID.randomUUID().toString())
                .annotated(Frame.builder()
                    .id(chapter.getId())
                    .name(chapter.getName())
                    .isPartOf(chapter.getIsPartOf())
                    .version(chapter.getVersion())
                    .index(1)
                    .build())
                .tags(Lists.newArrayList("to-read", "1776", "shared-with-project-team"))
                .build())
            .eventTime(SampleTime.getDefaultEventTime().plusSeconds(420))
            .edApp(courseSmart)
            .group(organization)
            .membership(membership)
                .build();

        // Process event
        eventSender.send(annoEvent);

        output.append("Generated Tag AnnotationEvent \n");
        output.append("actor : " + annoEvent.getActor().getId() + "\n");
        output.append("action : " + annoEvent.getAction() + "\n");
        output.append("object : " + ((Frame) annoEvent.getObject()).getId() + "\n");
        output.append("generated : " + ((TagAnnotation) annoEvent.getGenerated()).getId()  + "\n");
        output.append("tags : " + ((TagAnnotation) annoEvent.getGenerated()).getTags().toString() + "\n\n");

        // Shared AnnotationEvent: shared reading with other students
        annoEvent = AnnotationEvent.builder()
            .actor(actor)
            .action(Action.SHARED.getValue())
            .object(Frame.builder()
                .id(chapter.getId())
                .name(chapter.getName())
                .isPartOf(chapter.getIsPartOf())
                .version(chapter.getVersion())
                .index(1)
                .build())
            .generated(SharedAnnotation.builder()
                .id("https://someEduApp.edu/shared/" + UUID.randomUUID().toString())
                .annotated(Frame.builder()
                    .id(chapter.getId())
                    .name(chapter.getName())
                    .isPartOf(chapter.getIsPartOf())
                    .version(chapter.getVersion())
                    .index(1)
                    .build())
                .withAgents(Lists.<Agent>newArrayList(
                    Person.builder()
                        .id("https://example.edu/user/657585")
                        .dateCreated(SampleTime.getDefaultDateCreated())
                        .dateModified(SampleTime.getDefaultDateModified())
                        .build(),
                    Person.builder()
                        .id("https://example.edu/user/667788")
                        .dateCreated(SampleTime.getDefaultDateCreated())
                        .dateModified(SampleTime.getDefaultDateModified())
                        .build()))
                .build())
            .eventTime(SampleTime.getDefaultEventTime().plusSeconds(440))
            .edApp(courseSmart)
            .group(organization)
            .membership(membership)
            .build();

        // Process event
        eventSender.send(annoEvent);

        output.append("Generated Shared AnnotationEvent \n");
        output.append("actor : " + annoEvent.getActor().getId() + "\n");
        output.append("action : " + annoEvent.getAction() + "\n");
        output.append("object : " + ((Frame) annoEvent.getObject()).getId() + "\n");
        output.append("generated : " + ((SharedAnnotation) annoEvent.getGenerated()).getId()  + "\n");

        // Retrieve agents
        SharedAnnotation shared = (SharedAnnotation) annoEvent.getGenerated();
        for (Agent agent: shared.getWithAgents()) {
            output.append("Shared with: " + agent.getId() + "\n");
        }
        output.append("\n");

        // SessionEvent: logged out of Canvas LMS
        sessionEvent = SessionEvent.builder()
            .actor(actor)
            .action(Action.LOGGED_OUT.getValue())
            .object(canvas)
            .target(Session.builder()
                .id("https://example.edu/session-123456789")
                .name("session-123456789")
                .actor(actor)
                .dateCreated(SampleTime.getDefaultDateCreated())
                .startedAtTime(SampleTime.getDefaultStartedAtTime())
                .endedAtTime(SampleTime.getDefaultEventTime().plusSeconds(445))
                .duration("PT445S")
                .build())
            .eventTime(SampleTime.getDefaultEventTime().plusSeconds(445))
            .edApp(canvas)
            .group(organization)
            .membership(membership)
            .build();

        // Process event
        eventSender.send(sessionEvent);

        output.append("Generated SessionEvent \n");
        output.append("actor : " + sessionEvent.getActor().getId() + "\n");
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

        // EdApp, Agent, Organization (CourseSection), Membership
        SoftwareApplication canvas = SampleAgentEntities.buildCanvas();
        Person actor = SampleAgentEntities.buildStudent554433();
        Organization organization = SampleLISEntities.buildAmRev101CourseSection();
        Membership membership = SampleLISEntities.buildAmRev101Membership(actor, organization, Role.LEARNER, Status.ACTIVE);

        SessionEvent sessionEvent = SessionEvent.builder()
            .actor(actor)
            .action(Action.LOGGED_IN.getValue())
            .object(canvas)
            .target(SampleReadingEntities.buildAmRev101LandingPage())
            .generated(Session.builder()
                .id("https://example.edu/session-123456789")
                .name("session-123456789")
                .actor(actor)
                .dateCreated(SampleTime.getDefaultDateCreated())
                .startedAtTime(SampleTime.getDefaultStartedAtTime())
                .build())
            .eventTime(SampleTime.getDefaultEventTime())
            .edApp(canvas)
            .group(organization)
            .membership(membership)
            .build();

        // Process event
        eventSender.send(sessionEvent);

        output.append("Generated SessionEvent \n");
        output.append("actor : " + sessionEvent.getActor().getId() + "\n");
        output.append("action : " + sessionEvent.getAction() + "\n");
        output.append("object : " + ((SoftwareApplication) sessionEvent.getObject()).getId() + "\n");
        output.append("target : " + ((DigitalResource) sessionEvent.getTarget()).getId() + "\n");
        output.append("generated : " + ((Session) sessionEvent.getGenerated()).getId() + "\n\n");

        //NavigationEvent: navigated to assessment
        SoftwareApplication quizEngine = SampleAgentEntities.buildQuizEngine();
        Assessment assessment = SampleAssessmentEntities.buildAssessment();
        NavigationEvent navEvent = NavigationEvent.builder()
            .actor(actor)
            .action(Action.NAVIGATED_TO.getValue())
            .object(assessment)
            .fromResource(SampleReadingEntities.buildAmRev101LandingPage())
            .target(Frame.builder()
                .id(assessment.getId())
                .name(assessment.getName())
                .index(0)
                .build())
            .eventTime(SampleTime.getDefaultEventTime().plusSeconds(60))
            .edApp(quizEngine)
            .group(organization)
            .membership(membership)
                .build();

        // Process event
        eventSender.send(navEvent);

        output.append("Generated NavigationEvent \n");
        output.append("actor : " + navEvent.getActor().getId() + "\n");
        output.append("action : " + navEvent.getAction() + "\n");
        output.append("object : " + ((Assessment) navEvent.getObject()).getId() + "\n");
        output.append("fromResource : " + navEvent.getFromResource().getId() + "\n");
        output.append("target : " + ((Frame) navEvent.getTarget()).getId() + "\n\n");

        // Assessment Attempt (90 sec)
        Attempt assessAttempt = Attempt.builder()
            .id(assessment.getId() + "/attempt/001")
            .assignable(assessment)
            .actor(actor)
            .count(1) // First attempt
            .startedAtTime(SampleTime.getDefaultStartedAtTime().plusSeconds(90))
            .build();

        // AssessmentEvent: started assessment
        AssessmentEvent assessmentEvent = AssessmentEvent.builder()
            .actor(actor)
            .action(Action.STARTED.getValue())
            .object(assessment)
            .generated(assessAttempt)
            .eventTime(assessAttempt.getStartedAtTime())
            .edApp(quizEngine)
            .group(organization)
            .membership(membership)
                .build();

        // Process event
        eventSender.send(assessmentEvent);

        output.append("Generated AssessmentEvent \n");
        output.append("actor : " + assessmentEvent.getActor().getId() + "\n");
        output.append("action : " + assessmentEvent.getAction() + "\n");
        output.append("object : " + ((Assessment) assessmentEvent.getObject()).getId() + "\n");
        output.append("generated attempt count : " + Integer.toString(((Attempt) assessmentEvent.getGenerated()).getCount()) + "\n\n");

        // AssessmentItem Event: started item 01 (95 sec)
        AssessmentItem item01 = SampleAssessmentEntities.buildAssessmentItems(assessment).get(0);

        // Item01 Attempt (120 sec)
        Attempt item01Attempt = Attempt.builder()
            .id(assessment.getId() + "/item/001/attempt/001")
            .actor(actor)
            .assignable(item01)
            .count(1)
            .startedAtTime(assessAttempt.getStartedAtTime().plusSeconds(5))
            .build();

        AssessmentItemEvent itemEvent = AssessmentItemEvent.builder()
            .actor(actor)
            .object(item01)
            .action(Action.STARTED.getValue())
            .generated(item01Attempt)
            .eventTime(item01Attempt.getStartedAtTime())
            .edApp(quizEngine)
            .group(organization)
            .membership(membership)
            .build();

        // Process event
        eventSender.send(itemEvent);

        output.append("Generated AssessmentItemEvent \n");
        output.append("actor : " + itemEvent.getActor().getId() + "\n");
        output.append("action : " + itemEvent.getAction() + "\n");
        output.append("object : " + ((AssessmentItem) itemEvent.getObject()).getId() + "\n");
        output.append("generated attempt count : " + Integer.toString(((Attempt) itemEvent.getGenerated()).getCount()) + "\n\n");

        // AssessmentItemEvent: completed item 01 (95 - 120 secs)
        itemEvent = AssessmentItemEvent.builder()
            .actor(actor)
            .action(Action.COMPLETED.getValue())
            .object(item01)
            .generated(MultipleChoiceResponse.builder()
                .id(item01.getId())
                .assignable(assessment)
                .actor(actor)
                .attempt(item01Attempt)
                .value("A")
                .startedAtTime(item01Attempt.getStartedAtTime())
                .endedAtTime(item01Attempt.getStartedAtTime().plusSeconds(25))
                .duration("PT25S")
                .build())
            .eventTime(item01Attempt.getStartedAtTime().plusSeconds(25))
            .edApp(quizEngine)
            .group(organization)
            .membership(membership)
                .build();

        // Process Event
        eventSender.send(itemEvent);

        output.append("Generated AssessmentItemEvent \n");
        output.append("actor : " + itemEvent.getActor().getId() + "\n");
        output.append("action : " + itemEvent.getAction() + "\n");
        output.append("object : " + ((AssessmentItem) itemEvent.getObject()).getId() + "\n");
        output.append("generated response : " + ((MultipleChoiceResponse) itemEvent.getGenerated()).getValue() + "\n\n");

        // AssessmentItemEvent: started item 02 (121 sec)
        AssessmentItem item02 = SampleAssessmentEntities.buildAssessmentItems(assessment).get(1);

        // Item02 attempt
        Attempt item02Attempt = Attempt.builder()
            .id(assessment.getId() + "//item/002/attempt/001")
            .actor(actor)
            .assignable(item02)
            .count(1)
            .startedAtTime(assessAttempt.getStartedAtTime().plusSeconds(31))
            .build();

        itemEvent = AssessmentItemEvent.builder()
            .actor(actor)
            .action(Action.STARTED.getValue())
            .object(item02)
            .generated(item02Attempt)
            .eventTime(item02Attempt.getStartedAtTime())
            .edApp(quizEngine)
            .group(organization)
            .membership(membership)
            .build();

        // Process event
        eventSender.send(itemEvent);

        output.append("Generated AssessmentItemEvent \n");
        output.append("actor : " + itemEvent.getActor().getId() + "\n");
        output.append("action : " + itemEvent.getAction() + "\n");
        output.append("object : " + ((AssessmentItem) itemEvent.getObject()).getId() + "\n");
        output.append("generated attempt count : " + Integer.toString(((Attempt) itemEvent.getGenerated()).getCount()) + "\n\n");

        // AssessmentItemEvent: completed item 02 (121 - 150 secs)
        itemEvent = AssessmentItemEvent.builder()
            .actor(actor)
            .action(Action.COMPLETED.getValue())
            .object(item02)
            .generated(MultipleChoiceResponse.builder()
                .id(item02.getId())
                .assignable(assessment)
                .actor(actor)
                .attempt(item02Attempt)
                .value("C")
                .startedAtTime(item02Attempt.getStartedAtTime())
                .endedAtTime(item02Attempt.getStartedAtTime().plusSeconds(29))
                .duration("PT29S")
                .build())
            .eventTime(item02Attempt.getStartedAtTime().plusSeconds(29))
            .edApp(quizEngine)
            .group(organization)
            .membership(membership)
            .build();

        // Process event
        eventSender.send(itemEvent);

        output.append("Generated AssessmentItemEvent \n");
        output.append("actor : " + itemEvent.getActor().getId() + "\n");
        output.append("action : " + itemEvent.getAction() + "\n");
        output.append("object : " + ((AssessmentItem) itemEvent.getObject()).getId() + "\n");
        output.append("generated response : " + ((MultipleChoiceResponse) itemEvent.getGenerated()).getValue() + "\n\n");

        // AssessmentItemEvent: started item 03 (151 sec)
        AssessmentItem item03 = SampleAssessmentEntities.buildAssessmentItems(assessment).get(2);

        // Item03 attempt
        Attempt item03Attempt = Attempt.builder()
            .id(assessment.getId() + "/item/003/attempt/001")
            .actor(actor)
            .assignable(item03)
            .count(1)
            .startedAtTime(assessAttempt.getStartedAtTime().plusSeconds(61))
            .build();

        itemEvent = AssessmentItemEvent.builder()
            .actor(actor)
            .action(Action.STARTED.getValue())
            .object(item03)
            .generated(item03Attempt)
            .eventTime(item03Attempt.getStartedAtTime())
            .edApp(quizEngine)
            .group(organization)
            .membership(membership)
            .build();

        // Process event
        eventSender.send(itemEvent);

        output.append("Generated AssessmentItemEvent \n");
        output.append("actor : " + itemEvent.getActor().getId() + "\n");
        output.append("action : " + itemEvent.getAction() + "\n");
        output.append("object : " + ((AssessmentItem) itemEvent.getObject()).getId() + "\n");
        output.append("generated attempt count : " + Integer.toString(((Attempt) itemEvent.getGenerated()).getCount()) + "\n\n");

        // AssessmentItemEvent: completed item 03 (151 - 180 secs)
        itemEvent = AssessmentItemEvent.builder()
            .actor(actor)
            .action(Action.COMPLETED.getValue())
            .object(item03)
            .generated(MultipleChoiceResponse.builder()
                .id(item03.getId())
                .assignable(assessment)
                .actor(actor)
                .attempt(item03Attempt)
                .value("B")
                .startedAtTime(item03Attempt.getStartedAtTime())
                .endedAtTime(item03Attempt.getStartedAtTime().plusSeconds(29))
                .duration("PT29S")
                .build())
            .eventTime(item03Attempt.getStartedAtTime().plusSeconds(29))
            .edApp(quizEngine)
            .group(organization)
            .membership(membership)
            .build();

        // Process event
        eventSender.send(navEvent);

        output.append("Generated AssessmentItemEvent \n");
        output.append("actor : " + itemEvent.getActor().getId() + "\n");
        output.append("action : " + itemEvent.getAction() + "\n");
        output.append("object : " + ((AssessmentItem) itemEvent.getObject()).getId() + "\n");
        output.append("generated response : " + ((MultipleChoiceResponse) itemEvent.getGenerated()).getValue() + "\n\n");

        // AssessmentEvent: submitted assessment (181 sec)
        assessmentEvent = AssessmentEvent.builder()
            .actor(actor)
            .action(Action.SUBMITTED.getValue())
            .object(assessment)
            .generated(assessAttempt)
            .eventTime(assessAttempt.getStartedAtTime().plusSeconds(91))
            .edApp(quizEngine)
            .group(organization)
            .membership(membership)
            .build();

        // Process event
        eventSender.send(assessmentEvent);

        output.append("Generated AssessmentEvent \n");
        output.append("actor : " + assessmentEvent.getActor().getId() + "\n");
        output.append("action : " + assessmentEvent.getAction() + "\n");
        output.append("object : " + ((Assessment) assessmentEvent.getObject()).getId() + "\n");
        output.append("generated attempt : " + Integer.toString(((Attempt) assessmentEvent.getGenerated()).getCount()) + "\n\n");

        // OutcomeEvent: generated result (182 sec)
        OutcomeEvent outcomeEvent = OutcomeEvent.builder()
            .actor(quizEngine)
            .action(Action.GRADED.getValue())
            .object(assessAttempt)
            .generated(Result.builder()
                .id("https://example.edu/politicalScience/2014/american-revolution-101/activityContext1/attempt/001/result")
                .totalScore(4.2d)
                .normalScore(4.2d)
                .assignable(assessment)
                .actor(actor)
                .scoredBy(quizEngine)
                .build())
            .eventTime(assessAttempt.getStartedAtTime().plusSeconds(92))
            .edApp(quizEngine)
            .group(organization)
            .build();

        // Process event
        eventSender.send(outcomeEvent);

        output.append("Generated OutcomeEvent \n");
        output.append("actor : " + outcomeEvent.getActor().getId() + "\n");
        output.append("action : " + outcomeEvent.getAction() + "\n");
        output.append("object : " + ((Attempt) outcomeEvent.getObject()).getId() + "\n");
        output.append("attempt count : " + Integer.toString(((Attempt) outcomeEvent.getObject()).getCount()) + "\n");
        output.append("generated outcome : " + String.valueOf(((Result) outcomeEvent.getGenerated()).getTotalScore()) + "\n");
        output.append("scored by : " + ((Result) outcomeEvent.getGenerated()).getScoredBy() + "\n\n");

        // Session Event: logged out of Canvas LMS (185 sec)
        sessionEvent = SessionEvent.builder()
            .actor(actor)
            .action(Action.LOGGED_OUT.getValue())
            .object(canvas)
            .target(Session.builder()
                .id("https://example.edu/session-123456789")
                .name("session-123456789")
                .actor(actor)
                .dateCreated(SampleTime.getDefaultDateCreated())
                .startedAtTime(SampleTime.getDefaultStartedAtTime())
                .endedAtTime(SampleTime.getDefaultEventTime().plusSeconds(95))
                .duration("PT185S")
                .build())
            .eventTime(SampleTime.getDefaultEventTime().plusSeconds(95))
            .edApp(canvas)
            .group(organization)
            .membership(membership)
            .build();

        // Process event
        eventSender.send(sessionEvent);

        output.append("Generated SessionEvent \n");
        output.append("actor : " + sessionEvent.getActor().getId() + "\n");
        output.append("action : " + sessionEvent.getAction() + "\n");
        output.append("object : " + ((SoftwareApplication) sessionEvent.getObject()).getId() + "\n");
        output.append("target : " + ((Session) sessionEvent.getTarget()).getId() + "\n\n");

        output.append("FINIS\n\n");
    }
}