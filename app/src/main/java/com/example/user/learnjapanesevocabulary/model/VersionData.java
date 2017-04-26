package com.example.user.learnjapanesevocabulary.model;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;

/**
 * Created by User on 09/03/2017.
 */

@IgnoreExtraProperties
public class VersionData {

    private Lesson[] mLesson;
    private String mVersion;

    public String getmVersion() {
        return mVersion;
    }

    public void setmVersion(String mVersion) {
        this.mVersion = mVersion;

    }

    public VersionData(Lesson[] lessons, String mVersion) {
        this.mLesson = lessons;
        this.mVersion = mVersion;
    }

    public VersionData() {

    }

    public Lesson[] getmLesson() {
        return mLesson;
    }

    public void setmLesson(Lesson[] mLesson) {
        this.mLesson = mLesson;
    }
}
