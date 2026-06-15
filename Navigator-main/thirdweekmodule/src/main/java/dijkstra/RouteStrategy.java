package dijkstra;

import model.Point;
import model.Route;

import java.util.List;

public interface RouteStrategy {
    Route buildRoute(List<Point> points, int startIndex);
}