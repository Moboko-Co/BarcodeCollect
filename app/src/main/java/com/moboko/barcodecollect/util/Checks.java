package com.moboko.barcodecollect.util;

public class Checks {
    public boolean isNumber(String num) {
        try {
            Integer.parseInt(num);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public boolean isNull(String str) {
        if (str == null || str.isEmpty()) {
            return true;
        } else {
            return false;
        }
    }
}
