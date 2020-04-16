package edu.floridapoly.mobiledeviceapps.spring20.getoutofit.helpers;

import android.os.AsyncTask;
import android.util.Log;

import okhttp3.Credentials;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.Response;

public class TextMessenger {
    private static final String ACCOUNT_SID = "REPLACE WITH TWILIO SID";
    private static final String AUTH_TOKEN = "REPLACE WITH TWILIO AUTH";
    private static final String TAG = TextMessenger.class.getSimpleName();


    /**
     * --DISABLED-- Free trial of sending/receiving expires 4/28. Also my number is the only one verified atm
     * Sends a text Message to the user. Uses Twilio's api.
     *
     * @param from    The from number. Should be obtained from list of usable numbers
     * @param message The message to send to the user
     */
    public void sendText(String from, String message) {
        // Make sure the sid is valid
        if (ACCOUNT_SID.equals("REPLACE WITH TWILIO SID")) {
            Log.e(TAG, String.format("Unable to send text:\n%s\n%s\n%s", "ACCOUNT_SID is not valid. Needs to be replaced with the actual Twilio SID number", "SID Number looks like: ACXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX", "DO NOT COMMIT THIS TO GIT"));
            return;
        }
        // Make sure the token is valid
        if (AUTH_TOKEN.equals("REPLACE WITH TWILIO AUTH")) {
            Log.e(TAG, String.format("Unable to send text:\n%s\n%s\n%s", "AUTH_TOKEN is not valid. Needs to be replaced with the actual Twilio auth token", "Auth Token looks like: XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX", "DO NOT COMMIT THIS TO GIT"));
            return;
        }
        //new TextSenderAsync(ACCOUNT_SID, AUTH_TOKEN, from, message).execute();
    }

    private static class TextSenderAsync extends AsyncTask<Void, Void, Void> {
        private final String TAG = TextSenderAsync.class.getSimpleName();
        private String sid, auth, from, message;

        public TextSenderAsync(String sid, String auth, String from, String message) {
            this.sid = sid;
            this.auth = auth;
            this.from = from;
            this.message = message;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();
                clientBuilder.authenticator((route, response) -> {
                    if (responseCount(response) >= 3) {
                        return null; // If we've failed 3 times, give up. - in real life, never give up!!
                    }
                    String credential = Credentials.basic(sid, auth);
                    return response.request().newBuilder().header("Authorization", credential).build();
                });
                OkHttpClient client = clientBuilder.build();
                RequestBody body = new FormBody.Builder()
                        .addEncoded("Body", message)
                        .addEncoded("From", from)
                        .addEncoded("To", "+12072843760")
                        .build();
                Log.d(TAG, "bodyMsg:" + body.toString());
                okhttp3.Request request = new okhttp3.Request.Builder()
                        .header("Content-Type", "application/x-www-form-urlencoded")
                        .url(String.format("https://api.twilio.com/2010-04-01/Accounts/%s/Messages.json", sid))
                        .post(body)
                        .build();

                client.newCall(request).execute();
            } catch (Exception ex) {
                String err = String.format("{\"result\":\"false\",\"error\":\"%s\"}", ex.getMessage());
                Log.e(TAG, "sendText failed:", ex);
            }
            return null;
        }

        private int responseCount(Response response) {
            int result = 1;
            while ((response = response.priorResponse()) != null) {
                result++;
            }
            return result;
        }
    }
}
