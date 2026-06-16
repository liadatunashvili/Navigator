package service;

import dijkstra.NearestNeighbourStrategy;
import dijkstra.RouteStrategy;
import model.Point;
import model.Route;
import repository.RouteRepository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class RouteService {

    public record RouteBuildRequest(RouteStrategy strategy, int startIndex, String label) {
        public RouteBuildRequest(RouteStrategy strategy, int startIndex) {
            this(strategy, startIndex, strategy.getClass().getSimpleName());
        }
    }

    private final RouteRepository routeRepository;
    private final List<RouteBuildRequest> buildRequests;

    public RouteService(RouteRepository routeRepository, List<RouteBuildRequest> buildRequests) {
        if (buildRequests == null || buildRequests.isEmpty()) {
            throw new IllegalArgumentException("At least one route build request is required");
        }
        this.routeRepository = routeRepository;
        this.buildRequests = List.copyOf(buildRequests);
    }

    public List<Route> generateAndSave(List<Point> points) {
        if (points == null || points.isEmpty()) {
            throw new IllegalArgumentException("Points are required to compute routes");
        }

        routeRepository.clearAll();

        List<Route> routes = new ArrayList<>();
        for (RouteBuildRequest request : buildRequests) {
            routes.add(request.strategy().buildRoute(points, request.startIndex()));
        }

        routes.sort(Comparator.comparingDouble(Route::getTotalTime));
        routes.forEach(route -> route.setFastest(false));
        routes.getFirst().setFastest(true);

        routeRepository.saveAll(routes);
        return routes;
    }

    public List<Route> compareStrategies(List<Point> points) {
        if (points == null || points.isEmpty()) {
            throw new IllegalArgumentException("Points are required to compare strategies");
        }
        Route dijkstraRoute = buildRequests.get(0).strategy().buildRoute(points, 0);
        Route nnRoute = new NearestNeighbourStrategy().buildRoute(points, 0);
        return List.of(dijkstraRoute, nnRoute);
    }

    public List<Route> getAll() {
        return routeRepository.getAll();
    }
}