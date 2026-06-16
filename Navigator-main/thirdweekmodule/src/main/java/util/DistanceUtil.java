package util;

import model.Point;

import java.util.List;

public final class DistanceUtil {

    private DistanceUtil() {
    }

    public static double euclidean(Point a, Point b) {
        double dx = a.getX() - b.getX();
        double dy = a.getY() - b.getY();
        return Math.hypot(dx, dy);
    }

    public static double totalPathDistance(List<Point> path) {
        double total = 0;
        for (int i = 0; i < path.size() - 1; i++) {
            total += euclidean(path.get(i), path.get(i + 1));
        }
        return total;
    }
}
