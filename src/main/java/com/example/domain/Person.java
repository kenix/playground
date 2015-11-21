/*
* Created at 20:04 on 20/11/15
*/
package com.example.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * @author zzhao
 */
@Entity
@Getter
@Setter
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String firstName;

    private String lastName;

    @ManyToOne(cascade = CascadeType.PERSIST)
    private Planet planet;
}
