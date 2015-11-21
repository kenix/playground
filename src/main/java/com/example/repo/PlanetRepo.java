/*
* Created at 20:23 on 20/11/15
*/
package com.example.repo;

import com.example.domain.Planet;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author zzhao
 */
public interface PlanetRepo extends JpaRepository<Planet, String> {
}
