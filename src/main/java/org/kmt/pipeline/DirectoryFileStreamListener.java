package org.kmt.pipeline;

import java.nio.file.Path;

public class DirectoryFileStreamListener<ENTITY, RECONCILER> {
    private final JsonElementHandler<ENTITY, RECONCILER> jsonElementHandler;
    private final Path source;
    public DirectoryFileStreamListener(Path srcDir, JsonElementHandler<ENTITY, RECONCILER> jsonElementHandler) {
        this.source = srcDir;
        this.jsonElementHandler = jsonElementHandler;
    }

    public JsonElementHandler<ENTITY, RECONCILER> getJsonElementHandler() {
        return jsonElementHandler;
    }

    public Path getSource() {
        return source;
    }
}