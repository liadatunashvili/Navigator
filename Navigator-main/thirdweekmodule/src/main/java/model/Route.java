package model;

import java.util.List;

public class Route {

    private Long id;
    private double totalTime;
    private boolean isFastest;
    private List<Point> points;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(double totalTime) {
        this.totalTime = totalTime;
    }

    public boolean isFastest() {
        return isFastest;
    }

    public void setFastest(boolean fastest) {
        isFastest = fastest;
    }

    public List<Point> getPoints() {
        return points;
    }

    public void setPoints(List<Point> points) {
        this.points = points;
    }
}
