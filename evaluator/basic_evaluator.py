import json
from re import I
from types import SimpleNamespace
from abstract_evaluator import AbstractEvaluator
from database import Database
from docker_manager import DockerManager

class BasicEvalRequest:
    def __init__(self, request_id, task_solver_id, input_answer, graph):
        self.request_id = request_id
        self.task_solver_id = task_solver_id
        self.input_answer = input_answer
        self.graph = graph


    def __str__(self):
        return "InputAnswer: request_id=" + str(self.request_id)  + " task_solver_id=" + str(self.task_solver_id) + " input_answer=" + str(self.input_answer)

    

class BasicEvaluator(AbstractEvaluator):

    def __init__(self,docker_manager) -> None:
        self.db = Database()
        self.docker_manager = docker_manager;

    def get_queue_name(self):
        return "TASK_REQUEST"

    def run(self,request: BasicEvalRequest):
        # task_solver = self.db.get_task_solver_by_id(input_answer.task_solver_id)
        # if not task_solver:
        #     print(f"Task solver with id {input_answer.task_solver_id} not found. aborting request {input_answer.request_id}")
        #     return
        eval_script = "def a(g,a): print(True)"
        container = self.docker_manager.allocate_container()
        container.upload_script_file(eval_script)
        container.add_result_observer(self.on_result)
        container.run_task_solver(request.graph,request.input_answer,request.request_id)

        
    def on_result(self,request_id,is_correct):
        print("got result",request_id,is_correct)
        # TODO: save to db
        

    def on_request_received(self,body):
        print("Got request")
        o = json.loads(body, object_hook=lambda d: SimpleNamespace(**d))
        self.run(BasicEvalRequest(o.requestId,o.taskSolverId,o.inputAnswer))


