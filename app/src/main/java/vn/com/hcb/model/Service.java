package vn.com.hcb.model;

import java.io.Serializable;

/**
 * Created by HUYCHAU on 3/11/2017.
 */

public class Service implements Serializable {
    // Nếu ko đc thì thêm implements Seriarilaze
    private String name;
    private String address;
    private String city;
    private String district;
    private float lat;
    private float lng;
    private int price_low;
    private int price_high;
    private String intro;
    private String image;

    public Service() {
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public Service(String name, String address, String city, String district, float lat, float lng, int price_low, int price_high, String intro, String image) {

        this.name = name;
        this.address = address;
        this.city = city;
        this.district = district;
        this.lat = lat;
        this.lng = lng;
        this.price_low = price_low;
        this.price_high = price_high;
        this.intro = intro;
        this.image = image;
    }
    public void setComplex(String name, String address, float lat, float lng, int price_low, int price_high, String intro){
        this.name = name;
        this.address = address;
        this.lat = lat;
        this.lng = lng;
        this.price_low = price_low;
        this.price_high = price_high;
        this.intro = intro;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(float lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(float lng) {
        this.lng = lng;
    }

    public int getPrice_low() {
        return price_low;
    }

    public void setPrice_low(int price_low) {
        this.price_low = price_low;
    }

    public int getPrice_high() {
        return price_high;
    }

    public void setPrice_high(int price_high) {
        this.price_high = price_high;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }
    @Override
    public String toString() {
        return "Service{" +
                "name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", city='" + city + '\'' +
                ", district='" + district + '\'' +
                ", lat=" + lat +
                ", lng=" + lng +
                ", price_low=" + price_low +
                ", price_high=" + price_high +
                ", intro='" + intro + '\'' +
                '}';
    }
    public String pricetoString(){
        String s="";
        s = this.price_low + " đồng - " + this.price_high +" đồng ";
        return s;
    }

}
