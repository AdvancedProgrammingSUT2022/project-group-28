package models;

import models.civilization.Civilization;
import models.tiles.enums.ResourceTemplate;

public class Trade {
    private final Civilization customer;
    private final Civilization seller;
    private final ResourceTemplate resource;
    private final int count;
    private final int cost;
    public Trade(Civilization customer, Civilization seller, ResourceTemplate resource, int count, int cost) {
        this.customer = customer;
        this.seller = seller;
        this.resource = resource;
        this.count = count;
        this.cost = cost;
    }

    public Civilization getCustomer() { return customer; }

    public Civilization getSeller() { return seller; }

    public ResourceTemplate getResource() { return resource; }

    public int getCount() { return count; }

    public int getCost() { return cost; }
}
