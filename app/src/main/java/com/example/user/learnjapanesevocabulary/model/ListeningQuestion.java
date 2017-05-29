package com.example.user.learnjapanesevocabulary.model;

/**
 * Created by minh.nt on 5/29/2017.
 */

public class ListeningQuestion {
    private String sound;
    private String correctWord;
    private String firstWrongWord;
    private String secondWrongWord;

    public ListeningQuestion(String sound, String correctWord, String firstWrongWord, String secondWrongWord) {
        this.sound = sound;
        this.correctWord = correctWord;
        this.firstWrongWord = firstWrongWord;
        this.secondWrongWord = secondWrongWord;
    }

    public String getSound() {
        return sound;
    }

    public void setSound(String sound) {
        this.sound = sound;
    }

    public String getCorrectWord() {
        return correctWord;
    }

    public void setCorrectWord(String correctWord) {
        this.correctWord = correctWord;
    }

    public String getFirstWrongWord() {
        return firstWrongWord;
    }

    public void setFirstWrongWord(String firstWrongWord) {
        this.firstWrongWord = firstWrongWord;
    }

    public String getSecondWrongWord() {
        return secondWrongWord;
    }

    public void setSecondWrongWord(String secondWrongWord) {
        this.secondWrongWord = secondWrongWord;
    }
}
