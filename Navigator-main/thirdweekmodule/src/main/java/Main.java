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
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

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

            NavigatorController controller = new NavigatorController(
                    pointService, routeService, scanner);

            runMenu(controller, scanner);

            session.commit();
        } finally {
            scanner.close();
        }
    }

    private static void runMenu(NavigatorController controller, Scanner scanner) {
        while (true) {
            System.out.println("\n=== NAVIGATOR ===");
            System.out.println("1. Generate new points and compute routes");
            System.out.println("2. View saved routes");
            System.out.println("3. View saved points");
            System.out.println("4. Compare Dijkstra vs Nearest Neighbour");
            System.out.println("5. Free navigation");
            System.out.println("6. Exit");
            System.out.print("Choose: ");

            int choice;
            try {
                choice = Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input.");
                continue;
            }

            switch (choice) {
                case 1 -> controller.run();
                case 2 -> controller.displaySavedRoutes();
                case 3 -> controller.displaySavedPoints();
                case 4 -> controller.compareStrategies();
                case 5 -> controller.startFreeNavigation();
                case 6 -> { System.out.println("Goodbye!"); return; }
                default -> System.out.println("Invalid option.");
            }
        }
    }
}