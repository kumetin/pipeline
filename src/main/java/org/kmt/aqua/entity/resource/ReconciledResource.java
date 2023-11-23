package org.kmt.aqua.entity.resource;

import org.kmt.aqua.entity.Scan;

public interface ReconciledResource<T extends ResourceImpl> {
    void updateSpecificResourceData(T resource);
    void updateScanData(Scan scan);
}