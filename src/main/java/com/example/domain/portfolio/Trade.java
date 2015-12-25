/*
* Created at 22:23 on 24/12/15
*/
package com.example.domain.portfolio;

import lombok.*;

import java.math.BigDecimal;

/**
 * @author zzhao
 */
@ToString
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
@Getter
public class Trade {

    private final String ticker;

    private final TradeType type;

    private final BigDecimal price;

    private final int shares;

    @Setter
    private String userName;
}
