/*
* Created at 21:17 on 24/12/15
*/
package com.example.domain.portfolio;

import lombok.Getter;
import lombok.ToString;
import org.springframework.util.Assert;

import java.util.Optional;

/**
 * @author zzhao
 */
@Getter
@ToString
public class PortfolioPosition {

    private final String ticker;

    private double price;

    private int shares;

    private long updateTime;

    public PortfolioPosition(String ticker, double price, int shares) {
        this.ticker = ticker;
        this.price = price;
        this.shares = shares;
        this.updateTime = System.currentTimeMillis();
    }

    public PortfolioPosition buy(int shares2Buy, double price) {
        Assert.isTrue(shares2Buy > 1, "invalid shares to buy");
        final double newPrice = (this.price * this.shares + shares2Buy * price) / (this.shares + shares2Buy);
        return new PortfolioPosition(this.ticker, newPrice, this.shares + shares2Buy);
    }

    public Optional<PortfolioPosition> sell(int shares2Sell) {
        Assert.isTrue(shares2Sell <= this.shares, "invalid shares to sell");
        return this.shares == shares2Sell
                ? Optional.empty()
                : Optional.of(new PortfolioPosition(this.ticker, this.price, this.shares - shares2Sell));
    }
}
