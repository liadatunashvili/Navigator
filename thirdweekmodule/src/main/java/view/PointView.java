package view;

import model.Point;

import java.util.List;

public class PointView {

    public void displayPoints(List<Point> points) {
        System.out.println();
        if (points == null || points.isEmpty()) {
            System.out.println("No points saved.");
            return;
        }

        System.out.printf("Saved points (%d):%n", points.size());
        for (int i = 0; i < points.size(); i++) {
            Point point = points.get(i);
            System.out.printf("  %d. %s%n", i + 1, point);
        }
        System.out.println();
    }
}
