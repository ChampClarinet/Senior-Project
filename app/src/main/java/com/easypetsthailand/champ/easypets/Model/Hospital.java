package com.easypetsthailand.champ.easypets.Model;

public class Hospital extends Service{

    private boolean isAcceptBigOperation;
    private int checkupPriceRate;
    private int vaccinePriceRate;
    private int operationPriceRate;

    public Hospital(int serviceId, String ownerUid, String name, String logoPath
            , String picturePath, String facebookUrl, String openDays
            , String openTime, String closeTime, String tel, String address
            , int likes, double latitude, double longitude, String description
            , boolean isAcceptBigOperation, int checkupPriceRate
            , int vaccinePriceRate, int operationPriceRate) {
        super(serviceId, ownerUid, name, logoPath, picturePath, facebookUrl, openDays, openTime
                , closeTime, tel, address, likes, latitude, longitude, description);
        this.isAcceptBigOperation = isAcceptBigOperation;
        this.checkupPriceRate = checkupPriceRate;
        this.vaccinePriceRate = vaccinePriceRate;
        this.operationPriceRate = operationPriceRate;
    }

    public Hospital(int serviceId, String ownerUid, String name, String logoPath
            , String picturePath, String facebookUrl, String openDays
            , String openTime, String closeTime, String tel, String address
            , int likes, double latitude, double longitude, String description
            , int isAcceptBigOperation, int checkupPriceRate
            , int vaccinePriceRate, int operationPriceRate) {
        super(serviceId, ownerUid, name, logoPath, picturePath, facebookUrl, openDays, openTime
                , closeTime, tel, address, likes, latitude, longitude, description);
        this.isAcceptBigOperation = isAcceptBigOperation == 1;
        this.checkupPriceRate = checkupPriceRate;
        this.vaccinePriceRate = vaccinePriceRate;
        this.operationPriceRate = operationPriceRate;
    }

    public Hospital(Service service, int isAcceptBigOperation, int checkupPriceRate
            , int vaccinePriceRate, int operationPriceRate) {
        super(service.getServiceId(), service.getOwnerUid(), service.getName(), service.getLogoPath()
                , service.getPicturePath(), service.getFacebookUrl(), service.getOpenDays(), service.getOpenTime()
                , service.getCloseTime(), service.getTel(), service.getAddress(), service.getLikes()
                , service.getLatitude(), service.getLongitude(), service.getDescription());
        this.isAcceptBigOperation = isAcceptBigOperation == 1;
        this.checkupPriceRate = checkupPriceRate;
        this.vaccinePriceRate = vaccinePriceRate;
        this.operationPriceRate = operationPriceRate;
    }

    public boolean isAcceptBigOperation() {
        return isAcceptBigOperation;
    }

    public int getCheckupPriceRate() {
        return checkupPriceRate;
    }

    public int getVaccinePriceRate() {
        return vaccinePriceRate;
    }

    public int getOperationPriceRate() {
        return operationPriceRate;
    }

}
