package org.kmt.aqua;

import org.json.JSONException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;
import java.time.Duration;
import java.util.Map;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = SpringBootRestApplication.class)
class AquaPipelineTest {

    @Autowired
    AquaPipeline aquaPipeline;

    @Value("${aqua.pipeline.output}")
    String output;

    @Test
    void sanityPipelineTest() throws IOException, InterruptedException, JSONException {
        aquaPipeline.start();
        Thread.sleep(Duration.ofSeconds(1));
        Map<String, String> resultMap = aquaPipeline.exportToMap();
        JSONAssert.assertEquals(expectedRepositoriesJson, resultMap.get("repositories"), true);
        JSONAssert.assertEquals(expectedImagesJson, resultMap.get("images"), true);
        aquaPipeline.stop();
    }

    private String expectedRepositoriesJson = """
        [
          {
            "id": "550e8400-e29b-41d4-a716-446655440000",
            "name": "my-awesome-repo",
            "url": "https://github.com/my-org/my-awesome-repo",
            "created_date_timestamp": 1699358834,
            "source": "github",
            "last_push": "2023-01-01T12:00:00Z",
            "updated_date_timestamp": 1701950834,
            "scan_id": 123,
            "highest_severity": "high",
            "total_findings": 3,
            "scan_date_timestamp": 1699352316,
            "size": 1234567890,
            "connected_image_id": "9b3dee35-aa47-40eb-8d8b-2e23bf7cbbcf"
          },
          {
            "id": "7bb72d11-3adb-47fd-a90c-9896a8556183",
            "updated_date_timestamp": 1701950834
          },
          {
            "id": "95076fb7-26a1-482a-9acc-e64fa62ec4a9",
            "name": "my-second-repo",
            "url": "https://bitbucket.com/my-org/my-second-repo",
            "created_date_timestamp": 1699366034,
            "source": "bitbucket",
            "last_push": "2023-02-02T14:00:00Z",
            "updated_date_timestamp": 1701950834,
            "size": 321321321
          }
        ]
        """;

    private String expectedImagesJson = """
        [
          {
            "id": "1802c7b7-572c-4fab-b602-3cad7e672dbd",
            "scan_id": 456,
            "highest_severity": "medium",
            "total_findings": 2,
            "scan_date_timestamp": 1699352525,
            "updated_date_timestamp": 1701950834
          },
          {
            "id": "4af8679b-ec97-4067-b3ae-a81577eb585e",
            "updated_date_timestamp": 1701950834
          },
          {
            "id": "9b3dee35-aa47-40eb-8d8b-2e23bf7cbbcf",
            "name": "my-first-image",
            "url": "https://dockerhub.com/my-org/my-first-image",
            "created_date_timestamp": 1699352316,
            "number_of_layers": 3,
            "architecture": "arm",
            "updated_date_timestamp": 1701950834,
            "scan_id": 321,
            "highest_severity": "low",
            "total_findings": 1,
            "scan_date_timestamp": 1699352325,
            "source": "dockerhub",
            "connected_repository_id": "550e8400-e29b-41d4-a716-446655440000"
          }
        ]
        """;
}