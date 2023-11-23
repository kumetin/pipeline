package org.kmt.aqua.elementhandler;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import org.kmt.aqua.AquaReconciler;
import org.kmt.aqua.entity.resource.Image;
import org.kmt.aqua.entity.resource.Repository;
import org.kmt.aqua.entity.resource.Resource;
import org.kmt.pipeline.JsonElementHandler;

public class ResourceElementHandler extends JsonElementHandler<Resource, AquaReconciler> {
    @Override
    protected Resource parse(JsonNode element) throws JsonProcessingException {
        String type = element.get("type").asText();
        if ("repository".equals(type)) {
            return objectMapper.treeToValue(element, Repository.class);
        } else if ("image".equals(type)) {
            return objectMapper.treeToValue(element, Image.class);
        } else if (type != null && !type.isEmpty()) {
            throw new JsonParseException("Unsupported resource type '%s'".formatted(type));
        } else
            throw new JsonParseException("Attribute 'type' not provided in resource");
    }

    @Override
    protected void handleEntity(Resource resource, AquaReconciler reconciler) {
        reconciler.processResource(resource);
    }
}
