package org.kmt.aqua;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Profile("!test")
public class AquaPipelineStarter {

    @Autowired
    AquaPipeline aquaPipeline;

    @PostConstruct
    public void startPipeline() throws IOException {
        aquaPipeline.start();
    }

    @PreDestroy
    public void stopPipeline() {
        aquaPipeline.stop();
    }
}
