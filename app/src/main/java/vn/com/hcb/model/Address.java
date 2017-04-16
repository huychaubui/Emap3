package vn.com.hcb.model;

/**
 * Created by HUYCHAU on 3/11/2017.
 */

public class Address {
    private String name;
    private String[] districts;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String[] getDistricts() {
        return districts;
    }

    public void setDistricts(String[] districts) {
        this.districts = districts;
    }

    public Address() {

    }

    public Address(String name, String[] districts) {

        this.name = name;
        this.districts = districts;
    }
}
