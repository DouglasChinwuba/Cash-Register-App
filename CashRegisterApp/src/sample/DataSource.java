package sample;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.math.BigDecimal;
import java.sql.*;

public class DataSource {
    /**
     * SQL statements
     */
    public static final String DB_NAME = "itemsInStore.db";
    public static final String CONNECTION_STRING =  "jdbc:sqlite:src/sample/" + DB_NAME;

    public static final String TABLE_ITEMS = "items";
    public static final String COLUMN_ITEM_ID = "_id";
    public static final String COLUMN_ITEM_NAME = "name";
    public static final String COLUMN_ITEM_PLU = "plu";
    public static final String COLUMN_ITEM_PRICE = "price";
    public static final String COLUMN_ITEM_HAS_TAX = "hasTax";
    public static final String COLUMN_ITEM_PRICE_TWO_DECIMAL_PLACE = "printf('%.2f'," + COLUMN_ITEM_PRICE + ") AS Field";


    public static final String TABLE_COMPANY_INFO = "company_info";
    public static final String COLUMN_COMPANY_NAME = "name";
    public static final String COLUMN_COMPANY_ADDRESS = "address";
    public static final String COLUMN_COMPANY_PHONE_NUMBER = "phone_number";
    public static final String COLUMN_COMPANY_EMAIL = "email";


    public static final String CREATE_TABLE_ITEMS = "CREATE TABLE IF NOT EXISTS " + TABLE_ITEMS +
                " (" + COLUMN_ITEM_ID +  " INTEGER PRIMARY KEY, " + COLUMN_ITEM_NAME + " TEXT NOT NULL, " + COLUMN_ITEM_PLU +
                " INTEGER, " + COLUMN_ITEM_PRICE + " DECIMAL (6,2) NOT NULL, " + COLUMN_ITEM_HAS_TAX +
                " INTEGER NOT NULL)";

    public static final String QUERY_ITEM_WITH_NAME = "SELECT " + COLUMN_ITEM_NAME + ", " + COLUMN_ITEM_PRICE_TWO_DECIMAL_PLACE + ", " + COLUMN_ITEM_HAS_TAX +
            " FROM " + TABLE_ITEMS + " WHERE " + COLUMN_ITEM_NAME + " = ? COLLATE NOCASE";

    public static final String QUERY_ITEM_WITH_PLU = "SELECT " + COLUMN_ITEM_NAME + ", " + COLUMN_ITEM_PRICE_TWO_DECIMAL_PLACE + ", " + COLUMN_ITEM_HAS_TAX +
            " FROM " + TABLE_ITEMS + " WHERE " + COLUMN_ITEM_PLU + " = ?";

    public static final String INSERT_ITEM = "INSERT INTO " + TABLE_ITEMS +
            '(' + COLUMN_ITEM_NAME + ", " + COLUMN_ITEM_PLU + ", " + COLUMN_ITEM_PRICE + ", " + COLUMN_ITEM_HAS_TAX + ") VALUES(?, ?, ?, ?)";

   public static final String DELETE_ITEM = "DELETE FROM " + TABLE_ITEMS + " WHERE " +
           COLUMN_ITEM_NAME + " = ? COLLATE NOCASE";

   public static final String QUERY_ALL_ITEMS_FROM_TABLE = "SELECT " + COLUMN_ITEM_NAME + "," + COLUMN_ITEM_PLU + ", " +
           COLUMN_ITEM_PRICE_TWO_DECIMAL_PLACE + "," + COLUMN_ITEM_HAS_TAX + " FROM " + TABLE_ITEMS;


    public static final String CREATE_TABLE_COMPANY_INFO = "CREATE TABLE IF NOT EXISTS " + TABLE_COMPANY_INFO +
            "(" + COLUMN_COMPANY_NAME + " TEXT, " + COLUMN_COMPANY_ADDRESS + " TEXT," + COLUMN_COMPANY_PHONE_NUMBER + " TEXT, " +
            COLUMN_COMPANY_EMAIL + " TEXT)";

    public static final String INSERT_COMPANY_INFO = "INSERT INTO " + TABLE_COMPANY_INFO +
            '(' + COLUMN_ITEM_NAME + ", " + COLUMN_COMPANY_ADDRESS + ", " + COLUMN_COMPANY_PHONE_NUMBER + ", " + COLUMN_COMPANY_EMAIL + ") VALUES(?, ?, ?, ?)";

