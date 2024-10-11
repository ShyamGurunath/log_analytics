from models.dsklogger import DskLogger


class Mapper:
    class LoggerMapper:

        @staticmethod
        def map_to_logger(app_name: str, app_id: str, rows: list) -> list[dict]:
            loggers = []
            for l in rows:
                loggers.append(DskLogger(app_name=app_name, app_id=app_id, kafka_topic=l[1], name=l[0]).dict())
            return loggers
