package com.automation.epic.etnappli;

import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by root on 02/05/16.
 */
public class cardContent {

    public String picture;
    public String generalNote;
    public ArrayList<String> badges;
    public String username;
    public String firstname;
    public String lastname;
    public String description;
    public JSONObject grades;

    public cardContent(String picture, ArrayList<String> badges, String username, String firstname, String lastname, String description, JSONObject grades, String generalNote) {
        this.picture = picture;
        this.badges = badges;
        this.username = username;
        this.firstname = firstname;
        this.lastname = lastname;
        this.description = "    "+description;
        this.grades = grades;
        this.generalNote = generalNote;
    }

    public String getGeneralNote() {
        return generalNote;
    }

    public void setGeneralNote(String generalNote) {
        this.generalNote = generalNote;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public ArrayList<String> getBadges() {
        return badges;
    }

    public void setBadges(ArrayList<String> badges) {
        this.badges = badges;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public JSONObject getGrades() {
        return grades;
    }

    public void setGrades(JSONObject grades) {
        this.grades = grades;
    }
}
