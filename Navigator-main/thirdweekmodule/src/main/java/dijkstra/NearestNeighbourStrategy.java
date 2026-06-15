package dijkstra;

import model.Point;
import model.Route;
import view.RouteUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class NearestNeighbourStrategy implements RouteStrategy {

    @Override
    public Route buildRoute(List<Point> points, int startIndex) {
        List<Point> unvisited = new ArrayList<>(points);
        List<Point> path = new ArrayList<>();

        Point current = unvisited.remove(startIndex);
        path.add(current);

        while (!unvisited.isEmpty()) {
            Point next = findNearest(current, unvisited);
            unvisited.remove(next);
            path.add(next);
            current = next;
        }

        Route route = new Route();
        route.setPoints(path);
        route.setTotalTime(RouteUtils.totalDistance(path));
        return route;
    }

    private Point findNearest(Point current, List<Point> candidates) {
        return candidates.stream()
                .min(Comparator.comparingDouble(p -> RouteUtils.euclidean(current, p)))
                .orElseThrow();
    }
}