package com.fm.inventory.exception;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by francisco on 5/24/17.
 */
public class InventoryException extends Exception {

    public enum InventoryExceptionTypeEnum {
        NO_ROW_SELECTED,
        INVENTORY_PROBLEM,
        NAME_NOT_INFORMED,
        PRICE_NOT_INFORMED,
        INV_NOT_INFORMED,
        PART_IN_PRODUCT,
        MAX_HIGHERTHAN_MIN,
        INVVALUE_EXCEEDS_MINIMUM,
        INVVALUE_EXCEEDS_MAXIMUM,
        PRODUCT_HAS_NOT_PART,
        PRODUCT_HAS_PARTS,
        PRICE_PARTS_HIGHER_THAN_PRODUCT,
    }

    private List<InventoryExceptionTypeEnum> inventoryExceptionTypeEnumList;

    public InventoryException(){
        super();
        this.inventoryExceptionTypeEnumList = new ArrayList<InventoryExceptionTypeEnum>();
    }

    public InventoryException(InventoryExceptionTypeEnum inventoryExceptionTypeEnum){
        super();
        this.inventoryExceptionTypeEnumList = new ArrayList<InventoryExceptionTypeEnum>();
        this.inventoryExceptionTypeEnumList.add(inventoryExceptionTypeEnum);
    }

    public void addInventoryExceptionType(InventoryExceptionTypeEnum inventoryExceptionTypeEnum){
        this.inventoryExceptionTypeEnumList.add(inventoryExceptionTypeEnum);
    }

    public List<InventoryExceptionTypeEnum> getInventoryExceptionTypeEnumList() {
        return inventoryExceptionTypeEnumList;
    }
}
