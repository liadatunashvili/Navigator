package controller;

import model.Point;
import model.Route;
import service.PointService;
import service.RouteService;
import view.AsciiVisualiser;
import util.DistanceUtil;

import java.util.*;
import java.util.stream.Collectors;

public class NavigatorController {
    private final PointService pointService;
    private final RouteService routeService;
    private final Scanner scanner;

    public NavigatorController(PointService pointService,
                               RouteService routeService,
                               Scanner scanner) {
        this.pointService = pointService;
        this.routeService = routeService;
        this.scanner = scanner;
    }
    public void run() {
        System.out.println("Generating 100 random points...");
        List<Point> points = pointService.generateAndSave(100);
        System.out.println("Points generated and saved.\n");

        System.out.println("Computing routes...");
        List<Route> routes = routeService.generateAndSave(points);
        System.out.println("Routes computed and saved.\n");

        printRoutes(routes, points);
    }

    public void displaySavedRoutes() {
        List<Route> routes = routeService.getAll();
        if (routes.isEmpty()) {
            System.out.println("\nNo saved routes found. Generate first.");
            return;
        }
        List<Point> points = pointService.getAll();
        printRoutes(routes, points);
    }

    public void displaySavedPoints() {
        List<Point> points = pointService.getAll();
        if (points.isEmpty()) {
            System.out.println("\nNo saved points found. Generate first.");
            return;
        }
        System.out.println("\n=== SAVED POINTS (" + points.size() + ") ===");
        points.forEach(p -> System.out.println("  " + p));
    }
    public void compareStrategies() {
        List<Point> points = pointService.getAll();
        if (points.isEmpty()) {
            System.out.println("\nNo points found. Generate first.");
            return;
        }
        System.out.println("\nComparing Dijkstra vs Nearest Neighbour...");
        List<Route> routes = routeService.compareStrategies(points);
        AsciiVisualiser.compare(points, routes.get(0), routes.get(1));
    }
    public void startFreeNavigation() {
        List<Point> points = pointService.getAll();
        if (points.isEmpty()) {
            System.out.println("\nNo points found. Generate first.");
            return;
        }

        Map<Long, Point> pointMap = points.stream()
                .collect(Collectors.toMap(Point::getId, p -> p));

        long minId = pointMap.keySet().stream().min(Long::compareTo).orElseThrow();
        long maxId = pointMap.keySet().stream().max(Long::compareTo).orElseThrow();

        System.out.printf("%nSaved points: %d total (IDs %d to %d)%n",
                points.size(), minId, maxId);
        System.out.print("Enter starting point ID: ");

        long currentId;
        try {
            currentId = Long.parseLong(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Invalid ID.");
            return;
        }

        if (!pointMap.containsKey(currentId)) {
            System.out.println("Point ID not found.");
            return;
        }

        Set<Long> visited = new LinkedHashSet<>();
        visited.add(currentId);
        double totalDistance = 0;

        while (true) {
            Point current = pointMap.get(currentId);
            System.out.printf("%n--- At %s ---%n", current);
            System.out.printf("Distance travelled: %.2f   Visited: %d / %d%n",
                    totalDistance, visited.size(), points.size());

            List<Point> nearest = points.stream()
                    .filter(p -> !visited.contains(p.getId()))
                    .sorted(Comparator.comparingDouble(p -> DistanceUtil.euclidean(current, p)))
                    .limit(5)
                    .collect(Collectors.toList());

            if (nearest.isEmpty()) {
                System.out.println("\nAll points visited!");
                break;
            }

            System.out.println("\nNearest unvisited points:");
            for (int i = 0; i < nearest.size(); i++) {
                Point p = nearest.get(i);
                System.out.printf("  %d. %-28s  dist: %.2f%n",
                        i + 1, p, DistanceUtil.euclidean(current, p));
            }
            System.out.println("  0. Stop navigation");
            System.out.print("Choose: ");

            int choice;
            try {
                choice = Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input.");
                continue;
            }

            if (choice == 0) {
                System.out.printf("%nStopped. Total distance: %.2f%n", totalDistance);
                break;
            }
            if (choice < 1 || choice > nearest.size()) {
                System.out.println("Invalid choice.");
                continue;
            }

            Point next = nearest.get(choice - 1);
            totalDistance += DistanceUtil.euclidean(current, next);
            visited.add(next.getId());
            currentId = next.getId();
        }

        System.out.println("\n=== NAVIGATION SUMMARY ===");
        System.out.printf("Points visited : %d / %d%n", visited.size(), points.size());
        System.out.printf("Total distance : %.2f%n", totalDistance);
    }
    private void printRoutes(List<Route> routes, List<Point> allPoints) {
        for (int i = 0; i < routes.size(); i++) {
            Route r = routes.get(i);
            System.out.printf("=== Route %d %s (total distance: %.2f) ===%n",
                    i + 1,
                    r.isFastest() ? "[FASTEST]" : "",
                    r.getTotalTime());
            for (int j = 0; j < r.getPoints().size(); j++) {
                System.out.printf("  %3d. %s%n", j + 1, r.getPoints().get(j));
            }
            System.out.println();
            AsciiVisualiser.print(allPoints, r);
            System.out.println();
        }
    }
}