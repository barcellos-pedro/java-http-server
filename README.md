# Simple Java HTTP Server

This project implements a minimal, educational HTTP server in Java.  
The goal is to understand low-level request handling, routing, controllers, and response building without relying on large frameworks.

It includes:

- Socket-based HTTP server  
- Basic router and mapping system  
- Controllers implementing a `RequestHandler` interface  
- Request parsing (method, path, headers, body)  
- Response builder with support for multiple formats  
- A complete class diagram for architecture visibility

---

## 🚀 Overview

The server listens on a configurable port and processes each incoming socket by:

1. Parsing the HTTP request into a `Request` object  
2. Passing it to the `Router`  
3. Resolving the appropriate `RequestHandler` via `Routes`  
4. Building the HTTP response with `Response` helpers  
5. Sending the result back through the socket

The structure is intentionally simple so you can extend it — adding controllers, middleware, or even template rendering.

---

## 📦 Project Structure

```text
java-echo-server/
├── README.md
├── src
│   └── main
│       └── java
│           └── com
│               └── server
│                   ├── controller
│                   │   ├── EchoController.java
│                   │   ├── GreetingController.java
│                   │   ├── NotFoundController.java
│                   │   └── RequestHandler.java
│                   ├── http
│                   │   ├── HttpMethod.java
│                   │   ├── HttpServer.java
│                   │   ├── Request.java
│                   │   └── Response.java
│                   ├── Main.java
│                   ├── router
│                   │   ├── Router.java
│                   │   └── Routes.java
│                   └── utils
│                       └── ResponseTemplate.java
└── tests
    ├── load_get_greeting.sh
    ├── load_post_message.sh
    └── run_all_load_tests.sh
```

## 🧩 Class Diagram

Below is a full Mermaid UML diagram representing the architecture:

```mermaid
sequenceDiagram
    autonumber

    participant Client
    participant HttpServer
    participant Router
    participant Routes
    participant Controller
    participant Response

    Client ->> HttpServer: HTTP request
    HttpServer ->> Router: handle(socket)
    Router ->> Routes: getOrNotFound(request)
    Routes -->> Router: handler
    Router ->> Controller: handle(request)
    Controller -->> Router: body
    Router ->> Response: build response
    Response -->> Router: http string
    Router -->> Client: http response
```

## 🧱Architecture Overview

```mermaid
flowchart LR

    Client["Client(curl / browser)"]

    subgraph Server["Java Echo Server"]
        HttpServer["HttpServer(Socket listener)"]
        Router["Router(Request dispatcher)"]
        Routes["Routes(Path → Handler mapping)"]

        subgraph Controllers
            Greeting["GreetingController"]
            Echo["EchoController"]
            NotFound["NotFoundController"]
        end

        subgraph HTTP
            Request["Request(Parsed HTTP)"]
            Response["Response(HTTP builder)"]
            Templates["ResponseTemplate"]
        end
    end

    Client --> HttpServer
    HttpServer --> Router
    Router --> Request
    Router --> Routes
    Routes --> Greeting
    Routes --> Echo
    Routes --> NotFound
    Router --> Response
    Response --> Templates
    Response --> Client
```
