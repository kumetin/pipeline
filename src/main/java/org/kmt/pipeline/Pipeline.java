package org.kmt.pipeline;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

@Component
public class Pipeline<T> {
    protected final T reconciler;
    private final List<DirectoryFileStreamListener<?,T>> directoriesListeners;
    private DirectoriesWatcher directoriesWatcher;

    public Pipeline(List<DirectoryFileStreamListener<?,T>> directoriesListeners, T reconciler) {
        this.reconciler = reconciler;
        this.directoriesListeners = directoriesListeners;
    }

    public void start() throws IOException {
        System.out.println("Starting pipeline");
        this.directoriesWatcher = new DirectoriesWatcher();
        startWatchers(directoriesListeners);
    }

    public void stop() {
        System.out.println("Stopping pipeline");
        stopWatchers();
    }

    private void startWatchers(List<DirectoryFileStreamListener<?,T>> directoriesListeners) {
        directoriesListeners.forEach(listener -> {
            try {
                JsonArrayFileProcessor jsonArrayProcessor = new JsonArrayFileProcessor() {
                    @Override
                    public void processJsonObject(JsonNode element, Path theFile) {
                        try {
                            listener.getJsonElementHandler().handle(element, reconciler);
                        } catch (JsonProcessingException e) {
                            System.out.printf("Failed processing json object in file %s: %s%n", theFile, e);
                        }
                    }
                };

                directoriesWatcher.registerHandler(listener.getSource(), file -> {
                    System.out.printf("Starting processing json array file %s%n", file);
                    try {
                        jsonArrayProcessor.processFile(file.toPath());
                    } catch (IOException e) {
                        System.out.printf("Failed processing json array file %s: %s%n", listener.getSource(), e);
                    }
                });
            } catch (IOException e) {
                throw new RuntimeException("Failed registering listener on directory %s".formatted(listener.getSource()), e);
            }
        });
    }

    private void stopWatchers() {
        directoriesWatcher.stopWatchers();
    }

}
