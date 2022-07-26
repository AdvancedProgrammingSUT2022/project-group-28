package models;

import models.civilization.Civilization;
import models.tiles.enums.ResourceTemplate;

public class Trade {
    private final Civilization customer;
    private final Civilization seller;
    private final ResourceTemplate customerResource;
    private final int customerCount;
    private final ResourceTemplate sellerResource;
    private Boolean result;
    
    
    private final int sellerCount;
    public Trade(Civilization customer, Civilization seller,
    ResourceTemplate customerResource,ResourceTemplate sellerResource,
                 int customerCount, int sellerCount) {
        this.customer = customer;
        this.seller = seller;
        this.customerResource = customerResource;
        this.sellerResource = sellerResource;
        this.customerCount = customerCount;
        this.sellerCount = sellerCount;
    }
    
    public Civilization getCustomer() { return customer; }
    
    public Civilization getSeller() { return seller; }
    
    public ResourceTemplate getCustomerResource() { return customerResource; }
    
    public int getCustomerCount() { return customerCount; }
    
    public int getSellerCount() { return sellerCount; }
    
    public ResourceTemplate getSellerResource() {
        return sellerResource;
    }
    
    public boolean isResult() {
        return result;
    }
    
    public void setResult(boolean result) {
        this.result = result;
    }
}
