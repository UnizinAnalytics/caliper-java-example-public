package org.imsglobal.caliper.test;

import org.imsglobal.caliper.entities.agent.SoftwareApplication;
import org.imsglobal.caliper.entities.reading.EpubPart;
import org.imsglobal.caliper.entities.reading.EpubSubChapter;
import org.imsglobal.caliper.entities.reading.EpubVolume;
import org.imsglobal.caliper.entities.reading.WebPage;

public class SampleReadingEntities {

    /**
     * Sample EPUB volume.
     * @return digital resource
     */
    public static final EpubVolume buildEpubAllisonAmRevVolume() {
        return EpubVolume.builder()
            .id("http://www.coursesmart.com/the-american-revolution-a-concise-history/robert-j-allison/dp/9780199347322")
            .name("The American Revolution: A Concise History | 978-0-19-531295-9")
            .version("1st ed.")
            .dateCreated(SampleTime.getDefaultDateCreated())
            .dateModified(SampleTime.getDefaultDateModified())
            .build();
    }

    /**
     * Sample EPUB sub chapter.
     * @return digital resource
     */
    public static final EpubSubChapter buildEpubAllisonAmRevSubChapter() {
        return EpubSubChapter.builder()
            .id("http://www.coursesmart.com/the-american-revolution-a-concise-history/robert-j-allison/dp/9780199347322/aXfsadf12")
            .name("The Boston Tea Party")
            .dateCreated(SampleTime.getDefaultDateCreated())
            .dateModified(SampleTime.getDefaultDateModified())
            .isPartOf(buildEpubAllisonAmRevVolume())
            .version(buildEpubAllisonAmRevVolume().getVersion())
            .build();
    }


    /**
     * Sample EPUB Volume.
     * @return digital resource
     */
    public static final EpubVolume buildEpubGloriousCauseVolume() {
        return EpubVolume.builder()
            .id("https://example.com/viewer/book/34843")
            .name("The Glorious Cause: The American Revolution, 1763-1789 (Oxford History of the United States)")
            .version("1st ed.")
            .dateCreated(SampleTime.getDefaultDateCreated())
            .dateModified(SampleTime.getDefaultDateModified())
            .build();
    }

    /**
     * Sample EPUB fragment.
     * @return digital resource
     */
    public static final EpubSubChapter buildEpubGloriousCauseSubChap43() {
        return EpubSubChapter.builder()
            .id("https://example.com/viewer/book/34843#epubcfi(/4/3)")
            .name("Key Figures")
            .isPartOf(buildEpubGloriousCauseVolume())
            .version(buildEpubGloriousCauseVolume().getVersion())
            .dateCreated(buildEpubGloriousCauseVolume().getDateCreated())
            .dateModified(buildEpubGloriousCauseVolume().getDateModified())
            .build();
    }

    /**
     * Sample EPUB fragment.
     * @return digital resource
     */
    public static final EpubPart buildEpubGloriousCausePart431() {
        return EpubPart.builder()
            .id("https://example.com/viewer/book/34843#epubcfi(/4/3/1)")
            .name("Key Figures: George Washington")
            .isPartOf(buildEpubGloriousCauseSubChap43())
            .version(buildEpubGloriousCauseSubChap43().getVersion())
            .dateCreated(buildEpubGloriousCauseSubChap43().getDateCreated())
            .dateModified(buildEpubGloriousCauseSubChap43().getDateModified())
            .build();
    }

    /**
     * Sample EPUB fragment.
     * @return digital resource
     */
    public static final EpubPart buildEpubGloriousCausePart432() {
        return EpubPart.builder()
            .id("https://example.com/viewer/book/34843#epubcfi(/4/3/2)")
            .name("Key Figures: Lord North")
            .isPartOf(buildEpubGloriousCauseSubChap43())
            .version(buildEpubGloriousCauseSubChap43().getVersion())
            .dateCreated(buildEpubGloriousCauseSubChap43().getDateCreated())
            .dateModified(buildEpubGloriousCauseSubChap43().getDateModified())
            .build();
    }

    /**
     * Sample EPUB fragment.
     * @return digital resource
     */
    public static final EpubPart buildEpubGloriousCausePart433() {
        return EpubPart.builder()
            .id("https://example.com/viewer/book/34843#epubcfi(/4/3/3)")
            .name("Key Figures: John Adams")
            .isPartOf(buildEpubGloriousCauseSubChap43())
            .version(buildEpubGloriousCauseSubChap43().getVersion())
            .dateCreated(buildEpubGloriousCauseSubChap43().getDateCreated())
            .dateModified(buildEpubGloriousCauseSubChap43().getDateModified())
            .build();
    }

    /**
     * Sample Am-Rev 101 landing page.
     * @return web page
     */
    public static final WebPage buildAmRev101LandingPage() {
        return WebPage.builder()
            .id("https://example.edu/politicalScience/2014/american-revolution-101/index.html")
            .name("American Revolution 101 Landing Page")
            .isPartOf(SoftwareApplication.builder()
                .id("https://example.com/lms")
                .dateCreated(SampleTime.getDefaultDateCreated())
                .dateModified(SampleTime.getDefaultDateModified())
                .build())
            .dateCreated(SampleTime.getDefaultDateCreated())
            .build();
    }
}