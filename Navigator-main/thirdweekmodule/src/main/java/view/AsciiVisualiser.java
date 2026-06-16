package view;

import model.Point;
import model.Route;

import java.util.List;

public final class AsciiVisualiser {

    private static final int COLS = 60;
    private static final int ROWS = 24;

    private AsciiVisualiser() {}

    public static void print(List<Point> allPoints, Route route) {
        char[][] grid = new char[ROWS][COLS];
        for (char[] row : grid) java.util.Arrays.fill(row, ' ');

        double minX = allPoints.stream().mapToDouble(Point::getX).min().orElse(0);
        double maxX = allPoints.stream().mapToDouble(Point::getX).max().orElse(1);
        double minY = allPoints.stream().mapToDouble(Point::getY).min().orElse(0);
        double maxY = allPoints.stream().mapToDouble(Point::getY).max().orElse(1);

        for (Point p : allPoints) {
            int col = toCol(p.getX(), minX, maxX);
            int row = toRow(p.getY(), minY, maxY);
            grid[row][col] = '·';
        }

        List<Point> path = route.getPoints();
        for (int i = 0; i < path.size(); i++) {
            Point p = path.get(i);
            int col = toCol(p.getX(), minX, maxX);
            int row = toRow(p.getY(), minY, maxY);

            char label;
            if (i == 0)                   label = 'S';
            else if (i == path.size() - 1) label = 'E';
            else if (i < 10)               label = (char) ('0' + i);
            else if (i < 36)               label = (char) ('A' + i - 10);
            else                           label = '+';

            grid[row][col] = label;
        }

        String border = "+" + "-".repeat(COLS) + "+";
        System.out.println(border);
        for (char[] row : grid) {
            System.out.print("|");
            System.out.print(new String(row));
            System.out.println("|");
        }
        System.out.println(border);
        System.out.println("  S = start   E = end   0-9/A-Z = waypoints   · = unvisited point");
        System.out.printf("  Total distance: %.2f%n", route.getTotalTime());
    }

    public static void compare(List<Point> allPoints,
                               Route dijkstra, Route nearestNeighbour) {
        System.out.println("\n╔══════════════════════════════╗");
        System.out.println("║     STRATEGY COMPARISON      ║");
        System.out.println("╚══════════════════════════════╝");

        System.out.printf("  Dijkstra (sparse K-NN graph):  %.2f%n",
                dijkstra.getTotalTime());
        System.out.printf("  Nearest Neighbour (greedy):    %.2f%n",
                nearestNeighbour.getTotalTime());

        double diff = nearestNeighbour.getTotalTime() - dijkstra.getTotalTime();
        double pct  = diff / nearestNeighbour.getTotalTime() * 100;

        if (diff > 0) {
            System.out.printf("  → Dijkstra saved %.2f (%.1f%% shorter)%n", diff, pct);
        } else if (diff < 0) {
            System.out.printf("  → Nearest Neighbour saved %.2f (%.1f%% shorter)%n",
                    -diff, -pct);
        } else {
            System.out.println("  → Both strategies produced the same distance.");
        }

        System.out.println("\n--- Dijkstra route map ---");
        print(allPoints, dijkstra);

        System.out.println("\n--- Nearest Neighbour route map ---");
        print(allPoints, nearestNeighbour);
    }

    private static int toCol(double x, double minX, double maxX) {
        double norm = (maxX == minX) ? 0 : (x - minX) / (maxX - minX);
        return (int) Math.min(COLS - 1, norm * (COLS - 1));
    }

    private static int toRow(double y, double minY, double maxY) {

        double norm = (maxY == minY) ? 0 : (y - minY) / (maxY - minY);
        return (int) Math.min(ROWS - 1, (1.0 - norm) * (ROWS - 1));
    }
}