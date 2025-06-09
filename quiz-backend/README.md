# 🧠 Kahoot Real-Time Quiz Backend

This project powers a real-time vocabulary quiz system designed for live multi-user participation, scoring, and leaderboard updates. The backend is structured into modular services for scalability and clarity.

---

## 🗂️ Project Modules

| Module             | Description                                           |
|--------------------|-------------------------------------------------------|
| `core`             | Quiz domain logic, scoring, persistence,              |
| `realtime-ws`      | WebSocket service that receives/sends real-time messages |

---

## 🚀 Running the Backend Locally

### ✅ Prerequisites

- [Java 21+](https://adoptium.net/)
- [Gradle 8+](https://gradle.org/install/)
- [Redis](https://redis.io/docs/getting-started/installation/) (must be installed natively)

---

### 📦 Install Redis (macOS with Homebrew)

```bash
brew install redis
brew services start redis
```

▶️ Start Services
1. Start the Quiz Service (core)

```bash
./gradlew :core:bootRun
```
Runs the backend logic that handles scoring, ranking, and leaderboard updates. Listens to Kafka events like quiz.answer_submitted and quiz.participant_disconnected.

2. Start the Real-Time WebSocket Server (realtime-ws)
```bash
./gradlew :realtime-ws:bootRun
```
Hosts a WebSocket server. Clients can connect using:

```bash
ws://localhost:8081/ws?quizId=...&participantId=...
```
Emits Kafka events for quiz participation and answers.
