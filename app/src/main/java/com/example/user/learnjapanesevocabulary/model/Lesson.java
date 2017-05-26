package com.example.user.learnjapanesevocabulary.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by User on 09/03/2017.
 */

public class Lesson implements Parcelable{
    private int lesson;
    private ArrayList<Vocabulary> mVocabularyList;

    protected Lesson(Parcel in) {
        lesson = in.readInt();
    }

    public static final Creator<Lesson> CREATOR = new Creator<Lesson>() {
        @Override
        public Lesson createFromParcel(Parcel in) {
            return new Lesson(in);
        }

        @Override
        public Lesson[] newArray(int size) {
            return new Lesson[size];
        }
    };

    public ArrayList<Vocabulary> getmVocabularyList() {
        return mVocabularyList;
    }

    public void setmVocabularyList(ArrayList<Vocabulary> mVocabularyList) {
        this.mVocabularyList = mVocabularyList;
    }

    public Lesson(ArrayList<Vocabulary> mVocabularyList) {
        this.mVocabularyList = mVocabularyList;
    }

    public Lesson() {

    }

    public Lesson(int lesson, ArrayList<Vocabulary> mVocabularyList) {
        this.lesson = lesson;
        this.mVocabularyList = mVocabularyList;
    }

    public void setLesson(int lesson) {
        this.lesson = lesson;
    }

    public int getLesson() {
        return lesson;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(lesson);
    }
}
