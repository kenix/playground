/*
* Created at 21:33 on 24/12/15
*/
package com.example.domain.portfolio;

import com.google.common.collect.ImmutableList;
import org.springframework.util.Assert;

import javax.annotation.concurrent.NotThreadSafe;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author zzhao
 */
@NotThreadSafe
public class Portfolio {

    private final Map<String, PortfolioPosition> byTicker = new LinkedHashMap<>();

    public List<PortfolioPosition> getPositions() {
        return ImmutableList.copyOf(this.byTicker.values());
    }

    public Optional<PortfolioPosition> getPosition(String ticker) {
        return Optional.ofNullable(this.byTicker.get(ticker));
    }

    public Optional<PortfolioPosition> buy(String ticker, double price, int shares2Buy) {
        Assert.isTrue(shares2Buy > 1, "invalid shares to buy");

        final PortfolioPosition portfolioPosition = this.byTicker.containsKey(ticker)
                ? this.byTicker.get(ticker).buy(shares2Buy, price)
                : new PortfolioPosition(ticker, price, shares2Buy);

        this.byTicker.put(ticker, portfolioPosition);
        return Optional.of(portfolioPosition);
    }

    public Optional<PortfolioPosition> sell(String ticker, int shares2Sell) {
        if (!this.byTicker.containsKey(ticker) || shares2Sell < 1) {
            return Optional.empty();
        }

        return this.byTicker.get(ticker).sell(shares2Sell);
    }
}
