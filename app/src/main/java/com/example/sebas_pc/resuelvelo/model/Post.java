package com.example.sebas_pc.resuelvelo.model;


public class Post {
    public String uid;
    public String author;
    public String authorPicUrl;
    public String mediaUrl;
    public String mediaTYPE;

    public Post(String uid, String author, String authorPicUrl) {
        this.uid = uid;
        this.author = author;
        this.authorPicUrl = authorPicUrl;

    }
    public Post(String uid, String author, String authorPicUrl, String mediaUrl, String mediaTYPE) {
        this.uid = uid;
        this.author = author;
        this.authorPicUrl = authorPicUrl;
        this.mediaUrl = mediaUrl;
        this.mediaTYPE = mediaTYPE;
    }
    public Post() {}
}