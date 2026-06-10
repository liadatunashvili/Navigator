package service;

import model.Point;
import repository.PointRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PointService {

    private static final double PLANE_SIZE = 1000.0;

    private final PointRepository repository;

    public PointService(PointRepository repository) {
        this.repository = repository;
    }

    public List<Point> generateAndSave(int count) {
        List<Point> points = generateRandomPoints(count);
        repository.clearAll();
        repository.saveAll(points);
        return points;
    }

    private List<Point> generateRandomPoints(int count) {
        Random random = new Random();
        List<Point> points = new ArrayList<>(count);

        for (int i = 0; i < count; i++) {
            Point point = new Point();
            point.setX(random.nextDouble() * PLANE_SIZE);
            point.setY(random.nextDouble() * PLANE_SIZE);
            points.add(point);
        }

        return points;
    }
}
