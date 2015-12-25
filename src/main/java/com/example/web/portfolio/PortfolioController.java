/*
* Created at 10:17 on 25/12/15
*/
package com.example.web.portfolio;

import com.example.domain.portfolio.Portfolio;
import com.example.domain.portfolio.PortfolioPosition;
import com.example.domain.portfolio.Trade;
import com.example.service.portfolio.PortfolioService;
import com.example.service.portfolio.TradeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * @author zzhao
 */
@Slf4j
@RequiredArgsConstructor(onConstructor = @__({@Autowired}))
@Controller
public class PortfolioController {

    private final PortfolioService portfolioService;

    private final TradeService tradeService;

    @SubscribeMapping("/positions")
    public List<PortfolioPosition> listPositions(Principal user) {
        log.info("<listPositions> {}", user.getName());
        final Optional<Portfolio> portfolio = this.portfolioService.findPortfolio(user.getName());

        return portfolio.isPresent()
                ? portfolio.get().getPositions()
                : Collections.emptyList();
    }

    @SubscribeMapping("/all")
    public List<PortfolioPosition> listPositions() {
        log.info("<listPositions> all");
        return this.portfolioService.allPositions();
    }

    @MessageMapping("/trade")
    public void executeTrade(Trade trade, Principal principal) {
        trade.setUserName(principal.getName());
        log.info("<executeTrade> {}", trade);
        this.tradeService.executeTrade(trade);
    }

    @MessageExceptionHandler
    @SendToUser("/queue/errors")
    public String handleException(Throwable exception) {
        return exception.getMessage();
    }
}
