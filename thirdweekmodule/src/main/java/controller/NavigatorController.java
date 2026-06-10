package controller;

import model.Point;
import model.Route;
import service.PointService;
import service.RouteService;

import java.util.List;

public class NavigatorController {
    private final PointService pointService;
    private final RouteService routeService;

    public NavigatorController(PointService pointService,
                               RouteService routeService) {
        this.pointService = pointService;
        this.routeService = routeService;
    }

    public void run() {
        System.out.println("Generating 100 random points...");
        List<Point> points = pointService.generateAndSave(PointService.DEFAULT_POINT_COUNT);
        System.out.println("Points generated and saved.\n");

        System.out.println("Computing routes...");
        List<Route> routes = routeService.generateAndSave(points);
        System.out.println("Routes computed and saved.\n");

        printRoutes(routes);
    }

    private void printRoutes(List<Route> routes) {
        for (int i = 0; i < routes.size(); i++) {
            Route r = routes.get(i);
            System.out.printf("=== Route %d %s (total distance: %.2f) ===%n",
                    i + 1,
                    r.isFastest() ? "[FASTEST]" : "",
                    r.getTotalTime());
            for (int j = 0; j < r.getPoints().size(); j++) {
                System.out.printf("  %d. %s%n", j + 1, r.getPoints().get(j));
            }
            System.out.println();
        }
    }
}