    public static final String QUERY_COMPANY_INFO = "SELECT * FROM " + TABLE_COMPANY_INFO;

    /**
     * Singleton instance of DataSource
     */
    private static DataSource instance = new DataSource();

    /**
     * Connection instance
     */
    private Connection conn;

    /**
     * Statements intances
     */
    private Statement createItemTable;
    private PreparedStatement queryItemWithName;
    private PreparedStatement queryItemWithPlu;
    private PreparedStatement insertIntoItemsTable;
    private PreparedStatement deleteFromItemsTable;
    private Statement queryAllItemsFromTable;
    private Statement createCompanyInfoTable;
    private PreparedStatement insertIntoCompanyInfoTable;
    private Statement queryCompanyInfo;

    /**
     * Observable list of itemsOnFile
     */
    private ObservableList<ItemOnFile> itemsOnFile;

    /**
     * Returns singleton instance of DataSource
     * @return instance of DataSource
     */
    public static DataSource getInstance(){
        return instance;
    }

    /**
     * Returns observable list of items on file
     * @return itemsOnFile
     */
    public ObservableList<ItemOnFile> getItemsOnFile(){
        return itemsOnFile;
    }

    /**
     * Creates all connections
     * @return true if successful
     */
    public boolean open(){
        try{
            itemsOnFile = FXCollections.observableArrayList();

            conn = DriverManager.getConnection(CONNECTION_STRING);

            createItemTable = conn.createStatement();
            createItemTable.execute(CREATE_TABLE_ITEMS);
            queryItemWithName = conn.prepareStatement(QUERY_ITEM_WITH_NAME);
            queryItemWithPlu = conn.prepareStatement(QUERY_ITEM_WITH_PLU);
            insertIntoItemsTable = conn.prepareStatement(INSERT_ITEM);
            deleteFromItemsTable = conn.prepareStatement(DELETE_ITEM);
            queryAllItemsFromTable = conn.createStatement();

            createCompanyInfoTable = conn.createStatement();
            createCompanyInfoTable.execute(CREATE_TABLE_COMPANY_INFO);
            insertIntoCompanyInfoTable = conn.prepareStatement(INSERT_COMPANY_INFO);
            queryCompanyInfo = conn.createStatement();
            return true;
        }catch (SQLException e){
            System.out.println(e.getMessage());
            return false;
        }
    }

    /**
     * Closes all connection
     */
    public void close(){
        try{
            if(createItemTable != null){
                createItemTable.close();
            }

            if(deleteFromItemsTable != null){
                deleteFromItemsTable.close();
            }

            if(insertIntoItemsTable != null){
                insertIntoItemsTable.close();
            }

            if(queryItemWithPlu != null){
                queryItemWithPlu.close();
            }

            if (queryItemWithName != null){
                queryItemWithName.close();
            }

            if(queryAllItemsFromTable != null){
                queryAllItemsFromTable.close();
            }

            if(createCompanyInfoTable != null){
                createCompanyInfoTable.close();
            }

            if(insertIntoCompanyInfoTable != null){
                insertIntoCompanyInfoTable.close();
            }

            if(queryCompanyInfo != null){
                queryCompanyInfo.close();
            }

            if(conn != null){
                conn.close();
            }
        }catch (SQLException e){
            System.out.println("Couldn't close connection: " + e.getMessage());
        }
    }

    /**
     * Inserts item into database
     * @param itemName   item name
     * @param itemPlu    item plu
     * @param itemPrice  price of item
     * @param hasTax     tax item
     * @return true if successful
     * @throws SQLException
     */
    public boolean insertItem(String itemName, int itemPlu, BigDecimal itemPrice, int hasTax) throws SQLException{
        queryItemWithName.setString(1, itemName);
        ResultSet results = queryItemWithName.executeQuery();
        if(results.next()){
            return false;
        }else{
            insertIntoItemsTable.setString(1, itemName);
            insertIntoItemsTable.setInt(2, itemPlu);
            insertIntoItemsTable.setBigDecimal(3,itemPrice);
            insertIntoItemsTable.setInt(4, hasTax);
            insertIntoItemsTable.executeUpdate();
            ItemOnFile item = new ItemOnFile(itemName, itemPlu, itemPrice, hasTax);
            itemsOnFile.add(item);
            return true;
        }
    }

