package io.github.cepr0.demo.model;

import lombok.Value;
import lombok.With;
import lombok.experimental.Tolerate;

import java.io.Serializable;
import java.time.Instant;

@Value
public class Model implements Serializable {
    long id;
    @With String name;
    @With Instant updatedAt;

    @Tolerate
    public Model(long id) {
        this.id = id;
        this.name = "Model " + id;
        this.updatedAt = Instant.now();
    }
}
