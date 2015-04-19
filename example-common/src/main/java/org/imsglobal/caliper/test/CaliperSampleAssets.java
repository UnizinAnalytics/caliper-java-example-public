package org.imsglobal.caliper.test;

import com.google.common.collect.ImmutableList;
import org.imsglobal.caliper.actions.Action;
import org.imsglobal.caliper.entities.*;
import org.imsglobal.caliper.entities.agent.Person;
import org.imsglobal.caliper.entities.agent.SoftwareApplication;
import org.imsglobal.caliper.entities.assessment.Assessment;
import org.imsglobal.caliper.entities.assessment.AssessmentItem;
import org.imsglobal.caliper.entities.assignable.Attempt;
import org.imsglobal.caliper.entities.foaf.Agent;
import org.imsglobal.caliper.entities.lis.CourseSection;
import org.imsglobal.caliper.entities.media.VideoObject;
import org.imsglobal.caliper.entities.reading.EpubSubChapter;
import org.imsglobal.caliper.entities.reading.EpubVolume;
import org.imsglobal.caliper.entities.reading.WebPage;
import org.imsglobal.caliper.entities.response.Response;
import org.imsglobal.caliper.entities.response.SelectTextResponse;
import org.imsglobal.caliper.entities.session.Session;
import org.imsglobal.caliper.events.SessionEvent;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

/**
 * Set of sample entities that are used in the construction of a Caliper event.
 */
public class CaliperSampleAssets {

    private static DateTime now = DateTime.now();

    /**
     * Sample Amm-Rev 101 Course section.
     * @return course section
     */
    public static final CourseSection buildAmRev101CourseSection() {
        return CourseSection.builder()
            .id("https://some-university.edu/politicalScience/2014/american-revolution-101")
            .academicSession("Spring-2014")
            .courseNumber("AmRev-101")
            .name("American Revolution 101")
            .dateCreated(getDefaultDateCreated())
            .dateModified(getDefaultDateModified())
            .build();
    }

    /**
     * Sample Am-Rev 101 landing page.
     * @return web page
     */
    public static final WebPage buildAmRev101LandingPage() {
        return WebPage.builder()
            .id("https://some-university.edu/politicalScience/2014/american-revolution-101/index.html")
            .name("American Revolution 101 Landing Page")
            .isPartOf(SoftwareApplication.builder()
                .id("https://canvas.instructure.com")
                .dateCreated(getDefaultDateCreated())
                .dateModified(getDefaultDateModified())
                .build())
            .dateCreated(getDefaultDateCreated())
            .build();
    }

    /**
     * Sample Assessment.  Pass a slim version of the parent assessment to .assessmentItems(Assessment parent)
     * in order to avoid generating an infinite loop during instance construction.
     *
     * @return assessment
     */
    public static final Assessment buildAssessment() {
        return Assessment.builder()
            .id("https://some-university.edu/politicalScience/2014/american-revolution-101/assessment1")
            .name("American Revolution - Key Figures Assessment")
            .dateCreated(getDefaultDateCreated())
            .datePublished(getDefaultDatePublished())
            .dateToActivate(getDefaultDateToActivate())
            .dateToShow(getDefaultDateToShow())
            .dateToSubmit(getDefaultDateToSubmit())
            .maxAttempts(2)
            .maxSubmits(2)
            .maxScore(5) // WARN original value "5.0d"
            .assessmentItems(buildAssessmentItems(Assessment.builder()
                .id("https://some-university.edu/politicalScience/2015/american-revolution-101/assessment1")
                .build()))
            .build();
    }

    public static final Attempt buildAttempt() {
        return Attempt.builder()
                .id("https://some-university.edu/politicalScience/2014/american-revolution-101/attempt1")
                .name("American Revolution - Key Figures Assessment")
                .dateCreated(getDefaultDateCreated())
                .startedAtTime(getDefaultStartedAtTime())
                .assignable(buildAssessment())
                .actor(buildStudent554433())
                .count(1)
                .build();
    }

    public static final Response buildResponse() {
        return SelectTextResponse.builder()
                .id("https://some-university.edu/politicalScience/2014/american-revolution-101/response1")
                .name("American Revolution - Key Figures Assessment")
                .dateCreated(getDefaultDateCreated())
                .startedAtTime(getDefaultStartedAtTime())
                .assignable(buildAssessment())
                .actor(buildStudent554433())
                .value("sdafgdfas")
                .attempt(buildAttempt())
                .build();
    }


