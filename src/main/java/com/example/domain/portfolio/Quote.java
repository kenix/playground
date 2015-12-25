/*
* Created at 22:21 on 24/12/15
*/
package com.example.domain.portfolio;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;

/**
 * @author zzhao
 */
@RequiredArgsConstructor
@Getter
@ToString
public class Quote {

    private final String ticker;

    private final BigDecimal price;

    public Quote withPrice(BigDecimal price) {
        return new Quote(this.ticker, price);
    }
}
