package com.fm.inventory.ui.part;

import com.fm.inventory.domain.Inhouse;
import com.fm.inventory.domain.Inventory;
import com.fm.inventory.domain.Outsourced;
import com.fm.inventory.domain.Part;
import com.fm.inventory.exception.InventoryException;
import com.fm.inventory.ui.fxml.UtilMessages;
import com.fm.inventory.ui.fxml.UtilUI;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Created by francisco on 4/24/17.
 */
public class EditPartController implements Initializable {

    static class EditPartType{

        enum PartType{
            IN_HOUSE,
            OUTSOURCED
        }

        PartType partType;

        String labelStr;

        Label variableFieldLbl;

        RadioButton radioButton;

        TextField textField;

        EditPartType(PartType partType, String labelStr, Label variableFieldLbl, RadioButton radioButton, TextField textField){
            this.partType = partType;
            this.labelStr = labelStr;
            this.variableFieldLbl = variableFieldLbl;
            this.radioButton = radioButton;
            this.textField = textField;
        }

        public PartType getPartType() {
            return partType;
        }

        public String getLabelStr() {
            return labelStr;
        }

        public Label getVariableFieldLbl() {
            return variableFieldLbl;
        }

        public RadioButton getRadioButton() {
            return radioButton;
        }

        public TextField getTextField() {
            return textField;
        }
    }

    private EditPartType outsourcedPartType;

    private EditPartType inhousePartType;

    private EditPartType editPartTypeSelected;

    private UtilUI utilUI;

    private UtilMessages utilMessages;

    private Inventory inventory = Inventory.getInstance();

    enum OperationEnum{
        ADD("Add Part"),
        MODIFY("Modify Part");

        private String labelTxt;

        OperationEnum(String labelTxt){
            this.labelTxt = labelTxt;
        }

        public String getLabelTxt() {
            return labelTxt;
        }
    }
    private OperationEnum operation;

    @FXML
    private Label operationLbl;

    @FXML
    private RadioButton outsourcedRbtn;

    @FXML
    private RadioButton inhouseRbtn;

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
    private TextField companyNameTxt;

    @FXML
    private TextField machineIDTxt;

    @FXML
    private Label variableFieldLbl;

    private final String CANCEL_DIALOG_KEY = "dialog.CANCEL_BUTTON_CONFIRMATION";

    public EditPartController() throws IOException {
        this.utilUI = UtilUI.getInstance();
        this.utilMessages = UtilMessages.getInstance();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        operation = OperationEnum.ADD;
        outsourcedPartType = new EditPartType(EditPartType.PartType.OUTSOURCED,"Company Name", variableFieldLbl, outsourcedRbtn, companyNameTxt);
        inhousePartType = new EditPartType(EditPartType.PartType.IN_HOUSE,"Machine ID", variableFieldLbl, inhouseRbtn, machineIDTxt);
        if(inventory.getPartSelected() != null) {
            operation = OperationEnum.MODIFY;
            informFields(inventory.getPartSelected());
        }
        else{
            changeTypeForm(inhousePartType);
            generateDefaultValues();
        }
        operationLbl.setText(operation.getLabelTxt());
    }

    private void generateDefaultValues(){
        idTxt.setText(String.valueOf(Inventory.getNextIdForPart()));
        invTxt.setText("0");
        priceTxt.setText("0.0");
        maxTxt.setText("0");
        minTxt.setText("0");
        machineIDTxt.setText("0");
    }

    private void informFields(Part part){
        operationLbl.setText(OperationEnum.MODIFY.getLabelTxt());
        if(part instanceof Outsourced){
            changeTypeForm(outsourcedPartType);
            companyNameTxt.setText(((Outsourced)part).getCompanyName());
        }
        else if (part instanceof Inhouse){
            changeTypeForm(inhousePartType);
            machineIDTxt.setText(String.valueOf(((Inhouse)part).getMachine()));
        }
        idTxt.setText(String.valueOf(part.getPartID()));
        nameTxt.setText(part.getName());
        invTxt.setText(String.valueOf(part.getInstock()));
        priceTxt.setText(String.valueOf(part.getPrice()));
        maxTxt.setText(String.valueOf(part.getMax()));
        minTxt.setText(String.valueOf(part.getMin()));
    }

    private void storeFields(ActionEvent actionEvent) throws IOException{
        Part part = getPartFromFields();
        try {
            if (operation == OperationEnum.ADD)
                inventory.addPart(part);
            else
                inventory.updatePart(part.getPartID(), part);
            utilUI.openUI(actionEvent, UtilUI.UIEnum.INVENTORY_UI);
        } catch (InventoryException e){
            List<String> messages = utilMessages.getMessageListByInventoryException(e);
            utilUI.openWarningDialog(messages);
        }
    }

    private Part getPartFromFields(){
        Part part = null;
        if(editPartTypeSelected.getPartType() == EditPartType.PartType.IN_HOUSE){
            part = new Inhouse();
            ((Inhouse)part).setMachine(machineIDTxt.getText() != null && !"".equals(machineIDTxt.getText())? Integer.valueOf(machineIDTxt.getText()): 0);
        }  else if(editPartTypeSelected.getPartType() == EditPartType.PartType.OUTSOURCED){
            part = new Outsourced();
            ((Outsourced)part).setCompanyName(companyNameTxt.getText());
        }
        part.setName(nameTxt.getText());
        part.setInstock(invTxt.getText() != null? Integer.valueOf(invTxt.getText()): 0);
        part.setMin(minTxt.getText() != null && !"".equals(minTxt.getText())? Integer.valueOf(minTxt.getText()): 0);
        part.setMax(maxTxt.getText() != null && !"".equals(maxTxt.getText())? Integer.valueOf(maxTxt.getText()): 0);
        part.setPartID(Integer.valueOf(idTxt.getText()));
        part.setPrice(priceTxt.getText() != null && !"".equals(priceTxt.getText())? Double.valueOf(priceTxt.getText()).doubleValue(): 0.0);
        return part;
    }

    private void changeTypeForm(EditPartType editPartType){
        editPartTypeSelected = editPartType;
        companyNameTxt.setVisible(false);
        machineIDTxt.setVisible(false);
        editPartTypeSelected.getRadioButton().setSelected(true);
        editPartTypeSelected.getTextField().setVisible(true);
        editPartTypeSelected.getVariableFieldLbl().setText(editPartTypeSelected.getLabelStr());
    }

    public void handleChangeTypeRbtn(ActionEvent actionEvent) throws IOException {
        if(actionEvent.getSource() == outsourcedRbtn) changeTypeForm(outsourcedPartType);
        if(actionEvent.getSource() == inhouseRbtn) changeTypeForm(inhousePartType);
    }

    public void handleCancelAction(ActionEvent actionEvent) throws IOException {
        if(utilUI.openConfirmationDialog(utilMessages.getMessageByKey(CANCEL_DIALOG_KEY)))
            utilUI.openUI(actionEvent, UtilUI.UIEnum.INVENTORY_UI);
    }

    public void handleGuardarAction(ActionEvent actionEvent) throws IOException{
        storeFields(actionEvent);
    }

}
