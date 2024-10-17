/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author Admin
 */
public class ChapterImage {
    private int ImageID;
    private int chapter_id;
    private String image_url;
    private int PageNumber;

    public ChapterImage() {
    }

    public ChapterImage(int ImageID, int chapter_id, String image_url, int PageNumber) {
        this.ImageID = ImageID;
        this.chapter_id = chapter_id;
        this.image_url = image_url;
        this.PageNumber = PageNumber;
    }

    public int getImageID() {
        return ImageID;
    }

    public void setImageID(int ImageID) {
        this.ImageID = ImageID;
    }

    public int getChapter_id() {
        return chapter_id;
    }

    public void setChapter_id(int chapter_id) {
        this.chapter_id = chapter_id;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public int getPageNumber() {
        return PageNumber;
    }

    public void setPageNumber(int PageNumber) {
        this.PageNumber = PageNumber;
    }
    
    
    
}
