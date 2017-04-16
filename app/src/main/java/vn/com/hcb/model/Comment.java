package vn.com.hcb.model;

/**
 * Created by HUYCHAU on 4/13/2017.
 */

public class Comment {
    private String uid;
    private String key;


    private String content;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Comment() {

    }

    public Comment(String uid, String key, String content) {

        this.uid = uid;
        this.key = key;
        this.content = content;
    }
}
