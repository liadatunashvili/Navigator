package service;

import model.Point;
import repository.PointRepository;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class PointService {
    private final PointRepository repository;

    public PointService(PointRepository repository) {
        this.repository = repository;
    }

    public List<Point> generateAndSave(int count) {
        List<Point> points = generateRandom(count);
        repository.clearAll();
        repository.saveAll(points);
        return points;
    }

    private List<Point> generateRandom(int count) {
        Random rnd = new Random();
        return IntStream.range(0, count).mapToObj(i -> {
            Point p = new Point();
            p.setX(rnd.nextDouble() * 1000);
            p.setY(rnd.nextDouble() * 1000);
            return p;
        }).collect(Collectors.toList());
    }
}