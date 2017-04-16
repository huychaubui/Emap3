package vn.com.hcb.model;

/**
 * Created by HUYCHAU on 3/11/2017.
 */

public class User {
    private String name;
    private String email;
    private String avatar;

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public User(String email, String name, String avatar ) {
        this.email = email;
        this.name = name;
        this.avatar = avatar;
    }

    public User() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
