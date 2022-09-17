import json
from re import I
from types import SimpleNamespace
from abstract_evaluator import AbstractEvaluator
from database import Database
from input_answer import InputAnswer


class BasicEvaluator(AbstractEvaluator):

    def __init__(self) -> None:
        self.db = Database()

    def get_queue_name(self):
        return "TASK_REQUEST"

    def run(self,input_answer: InputAnswer):
        print(str(input_answer))
        task_solver = self.db.get_task_solver_by_id(input_answer.task_solver_id);
        if not task_solver:
            print(f"Task solver with id {input_answer.task_solver_id} not found. aborting request {input_answer.request_id}")
            return
            
        print("using solver",task_solver)
        


    def on_result(self,request_id,is_correct):
        print("got result",request_id,is_correct)
        # TODO: save to db
        

    def on_request_received(self,body):
        print("Got request")
        o = json.loads(body, object_hook=lambda d: SimpleNamespace(**d))
        self.run(InputAnswer(o.requestId,o.taskSolverId,o.inputAnswer))

    