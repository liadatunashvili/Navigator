package service;

import model.Point;
import repository.PointRepository;
import repository.RouteRepository;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class PointService {

    public static final int DEFAULT_POINT_COUNT = 100;
    public static final double PLANE_SIZE = 1000.0;

    private final PointRepository pointRepository;
    private final RouteRepository routeRepository;
    private final Random random;

    public PointService(PointRepository pointRepository, RouteRepository routeRepository) {
        this(pointRepository, routeRepository, new Random());
    }

    PointService(PointRepository pointRepository, RouteRepository routeRepository, Random random) {
        this.pointRepository = pointRepository;
        this.routeRepository = routeRepository;
        this.random = random;
    }

    public List<Point> generateAndSave(int count) {
        if (count <= 0) {
            throw new IllegalArgumentException("Point count must be positive: " + count);
        }

        routeRepository.clearAll();
        pointRepository.clearAll();

        List<Point> points = generateRandom(count);
        pointRepository.saveAll(points);
        return points;
    }

    public List<Point> getAll() {
        return pointRepository.getAll();
    }

    private List<Point> generateRandom(int count) {
        return IntStream.range(0, count)
                .mapToObj(i -> {
                    Point point = new Point();
                    point.setX(random.nextDouble() * PLANE_SIZE);
                    point.setY(random.nextDouble() * PLANE_SIZE);
                    return point;
                })
                .collect(Collectors.toList());
    }
}
