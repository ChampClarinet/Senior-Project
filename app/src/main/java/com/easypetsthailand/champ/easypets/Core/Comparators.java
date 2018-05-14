package com.easypetsthailand.champ.easypets.Core;

import com.easypetsthailand.champ.easypets.Model.Groom;
import com.easypetsthailand.champ.easypets.Model.Hospital;
import com.easypetsthailand.champ.easypets.Model.Hotel;
import com.easypetsthailand.champ.easypets.Model.Service;

import java.util.Comparator;

import static com.easypetsthailand.champ.easypets.Core.Utils.calculateDistance;

public class Comparators {

    public static final Comparator<Service> distanceComparator = new Comparator<Service>() {
        @Override
        public int compare(Service o1, Service o2) {
            Double d1 = calculateDistance(o1.getLatitude(), o1.getLongitude());
            Double d2 = calculateDistance(o2.getLatitude(), o2.getLongitude());
            return d1.compareTo(d2);
        }
    };

    public static final Comparator<Service> popularityComparator = new Comparator<Service>() {
        @Override
        public int compare(Service o1, Service o2) {
            return o2.getLikes() - o1.getLikes();
        }
    };

    public static final Comparator<Service> nameComparator = new Comparator<Service>() {
        @Override
        public int compare(Service o1, Service o2) {
            return o1.getName().compareTo(o2.getName());
        }
    };

    public static final Comparator<Groom> groomPriceComparator = new Comparator<Groom>() {
        @Override
        public int compare(Groom o1, Groom o2) {
            return o1.getPriceRate() - o2.getPriceRate();
        }
    };

    public static final Comparator<Hotel> hotelPriceComparator = new Comparator<Hotel>() {
        @Override
        public int compare(Hotel o1, Hotel o2) {
            return o1.getHotelPrice() - o2.getHotelPrice();
        }
    };

    public static final Comparator<Hospital> checkupComparator = new Comparator<Hospital>() {
        @Override
        public int compare(Hospital o1, Hospital o2) {
            return o1.getCheckupPriceRate() - o2.getCheckupPriceRate();
        }
    };

    public static final Comparator<Hospital> vaccineComparator = new Comparator<Hospital>() {
        @Override
        public int compare(Hospital o1, Hospital o2) {
            return o1.getVaccinePriceRate() - o2.getVaccinePriceRate();
        }
    };

    public static final Comparator<Hospital> operationComparator = new Comparator<Hospital>() {
        @Override
        public int compare(Hospital o1, Hospital o2) {
            return o1.getOperationPriceRate() - o2.getOperationPriceRate();
        }
    };

}
