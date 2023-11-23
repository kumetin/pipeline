package org.kmt.aqua.entity.resource;

public interface Resource {
    String getType();
    String getId();
    String getName();
    String getSource();
    String getUrl();
    long getCreatedDateTimestamp();
}
