from dataclasses import dataclass
from typing import override

from litestar import Controller, Litestar, Response, get, post
from litestar.connection import ASGIConnection
from litestar.exceptions import NotAuthorizedException
from litestar.middleware import AbstractAuthenticationMiddleware
from litestar.middleware.authentication import AuthenticationResult

# ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
# DTOs
# ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~


@dataclass
class CreateResourceRequest:
    first_name: str


@dataclass
class Resource:
    user_id: int
    first_name: str


@dataclass
class BadRequest:
    status_code: int
    error_code: str
    message: str


# ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
# Routing / Controllers
# ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~


class ResourceController(Controller):
    path = "/resource"

    @get("/{resource_id:int}")
    async def fetch_resource(self, resource_id: int) -> Resource | Response:
        match resource_id:
            case 1:
                return Resource(1, "a")
            case 2:
                bad_request = BadRequest(
                    status_code=404,
                    error_code="NOT_FOUND",
                    message=f"Could not locate resource '{resource_id}'",
                )
                return Response(content=bad_request, status_code=400)
            case _:
                bad_request = BadRequest(
                    status_code=400,
                    error_code="BAD_REQUEST",
                    message=f"Generic bad request for resource '{resource_id}'",
                )
                return Response(content=bad_request, status_code=400)

    @post()
    async def create_resource(self, data: CreateResourceRequest) -> Resource | Response:
        match data.first_name:
            case "a":
                return Resource(1, "a")
            case "b":
                bad_request = BadRequest(
                    status_code=409,
                    error_code="CONFLICTING",
                    message=f"Resource already exists with first_name '{data.first_name}'",
                )
                return Response(content=bad_request, status_code=400)
            case _:
                bad_request = BadRequest(
                    status_code=400,
                    error_code="BAD_REQUEST",
                    message=f"Generic bad request for resource with first_name '{data.first_name}'",
                )
                return Response(content=bad_request, status_code=400)


# ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
# Middleware
# ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

API_KEY_HEADER = "X-API-KEY"
API_KEY = "someKey"


class ApiKeyAuthentication(AbstractAuthenticationMiddleware):
    @override
    async def authenticate_request(self, connection: ASGIConnection) -> AuthenticationResult:
        auth_header = connection.headers.get(API_KEY_HEADER)
        if auth_header != API_KEY:
            raise NotAuthorizedException

        return AuthenticationResult(user=None, auth=None)


# ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
# Litestar registration
# ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

app = Litestar(
    [ResourceController],
    path="/api/v1",
    middleware=[ApiKeyAuthentication],
    openapi_config=None,
)
