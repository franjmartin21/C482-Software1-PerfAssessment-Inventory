package com.fm.inventory.domain;

import com.fm.inventory.exception.InventoryException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by francisco on 4/21/17.
 */
public class Inventory {

    private static Inventory instance;

    private List<Product> products;

    private List<Part> parts;

    private Product productSelected;

    private Part partSelected;

    private static int lastIdProduct = 1111;

    private static int lastIdPart = 111;

    private Inventory(){
        parts = new ArrayList<Part>();
        products = new ArrayList<Product>();
    }

    public static synchronized Inventory getInstance(){
        if(instance == null) instance = new Inventory();

        return instance;
    }

    public static int getNextIdForPart(){
        return lastIdPart++;
    }

    public static int getNextIdForProduct(){
        return lastIdProduct++;
    }

    public void addProduct(Product product) throws InventoryException{
        validateProduct(product);
        products.add(product);
    }

    public void removeProduct(int productID) throws InventoryException {
        Product product = lookupProduct(productID);
        validateRemoveProduct(product);
        products.remove(product);
    }

    public Product lookupProduct(int productID){
        for(Product product: products){
            if(productID == product.getProductID())
                productSelected = product;
        }
        return productSelected;
    }

    public void updateProduct(int productID, Product product) throws InventoryException{
        validateProduct(product);
        for(int i = 0; i < products.size(); i++){
            if(products.get(i).getProductID() == productID){
                products.remove(i);
                products.add(i, product);
            }
        }
    }

    private void validateProduct(Product product) throws InventoryException {

        InventoryException inventoryException = new InventoryException();

        if(product.getName() == null || "".equals(product.getName())) inventoryException.addInventoryExceptionType(InventoryException.InventoryExceptionTypeEnum.NAME_NOT_INFORMED);

        if(product.getPrice() <= 0) inventoryException.addInventoryExceptionType(InventoryException.InventoryExceptionTypeEnum.PRICE_NOT_INFORMED);

        if(product.getInstock() <= 0) inventoryException.addInventoryExceptionType(InventoryException.InventoryExceptionTypeEnum.INV_NOT_INFORMED);

        if(product.getMin() > product.getMax()) inventoryException.addInventoryExceptionType(InventoryException.InventoryExceptionTypeEnum.MAX_HIGHERTHAN_MIN);

        if(product.getInstock() > product.getMax()) inventoryException.addInventoryExceptionType(InventoryException.InventoryExceptionTypeEnum.INVVALUE_EXCEEDS_MAXIMUM);

        if(product.getInstock() < product.getMin()) inventoryException.addInventoryExceptionType(InventoryException.InventoryExceptionTypeEnum.INVVALUE_EXCEEDS_MINIMUM);

        if(product.getParts() == null || product.getParts().isEmpty()) inventoryException.addInventoryExceptionType(InventoryException.InventoryExceptionTypeEnum.PRODUCT_HAS_NOT_PART);

        if(!validatePriceProductWithPriceParts(product)) inventoryException.addInventoryExceptionType(InventoryException.InventoryExceptionTypeEnum.PRICE_PARTS_HIGHER_THAN_PRODUCT);

        if(!inventoryException.getInventoryExceptionTypeEnumList().isEmpty()) throw inventoryException;
    }

    private boolean validatePriceProductWithPriceParts(Product product){
        if(product == null || product.getParts() == null || product.getParts().isEmpty()) return true;

        double priceProducts = 0;
        for(Part part: product.getParts())
            priceProducts += part.getPrice();

        return priceProducts <= product.getPrice();
    }

    public void addPart(Part part) throws InventoryException{
        validatePart(part);
        parts.add(part);
    }

    public Part lookupPart(int partID){
        for(Part part: parts){
            if(partID == part.getPartID())
                partSelected = part;
        }
        return partSelected;
    }

    /**
     * Changed signature of this method
     */
    public void updatePart(int partID, Part part) throws InventoryException{
        validatePart(part);
        for(int i = 0; i < parts.size(); i++){
            if(parts.get(i).getPartID() == partID){
                parts.remove(i);
                parts.add(i, part);
            }
        }
    }

    public void removePart(int partID) throws InventoryException{
        validateRemovePart(lookupPart(partID));

        for(int i = 0; i < parts.size(); i++){
            if(parts.get(i).getPartID() == partID){
                parts.remove(i);
            }
        }
    }

    private void validatePart(Part part) throws InventoryException{
        InventoryException inventoryException = new InventoryException();

        if(part.getName() == null || "".equals(part.getName())) inventoryException.addInventoryExceptionType(InventoryException.InventoryExceptionTypeEnum.NAME_NOT_INFORMED);

        if(part.getPrice() <= 0) inventoryException.addInventoryExceptionType(InventoryException.InventoryExceptionTypeEnum.PRICE_NOT_INFORMED);

        if(part.getInstock() <= 0) inventoryException.addInventoryExceptionType(InventoryException.InventoryExceptionTypeEnum.INV_NOT_INFORMED);

        if(part.getMin() > part.getMax()) inventoryException.addInventoryExceptionType(InventoryException.InventoryExceptionTypeEnum.MAX_HIGHERTHAN_MIN);

        if(part.getInstock() > part.getMax()) inventoryException.addInventoryExceptionType(InventoryException.InventoryExceptionTypeEnum.INVVALUE_EXCEEDS_MAXIMUM);

        if(part.getInstock() < part.getMin()) inventoryException.addInventoryExceptionType(InventoryException.InventoryExceptionTypeEnum.INVVALUE_EXCEEDS_MINIMUM);

        if(!inventoryException.getInventoryExceptionTypeEnumList().isEmpty()) throw inventoryException;
    }

    private void validateRemoveProduct(Product product) throws InventoryException{
        if(product.getParts() != null && !product.getParts().isEmpty()) throw new InventoryException(InventoryException.InventoryExceptionTypeEnum.PRODUCT_HAS_PARTS);
    }

    private void validateRemovePart(Part part) throws InventoryException{
        if(products != null && !products.isEmpty() && part != null){
            for(Product product: products){
                validatePartInProduct(product, part);
            }
        }
    }

    private void validatePartInProduct(Product product, Part partSelect) throws InventoryException{
        for(Part part: product.getParts()){
            if(partSelect.getPartID() == part.getPartID())
                throw new InventoryException(InventoryException.InventoryExceptionTypeEnum.PART_IN_PRODUCT);
        }
    }

    public List<Product> getProducts() {
        return products;
    }

    public List<Part> getParts() {
        return parts;
    }

    public void clearProductSelected() {
        this.productSelected = null;
    }

    public void clearPartSelected() {
        this.partSelected = null;
    }

    public Product getProductSelected() {
        return productSelected;
    }

    public Part getPartSelected() {
        return partSelected;
    }
}
