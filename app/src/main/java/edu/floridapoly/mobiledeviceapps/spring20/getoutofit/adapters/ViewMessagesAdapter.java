package edu.floridapoly.mobiledeviceapps.spring20.getoutofit.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import edu.floridapoly.mobiledeviceapps.spring20.getoutofit.R;
import edu.floridapoly.mobiledeviceapps.spring20.getoutofit.data.MessageData;
import edu.floridapoly.mobiledeviceapps.spring20.getoutofit.data.TextAlarmData;
import edu.floridapoly.mobiledeviceapps.spring20.getoutofit.helpers.IChangeItem;

public class ViewMessagesAdapter extends RecyclerView.Adapter<ViewMessagesAdapter.ViewMessagesViewHolder> {
    public static final MessageData[] testMessages = {
            new MessageData("Summary 1", "Please help!", 0),
            new MessageData("Summary 2", "Beloved lizard died!", 1),
            new MessageData("Summary 3", "It's an emergency!", 2)
    };

    private ArrayList<MessageData> mMessageEntries;
    private IChangeItem<MessageData> mChangeHandler;

    private Context mContext;

    public ViewMessagesAdapter(Context context, IChangeItem<MessageData> touchHandler) {
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
        MessageData data = mMessageEntries.get(position);

        // Set the data in the TextViews
        holder.summaryView.setText(data.getSummary());
        holder.messageView.setText(data.getMessage());

        // Add the position of the TextAlarmData, in case it is needed again
        holder.summaryView.setTag(position);
    }

    @Override
    public int getItemCount() {
        if (mMessageEntries == null)
            return 0;
        return mMessageEntries.size();
    }

    // Getters and setters

    public List<MessageData> getAlarmEntries() {
        return mMessageEntries;
    }

    public void setAlarmEntries(List<MessageData> messages) {
        this.mMessageEntries = new ArrayList<>(messages);
        notifyDataSetChanged();
    }

    public MessageData getTextAlarm(int index) {
        return mMessageEntries.get(index);
    }

    // Removes the TextAlarm from the internal list.
    // TODO: Replace with either auto update of data with Room, or update the database and retrieve from the database again
    public void removeElement(int index) {
        mMessageEntries.remove(index);
        notifyDataSetChanged();
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
            mChangeHandler.editItem(mMessageEntries.get(getAdapterPosition()));
        }
    }
}
