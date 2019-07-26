package co.com.geo.uservalidator.data.model;

public class IntentEntity {
    private String username;
    private String date;
    private boolean result;
    private String latitude;
    private String longitude;

    public String getUsername() {
        return username;
    }

    public String getDate() {
        return date;
    }

    public boolean isResult() {
        return result;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public IntentEntity(String username, String date, boolean result, String latitude, String longitude) {
        this.username = username;
        this.date = date;
        this.result = result;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public IntentEntity() {

    }
}
