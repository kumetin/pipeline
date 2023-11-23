package org.kmt.aqua.elementhandler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import org.kmt.aqua.AquaReconciler;
import org.kmt.aqua.entity.Scan;
import org.kmt.pipeline.JsonElementHandler;

public class ScanElementHandler extends JsonElementHandler<Scan, AquaReconciler> {

    @Override
    protected Scan parse(JsonNode element) throws JsonProcessingException {
        return objectMapper.treeToValue(element, Scan.class);
    }

    @Override
    protected void handleEntity(Scan scan, AquaReconciler reconciler) {
        reconciler.processScan(scan);
    }
}
