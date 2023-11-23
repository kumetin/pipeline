package org.kmt.pipeline;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.LinkedList;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Every call to registerHandler creates a background thread which constantly monitors the watched directory
 * and each time a created event is intercepted, all `FileHandler`s for that watched directory are executed with the new
 * file
 */
public class DirectoriesWatcher {

    @FunctionalInterface
    public interface FileHandler {
        void handle(File file);
    }

    private final WatchService watchService = FileSystems.getDefault().newWatchService();
    private final ConcurrentHashMap<Path, WatchThreadRunnable> watchedDirectories;

    public DirectoriesWatcher() throws IOException {
        this.watchedDirectories = new ConcurrentHashMap<>();
    }

    public void registerHandler(Path dir, FileHandler handler) throws IOException {

        assert dir.toFile().exists();
        assert dir.toFile().isDirectory();

        // handle the existing files
        handleCurrentFiles(Optional.ofNullable(dir.toFile().listFiles()).orElse(new File[0]), handler);

        // watch for future files
        watchedDirectories.computeIfAbsent(dir, k -> {
            try {
                dir.register(watchService, StandardWatchEventKinds.ENTRY_CREATE);
            } catch (IOException e) {
                throw new RuntimeException("Failed registering watcher on file %s".formatted(dir), e);
            }
            WatchThreadRunnable task = new WatchThreadRunnable(dir, watchService);
            Thread watchDaemonThread = new Thread(task);
            watchDaemonThread.start();
            return task;
        }).registerHandler(handler);
    }

    private void handleCurrentFiles(File[] files, FileHandler handler) {
        for (File file : files) {
            new Thread(() -> handler.handle(file)).start();
        }
    }


    public void stopWatchers() {
        watchedDirectories.values().forEach(WatchThreadRunnable::stop);
    }

    private static class WatchThreadRunnable implements Runnable {

        private final Path watchedDirectory;
        private final LinkedList<FileHandler> handlers = new LinkedList<>();
        private final WatchService watchService;
        private boolean stop = false;

        public WatchThreadRunnable(Path watchedDirectory,
                                   WatchService watchService) {
            this.watchedDirectory = watchedDirectory;
            this.watchService = watchService;
        }

        public void registerHandler(FileHandler handler) {
            handlers.add(handler);
        }

        @Override
        public void run() {
            System.out.printf("Watching directory %s%n", watchedDirectory);
            try {
                while (!stop) {
                    WatchKey key = watchService.take();

                    // Process the events
                    for (WatchEvent<?> event : key.pollEvents()) {
                        if (event.kind() == StandardWatchEventKinds.ENTRY_CREATE) {
                            // A new file was created
                            Path newFile = (Path) event.context();
                            System.out.printf("New file added in directory '%s': %s%n", watchedDirectory, newFile);
                            // Perform additional actions as needed
                            handlers.forEach(h -> h.handle(newFile.toFile()));
                        }
                    }

                    // Reset the key for the next iteration
                    boolean valid = key.reset();
                    if (!valid) {
                        // If the directory is no longer accessible, exit the loop
                        break;
                    }
                }
            } catch (InterruptedException e) {
                System.out.printf("Watch thread for directory %s was interrupted. Existing gracefully%n", watchedDirectory);
            }

        }

        public void stop() {
            this.stop = true;
            Thread.currentThread().interrupt();
        }
    }
}
