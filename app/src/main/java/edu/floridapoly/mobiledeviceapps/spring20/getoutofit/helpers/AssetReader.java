package edu.floridapoly.mobiledeviceapps.spring20.getoutofit.helpers;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import edu.floridapoly.mobiledeviceapps.spring20.getoutofit.BuildConfig;
import edu.floridapoly.mobiledeviceapps.spring20.getoutofit.data.MessageDataEntry;

public class AssetReader {
    private static final String TAG = AssetManager.class.getSimpleName();

    public static List<MessageDataEntry> getDefaultMessages(Context context) {
        AssetManager assetManager = context.getAssets();
        ArrayList<MessageDataEntry> messages = new ArrayList<>();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(assetManager.open("default_messages.csv"), StandardCharsets.UTF_8));
            // Read until end of line
            String line;
            String[] cols = new String[4];
            // Skip the header
            reader.readLine();
            while ((line = reader.readLine()) != null) {
                cols = line.split(",");
                // Extract the data from the line
                int messageID = Integer.parseInt(cols[0]);
                String summary = cols[1];
                String message = cols[2];
                boolean isTemplate = Boolean.parseBoolean(cols[3]);
                // Create the message and store in the list
                MessageDataEntry data = new MessageDataEntry(messageID, summary, message, isTemplate);
                messages.add(data);
            }
        } catch (IOException e) {
            //log the exception
            if (BuildConfig.DEBUG) Log.d(TAG, "Unable to open default_messages \n", e);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    if (BuildConfig.DEBUG)
                        Log.d(TAG, "Problem occurred when trying to close the buffered reader.\n", e);
                }
            }
        }

        return messages;
    }
}
