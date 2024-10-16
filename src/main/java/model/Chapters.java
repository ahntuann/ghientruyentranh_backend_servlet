
package model;

import java.sql.Date;

public class Chapters {

    private int chapterId;
    private int storyId;
    private String chapterNumber;
    private String title;
    private Date createdAt;
    private Date updatedAt;

    public Chapters() {
    }

    public Chapters(int chapterId, int storyId, String chapterNumber, String title, Date createdAt, Date updatedAt) {
        this.chapterId = chapterId;
        this.storyId = storyId;
        this.chapterNumber = chapterNumber;
        this.title = title;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Chapters(int chapterId, String chapterNumber) {
        this.chapterId = chapterId;
        this.chapterNumber = chapterNumber;
    }

    public Chapters(int chapterId, int storyId, String chapterNumber) {
        this.chapterId = chapterId;
        this.storyId = storyId;
        this.chapterNumber = chapterNumber;
    }
    
    

    public int getChapterId() {
        return chapterId;
    }

    public void setChapterId(int chapterId) {
        this.chapterId = chapterId;
    }

    public int getStoryId() {
        return storyId;
    }

    public void setStoryId(int storyId) {
        this.storyId = storyId;
    }

    public String getChapterNumber() {
        return chapterNumber;
    }

    public void setChapterNumber(String chapterNumber) {
        this.chapterNumber = chapterNumber;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }
}
