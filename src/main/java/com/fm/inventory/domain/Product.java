package com.fm.inventory.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by francisco on 4/21/17.
 */
public class Product {

    private List<Part> parts;

    private int productID;

    private String name;

    private double price;

    private int instock;

    private int min;

    private int max;

    public Product(){
        this.parts = new ArrayList<Part>();
    }

    public void addPart(Part part){
        this.parts.add(part);
    }

    //NOT USING THIS METHODS, I INCLUDED THEM INTO THE INVENTORY CLASS WHERE I AM ACTUALLY USING THEM
    //TO MANIPULATE CHANGES IN THE LIST OF PARTS
    public void removePart(int i){ //UML says boolean, will need to ask for confirmation on it
        this.parts.remove(i);
    }

    public Part lookupPart(int i){
        return this.parts.get(i);
    }

    public void updatePart(int i){}

    public List<Part> getParts() {
        return parts;
    }

    public int getProductID() {
        return productID;
    }

    public void setProductID(int productID) {
        this.productID = productID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getInstock() {
        return instock;
    }

    public void setInstock(int instock) {
        this.instock = instock;
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }
}
