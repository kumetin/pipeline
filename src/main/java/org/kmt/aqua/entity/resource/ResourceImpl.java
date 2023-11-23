package org.kmt.aqua.entity.resource;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public abstract class ResourceImpl implements Resource {
    private String id;
    private String type;
    private String name;
    private String url;
    private long createdDateTimestamp;
    private String source;

    public ResourceImpl() {
    }

    public ResourceImpl(String id, String type) {
        this.id = id;
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public long getCreatedDateTimestamp() {
        return createdDateTimestamp;
    }

    public String getType() {
        return type;
    }

    public String getSource() {
        return source;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setCreatedDateTimestamp(long createdDateTimestamp) {
        this.createdDateTimestamp = createdDateTimestamp;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public void updateResourceData(ResourceImpl resource) {
        this.id = resource.id;
        this.type = resource.type;
        this.name = resource.name;
        this.url = resource.url;
        this.source = resource.source;
        this.createdDateTimestamp = resource.createdDateTimestamp;
    }
}



