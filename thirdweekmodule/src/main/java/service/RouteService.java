package service;

import dijkstra.RouteStrategy;
import model.Point;
import model.Route;
import repository.RouteRepository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class RouteService {

    private static final int ROUTE_COUNT = 2;

    private final RouteRepository repository;
    private final RouteStrategy strategy;

    public RouteService(RouteRepository repository, RouteStrategy strategy) {
        this.repository = repository;
        this.strategy = strategy;
    }

    public List<Route> generateAndSave(List<Point> points) {
        List<Route> routes = calculateRoutes(points);
        markFastestRoute(routes);
        repository.saveAll(routes);
        return routes;
    }

    private List<Route> calculateRoutes(List<Point> points) {
        List<Route> routes = new ArrayList<>(ROUTE_COUNT);

        for (int startIndex = 0; startIndex < ROUTE_COUNT; startIndex++) {
            routes.add(strategy.buildRoute(points, startIndex));
        }

        routes.sort(Comparator.comparingDouble(Route::getTotalTime));
        return routes;
    }

    private void markFastestRoute(List<Route> routes) {
        if (routes.isEmpty()) {
            return;
        }
        routes.get(0).setFastest(true);
    }
}
