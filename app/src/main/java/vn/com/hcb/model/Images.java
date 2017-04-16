package vn.com.hcb.model;

/**
 * Created by HUYCHAU on 3/11/2017.
 */

public class Images {
    private String name;
    private String url;

    public Images() {
    }

    public Images(String name, String url) {

        this.name = name;
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
