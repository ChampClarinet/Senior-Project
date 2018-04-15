package com.easypetsthailand.champ.easypets.Model;

import com.easypetsthailand.champ.easypets.Model.Service.Service;

import java.util.ArrayList;

public class MockService {

    private ArrayList<Service> mock = new ArrayList<>();

    public MockService(){
        addNewService(1, "test1", "1000001",
                "10:00",
                "20:00",
                200,
                13.7424095,
                100.2432542);

        addNewService(2, "test2", "1111111",
                "09:00",
                "18:00",
                159,
                13.7424095,
                100.2432542);

        addNewService(3, "test3", "0111111",
                null,
                null,
                244,
                13.7424095,
                100.2432542);
    }

    public ArrayList<Service> getMockService() {
        return mock;
    }

    private void addNewService(int id, String name, String openDays, String openTime,
                               String closeTime, int likes, double latitude, double longitude){
        Service newService = new Service(id, name, null, null, openDays, openTime,
                closeTime, null, likes, latitude, longitude, null);
        mock.add(newService);
    }

}
