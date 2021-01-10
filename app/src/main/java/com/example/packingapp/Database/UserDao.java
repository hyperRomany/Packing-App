package com.example.packingapp.Database;

import com.example.packingapp.model.DriverModules.DriverPackages_Details_DB;
import com.example.packingapp.model.DriverModules.DriverPackages_Header_DB;
import com.example.packingapp.model.GetOrderResponse.ItemsOrderDataDBDetails;
import com.example.packingapp.model.GetOrderResponse.ItemsOrderDataDBDetails_Scanned;
import com.example.packingapp.model.GetOrderResponse.OrderDataModuleDBHeader;
import com.example.packingapp.model.ModulesIDS;
import com.example.packingapp.model.PackedPackageItemsModule;
import com.example.packingapp.model.PackedPackageModule;
import com.example.packingapp.model.RecievePacked.RecievePackedModule;
import com.example.packingapp.model.RecievePacked.RecievePackedModule_For_selection_loop_for_update;
import com.example.packingapp.model.RecievedPackageModule;
import com.example.packingapp.model.RecordsItem;
import com.example.packingapp.model.TrackingnumbersListDB;
import com.example.packingapp.model.ValidationforOrderNumberInPacked;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
import io.reactivex.Observable;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface UserDao {
   @Query("SELECT * FROM user")
   Observable<List<RecordsItem>> getUserData();

    @Query("SELECT * FROM user")
    RecordsItem getUserData_MU();


    @Insert(onConflict = REPLACE)
    void insertUser(RecordsItem mUser);

    @Insert(onConflict = REPLACE)
    void insertModules(List<ModulesIDS> mUser);

    @Query("SELECT * FROM ModulesIDS")
    List<ModulesIDS> getModules();

    @Delete
    void delete(RecordsItem mUser);

    @Update
    void updateUser(RecordsItem mUser);

    @Query("DELETE FROM user")
    void deleteAll();

    @Query("DELETE FROM ModulesIDS")
    void deleteAll_Modules();

//    @Query(" INSERT INTO OrderDataModuleDBHeader VALUES(" +)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertOrderHeader(OrderDataModuleDBHeader orderDataModuleDBHeader);

    @Query("UPDATE OrderDataModuleDBHeader SET OutBound_delivery = :OutBoundDelivery WHERE  Order_number in (:Ordernumber) ")
    void UpdateOutBoundDelievery(String OutBoundDelivery ,String Ordernumber);

     @Insert(onConflict = OnConflictStrategy.REPLACE)
     void insertOrderItems(List<ItemsOrderDataDBDetails> itemsOrderDataDBDetails);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertOrderItem(ItemsOrderDataDBDetails itemsOrderDataDBDetails);


    @Query("SELECT * FROM OrderDataModuleDBHeader")
    Observable<OrderDataModuleDBHeader> getHeader();


    @Query("SELECT * FROM OrderDataModuleDBHeader where Order_number =:Order_number")
    OrderDataModuleDBHeader getordernumberData(String Order_number);


    @Query("SELECT * FROM itemsOrderDataDBDetails where Order_number =:Order_number")
    List<ItemsOrderDataDBDetails> CheckordernumberData_inlast(String Order_number);

    @Query("SELECT * FROM itemsOrderDataDBDetails")
    List<ItemsOrderDataDBDetails> getDetailsTrackingnumberToUpload();

    @Query("SELECT * FROM ItemsOrderDataDBDetails_Scanned Where Order_number =:Order_number")
    List<ItemsOrderDataDBDetails_Scanned> getDetailsTrackingnumberToUpload_scannedbyordernumber(String Order_number);

    @Query("SELECT * FROM ItemsOrderDataDBDetails_Scanned where TrackingNumber=:TrackingNumber")
    List<ItemsOrderDataDBDetails_Scanned> getDetailsTrackingnumberToUpload(String TrackingNumber);

    @Query("SELECT * FROM TrackingnumbersListDB")
    List<TrackingnumbersListDB> getTrackingnumberDB();

    @Query("SELECT distinct(Ordernumber) FROM TrackingnumbersListDB")
    List<String> getOrdersNumberDB();

    @Query("SELECT distinct(TrackingNumber) FROM ItemsOrderDataDBDetails_Scanned where TrackingNumber LIKE :ORDER_NO ")
    List<String> getNoOfPackagesToUpload(String ORDER_NO);

    @Query("SELECT Order_number FROM OrderDataModuleDBHeader")
    String getOrderNumber();

    @Query("DELETE FROM OrderDataModuleDBHeader where Order_number =:Order_number")
    void deleteAllHeader(String Order_number);

    @Query("DELETE FROM itemsOrderDataDBDetails where Order_number =:Order_number")
    void deleteAllOrderItems(String Order_number);

    @Query("DELETE FROM TrackingnumbersListDB where Ordernumber =:Order_number")
    void deleteAllTrckingNumber(String Order_number);


    @Query("DELETE FROM ItemsOrderDataDBDetails_Scanned where Order_number =:Order_number")
    void deleteAllOrderItems_scanned(String Order_number);

    @Query("SELECT * FROM itemsOrderDataDBDetails where TrackingNumber =:tracking")
    Observable<List<ItemsOrderDataDBDetails>> getAllItem(String tracking);

//TODO use query to validation
    @Query("select order2  , sku2, ifnull(qty2,0) as'sumqty2',ifnull(qty1,0) as 'sumqty1' ,ifnull(qty2,0)-ifnull(qty1,0) as 'diff' from \n" +
            "(SELECT Order_number as 'order2',sku as'sku2', sum(ifnull(quantity,0))as 'qty2' from ItemsOrderDataDBDetails\n" +
            "group by sku\n" +
            ")as b\n" +
            "left outer join \n" +
            "(\n" +
            "SELECT Order_number as 'order1' ,sku as 'sku1', sum(ifnull(quantity,0))as 'qty1' from ItemsOrderDataDBDetails_Scanned \n" +
            "group by sku\n" +
            ")as a \n" +
            "on ( order2 = order1 and sku1 = sku2)\n" +
            "where sumqty2 <> sumqty1\n" +
            "and order2 =:Ordernumber")
    List<ValidationforOrderNumberInPacked> getAllItemsNotScannedORLessRequiredQTY(String Ordernumber);

    //TODO use query to validation
    @Query("select sku2 from (select order2  , sku2, ifnull(qty2,0) as'sumqty2',ifnull(qty1,0) as 'sumqty1' ,ifnull(qty2,0)-ifnull(qty1,0) as 'diff' from \n" +
            "(SELECT Order_number as 'order2',sku as'sku2', sum(ifnull(quantity,0))as 'qty2' from ItemsOrderDataDBDetails\n" +
            "group by sku\n" +
            ")as b\n" +
            "left outer join \n" +
            "(\n" +
            "SELECT Order_number as 'order1' ,sku as 'sku1', sum(ifnull(quantity,0))as 'qty1' from ItemsOrderDataDBDetails_Scanned \n" +
            "group by sku\n" +
            ")as a \n" +
            "on ( order2 = order1 and sku1 = sku2)\n" +
            "where sumqty2 <> sumqty1\n" +
            "and order2 =:Ordernumber )")
    List<String> getBarcodesAllItemsNotScannedORLessRequiredQTY(String Ordernumber);


    @Query("SELECT * FROM ItemsOrderDataDBDetails_Scanned where TrackingNumber is not null or TrackingNumber !=''")
    List<ItemsOrderDataDBDetails_Scanned> CheckItemsWithTrackingnumber();

    @Delete
    void deleteOrder(OrderDataModuleDBHeader mUser);

    @Query("SELECT * FROM itemsOrderDataDBDetails where Order_Number =:OrderNumber and sku =:barcode")
    List<ItemsOrderDataDBDetails> getItem(String OrderNumber , String barcode);

    @Query("SELECT * FROM ItemsOrderDataDBDetails_Scanned where sku =:barcode")
    List<ItemsOrderDataDBDetails_Scanned> getItem_scanned(String barcode);

    @Query("SELECT sum(quantity) FROM ItemsOrderDataDBDetails_Scanned where sku =:barcode")
    float getSumofScannedqty(String barcode);

    @Query("SELECT * FROM itemsOrderDataDBDetails where  sku LIKE :barcode")
    List<ItemsOrderDataDBDetails> getItem_scales(String barcode);

    @Query("SELECT * FROM TrackingnumbersListDB where TrackingNumber is not null and OrderNumber=:Ordernumber ORDER BY TrackingNumber DESC LIMIT 1")
    TrackingnumbersListDB getLastTrackingnumber(String Ordernumber);

//    @Query("UPDATE itemsOrderDataDBDetails SET TrackingNumber =:tracking where (SELECT * FROM itemsOrderDataDBDetails ORDER BY TrackingNumber DESC LIMIT 1)")
//    ItemsOrderDataDBDetails updatetrackingnumber(String tracking);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void Insertrackingnumber(TrackingnumbersListDB trackingnumbersListDB);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void InsertItemsDetails_scanned(ItemsOrderDataDBDetails_Scanned i);

    @Query("UPDATE itemsOrderDataDBDetails SET TrackingNumber = :tracking WHERE  sku in (:items) ")
     void updatetrackingnumberforListOfItems(String tracking , List<String> items);

    @Query("DELETE FROM ItemsOrderDataDBDetails_Scanned  WHERE Order_number=:Order_number and sku in (:items) ")
    void DeleteTrackingNumberFromDetailstable_using_sku( String Order_number , List<String> items);

    @Query("SELECT DISTINCT TrackingNumber FROM ItemsOrderDataDBDetails_Scanned where TrackingNumber is not null or TrackingNumber !=''")
    List<PackedPackageModule> getAllPckages_scanned();

    @Query("SELECT * FROM ItemsOrderDataDBDetails_Scanned where TrackingNumber =:TrackingNumber")
    List<PackedPackageItemsModule> getItemsOfTrackingNumber(String TrackingNumber);

    @Query("SELECT sku FROM ItemsOrderDataDBDetails_Scanned where Order_number=:Order_number and TrackingNumber =:TrackingNumber")
    List<String> getskuOfTrackingNumber(String Order_number , String TrackingNumber);

    @Query("UPDATE itemsOrderDataDBDetails SET TrackingNumber = NULL WHERE  TrackingNumber in (:tracking) ")
    void DeleteTrackingNumber(String tracking );

    //@Query("UPDATE TrackingnumbersListDB SET TrackingNumber = NULL WHERE  TrackingNumber in (:tracking) ")
    @Query("DELETE FROM TrackingnumbersListDB WHERE  TrackingNumber in (:tracking) ")
    void DeleteTrackingNumberFromtrackingtable(String tracking );

    @Query("DELETE FROM TrackingnumbersListDB WHERE OrderNumber=:OrderNumber and uid in (:uid) ")
    void DeleteTrackingNumberFromtrackingtable_using_uid (String OrderNumber , int uid );

    @Query("SELECT uid FROM TrackingnumbersListDB WHERE OrderNumber=:OrderNumber and  TrackingNumber = :tracking ")
    int GetUID( String OrderNumber , String tracking );

    @Query(" SELECT TrackingNumber from TrackingnumbersListDB WHERE  uid >(\n" +
            "  SELECT uid from TrackingnumbersListDB where OrderNumber=:OrderNumber and TrackingNumber = :tracking ORDER BY TrackingNumber \n" +
            "  ) ")
    List<String> GetTrackingNumbersAfterDeleteOne(String OrderNumber ,  String tracking );

    @Query(" SELECT  CAST(substr( TrackingNumber,instr( TrackingNumber ,'-')+1 , 1000 )" +
            " AS INT)-1  AS NewTrackingNumber  FROM TrackingnumbersListDB \n" +
            "WHERE  uid >(\n" +
            "  SELECT uid from TrackingnumbersListDB where OrderNumber=:OrderNumber and  TrackingNumber = :tracking " +
            "  )  ")
    List<Integer> GetNewTrackingNumbersAfterDeleteOne(String OrderNumber , String tracking );

    @Query("UPDATE TrackingnumbersListDB SET TrackingNumber = :trackingnew WHERE OrderNumber=:OrderNumber and TrackingNumber =:trackinglast ")
    void updatetrackingnumberAfterDeleteOne_ListDB(String OrderNumber , String trackingnew ,String trackinglast);

    @Query("UPDATE ItemsOrderDataDBDetails_Scanned SET TrackingNumber = :trackingnew WHERE  Order_number=:OrderNumber and TrackingNumber =:trackinglast ")
    void updatetrackingnumberAfterDeleteOne_Details(String OrderNumber , String trackingnew ,String trackinglast);

    @Query("DELETE FROM ItemsOrderDataDBDetails_Scanned WHERE Order_number=:OrderNumber and sku in (:Barcode) ")
    void DeleteItemfromScanned(String OrderNumber , String Barcode );

    @Insert(onConflict = REPLACE)
    void insertRecievePacked(RecievePackedModule recievePackedModule);

    @Query("SELECT * FROM RecievePackedModule where ORDER_NO =:ORDER_NO")
    List<RecievePackedModule> getRecievePacked_ORDER_NO(String ORDER_NO);

    @Query("SELECT * FROM RecievePackedModule where Tracking_Number =:Tracking_Number")
    List<RecievePackedModule> getRecievePacked_Tracking_Number(String Tracking_Number);

    @Query("SELECT * FROM RecievePackedModule where Tracking_Number =:Tracking_Number")
    List<RecievedPackageModule> getRecievePacked_Tracking_Number_ForSearch(String Tracking_Number);

    @Query("SELECT DISTINCT( ORDER_NO ) ,NO_OF_PACKAGES  , uid FROM RecievePackedModule ")
    List<RecievePackedModule> getRecievePacked_ORDER_NO_Distinct();

    //NO_OF_PACKAGES THAT RETURN FROM THIS QUERY IDECATE TO COUNT OF NO OF PACKAGES FOR ORDERNUMBER
    @Query("SELECT count(Tracking_Number) NO_OF_PACKAGES FROM RecievePackedModule where ORDER_NO =:ORDER_NO")
    List<String> getRecievePacked_Tracking_Number_count(String ORDER_NO);

    @Query("DELETE FROM RecievePackedModule")
    void deleteRecievePackedModule();

    @Query("UPDATE RecievePackedModule SET Zone = :zone WHERE  ORDER_NO in (:ORDER_NO) ")
    void UpdatezoneForORDER_NO(String ORDER_NO , String zone );

    @Query("SELECT * FROM RecievePackedModule")
    List<RecievePackedModule> getorderNORecievePackedModule();

    @Query("SELECT DISTINCT(ORDER_NO)   FROM RecievePackedModule")
    List<String> GetDistinctordernumbersFromRecievePackedModule_FOR_FORLoop();

    @Query("SELECT DISTINCT(ORDER_NO) ,NO_OF_PACKAGES , STATUS , DRIVER_ID,Zone  FROM RecievePackedModule")
    List<RecievePackedModule_For_selection_loop_for_update> GetDistinctordernumbersFromRecievePackedModule();

    @Query("SELECT * FROM RecievePackedModule")
    List<RecievedPackageModule> getAllRecievedPackages();

    @Query("DELETE FROM RecievePackedModule where Tracking_Number=:Tracking_Number")
    void deleteRecievePackedModule_ForTrackingNumber( String Tracking_Number);

    @Insert(onConflict = REPLACE)
    void insertDriverOrders(List<DriverPackages_Header_DB> driverPackages_Header_dblist);

    @Insert(onConflict = REPLACE)
    void insertDriverPackages(List<DriverPackages_Details_DB> driverPackages_details_dbs);

    @Query("SELECT * FROM DriverPackages_Header_DB")
    DriverPackages_Header_DB getDriverorder();

    @Query("UPDATE DriverPackages_Header_DB SET Passcode =:Passcode WHERE  ORDER_NO =:OrderNumber ")
    void UpdatePasscode(String OrderNumber ,String Passcode);

    @Query("SELECT Passcode FROM DriverPackages_Header_DB where ORDER_NO =:ORDER_NO")
    String getPasscode(String ORDER_NO);

    @Query("DELETE FROM DriverPackages_Header_DB")
    void deleteDriverPackages_Header_DB();
    @Query("DELETE FROM DriverPackages_Details_DB")
    void deleteDriverPackages_Details_DB();

    @Query("UPDATE DriverPackages_Details_DB SET STATUS =:STATUS ,  REASON =:REASON WHERE  TRACKING_NO =:TrackingNumber ")
    void UpdatestatusAndReason_ForTrackingnumber(String TrackingNumber , String STATUS , String REASON);

    @Query("UPDATE DriverPackages_Details_DB SET STATUS =:STATUS WHERE  ORDER_NO =:OrderNumber And REASON is null or REASON =''")
    void UpdatestatusForNotRejectWhenClickConfirmed(String OrderNumber ,String STATUS );

    @Query("SELECT * FROM DriverPackages_Details_DB where REASON is not null or REASON !=''")
    List<DriverPackages_Details_DB> getAllPckagesForReject();

    @Query("SELECT * FROM DriverPackages_Details_DB WHERE  ORDER_NO =:OrderNumber")
    List<DriverPackages_Details_DB> getAllPckagesForUpload(String OrderNumber);

    @Query("select sum(quantity) FROM itemsOrderDataDBDetails ")
    float SumOfQTYFromDetials();

    @Query("SELECT * FROM RecievePackedModule")
    List<RecievePackedModule> getRecievePackedModule();

    @Query("SELECT * FROM OrderDataModuleDBHeader where Order_number =:OrderNumber")
    OrderDataModuleDBHeader getHeaderToUpload(String OrderNumber);

    @Query("SELECT * FROM OrderDataModuleDBHeader where Order_number =:OrderNumber")
    List<OrderDataModuleDBHeader> getHeaderToUpload_list(String OrderNumber);

    @Query("SELECT * FROM TrackingnumbersListDB")
    List<TrackingnumbersListDB> countShipment();

    @Query("select Tracking_Number /*, NO_OF_PACKAGES,CountTrackingnumber*/ from (select ORDER_NO , NO_OF_PACKAGES , Tracking_Number, count(ifnull(Tracking_Number,0)) as CountTrackingnumber from RecievePackedModule \n" +
            "group by ORDER_NO )\n" +
            "where  NO_OF_PACKAGES <> CountTrackingnumber")
    List<String> getTrackingnumber_of_ordersThatNotcompleteAllpackages();
}
