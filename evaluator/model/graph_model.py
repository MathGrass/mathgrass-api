class Graph:

    def __init__(id,self,edges,vertices):
        self.id = id
        self.edges = edges;
        self.vertices = vertices;

class Vertex:
    def __init__(self,id,label):
        self.id = id
        self.label = label

class Edge:
    def __init__(self,vertex1,vertex2):
        self.vertex1 = vertex1
        self.vertex2 = vertex2
