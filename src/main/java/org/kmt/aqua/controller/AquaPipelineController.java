package org.kmt.aqua.controller;

import org.kmt.aqua.AquaPipeline;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api")
public class AquaPipelineController {

    private final AquaPipeline appService;
    private final String output;

    public AquaPipelineController(AquaPipeline aquaPipeline,
                                  @Value("${aqua.pipeline.output}") String output) {
        this.appService = aquaPipeline;
        this.output = output;
    }

    @GetMapping(name = "/export")
    public ResponseEntity<Void> export() {
        try {
            appService.exportToFile(output);
            return ResponseEntity.ok().build();
        } catch (IOException e) {
            System.out.printf("Failed exporting reconciled pipeline data: %s%n", e);
            return ResponseEntity.internalServerError().build();
        }
    }

}
