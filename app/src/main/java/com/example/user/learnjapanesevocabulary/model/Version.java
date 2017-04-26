package com.example.user.learnjapanesevocabulary.model;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by User on 09/03/2017.
 */
@IgnoreExtraProperties
public class Version {
    private String version;

    public Version(String mVersion) {
        this.version = mVersion;
    }
    public Version(){

    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String mVersion) {
        this.version = mVersion;
    }
}
