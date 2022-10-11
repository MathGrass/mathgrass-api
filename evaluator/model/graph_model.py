import json


class Graph:

    def __init__(self,id,label,vertices,edges):
        self.id = id
        self.edges = edges;
        self.vertices = vertices;
        self.label = label;


    
    def to_json(self):
        return {
            "id": self.id,
            "edges": [edge.to_json() for edge in self.edges],
            "vertices": [vertex.to_json() for vertex in self.vertices],
            "label" : self.label
        }
    

class Vertex:
    def __init__(self,id,label,x,y):
        self.id = id
        self.label = label
        self.x = x
        self.y = y


    def to_json(self):
        return {
            "id": self.id,
            "label": self.label,
            "x": self.x,
            "y": self.y
        }

class Edge:
    def __init__(self,first_vertex,second_vertex,label):
        self.first_vertex = first_vertex
        self.second_vertex = second_vertex
        self.label = label

    def to_json(self):
        return {
            "firstVertex" : self.first_vertex.to_json(),
            "secondVertex" : self.second_vertex.to_json(),
            "label" : self.label
        }

