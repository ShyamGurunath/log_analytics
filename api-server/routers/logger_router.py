from fastapi import APIRouter
from starlette import status
from models.response import Response

router = APIRouter()

@router.post("/logger/add", description="Add a logger", response_model=Response,
            status_code=status.HTTP_201_CREATED, tags=["Logger"])
def add_logger():
    pass


@router.get("/logger/list", description="List loggers", response_model=Response,
           status_code=status.HTTP_200_OK, tags=["Logger"])
def list_loggers():
    pass


@router.get("/logger/fetch", description="Fetch a logger", response_model=Response,
           status_code=status.HTTP_200_OK, tags=["Logger"])
def fetch_logger():
    pass
