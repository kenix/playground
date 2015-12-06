/*
* vwd KL
* Created by zzhao on 12/4/15 4:54 PM
*/
package com.example.domain;

import com.example.domain.template.Template;
import com.google.common.collect.ImmutableList;
import lombok.*;

import javax.persistence.*;
import java.util.List;

/**
 * @author zzhao
 */
@Entity
@Table(name = "mandator",
        indexes = {@Index(name = "idx_mandator_zone", columnList = "zone", unique = true)}
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

    @OneToMany(
            mappedBy = "mandator",
            fetch = FetchType.LAZY
    )
    private List<Template> templates = ImmutableList.of();

    public Mandator(String zone, String name) {
        this.zone = zone;
        this.name = name;
    }
}
