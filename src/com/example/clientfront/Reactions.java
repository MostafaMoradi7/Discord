package com.example.clientfront;

public class Reactions {
    private int messageId;
    private int like;
    private int dislike;
    private int laugh;
    private String created_At;

    public Reactions(int messageId, int like, int dislike, int laugh) {
        this.messageId = messageId;
        this.like = like;
        this.dislike = dislike;
        this.laugh = laugh;
    }

    public int getMessageId() {
        return messageId;
    }

    public void setMessageId(int messageId) {
        this.messageId = messageId;
    }

    public int getLike() {
        return like;
    }

    public void setLike(int like) {
        this.like = like;
    }

    public int getDislike() {
        return dislike;
    }

    public void setDislike(int dislike) {
        this.dislike = dislike;
    }

    public int getLaugh() {
        return laugh;
    }

    public void setLaugh(int laugh) {
        this.laugh = laugh;
    }

    public String getCreated_At() {
        return created_At;
    }

    public void setCreated_At(String created_At) {
        this.created_At = created_At;
    }
}
