/*
* vwd KL
* Created by zzhao on 11/4/15 4:36 PM
*/
package com.example.domain.template;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.example.domain.Mandator;
import lombok.Getter;

/**
 * @author zzhao
 */
@Entity
@Table(name = "template")
@Getter
public class Template {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "mandator", referencedColumnName = "id")
    private Mandator mandator;

    private String name;

    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable(name = "template_section",
            joinColumns = @JoinColumn(name = "template_id"),
            inverseJoinColumns = @JoinColumn(name = "section_id"))
    private List<Section> sections = new ArrayList<>();

    public Template(Mandator mandator, String name) {
        this.mandator = mandator;
        this.name = name;
    }

    public void addSection(Section section) {
        this.sections.add(section);
    }
}
