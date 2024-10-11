import mysql.connector
from config import Config


class MySQLConnectionProvider:

    def __init__(self):
        self.connection = mysql.connector.connect(
                          host=Config.HOST,
                          user=Config.USER,
                          password=Config.PASSWORD,
                          database=Config.DATABASE)
        print(self.connection)
        print("MySQL connection provider initialized")

    def get_connection(self):
        return self.connection