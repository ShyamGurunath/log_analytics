from models.response import Response
from utils.mysql_connection_provider import MySQLConnectionProvider


class ApplicationDataAccess:

    def __init__(self, db_provider: MySQLConnectionProvider):
        self.db_provider = db_provider

    def create_application(self, app_name: str) -> Response:
        cursor = None
        try:
            connection = self.db_provider.get_connection()
            cursor = connection.cursor(buffered=True)
            cursor.execute("INSERT INTO DSK_APPLICATION (app_name) VALUES (%s)", (app_name,))
            if cursor.rowcount <= 0:
                return Response(success=False, message="Failed to Insert", data={})
            connection.commit()
            return Response(success=True, message="Successfully Inserted", data={"app_name": app_name})
        finally:
            if cursor is not None:
                cursor.close()

    def list_applications(self) -> Response:
        cursor = None
        try:
            connection = self.db_provider.get_connection()
            cursor = connection.cursor(buffered=True)
            cursor.execute("SELECT * FROM DSK_APPLICATION")
            rows = cursor.fetchall()
            if cursor.rowcount <= 0:
                return Response(success=False, message="Failed to Get data since its empty", data=[])
            return Response(success=True, message="Successfully Fetched", data=[r[1] for r in rows])
        finally:
            if cursor is not None:
                cursor.close()
