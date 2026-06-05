package dijkstra;

import model.Point;
import model.Route;

import java.util.*;
import java.util.stream.Collectors;

public class DijkstraStrategy implements RouteStrategy {

    @Override
    public Route buildRoute(List<Point> points, int startIndex) {
        Map<Long, Point> byId = points.stream()
                .collect(Collectors.toMap(Point::getId, p -> p));

        List<Point> unvisited = new ArrayList<>(points);
        List<Point> path = new ArrayList<>();

        Point current = unvisited.remove(startIndex);
        path.add(current);

        while (!unvisited.isEmpty()) {
            Point next = dijkstraNearest(current, unvisited, byId);
            unvisited.remove(next);
            path.add(next);
            current = next;
        }

        Route route = new Route();
        route.setPoints(path);
        route.setTotalTime(calculateTotalTime(path));
        return route;
    }

    private Point dijkstraNearest(Point src, List<Point> candidates,
                                  Map<Long, Point> byId) {
        Map<Long, Double> dist = new HashMap<>();
        PriorityQueue<Long> pq = new PriorityQueue<>(
                Comparator.comparingDouble(dist::get));

        for (Point p : candidates) dist.put(p.getId(), Double.MAX_VALUE);
        dist.put(src.getId(), 0.0);
        pq.add(src.getId());

        while (!pq.isEmpty()) {
            Long u = pq.poll();
            Point uPoint = byId.get(u);

            for (Point neighbour : candidates) {
                if (neighbour.getId().equals(u)) continue;
                double alt = dist.get(u) + euclidean(uPoint, neighbour);
                if (alt < dist.getOrDefault(neighbour.getId(), Double.MAX_VALUE)) {
                    dist.put(neighbour.getId(), alt);
                    pq.remove(neighbour.getId());
                    pq.add(neighbour.getId());
                }
            }
        }

        return candidates.stream()
                .min(Comparator.comparingDouble(p -> dist.get(p.getId())))
                .orElseThrow();
    }

    private double calculateTotalTime(List<Point> path) {
        double total = 0;
        for (int i = 0; i < path.size() - 1; i++) {
            total += euclidean(path.get(i), path.get(i + 1));
        }
        return total;
    }

    private double euclidean(Point a, Point b) {
        return Math.sqrt(Math.pow(a.getX() - b.getX(), 2)
                + Math.pow(a.getY() - b.getY(), 2));
    }
}