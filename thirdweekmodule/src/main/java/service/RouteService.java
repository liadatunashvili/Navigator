package service;

import dijkstra.RouteStrategy;
import model.Point;
import model.Route;
import repository.RouteRepository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class RouteService {
    private final RouteRepository repository;
    private final RouteStrategy strategy;

    public RouteService(RouteRepository repository, RouteStrategy strategy) {
        this.repository = repository;
        this.strategy = strategy;
    }

    public List<Route> generateAndSave(List<Point> points) {
        Route r1 = strategy.buildRoute(points, 0);
        Route r2 = strategy.buildRoute(points, 1);

        List<Route> routes = new ArrayList<>(List.of(r1, r2));
        routes.sort(Comparator.comparingDouble(Route::getTotalTime));
        routes.get(0).setFastest(true);

        repository.saveAll(routes);
        return routes;
    }
}
