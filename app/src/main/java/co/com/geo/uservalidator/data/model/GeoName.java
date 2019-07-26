package co.com.geo.uservalidator.data.model;

public class GeoName {
    private String time;
    private String countryName;
    private double latitude;
    private double longitude;

    public String getTime() {
        return time;
    }

    public String getCountryName() {
        return countryName;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public GeoName(String time, String countryName, double latitude, double longitude) {
        this.time = time;
        this.countryName = countryName;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public GeoName() {
    }
}
