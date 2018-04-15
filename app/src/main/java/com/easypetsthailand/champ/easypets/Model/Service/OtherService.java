package com.easypetsthailand.champ.easypets.Model.Service;

public class OtherService {

    private int serviceId;
    private String serviceDetails;
    private String servicePrice;

    public OtherService(int serviceId, String serviceDetails, String servicePrice) {
        this.serviceId = serviceId;
        this.serviceDetails = serviceDetails;
        this.servicePrice = servicePrice;
    }

    public int getServiceId() {
        return serviceId;
    }

    public String getServiceDetails() {
        return serviceDetails;
    }

    public String getServicePrice() {
        return servicePrice;
    }

}
