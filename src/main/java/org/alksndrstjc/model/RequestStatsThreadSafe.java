package org.alksndrstjc.model;

import java.util.*;
import java.util.function.Function;

public class RequestStatsThreadSafe {

    private static final Map<RequestedItem, Function<? super RequestStats, ? super Double>> ruleMap;

    static {
        ruleMap = new HashMap<>();
        ruleMap.put(RequestedItem.TOTAL_TIME, RequestStats::timeTaken);
        ruleMap.put(RequestedItem.TTFB, RequestStats::timeToFirstByte);
        ruleMap.put(RequestedItem.TTLB, RequestStats::timeToLastByte);
    }

    private static final List<RequestStats> stats = Collections.synchronizedList(new ArrayList<>());

    public static void addStats(RequestStats statsModel) {
        synchronized (stats) {
            stats.add(statsModel);
        }
    }

    public static OptionalDouble getRequestedItemMin(RequestedItem requestedItem) {
        Function<? super RequestStats, ? super Double> function = ruleMap.get(requestedItem);
        return stats.stream()
                .map(function)
                .mapToDouble(d -> (double) d)
                .min();
    }

    public static OptionalDouble getRequestedItemMax(RequestedItem requestedItem) {
        Function<? super RequestStats, ? super Double> function = ruleMap.get(requestedItem);
        return stats.stream()
                .map(function)
                .mapToDouble(d -> (double) d)
                .max();
    }

    public static OptionalDouble getRequestedItemAvrg(RequestedItem requestedItem) {
        Function<? super RequestStats, ? super Double> function = ruleMap.get(requestedItem);
        return stats.stream()
                .map(function)
                .mapToDouble(d -> (double) d)
                .average();
    }
}
