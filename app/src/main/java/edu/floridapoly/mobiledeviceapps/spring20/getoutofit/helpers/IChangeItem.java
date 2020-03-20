package edu.floridapoly.mobiledeviceapps.spring20.getoutofit.helpers;

public interface IChangeItem<T> {
    /**
     * Called when an item in a RecyclerView is swiped in either left or right direction
     *
     * @param dataIndex The index of the data that was swiped.
     */
    void deleteItem(int dataIndex);

    /**
     * Called when an item in a RecyclerView is tapped
     *
     * @param data The data object that was tapped
     */
    void editItem(T data);
}
