package edu.floridapoly.mobiledeviceapps.spring20.getoutofit.activities;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import edu.floridapoly.mobiledeviceapps.spring20.getoutofit.BuildConfig;
import edu.floridapoly.mobiledeviceapps.spring20.getoutofit.R;
import edu.floridapoly.mobiledeviceapps.spring20.getoutofit.data.Converters;
import edu.floridapoly.mobiledeviceapps.spring20.getoutofit.data.DatabaseManager;
import edu.floridapoly.mobiledeviceapps.spring20.getoutofit.data.MessageDataEntry;
import edu.floridapoly.mobiledeviceapps.spring20.getoutofit.data.MessageDataViewModel;
import edu.floridapoly.mobiledeviceapps.spring20.getoutofit.data.TextAlarmEntry;
import edu.floridapoly.mobiledeviceapps.spring20.getoutofit.helpers.TextAlarmReceiver;
import edu.floridapoly.mobiledeviceapps.spring20.getoutofit.helpers.TextMessenger;

public class TextAlarmActivity extends AppCompatActivity {
    public static final String EXTRA_TEXT_ALARM_ID = "PASSED_TEXT_ALARM_ID";
    public static final String EXTRA_TEXT_ALARM_SENDER = "PASSED_TEXT_ALARM_SENDER";
    public static final String EXTRA_TEXT_ALARM_DATE = "PASSED_TEXT_ALARM_DATE";

    // Constant for default message id to be used when not in update mode
    public static final int DEFAULT_TEXT_ALARM_ID = -1;
    private static final String TAG = TextAlarmEntry.class.getSimpleName();
    private static final DateFormat dateTimeFormatter = new SimpleDateFormat("MM/dd/yyyy HH:mm");
    // date/time selection
    private static int DATE_DIALOG_ID = 999;
    private static int TIME_DIALOG_ID = 1000;
    private Calendar calendar;
    private int year, month, day, hour, minute;

    private Spinner mFrom;
    private EditText mSummary;
    private Spinner mSummarySpinner;
    private CheckBox mCreateMessage;
    private RadioGroup mMessageOptions;
    private TextView mDate;
    private TextView mTime;
    private EditText mMessage;

    // Extracted Strings from EditTexts
    private String fromText;
    private String summaryText;
    private Date extractedDate;
    private String messageText;

    private TableLayout mDateTimeTable;
    private Button mBottomButton;
    private boolean isInstantText;
    private boolean createNewMessage;

