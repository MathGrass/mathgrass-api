from sage.all import *

def instructor_evaluation(graph: Graph, user_answer):
    if str(len(graph.edges())) == user_answer:
        return True
    else:
        return False