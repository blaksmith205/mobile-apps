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

    public TextAlarmAdapter(Context context, IChangeItem<TextAlarmEntry> touchHandler) {
        super(context, touchHandler);
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
        TextAlarmEntry textAlarm = mEntries.get(position);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/mm/yyyy");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");

        // Set the data in the TextViews
        holder.dateView.setText(dateFormat.format(textAlarm.getDateTime()));
        holder.timeView.setText(timeFormat.format(textAlarm.getDateTime()));
        holder.fromView.setText(textAlarm.getFrom());
        // TODO: Extract actual message data from reference object
        holder.summaryView.setText("Example Message");//textAlarm.getData().getSummary());

        // Add the position of the TextAlarmData, in case it is needed again
        holder.dateView.setTag(position);
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
            mChangeHandler.editItem(mEntries.get(getAdapterPosition()));
        }
    }
}
