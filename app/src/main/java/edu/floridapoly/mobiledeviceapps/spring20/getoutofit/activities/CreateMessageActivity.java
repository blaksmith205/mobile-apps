package edu.floridapoly.mobiledeviceapps.spring20.getoutofit.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import edu.floridapoly.mobiledeviceapps.spring20.getoutofit.R;
import edu.floridapoly.mobiledeviceapps.spring20.getoutofit.data.DatabaseManager;
import edu.floridapoly.mobiledeviceapps.spring20.getoutofit.data.MessageDataEntry;
import edu.floridapoly.mobiledeviceapps.spring20.getoutofit.helpers.AppExecutors;

public class CreateMessageActivity extends AppCompatActivity {

    private EditText mSummary;
    private EditText mMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_message);

        mSummary = findViewById(R.id.ev_excuse_summary);
        mMessage = findViewById(R.id.ev_message);
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

        // Insert into database
        final MessageDataEntry dataEntry = new MessageDataEntry(summaryText, messageText);
        final DatabaseManager db = DatabaseManager.getInstance(this);
        AppExecutors.getInstance().diskIO().execute(() -> db.messageDataDao().insert(dataEntry));

        // Return to calling Activity
        finish();
    }
}
