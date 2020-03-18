package edu.floridapoly.mobiledeviceapps.spring20.getoutofit.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import edu.floridapoly.mobiledeviceapps.spring20.getoutofit.R;
import edu.floridapoly.mobiledeviceapps.spring20.getoutofit.adapters.TextAlarmAdapter;
import edu.floridapoly.mobiledeviceapps.spring20.getoutofit.data.TextAlarmData;
import edu.floridapoly.mobiledeviceapps.spring20.getoutofit.helpers.GeneralTouchHelper;

public class MainActivity extends AppCompatActivity implements GeneralTouchHelper.IChangeHandler<TextAlarmData> {

    public static final String EXTRA_INSTANT_MESSAGE = "INSTANT_MESSAGE";

    private RecyclerView mRecyclerView;
    private TextAlarmAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Obtain reference to recyclerview
        mRecyclerView = findViewById(R.id.rv_text_alarms);

        // Setup the recyclerview to use a linearlayout
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize adapter and attach to recycler
        mAdapter = new TextAlarmAdapter(this, this);
        mRecyclerView.setAdapter(mAdapter);

        // TODO: Display real data from database
        // Add fake data to display
        mAdapter.setAlarmEntries(Arrays.asList(TextAlarmAdapter.testAlarms));
    }

    public void viewMessageButton(View view) {
        Intent intent = new Intent(this, ViewMessagesActivity.class);
        startActivity(intent);
    }

    public void textAlarmButton(View view) {
        boolean instantText = false;
        // Check if the view is the instant text button
        if (view.getId() == R.id.bt_instant_text)
            instantText = true;

        Intent intent = new Intent(this, TextAlarmActivity.class);
        intent.putExtra(EXTRA_INSTANT_MESSAGE, instantText);
        startActivity(intent);
    }

    @Override
    public void onDelete(TextAlarmData object) {

    }

    @Override
    public void onEdit(TextAlarmData object) {

    }
}
