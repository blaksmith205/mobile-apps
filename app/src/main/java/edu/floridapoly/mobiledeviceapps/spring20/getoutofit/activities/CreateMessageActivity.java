package edu.floridapoly.mobiledeviceapps.spring20.getoutofit.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import edu.floridapoly.mobiledeviceapps.spring20.getoutofit.R;
import edu.floridapoly.mobiledeviceapps.spring20.getoutofit.data.DatabaseManager;
import edu.floridapoly.mobiledeviceapps.spring20.getoutofit.data.MessageDataEntry;

public class CreateMessageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_message);
    }

    public void saveMessageButton(View view) {
        EditText summary = findViewById(R.id.ev_excuse_summary);
        EditText message = findViewById(R.id.ev_message);

        // Obtain the text from the boxes
        // TODO: Check if the strings are empty or not
        String summaryText = summary.getText().toString();
        String messageText = message.getText().toString();

        // Insert into database
        final MessageDataEntry dataEntry = new MessageDataEntry(summaryText, messageText);
        final DatabaseManager db = DatabaseManager.getInstance(this);
        db.getQueryExecutor().execute(new Runnable() {
            @Override
            public void run() {
                db.messageDataDao().insert(dataEntry);
            }
        });

        // Return to calling Activity
        finish();
    }
}
