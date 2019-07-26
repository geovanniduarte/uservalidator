package co.com.geo.uservalidator.data.model;

public class UserEntity {

    private long id;
    private String username;
    private String password;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }



    public UserEntity(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public UserEntity(long id) {
        this.id = id;
    }

    public UserEntity() {

    }
}
