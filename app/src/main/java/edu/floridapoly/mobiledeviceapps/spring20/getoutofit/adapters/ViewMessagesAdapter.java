package edu.floridapoly.mobiledeviceapps.spring20.getoutofit.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import edu.floridapoly.mobiledeviceapps.spring20.getoutofit.R;
import edu.floridapoly.mobiledeviceapps.spring20.getoutofit.data.MessageDataEntry;
import edu.floridapoly.mobiledeviceapps.spring20.getoutofit.helpers.IChangeItem;

public class ViewMessagesAdapter extends GeneralAdapter<MessageDataEntry, ViewMessagesAdapter.ViewMessagesViewHolder> {

    public ViewMessagesAdapter(Context context, IChangeItem<MessageDataEntry> touchHandler) {
        super(context, touchHandler);
    }

    /**
     * Called when ViewHolders are created to fill a RecyclerView.
     *
     * @return A new TextAlarmViewHolder that holds the view for each TextAlarm
     */
    @NonNull
    @Override
    public ViewMessagesAdapter.ViewMessagesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.rv_view_messages_layout, parent, false);
        return new ViewMessagesAdapter.ViewMessagesViewHolder(view);
    }

    /**
     * Called by the RecyclerView to display data at a specified position
     *
     * @param holder   The ViewHolder to bind data to
     * @param position The position of the data
     */
    @Override
    public void onBindViewHolder(@NonNull ViewMessagesAdapter.ViewMessagesViewHolder holder, int position) {
        MessageDataEntry data = mEntries.get(position);

        String summaryString = String.format(mContext.getString(R.string.formatted_message_summary), data.getSummary());
        String messageString = String.format(mContext.getString(R.string.formatted_message), data.getMessage());
        // Set the data in the TextViews
        holder.summaryView.setText(summaryString);
        holder.messageView.setText(messageString);

        // Set the templates to bold
        if (data.isTemplate()) {
            holder.summaryView.setTypeface(holder.summaryView.getTypeface(), Typeface.BOLD);
            holder.messageView.setTypeface(holder.messageView.getTypeface(), Typeface.BOLD);
        } else {
            holder.summaryView.setTypeface(null, Typeface.NORMAL);
            holder.messageView.setTypeface(null, Typeface.NORMAL);
        }
        // Add the position of the TextAlarmData, in case it is needed again
        holder.summaryView.setTag(position);
    }

    class ViewMessagesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView summaryView;
        TextView messageView;

        ViewMessagesViewHolder(@NonNull View itemView) {
            super(itemView);

            summaryView = itemView.findViewById(R.id.tv_view_message_rv_summary);
            messageView = itemView.findViewById(R.id.tv_view_message_rv_message);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mChangeHandler.editItem(mEntries.get(getAdapterPosition()));
        }
    }
}
