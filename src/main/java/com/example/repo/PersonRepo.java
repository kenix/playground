/*
* Created at 20:22 on 20/11/15
*/
package com.example.repo;

import com.example.domain.Person;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author zzhao
 */
public interface PersonRepo extends JpaRepository<Person, Long> {
}
