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
classDiagram

    %% Interfaces
    class RequestHandler {
        <<interface>>
        +handle(request) String
    }

    %% Enums
    class HttpMethod {
        <<enumeration>>
        GET
        POST
        PUT
        PATCH
        DELETE
    }

    class Response_Type {
        <<enumeration>>
        TEXT
        JSON
    }

    %% Core classes
    class Main {
        +main()
    }

    class HttpServer {
        -port : int
        +HttpServer(int)
        +create(int) HttpServer
        +listen(Consumer<Socket>) void
    }

    class Router {
        +handle(Socket) void
        +route(Request) String
    }

    class Routes {
        +mapping : Map<String, RequestHandler>
        +getOrNotFound(Request) String
    }

    class Request {
        -method : HttpMethod
        -path : String
        -protocol : String
        -headers : Map<String, String>
        -reader : BufferedReader

        +Request()
        +of(Socket) Request
        +getReader(Socket) BufferedReader
        +setRequestLineFields() void
        +parseHeaders() void
        +parseBody() String
        +hasBody() boolean
        +getMethod() HttpMethod
        +setMethod(HttpMethod) void
        +getPath() String
        +setPath(String) void
        +getProtocol() String
        +setProtocol(String) void
        +getHeaders() Map<String,String>
        +setHeaders(Map<String,String>) void
        +setReader(BufferedReader) void
        +toString() String
    }

    class Response {
        +of(String) String
        +of(String, Type) String
        +echo(Request, Type) String
        +bindValues(Type, String) String
    }

    class ResponseTemplate {
        +TEXT : String
        +JSON : String
    }

    class GreetingController {
        +handle(Request) String
    }

    class EchoController {
        +handle(Request) String
        -valid(Request) boolean
    }

    class NotFoundController {
        +handle(Request) String
    }

    class IO {
        +println(Object) void
    }

    %% Relationships
    RequestHandler <|.. GreetingController
    RequestHandler <|.. EchoController
    RequestHandler <|.. NotFoundController

    Routes "1" *-- "0..*" RequestHandler : mapping
    Router --> Request : uses
    Router --> Routes : route / getOrNotFound
    HttpServer --> Router : passes handler
    Main --> HttpServer : creates / listens
    Response ..> Response_Type : uses
    Response --> ResponseTemplate : uses
    Response --> Request : echo()
    Request --> HttpMethod : uses
    Request "1" *-- "1" BufferedReader : reader
    IO <.. Router
    IO <.. Request
    IO <.. HttpServer
    IO <.. Response

    %% Notes
    Note right of Request : Request.of(socket)\nbuilds a Request\n- getReader(socket)\n- setRequestLineFields()\n- parseHeaders()
    Note left of Routes : mapping = Map.of("/greeting" -> GreetingController,\n"/message" -> EchoController")
