package dijkstra;

import model.Point;
import model.Route;
import util.DistanceUtil;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Builds a route by always visiting the closest unvisited point next.
 */
public class NearestNeighbourStrategy implements RouteStrategy {

    @Override
    public Route buildRoute(List<Point> points, int startIndex) {
        if (points == null || points.isEmpty()) {
            throw new IllegalArgumentException("At least one point is required to build a route");
        }
        if (startIndex < 0 || startIndex >= points.size()) {
            throw new IllegalArgumentException("Start index is out of bounds: " + startIndex);
        }

        List<Point> unvisited = new ArrayList<>(points);
        List<Point> path = new ArrayList<>();

        Point current = unvisited.remove(startIndex);
        path.add(current);

        while (!unvisited.isEmpty()) {
            Point next = unvisited.stream()
                    .min(Comparator.comparingDouble(p -> DistanceUtil.euclidean(current, p)))
                    .orElseThrow();
            unvisited.remove(next);
            path.add(next);
            current = next;
        }

        Route route = new Route();
        route.setPoints(path);
        route.setTotalTime(DistanceUtil.totalPathDistance(path));
        return route;
    }
}