    private ArrayAdapter<MessageDataEntry> adapter;
    private int messageId;
    private MessageDataEntry selectedMessage;
    private TextAlarmEntry editedAlarm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_alarm);

        referenceObjects();
        setupListeners();

        Intent intent = getIntent();
        int textAlarmId = intent.getIntExtra(EXTRA_TEXT_ALARM_ID, DEFAULT_TEXT_ALARM_ID);
        messageId = intent.getIntExtra(CreateMessageActivity.EXTRA_MESSAGE_DATA_ID, CreateMessageActivity.DEFAULT_MESSAGE_ID);
        // Check for update id
        if (textAlarmId != DEFAULT_TEXT_ALARM_ID) {
            // Update text for button
            mBottomButton.setText(getString(R.string.update_text_alarm_btn));
            // Obtain the passed data
            String senderInfo = intent.getStringExtra(EXTRA_TEXT_ALARM_SENDER);
            Date date = new Date(intent.getLongExtra(EXTRA_TEXT_ALARM_DATE, Converters.DEFAULT_DATE.getTime()));
            String messageDataSummary = intent.getStringExtra(CreateMessageActivity.EXTRA_MESSAGE_DATA_SUMMARY);
            String messageDataMessage = intent.getStringExtra(CreateMessageActivity.EXTRA_MESSAGE_DATA_MESSAGE);
            boolean messageDataTemplate = intent.getBooleanExtra(CreateMessageActivity.EXTRA_MESSAGE_DATA_TEMPLATE, false);
            editedAlarm = new TextAlarmEntry(textAlarmId, date, senderInfo, new MessageDataEntry(messageId, messageDataSummary, messageDataMessage, messageDataTemplate));
            // Populate the UI
            populateUI();
        } else {
            if (BuildConfig.DEBUG) Log.d(TAG, "Failed to populate UI");
        }

        // Date/time selection
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        setDate(year, month, day);
        hour = calendar.get(Calendar.HOUR_OF_DAY);
        minute = calendar.get(Calendar.MINUTE) + 1;
        setTime(hour, minute);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == DATE_DIALOG_ID) {
            return new DatePickerDialog(this,
                    dateSetListener, year, month, day);
        }
        if (id == TIME_DIALOG_ID) {
            return new TimePickerDialog(this, timeSetListener, hour, minute, true);
        }
        return null;
    }

    private void referenceObjects() {
        // Get the objects
        mFrom = findViewById(R.id.ev_create_alarm_from);
        mSummary = findViewById(R.id.ev_create_alarm_summary);
        mSummarySpinner = findViewById(R.id.create_alarm_summary_spinner);
        mCreateMessage = findViewById(R.id.cb_create_alarm_new_message);
        mMessageOptions = findViewById(R.id.rg_message_options);
        mDate = findViewById(R.id.ev_create_alarm_date);
        mTime = findViewById(R.id.ev_create_alarm_time);
        mMessage = findViewById(R.id.ev_create_alarm_message);
        mDateTimeTable = findViewById(R.id.table_date_time);
        mBottomButton = findViewById(R.id.bt_create_alarm_main);
    }

    private void setupListeners() {
        mCreateMessage.setOnCheckedChangeListener((buttonView, isChecked) -> createNewMessage = isChecked);

        mMessageOptions.setOnCheckedChangeListener((group, radioButtonId) -> {
            // Use the radio button id to change how the ui looks
            isInstantText = radioButtonId == R.id.rb_instant_message;
            redrawUI();
        });

        // Obtain the Extra from Main Activity
        if (getIntent().hasExtra(MainActivity.EXTRA_INSTANT_MESSAGE)) {
            isInstantText = getIntent().getBooleanExtra(MainActivity.EXTRA_INSTANT_MESSAGE, false);
            // Make sure the Instant Message Radio Button is selected
            if (isInstantText) {
                mMessageOptions.check(R.id.rb_instant_message);
            } else {
                mMessageOptions.check(R.id.rb_delayed_message);
            }
            // UI is updated from mMessageOptions OnCheckChangedListener
        }

        // Change click functionality of button depending on radio button
        mBottomButton.setOnClickListener(v -> {
            if (isInstantText)
                sendInstantText();
            else
                saveTextAlarm();
        });

        // Populate spinner with message summaries
        MessageDataViewModel viewModel = new ViewModelProvider(this).get(MessageDataViewModel.class);
        viewModel.getEntries().observe(this, new Observer<List<MessageDataEntry>>() {
            @Override
            public void onChanged(List<MessageDataEntry> entries) {
                viewModel.getEntries().removeObserver(this);
                adapter = new ArrayAdapter<>(
                        TextAlarmActivity.this, android.R.layout.simple_spinner_item,
                        entries.toArray(new MessageDataEntry[0]));
                mSummarySpinner.setAdapter(adapter);
                // Enable spinner if User created messages
                mSummarySpinner.setEnabled(!adapter.isEmpty());
                // Create new message if there is no messages
                mCreateMessage.setChecked(adapter.isEmpty());

                // Select the appropriate message
                if (messageId != CreateMessageActivity.DEFAULT_MESSAGE_ID) {
                    int messagePos = -1;
                    // Find the proper position of the message id
                    for (int i = 0; i < entries.size(); i++) {
                        if (entries.get(i).getMessageId() == messageId) {
                            messagePos = i;
                            break;
                        }
                    }
                    if (messagePos != -1) {
                        mSummarySpinner.setSelection(messagePos);
                    }
                }
            }
        });

        // Populate message data when an item is selected from dropdown
        mSummarySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position,
                                       long id) {
                selectedMessage = (MessageDataEntry) parent.getItemAtPosition(position);
                mSummary.setText(selectedMessage.getSummary());
                mMessage.setText(selectedMessage.getMessage());
                mCreateMessage.setChecked(selectedMessage.isTemplate());
                if (selectedMessage.isTemplate())
                    mCreateMessage.setEnabled(false);
                else
                    mCreateMessage.setEnabled(true);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mDate.setOnClickListener(v -> {
            showDialog(DATE_DIALOG_ID);
        });

        mTime.setOnClickListener(v -> {
            showDialog(TIME_DIALOG_ID);
        });
    }

    private boolean stringExtractionFailed() {
        // Make sure all EditTexts are filled
        if (isAllDataEmpty()) return true;

        // Obtain the text from the boxes
        fromText = mFrom.getSelectedItem().toString();
        summaryText = mSummary.getText().toString();
        messageText = mMessage.getText().toString();

        //TODO: Use a DatePicker and TimePicker to allow user to select dateTime easily
        String dateTime = String.format("%s %s", mDate.getText().toString(),
                mTime.getText().toString());
        if (isInstantText) {
            extractedDate = new Date(System.currentTimeMillis());
        } else {
            try {
                extractedDate = dateTimeFormatter.parse(dateTime);
            } catch (ParseException | NullPointerException e) {
                if (BuildConfig.DEBUG) Log.e(TAG, "Exception when parsing data:\n", e);
                extractedDate = Converters.DEFAULT_DATE;
            }
        }
        return false;
    }

    private void sendInstantText() {
        // Make sure data was extracted
        if (stringExtractionFailed()) return;

        saveMessage();

        // TODO: Send Text message instantly
        TextMessenger messenger = new TextMessenger();
        messenger.sendText(fromText, messageText);
        finish();
    }

    private void saveMessage() {
        // Insert a new message or Update the selected message into proper table
        if (!createNewMessage && selectedMessage == null)
            selectedMessage = new MessageDataEntry(CreateMessageActivity.DEFAULT_MESSAGE_ID, summaryText, messageText, false);
        else if (createNewMessage && selectedMessage != null)
            selectedMessage = null;
        selectedMessage = DatabaseManager.getInstance(this).insertOrUpdateMessage(selectedMessage, summaryText, messageText, false);
    }

    private void saveTextAlarm() {
        // Make sure data was extracted
        if (stringExtractionFailed()) return;

        saveMessage();

        // Insert or update the textAlarm
        editedAlarm = DatabaseManager.getInstance(this).insertOrUpdateAlarm(editedAlarm, extractedDate, fromText, selectedMessage);

        // Send alarm at desired time
        TextAlarmReceiver receiver = new TextAlarmReceiver();
        receiver.setTextAlarm(this, extractedDate, editedAlarm);

        // Return to calling Activity
        finish();
    }

    private void redrawUI() {
        if (isInstantText) {
            mDateTimeTable.setVisibility(View.GONE);
            mBottomButton.setText(R.string.send_instant_message_btn);
        } else {
            mDateTimeTable.setVisibility(View.VISIBLE);
            mBottomButton.setText(R.string.save_text_alarm_btn);
        }
    }

    private boolean isAllDataEmpty() {
        String errorString = getString(R.string.generic_empty_error);
        boolean dataInAll = isTextViewFilled(mSummary, errorString);
        if (!isInstantText) {
            dataInAll &= isTextViewFilled(mDate, errorString);
            // No time field means 00:00 for HH:mm
        }

        dataInAll &= isTextViewFilled(mMessage, errorString);

        return !dataInAll;
    }

    private boolean isTextViewFilled(TextView textView, String errorString) {
        if (textView.getText().toString().isEmpty()) {
            textView.setError(errorString);
            return false;
        }
        return true;
    }

    private void populateUI() {
        // TODO: Select correct number when user edits a TextAlarmEntry
        // TODO: Show the mapped values from user defined contact to the available numbers
        //mFrom.setSelection(mFrom.getSelectedItemPosition());
        mSummary.setText(editedAlarm.getMessageData().getSummary());
        mMessage.setText(editedAlarm.getMessageData().getMessage());
        if (!isInstantText) {
            String dateTime = dateTimeFormatter.format(editedAlarm.getDateTime());
            String[] dateInfo = dateTime.split(" ");
            mDate.setText(dateInfo[0]);
            mTime.setText(dateInfo[1]);
        }
    }

    private DatePickerDialog.OnDateSetListener dateSetListener = (view, year, month, dayOfMonth) -> setDate(year, month, dayOfMonth);
    private TimePickerDialog.OnTimeSetListener timeSetListener = (view, hourOfDay, minute) -> setTime(hourOfDay, minute);

    private void setDate(int year, int month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;
        Date tempDate = new GregorianCalendar(year, month, day).getTime();
        mDate.setText(new SimpleDateFormat("MM/dd/yyyy").format(tempDate));
    }

    private void setTime(int hourOfDay, int minute) {
        this.hour = hourOfDay;
        this.minute = minute;
        Date tempDate = new GregorianCalendar(year, month, day, hourOfDay, minute).getTime();
        mTime.setText(new SimpleDateFormat("HH:mm").format(tempDate));
    }
}
