/*
* Created at 23:21 on 05/12/15
*/
package com.example.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.Repository;

import java.io.Serializable;

/**
 * @author zzhao
 */
@NoRepositoryBean
public interface RepoBase<T, ID extends Serializable> extends Repository<T, ID> {

    <S extends T> S save(S entity);

    <S extends T> S saveAndFlush(S entity);

    T findOne(ID id);

    boolean exists(ID id);

    long count();

    void flush();

    Page<T> findAll(Pageable pageable);
}
