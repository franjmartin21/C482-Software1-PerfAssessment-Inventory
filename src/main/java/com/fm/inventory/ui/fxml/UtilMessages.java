package com.fm.inventory.ui.fxml;

import com.fm.inventory.exception.InventoryException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Created by francisco on 5/25/17.
 */
public class UtilMessages {

    private final static String PROPERTY_FILE = "/message.properties";

    private final static String MESSAGE_STR_PREFIX = "exception.";

    private static UtilMessages instance;

    private Properties prop = new Properties();

    private UtilMessages() throws IOException {
        final InputStream input = this.getClass().getResourceAsStream(PROPERTY_FILE);
        prop.load(input);
    }

    public static synchronized UtilMessages getInstance() throws IOException {
        if(instance == null) instance = new UtilMessages();

        return instance;
    }

    public String getMessageByKey(String key){
        return prop.getProperty(key);
    }

    public String getMessageByInventoryException(InventoryException.InventoryExceptionTypeEnum typeEnum){
        return prop.getProperty(MESSAGE_STR_PREFIX + String.valueOf(typeEnum));
    }

    public String getMessageByInventoryException(InventoryException ex){
        return prop.getProperty(MESSAGE_STR_PREFIX + String.valueOf(ex.getInventoryExceptionTypeEnumList().get(0)));
    }

    public List<String> getMessageListByInventoryException(InventoryException ex){
        if(ex == null || ex.getInventoryExceptionTypeEnumList() == null ||  ex.getInventoryExceptionTypeEnumList().isEmpty());

        List<String> messageList = new ArrayList<String>();
        for(InventoryException.InventoryExceptionTypeEnum inventoryExceptionTypeEnum: ex.getInventoryExceptionTypeEnumList()){
            messageList.add(prop.getProperty(MESSAGE_STR_PREFIX + String.valueOf(inventoryExceptionTypeEnum)));
        }
        return messageList;
    }
}
