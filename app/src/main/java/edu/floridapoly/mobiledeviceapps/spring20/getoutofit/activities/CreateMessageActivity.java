package edu.floridapoly.mobiledeviceapps.spring20.getoutofit.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;

import edu.floridapoly.mobiledeviceapps.spring20.getoutofit.R;
import edu.floridapoly.mobiledeviceapps.spring20.getoutofit.data.DatabaseManager;
import edu.floridapoly.mobiledeviceapps.spring20.getoutofit.data.MessageDataEntry;

public class CreateMessageActivity extends AppCompatActivity {
    public static final String EXTRA_MESSAGE_DATA_ID = "PASSED_MESSAGE_DATA_ID";
    public static final String EXTRA_MESSAGE_DATA_SUMMARY = "PASSED_MESSAGE_DATA_SUMMARY";
    public static final String EXTRA_MESSAGE_DATA_MESSAGE = "PASSED_MESSAGE_DATA_MESSAGE";
    public static final String EXTRA_MESSAGE_DATA_TEMPLATE = "PASSED_MESSAGE_DATA_TEMPLATE";

    // Constant for default message id to be used when not in update mode
    public static final int DEFAULT_MESSAGE_ID = -1;

    private EditText mSummary;
    private EditText mMessage;
    private CheckBox mTemplateCB;
    private RadioGroup mTemplateOptions;
    private int mMessageId;
    private boolean isTemplate;
    private boolean createMessageFromTemplate;
    private boolean updateMessage;
    private MessageDataEntry messageData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_message);

        mSummary = findViewById(R.id.ev_excuse_summary);
        mMessage = findViewById(R.id.ev_message);
        Button button = findViewById(R.id.bt_save_message);

        // Setup listener for CheckBox
        mTemplateCB = findViewById(R.id.cb_create_template_message);
        mTemplateCB.setOnCheckedChangeListener((buttonView, isChecked) -> {
            isTemplate = isChecked;
        });

        // Setup radioGroup
        mTemplateOptions = findViewById(R.id.rg_template_options);
        mTemplateOptions.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case R.id.rb_new_message_from_template:
                    createMessageFromTemplate = false;
                    button.setText(getString(R.string.save_message_btn));
                    break;
                case R.id.rb_update_template:
                    createMessageFromTemplate = true;
                    button.setText(getString(R.string.update_template_rb));
                    break;
            }
        });

        Intent intent = getIntent();
        if (intent == null) {
            return;
        }
        // Get message id if one exists
        mMessageId = intent.getIntExtra(EXTRA_MESSAGE_DATA_ID, DEFAULT_MESSAGE_ID);

        // Check for update id
        if (mMessageId != DEFAULT_MESSAGE_ID) {
            button.setText(getString(R.string.update_message_btn));
            // Obtain the data
            String summary = intent.getStringExtra(EXTRA_MESSAGE_DATA_SUMMARY);
            String message = intent.getStringExtra(EXTRA_MESSAGE_DATA_MESSAGE);
            boolean template = intent.getBooleanExtra(EXTRA_MESSAGE_DATA_TEMPLATE, false);
            updateMessage =  !template;
            // isTemplate gets updated through populateUI
            messageData = new MessageDataEntry(mMessageId, summary, message, template);
            // Populate the UI
            populateUI();
        } else {
            // Hide the radiogroup
            mTemplateOptions.setVisibility(View.GONE);
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

        if (!updateMessage) {
            // Create a non-template message from a template. This means a template was selected to edit
            if (!createMessageFromTemplate && isTemplate && mMessageId != DEFAULT_MESSAGE_ID) {
                messageData = null;
                isTemplate = false;
            }
        }

        // Insert or Update the message
        DatabaseManager.getInstance(this).insertOrUpdateMessage(messageData, summaryText, messageText, isTemplate);

        // Return to calling Activity
        finish();
    }

    private void populateUI() {
        if (messageData == null)
            return;
        if (messageData.isTemplate()) {
            // Hide the template creating checkbox
            mTemplateCB.setVisibility(View.GONE);
            // Select Create new message option
            mTemplateOptions.check(R.id.rb_new_message_from_template);
        } else {
            mTemplateOptions.setVisibility(View.GONE);
        }
        mSummary.setText(messageData.getSummary());
        mMessage.setText(messageData.getMessage());
        mTemplateCB.setChecked(messageData.isTemplate());
    }
}
