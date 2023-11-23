package org.kmt.aqua;

import org.kmt.aqua.entity.Connection;
import org.kmt.aqua.entity.Scan;
import org.kmt.aqua.entity.resource.*;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

/**
 * All different entities of the aqua pipeline, that is the resources (repository/image), scans & connections are
 * processed by this class. reconciliation is done here into `repositoryMap` & `imageMap`
 */
public class AquaReconciler {

    ConcurrentHashMap<String, ReconciledRepository> repositoryMap = new ConcurrentHashMap<>();
    ConcurrentHashMap<String, ReconciledImage> imageMap = new ConcurrentHashMap<>();

    public void processScan(Scan scan) {
        System.out.printf("Processing scan id %s%n", scan.scanId());
        if ("repository".equals(scan.resourceType())) {
            ReconciledRepository reconciledRepository = repositoryMap.computeIfAbsent(
                scan.resourceId(),
                k -> ReconciledRepository.fromScan(scan)
            );
            reconciledRepository.updateScanData(scan);
        } else if ("image".equals(scan.resourceType())) {
            ReconciledImage reconciledImage = imageMap.computeIfAbsent(
                scan.resourceId(),
                k -> ReconciledImage.fromScan(scan)
            );
            reconciledImage.updateScanData(scan);
        } else {
            System.out.printf("Ignoring scan of resource of type '%s'%n", scan.resourceType());
        }
    }

    public void processConnection(Connection connection) {
        System.out.printf("Processing connection of repository id %s & image id %s%n", connection.repositoryId(), connection.imageId());
        ReconciledRepository repository = repositoryMap.computeIfAbsent(
            connection.repositoryId(),
            k -> new ReconciledRepository(connection.repositoryId())
        );
        repository.setUpdatedDateTimestamp(System.currentTimeMillis());
        repository.setConnectedImageId(connection.imageId());

        ReconciledImage image = imageMap.computeIfAbsent(
            connection.imageId(),
            k -> new ReconciledImage(connection.imageId())
        );
        image.setUpdatedDateTimestamp(System.currentTimeMillis());
        image.setConnectedRepositoryId(connection.repositoryId());
    }

    public void processResource(Resource resource) {
        System.out.printf("Processing resource id %s%n", resource.getId());
        if ("repository".equals(resource.getType())) {
            ReconciledRepository reconciledRepository = repositoryMap.computeIfAbsent(
                resource.getId(),
                k -> ReconciledRepository.fromResource(resource)
            );
            reconciledRepository.updateSpecificResourceData((Repository) resource);
        } else if ("image".equals(resource.getType())) {
            ReconciledImage reconciledImage = imageMap.computeIfAbsent(
                resource.getId(),
                k -> ReconciledImage.fromResource(resource)
            );
            reconciledImage.updateSpecificResourceData((Image) resource);
        } else {
            System.out.printf("Ignoring resource of type '%s'%n", resource.getType());
        }
    }

    /**
     * @return a weakly consistent view of the currently processed repositories, whether completely reconciled or not.
     *         The view is designed to tolerate concurrent modifications without throwing ConcurrentModificationException.
     */
    public Collection<ReconciledRepository> getRepositories() {
        return repositoryMap.values();
    }

    /**
     * @return a weakly consistent view of the currently processed images, whether completely reconciled or not.
     *         The view is designed to tolerate concurrent modifications without throwing ConcurrentModificationException.
     */
    public Collection<ReconciledImage> getImages() {
        return imageMap.values();
    }
}
