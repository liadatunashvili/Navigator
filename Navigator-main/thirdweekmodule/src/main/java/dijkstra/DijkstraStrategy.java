package dijkstra;

import model.Point;
import model.Route;
import view.RouteUtils;
import view.SparseGraphBuilder;

import java.util.*;

public class DijkstraStrategy implements RouteStrategy {

    private static final int K = 7;
    @Override
    public Route buildRoute(List<Point> points, int startIndex) {
        if (points.isEmpty()) throw new IllegalArgumentException("Points list is empty");

        Map<Long, List<long[]>> graph = SparseGraphBuilder.build(points, K);
        Map<Long, Point> byId = new HashMap<>();
        for (Point p : points) byId.put(p.getId(), p);

        Set<Long> unvisited = new LinkedHashSet<>();
        for (Point p : points) unvisited.add(p.getId());

        List<Point> path = new ArrayList<>();
        Point current = points.get(startIndex);
        path.add(current);
        unvisited.remove(current.getId());

        while (!unvisited.isEmpty()) {
            Point next = dijkstraCheapest(current.getId(), unvisited, graph, byId);
            path.add(next);
            unvisited.remove(next.getId());
            current = next;
        }

        Route route = new Route();
        route.setPoints(path);
        route.setTotalTime(RouteUtils.totalDistance(path));
        return route;
    }

    private Point dijkstraCheapest(Long srcId, Set<Long> unvisited,
                                   Map<Long, List<long[]>> graph,
                                   Map<Long, Point> byId) {
        Map<Long, Double> dist = new HashMap<>();
        PriorityQueue<Long> pq = new PriorityQueue<>(
                Comparator.comparingDouble(id -> dist.getOrDefault(id, Double.MAX_VALUE)));

        dist.put(srcId, 0.0);
        pq.add(srcId);

        while (!pq.isEmpty()) {
            Long u = pq.poll();
            double uDist = dist.getOrDefault(u, Double.MAX_VALUE);

            for (long[] edge : graph.getOrDefault(u, List.of())) {
                Long v = edge[0];
                double w = Double.longBitsToDouble(edge[1]);
                double alt = uDist + w;
                if (alt < dist.getOrDefault(v, Double.MAX_VALUE)) {
                    dist.put(v, alt);
                    pq.remove(v);
                    pq.add(v);
                }
            }
        }

        Point best = null;
        double bestDist = Double.MAX_VALUE;
        for (Long id : unvisited) {
            double d = dist.getOrDefault(id, Double.MAX_VALUE);
            if (d == Double.MAX_VALUE) {
                d = RouteUtils.euclidean(byId.get(srcId), byId.get(id));
            }
            if (d < bestDist) {
                bestDist = d;
                best = byId.get(id);
            }
        }
        return Objects.requireNonNull(best);
    }
}