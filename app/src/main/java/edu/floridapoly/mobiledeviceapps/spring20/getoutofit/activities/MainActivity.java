package edu.floridapoly.mobiledeviceapps.spring20.getoutofit.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Arrays;
import java.util.Date;

import edu.floridapoly.mobiledeviceapps.spring20.getoutofit.R;
import edu.floridapoly.mobiledeviceapps.spring20.getoutofit.adapters.TextAlarmAdapter;
import edu.floridapoly.mobiledeviceapps.spring20.getoutofit.data.TextAlarmDataEntry;
import edu.floridapoly.mobiledeviceapps.spring20.getoutofit.helpers.IChangeItem;
import edu.floridapoly.mobiledeviceapps.spring20.getoutofit.helpers.SwipeCallback;

public class MainActivity extends AppCompatActivity implements IChangeItem<TextAlarmDataEntry> {

    // TODO: Obtain TextAlarms from database
    static final TextAlarmDataEntry[] testAlarms = {
            new TextAlarmDataEntry(new Date(), "10:00 am", "Robert", "Summary 1", "", 0),
            new TextAlarmDataEntry(new Date(), "11:00 am", "Will", "Summary 2", "", 1),
            new TextAlarmDataEntry(new Date(), "12:00 pm", "Leon", "Summary 3", "", 2)
    };

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

        // Setup swipe functionality
        new ItemTouchHelper(new SwipeCallback<TextAlarmDataEntry>(this)).attachToRecyclerView(mRecyclerView);

        // TODO: Display real data from database
        // Add fake data to display
        mAdapter.setAlarmEntries(Arrays.asList(testAlarms));
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
    public void deleteItem(int dataPosition) {
        // TODO: Delete the object from the database
        TextAlarmDataEntry data = mAdapter.getEntry(dataPosition);
        mAdapter.removeElement(dataPosition);
    }

    @Override
    public void editItem(TextAlarmDataEntry data) {
        // TODO: send an Intent to TextAlarmActivity with the data from object.
        Toast.makeText(MainActivity.this, String.format("Clicked on TextAlarm: %d", data.getId()), Toast.LENGTH_SHORT).show();
    }
}
