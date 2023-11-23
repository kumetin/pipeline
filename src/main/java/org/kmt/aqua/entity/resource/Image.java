package org.kmt.aqua.entity.resource;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class Image extends ResourceImpl {
    protected int numberOfLayers;
    protected String architecture;

    public Image() {
    }

    public Image(String id, String type) {
        super(id, type);
    }

    public Image(String id, String type, int numberOfLayers, String architecture) {
        super(id, type);
        this.numberOfLayers = numberOfLayers;
        this.architecture = architecture;
    }

    public int getNumberOfLayers() {
        return numberOfLayers;
    }

    public String getArchitecture() {
        return architecture;
    }
}
