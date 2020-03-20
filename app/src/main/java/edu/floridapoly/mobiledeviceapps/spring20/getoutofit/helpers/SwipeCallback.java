package edu.floridapoly.mobiledeviceapps.spring20.getoutofit.helpers;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

public class SwipeCallback<T> extends ItemTouchHelper.SimpleCallback {

    private IChangeItem<T> itemChanger;

    /**
     * Creates a Callback for swiping right or left. Calls itemTouchHandler.onDelete(int)
     * when the recycler view item is swiped. Moving the view is not supported.
     *
     * @param itemHandler The implemented handler for deletion of a swiped object
     */
    public SwipeCallback(@NonNull IChangeItem<T> itemHandler) {
        super(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        this.itemChanger = itemHandler;
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        itemChanger.deleteItem(viewHolder.getAdapterPosition());
    }
}
