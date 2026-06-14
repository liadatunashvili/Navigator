package dijkstra;

import model.Point;
import model.Route;

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
        route.setTotalTime(calculateTotalTime(path));
        return route;
    }

    private Point findNearest(Point current, List<Point> candidates) {
        return candidates.stream()
                .min(Comparator.comparingDouble(p -> euclidean(current, p)))
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
