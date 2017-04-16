package vn.com.hcb.model;

/**
 * Created by HUYCHAU on 3/11/2017.
 */

public class Favorite {
    private String id_user;
    private String id_service;

    public Favorite() {
    }

    public Favorite(String id_user, String id_service) {

        this.id_user = id_user;
        this.id_service = id_service;
    }

    public String getId_user() {
        return id_user;
    }

    public void setId_user(String id_user) {
        this.id_user = id_user;
    }

    public String getId_service() {
        return id_service;
    }

    public void setId_service(String id_service) {
        this.id_service = id_service;
    }
}
