package com.easypetsthailand.champ.easypets.Model.Service;

public class Hotel {

    private int serviceId;
    private boolean isAcceptOvernight;
    private int vaccinePriceRate;
    private int operationPriceRate;
    private int checkupPriceRate;

    public Hotel(int serviceId, boolean isAcceptOvernight, int vaccinePriceRate, int operationPriceRate, int checkupPriceRate) {
        this.serviceId = serviceId;
        this.isAcceptOvernight = isAcceptOvernight;
        this.vaccinePriceRate = vaccinePriceRate;
        this.operationPriceRate = operationPriceRate;
        this.checkupPriceRate = checkupPriceRate;
    }

    public int getServiceId() {
        return serviceId;
    }

    public boolean isAcceptOvernight() {
        return isAcceptOvernight;
    }

    public int getVaccinePriceRate() {
        return vaccinePriceRate;
    }

    public int getOperationPriceRate() {
        return operationPriceRate;
    }

    public int getCheckupPriceRate() {
        return checkupPriceRate;
    }

}