    /**
     * Query database for item using plu
     * @param plu item plu
     * @return queried item
     * @throws SQLException
     */
    public Item getItemUsingPlu(int plu) throws SQLException{
        queryItemWithPlu.setInt(1, plu);
        ResultSet result = queryItemWithPlu.executeQuery();
        if(result.next()){
            String itemName = result.getString(1);
            BigDecimal itemPrice = result.getBigDecimal(2);
            Boolean itemHasTax = result.getInt(3) == 1 ? true : false;
            Item item = new Item(itemName, itemPrice, 1, itemHasTax);
            return item;
        }else{
            return null;
        }
    }

    /**
     * Query database for item using plu
     * @param plu       item plu
     * @param quantity  item quantity
     * @return queried item
     * @throws SQLException
     */
    public Item getItemUsingPlu(int plu, int quantity) throws SQLException{
        queryItemWithPlu.setInt(1, plu);
        ResultSet result = queryItemWithPlu.executeQuery();
        if(result.next()){
            String itemName = result.getString(1);
            BigDecimal itemPrice = result.getBigDecimal(2);
            Boolean itemHasTax = result.getInt(3) == 1 ? true : false;
            Item item = new Item(itemName, itemPrice, quantity, itemHasTax);
            return item;
        }else{
            return null;
        }
    }

    /**
     * Query database for item using name
     * @param itemName item name
     * @param quantity item quantity
     * @return queried item
     * @throws SQLException
     */
    public Item getItemUsingName(String itemName, int quantity) throws SQLException{
        queryItemWithName.setString(1, itemName);
        ResultSet result = queryItemWithName.executeQuery();
        if(result.next()){
            String name = result.getString(1);
            BigDecimal price = result.getBigDecimal(2);
            Boolean hasTax = result.getInt(3) == 1 ? true : false;
            Item item = new Item(name, price, quantity, hasTax);
            return item;
        }else{
            return null;
        }
    }

    /**
     * Delete item from database
     * @param itemName item name
     * @return true if successful
     * @throws SQLException
     */
    public boolean deleteItem(String itemName) throws SQLException{
        queryItemWithName.setString(1, itemName);
        ResultSet result = queryItemWithName.executeQuery();
        if(result.next()){
            deleteFromItemsTable.setString(1, itemName);
            deleteFromItemsTable.executeUpdate();
            itemsOnFile.clear();
            populateList();
            return true;
        }else{
            return false;
        }
    }

    /**
     * Populates itemsOnFile list
     */
    public void populateList() {
        try{
            ResultSet results = queryAllItemsFromTable.executeQuery(QUERY_ALL_ITEMS_FROM_TABLE);
            while (results.next()){
                String name = results.getString(1);
                int plu = results.getInt(2);
                BigDecimal price = results.getBigDecimal(3);
                int hasTax = results.getInt(4);
                ItemOnFile item = new ItemOnFile(name, plu, price, hasTax);
                itemsOnFile.add(item);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    /**
     * Inserts company info into database
     * @param name          Company name
     * @param address       Company address
     * @param phoneNumber   Company phone-number
     * @param email         Company email address
     * @throws SQLException
     */
    public void insertCompanyInfo(String name, String address, String phoneNumber, String email) throws SQLException {
        insertIntoCompanyInfoTable.setString(1, name);
        insertIntoCompanyInfoTable.setString(2, address);
        insertIntoCompanyInfoTable.setString(3, phoneNumber);
        insertIntoCompanyInfoTable.setString(4, email);
        insertIntoCompanyInfoTable.execute();
    }

    /**
     * Query database for company name
     * @return company info instance
     * @throws SQLException
     */
    public CompanyInfo getCompanyInfo() throws SQLException{
        ResultSet result = queryCompanyInfo.executeQuery(QUERY_COMPANY_INFO);
        CompanyInfo companyInfo = new CompanyInfo();
        companyInfo.setCompanyName(result.getString(1));
        companyInfo.setCompanyAddress(result.getString(2));
        companyInfo.setCompanyPhoneNumber(result.getString(3));
        companyInfo.setCompanyEmail(result.getString(4));
        return companyInfo;
    }
}
