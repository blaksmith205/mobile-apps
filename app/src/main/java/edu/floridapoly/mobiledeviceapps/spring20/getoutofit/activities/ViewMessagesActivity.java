package edu.floridapoly.mobiledeviceapps.spring20.getoutofit.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import edu.floridapoly.mobiledeviceapps.spring20.getoutofit.BuildConfig;
import edu.floridapoly.mobiledeviceapps.spring20.getoutofit.R;
import edu.floridapoly.mobiledeviceapps.spring20.getoutofit.adapters.ViewMessagesAdapter;
import edu.floridapoly.mobiledeviceapps.spring20.getoutofit.data.DatabaseManager;
import edu.floridapoly.mobiledeviceapps.spring20.getoutofit.data.MessageDataEntry;
import edu.floridapoly.mobiledeviceapps.spring20.getoutofit.data.MessageDataViewModel;
import edu.floridapoly.mobiledeviceapps.spring20.getoutofit.helpers.AppExecutors;
import edu.floridapoly.mobiledeviceapps.spring20.getoutofit.helpers.IChangeItem;
import edu.floridapoly.mobiledeviceapps.spring20.getoutofit.helpers.SwipeCallback;

public class ViewMessagesActivity extends AppCompatActivity implements IChangeItem<MessageDataEntry> {

    private static final String TAG = "ViewMessagesActivity";

    RecyclerView mRecyclerView;
    ViewMessagesAdapter mAdapter;

    MessageDataViewModel viewModel;

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

        // Display real data from database
        setupViewModel();
    }

    public void createMessageButton(View view) {
        Intent intent = new Intent(this, CreateMessageActivity.class);
        startActivity(intent);
    }

    @Override
    public void deleteItem(int dataPosition) {
        MessageDataEntry data = mAdapter.getEntry(dataPosition);

        final DatabaseManager db = DatabaseManager.getInstance(this);
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                db.messageDataDao().delete(data);
            }
        });
    }

    @Override
    public void editItem(MessageDataEntry data) {
        // TODO: send an Intent to CreateAlarmActivity with the data from object.
        Toast.makeText(ViewMessagesActivity.this, String.format("Clicked on Message: %d", data.getId()), Toast.LENGTH_SHORT).show();
    }

    private void setupViewModel() {
        viewModel = new ViewModelProvider(this).get(MessageDataViewModel.class);
        viewModel.getEntries().observe(this, messages -> {
            if (BuildConfig.DEBUG) Log.d(TAG, "Updating adapter to show data from ViewModel");
            mAdapter.setEntries(messages);
        });
    }
}
