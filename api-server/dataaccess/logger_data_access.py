from models.response import Response
from utils.elastic_search_admin_handler import ElasticSearchAdminHandler
from utils.kafka_admin_handler import KafkaAdminHandler
from utils.mapper import Mapper
from utils.mysql_connection_provider import MySQLConnectionProvider


class LoggerDataAccess:

    def __init__(self, db_provider: MySQLConnectionProvider, kafka_provider: KafkaAdminHandler, es_provider: ElasticSearchAdminHandler):
        self.db_provider = db_provider
        self.kafka_handler = kafka_provider
        self.es_handler = es_provider

    def create_logger(self, app_name: str, logger_name: str) -> Response:
        cursor = None
        try:
            connection = self.db_provider.get_connection()
            cursor = connection.cursor(buffered=True)
            cursor.execute("SELECT app_id from DSK_APPLICATION where app_name = %s", (app_name,))
            if cursor.rowcount <= 0:
                return Response(success=False, message="Failed to Get application data since its empty", data=[])
            app_id: int = cursor.fetchone()[0]
            print(f"App id : {app_id}")
            unique_logger = f"{app_name}_{app_id}_{logger_name}"
            cursor.execute("INSERT INTO DSK_LOGGER (app_id, name, kafka_topic) VALUES (%s, %s, %s)", (app_id, logger_name, unique_logger))
            if cursor.rowcount <= 0:
                return Response(success=False, message="Failed to Insert", data={})
            self.kafka_handler.create_kafka_topic(unique_logger)
            self.es_handler.create_index(unique_logger)
            connection.commit()
            return Response(success=True, message="Successfully Inserted", data={"logger_name": logger_name})
        finally:
            if cursor is not None:
                cursor.close()

    def list_loggers(self, app_name: str) -> Response:
        cursor = None
        try:
            connection = self.db_provider.get_connection()
            cursor = connection.cursor(buffered=True)
            cursor.execute("SELECT app_id from DSK_APPLICATION where app_name = %s", (app_name,))
            if cursor.rowcount <= 0:
                return Response(success=False, message="Failed to Get logger data since its empty", data=[])
            app_id: int = cursor.fetchone()[0]
            cursor.execute("SELECT name, kafka_topic FROM DSK_LOGGER where app_id = %s", (app_id,))
            rows = cursor.fetchall()
            print(f"Rows    : {len(rows)} : {rows}")
            if cursor.rowcount <= 0:
                return Response(success=False, message="Failed to list logger data since its empty", data=[])
            return Response(success=True, message="Successfully Fetched", data=Mapper.LoggerMapper.map_to_logger(app_name, app_id, rows))
        finally:
            if cursor is not None:
                cursor.close()
