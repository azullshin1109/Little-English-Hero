package com.thaivantrung.littleenglishhero;

public class LessonModel {
    private int image;
    private String lesson;
    private String title;
    private String activity;

    public LessonModel(int image, String lesson, String title, String activity) {
        this.image = image;
        this.lesson = lesson;
        this.title = title;
        this.activity = activity;
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
}
