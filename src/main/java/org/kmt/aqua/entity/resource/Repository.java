package org.kmt.aqua.entity.resource;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.time.LocalDateTime;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class Repository extends ResourceImpl {
    protected LocalDateTime lastPush;
    protected long size;

    public Repository() {
        super();
    }

    public Repository(String id, String type) {
        super(id, type);
    }

    public Repository(String id, String type, LocalDateTime lastPush, long size) {
        super(id, type);
        this.lastPush = lastPush;
        this.size = size;
    }

    public LocalDateTime getLastPush() {
        return lastPush;
    }

    public long getSize() {
        return size;
    }
}
