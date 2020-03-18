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

import edu.floridapoly.mobiledeviceapps.spring20.getoutofit.R;
import edu.floridapoly.mobiledeviceapps.spring20.getoutofit.adapters.TextAlarmAdapter;
import edu.floridapoly.mobiledeviceapps.spring20.getoutofit.adapters.ViewMessagesAdapter;
import edu.floridapoly.mobiledeviceapps.spring20.getoutofit.data.MessageData;
import edu.floridapoly.mobiledeviceapps.spring20.getoutofit.data.TextAlarmData;
import edu.floridapoly.mobiledeviceapps.spring20.getoutofit.helpers.IChangeItem;
import edu.floridapoly.mobiledeviceapps.spring20.getoutofit.helpers.SwipeCallback;

public class ViewMessagesActivity extends AppCompatActivity implements IChangeItem<MessageData> {

    RecyclerView mRecyclerView;
    ViewMessagesAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_messages);

        // Obtain reference to recyclerview
        mRecyclerView = findViewById(R.id.rv_view_messages);

        // Setup the recyclerview to use a linearlayout
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize adapter and attach to recycler
        mAdapter = new ViewMessagesAdapter(this, this);
        mRecyclerView.setAdapter(mAdapter);

        // Setup swipe functionality
        new ItemTouchHelper(new SwipeCallback<MessageData>(this)).attachToRecyclerView(mRecyclerView);

        // TODO: Display real data from database
        // Add fake data to display
        mAdapter.setAlarmEntries(Arrays.asList(ViewMessagesAdapter.testMessages));
    }

    public void createMessageButton(View view) {
        Intent intent = new Intent(this, CreateMessageActivity.class);
        startActivity(intent);
    }

    @Override
    public void deleteItem(int dataPosition) {
        // TODO: Delete the object from the database
        MessageData data = mAdapter.getTextAlarm(dataPosition);
        mAdapter.removeElement(dataPosition);
    }

    @Override
    public void editItem(MessageData data) {
        // TODO: send an Intent to TextAlarmActivity with the data from object.
        Toast.makeText(ViewMessagesActivity.this, String.format("Clicked on Message: %d", data.getId()), Toast.LENGTH_SHORT).show();
    }
}
