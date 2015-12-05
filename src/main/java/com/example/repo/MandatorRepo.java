/*
* Created at 16:22 on 05/12/15
*/
package com.example.repo;

import com.example.domain.Mandator;

/**
 * @author zzhao
 */
public interface MandatorRepo extends RepoBase<Mandator, Long> {

    Mandator findByZone(String zone);
}