    /**
     * Sample assessment items
     * @return immutable list
     */
    public static final ImmutableList<AssessmentItem> buildAssessmentItems(Assessment parent) {

        return ImmutableList.<AssessmentItem>builder()
                .add(AssessmentItem.builder()
                        .id("https://some-university.edu/politicalScience/2014/american-revolution-101/assessment1/item1")
                        .name("Assessment Item 1")
                        .isPartOf(parent)
                .build())
                .add(AssessmentItem.builder()
                        .id("https://some-university.edu/politicalScience/2014/american-revolution-101/assessment1/item2")
                        .name("Assessment Item 2")
                        .isPartOf(parent)
                .build())
                .add(AssessmentItem.builder()
                        .id("https://some-university.edu/politicalScience/2014/american-revolution-101/assessment1/item3")
                        .name("Assessment Item 3")
                        .isPartOf(parent)
                .build())
            .build();
    }

    /**
     * Sample learning context composed of an edApp (Canvas), organization (AmRev-101) and agent
     * (student 554433).
     *
     * @return learning context
     */
    public static final LearningContext buildCanvasLearningContext() {
        return LearningContext.builder()
                .edApp(SoftwareApplication.builder()
                        .id("https://canvas.instructure.com")
                        .dateCreated(getDefaultDateCreated())
                        .dateModified(getDefaultDateModified())
                        .build())
            .agent(buildStudent554433())
            .build();
    }

    /**
     * Build Canvas login event
     * @return sessionEvent
     */
    public static SessionEvent buildCanvasLoginEvent() {
        LearningContext canvas = buildCanvasLearningContext();

        return SessionEvent.builder()
                .edApp(canvas.getEdApp())
                .actor(canvas.getAgent())
            .action(Action.LOGGED_IN)
                .object(canvas.getEdApp())
                .target(buildEpubSubChap43())
                .generated(buildSessionStart())
                .startedAtTime(getDefaultStartedAtTime())
            .build();
    }

    /**
     * Build Canvas Logout event
     * @return sessionEvent
     */
    public static SessionEvent buildCanvasLogoutEvent() {
        LearningContext canvas = buildCanvasLearningContext();

        return SessionEvent.builder()
                .edApp(canvas.getEdApp())
                .actor(canvas.getAgent())
            .action(Action.LOGGED_OUT)
                .object(canvas.getEdApp())
                .target(buildSessionEnd())
            .startedAtTime(getDefaultStartedAtTime())
                .endedAtTime(getDefaultEndedAtTime())
            .duration("PT3000S")
            .build();
    }

    /**
     * Sample learning context composed of an edApp (CourseSmart), organization (AmRev-101) and agent
     * (student 554433).
     *
     * @return learning context
     */
    public static final LearningContext buildCourseSmartLearningContext() {
        return LearningContext.builder()
            .edApp(SoftwareApplication.builder()
                    .id("http://www.coursesmart.com/reader")
                    .dateCreated(getDefaultDateCreated())
                    .dateModified(getDefaultDateModified())
                    .build())
            .agent(buildStudent554433())
            .build();
    }

    /**
     * Sample EPUB fragment.
     * @return digital resource
     */
    public static final EpubSubChapter buildEpubSubChap43() {
        return EpubSubChapter.builder()
            .id("https://github.com/readium/readium-js-viewer/book/34843#epubcfi(/4/3)")
            .name("The Glorious Cause: The American Revolution, 1763-1789 (Oxford History of the United States)")
            .dateCreated(getDefaultDateCreated())
            .dateModified(getDefaultDateModified())
            .build();
    }

    /**
     * Sample EPUB fragment.
     * @return digital resource
     */
    public static final EpubSubChapter buildEpubSubChap431() {
        return EpubSubChapter.builder()
            .id("https://github.com/readium/readium-js-viewer/book/34843#epubcfi(/4/3/1)")
            .name("Key Figures: George Washington")
                .dateCreated(getDefaultDateCreated())
                .dateModified(getDefaultDateModified())
                .isPartOf(EpubSubChapter.builder()
                    .id("https://github.com/readium/readium-js-viewer/book/34843#epubcfi(/4/3)")
                    .name("The Glorious Cause: The American Revolution, 1763-1789 (Oxford History of the United States)")
                    .dateCreated(getDefaultDateCreated())
                    .dateModified(getDefaultDateModified())
                    .build())
            .build();
    }

