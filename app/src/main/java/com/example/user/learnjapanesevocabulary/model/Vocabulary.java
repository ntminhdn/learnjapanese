package com.example.user.learnjapanesevocabulary.model;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by User on 08/03/2017.
 */
@IgnoreExtraProperties
public class Vocabulary {
    private String kanji;
    private String mean;
    private String sound;
    private String word;

    public Vocabulary(String mKanji, String mMean, String mSound, String mWord) {
        this.kanji = mKanji;
        this.mean = mMean;
        this.sound = mSound;
        this.word = mWord;
    }

    public Vocabulary() {
    }

    public String getKanji() {
        return kanji;
    }

    public void setKanji(String mKanji) {
        this.kanji = mKanji;
    }

    public String getMean() {
        return mean;
    }

    public void setMean(String mMean) {
        this.mean = mMean;
    }

    public String getSound() {
        return sound;
    }

    public void setSound(String mSound) {
        this.sound = mSound;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String mWord) {
        this.word = mWord;
    }
}
