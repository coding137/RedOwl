package org.hyunbeom.owls_02;

/**
 * Created by DIE on 2017-02-21.
 */

public class Diary {

    String diary;
    String name;
    String date;

    public Diary(String diary, String name, String date) {
        this.diary = diary;
        this.name = name;
        this.date = date;
    }

    public String getDiary() {
        return diary;
    }

    public void setDiary(String diary) {
        this.diary = diary;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
