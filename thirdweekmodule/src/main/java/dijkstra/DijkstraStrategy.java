package dijkstra;

import model.Point;
import model.Route;
import util.DistanceUtil;

import java.util.*;

/**
 * Builds a route by repeatedly choosing the next unvisited point with the
 * shortest Dijkstra path from the current position on the complete graph of
 * remaining points.
 */
public class DijkstraStrategy implements RouteStrategy {

    @Override
    public Route buildRoute(List<Point> points, int startIndex) {
        validateInput(points, startIndex);

        List<Point> unvisited = new ArrayList<>(points);
        List<Point> path = new ArrayList<>();

        Point current = unvisited.remove(startIndex);
        path.add(current);

        while (!unvisited.isEmpty()) {
            Point next = findNextByDijkstra(current, unvisited);
            unvisited.remove(next);
            path.add(next);
            current = next;
        }

        Route route = new Route();
        route.setPoints(path);
        route.setTotalTime(DistanceUtil.totalPathDistance(path));
        return route;
    }

    private Point findNextByDijkstra(Point source, List<Point> unvisited) {
        Map<Long, Double> distances = new HashMap<>();
        PriorityQueue<Point> queue = new PriorityQueue<>(
                Comparator.comparingDouble(p -> distances.getOrDefault(p.getId(), Double.MAX_VALUE)));

        for (Point point : unvisited) {
            distances.put(point.getId(), Double.MAX_VALUE);
            queue.add(point);
        }

        for (Point candidate : unvisited) {
            double direct = DistanceUtil.euclidean(source, candidate);
            if (direct < distances.get(candidate.getId())) {
                distances.put(candidate.getId(), direct);
            }
        }

        queue.clear();
        queue.addAll(unvisited);

        while (!queue.isEmpty()) {
            Point current = queue.poll();
            double currentDistance = distances.get(current.getId());

            for (Point neighbour : unvisited) {
                if (neighbour.getId().equals(current.getId())) {
                    continue;
                }

                double alternative = currentDistance + DistanceUtil.euclidean(current, neighbour);
                if (alternative < distances.get(neighbour.getId())) {
                    distances.put(neighbour.getId(), alternative);
                    queue.remove(neighbour);
                    queue.add(neighbour);
                }
            }
        }

        return unvisited.stream()
                .min(Comparator.comparingDouble(p -> distances.get(p.getId())))
                .orElseThrow();
    }

    private void validateInput(List<Point> points, int startIndex) {
        if (points == null || points.isEmpty()) {
            throw new IllegalArgumentException("At least one point is required to build a route");
        }
        if (startIndex < 0 || startIndex >= points.size()) {
            throw new IllegalArgumentException("Start index is out of bounds: " + startIndex);
        }
    }
}
