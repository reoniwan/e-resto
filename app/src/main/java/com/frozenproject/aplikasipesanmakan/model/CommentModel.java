package com.frozenproject.aplikasipesanmakan.model;

import java.util.Map;

public class CommentModel {

    private float ratingValue;
    private String comment,name,uid;
    private Map<String,Object> commentStamp;

    public CommentModel() {
    }

    public float getRatingValue() {
        return ratingValue;
    }

    public void setRatingValue(float ratingValue) {
        this.ratingValue = ratingValue;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Map<String, Object> getCommentStamp() {
        return commentStamp;
    }

    public void setCommentStamp(Map<String, Object> commentStamp) {
        this.commentStamp = commentStamp;
    }
}
