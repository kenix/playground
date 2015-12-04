/*
* vwd KL
* Created by zzhao on 12/4/15 4:54 PM
*/
package com.example.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.example.domain.template.Template;
import lombok.Getter;

/**
 * @author zzhao
 */
@Entity
@Table(name = "mandator")
@Getter
public class Mandator {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String name;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "mandator",
            orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Template> templates = new ArrayList<>();

    public Mandator(String name) {
        this.name = name;
    }

    public void addTemplate(Template template) {
        this.templates.add(template);
    }
}
