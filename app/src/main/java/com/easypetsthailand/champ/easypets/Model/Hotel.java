package com.easypetsthailand.champ.easypets.Model;

public class Hotel extends Service{

    private boolean isAcceptOvernight;
    private int hotelPrice;

    public Hotel(int serviceId, String ownerUid, String name, String logoPath, String picturePath
            , String facebookUrl, String openDays, String openTime, String closeTime, String tel
            , String address, int likes, double latitude, double longitude, String description
            , boolean isAcceptOvernight, int hotelPrice) {
        super(serviceId, ownerUid, name, logoPath, picturePath, facebookUrl, openDays, openTime
                , closeTime, tel, address, likes, latitude, longitude, description);
        this.isAcceptOvernight = isAcceptOvernight;
        this.hotelPrice = hotelPrice;
    }

    public Hotel(int serviceId, String ownerUid, String name, String logoPath, String picturePath
            , String facebookUrl, String openDays, String openTime, String closeTime, String tel
            , String address, int likes, double latitude, double longitude, String description
            , int isAcceptOvernight, int hotelPrice) {
        super(serviceId, ownerUid, name, logoPath, picturePath, facebookUrl, openDays, openTime
                , closeTime, tel, address, likes, latitude, longitude, description);
        this.isAcceptOvernight = isAcceptOvernight == 1;
        this.hotelPrice = hotelPrice;
    }

    public boolean isAcceptOvernight() {
        return isAcceptOvernight;
    }

    public int getHotelPrice() {
        return hotelPrice;
    }

}
