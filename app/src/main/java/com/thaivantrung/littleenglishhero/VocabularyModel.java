package com.thaivantrung.littleenglishhero;

public class VocabularyModel {

    private String word;
    private String meaning;
    private String image;

    public VocabularyModel(String word, String meaning, String image) {
        this.word = word;
        this.meaning = meaning;
        this.image = image;
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