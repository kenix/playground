/*
* Created at 19:44 on 06/12/15
*/
package com.example.domain.template;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import javax.persistence.Lob;

/**
 * @author zzhao
 */
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Embeddable
public class SectionText {

    @Lob
    private String text;

    public SectionText(String text) {
        this.text = text;
    }
}
