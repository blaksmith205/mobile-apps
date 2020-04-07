package edu.floridapoly.mobiledeviceapps.spring20.getoutofit.data;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

public class MessageDataViewModel extends AndroidViewModel {
    private static final String TAG = MessageDataViewModel.class.getSimpleName();

    private LiveData<List<MessageDataEntry>> entries;

    public MessageDataViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<List<MessageDataEntry>> getEntries() {
        if (entries == null) {
            entries = new MutableLiveData<List<MessageDataEntry>>();
            loadEntries();
        }
        return entries;
    }


    private void loadEntries() {
        entries = DatabaseManager.getInstance(this.getApplication()).messageDataDao().loadMessages();
    }
}