    /**
     * Sample EPUB fragment.
     * @return digital resource
     */
    public static final EpubSubChapter buildEpubSubChap432() {
        return EpubSubChapter.builder()
            .id("https://github.com/readium/readium-js-viewer/book/34843#epubcfi(/4/3/2)")
            .name("Key Figures: Lord North")
                .dateCreated(getDefaultDateCreated())
                .dateModified(getDefaultDateModified())
                .isPartOf(EpubSubChapter.builder()
                    .id("https://github.com/readium/readium-js-viewer/book/34843#epubcfi(/4/3)")
                    .name("The Glorious Cause: The American Revolution, 1763-1789 (Oxford History of the United States)")
                    .dateCreated(getDefaultDateCreated())
                    .dateModified(getDefaultDateModified())
                    .build())
            .build();
    }

    /**
     * Sample EPUB fragment.
     * @return digital resource
     */
    public static final EpubSubChapter buildEpubSubChap433() {
        return EpubSubChapter.builder()
            .id("https://github.com/readium/readium-js-viewer/book/34843#epubcfi(/4/3/3)")
            .name("Key Figures: John Adams")
                .dateCreated(getDefaultDateCreated())
                .dateModified(getDefaultDateModified())
                .isPartOf(EpubSubChapter.builder()
                    .id("https://github.com/readium/readium-js-viewer/book/34843#epubcfi(/4/3)")
                    .name("The Glorious Cause: The American Revolution, 1763-1789 (Oxford History of the United States)")
                    .dateCreated(getDefaultDateCreated())
                    .dateModified(getDefaultDateModified())
                    .build())
            .build();
    }

    /**
     * Sample EPUB volume.
     * @return digital resource
     */
    public static final EpubVolume buildAllisonAmRevEpubVolume() {
        return EpubVolume.builder()
            .id("http://www.coursesmart.com/the-american-revolution-a-concise-history/robert-j-allison/dp/9780199347322")
            .name("The American Revolution: A Concise History | 978-0-19-531295-9")
            .dateCreated(getDefaultDateCreated())
            .dateModified(getDefaultDateModified())
            .build();
    }

    /**
     * Sample EPUB sub chapter.
     * @return digital resource
     */
    public static final EpubSubChapter buildAllisonAmRevEpubSubChap() {
        return EpubSubChapter.builder()
            .id("http://www.coursesmart.com/the-american-revolution-a-concise-history/robert-j-allison/dp/9780199347322/aXfsadf12")
            .name("The Boston Tea Party")
                .dateCreated(getDefaultDateCreated())
                .dateModified(getDefaultDateModified())
                .isPartOf((DigitalResource) EpubVolume.builder()
                    .id("http://www.coursesmart.com/the-american-revolution-a-concise-history/robert-j-allison/dp/9780199347322")
                    .name("The American Revolution: A Concise History | 978-0-19-531295-9")
                    .dateCreated(getDefaultDateCreated())
                    .dateModified(getDefaultDateModified())
                    .build())
            .build();
    }

    /**
     * Sample learning context
     * (student 554433).composed of an edApp (Readium), organization (AmRev-101) and agent
     * @return learning context
     */
    public static final LearningContext buildReadiumLearningContext() {
        return LearningContext.builder()
            .edApp(SoftwareApplication.builder()
                    .id("https://github.com/readium/readium-js-viewer")
                    .dateCreated(getDefaultDateCreated())
                    .dateModified(getDefaultDateModified())
                    .build())
            .agent(buildStudent554433())
            .build();
    }

    /**
     * @return Session
     */
    public static Session buildSessionStart() {
        return Session.builder()
            .id("https://univ.instructure.com/session-123456789")
            .name("session-123456789")
            .actor(buildStudent554433())
            .dateCreated(getDefaultDateCreated())
            .startedAtTime(getDefaultStartedAtTime())
            .build();
    }

