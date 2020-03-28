package edu.floridapoly.mobiledeviceapps.spring20.getoutofit.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import edu.floridapoly.mobiledeviceapps.spring20.getoutofit.BuildConfig;
import edu.floridapoly.mobiledeviceapps.spring20.getoutofit.R;
import edu.floridapoly.mobiledeviceapps.spring20.getoutofit.adapters.TextAlarmAdapter;
import edu.floridapoly.mobiledeviceapps.spring20.getoutofit.data.DatabaseManager;
import edu.floridapoly.mobiledeviceapps.spring20.getoutofit.data.TextAlarmEntry;
import edu.floridapoly.mobiledeviceapps.spring20.getoutofit.data.TextAlarmViewModel;
import edu.floridapoly.mobiledeviceapps.spring20.getoutofit.helpers.AppExecutors;
import edu.floridapoly.mobiledeviceapps.spring20.getoutofit.helpers.IChangeItem;
import edu.floridapoly.mobiledeviceapps.spring20.getoutofit.helpers.SwipeCallback;

public class MainActivity extends AppCompatActivity implements IChangeItem<TextAlarmEntry> {

    public static final String EXTRA_INSTANT_MESSAGE = "INSTANT_MESSAGE";
    private static final String TAG = MainActivity.class.getSimpleName();

    private RecyclerView mRecyclerView;
    private TextAlarmAdapter mAdapter;
    private TextAlarmViewModel viewModel;
    private Observer<List<TextAlarmEntry>> dataObserver = new Observer<List<TextAlarmEntry>>() {
        @Override
        public void onChanged(List<TextAlarmEntry> alarms) {
            if (BuildConfig.DEBUG) Log.d(TAG, "Updating adapter to show data from ViewModel");
            mAdapter.setEntries(alarms);
        }
    };

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
        new ItemTouchHelper(new SwipeCallback<TextAlarmEntry>(this)).attachToRecyclerView(mRecyclerView);

        // Display real data from database
        // Add fake data to display
        setupViewModel();
    }

    @Override
    protected void onDestroy() {
        viewModel.getEntries().removeObserver(dataObserver);
        super.onDestroy();
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
        TextAlarmEntry entry = mAdapter.getEntry(dataPosition);

        final DatabaseManager db = DatabaseManager.getInstance(this);
        AppExecutors.getInstance().diskIO().execute(() -> db.textAlarmDao().delete(entry));
    }

    @Override
    public void editItem(TextAlarmEntry entry) {
        // TODO: send an Intent to TextAlarmActivity with the data from object.
        Toast.makeText(MainActivity.this, String.format("Clicked on TextAlarm: %d", entry.getAlarmId()), Toast.LENGTH_SHORT).show();
    }

    private void setupViewModel() {
        viewModel = new ViewModelProvider(this).get(TextAlarmViewModel.class);
        viewModel.getEntries().observe(this, dataObserver);
    }
}
