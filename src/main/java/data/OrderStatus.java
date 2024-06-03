package data;

import lombok.AllArgsConstructor;

/**
 * Enum for the status of an order.
 */
@AllArgsConstructor
public enum OrderStatus {
    NOT_PLACED(1, "not placed"),
    PLACED(2, "placed"),
    DELIVERED(3, "delivered");

    /**
     * The id of the order status.
     */
    public final int id;

    /**
     * The name of the order status.
     */
    public final String name;

    /**
     * Get the name of an order status from its id.
     *
     * @param id The id of the order status.
     * @return The name of the order status.
     * @throws IllegalArgumentException If no order status with the given id is found.
     */
    public static String getNameFromId(int id) {
        OrderStatus[] var1 = values();
        int var2 = var1.length;

        for(int var3 = 0; var3 < var2; ++var3) {
            OrderStatus orderStatus = var1[var3];
            if (orderStatus.id == id) {
                return orderStatus.name;
            }
        }

        throw new IllegalArgumentException("No order status with id %d found.".formatted(id));
    }

}
