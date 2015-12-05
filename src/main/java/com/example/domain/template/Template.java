/*
* vwd KL
* Created by zzhao on 11/4/15 4:36 PM
*/
package com.example.domain.template;

import com.example.domain.Mandator;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.ConstraintViolationException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author zzhao
 */
@Entity
@Table(name = "template",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_mandator_template", columnNames = {"mandator_id", "name"})
        }
)
@NoArgsConstructor
@Getter
@ToString(of = {"id", "mandator", "name"})
@EqualsAndHashCode(of = {"id"})
public class Template {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "mandator_id", referencedColumnName = "id")
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

    @PrePersist
    @PreUpdate
    private void checkSections() {
        final List<SectionType> dupTypes = this.sections
                .stream()
                .collect(Collectors
                        .groupingBy(Section::getType, () -> new EnumMap<>(SectionType.class), Collectors.counting()))
                .entrySet()
                .stream()
                .filter(e -> e.getValue() > 1)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
        if (!dupTypes.isEmpty()) {
            throw new ConstraintViolationException("Multiple sections with same type: " + dupTypes,
                    Collections.emptySet());
        }

        final List<String> mandators = this.sections
                .stream()
                .flatMap(s -> s.getTemplates().stream().map(t -> t.getMandator().getName()))
                .distinct()
                .collect(Collectors.toList());
        if (mandators.size() > 1) {
            throw new ConstraintViolationException("Sharing sections among mandators: " + mandators,
                    Collections.emptySet());
        }
    }
}
