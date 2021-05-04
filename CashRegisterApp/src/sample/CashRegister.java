package sample;

import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.LinkedList;
import java.awt.Desktop;

public class CashRegister {
    /**
     * Singleton instance of CashRegister
     */
    private static CashRegister instance = new CashRegister();

    /**
     * Linked list of CashRegister items
     */
    private LinkedList<Item> itemList;

    /**
     * BigDecimal that stores total tax
     */
    private BigDecimal totalTax;

    /**
     * Constructor for CashRegister class
     */
    private CashRegister(){
        itemList = new LinkedList<>();
        totalTax = new BigDecimal("0.00").setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * Returns singleton instance of CashRegister
     * @return instance of CashRegister
     */
    public static CashRegister getInstance() {
        return instance;
    }

    /**
     * Returns list of CashRegister items
     * @return LinkedList of CashRegister items
     */
    public LinkedList<Item> getItemList() {
        return itemList;
    }

    /**
     * Returns total tax
     * @return  BigDecimal totalTax
     */
    public BigDecimal getTotalTax(){
        return totalTax;
    }

    /**
     * Adds item to CashRegister item list and increments total tax
     * @param item  item to add to list
     */
    public void addToItemList(Item item){
        itemList.addLast(item);
        incrementTotalTax(item);
    }

    /**
     * Removes last item on CashRegister item list and decrements total tax
     * @return removed item
     * @throws Exception if the list is empty
     */
    public Item voidLastItem() throws Exception{
        Item removedItem = itemList.removeLast();
        totalTax = totalTax.subtract(incrementTotalTax(removedItem));
        return removedItem;
    }

    /**
     * Increments total tax
     * @param item
     * @return  item's tax
     */
    private BigDecimal incrementTotalTax(Item item){
        BigDecimal itemTax = new BigDecimal(item.hasTax() ? String.valueOf(item.getPriceForEach().multiply(new BigDecimal("0.13"))) : "0.00").setScale(2, BigDecimal.ROUND_HALF_UP);;
        totalTax = totalTax.add(itemTax);
        return itemTax;
    }

    /**
     * Returns total
     * @return total
     */
    public BigDecimal getTotal(){
         return getSubTotal().add(totalTax);
    }

    /**
     * Returns subtotal
     * @return subtotal
     */
    public BigDecimal getSubTotal(){
        BigDecimal subTotal = new BigDecimal("0.00");
        Iterator<Item> iterator = itemList.iterator();
        while(iterator.hasNext()){
            Item item = iterator.next();
            subTotal = subTotal.add(item.getPriceForEach().multiply(new BigDecimal(item.getQuantity())));
        }
        return subTotal;
    }

    /**
     * Returns change
     * @param cashPaid cashPaid by customer
     * @return
     */
    public BigDecimal getChange(BigDecimal cashPaid){
        BigDecimal change = new BigDecimal(String.valueOf(cashPaid.subtract(getTotal())));
        return change;
    }

    /**
     * Reinitialize CashRegister
     */
    public void reInitialize(){
        itemList.clear();
        totalTax = new BigDecimal("0.00").setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * Generates receipt
     * @param receiptTotal  cost of all item
     * @param receiptChange customers change
     */
    public void generateReceipt(String receiptTotal, String receiptChange){
        try{
            File file = new File("receipt.pdf");
            String fileName = file.getAbsolutePath();
//            String fileName = "C:\\Users\\dougl\\Documents\\receipt.pdf";
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(fileName));
            document.open();

            CompanyInfo companyInfo;
            try{
                companyInfo = DataSource.getInstance().getCompanyInfo();
                if(companyInfo != null){
                    Paragraph heading = new Paragraph(companyInfo.getCompanyName() + "\n");
                    heading.add(companyInfo.getCompanyAddress() + "\n");
                    heading.add(companyInfo.getCompanyPhoneNumber() + "\n");
                    heading.add(companyInfo.getCompanyEmail() + "\n");
                    heading.add("------------------------------------------------- \n");
                    document.add(heading);
                }
            }catch (SQLException e){
                e.printStackTrace();
            }

            Paragraph items = new Paragraph();
            for(Item item : itemList){
                items.add(item.toString());
            }
            document.add(items);

            Paragraph total = new Paragraph("------------------------------------------------- \n");
            total.add(receiptTotal + "\n");
            total.add("------------------------------------------------- \n");
            total.add(receiptChange + "\n");
            total.add("------------------------------------------------- \n");
            total.add("Thank you");
            document.add(total);
            document.close();
            Desktop.getDesktop().open(new File(fileName));

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
