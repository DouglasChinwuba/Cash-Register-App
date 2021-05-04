package sample;

import java.math.BigDecimal;

public class Item {

    /**
     * String to store item name
     */
    private String name;

    /**
     * BigDecimal to store price for each item
     */
    private BigDecimal priceForEach;

    /**
     * int to store item quantity
     */
    private int quantity;

    /**
     * boolean that stores if item has tax
     */
    private boolean hasTax;


    /**
     * Constructor for Item class
     * @param name      name of item
     * @param quantity  quantity of the item
     * @param hasTax    tax item
     */
    private Item(String name, int quantity, boolean hasTax){
        this.name = name;
        this.quantity = quantity;
        this.hasTax = hasTax;
    }

    /**
     * Another constructor for Item class
     * @param name           name of item
     * @param priceForEach   price of item
     * @param quantity       quantity of item
     * @param hasTax         tax item
     */
    public Item(String name, String priceForEach, int quantity, boolean hasTax){
        this(name, quantity, hasTax);
        this.priceForEach = new BigDecimal(priceForEach).setScale(2, BigDecimal.ROUND_HALF_UP);
    }


    /**
     * Another constructor for Item class
     * @param name          name of item
     * @param priceForEach  price of item
     * @param quantity      quantity of item
     * @param hasTax        tax item
     */
    public Item(String name, BigDecimal priceForEach, int quantity, boolean hasTax){
        this(name, quantity, hasTax);
        this.priceForEach = priceForEach;
    }

    /**
     * Item name getter
     * @return  item name
     */
    public String getName() {
        return name;
    }

    /**
     * Item price getter
     * @return  item price
     */
    public BigDecimal getPriceForEach() {
        return priceForEach;
    }

    /**
     * Item hasTax getter
     * @return  has tax
     */
    public boolean hasTax() {
        return hasTax;
    }

    /**
     * Item quantity getter
     * @return  item quantity
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * Returns item toString
     * @return item toString
     */
    @Override
    public String toString() {
        StringBuilder item = new StringBuilder(name);
        item.append(hasTax() ? " (H) " : " ");
        item.append("    " + priceForEach.multiply(new BigDecimal(quantity)));
        item.append("\n");
        return item.toString();
    }
}
