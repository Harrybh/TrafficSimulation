package bean;

public class Poi {
    private double longitude;
    private double latitude;
    private String name;
    private String address;

    public Poi() {

    }

    public Poi(double longitude, double latitude, String name, String address) {
        this.longitude = longitude;
        this.latitude = latitude;
        this.name = name;
        this.address = address;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "Poi{" +
                "longitude=" + longitude +
                ", latitude=" + latitude +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                '}';
    }

}
