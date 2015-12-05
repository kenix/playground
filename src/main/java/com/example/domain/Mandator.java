/*
* vwd KL
* Created by zzhao on 12/4/15 4:54 PM
*/
package com.example.domain;

import com.example.domain.template.Template;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author zzhao
 */
@Entity
@Table(name = "mandator",
        indexes = {@Index(name = "idx_mandator_zone", columnList = "zone", unique = true)}
)
@NoArgsConstructor
@Getter
@ToString(of = {"id", "zone", "name"})
@EqualsAndHashCode(of = {"id"})
public class Mandator {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String zone;

    @Setter
    private String name;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "mandator",
            orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Template> templates = new ArrayList<>();

    public Mandator(String zone, String name) {
        this.zone = zone;
        this.name = name;
    }

    public void addTemplate(Template template) {
        this.templates.add(template);
    }
}
