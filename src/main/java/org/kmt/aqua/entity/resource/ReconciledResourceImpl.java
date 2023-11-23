package org.kmt.aqua.entity.resource;

import java.util.Optional;

public class ReconciledResourceImpl {

    protected String id;
    protected Optional<String> name;
    protected Optional<String> source;
    protected Optional<String> url;
    protected Optional<Long> createdDateTimestamp;
    protected Long updatedDateTimestamp;

    public final String getId() {
        return id;
    }

    public final Optional<String> getName() {
        return name;
    }

    public final Optional<String> getSource() {
        return source;
    }

    public final Optional<String> getUrl() {
        return url;
    }

    public final Optional<Long> getCreatedDateTimestamp() {
        return createdDateTimestamp;
    }

    public Long getUpdatedDateTimestamp() {
        return updatedDateTimestamp;
    }

    public void setUpdatedDateTimestamp(long updatedDateTimestamp) {
        this.updatedDateTimestamp = updatedDateTimestamp;
    }

    protected final void updateCommonResourceData(Resource resource) {
        this.id = resource.getId();
        this.name = Optional.ofNullable(resource.getName());
        this.source = Optional.ofNullable(resource.getSource());
        this.url = Optional.ofNullable(resource.getUrl());
        this.createdDateTimestamp = Optional.of(resource.getCreatedDateTimestamp());
    }
}
