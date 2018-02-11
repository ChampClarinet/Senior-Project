package com.easypetsthailand.champ.easypets.Model;

import java.io.Serializable;

public class Filter implements Serializable {

    private Boolean isOpen;
    private Boolean isLowPriceRateEnabled;
    private Boolean isMidPriceRateEnabled;
    private Boolean isHighPriceRateEnabled;

    public Filter() {
        reset();
    }

    @Override
    public String toString() {
        String s = "Filtering on ";
        int i = s.length();
        if (isOpen != null) s += "isOpen = " + isOpen + " ";
        if (isLowPriceRateEnabled != null) s += "low = " + isLowPriceRateEnabled + " ";
        if (isMidPriceRateEnabled != null) s += "mid = " + isMidPriceRateEnabled + " ";
        if (isHighPriceRateEnabled != null) s += "high = " + isHighPriceRateEnabled + " ";
        if (i == s.length()) s += "nothing";
        return s;
    }

    public boolean isUnset() {
        return isOpen == null && isLowPriceRateEnabled == null && isMidPriceRateEnabled == null && isHighPriceRateEnabled == null;
    }

    public void reset() {
        isOpen = null;
        isLowPriceRateEnabled = null;
        isMidPriceRateEnabled = null;
        isHighPriceRateEnabled = null;
    }

    public Boolean getOpen() {
        return isOpen;
    }

    public void setOpen(Boolean open) {
        if(open) this.isOpen = true;
        else isOpen = null;
    }

    public Boolean getLowPriceRateEnabled() {
        return isLowPriceRateEnabled;
    }

    public void setLowPriceRateEnabled(Boolean lowPriceRateEnabled) {
        isLowPriceRateEnabled = lowPriceRateEnabled;
    }

    public Boolean getMidPriceRateEnabled() {
        return isMidPriceRateEnabled;
    }

    public void setMidPriceRateEnabled(Boolean midPriceRateEnabled) {
        isMidPriceRateEnabled = midPriceRateEnabled;
    }

    public Boolean getHighPriceRateEnabled() {
        return isHighPriceRateEnabled;
    }

    public void setHighPriceRateEnabled(Boolean highPriceRateEnabled) {
        isHighPriceRateEnabled = highPriceRateEnabled;
    }

    public String checkAllStatus() {
        String s = "Filter stats ";
        s += "isOpen = " + isOpen + " ";
        s += "low = " + isLowPriceRateEnabled + " ";
        s += "mid = " + isMidPriceRateEnabled + " ";
        s += "high = " + isHighPriceRateEnabled + " ";
        return s;
    }

}
