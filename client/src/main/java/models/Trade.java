package models;

import models.civilization.Civilization;
import models.tiles.enums.ResourceTemplate;

public class Trade {
    public enum Result{
        OFFER,
        ACCEPT,
        REJECT;
    }

    private final Civilization customer;
    private final Civilization seller;
    private final ResourceTemplate customerResource;
    private final int customerCount;
    private final ResourceTemplate sellerResource;
    private Result result;
    private final String message;
    
    
    
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
        this.result = Result.OFFER;
        message = "You have a trade offer from " + this.getCustomer().getCivilizationNames().getName() + 
        "\n to trade " + this.getSellerCount() + " " + this.getSellerResource() + 
        "\n for "+ this.getCustomerCount() + " " + this.getCustomerResource() + ".";
    }
    
    public Civilization getCustomer() { return customer; }
    
    public Civilization getSeller() { return seller; }
    
    public String getCustomerResource() { 
        if (customerResource == null) return "Coin";
        return customerResource.getName();
    }
    
    public int getCustomerCount() { return customerCount; }
    
    public int getSellerCount() { return sellerCount; }
    
    public String getSellerResource() {
        if (sellerResource == null) return "Coin";
        return sellerResource.getName();
    }
    
    public String getMessage() {
        return message;
    }

    public Result getResult() {
        return result;
    }
    
    public void setResult(Result result) {
        this.result = result;
    }
}
