import mysql.connector.cursor


class DbUtil:

    @staticmethod
    def commit(connection: mysql.connector.cursor.MySQLConnection):
        connection.commit()

    @staticmethod
    def get_cursor(connection: mysql.connector.cursor.MySQLConnection):
        return connection.cursor()

    @staticmethod
    def close_cursor(cursor: mysql.connector.cursor.MySQLCursor):
        cursor.close()