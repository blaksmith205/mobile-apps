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
import edu.floridapoly.mobiledeviceapps.spring20.getoutofit.adapters.ViewMessagesAdapter;
import edu.floridapoly.mobiledeviceapps.spring20.getoutofit.data.MessageDataEntry;
import edu.floridapoly.mobiledeviceapps.spring20.getoutofit.helpers.IChangeItem;
import edu.floridapoly.mobiledeviceapps.spring20.getoutofit.helpers.SwipeCallback;

public class ViewMessagesActivity extends AppCompatActivity implements IChangeItem<MessageDataEntry> {

    // TODO: Obtain messages from database
    static final MessageDataEntry[] testMessages = {
            new MessageDataEntry("Summary 1", "Please help!", 0),
            new MessageDataEntry("Summary 2", "Beloved lizard died!", 1),
            new MessageDataEntry("Summary 3", "It's an emergency!", 2)
    };

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
        new ItemTouchHelper(new SwipeCallback<MessageDataEntry>(this)).attachToRecyclerView(mRecyclerView);

        // TODO: Display real data from database
        // Add fake data to display
        mAdapter.setAlarmEntries(Arrays.asList(testMessages));
    }

    public void createMessageButton(View view) {
        Intent intent = new Intent(this, CreateMessageActivity.class);
        startActivity(intent);
    }

    @Override
    public void deleteItem(int dataPosition) {
        // TODO: Delete the object from the database
        MessageDataEntry data = mAdapter.getEntry(dataPosition);
        mAdapter.removeElement(dataPosition);
    }

    @Override
    public void editItem(MessageDataEntry data) {
        // TODO: send an Intent to TextAlarmActivity with the data from object.
        Toast.makeText(ViewMessagesActivity.this, String.format("Clicked on Message: %d", data.getId()), Toast.LENGTH_SHORT).show();
    }
}
