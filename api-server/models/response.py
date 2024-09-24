from typing import List, Union

from pydantic import BaseModel


class Response(BaseModel):
    success: bool
    message: str
    data: Union[dict, List[dict], List]

