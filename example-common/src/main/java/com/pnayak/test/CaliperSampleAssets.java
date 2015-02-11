package com.pnayak.test;

import com.google.common.collect.ImmutableList;
import org.imsglobal.caliper.entities.*;
import org.imsglobal.caliper.entities.assessment.Assessment;
import org.imsglobal.caliper.entities.assessment.AssessmentItem;
import org.imsglobal.caliper.entities.foaf.Agent;
import org.imsglobal.caliper.entities.lis.CourseSection;
import org.imsglobal.caliper.entities.lis.Person;
import org.imsglobal.caliper.entities.media.VideoObject;
import org.imsglobal.caliper.entities.reading.EpubSubChapter;
import org.imsglobal.caliper.entities.reading.EpubVolume;
import org.joda.time.DateTime;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class CaliperSampleAssets {

    private static DateTime now = DateTime.now();

    /**
     * Sample Amm-Rev 101 Course section.
     * @return course section
     */
    public static final CourseSection buildAmRev101CourseSection() {
        return CourseSection.builder()
            .id("https://some-university.edu/politicalScience/2014/american-revolution-101")
            .semester("Spring-2014")
            .courseNumber("AmRev-101")
            //.sectionNumber("001") TODO add section number property?
            .label("Am Rev 101")
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
            .id("AmRev-101-landingPage")
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
     * Sample Assessment.
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
            .assessmentItems(buildAssessmentItems())
            .build();
    }

    /**
     * Sample assessment items
     * @return immutable list
     */
    public static final ImmutableList<AssessmentItem> buildAssessmentItems() {
        return ImmutableList.<AssessmentItem>builder()
            .add(AssessmentItem.builder()
                .id("https://some-university.edu/politicalScience/2014/american-revolution-101/assessment1/item1")
                .name("Assessment Item 1")
                .isPartOf("https://some-university.edu/politicalScience/2014/american-revolution-101/assessment1")
                .build())
            .add(AssessmentItem.builder()
                .id("https://some-university.edu/politicalScience/2014/american-revolution-101/assessment1/item2")
                .name("Assessment Item 2")
                .isPartOf("https://some-university.edu/politicalScience/2014/american-revolution-101/assessment1")
                .build())
            .add(AssessmentItem.builder()
                .id("https://some-university.edu/politicalScience/2014/american-revolution-101/assessment1/item3")
                .name("Assessment Item 3")
                .isPartOf("https://some-university.edu/politicalScience/2014/american-revolution-101/assessment1")
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
            .lisOrganization(buildAmRev101CourseSection())
            .agent(buildStudent554433())
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
            .lisOrganization(buildAmRev101CourseSection())
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
            .lisOrganization(buildAmRev101CourseSection())
            .agent(buildStudent554433())
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
            .lisOrganization(buildAmRev101CourseSection())
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
            .lisOrganization(buildAmRev101CourseSection())
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
     * Generate create date.
     * @return ISO-8601 date
     */
    public static Date getDefaultDateCreated(){
        //January 1, 2015, 06:00:00.000 GMT
        Calendar cal = Calendar.getInstance();
        cal.setTimeZone(TimeZone.getTimeZone("GMT"));
        cal.set(Calendar.YEAR, 2015);
        cal.set(Calendar.MONTH, Calendar.JANUARY);
        cal.set(Calendar.DATE, 1);
        cal.set(Calendar.HOUR_OF_DAY, 6);
        cal.set(Calendar.MINUTE,0);
        cal.set(Calendar.SECOND,0);
        cal.set(Calendar.MILLISECOND,0);
        return cal.getTime();
    }

    /**
     * Generate modified date.
     * @return ISO-8601 date
     */
    public static Date getDefaultDateModified(){
        //February 2, 2015, 11:30:00.000 GMT
        Calendar cal = Calendar.getInstance();
        cal.setTimeZone(TimeZone.getTimeZone("GMT"));
        cal.set(Calendar.YEAR, 2015);
        cal.set(Calendar.MONTH, Calendar.FEBRUARY);
        cal.set(Calendar.DATE, 2);
        cal.set(Calendar.HOUR_OF_DAY, 11);
        cal.set(Calendar.MINUTE, 30);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    /**
     * Generate published date.
     * @return ISO-8601 date
     */
    public static Date getDefaultDatePublished(){
        //January 15, 2015, 09:30:00.000 GMT
        Calendar cal = Calendar.getInstance();
        cal.setTimeZone(TimeZone.getTimeZone("GMT"));
        cal.set(Calendar.YEAR, 2015);
        cal.set(Calendar.MONTH, Calendar.JANUARY);
        cal.set(Calendar.DATE, 15);
        cal.set(Calendar.HOUR_OF_DAY, 9);
        cal.set(Calendar.MINUTE, 30);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    /**
     * Generate activated date
     * @return ISO-8601 date
     */
    public static Date getDefaultDateToActivate(){
        //January 16, 2015, 05:00:00.000 GMT
        Calendar cal = Calendar.getInstance();
        cal.setTimeZone(TimeZone.getTimeZone("GMT"));
        cal.set(Calendar.YEAR, 2015);
        cal.set(Calendar.MONTH, Calendar.JANUARY);
        cal.set(Calendar.DATE, 16);
        cal.set(Calendar.HOUR_OF_DAY, 5);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    /**
     * Generate shown date
     * @return ISO-8601 date
     */
    public static Date getDefaultDateToShow() {
        return getDefaultDateToActivate();
    }

    /**
     * Generate submit date
     * @return ISO-8601 date
     */
    public static Date getDefaultDateToSubmit() {
        //February 28, 2015, 11:59:59.000 GMT
        Calendar cal = Calendar.getInstance();
        cal.setTimeZone(TimeZone.getTimeZone("GMT"));
        cal.set(Calendar.YEAR, 2015);
        cal.set(Calendar.MONTH, Calendar.FEBRUARY);
        cal.set(Calendar.DATE, 28);
        cal.set(Calendar.HOUR_OF_DAY, 11);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    /**
     * Generate started at time.
     * @return ISO-8601 date
     */
    public static Date getDefaultStartedAtTime() {
        //February 15, 2015, 10:15:00.000 GMT
        Calendar cal = Calendar.getInstance();
        cal.setTimeZone(TimeZone.getTimeZone("GMT"));
        cal.set(Calendar.YEAR, 2015);
        cal.set(Calendar.MONTH, Calendar.FEBRUARY);
        cal.set(Calendar.DATE, 15);
        cal.set(Calendar.HOUR_OF_DAY, 10);
        cal.set(Calendar.MINUTE, 15);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    /**
     * Generate ended at time.
     * @return ISO-8601 date
     */
    public static Date getDefaultEndedAtTime() {
        //February 15, 2015, 11:05:00.000 GMT
        Calendar cal = Calendar.getInstance();
        cal.setTimeZone(TimeZone.getTimeZone("GMT"));
        cal.set(Calendar.YEAR, 2015);
        cal.set(Calendar.MONTH, Calendar.FEBRUARY);
        cal.set(Calendar.DATE, 15);
        cal.set(Calendar.HOUR_OF_DAY, 11);
        cal.set(Calendar.MINUTE, 05);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }
}