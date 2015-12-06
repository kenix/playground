/*
* vwd KL
* Created by zzhao on 11/4/15 4:46 PM
*/
package com.example.domain.template;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author zzhao
 */
@Entity
@Table(name = "section")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = {"id"})
@ToString(of = {"id", "type", "docSectionIds"})
public class Section implements Comparable<Section> {

    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @ManyToMany(
            cascade = {CascadeType.PERSIST, CascadeType.MERGE},
            mappedBy = "sections",
            fetch = FetchType.LAZY
    )
    private List<Template> templates = new ArrayList<>(5);

    @Getter
    @Enumerated(EnumType.STRING)
    private SectionType type;

    private String docSectionIds;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "section_version",
            joinColumns = @JoinColumn(name = "section_id"),
            inverseJoinColumns = @JoinColumn(name = "version_id")
    )
    @OrderBy("version_id DESC")
    private List<Version> versions = new ArrayList<>(5);

    public Section(SectionType type, String docSectionIds) {
        this.type = type;
        this.docSectionIds = docSectionIds;
    }

    void associateTemplate(Template template) {
        this.templates.add(template);
    }

    public List<Template> getTemplates() {
        return Collections.unmodifiableList(this.templates);
    }

    public List<Version> getVersions() {
        return Collections.unmodifiableList(this.versions);
    }

    public void commitVersion(Version version) {
        this.versions.add(version);
    }

    public List<String> getDocSectionIds() {
        return Arrays.asList(this.docSectionIds.split(","));
    }

    @Override
    public int compareTo(Section o) {
        return Integer.compare(this.type.getOrder(), o.type.getOrder());
    }
}
