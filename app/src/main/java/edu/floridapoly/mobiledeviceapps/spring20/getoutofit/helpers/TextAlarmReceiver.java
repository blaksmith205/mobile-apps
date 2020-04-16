package edu.floridapoly.mobiledeviceapps.spring20.getoutofit.helpers;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import java.util.Date;

import edu.floridapoly.mobiledeviceapps.spring20.getoutofit.BuildConfig;
import edu.floridapoly.mobiledeviceapps.spring20.getoutofit.activities.CreateMessageActivity;
import edu.floridapoly.mobiledeviceapps.spring20.getoutofit.activities.TextAlarmActivity;
import edu.floridapoly.mobiledeviceapps.spring20.getoutofit.data.Converters;
import edu.floridapoly.mobiledeviceapps.spring20.getoutofit.data.MessageDataEntry;
import edu.floridapoly.mobiledeviceapps.spring20.getoutofit.data.TextAlarmEntry;

public class TextAlarmReceiver extends BroadcastReceiver {
    private static final int SEND_DELAYED_TEXT = 527;
    private static final String TAG = TextAlarmReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        if (!intent.hasExtra(TextAlarmActivity.EXTRA_TEXT_ALARM_ID)) return;
        // Obtain the passed information
        int textAlarmId = intent.getIntExtra(TextAlarmActivity.EXTRA_TEXT_ALARM_ID, TextAlarmActivity.DEFAULT_TEXT_ALARM_ID);
        int messageId = intent.getIntExtra(CreateMessageActivity.EXTRA_MESSAGE_DATA_ID, CreateMessageActivity.DEFAULT_MESSAGE_ID);
        String senderInfo = intent.getStringExtra(TextAlarmActivity.EXTRA_TEXT_ALARM_SENDER);
        Date date = new Date(intent.getLongExtra(TextAlarmActivity.EXTRA_TEXT_ALARM_DATE, Converters.DEFAULT_DATE.getTime()));
        String messageDataSummary = intent.getStringExtra(CreateMessageActivity.EXTRA_MESSAGE_DATA_SUMMARY);
        String messageDataMessage = intent.getStringExtra(CreateMessageActivity.EXTRA_MESSAGE_DATA_MESSAGE);
        boolean messageDataTemplate = intent.getBooleanExtra(CreateMessageActivity.EXTRA_MESSAGE_DATA_TEMPLATE, false);
        TextAlarmEntry textAlarm = new TextAlarmEntry(textAlarmId, date, senderInfo, new MessageDataEntry(messageId, messageDataSummary, messageDataMessage, messageDataTemplate));

        // Send the text
        TextMessenger messenger = new TextMessenger();
        //messenger.sendText(senderInfo, textAlarm.getMessageData().getMessage());
        Toast.makeText(context, "Text Message Sent!", Toast.LENGTH_SHORT).show();
    }

    public void setTextAlarm(Context context, Date runOnDate, TextAlarmEntry entry) {
        try {
            AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

            Intent intent = new Intent(context, TextAlarmReceiver.class);
            intent.putExtra(TextAlarmActivity.EXTRA_TEXT_ALARM_ID, entry.getAlarmId());
            intent.putExtra(TextAlarmActivity.EXTRA_TEXT_ALARM_SENDER, entry.getSenderInfo());
            intent.putExtra(TextAlarmActivity.EXTRA_TEXT_ALARM_DATE, entry.getDateTime().getTime());
            intent.putExtra(CreateMessageActivity.EXTRA_MESSAGE_DATA_ID, entry.getMessageData().getMessageId());
            intent.putExtra(CreateMessageActivity.EXTRA_MESSAGE_DATA_SUMMARY, entry.getMessageData().getSummary());
            intent.putExtra(CreateMessageActivity.EXTRA_MESSAGE_DATA_MESSAGE, entry.getMessageData().getMessage());
            intent.putExtra(CreateMessageActivity.EXTRA_MESSAGE_DATA_TEMPLATE, entry.getMessageData().isTemplate());
            PendingIntent sender = PendingIntent.getBroadcast(context, SEND_DELAYED_TEXT, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            am.set(AlarmManager.RTC_WAKEUP, runOnDate.getTime(), sender);
        } catch (NullPointerException e) {
            if (BuildConfig.DEBUG) Log.e(TAG, "Failed to set text alarm...", e);
            Toast.makeText(context, "Failed to set text alarm", Toast.LENGTH_SHORT).show();
        }
    }
}
