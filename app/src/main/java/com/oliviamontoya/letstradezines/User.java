package com.oliviamontoya.letstradezines;

/**
 * Created by Olivia on 6/22/17.
 */

import android.net.Uri;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class User {

    public String username;
    public String email;
    public String birthYear;
    public String gender;
    public String location;
    public String profileInfo;
    public Boolean perzines;
    public Boolean fanzines;
    public Boolean artZines;
    public Boolean compZines;
    public Boolean chapbooks;
    public Boolean minicomics;
    public Boolean queerZines;
    public Boolean feministZines;
    public Boolean politicalZines;
    public Boolean musicZines;
    public Boolean fictionZines;
    public Boolean other;
    public Boolean perzines2;
    public Boolean fanzines2;
    public Boolean artZines2;
    public Boolean compZines2;
    public Boolean chapbooks2;
    public Boolean minicomics2;
    public Boolean queerZines2;
    public Boolean feministZines2;
    public Boolean politicalZines2;
    public Boolean musicZines2;
    public Boolean fictionZines2;
    public Boolean other2;
    public String profilePic;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String username, String email, String birthYear, String gender, String location,
                String profileInfo, Boolean perzines, Boolean fanzines, Boolean artZines,
                Boolean compZines, Boolean chapbooks, Boolean minicomics, Boolean queerZines,
                Boolean feministZines, Boolean politicalZines, Boolean musicZines, Boolean fictionZines,
                Boolean other, Boolean perzines2, Boolean fanzines2, Boolean artZines2, Boolean compZines2,
                Boolean chapbooks2, Boolean minicomics2, Boolean queerZines2, Boolean feministZines2,
                Boolean politicalZines2, Boolean musicZines2, Boolean fictionZines2, Boolean other2, String url) {
        this.username = username;
        this.email = email;
        this.birthYear = birthYear;
        this.gender = gender;
        this.location = location;
        this.profileInfo = profileInfo;
        this.perzines = perzines;
        this.perzines2 = perzines2;
        this.fanzines = fanzines;
        this.fanzines2 = fanzines2;
        this.artZines = artZines;
        this.artZines2 = artZines2;
        this.compZines = compZines;
        this.compZines2 = compZines2;
        this.chapbooks = chapbooks;
        this.chapbooks2 = chapbooks2;
        this.minicomics = minicomics;
        this.minicomics2 = minicomics2;
        this.queerZines = queerZines;
        this.queerZines2 = queerZines2;
        this.feministZines = feministZines;
        this.feministZines2 = feministZines2;
        this.politicalZines = politicalZines;
        this.politicalZines2 = politicalZines2;
        this.musicZines = musicZines;
        this.musicZines2 = musicZines2;
        this.fictionZines = fictionZines;
        this.fictionZines2 = fictionZines2;
        this.other = other;
        this.other2 = other2;
        this.profilePic = url;
    }

}
