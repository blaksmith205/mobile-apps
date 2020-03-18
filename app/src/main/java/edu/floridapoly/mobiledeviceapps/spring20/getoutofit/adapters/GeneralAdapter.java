package edu.floridapoly.mobiledeviceapps.spring20.getoutofit.adapters;

import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import edu.floridapoly.mobiledeviceapps.spring20.getoutofit.helpers.IChangeItem;

public abstract class GeneralAdapter<T, VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {

    protected ArrayList<T> mEntries;
    protected IChangeItem<T> mChangeHandler;
    protected Context mContext;

    public GeneralAdapter(Context context, IChangeItem<T> touchHandler) {
        this.mContext = context;
        this.mChangeHandler = touchHandler;
    }

    @Override
    public int getItemCount() {
        if (mEntries == null)
            return 0;
        return mEntries.size();
    }

    // Getters and setters

    public List<T> getEntries() {
        return mEntries;
    }

    public void setAlarmEntries(List<T> entries) {
        this.mEntries = new ArrayList<>(entries);
        notifyDataSetChanged();
    }

    public T getEntry(int index) {
        return mEntries.get(index);
    }

    // Removes the TextAlarm from the internal list.
    // TODO: Replace with either auto update of data with Room, or update the database and retrieve from the database again
    public void removeElement(int index) {
        mEntries.remove(index);
        notifyDataSetChanged();
    }
}
