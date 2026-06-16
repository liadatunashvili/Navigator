package repository;

import dao.PointMapper;
import model.Point;

import java.util.List;

public class PointRepository {
    private final PointMapper mapper;

    public PointRepository(PointMapper mapper) {
        this.mapper = mapper;
    }

    public void save(Point point) {
        mapper.insert(point);
    }

    public void saveAll(List<Point> points) {
        points.forEach(mapper::insert);
    }

    public List<Point> getAll() {
        return mapper.findAll();
    }

    public void clearAll() {
        mapper.deleteAll();
    }
}
