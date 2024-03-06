package org.alksndrstjc.model;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

import static org.alksndrstjc.model.RequestedItemRuleMap.ruleMap;

public class RequestStatsThreadSafe {

    private static final Map<String, List<RequestStats>> perRequestStats = new ConcurrentHashMap<>();

    public static void addStats(String request, RequestStats stats) {
        perRequestStats.computeIfAbsent(request, k -> Collections.synchronizedList(new ArrayList<>()));
        List<RequestStats> requestStats = perRequestStats.get(request);
        synchronized (requestStats) {
            requestStats.add(stats);
        }
    }

    public static double getRequestedItemMin(String request, RequestedItem requestedItem) {
        Function<? super RequestStats, ? super Double> function = ruleMap.get(requestedItem);
        return perRequestStats.get(request)
                .stream()
                .map(function)
                .mapToDouble(d -> (double) d)
                .min()
                .getAsDouble();
    }

    public static double getRequestedItemMax(String request, RequestedItem requestedItem) {
        Function<? super RequestStats, ? super Double> function = ruleMap.get(requestedItem);
        return perRequestStats.get(request)
                .stream()
                .map(function)
                .mapToDouble(d -> (double) d)
                .max()
                .getAsDouble();
    }

    public static double getRequestedItemAvrg(String request, RequestedItem requestedItem) {
        Function<? super RequestStats, ? super Double> function = ruleMap.get(requestedItem);
        return perRequestStats.get(request)
                .stream()
                .map(function)
                .mapToDouble(d -> (double) d)
                .average()
                .getAsDouble();
    }
}
