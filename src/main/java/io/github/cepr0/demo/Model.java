package io.github.cepr0.demo;

import lombok.Value;
import org.springframework.data.annotation.Id;

import java.io.Serializable;

@Value
public class Model implements Serializable {
    @Id int id;
    String name;
}
