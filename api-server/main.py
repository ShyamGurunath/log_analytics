from fastapi import FastAPI
from contextlib import asynccontextmanager
from routers.app_router import router as app_router
from routers.logger_router import router as logger_router
from utils.mysql_connection_provider import MySQLConnectionProvider


@asynccontextmanager
async def lifespan(app: FastAPI):
    db: MySQLConnectionProvider = MySQLConnectionProvider()
    app.state.db_provider = db
    yield
app = FastAPI(lifespan=lifespan)

app.include_router(app_router)

app.include_router(logger_router)


@app.get("/")
async def health_check():
    return {"message": "Application Running"}
