package org.kmt.aqua;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.util.DefaultIndenter;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

import static java.nio.file.StandardOpenOption.*;

public class JsonSerializer {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    static {
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.registerModule(new Jdk8Module());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    public static <T> void writeJsonString(Stream<T> entities, Writer writer) throws IOException {
        writeEntities(jsonGenerator(writer), entities);
    }

    public static <T> void writeJsonFile(Stream<T> entities, Path outputFile) throws IOException {
        writeEntities(jsonGenerator(Files.newBufferedWriter(outputFile, CREATE, WRITE, TRUNCATE_EXISTING)), entities);
    }

    private static JsonGenerator jsonGenerator(Writer writer) throws IOException {
        return objectMapper.writer()
            .with(SerializationFeature.INDENT_OUTPUT)
            .createGenerator(writer)
            .setPrettyPrinter(new DefaultPrettyPrinter().withArrayIndenter(
                new DefaultIndenter("  ", DefaultIndenter.SYS_LF)));
    }

    private static <T> void writeEntities(JsonGenerator generator, Stream<T> entities) {
        try (JsonGenerator ignored = generator) {
            generator.writeStartArray();

            entities.forEach(resource -> {
                try {
                    generator.writeObject(resource);
                } catch (IOException e) {
                    System.out.printf("Failed writing a %s entity: %s%n", resource.getClass().getSimpleName(), e);
                }
            });

            generator.writeEndArray();
        } catch (IOException e) {
            throw new RuntimeException("Failed writing entities: %s%n", e);
        }
    }

}
