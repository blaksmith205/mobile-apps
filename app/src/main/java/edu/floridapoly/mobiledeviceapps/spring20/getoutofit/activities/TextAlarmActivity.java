package edu.floridapoly.mobiledeviceapps.spring20.getoutofit.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TableLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import edu.floridapoly.mobiledeviceapps.spring20.getoutofit.BuildConfig;
import edu.floridapoly.mobiledeviceapps.spring20.getoutofit.R;
import edu.floridapoly.mobiledeviceapps.spring20.getoutofit.data.Converters;
import edu.floridapoly.mobiledeviceapps.spring20.getoutofit.data.DatabaseManager;
import edu.floridapoly.mobiledeviceapps.spring20.getoutofit.data.MessageDataEntry;
import edu.floridapoly.mobiledeviceapps.spring20.getoutofit.data.TextAlarmEntry;
import edu.floridapoly.mobiledeviceapps.spring20.getoutofit.helpers.AppExecutors;

public class TextAlarmActivity extends AppCompatActivity {
    private static final String TAG = TextAlarmEntry.class.getSimpleName();
    private static final DateFormat dateTimeFormatter = new SimpleDateFormat("MM/dd/yyyy HH:mm");

    private EditText mFrom;
    private EditText mSummary;
    private EditText mData;
    private EditText mTime;
    private EditText mMessage;

    private TableLayout mDateTimeTable;
    private Button mBottomButton;
    private boolean isInstantText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_alarm);

        // Get the objects
        mDateTimeTable = findViewById(R.id.table_date_time);
        mBottomButton = findViewById(R.id.bt_create_alarm_main);
        mFrom = findViewById(R.id.ev_create_alarm_from);
        mSummary = findViewById(R.id.ev_create_alarm_summary);
        mData = findViewById(R.id.ev_create_alarm_date);
        mTime = findViewById(R.id.ev_create_alarm_time);
        mMessage = findViewById(R.id.ev_create_alarm_message);

        RadioGroup messageOptions = findViewById(R.id.rg_message_options);
        messageOptions.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int radioButtonId) {
                // Use the radio button id to change how the ui looks
                isInstantText = radioButtonId == R.id.rb_instant_message;
                redrawUI();
            }
        });

        // Obtain the Extra from Main Activity
        if (getIntent().hasExtra(MainActivity.EXTRA_INSTANT_MESSAGE)) {
            boolean isInstantText = getIntent().getBooleanExtra(MainActivity.EXTRA_INSTANT_MESSAGE, false);
            // Make sure the Instant Message Radio Button is selected
            if (isInstantText) {
                messageOptions.check(R.id.rb_instant_message);
            } else {
                messageOptions.check(R.id.rb_delayed_message);
            }
            // UI is updated from OnCheckChangedListener
        }

        // Change click functionality of button depending on radio button
        mBottomButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isInstantText)
                    sendInstantText();
                else
                    saveTextAlarm();
            }
        });
    }

    private void sendInstantText() {
        Toast.makeText(this, "Extract message and send instantly", Toast.LENGTH_SHORT).show();
    }

    private void saveTextAlarm() {
        // Obtain the text from the boxes
        // TODO: Check if the strings are empty or not
        String fromText = mFrom.getText().toString();
        String summaryText = mSummary.getText().toString();
        String dateTime = String.format("%s %s", mData.getText().toString(),
                mTime.getText().toString());
        String messageText = mMessage.getText().toString();

        Date date = Converters.DEFAULT_DATE;
        if (isInstantText) {
            date = new Date(System.currentTimeMillis());
        } else {
            try {
                date = dateTimeFormatter.parse(dateTime);
            } catch (ParseException | NullPointerException e) {
                if (BuildConfig.DEBUG) Log.e(TAG, "Exception when parsing data:\n", e);
            }
        }

        // TODO: Edit MessageDataEntry if user edited a previous message
        // TODO: Save a newly created MessageDataEntry if user created a new message
        MessageDataEntry data = new MessageDataEntry(summaryText, messageText);
        // Insert into database
        TextAlarmEntry alarm = new TextAlarmEntry(date, fromText, data);
        final DatabaseManager db = DatabaseManager.getInstance(this);
        AppExecutors.getInstance().diskIO().execute(() -> db.textAlarmDao().insert(alarm));

        // Return to calling Activity
        finish();
    }

    private void redrawUI() {
        if (isInstantText) {
            mDateTimeTable.setVisibility(View.GONE);
            mBottomButton.setText(R.string.send_instant_message_btn);
        } else {
            mDateTimeTable.setVisibility(View.VISIBLE);
            mBottomButton.setText(R.string.save_message_btn);
        }
    }
}
