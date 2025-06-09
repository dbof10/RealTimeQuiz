# Quiz System Template

## Overview

This is a multi-module Gradle project for a real-time quiz system.

## Modules

- **core**: REST API and business logic (join, answer).
- **redis-integration**: Redis repositories and event publisher.
- **realtime-ws**: WebSocket service for real-time messaging.

## Setup

### Generate Gradle Wrapper

Run in the root directory:

```bash
gradle wrapper
```

### Build

```bash
./gradlew build
```

### Run Services

```bash
# Core API
java -jar core/build/libs/core-0.1.0.jar

# Real-time WebSocket
java -jar realtime-ws/build/libs/realtime-ws-0.1.0.jar
```

### Docker Compose

```bash
docker-compose up --build
```
