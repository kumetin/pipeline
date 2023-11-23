package org.kmt.aqua.entity.resource;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import org.kmt.aqua.entity.Scan;

import java.util.Optional;
import java.util.OptionalInt;
import java.util.OptionalLong;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReconciledImage extends ReconciledResourceImpl implements ReconciledResource<Image> {

    private Optional<Integer> numberOfLayers;
    private Optional<String> architecture;
    private OptionalInt scanId;
    private Optional<String> highestSeverity;
    private OptionalInt totalFindings;
    private OptionalLong scanDateTimestamp;
    private Optional<String> connectedRepositoryId;

    public ReconciledImage(String id) {
        this.id = id;
    }

    public ReconciledImage(String id, int numberOfLayers, String architecture) {
        this(id);
        this.numberOfLayers = Optional.of(numberOfLayers);
        this.architecture = Optional.of(architecture);
    }

    public ReconciledImage(String id, int scanId, String highestSeverity, int totalFindings, long scanDateTimestamp) {
        this(id);
        this.scanId = OptionalInt.of(scanId);
        this.highestSeverity = Optional.ofNullable(highestSeverity);
        this.totalFindings = OptionalInt.of(totalFindings);
        this.scanDateTimestamp = OptionalLong.of(scanDateTimestamp);
    }

    public static ReconciledImage fromResource(Resource resource) {
        if (!"image".equals(resource.getType())) {
            throw new IllegalArgumentException("Cannot create a ReconciledImage from a resource of type " + resource.getType());
        }
        Image image = (Image) resource;
        return new ReconciledImage(image.getId(), image.getNumberOfLayers(), image.getArchitecture());
    }

    public static ReconciledImage fromScan(Scan scan) {
        if (!"image".equals(scan.resourceType())) {
            throw new IllegalArgumentException("Cannot create a ReconciledImage from a scan of type " + scan.resourceType());
        }
        return new ReconciledImage(scan.resourceId(), scan.scanId(), scan.highestSeverity(), scan.totalFindings(), scan.scanDateTimestamp());
    }

    public Optional<Integer> getNumberOfLayers() {
        return numberOfLayers;
    }

    public Optional<String> getArchitecture() {
        return architecture;
    }

    public OptionalInt getScanId() {
        return scanId;
    }

    public Optional<String> getHighestSeverity() {
        return highestSeverity;
    }

    public OptionalInt getTotalFindings() {
        return totalFindings;
    }

    public OptionalLong getScanDateTimestamp() {
        return scanDateTimestamp;
    }

    public Optional<String> getConnectedRepositoryId() {
        return connectedRepositoryId;
    }

    public void setScanId(int scanId) {
        this.scanId = OptionalInt.of(scanId);
    }

    public void setHighestSeverity(String highestSeverity) {
        this.highestSeverity = Optional.ofNullable(highestSeverity);
    }

    public void setTotalFindings(int totalFindings) {
        this.totalFindings = OptionalInt.of(totalFindings);
    }

    public void setScanDateTimestamp(long scanDateTimestamp) {
        this.scanDateTimestamp = OptionalLong.of(scanDateTimestamp);
    }

    public void setConnectedRepositoryId(String connectedRepositoryId) {
        this.connectedRepositoryId = Optional.ofNullable(connectedRepositoryId);
    }

    @Override
    public void updateSpecificResourceData(Image image) {
        this.numberOfLayers = Optional.of(image.getNumberOfLayers());
        this.architecture = Optional.ofNullable(image.getArchitecture());
        super.updateCommonResourceData((Resource) image);
        this.updatedDateTimestamp = System.currentTimeMillis();
    }

    @Override
    public void updateScanData(Scan scan) {
        this.scanId = OptionalInt.of(scan.scanId());
        this.scanDateTimestamp = OptionalLong.of(scan.scanDateTimestamp());
        this.totalFindings = OptionalInt.of(scan.totalFindings());
        this.highestSeverity = Optional.ofNullable(scan.highestSeverity());
        this.updatedDateTimestamp = System.currentTimeMillis();
    }
}
