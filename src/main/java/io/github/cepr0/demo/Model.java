package io.github.cepr0.demo;

import lombok.Value;

import java.io.Serializable;

@Value
public class Model implements Serializable {
    int id;
    String name;
}
