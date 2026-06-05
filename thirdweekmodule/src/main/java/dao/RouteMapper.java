package dao;

import model.Route;
import model.RoutePoint;

import java.util.List;

public interface RouteMapper {

    void insertRoute(Route route);
    void insertRoutePoint(RoutePoint rp);
    List<Route> findAllWithPoints();
    Route findById(Long id);
}
