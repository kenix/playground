/*
* Created at 22:33 on 24/12/15
*/
package com.example.service.portfolio;

import com.example.domain.portfolio.Quote;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.core.MessageSendingOperations;
import org.springframework.messaging.simp.broker.BrokerAvailabilityEvent;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.MathContext;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * @author zzhao
 */
@Service
@RequiredArgsConstructor(onConstructor = @__({@Autowired}))
@Slf4j
public class QuoteService implements ApplicationListener<BrokerAvailabilityEvent> {

    private final AtomicBoolean brokerAvailable = new AtomicBoolean();

    private final StockQuoteGenerator quoteGenerator = new StockQuoteGenerator();

    private final MessageSendingOperations<String> messagingTemplate;

    @Override
    public void onApplicationEvent(BrokerAvailabilityEvent event) {
        this.brokerAvailable.set(event.isBrokerAvailable());
    }

    @Scheduled(fixedDelay = 2000)
    public void sendQuotes() {
        if (log.isDebugEnabled()) {
            log.debug("<sendQuotes> ");
        }
        this.quoteGenerator
                .generateQuotes()
                .peek(quote -> {
                    if (log.isDebugEnabled()) {
                        log.debug("<sendQuotes> {}", quote);
                    }

                })
                .forEach(quote -> {
                    if (this.brokerAvailable.get()) {
                        this.messagingTemplate.convertAndSend("/topic/price.stock." + quote.getTicker(), quote);
                    }
                });
    }

    private static final class StockQuoteGenerator {
        private static final MathContext MC = new MathContext(2);

        public static final BigDecimal MARGIN = new BigDecimal(0.02);

        private static final List<Quote> QUOTES = Arrays.asList(
                new Quote("CTXS", new BigDecimal("24.30")),
                new Quote("DELL", new BigDecimal("13.03")),
                new Quote("EMC", new BigDecimal("24.13")),
                new Quote("GOOG", new BigDecimal("893.49")),
                new Quote("MSFT", new BigDecimal("34.21")),
                new Quote("ORCL", new BigDecimal("31.22")),
                new Quote("RHT", new BigDecimal("48.30")),
                new Quote("VMW", new BigDecimal("66.98"))
        );

        private final SecureRandom random = new SecureRandom(String.valueOf(System.currentTimeMillis()).getBytes());


        public Stream<Quote> generateQuotes() {
            return IntStream
                    .rangeClosed(1, QUOTES.size())
                    .map(i -> this.random.nextInt(QUOTES.size()))
                    .distinct()
                    .mapToObj(i -> {
                        final Quote quote = QUOTES.get(i);
                        return quote.withPrice(nextPrice(quote.getPrice()));
                    });
        }

        private BigDecimal nextPrice(BigDecimal seedPrice) {
            final double range = seedPrice.multiply(MARGIN).doubleValue();
            final BigDecimal priceChange = new BigDecimal(String.valueOf(this.random.nextDouble() * range), MC);
            return this.random.nextBoolean() ? seedPrice.add(priceChange) : seedPrice.subtract(priceChange);
        }
    }
}
