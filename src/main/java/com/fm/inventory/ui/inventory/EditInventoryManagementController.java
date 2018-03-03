package com.fm.inventory.ui.inventory;

import com.fm.inventory.domain.Inventory;
import com.fm.inventory.domain.Part;
import com.fm.inventory.domain.Product;
import com.fm.inventory.exception.InventoryException;
import com.fm.inventory.ui.fxml.UtilMessages;
import com.fm.inventory.ui.fxml.UtilUI;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * This class is the controller for the main inventory UI
 */
public class EditInventoryManagementController implements Initializable {

    private Inventory inventory = Inventory.getInstance();

    private UtilUI utilUI;

    private UtilMessages utilMessages;

    @FXML
    private TableView<Part> partsTable;

    @FXML
    private TableView<Product> productTable;

    @FXML
    private TextField partSearchTxt;

    @FXML
    private TextField productSearchTxt;

    private final String DELETE_DIALOG_KEY = "dialog.DELETE_BUTTON_CONFIRMATION";

    /**
     * Constructor initializes the two helper instances for the ui transitions and message readers
     * @throws IOException
     */
    public EditInventoryManagementController() throws IOException {
        utilUI = UtilUI.getInstance();
        utilMessages = UtilMessages.getInstance();
    }

    /**
     * Initialize method initializes the UI TableViews
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        startTables();
    }

    private void startTables(){
        ObservableList<Part> partObservableList = partsTable.getItems();
        partObservableList.clear();
        partObservableList.addAll(inventory.getParts());

        ObservableList<Product> productObservableList = productTable.getItems();
        productObservableList.clear();
        productObservableList.addAll(inventory.getProducts());
    }

    /**
     * Method that handles the event of the button Add part. I clears the variable partSelected to make sure it is empty
     * and opens the Part UI
     * @param actionEvent
     * @throws IOException
     */
    public void handleAddPart(ActionEvent actionEvent) throws IOException {
        inventory.clearPartSelected();
        utilUI.openUI(actionEvent, UtilUI.UIEnum.ADD_PART_UI);
    }

    /**
     * Method that handles the event of the button Modify part. I gets the value of the row selected, with the id of that
     * part calls the method lookUp to get the part from the inventory, sets the selectedPart with that part retrieved,
     * and opens the Part UI
     * @param actionEvent
     * @throws IOException
     */
    public void handleModifyPart(ActionEvent actionEvent) throws IOException {
        Part part = partsTable.getSelectionModel().getSelectedItem();
        if(part == null) utilUI.openInformationDialog(utilMessages.getMessageByInventoryException(InventoryException.InventoryExceptionTypeEnum.NO_ROW_SELECTED));

        else {
            inventory.lookupPart(part.getPartID());
            utilUI.openUI(actionEvent, UtilUI.UIEnum.EDIT_PART_UI);
        }
    }

    /**
     * This method gets the value from the row selected and calls the method remove from the Inventory to delete the part
     * from it.
     * @param actionEvent
     */
    public void handleDeletePart(ActionEvent actionEvent) {
        try {
            if(utilUI.openConfirmationDialog(utilMessages.getMessageByKey(DELETE_DIALOG_KEY))) {
                Part part = partsTable.getSelectionModel().getSelectedItem();
                inventory.removePart(part.getPartID());
                startTables();
            }
        } catch (InventoryException e){
            utilUI.openWarningDialog(utilMessages.getMessageByInventoryException(e));
        }

    }

    /**
     * This method that handles the event search part, retrieves all the items in the table, and calls the method
     * searchParts to add to the table just the parts that fulfill the criteria of the method searchParts
     * @param actionEvent
     */
    public void handleSearchPart(ActionEvent actionEvent) {
        ObservableList<Part> partObservableList = partsTable.getItems();
        partObservableList.clear();
        partObservableList.addAll(searchParts(partSearchTxt.getText()));
    }

    /**
     * This method gets the partName 
     * @param partName
     * @return
     */
    private List<Part> searchParts(String partName){
        List<Part> listPartsReturn = null;
        if(partName == null) return inventory.getParts();

        listPartsReturn = new ArrayList<Part>();
        for(Part part: inventory.getParts()){
            if(part.getName().toUpperCase().contains(partName.toUpperCase())) listPartsReturn.add(part);
        }

        return listPartsReturn;
    }

    private List<Product> searchProducts(String productName){
        List<Product> listProductsReturn = null;
        if(productName == null) return inventory.getProducts();

        listProductsReturn = new ArrayList<Product>();
        for(Product product: inventory.getProducts()){
            if(product.getName().toUpperCase().contains(productName.toUpperCase())) listProductsReturn.add(product);
        }
        return listProductsReturn;
    }

    public void handleAddProduct(ActionEvent actionEvent) throws IOException {
        inventory.clearProductSelected();
        utilUI.openUI(actionEvent, UtilUI.UIEnum.ADD_PRODUCTOS_UI);
    }

    public void handleModifyProduct(ActionEvent actionEvent) throws IOException {
        Product product = productTable.getSelectionModel().getSelectedItem();
        if(product == null) utilUI.openInformationDialog(utilMessages.getMessageByInventoryException(InventoryException.InventoryExceptionTypeEnum.NO_ROW_SELECTED));

        else {
            inventory.lookupProduct(product.getProductID());
            utilUI.openUI(actionEvent, UtilUI.UIEnum.EDIT_PRODUCTOS_UI);
        }
    }

    public void handleDeleteProduct(ActionEvent actionEvent) {
        try{
            if(utilUI.openConfirmationDialog(utilMessages.getMessageByKey(DELETE_DIALOG_KEY))) {
                Product product = productTable.getSelectionModel().getSelectedItem();
                inventory.removeProduct(product.getProductID());
                startTables();
            }
        } catch (InventoryException e){
            utilUI.openWarningDialog(utilMessages.getMessageByInventoryException(e));
        }
    }

    public void handleSearchProduct(ActionEvent actionEvent){
        ObservableList<Product> productObservableList = productTable.getItems();
        productObservableList.clear();
        productObservableList.addAll(searchProducts(productSearchTxt.getText()));
    }

    public void handleExitButtonAction(ActionEvent actionEvent) {
        Platform.exit();
    }
}
