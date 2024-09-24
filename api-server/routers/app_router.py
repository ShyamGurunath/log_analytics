from starlette import status

from dataaccess.application_data_access import ApplicationDataAccess
from models.response import Response
from fastapi import APIRouter, Request

router = APIRouter()

@router.post("/application/register", description="Register application", response_model=Response,
            status_code=status.HTTP_201_CREATED, tags=["Application"])
def register_application(request: Request, app_name: str) -> Response:
    return ApplicationDataAccess(request.app.state.db_provider).create_application(app_name=app_name)

@router.get("/application/list", description="List applications", response_model=Response,
           status_code=status.HTTP_200_OK, tags=["Application"])
def list_applications(request: Request) -> Response:
    return ApplicationDataAccess(request.app.state.db_provider).list_applications()

@router.get("/application/fetch", description="Fetch a application", response_model=Response,
           status_code=status.HTTP_200_OK, tags=["Application"])
def fetch_application(request: Request, app_name: str) -> Response:
    return ApplicationDataAccess(request.app.state.db_provider).fetch_application(app_name=app_name)