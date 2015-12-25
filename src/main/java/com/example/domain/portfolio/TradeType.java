package com.example.domain.portfolio;

import java.math.BigDecimal;

/**
 * @author zzhao
 */
public enum TradeType {
    Buy,
    Sell;

    public Trade of(String ticker, BigDecimal price, int shares, String userName) {
        return new Trade(ticker, this, price, shares);
    }
}
