package edu.floridapoly.mobiledeviceapps.spring20.getoutofit.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;

import edu.floridapoly.mobiledeviceapps.spring20.getoutofit.R;
import edu.floridapoly.mobiledeviceapps.spring20.getoutofit.data.TextAlarmEntry;
import edu.floridapoly.mobiledeviceapps.spring20.getoutofit.helpers.IChangeItem;

public class TextAlarmAdapter extends GeneralAdapter<TextAlarmEntry, TextAlarmAdapter.TextAlarmViewHolder> {

    private Context mContext;

    public TextAlarmAdapter(Context context, IChangeItem<TextAlarmEntry> touchHandler) {
        super(context, touchHandler);
        this.mContext = context;
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
        TextAlarmEntry entry = mEntries.get(position);
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm a");

        // Set the data in the TextViews
        String dateString = String.format(mContext.getString(R.string.formatted_date), dateFormat.format(entry.getDateTime()));
        String fromString = String.format(mContext.getString(R.string.formatted_from), entry.getSenderInfo());
        String summaryString = String.format(mContext.getString(R.string.formatted_message_summary), entry.getMessageData().getSummary());
        holder.dateView.setText(dateString);
        holder.fromView.setText(fromString);
        holder.summaryView.setText(summaryString);

        // Add the position of the TextAlarmData, in case it is needed again
        holder.dateView.setTag(position);
    }

    class TextAlarmViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView dateView;
        TextView fromView;
        TextView summaryView;

        TextAlarmViewHolder(@NonNull View itemView) {
            super(itemView);

            dateView = itemView.findViewById(R.id.tv_text_alarm_rv_date);
            fromView = itemView.findViewById(R.id.tv_text_alarm_rv_from);
            summaryView = itemView.findViewById(R.id.tv_text_alarm_rv_summary);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mChangeHandler.editItem(mEntries.get(getAdapterPosition()));
        }
    }
}
