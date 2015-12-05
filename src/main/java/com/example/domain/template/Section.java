/*
* vwd KL
* Created by zzhao on 11/4/15 4:46 PM
*/
package com.example.domain.template;

import lombok.Getter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author zzhao
 */
@Entity
@Table(name = "section")
@Getter
public class Section {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @ManyToMany(mappedBy = "sections")
    private List<Template> templates = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private SectionType type;

    private String docSectionIds;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Version> versions = new ArrayList<>();

    public Section(String docSectionIds) {
        this.docSectionIds = docSectionIds;
    }

    public List<String> getDocSectionIds() {
        return Arrays.asList(this.docSectionIds.split(","));
    }
}
