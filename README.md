# JWT: microservice authentication and secure-transfer playground

A Java 21 / Dropwizard 5 sandbox that shows several security and data-transfer
patterns working between small HTTPS microservices:

1. **Session → JWT → resource access.** Classic token-based auth split across three services.
2. **ECDHE message encryption.** An ephemeral Elliptic-Curve Diffie-Hellman key exchange,
   followed by an AES-encrypted request/response, on top of TLS.
3. **Chunked file download.** Sequential and parallel range downloads, checked by MD5,
   plus a sliding-window prototype for ordered, flow-controlled fetching.
4. **Metrics collection (work in progress).** A Jackson model of Dropwizard's `/metrics` JSON
   and config classes for a collector that polls each service.

## Architecture

```
                        (1) POST /login {userid,password}
   ┌────────┐ ─────────────────────────────────────────────▶ ┌──────────────────────┐
   │        │ ◀───────────────────────────── sessionId ───── │ Authenticator  :8480 │──▶ DynamoDB (local :8000)
   │        │                                                │ LoginApplication     │    table JWTUser
   │        │      (2) POST /token {sessionId}               └──────────▲───────────┘
   │ client │ ─────────────────────────────────────────────▶ ┌──────────┴───────────┐
   │        │ ◀──────────────────────── ES256-signed JWT ─── │ Authorizer     :8280 │  POST /verify (session live?)
   │        │                                                │ AuthApplication      │  GET  /certificate (public key)
   │        │      (3) POST /service {jwt, payload}          └──────────▲───────────┘
   │        │ ─────────────────────────────────────────────▶ ┌──────────┴───────────┐
   │        │ ◀──────────────── VALID + PAYLOAD (uppercased) │ App            :8380 │  fetches /certificate at startup
   └────────┘                                                │ AppApplication       │  to check JWT signatures
        │  /keyexchange + /eservice  (ECDHE + AES)           │                      │
        └──────────────────────────────────────────────────▶ │  /meta + /chunk      │  file download
                                                             └──────────────────────┘
```

Each service runs on HTTPS. Its admin port (metrics and health checks) is the app port + 1.

## The flows

### 1. Login → token → service (`client.JWTTester`)
1. **Authenticator** `/login` checks the credentials against DynamoDB (`dynamodb.UserCreator`)
   and creates a session in `UserSessionCache`. Sessions expire 5 seconds after login,
   and logging in again replaces the previous session.
2. **Authorizer** `/token` asks the authenticator's `/verify` whether the session is live.
   If it is, it returns a JWS signed with ES256 (`util.JWSSignUtil`).
3. **App** `/service` checks the JWS signature (`util.JWSVerifyUtil`) with the public key
   it downloaded from the authorizer's `/certificate`, and then handles the request.

### 2. ECDHE encrypted messaging (`client.ECDHETester`)
1. The client makes a P-256 key pair and POSTs its public key to `/keyexchange`.
2. The server (`util.KeyExchangeManager`) makes its own ephemeral key pair, derives the shared secret,
   hashes it with SHA-256 into an AES-256 key (`util.DHUtil`), stores the key under a new session id
   (`util.SessionKeyCache`, 5-minute TTL), and returns its public key and the session id.
3. The client derives the same key, encrypts its message with AES/CBC and a random IV, and POSTs it to `/eservice`.
4. The server decrypts the message, upper-cases it, encrypts the reply, and deletes the session key (one use only).

### 3. File download (`client.FileDownloadTester`, `client.ParallelFileDownloadTester`)
* `/meta` returns the file size and MD5. The MD5 is cached until the file's size or mtime changes.
* `/chunk` returns bytes `[start, end)`, read at an offset and clipped to the end of the file.
* The clients fetch the ranges one after another, or on 4 threads at once, rebuild the file
  in `/tmp`, and compare MD5s.
* `util.SlidingWindow` is a prototype of the next step: workers fetch in parallel, at most N chunks
  are outstanding, and the consumer receives the chunks strictly in order.

## Package map

| Package | Contents |
|---|---|
| `services.authenticator` | Login service: `LoginResource` (REST), `LoginManager` (logic), `UserManager` → DynamoDB, `UserSessionCache` |
| `services.authorizer` | JWT-issuing service: `AuthResource` (`/token`, `/certificate`) |
| `services.app` | Resource service: `AppResource` (JWT-protected call, ECDHE, file download), `FileCreator` (makes test files) |
| `services.metrics` | WIP metrics service. `CircularQueue` keeps recent samples, and `config/*` describes what to poll |
| `services` | `ServiceConfiguration`, the shared Dropwizard YAML config base |
| `data` | JSON request/response DTOs for every endpoint |
| `util` | `Connection` (shared HTTPS client), `CryptUtil`, `DHUtil`, `KeyExchangeManager`, `SessionKeyCache`, `JWSSignUtil`/`JWSVerifyUtil`, `JSONUtil`, `CheckSum`, `SlidingWindow` |
| `dynamodb` | `UserRecord` entity and `UserCreator` (table setup, seeding, credential checks) |
| `metrics.data` | **Generated** (jsonschema2pojo) model of the Dropwizard `/metrics` and `/healthcheck` JSON |
| `client` | Manual end-to-end test drivers, each with a `main` |

## Running locally

Prerequisites, which are not part of this repo:
* A JKS keystore for TLS. The path is set in `src/main/resources/*.yml` (`keyStorePath`).
* An EC P-256 certificate and PKCS#8 DER private key for JWT signing. They are set with
  `-Djws.certFile=… -Djws.keyFile=…`, and otherwise come from the defaults in `JWSSignUtil`.
* DynamoDB Local on `localhost:8000`. Create and seed it with
  `java dynamodb.UserCreator create`, then `… UserCreator seed`.
* Clients must trust the self-signed certificate:
  `-Djavax.net.ssl.trustStore=<keystore> -Djavax.net.ssl.trustStorePassword=…`

```bash
mvn package
java -cp target/JWT-1.0-SNAPSHOT.jar services.authenticator.LoginApplication server src/main/resources/Authenticator.yml
java -cp target/JWT-1.0-SNAPSHOT.jar services.authorizer.AuthApplication    server src/main/resources/Auth.yml
java -cp target/JWT-1.0-SNAPSHOT.jar services.app.FileCreator               # one time: makes File1..File3
java -cp target/JWT-1.0-SNAPSHOT.jar services.app.AppApplication            server src/main/resources/App.yml
java -cp target/JWT-1.0-SNAPSHOT.jar client.JWTTester
```

You can override any YAML value with `-Ddw.<path>=…`, for example
`-Ddw.filesDirectory=/data/files/`.

## Known limitations (demo-grade)
* Passwords are stored and compared in plaintext. Use bcrypt, scrypt, or PBKDF2 for anything real.
* The JWT payload is a fixed `"Dummy Token"`. It has no claims (`sub`, `exp`, `aud`), so it never expires.
* `/meta` and `/chunk` do not check the JWT yet (marked `TODO`).
* Hostname verification is turned off in `util.Connection` to allow self-signed localhost certificates.
* The ECDHE messages use AES-CBC with no MAC. AES-GCM would add integrity protection.
* Service URLs and several file paths are hard-coded for a local developer machine.
