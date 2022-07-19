package models.tiles;

import models.tiles.enums.ResourceTemplate;

public class Resource {
    private final ResourceTemplate resourceTemplate;
    private final int count;
    public Resource(ResourceTemplate resourceTemplate, int count) {
        this.resourceTemplate = resourceTemplate;
        this.count = count;
    }

    public ResourceTemplate getResourceTemplate() {
        return resourceTemplate;
    }

    public int getCount() {
        return count;
    }
}
