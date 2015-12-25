/*
* Created at 23:30 on 24/12/15
*/
package com.example.service.portfolio;

import com.example.domain.portfolio.PortfolioPosition;
import com.example.domain.portfolio.Trade;
import com.example.domain.portfolio.TradeType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeTypeUtils;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author zzhao
 */
@Service
@RequiredArgsConstructor(onConstructor = @__({@Autowired}))
@Slf4j
public class TradeService {

    public static final Map<String, Object> HEADERS = Collections.singletonMap(MessageHeaders.CONTENT_TYPE,
            MimeTypeUtils.APPLICATION_JSON);

    private final SimpMessageSendingOperations messagingTemplate;

    private final PortfolioService portfolioService;

    private final Set<TradeResult> tradeResults = Collections.newSetFromMap(new ConcurrentHashMap<>());

    public void executeTrade(Trade trade) {
        this.portfolioService.findPortfolio(trade.getUserName()).ifPresent(portfolio -> {
            final String ticker = trade.getTicker();
            final int sharesToTrade = trade.getShares();

            final Optional<PortfolioPosition> newPosition = (trade.getType() == TradeType.Buy)
                    ? portfolio.buy(ticker, trade.getPrice().doubleValue(), sharesToTrade)
                    : portfolio.sell(ticker, sharesToTrade);

            newPosition.ifPresent(pos -> this.tradeResults.add(new TradeResult(trade.getUserName(), pos)));

            if (!newPosition.isPresent()) {
                String payload = "Rejected trade " + trade;
                this.messagingTemplate.convertAndSendToUser(trade.getUserName(), "/queue/errors", payload);
            }
        });
    }

    @Scheduled(fixedDelay = 1500)
    public void sendTradeNotifications() {
        this.tradeResults.removeIf(result -> {
            if (System.currentTimeMillis() < (result.timestamp + 1500)) {
                return false;
            }
            log.info("<sendTradeNotifications> {}", result.position);
            this.messagingTemplate.convertAndSendToUser(result.user, "/queue/position-update",
                    result.position, HEADERS);
            return true;
        });
    }


    private static class TradeResult {

        private final String user;

        private final PortfolioPosition position;

        private final long timestamp;

        public TradeResult(String user, PortfolioPosition position) {
            this.user = user;
            this.position = position;
            this.timestamp = System.currentTimeMillis();
        }
    }
}
