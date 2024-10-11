import logging
from elasticsearch import Elasticsearch


class ElasticSearchAdminHandler:

    def __init__(self):
        self.es = Elasticsearch("http://dsk_elastic_search:9200")
        logging.info("ElasticSearchAdminHandler initialized")

    def create_index(self, index_name):
        return self.es.indices.create(index=index_name)