package com.thaivantrung.littleenglishhero;

public class LessonModel {
    private int image;
    private String lesson;
    private String title;
    private String activity;

    private int lessonId;

    public LessonModel(int image, String lesson, String title, String activity, int lessonid) {
        this.image = image;
        this.lesson = lesson;
        this.title = title;
        this.activity = activity;
        this.lessonId = lessonid;
    }

    public int getImage() {
        return image;
    }

    public String getLesson() {
        return lesson;
    }

    public String getTitle() {
        return title;
    }

    public String getActivity() {
        return activity;
    }

    public int getLessonId() {
        return lessonId;
    }
}
