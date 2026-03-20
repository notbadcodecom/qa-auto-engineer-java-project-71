.DEFAULT_GOAL := build-run

setup:
	./gradlew wrapper --gradle-version 8.14.4

clean:
	./gradlew clean

build:
	./gradlew clean build

install:
	./gradlew clean install

run-dist:
	./build/install/app/bin/app $(ARGS)

run:
	./gradlew run

test:
	./gradlew test

report:
	./gradlew jacocoTestReport

lint:
	./gradlew spotlessApply

update-deps:
	./gradlew refreshVersions

build-run: build run

.SILENT: run-dist
.PHONY: build
