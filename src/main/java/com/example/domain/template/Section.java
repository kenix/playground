/*
* vwd KL
* Created by zzhao on 11/4/15 4:46 PM
*/
package com.example.domain.template;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Getter;

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
