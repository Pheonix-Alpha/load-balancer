# 🚦 OrderFlow — Custom Java HTTP Load Balancer

A custom HTTP load balancer built with **Java 25 and Spring Boot 4** that demonstrates how incoming HTTP requests can be distributed across multiple backend service instances.

The project was created to understand a fundamental concept used in distributed backend systems: **placing a routing layer between clients and multiple application servers to distribute traffic.**

---

## 🏗️ Architecture

```text
                    ┌─────────────────┐
                    │     Client      │
                    └────────┬────────┘
                             │
                             │ HTTP Request
                             ▼
                 ┌─────────────────────────┐
                 │   OrderFlow Load        │
                 │       Balancer          │
                 │                         │
                 │  Request Routing Layer  │
                 └────────────┬────────────┘
                              │
                 ┌────────────┼────────────┐
                 │            │            │
                 ▼            ▼            ▼
          ┌────────────┐ ┌────────────┐ ┌────────────┐
          │ Backend 1  │ │ Backend 2  │ │ Backend 3  │
          │   :8081    │ │   :8082    │ │   :8083    │
          └────────────┘ └────────────┘ └────────────┘
```

Instead of allowing clients to communicate directly with a single backend instance, requests pass through the load-balancing layer.

The load balancer determines which backend instance should receive each request.

---

## 🎯 Why This Project?

Modern backend applications often run multiple instances of the same service.

For example:

```text
                    Traffic
                       │
                       ▼
                Load Balancer
                 /     |     \
                /      |      \
               ▼       ▼       ▼
           Server 1 Server 2 Server 3
```

Running multiple instances provides the foundation for:

* Horizontal scaling
* Traffic distribution
* Better resource utilization
* Improved availability
* Handling increased request volume
* Independent scaling of application instances

This project focuses on understanding the **request-routing layer** behind that architecture.

---

## ✨ Features

* Custom HTTP request routing
* Multiple backend instance support
* Spring Boot based backend
* Java 25 implementation
* Maven project structure
* HTTP-based communication
* Separation between incoming traffic and backend services
* Designed as a learning project for distributed systems and backend scalability

> Additional routing strategies and resilience features can be added as the project evolves.

---

## 🛠️ Tech Stack

| Technology    | Purpose                         |
| ------------- | ------------------------------- |
| Java 25       | Core programming language       |
| Spring Boot 4 | Application framework           |
| Spring MVC    | HTTP request handling           |
| Maven         | Build and dependency management |
| HTTP          | Client/backend communication    |

---

## 📂 Project Structure

```text
load-balancer/
│
├── src/
│   └── main/
│       ├── java/
│       │   └── ...
│       │
│       └── resources/
│           └── ...
│
├── .gitignore
├── pom.xml
└── README.md
```

---

# 🔄 Request Flow

A typical request follows this architecture:

```text
Client
  │
  │ HTTP Request
  ▼
Load Balancer
  │
  │ Select backend instance
  ▼
Backend Service
  │
  │ Process request
  ▼
Response
  │
  ▼
Load Balancer
  │
  ▼
Client
```

The load balancer acts as an intermediary between the client and backend services.

---

# 🚀 Getting Started

## Prerequisites

Make sure you have:

* Java 25
* Maven
* Git

Verify Java:

```bash
java -version
```

Verify Maven:

```bash
mvn -version
```

---

## Clone the Repository

```bash
git clone https://github.com/Pheonix-Alpha/load-balancer.git
```

Navigate into the project:

```bash
cd load-balancer
```

---

## Build the Project

```bash
mvn clean package
```

---

## Run the Application

```bash
mvn spring-boot:run
```

The application will start on the configured Spring Boot port.

---

# 🧪 Testing

The load balancer can be tested by sending HTTP requests to its exposed endpoint.

Example:

```bash
curl http://localhost:<PORT>/<ENDPOINT>
```

You can then observe which backend instance processes the request.

---

# 📈 Scaling Concept

The architecture is designed around **horizontal scaling**.

Instead of making one server increasingly powerful:

```text
        Large Server
            │
         Traffic
```

multiple application instances can be used:

```text
             Load Balancer
             /     |     \
            /      |      \
           ▼       ▼       ▼
       Server 1 Server 2 Server 3
```

This allows additional backend instances to be introduced as traffic requirements increase.

---

# 🧠 System Design Concepts Demonstrated

This project provides hands-on exposure to:

* HTTP request routing
* Reverse-proxy concepts
* Horizontal scaling
* Multiple backend instances
* Distributed backend architecture
* Traffic distribution
* Service-to-service communication
* Scalability fundamentals

---

# 🔮 Future Improvements

The project can be extended with production-oriented capabilities such as:

* [ ] Round-robin routing
* [ ] Weighted routing
* [ ] Least-connections routing
* [ ] Backend health checks
* [ ] Automatic unhealthy-instance removal
* [ ] Connection/request metrics
* [ ] Request latency tracking
* [ ] Retry handling
* [ ] Circuit breaker
* [ ] Docker-based deployment
* [ ] Docker Compose with multiple backend instances
* [ ] Redis-based shared state
* [ ] Prometheus metrics
* [ ] Grafana dashboard
* [ ] Load testing with Apache JMeter or k6
* [ ] Kubernetes deployment

---

# 🎓 What I Learned

Building this project provided practical experience with the architecture behind horizontally scaled backend systems.

Key concepts explored:

1. How clients communicate with a routing layer.
2. How traffic can be distributed across multiple application instances.
3. Why multiple backend instances are useful.
4. How horizontal scaling differs from simply increasing server resources.
5. How a load-balancing layer can become a critical component of a distributed system.

---

# 📌 Project Status

**Status:** Functional learning project

The current implementation focuses on understanding the core mechanics of HTTP request routing and load balancing.

The architecture can be progressively extended toward a more production-oriented distributed load-balancing system.

---

## 👨‍💻 Author

**Pheonix-Alpha**

GitHub:
https://github.com/Pheonix-Alpha
