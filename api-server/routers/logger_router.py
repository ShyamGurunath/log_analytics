from fastapi import APIRouter
from starlette import status
from starlette.requests import Request

from dataaccess.logger_data_access import LoggerDataAccess
from models.dsklogger import CreateLogger
from models.response import Response

router = APIRouter()

@router.post("/logger/add", description="Add a logger", response_model=Response,
            status_code=status.HTTP_201_CREATED, tags=["DskLogger"])
def add_logger(request: Request, logger: CreateLogger):
    return LoggerDataAccess(request.app.state.db_provider, request.app.state.kafka, request.app.state.es).create_logger(logger.app_name, logger.logger_name)


@router.post("/logger/list", description="List loggers", response_model=Response,
           status_code=status.HTTP_200_OK, tags=["DskLogger"])
def list_loggers(request: Request, app_name: str):
    return LoggerDataAccess(request.app.state.db_provider, request.app.state.kafka, request.app.state.es).list_loggers(app_name)

