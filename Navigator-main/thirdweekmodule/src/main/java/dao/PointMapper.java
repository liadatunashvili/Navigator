package dao;

import model.Point;

import java.util.List;

public interface PointMapper {

    void insert(Point point);
    List<Point> findAll();
    Point findById(Long id);
    void deleteAll();
}
