package view;

import model.Route;

import java.util.List;

public class RouteView {

    public void displayRoutes(List<Route> routes) {
        System.out.println();
        if (routes == null || routes.isEmpty()) {
            System.out.println("No routes saved.");
            return;
        }

        for (int i = 0; i < routes.size(); i++) {
            Route route = routes.get(i);
            System.out.printf("=== Route %d %s (total distance: %.2f) ===%n",
                    i + 1,
                    route.isFastest() ? "[FASTEST]" : "",
                    route.getTotalTime());
            for (int j = 0; j < route.getPoints().size(); j++) {
                System.out.printf("  %d. %s%n", j + 1, route.getPoints().get(j));
            }
            System.out.println();
        }
    }
}
