package com.example.user.learnjapanesevocabulary.model;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;

/**
 * Created by User on 09/03/2017.
 */

@IgnoreExtraProperties
public class Lesson {
    private ArrayList<Vocabulary> mVocabularyList;

    public ArrayList<Vocabulary> getmVocabularyList() {
        return mVocabularyList;
    }

    public void setmVocabularyList(ArrayList<Vocabulary> mVocabularyList) {
        this.mVocabularyList = mVocabularyList;
    }

    public Lesson(ArrayList<Vocabulary> mVocabularyList) {
        this.mVocabularyList = mVocabularyList;
    }
    public Lesson(){

    }
}
