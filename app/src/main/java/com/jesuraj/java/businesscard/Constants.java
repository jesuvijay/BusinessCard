package com.jesuraj.java.businesscard;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Constants {

    public static String getDateTime(){
            return new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.US).format(new Date());
    }
}
