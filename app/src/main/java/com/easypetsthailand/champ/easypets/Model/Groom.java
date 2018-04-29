package com.easypetsthailand.champ.easypets.Model;

public class Groom extends Service{

    private int priceRate;

    public Groom(int serviceId, String ownerUid, String name, String logoPath, String picturePath,
                 String facebookUrl, String openDays, String openTime, String closeTime, String tel
            , String address, int likes, double latitude, double longitude, String description, int priceRate) {
        super(serviceId, ownerUid, name, logoPath, picturePath, facebookUrl, openDays, openTime
                , closeTime, tel, address, likes, latitude, longitude, description);
        this.priceRate = priceRate;
    }

    public int getPriceRate() {
        return priceRate;
    }

}
