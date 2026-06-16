package view;

import model.Point;

import java.util.*;

public final class SparseGraphBuilder {

    private SparseGraphBuilder() {}

    public static Map<Long, List<long[]>> build(List<Point> points, int k) {
        Map<Long, Point> byId = new LinkedHashMap<>();
        for (Point p : points) byId.put(p.getId(), p);

        Map<Long, List<long[]>> graph = new HashMap<>();

        for (Point src : points) {

            List<Point> sorted = new ArrayList<>(points);
            sorted.remove(src);
            sorted.sort(Comparator.comparingDouble(p -> RouteUtils.euclidean(src, p)));

            List<long[]> neighbours = new ArrayList<>();
            for (int i = 0; i < Math.min(k, sorted.size()); i++) {
                Point nb = sorted.get(i);
                long distBits = Double.doubleToLongBits(
                        RouteUtils.euclidean(src, nb));
                neighbours.add(new long[]{nb.getId(), distBits});
            }
            graph.put(src.getId(), neighbours);
        }
        return graph;
    }
}