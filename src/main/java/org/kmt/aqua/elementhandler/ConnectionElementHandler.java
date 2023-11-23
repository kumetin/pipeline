package org.kmt.aqua.elementhandler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import org.kmt.aqua.AquaReconciler;
import org.kmt.aqua.entity.Connection;
import org.kmt.pipeline.JsonElementHandler;

public class ConnectionElementHandler extends JsonElementHandler<Connection, AquaReconciler> {
    @Override
    protected Connection parse(JsonNode element) throws JsonProcessingException {
        return objectMapper.treeToValue(element, Connection.class);
    }

    @Override
    protected void handleEntity(Connection connection, AquaReconciler reconciler) {
        reconciler.processConnection(connection);
    }
}
