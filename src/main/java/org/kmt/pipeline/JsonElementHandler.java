package org.kmt.pipeline;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

abstract public class JsonElementHandler<ENTITY, RECONCILER> {

    protected ObjectMapper objectMapper = new ObjectMapper();

    {
        objectMapper.registerModule(new JavaTimeModule());
    }

    final public void handle(JsonNode element, RECONCILER reconciler) throws JsonProcessingException {
        handleEntity(parse(element), reconciler);
    }

    abstract protected ENTITY parse(JsonNode element) throws JsonProcessingException;

    abstract protected void handleEntity(ENTITY entity, RECONCILER reconciler);
}
