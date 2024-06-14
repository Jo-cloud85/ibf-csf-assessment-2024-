package ibf2023.csf.backend.models;

import java.time.LocalDateTime;

import org.bson.types.ObjectId;

public class Post {
    private ObjectId _id;
    private LocalDateTime date;
    private String title;
    private String comments;
    private String url;
    private long pictureSize; 
    
    public Post() {
    }

    public Post(ObjectId _id, LocalDateTime date, String title, String comments, String url, long pictureSize) {
        this._id = _id;
        this.date = date;
        this.title = title;
        this.comments = comments;
        this.url = url;
        this.pictureSize = pictureSize;
    }

    public ObjectId get_id() {
        return _id;
    }

    public void set_id(ObjectId _id) {
        this._id = _id;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public long getPictureSize() {
        return pictureSize;
    }

    public void setPictureSize(long pictureSize) {
        this.pictureSize = pictureSize;
    }

    @Override
    public String toString() {
        return "Post [_id=" + _id + ", date=" + date + ", title=" + title + ", comments=" + comments + ", url=" + url
                + ", pictureSize=" + pictureSize + "]";
    }
}
