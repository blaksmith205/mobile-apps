package edu.floridapoly.mobiledeviceapps.spring20.getoutofit.helpers;

import android.telephony.SmsManager;

import java.util.ArrayList;

public class TextMessenger {

    public static void sendText(String sender, String message){
        SmsManager smsManager = SmsManager.getDefault();
        ArrayList msgparts = smsManager.divideMessage(message);

        smsManager.sendMultipartTextMessage(sender, null, msgparts, null, null);
    }
}
