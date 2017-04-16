package vn.com.hcb.model;

/**
 * Created by HUYCHAU on 4/13/2017.
 */

public class Comments {


    private String key;
    private String name;
    private String content;
    private String avatar;
    private String uid;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Comments() {
    }

    public Comments(String name, String avatar, String content) {
        this.name = name;
        this.content = content;
        this.avatar = avatar;
    }
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String urlAvatar) {
        this.avatar = urlAvatar;
    }
}
