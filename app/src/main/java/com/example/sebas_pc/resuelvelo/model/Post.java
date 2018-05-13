package com.example.sebas_pc.resuelvelo.model;


import java.util.HashMap;

public class Post {
    public String uid;
    public String mediaUrl;
    public String mediaTYPE;

    public Post(String uid) {
        this.uid = uid;

    }
    public Post(String uid, String mediaUrl, String mediaTYPE) {
        this.uid = uid;
        this.mediaUrl = mediaUrl;
        this.mediaTYPE = mediaTYPE;
    }
    public Post() {}
}