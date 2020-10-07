package com.example.han.adding;

public class PreviewClass {
    int cnum;
    private String title;
    private String image;

    public PreviewClass(int cnum, String title, String image) {
        this.cnum = cnum;
        this.title = title;
        this.image = image;
    }

    public PreviewClass() {
    }

    public int getCnum() {
        return cnum;
    }

    public void setCnum(int cnum) {
        this.cnum = cnum;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

}
