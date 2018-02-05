package com.easypetsthailand.champ.easypets.Model;

import android.support.annotation.Nullable;

import java.io.Serializable;

import static com.easypetsthailand.champ.easypets.Core.Utils.openDaysStringToBooleanArray;

public class Store implements Serializable {

    private int storeId;
    private String name;
    private String logoPath;
    private String picturePath;
    private String type;
    private boolean[] openDays; //true at [0] for everyday open, 1 for Sunday, 2 for Tuesday etc.
    private String openTime;
    private String closeTime;
    private String tel;
    private int priceRate;
    private int likes = 0;
    private double latitude;
    private double longitude;
    private String description;

    public Store(int storeId, String name, String logoPath, String picturePath, String type,
                 String openDays, @Nullable String openTime, @Nullable String closeTime, String tel,
                 int priceRate, int likes, double latitude, double longitude, String description) {
        this.storeId = storeId;
        this.name = name;
        this.logoPath = logoPath;
        this.picturePath = picturePath;
        this.type = type;
        this.openDays = openDaysStringToBooleanArray(openDays);
        if(openTime == null) this.openTime = "nope";
        else this.openTime = openTime;
        if(closeTime == null) this.closeTime = "nope";
        this.closeTime = closeTime;
        this.tel = tel;
        this.priceRate = priceRate;
        this.likes = likes;
        this.latitude = latitude;
        this.longitude = longitude;
        this.description = description;
    }

    public int getStoreId() {
        return storeId;
    }

    public String getName() {
        return name;
    }

    public String getLogoPath() {
        return logoPath;
    }

    public String getPicturePath() {
        return picturePath;
    }

    public String getType() {
        return type;
    }

    public boolean[] getOpenDays() {
        return openDays;
    }

    public String getOpenTime() {
        return openTime;
    }

    public String getCloseTime() {
        return closeTime;
    }

    public String getTel() {
        return tel;
    }

    public int getPriceRate() {
        return priceRate;
    }

    public int getLikes() {
        return likes;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getDescription() {
        return description;
    }

}
