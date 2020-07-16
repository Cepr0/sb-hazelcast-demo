package io.github.cepr0.demo;

import lombok.Value;
import lombok.With;
import lombok.experimental.Tolerate;
import org.springframework.data.annotation.Id;
import org.springframework.data.keyvalue.annotation.KeySpace;

import java.io.Serializable;
import java.time.Instant;

@KeySpace("models")
@Value
public class Model implements Serializable {
    @Id int id;
    @With String name;
    @With Instant updatedAt;

    @Tolerate
    public Model(int id) {
        this.id = id;
        this.name = "Model " + id;
        this.updatedAt = Instant.now();
    }
}
