package edu.floridapoly.mobiledeviceapps.spring20.getoutofit.data;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

public class TextAlarmViewModel extends AndroidViewModel {
    private static final String TAG = TextAlarmViewModel.class.getSimpleName();

    private LiveData<List<TextAlarmEntry>> entries;

    public TextAlarmViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<List<TextAlarmEntry>> getEntries() {
        if (entries == null) {
            entries = new MutableLiveData<List<TextAlarmEntry>>();
            loadEntries();
        }
        return entries;
    }


    private void loadEntries() {
        entries = DatabaseManager.getInstance(this.getApplication()).textAlarmDao().loadTextAlarms();
    }
}
