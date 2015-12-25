/*
* Created at 23:17 on 24/12/15
*/
package com.example.service.portfolio;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @author zzhao
 */
@Service
public class StaticDataService {

    private static final Map<String, String> ticker2Company = new HashMap<>();

    static {
        ticker2Company.put("CTXS", "Citrix Systems, Inc.");
        ticker2Company.put("DELL", "Dell Inc.");
        ticker2Company.put("MSFT", "Microsoft Inc.");
        ticker2Company.put("ORCL", "Oracle Inc.");
        ticker2Company.put("EMC Corporation", "EMC");
        ticker2Company.put("Alphabet Inc.", "GOOG");
        ticker2Company.put("VMware, Inc.", "VMW");
        ticker2Company.put("Red Hat", "RHT");
    }

    public Optional<String> getCompanyName(String ticker) {
        return Optional.ofNullable(ticker2Company.get(ticker));
    }
}
