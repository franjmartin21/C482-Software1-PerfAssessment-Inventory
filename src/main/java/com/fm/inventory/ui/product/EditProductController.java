package com.fm.inventory.ui.product;

import com.fm.inventory.domain.Inventory;
import com.fm.inventory.domain.Part;
import com.fm.inventory.domain.Product;
import com.fm.inventory.exception.InventoryException;
import com.fm.inventory.ui.fxml.UtilMessages;
import com.fm.inventory.ui.fxml.UtilUI;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Created by francisco on 4/24/17.
 */
public class EditProductController implements Initializable {

    enum OperationEnum{
        ADD("Add Product"),
        MODIFY("Modify Product");

        private String labelTxt;

        OperationEnum(String labelTxt){
            this.labelTxt = labelTxt;
        }

        public String getLabelTxt() {
            return labelTxt;
        }
    }
    @FXML
    private Label operationLbl;

    @FXML
    private TextField idTxt;

    @FXML
    private TextField nameTxt;

    @FXML
    private TextField invTxt;

    @FXML
    private TextField priceTxt;

    @FXML
    private TextField maxTxt;

    @FXML
    private TextField minTxt;

    @FXML
    private TextField partSearchTxt;

    @FXML
    private TableView<Part> partsNotIncluded;

    @FXML
    private TableView<Part> partsIncluded;

    private UtilUI utilUI;

    private UtilMessages utilMessages;

    private OperationEnum operation;

    private Inventory inventory = Inventory.getInstance();

    private Product productSelected;

    private final String CANCEL_DIALOG_KEY = "dialog.CANCEL_BUTTON_CONFIRMATION";

    public EditProductController() throws IOException {
        this.utilUI = UtilUI.getInstance();
        this.utilMessages = UtilMessages.getInstance();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        operation = OperationEnum.ADD;
        productSelected = inventory.getProductSelected();
        if(inventory.getProductSelected() != null) {
            operation = OperationEnum.MODIFY;
            informFields();
        }
        else{
            generateDefaultValues();
            partsNotIncluded.getItems().addAll(inventory.getParts());
        }
    }

    private void generateDefaultValues(){
        idTxt.setText(String.valueOf(Inventory.getNextIdForProduct()));
        invTxt.setText("0");
        maxTxt.setText("0");
        minTxt.setText("0");
        priceTxt.setText("0.0");
    }

    private void storeFields(ActionEvent actionEvent) throws IOException{
        Product product = getProductFromFields();
        try {
            if (operation == OperationEnum.ADD)
                inventory.addProduct(product);
            else
                inventory.updateProduct(product.getProductID(), product);
            utilUI.openUI(actionEvent, UtilUI.UIEnum.INVENTORY_UI);
        } catch (InventoryException e){
            List<String> messages = utilMessages.getMessageListByInventoryException(e);
            utilUI.openWarningDialog(messages);
        }
    }

    private Product getProductFromFields(){
        Product product= new Product();
        product.setName(nameTxt.getText());
        product.setInstock(Integer.valueOf(invTxt.getText()));
        product.setMin(Integer.valueOf(minTxt.getText()));
        product.setMax(Integer.valueOf(maxTxt.getText()));
        product.setProductID(Integer.valueOf(idTxt.getText()));
        product.setPrice(Double.valueOf(priceTxt.getText()).doubleValue());
        for(Part part: partsIncluded.getItems())
            product.addPart(part);
        return product;
    }

    private void informFields(){
        operationLbl.setText(OperationEnum.MODIFY.getLabelTxt());
        idTxt.setText(String.valueOf(productSelected.getProductID()));
        nameTxt.setText(productSelected.getName());
        invTxt.setText(String.valueOf(productSelected.getInstock()));
        priceTxt.setText(String.valueOf(productSelected.getPrice()));
        maxTxt.setText(String.valueOf(productSelected.getMax()));
        minTxt.setText(String.valueOf(productSelected.getMin()));
        partsIncluded.getItems().addAll(productSelected.getParts());
        partsNotIncluded.getItems().addAll(getPartsNotIncluded(inventory.getParts(), productSelected.getParts()));
    }

    private List<Part> getPartsNotIncluded(List<Part> partsInventory, List<Part> partsProduct){
        if(partsInventory == null) return null;

        if(partsProduct == null) return partsInventory;

        List<Part> partsNotIncluded = new ArrayList<Part>();
        for(Part part: partsInventory){
            if(!partsProduct.contains(part))
                partsNotIncluded.add(part);
        }
        return partsNotIncluded;
    }

    public void handleCancelAction(ActionEvent actionEvent) throws IOException {
        if(utilUI.openConfirmationDialog(utilMessages.getMessageByKey(CANCEL_DIALOG_KEY)))
            utilUI.openUI(actionEvent, UtilUI.UIEnum.INVENTORY_UI);
    }

    public void handleGuardarAction(ActionEvent actionEvent) throws IOException{
        storeFields(actionEvent);
    }


    private boolean partIsSelected(Part part){
        if(part == null){
            utilUI.openWarningDialog(utilMessages.getMessageByInventoryException(InventoryException.InventoryExceptionTypeEnum.NO_ROW_SELECTED));
            return false;
        }
        return true;
    }

    public void handleAddPart(ActionEvent actionEvent){
        Part part = partsNotIncluded.getSelectionModel().getSelectedItem();
        if(partIsSelected(part)) {
            partsIncluded.getItems().add(part);
            partsNotIncluded.getItems().remove(part);
        }
    }

    public void handleDeletePart(ActionEvent actionEvent){
        Part part = partsIncluded.getSelectionModel().getSelectedItem();
        if(partIsSelected(part)) {
            partsIncluded.getItems().remove(part);
            partsNotIncluded.getItems().add(part);
        }
    }

    public void handleSearch(ActionEvent actionEvent){
        ObservableList<Part> partObservableList = partsNotIncluded.getItems();
        partObservableList.clear();
        List<Part> partsNotCurrentlyIncluded = getPartsNotIncluded(inventory.getParts(), partsIncluded.getItems());
        partObservableList.addAll(searchParts(partsNotCurrentlyIncluded, partSearchTxt.getText()));
    }

    private List<Part> searchParts(List<Part> partsNotIncludedList, String partName){
        List<Part> listPartsReturn = null;
        if(partName == null) return partsNotIncludedList;

        listPartsReturn = new ArrayList<Part>();
        for(Part part: partsNotIncludedList){
            if(part.getName().toUpperCase().contains(partName.toUpperCase())) listPartsReturn.add(part);
        }
        return listPartsReturn;
    }

}
