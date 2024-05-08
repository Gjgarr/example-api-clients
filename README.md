# example-api-clients

Provides a quick reference on making Java API clients using the following:
* `RestTemplate`
* `WebClient`
* `RestClient`
* Http Interfaces (`HttpExchange`)


## The Client

All requests by clients must adhere to the following:

* Call `http://localhost:8123/api/v1`
    * `POST /resource`
    * `GET  /resource/{resourceId}`
* Use a base url pulled from configuration
* Have a timeout of 30 seconds
* Have an error handler
* Inject an api key retrieved from a service
* Use snake-casing


## Server

* All API calls should be snake case.
* All API calls should verify the api key.

| Method | Path          | Code | Description                   |
|--------|---------------|------|-------------------------------|
| `GET`  | `/resource/1` | 200  | Response body contains object |
| `GET`  | `/resource/2` | 400  | Not found error               |
| `GET`  | `/resource/3` | 400  | Nondescript error             |
| `POST` | `/resource`   | 200  | Response body contains object |
| `POST` | `/resource`   | 400  | Conflict error                |
| `POST` | `/resource`   | 400  | Nondescript error             |
