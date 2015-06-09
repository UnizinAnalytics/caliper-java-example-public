package org.imsglobal.caliper.test;

import org.imsglobal.caliper.entities.agent.Person;
import org.imsglobal.caliper.entities.lis.CourseSection;
import org.imsglobal.caliper.entities.lis.Membership;
import org.imsglobal.caliper.entities.lis.Role;
import org.imsglobal.caliper.entities.lis.Status;
import org.imsglobal.caliper.entities.w3c.Organization;

public class SampleLISEntities {

    /**
     * Sample Amm-Rev 101 Course section.
     * @return course section
     */
    public static final CourseSection buildAmRev101CourseSection() {
        return CourseSection.builder()
            .id("https://example.edu/politicalScience/2014/american-revolution-101")
            .academicSession("Spring-2014")
            .courseNumber("AmRev-101")
            .name("American Revolution 101")
            .dateCreated(SampleTime.getDefaultDateCreated())
            .dateModified(SampleTime.getDefaultDateModified())
            .build();
    }

    /**
     * Build sample Course Section membership.
     * @return membership
     */
    public static final Membership buildAmRev101Membership(Person member, Organization organization, Role role, Status status) {
        return Membership.builder()
            .id("https://example.edu/politicalScience/2015/american-revolution-101/roster/554433")
            .name("American Revolution 101")
            .description("Roster entry")
            .member(member)
            .organization(organization)
            .role(role)
            .status(status)
            .dateCreated(SampleTime.getDefaultDateCreated())
            .build();
    }
}
