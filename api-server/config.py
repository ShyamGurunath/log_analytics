import os


class Config:

    DATABASE = os.environ.get('DATABASE')
    HOST = os.environ.get('HOST')
    USER = os.environ.get('USER')
    PASSWORD = os.environ.get('PASSWORD')