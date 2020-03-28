package edu.floridapoly.mobiledeviceapps.spring20.getoutofit.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import edu.floridapoly.mobiledeviceapps.spring20.getoutofit.BuildConfig;
import edu.floridapoly.mobiledeviceapps.spring20.getoutofit.R;
import edu.floridapoly.mobiledeviceapps.spring20.getoutofit.data.Converters;
import edu.floridapoly.mobiledeviceapps.spring20.getoutofit.data.DatabaseManager;
import edu.floridapoly.mobiledeviceapps.spring20.getoutofit.data.MessageDataEntry;
import edu.floridapoly.mobiledeviceapps.spring20.getoutofit.data.MessageDataViewModel;
import edu.floridapoly.mobiledeviceapps.spring20.getoutofit.data.TextAlarmEntry;
import edu.floridapoly.mobiledeviceapps.spring20.getoutofit.helpers.AppExecutors;

public class TextAlarmActivity extends AppCompatActivity {
    private static final String TAG = TextAlarmEntry.class.getSimpleName();
    private static final DateFormat dateTimeFormatter = new SimpleDateFormat("MM/dd/yyyy HH:mm");

    private EditText mFrom;
    private EditText mSummary;
    private Spinner mSummarySpinner;
    private EditText mDate;
    private EditText mTime;
    private EditText mMessage;

    private TableLayout mDateTimeTable;
    private Button mBottomButton;
    private boolean isInstantText;

    private MessageDataEntry selectedMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_alarm);

        // Get the objects
        mDateTimeTable = findViewById(R.id.table_date_time);
        mBottomButton = findViewById(R.id.bt_create_alarm_main);
        mFrom = findViewById(R.id.ev_create_alarm_from);
        mSummarySpinner = findViewById(R.id.create_alarm_summary_spinner);
        mSummary = findViewById(R.id.ev_create_alarm_summary);
        mDate = findViewById(R.id.ev_create_alarm_date);
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
            isInstantText = getIntent().getBooleanExtra(MainActivity.EXTRA_INSTANT_MESSAGE, false);
            // Make sure the Instant Message Radio Button is selected
            if (isInstantText) {
                messageOptions.check(R.id.rb_instant_message);
            } else {
                messageOptions.check(R.id.rb_delayed_message);
            }
            // UI is updated from OnCheckChangedListener
        }

        // Populate spinner with message summaries
        MessageDataViewModel viewModel = new ViewModelProvider(this).get(MessageDataViewModel.class);
        viewModel.getEntries().observe(this, new Observer<List<MessageDataEntry>>() {
            @Override
            public void onChanged(List<MessageDataEntry> entries) {
                viewModel.getEntries().removeObserver(this);
                ArrayAdapter<MessageDataEntry> adapter = new ArrayAdapter<MessageDataEntry>(
                        TextAlarmActivity.this, android.R.layout.simple_spinner_item,
                        entries.toArray(new MessageDataEntry[0]));
                mSummarySpinner.setAdapter(adapter);

                // Enable spinner if User created messages
                if (adapter.isEmpty())
                    mSummarySpinner.setEnabled(false);
                else
                    mSummarySpinner.setEnabled(true);
            }
        });

        // Populate message data when an item is selected from dropdown
        mSummarySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedMessage = (MessageDataEntry) parent.getItemAtPosition(position);
                mSummary.setText(selectedMessage.getSummary());
                mMessage.setText(selectedMessage.getMessage());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void onButtonClicked(View view) {
        // Make sure all EditTexts are filled
        if (isAllDataEmpty()) return;

        // Obtain the text from the boxes
        String fromText = mFrom.getText().toString();
        String summaryText = mSummary.getText().toString();
        String dateTime = String.format("%s %s", mDate.getText().toString(),
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

        // Insert a new message or Update the selected message into proper table
        selectedMessage = DatabaseManager.getInstance(this).insertOrUpdateMessage(selectedMessage, summaryText, messageText);

        // Insert alarm into database
        TextAlarmEntry alarm = new TextAlarmEntry(date, fromText, selectedMessage);
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

    private boolean isAllDataEmpty() {
        String errorString = getString(R.string.generic_empty_error);
        boolean dataInAll = isTextViewFilled(mFrom, errorString);
        dataInAll = isTextViewFilled(mSummary, errorString);
        if (!isInstantText) {
            dataInAll = isTextViewFilled(mDate, errorString);
            // No time field means 00:00 for HH:mm
        }

        dataInAll = isTextViewFilled(mMessage, errorString);

        return !dataInAll;
    }

    private boolean isTextViewFilled(TextView textView, String errorString) {
        if (textView.getText().toString().isEmpty()) {
            textView.setError(errorString);
            return false;
        }
        return true;
    }
}
