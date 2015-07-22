package org.guateora.oraconmigo.models;

/**
 * Created by franz on 7/22/2015.
 */
public class Testimonial {

    private String author;
    private String text;
    private String authorPhotoURL;

    public Testimonial(String author, String text, String authorPhotoURL) {
        this.author = author;
        this.text = text;
        this.authorPhotoURL = authorPhotoURL;
    }

    public String getAuthor() {
        return author;
    }

    public String getText() {
        return text;
    }

    public String getAuthorPhotoURL(){
        return authorPhotoURL;
    }
}
