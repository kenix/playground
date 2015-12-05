/*
* Created at 16:59 on 05/12/15
*/
package com.example.repo.template;

import com.example.domain.Mandator;
import com.example.domain.template.Template;
import com.example.repo.RepoBase;

/**
 * @author zzhao
 */
public interface TemplateRepo extends RepoBase<Template, Long> {

    Template findByMandatorAndName(Mandator mandator, String name);
}
