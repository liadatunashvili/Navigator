package repository;

import dao.RouteMapper;
import model.Route;
import model.RoutePoint;

import java.util.List;

public class RouteRepository {
    private final RouteMapper mapper;

    public RouteRepository(RouteMapper mapper) {
        this.mapper = mapper;
    }

    public void save(Route route) {
        mapper.insertRoute(route);
        for (int i = 0; i < route.getPoints().size(); i++) {
            RoutePoint rp = new RoutePoint();
            rp.setRouteId(route.getId());
            rp.setPointId(route.getPoints().get(i).getId());
            rp.setPosition(i);
            mapper.insertRoutePoint(rp);
        }
    }

    public void saveAll(List<Route> routes) {
        routes.forEach(this::save);
    }

    public List<Route> getAll() {
        return mapper.findAllWithPoints();
    }
}
