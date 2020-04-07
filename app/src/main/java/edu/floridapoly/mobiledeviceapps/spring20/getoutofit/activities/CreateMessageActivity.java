package edu.floridapoly.mobiledeviceapps.spring20.getoutofit.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import edu.floridapoly.mobiledeviceapps.spring20.getoutofit.R;
import edu.floridapoly.mobiledeviceapps.spring20.getoutofit.data.DatabaseManager;
import edu.floridapoly.mobiledeviceapps.spring20.getoutofit.data.MessageDataEntry;

public class CreateMessageActivity extends AppCompatActivity {
    public static final String EXTRA_MESSAGE_DATA_ID = "PASSED_MESSAGE_DATA_ID";
    public static final String EXTRA_MESSAGE_DATA_SUMMARY = "PASSED_MESSAGE_DATA_SUMMARY";
    public static final String EXTRA_MESSAGE_DATA_MESSAGE = "PASSED_MESSAGE_DATA_MESSAGE";

    // Constant for default message id to be used when not in update mode
    public static final int DEFAULT_MESSAGE_ID = -1;

    private EditText mSummary;
    private EditText mMessage;
    private MessageDataEntry messageData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_message);

        mSummary = findViewById(R.id.ev_excuse_summary);
        mMessage = findViewById(R.id.ev_message);
        Button button = findViewById(R.id.bt_save_message);

        Intent intent = getIntent();
        if (intent == null) {
            return;
        }
        // Get message id if one exists
        int mMessageId = intent.getIntExtra(EXTRA_MESSAGE_DATA_ID, DEFAULT_MESSAGE_ID);

        // Check for update id
        if (mMessageId != DEFAULT_MESSAGE_ID) {
            button.setText(getString(R.string.update_message_btn));
            // Obtain the data
            String summary = intent.getStringExtra(EXTRA_MESSAGE_DATA_SUMMARY);
            String message = intent.getStringExtra(EXTRA_MESSAGE_DATA_MESSAGE);
            messageData = new MessageDataEntry(mMessageId, summary, message);
            // Populate the UI
            populateUI();
        }
    }

    public void saveMessageButton(View view) {
        // Obtain the text from the boxes
        String summaryText = mSummary.getText().toString();
        String messageText = mMessage.getText().toString();

        if (messageText.isEmpty()) {
            mMessage.setError(getString(R.string.message_empty_error));
            return;
        }
        if (summaryText.isEmpty()) {
            mSummary.setError(getString(R.string.summary_empty_error));
            return;
        }

        // Insert or Update the message
        DatabaseManager.getInstance(this).insertOrUpdateMessage(messageData, summaryText, messageText);

        // Return to calling Activity
        finish();
    }

    private void populateUI() {
        mSummary.setText(messageData.getSummary());
        mMessage.setText(messageData.getMessage());
    }
}
