package models.tiles;

import models.tiles.enums.ResourceTemplate;

public class Resource {
    private ResourceTemplate resourceTemplate;
    private int count;
    public Resource(ResourceTemplate resourceTemplate, int count) {
        this.resourceTemplate = resourceTemplate;
        this.count = count;
    }

}
