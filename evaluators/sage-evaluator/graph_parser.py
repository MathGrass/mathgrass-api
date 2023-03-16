import json
from sage.all import *


def parse_jointjs_graph(filename):
    with open(filename) as file:
        data = json.load(file)
        graph = Graph()

        edges = data["edges"]
        vertices = data["vertices"]

        for vertex in vertices:
            graph.add_vertex(vertex["id"])
        for edge in edges:
            graph.add_edge(edge["firstVertex"]["id"], edge["secondVertex"]["id"], edge["id"])

        return graph
