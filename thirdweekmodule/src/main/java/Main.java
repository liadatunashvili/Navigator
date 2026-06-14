import controller.NavigatorController;
import dao.PointMapper;
import dao.RouteMapper;
import dijkstra.DijkstraStrategy;
import dijkstra.NearestNeighbourStrategy;
import mybatis.MyBatisUtil;
import org.apache.ibatis.session.SqlSession;
import repository.PointRepository;
import repository.RouteRepository;
import service.PointService;
import service.RouteService;

import java.util.List;

public class Main {

    public static void main(String[] args) {
        try (SqlSession session = MyBatisUtil.getSession()) {

            PointMapper pointMapper = session.getMapper(PointMapper.class);
            RouteMapper routeMapper = session.getMapper(RouteMapper.class);

            PointRepository pointRepository = new PointRepository(pointMapper);
            RouteRepository routeRepository = new RouteRepository(routeMapper);

            PointService pointService = new PointService(pointRepository, routeRepository);
            RouteService routeService = new RouteService(routeRepository, List.of(
                    new RouteService.RouteBuildRequest(new DijkstraStrategy(), 0),
                    new RouteService.RouteBuildRequest(new NearestNeighbourStrategy(), 0)
            ));

            new NavigatorController(pointService, routeService).run();

            session.commit();
        }
    }
}
