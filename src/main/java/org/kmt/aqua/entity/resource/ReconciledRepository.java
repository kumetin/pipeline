package org.kmt.aqua.entity.resource;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.kmt.aqua.entity.Scan;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.OptionalLong;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReconciledRepository extends ReconciledResourceImpl implements ReconciledResource<Repository> {

    private Optional<LocalDateTime> lastPush;
    private Optional<Long> size;
    private OptionalInt scanId;
    private Optional<String> highestSeverity;
    private OptionalInt totalFindings;
    private OptionalLong scanDateTimestamp;
    private Optional<String> connectedImageId;

    public ReconciledRepository(String id) {
        this.id = id;
    }

    public ReconciledRepository(String id, LocalDateTime lastPush, long size) {
        this.id = id;
        this.lastPush = Optional.ofNullable(lastPush);
        this.size = Optional.of(size);
    }

    public ReconciledRepository(String resourceId, int scanId, String highestSeverity, int totalFindings, long scanDateTimestamp) {
        this.id = resourceId;
        this.scanId = OptionalInt.of(scanId);
        this.highestSeverity = Optional.ofNullable(highestSeverity);
        this.totalFindings = OptionalInt.of(totalFindings);
        this.scanDateTimestamp = OptionalLong.of(scanDateTimestamp);
    }

    public static ReconciledRepository fromResource(Resource resource) {
        if (!"repository".equals(resource.getType())) {
            throw new IllegalArgumentException("Cannot create a ReconciledRepository from a resource of type " + resource.getType());
        }
        Repository repository = (Repository) resource;
        ReconciledRepository result =
            new ReconciledRepository(repository.getId(), repository.getLastPush(), repository.getSize());
        return result;
    }

    public static ReconciledRepository fromScan(Scan scan) {
        if (!"repository".equals(scan.resourceType())) {
            throw new IllegalArgumentException("Cannot create a ReconciledRepository from a resource of type " + scan.resourceType());
        }
        ReconciledRepository result =
            new ReconciledRepository(scan.resourceId(), scan.scanId(), scan.highestSeverity(),
                scan.totalFindings(), scan.scanDateTimestamp());
        return result;
    }

    public Optional<LocalDateTime> getLastPush() {
        return lastPush;
    }

    public Optional<Long> getSize() {
        return size;
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

    public Optional<String> getConnectedImageId() {
        return connectedImageId;
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

    public void setConnectedImageId(String connectedImageId) {
        this.connectedImageId = Optional.ofNullable(connectedImageId);
    }

    public void updateSpecificResourceData(Repository repository) {
        this.lastPush = Optional.ofNullable(repository.getLastPush());
        this.size = Optional.of(repository.getSize());
        super.updateCommonResourceData((Resource) repository);
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
