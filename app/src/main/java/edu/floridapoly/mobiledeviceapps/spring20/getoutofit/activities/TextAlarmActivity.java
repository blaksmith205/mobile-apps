package edu.floridapoly.mobiledeviceapps.spring20.getoutofit.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TableLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import edu.floridapoly.mobiledeviceapps.spring20.getoutofit.R;

public class TextAlarmActivity extends AppCompatActivity {

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
        Toast.makeText(this, "Extract message and date/time and save to DB. " +
                "Create a text message to be sent at desired time", Toast.LENGTH_LONG).show();
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
