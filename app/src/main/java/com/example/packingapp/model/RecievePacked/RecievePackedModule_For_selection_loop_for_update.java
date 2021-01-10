package com.example.packingapp.model.RecievePacked;

import androidx.room.Entity;

@Entity(tableName = "RecievePackedModule")
public class RecievePackedModule_For_selection_loop_for_update {



        private String ORDER_NO;


        private String NO_OF_PACKAGES;

              private String STATUS;




    private String DRIVER_ID;



        private String Zone;

    public RecievePackedModule_For_selection_loop_for_update() {
    }

    public RecievePackedModule_For_selection_loop_for_update(String ORDER_NO, String NO_OF_PACKAGES, String STATUS, String DRIVER_ID, String zone) {
        this.ORDER_NO = ORDER_NO;
        this.NO_OF_PACKAGES = NO_OF_PACKAGES;
        this.STATUS = STATUS;
        this.DRIVER_ID = DRIVER_ID;
        Zone = zone;
    }

    public String getORDER_NO() {
        return ORDER_NO;
    }

    public void setORDER_NO(String ORDER_NO) {
        this.ORDER_NO = ORDER_NO;
    }

    public String getNO_OF_PACKAGES() {
        return NO_OF_PACKAGES;
    }

    public void setNO_OF_PACKAGES(String NO_OF_PACKAGES) {
        this.NO_OF_PACKAGES = NO_OF_PACKAGES;
    }

    public String getSTATUS() {
        return STATUS;
    }

    public void setSTATUS(String STATUS) {
        this.STATUS = STATUS;
    }

    public String getDRIVER_ID() {
        return DRIVER_ID;
    }

    public void setDRIVER_ID(String DRIVER_ID) {
        this.DRIVER_ID = DRIVER_ID;
    }

    public String getZone() {
        return Zone;
    }

    public void setZone(String zone) {
        Zone = zone;
    }
}
