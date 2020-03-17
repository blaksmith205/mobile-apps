package edu.floridapoly.mobiledeviceapps.spring20.getoutofit.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import edu.floridapoly.mobiledeviceapps.spring20.getoutofit.R;

public class CreateMessageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_message);
    }

    public void saveMessageButton(View view) {
        Toast.makeText(this, "Save summary and message to DB", Toast.LENGTH_SHORT).show();
    }
}
