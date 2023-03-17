from graph_parser import parse_jointjs_graph
from sage.all import *
import argparse
import logging

from instructor_evaluation import instructor_evaluation

parser = argparse.ArgumentParser()
parser.add_argument("user_answer", type=str, help="Answer provided by the user")

json_path = "/sage-evaluation/graph.json"
# for debugging purposes
# json_path = "./example_graph/graph.json"
logging.info(f"Parsing graph, path: {json_path}")
graph = parse_jointjs_graph("%s" % json_path)
logging.info("Parsed graph successfully")

logging.info("Parsing student answer")
args = parser.parse_args()
user_answer = args.user_answer
logging.info("Parsed student answer successfully")

if __name__ == '__main__':
    # evaluation code here
    # example: count edges:
    logging.info("Beginning evaluation")
    evaluation_result = instructor_evaluation(graph, user_answer)
    if evaluation_result:
        logging.info("Student answer correct. Exiting with return code 0")
        sys.exit(0)
    else:
        logging.info("Student answer incorrect. Exiting with return code 1")
        sys.exit(1)