    /**
     * @return Session
     */
    public static Session buildSessionEnd() {
        return Session.builder()
            .id("https://univ.instructure.com/session-123456789")
            .name("session-123456789")
            .actor(buildStudent554433())
            .dateCreated(getDefaultDateCreated())
            .startedAtTime(getDefaultStartedAtTime())
            .endedAtTime(getDefaultEndedAtTime())
            .duration("PT3000S")
                .build();
    }

    /**
     * Sample student.
     * @return agent
     */
    public static final Agent buildStudent554433() {
        return Person.builder()
            .id("https://some-university.edu/students/554433")
            .dateCreated(getDefaultDateCreated())
            .dateModified(getDefaultDateModified())
            .name("Student 1")
            .build();
    }

    /**
     * Sample Assessment Tool learning context.
     * @return
     */
    public static final LearningContext buildSuperAssessmentToolLearningContext() {
        return LearningContext.builder()
            .edApp(SoftwareApplication.builder()
                .id("https://com.sat/super-assessment-tool")
                .dateCreated(getDefaultDateCreated())
                .dateModified(getDefaultDateModified())
                .build())
            .agent(buildStudent554433())
            .build();
    }

    /**
     * Sample Media Tool learning context composed of an edApp (Media Tool), organization (AmRev-101) and agent
     * @return learning context
     */
    public static final LearningContext buildSuperMediaToolLearningContext() {
        return LearningContext.builder()
            .edApp(SoftwareApplication.builder()
                .id("https://com.sat/super-media-tool")
                .dateCreated(getDefaultDateCreated())
                .dateModified(getDefaultDateModified())
                .build())
            .agent(buildStudent554433())
            .build();
    }

    /**
     * Sample Video with learning objective.
     * @return video
     */
    public static final VideoObject buildVideoWithLearningObjective() {
        return VideoObject.builder()
            .id("https://com.sat/super-media-tool/video/video1")
            .name("American Revolution - Key Figures Video")
            .learningObjective(LearningObjective.builder()
                .id("http://americanrevolution.com/personalities/learn")
                .build())
            .duration(1420)
            .build();
    }

    /**
     * January 1, 2015, 06:00:00.000 GMT
     * @return return date created
     */
    public static DateTime getDefaultDateCreated() {
        return new DateTime(2015, 1, 1, 6, 0, 0, 0, DateTimeZone.UTC);
    }

    /**
     * February 2, 2015, 11:30:00.000 GMT
     * @return return date modified
     */
    public static DateTime getDefaultDateModified() {
        return new DateTime(2015, 2, 2, 11, 30, 0, 0, DateTimeZone.UTC);
    }

    /**
     * January 15, 2015, 09:30:00.000 GMT
     * @return return date published
     */
    public static DateTime getDefaultDatePublished(){
        return new DateTime(2015, 1, 15, 9, 30, 0, 0, DateTimeZone.UTC);
    }

    /**
     * January 16, 2015, 05:00:00.000 GMT
     * @return date to activate
     */
    public static DateTime getDefaultDateToActivate(){
        return new DateTime(2015, 1, 16, 5, 0, 0, 0, DateTimeZone.UTC);
    }

    /**
     * Same date as activate date
     * @return date to show
     */
    public static DateTime getDefaultDateToShow() {
        return getDefaultDateToActivate();
    }

    /**
     * Same date as activate date
     * @return date to start on
     */
    public static DateTime getDefaultDateToStartOn() {
        return getDefaultDateToActivate();
    }

    /**
     * February 28, 2015, 11:59:59.000 GMT
     * @return date to submit
     */
    public static DateTime getDefaultDateToSubmit() {
        return new DateTime(2015, 2, 28, 11, 59, 59, 0, DateTimeZone.UTC);
    }

    /**
     * February 15, 2015, 10:15:00.000 GMT
     * @return started at time
     */
    public static DateTime getDefaultStartedAtTime() {
        return new DateTime(2015, 2, 15, 10, 15, 0, 0, DateTimeZone.UTC);
    }

    /**
     * February 15, 2015, 11:05:00.000 GMT
     * @return ended at time
     */
    public static DateTime getDefaultEndedAtTime() {
        return new DateTime(2015, 2, 15, 11, 05, 0, 0, DateTimeZone.UTC);
    }
}