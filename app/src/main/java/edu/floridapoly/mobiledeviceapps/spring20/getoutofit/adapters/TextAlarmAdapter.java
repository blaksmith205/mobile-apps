package edu.floridapoly.mobiledeviceapps.spring20.getoutofit.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import edu.floridapoly.mobiledeviceapps.spring20.getoutofit.R;
import edu.floridapoly.mobiledeviceapps.spring20.getoutofit.data.TextAlarmData;
import edu.floridapoly.mobiledeviceapps.spring20.getoutofit.helpers.IChangeItem;

public class TextAlarmAdapter extends RecyclerView.Adapter<TextAlarmAdapter.TextAlarmViewHolder> {

    public static final TextAlarmData[] testAlarms = {
            new TextAlarmData(new Date(), "10:00 am", "Robert", "Summary 1", "", 0),
            new TextAlarmData(new Date(), "11:00 am", "Will", "Summary 2", "", 1),
            new TextAlarmData(new Date(), "12:00 pm", "Leon", "Summary 3", "", 2)
    };

    private ArrayList<TextAlarmData> mAlarmEntries;
    private IChangeItem<TextAlarmData> mChangeHandler;

    private Context mContext;

    public TextAlarmAdapter(Context context, IChangeItem<TextAlarmData> touchHandler) {
        this.mContext = context;
        this.mChangeHandler = touchHandler;
    }

    /**
     * Called when ViewHolders are created to fill a RecyclerView.
     *
     * @return A new TextAlarmViewHolder that holds the view for each TextAlarm
     */
    @NonNull
    @Override
    public TextAlarmViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.rv_text_alarm_layout, parent, false);
        return new TextAlarmViewHolder(view);
    }

    /**
     * Called by the RecyclerView to display data at a specified position
     *
     * @param holder   The ViewHolder to bind data to
     * @param position The position of the data
     */
    @Override
    public void onBindViewHolder(@NonNull TextAlarmViewHolder holder, int position) {
        TextAlarmData data = mAlarmEntries.get(position);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/mm/yyyy");

        // Set the data in the TextViews
        holder.dateView.setText(dateFormat.format(data.getDate()));
        holder.timeView.setText(data.getTime());
        holder.fromView.setText(data.getFrom());
        holder.summaryView.setText(data.getSummary());

        // Add the position of the TextAlarmData, in case it is needed again
        holder.dateView.setTag(position);
    }

    @Override
    public int getItemCount() {
        if (mAlarmEntries == null)
            return 0;
        return mAlarmEntries.size();
    }

    // Getters and setters

    public List<TextAlarmData> getAlarmEntries() {
        return mAlarmEntries;
    }

    public void setAlarmEntries(List<TextAlarmData> textAlarms) {
        this.mAlarmEntries = new ArrayList<>(textAlarms);
        notifyDataSetChanged();
    }

    public TextAlarmData getTextAlarm(int index) {
        return mAlarmEntries.get(index);
    }

    // Removes the TextAlarm from the internal list.
    // TODO: Replace with either auto update of data with Room, or update the database and retrieve from the database again
    public void removeElement(int index) {
        mAlarmEntries.remove(index);
        notifyDataSetChanged();
    }

    class TextAlarmViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView dateView;
        TextView timeView;
        TextView fromView;
        TextView summaryView;

        TextAlarmViewHolder(@NonNull View itemView) {
            super(itemView);

            dateView = itemView.findViewById(R.id.tv_text_alarm_rv_date);
            timeView = itemView.findViewById(R.id.tv_text_alarm_rv_time);
            fromView = itemView.findViewById(R.id.tv_text_alarm_rv_from);
            summaryView = itemView.findViewById(R.id.tv_text_alarm_rv_summary);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mChangeHandler.editItem(mAlarmEntries.get(getAdapterPosition()));
        }
    }
}
