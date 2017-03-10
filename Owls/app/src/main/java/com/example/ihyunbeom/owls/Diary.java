package com.example.ihyunbeom.owls;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ihyunbeom on 2017-03-06.
 */

public class Diary implements Parcelable {

    private String diaryContent;
    private String diaryCreatDate;

    public Diary(String diaryContent, String diaryCreatDate){
        this.diaryContent = diaryContent;
        this.diaryCreatDate = diaryCreatDate;
    }

    public static final Creator<Diary> CREATOR = new Creator<Diary>() {
        @Override
        public Diary createFromParcel(Parcel in) {
            return new Diary(in);
        }

        @Override
        public Diary[] newArray(int size) {
            return new Diary[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }

    public String getDiaryContent() {
        return diaryContent;
    }

    public void setDiaryContent(String diaryContent) {
        this.diaryContent = diaryContent;
    }

    public String getDiaryCreatDate() {
        return diaryCreatDate;
    }

    public void setDiaryCreatDate(String diaryCreatDate) {
        this.diaryCreatDate = diaryCreatDate;
    }

    public static Creator<Diary> getCREATOR() {
        return CREATOR;
    }

    protected Diary(Parcel in) {
    }



}
