/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fm.inventory.ui.inventory;

import com.fm.inventory.domain.Inhouse;
import com.fm.inventory.domain.Inventory;
import com.fm.inventory.domain.Outsourced;
import com.fm.inventory.domain.Product;
import com.fm.inventory.exception.InventoryException;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableView;
import javafx.stage.Stage;


public class InventoryManagementFXML extends Application {

    private Inventory inventory = Inventory.getInstance();

    @FXML
    private TableView partsTable;
    
    @Override
    public void start(Stage stage) throws Exception {
        initializeData();
        Parent root = FXMLLoader.load(getClass().getResource("../fxml/inventory_management.fxml"));

        Scene scene = new Scene(root);
        scene.getStylesheets().add(InventoryManagementFXML.class.getResource("../css/style.css").toExternalForm());
        stage.setTitle("Inventory App");
        stage.setScene(scene);
        stage.show();
    }

    private void initializeData() throws InventoryException{
        /**
         * Temporary mock data
         */
        Outsourced part1 = new Outsourced();
        part1.setInstock(5);
        part1.setName("Part 1");
        part1.setMax(10);
        part1.setMin(1);
        part1.setPartID(Inventory.getNextIdForPart());
        part1.setPrice(3.34);
        part1.setCompanyName("Automasa");
        Inhouse part2 = new Inhouse();
        part2.setInstock(10);
        part2.setName("Part 2");
        part2.setMax(20);
        part2.setMin(1);
        part2.setPartID(Inventory.getNextIdForPart());
        part2.setPrice(4.65);
        part2.setMachine(345);
        inventory.addPart(part1);
        inventory.addPart(part2);

        Product product = new Product();
        product.setProductID(Inventory.getNextIdForProduct());
        product.setName("Mi product");
        product.setInstock(12);
        product.setPrice(12.54);
        product.setMin(1);
        product.setMax(20);
        product.addPart(part1);
        inventory.addProduct(product);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);

    }
    
}
