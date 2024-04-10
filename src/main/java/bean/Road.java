package bean;

public class Road {
    private int start_id;
    private int end_id;
    private double length;
    private double speed_limit;

    public Road() {
    }

    public Road(int start_id, int end_id, double length, double speed_limit) {
        this.start_id = start_id;
        this.end_id = end_id;
        this.length = length;
        this.speed_limit = speed_limit;
    }

    public int getStart_id() {
        return start_id;
    }

    public int getEnd_id() {
        return end_id;
    }

    public double getLength() {
        return length;
    }

    public double getSpeed_limit() {
        return speed_limit;
    }

    public void setStart_id(int start_id) {
        this.start_id = start_id;
    }

    public void setEnd_id(int end_id) {
        this.end_id = end_id;
    }

    public void setLength(double length) {
        this.length = length;
    }

    public void setSpeed_limit(double speed_limit) {
        this.speed_limit = speed_limit;
    }

    @Override
    public String toString() {
        return "Road{" +
                "start_id=" + start_id +
                ", end_id=" + end_id +
                ", length=" + length +
                ", speed_limit=" + speed_limit +
                '}';
    }
}
