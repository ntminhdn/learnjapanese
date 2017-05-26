package com.example.user.learnjapanesevocabulary.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;

/**
 * Created by User on 08/03/2017.
 */
@IgnoreExtraProperties
public class Vocabulary implements Parcelable {
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

    protected Vocabulary(Parcel in) {
        kanji = in.readString();
        mean = in.readString();
        sound = in.readString();
        word = in.readString();
    }

    public static final Creator<Vocabulary> CREATOR = new Creator<Vocabulary>() {
        @Override
        public Vocabulary createFromParcel(Parcel in) {
            return new Vocabulary(in);
        }

        @Override
        public Vocabulary[] newArray(int size) {
            return new Vocabulary[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(kanji);
        dest.writeString(mean);
        dest.writeString(sound);
        dest.writeString(word);
    }
}
