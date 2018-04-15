package com.easypetsthailand.champ.easypets.Model.Service;

public class Hospital {

    private int serviceId;
    private boolean isAcceptedOperation;
    private int vaccinePriceRate;
    private int operationPriceRate;
    private int checkupPriceRate;

    public Hospital(int serviceId, boolean isAcceptedOperation, int vaccinePriceRate, int operationPriceRate, int checkupPriceRate) {
        this.serviceId = serviceId;
        this.isAcceptedOperation = isAcceptedOperation;
        this.vaccinePriceRate = vaccinePriceRate;
        this.operationPriceRate = operationPriceRate;
        this.checkupPriceRate = checkupPriceRate;
    }

    public int getServiceId() {
        return serviceId;
    }

    public boolean isAcceptedOperation() {
        return isAcceptedOperation;
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
