package org.kmt.aqua.entity;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

/**
 *  {
 *     "scan_id": 123,
 *     "resource_id": "550e8400-e29b-41d4-a716-446655440000",
 *     "resource_type": "repository",
 *     "highest_severity": "high",
 *     "total_findings": 3,
 *     "scan_date_timestamp": 1699352316
 *   }
 */
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record Scan(int scanId,
                   String resourceId,
                   String resourceType,
                   String highestSeverity,
                   int totalFindings,
                   long scanDateTimestamp) {
}
