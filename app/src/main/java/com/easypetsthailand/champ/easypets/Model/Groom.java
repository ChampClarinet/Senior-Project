package com.easypetsthailand.champ.easypets.Model;

public class Groom extends Service {

    private int priceRate;

    public Groom(int serviceId, String ownerUid, String name, String logoPath, String picturePath,
                 String facebookUrl, String openDays, String openTime, String closeTime, String tel
            , String address, int likes, double latitude, double longitude, String description, int priceRate) {
        super(serviceId, ownerUid, name, logoPath, picturePath, facebookUrl, openDays, openTime
                , closeTime, tel, address, likes, latitude, longitude, description);
        this.priceRate = priceRate;
    }

    public Groom(Service service, int priceRate) {
        super(service.getServiceId(), service.getOwnerUid(), service.getName(), service.getLogoPath()
                , service.getPicturePath(), service.getFacebookUrl(), service.getOpenDays(), service.getOpenTime()
                , service.getCloseTime(), service.getTel(), service.getAddress(), service.getLikes()
                , service.getLatitude(), service.getLongitude(), service.getDescription());

        this.priceRate = priceRate;
    }

    public int getPriceRate() {
        return priceRate;
    }

    private String getOpenDayString(boolean[] openDays){
        String getOpenDays = "";
        for (boolean b : openDays) {
            if (b) {
                getOpenDays += "1";
            } else getOpenDays += "0";
        }
        return getOpenDays;
    }

}
