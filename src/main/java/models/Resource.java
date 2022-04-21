package models;

import models.enums.ResourceTemplate;

public class Resource {
    private ResourceTemplate resourceTemplate;
    private int count;
    public Resource(ResourceTemplate resourceTemplate, int count) {
        this.resourceTemplate = resourceTemplate;
        this.count = count;
    }

}
