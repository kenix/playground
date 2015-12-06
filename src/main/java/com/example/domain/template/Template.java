/*
* vwd KL
* Created by zzhao on 11/4/15 4:36 PM
*/
package com.example.domain.template;

import com.example.domain.Mandator;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author zzhao
 */
@Entity
@Table(name = "template",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_mandator_template", columnNames = {"mandator_id", "name"})
        }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(of = {"id", "name", "mandator"})
@EqualsAndHashCode(of = {"id"})
@Slf4j
public class Template {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @ManyToOne(optional = false, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "mandator_id")
    private Mandator mandator;

    private String name;

    @Getter(AccessLevel.NONE)
    @ManyToMany(
            cascade = {CascadeType.PERSIST, CascadeType.MERGE},
            fetch = FetchType.LAZY
    )
    @JoinTable(
            name = "template_section",
            joinColumns = @JoinColumn(name = "template_id"),
            inverseJoinColumns = @JoinColumn(name = "section_id")
    )
    private List<Section> sections = new ArrayList<>();

    public Template(Mandator mandator, String name) {
        this.mandator = mandator;
        this.name = name;
    }

    public List<Section> getSections() {
        return Collections.unmodifiableList(this.sections);
    }

    public void addSection(Section section) {
        checkSection(section);
        this.sections.add(section);
        section.associateTemplate(this);
    }

    private void checkSection(Section section) {
        ensureUniqueType(section);
        ensureSameMandator(section);
    }

    private void ensureSameMandator(Section section) {
        final List<Template> templates = section.getTemplates();
        if (!templates.isEmpty() && !templates.get(0).getMandator().equals(this.mandator)) {
            throw new ConstraintViolationException(
                    "Section from mandator: " + templates.get(0).getMandator().getZone()
                            + " cannot be shared with mandator: " + this.mandator.getZone(),
                    Collections.emptySet());
        }
    }

    private void ensureUniqueType(Section section) {
        final boolean typeExists = this.sections
                .stream()
                .filter(s -> s.getType() == section.getType())
                .findAny()
                .isPresent();
        if (typeExists) {
            throw new ConstraintViolationException("Section with type: " + section.getType() + " already exists",
                    Collections.emptySet());
        }
    }
}
