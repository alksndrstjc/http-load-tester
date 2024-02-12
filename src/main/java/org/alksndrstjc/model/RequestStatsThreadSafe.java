package org.alksndrstjc.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RequestStatsThreadSafe {

    private static final List<RequestStats> stats = Collections.synchronizedList(new ArrayList<>());

    public static void addStats(RequestStats statsModel) {
        synchronized (stats) {
            stats.add(statsModel);
        }
    }
}
