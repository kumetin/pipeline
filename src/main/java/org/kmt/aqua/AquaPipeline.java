package org.kmt.aqua;

import org.kmt.aqua.elementhandler.ConnectionElementHandler;
import org.kmt.aqua.elementhandler.ResourceElementHandler;
import org.kmt.aqua.elementhandler.ScanElementHandler;
import org.kmt.aqua.entity.resource.ReconciledImage;
import org.kmt.aqua.entity.resource.ReconciledRepository;
import org.kmt.pipeline.DirectoryFileStreamListener;
import org.kmt.pipeline.Pipeline;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.StringWriter;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@Component
public class AquaPipeline extends Pipeline<AquaReconciler> {

    private String input;

    public AquaPipeline(@Value("${aqua.pipeline.input}") String input) throws IOException {
        super(List.of(
            new DirectoryFileStreamListener<>(Path.of("%s/resources".formatted(input)), new ResourceElementHandler()),
            new DirectoryFileStreamListener<>(Path.of("%s/connections".formatted(input)), new ConnectionElementHandler()),
            new DirectoryFileStreamListener<>(Path.of("%s/scans".formatted(input)), new ScanElementHandler())
        ), new AquaReconciler());

        this.input = input;
    }

    public Map<String, String> exportToMap() throws IOException {
        Stream<ReconciledRepository> sortedRepositories =
            reconciler.getRepositories().stream().sorted(Comparator.comparing(ReconciledRepository::getId));

        System.out.printf("Starting export of %d processed repositories to in memory map%n", reconciler.getRepositories().size());
        StringWriter repositoriesWriter = new StringWriter();
        JsonSerializer.writeJsonString(sortedRepositories, repositoriesWriter);
        System.out.printf("Finished export of %d processed repositories to in memory map%n", reconciler.getRepositories().size());

        Stream<ReconciledRepository> sortedImages =
            reconciler.getRepositories().stream().sorted(Comparator.comparing(ReconciledRepository::getId));

        System.out.printf("Starting export of %d processed images to in memory map%n", reconciler.getImages().size());
        StringWriter imagesWriter = new StringWriter();
        JsonSerializer.writeJsonString(sortedImages, imagesWriter);
        System.out.printf("Finished export of %d processed repositories to in memory map%n", reconciler.getImages().size());

        return Map.of(
            "repositories", repositoriesWriter.toString(),
            "images", imagesWriter.toString()
        );
    }

    /**
     * saves the reconciled records to json files by resource type
     * ordered by the resource id (asc).
     * If target file already exists it will be overridden (the combination of open options CREATE, WRITE & TRUNCATE_EXISTING)
     */
    public void exportToFile(String output) throws IOException {

        Path exportedRepositoriesPath = Path.of("%s/repositories.json".formatted(output));
        Path exportedImagesPath = Path.of("%s/images.json".formatted(output));

        Stream<ReconciledRepository> sortedRepositories =
            reconciler.getRepositories().stream().sorted(Comparator.comparing(ReconciledRepository::getId));

        System.out.printf("Starting export of %d processed repositories to %s%n", reconciler.getRepositories().size(), exportedRepositoriesPath);
        JsonSerializer.writeJsonFile(sortedRepositories, exportedRepositoriesPath);
        System.out.printf("Finished export of %d processed repositories to %s%n", reconciler.getRepositories().size(), exportedRepositoriesPath);

        Stream<ReconciledImage> sortedImages = reconciler.getImages().stream().sorted(Comparator.comparing(ReconciledImage::getId));

        System.out.printf("Starting export of %d processed images to %s%n", reconciler.getImages().size(), exportedImagesPath);
        JsonSerializer.writeJsonFile(sortedImages, exportedImagesPath);
        System.out.printf("Finished export of %d processed images to %s%n", reconciler.getImages().size(), exportedImagesPath);

    }

}
