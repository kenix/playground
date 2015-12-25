/*
* Created at 23:14 on 24/12/15
*/
package com.example.service.portfolio;

import com.example.domain.portfolio.Portfolio;
import com.example.domain.portfolio.PortfolioPosition;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author zzhao
 */
@Service
public class PortfolioService {

    private final Map<String, Portfolio> portfolioByUserName = new HashMap<>();

    public PortfolioService() {
        Portfolio portfolio = new Portfolio();
        portfolio.buy("CTXS", 24.30, 75);
        portfolio.buy("DELL", 13.44, 50);
        portfolio.buy("MSFT", 34.15, 33);
        portfolio.buy("ORCL", 31.22, 45);
        this.portfolioByUserName.put("fabrice", portfolio);

        portfolio = new Portfolio();
        portfolio.buy("EMC", 24.30, 75);
        portfolio.buy("GOOG", 905.09, 5);
        portfolio.buy("VMW", 65.58, 23);
        portfolio.buy("RHT", 48.30, 15);
        this.portfolioByUserName.put("paulson", portfolio);
    }

    public List<PortfolioPosition> allPositions() {
        return this.portfolioByUserName.values()
                .stream()
                .map(Portfolio::getPositions)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    public Optional<Portfolio> findPortfolio(String username) {
        return Optional.ofNullable(this.portfolioByUserName.get(username));
    }
}
