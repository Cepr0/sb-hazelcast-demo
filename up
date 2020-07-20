#!/bin/bash

if [ "$1" == "rebuild" ]; then
  mvn clean package -DskipTests && docker-compose up -d --scale demo-service=3 --build;
else
  docker-compose up -d --scale demo-service=3
fi