/*
* Created at 20:05 on 20/11/15
*/
package com.example.domain;

import lombok.Getter;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.List;

/**
 * @author zzhao
 */
@Entity
@Getter
public class Planet {

    @Id
    private String name;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Person> people;
}
