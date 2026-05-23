package com.thaivantrung.littleenglishhero;

public class VocabularyModel {

    int id;
    int lessonId;

    String word;
    String meaning;
    String image;

    public VocabularyModel(int id, int lessonId,
                           String word,
                           String meaning,
                           String image) {

        this.id = id;
        this.lessonId = lessonId;
        this.word = word;
        this.meaning = meaning;
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public int getLessonId() {
        return lessonId;
    }

    public String getWord() {
        return word;
    }

    public String getMeaning() {
        return meaning;
    }

    public String getImage() {
        return image;
    }
}
