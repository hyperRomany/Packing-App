package com.example.packingapp.Database;

import android.content.Context;

import com.example.packingapp.model.DriverModules.DriverPackages_Details_DB;
import com.example.packingapp.model.DriverModules.DriverPackages_Header_DB;
import com.example.packingapp.model.GetOrderResponse.ItemsOrderDataDBDetails;
import com.example.packingapp.model.GetOrderResponse.ItemsOrderDataDBDetails_Scanned;
import com.example.packingapp.model.GetOrderResponse.OrderDataModuleDBHeader;
import com.example.packingapp.model.ModulesIDS;
import com.example.packingapp.model.RecievePacked.RecievePackedModule;
import com.example.packingapp.model.RecordsItem;
import com.example.packingapp.model.TrackingnumbersListDB;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;


@Database(entities = {RecordsItem.class , ModulesIDS.class , OrderDataModuleDBHeader.class,
        ItemsOrderDataDBDetails.class, ItemsOrderDataDBDetails_Scanned.class, TrackingnumbersListDB.class ,
        RecievePackedModule.class , DriverPackages_Header_DB.class ,
        DriverPackages_Details_DB.class}, version = 1, exportSchema = false)

    public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase mInstance;

    private static final String DATABASE_NAME = "PackingDB";

    public abstract UserDao userDao();
    public synchronized static AppDatabase getDatabaseInstance(Context context) {
        if (mInstance == null) {
            mInstance = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, DATABASE_NAME)
                    .allowMainThreadQueries()
        //            .addMigrations(MIGRATION_1_2)
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return mInstance;
    }
//TODO add migration to don't need to remove and install app again
    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {

    //        database.execSQL("ALTER TABLE DriverPackages_Details_DB ADD COLUMN  Checked_Item BOOLEAN " );
//"ALTER TABLE `PackingApp`.`vechile` \n"
//        "ADD COLUMN `test` VARCHAR(45) NULL AFTER `Weight`;"
        }
    };

    public static void destroyInstance() {
        mInstance = null;
    }

}
