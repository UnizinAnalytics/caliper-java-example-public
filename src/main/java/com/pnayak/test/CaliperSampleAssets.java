package com.pnayak.test;

import com.google.common.collect.ImmutableList;
import org.imsglobal.caliper.entities.DigitalResource;
import org.imsglobal.caliper.entities.LearningContext;
import org.imsglobal.caliper.entities.LearningObjective;
import org.imsglobal.caliper.entities.SoftwareApplication;
import org.imsglobal.caliper.entities.WebPage;
import org.imsglobal.caliper.entities.assessment.Assessment;
import org.imsglobal.caliper.entities.assessment.AssessmentItem;
import org.imsglobal.caliper.entities.foaf.Agent;
import org.imsglobal.caliper.entities.lis.CourseSection;
import org.imsglobal.caliper.entities.lis.Person;
import org.imsglobal.caliper.entities.media.VideoObject;
import org.imsglobal.caliper.entities.reading.EpubSubChapter;
import org.imsglobal.caliper.entities.reading.EpubVolume;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Weeks;

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
            .lastModifiedTime(now.minus(Weeks.weeks(4)).getMillis())
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
            .partOf(SoftwareApplication.builder()
                .id("https://canvas.instructure.com")
                //.context("http://purl.imsglobal.org/ctx/caliper/v1/edApp/lms") // WARN CaliperEntity prop
                .lastModifiedTime(now.minus(Weeks.weeks(10)).getMillis())
                .build())
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
            .dateCreated(now.minus(Weeks.weeks(1)).getMillis())
            .datePublished(now.minus(Weeks.weeks(1)).getMillis())
            .dateToActivate(now.minus(Days.days(1)).getMillis())
            .dateToShow(now.minus(Days.days(1)).getMillis())
            .dateToSubmit(now.minus(Days.days(10)).getMillis())
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
                .partOf("https://some-university.edu/politicalScience/2014/american-revolution-101/assessment1")
                .build())
            .add(AssessmentItem.builder()
                .id("https://some-university.edu/politicalScience/2014/american-revolution-101/assessment1/item2")
                .name("Assessment Item 2")
                .partOf("https://some-university.edu/politicalScience/2014/american-revolution-101/assessment1")
                .build())
            .add(AssessmentItem.builder()
                .id("https://some-university.edu/politicalScience/2014/american-revolution-101/assessment1/item3")
                .name("Assessment Item 3")
                .partOf("https://some-university.edu/politicalScience/2014/american-revolution-101/assessment1")
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
                //.context("http://purl.imsglobal.org/ctx/caliper/v1/edApp/lms") // WARN CaliperEntity prop
                .lastModifiedTime(now.minus(Weeks.weeks(10)).getMillis())
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
                .lastModifiedTime(now.minus(Weeks.weeks(6)).getMillis())
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
            .lastModifiedTime(now.minus(Weeks.weeks(53)).getMillis())
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
            .lastModifiedTime(now.minus(Weeks.weeks(53)).getMillis())
            .partOf(EpubSubChapter.builder()
                .id("https://github.com/readium/readium-js-viewer/book/34843#epubcfi(/4/3)")
                .name("The Glorious Cause: The American Revolution, 1763-1789 (Oxford History of the United States)")
                .lastModifiedTime(now.minus(Weeks.weeks(53)).getMillis())
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
            .lastModifiedTime(now.minus(Weeks.weeks(53)).getMillis())
            .partOf(EpubSubChapter.builder()
                .id("https://github.com/readium/readium-js-viewer/book/34843#epubcfi(/4/3)")
                .name("The Glorious Cause: The American Revolution, 1763-1789 (Oxford History of the United States)")
                .lastModifiedTime(now.minus(Weeks.weeks(53)).getMillis())
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
            .lastModifiedTime(now.minus(Weeks.weeks(53)).getMillis())
            .partOf(EpubSubChapter.builder()
                .id("https://github.com/readium/readium-js-viewer/book/34843#epubcfi(/4/3)")
                .name("The Glorious Cause: The American Revolution, 1763-1789 (Oxford History of the United States)")
                .lastModifiedTime(now.minus(Weeks.weeks(53)).getMillis())
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
            .lastModifiedTime(now.minus(Weeks.weeks(22)).getMillis())
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
            .lastModifiedTime(now.minus(Weeks.weeks(22)).getMillis())
            .partOf((DigitalResource) EpubVolume.builder()
                .id("http://www.coursesmart.com/the-american-revolution-a-concise-history/robert-j-allison/dp/9780199347322")
                .name("The American Revolution: A Concise History | 978-0-19-531295-9")
                .lastModifiedTime(now.minus(Weeks.weeks(22)).getMillis())
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
                .lastModifiedTime(now.minus(Weeks.weeks(8)).getMillis())
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
            .lastModifiedTime(now.minus(Weeks.weeks(3)).getMillis())
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
                .lastModifiedTime(now.minus(Weeks.weeks(8)).getMillis())
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
                .lastModifiedTime(now.minus(Weeks.weeks(8)).getMillis())
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
}