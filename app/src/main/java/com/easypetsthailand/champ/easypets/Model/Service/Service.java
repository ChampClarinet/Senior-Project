package com.easypetsthailand.champ.easypets.Model.Service;

import android.support.annotation.Nullable;

import java.io.Serializable;

import static com.easypetsthailand.champ.easypets.Core.Utils.openDaysStringToBooleanArray;

public class Service implements Serializable {

    private int serviceId;
    private String ownerUid;
    private String name;
    private String logoPath;
    private String picturePath;
    private boolean[] openDays; //true if open, 0 for Sunday, 1 for Tuesday etc.
    private String openTime;
    private String closeTime;
    private String tel;
    private String address;
    private int likes;
    private double latitude;
    private double longitude;
    private String description;

    public Service(int serviceId, String ownerUid, String name, String logoPath, String picturePath,
                   String openDays, String openTime, String closeTime, String tel,
                   String address, int likes, double latitude, double longitude, String description) {
        this.serviceId = serviceId;
        this.ownerUid = ownerUid;
        this.name = name;
        this.logoPath = logoPath;
        this.picturePath = picturePath;
        this.openDays = openDaysStringToBooleanArray(openDays);
        this.openTime = openTime;
        this.closeTime = closeTime;
        this.tel = tel;
        this.address = address;
        this.likes = likes;
        this.latitude = latitude;
        this.longitude = longitude;
        this.description = description;
    }

    public int getServiceId() {
        return serviceId;
    }

    public String getOwnerUid() {
        return ownerUid;
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

    public String getAddress() {
        return address;
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
