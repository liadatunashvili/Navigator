package view;

import model.Point;

import java.util.List;

public final class RouteUtils {

    private RouteUtils() {}

    public static double euclidean(Point a, Point b) {
        double dx = a.getX() - b.getX();
        double dy = a.getY() - b.getY();
        return Math.sqrt(dx * dx + dy * dy);
    }

    public static double totalDistance(List<Point> path) {
        double total = 0;
        for (int i = 0; i < path.size() - 1; i++) {
            total += euclidean(path.get(i), path.get(i + 1));
        }
        return total;
    }
}