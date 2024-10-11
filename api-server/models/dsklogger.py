from typing import Optional

from pydantic import BaseModel


class DskLogger(BaseModel):
    app_id: int
    app_name: str
    name: str
    kafka_topic: str


class CreateLogger(BaseModel):
    app_name: str
    logger_name: Optional[str] = None

