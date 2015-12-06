/*
* Created at 17:03 on 05/12/15
*/
package com.example.repo.template;

import com.example.domain.template.Section;
import com.example.domain.template.SectionType;
import com.example.domain.template.Template;
import com.example.repo.RepoBase;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author zzhao
 */
public interface SectionRepo extends RepoBase<Section, Long> {

    @Query("select s from Section s inner join s.templates t where t = :template")
    List<Section> findByTemplate(@Param("template") Template template);

    List<Section> findByType(SectionType type);
}
