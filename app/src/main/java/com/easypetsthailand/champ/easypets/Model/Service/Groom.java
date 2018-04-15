package com.easypetsthailand.champ.easypets.Model.Service;

import android.support.annotation.Nullable;

public class Groom {

    private int serviceId;
    private int groomingPriceRate;

    public Groom(int serviceId, int groomingPriceRate) {
        this.serviceId = serviceId;
        this.groomingPriceRate = groomingPriceRate;
    }

    public int getServiceId() {
        return serviceId;
    }

    public int getGroomingPriceRate() {
        return groomingPriceRate;
    }

}
