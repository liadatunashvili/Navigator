import controller.NavigatorController;
import dao.PointMapper;
import dao.RouteMapper;
import dijkstra.DijkstraStrategy;
import mybatis.MyBatisUtil;
import org.apache.ibatis.session.SqlSession;
import repository.PointRepository;
import repository.RouteRepository;
import service.PointService;
import service.RouteService;

public class Main {

    public static void main(String[] args) {
        try (SqlSession session = MyBatisUtil.getSession()) {

            PointMapper pointMapper = session.getMapper(PointMapper.class);
            RouteMapper routeMapper = session.getMapper(RouteMapper.class);

            PointRepository pointRepo = new PointRepository(pointMapper);
            RouteRepository routeRepo = new RouteRepository(routeMapper);

            PointService pointService = new PointService(pointRepo);
            RouteService routeService = new RouteService(routeRepo,
                    new DijkstraStrategy());

            new NavigatorController(pointService, routeService).run();

            session.commit();
        }
    }
}