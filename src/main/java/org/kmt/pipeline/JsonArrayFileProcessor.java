package org.kmt.pipeline;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.file.Path;

public abstract class JsonArrayFileProcessor {

    public abstract void processJsonObject(JsonNode jsonNode, Path theFile) throws JsonProcessingException;

    public void processFile(Path jsonArrayFile) throws IOException {
        JsonFactory jsonFactory = new JsonFactory();
        ObjectMapper objectMapper = new ObjectMapper();

        try (JsonParser jsonParser = jsonFactory.createParser(jsonArrayFile.toFile())) {
            while (jsonParser.nextToken() != null) {
                if (jsonParser.currentToken() == JsonToken.START_OBJECT) {
                    JsonNode jsonNode = objectMapper.readTree(jsonParser);
                    processJsonObject(jsonNode, jsonArrayFile);
                }
            }
        }
    }

}
