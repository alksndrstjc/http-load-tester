package org.alksndrstjc.model;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class RequestedItemRuleMap {

    public static final Map<RequestedItem, Function<? super RequestStats, ? super Double>> ruleMap;

    static {
        ruleMap = new HashMap<>();
        ruleMap.put(RequestedItem.TOTAL_TIME, RequestStats::timeTaken);
        ruleMap.put(RequestedItem.TTFB, RequestStats::timeToFirstByte);
        ruleMap.put(RequestedItem.TTLB, RequestStats::timeToLastByte);
    }
}
