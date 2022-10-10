from email.mime import base
import json
from re import I
from types import SimpleNamespace
from abstract_evaluator import AbstractEvaluator
from database import Database
from docker_container import ContentFile
from docker_manager import DockerManager
import base64
class BasicEvalRequest:
    def __init__(self, request_id, task_id, input_answer):
        self.request_id = request_id
        self.task_id = task_id
        self.input_answer = input_answer


    def __str__(self):
        return "InputAnswer: request_id=" + str(self.request_id)  + " task_id=" + str(self.task_id) + " input_answer=" + str(self.input_answer)

    

class BasicEvaluator(AbstractEvaluator):

    def __init__(self,docker_manager) -> None:
        self.db = Database()
        self.docker_manager = docker_manager;

    def get_queue_name(self):
        return "TASK_REQUEST"

    def run(self,request: BasicEvalRequest):
        
        # getting a free container
        container = self.docker_manager.allocate_container()
        if not container:
            print(f"could not run task {request.request_id} because no docker container could be allocated")
            return;

        
        # fetching data
        request_data = self.db.get_basic_eval_request_data(request.task_id)
        if not request_data:
            print("aborting")
            return
        eval_script = request_data.script
        graph_obj = request_data.graph

        # graph
        graph_json =  json.dumps(graph_obj)
        graph_encoded = base64.b64encode(graph_json.encode("utf-8")).decode("utf-8")

        # answer
        answer_encoded = base64.b64encode(request.input_answer.encode("utf-8")).decode("utf-8")

        # building the command to run
        command = f"sage eval.sage {answer_encoded} {graph_encoded}"

        # loading and moving eval script to docker
        
        container.upload_content_files([ContentFile("eval.sage",eval_script)])

        # preparing and launching
        container.add_result_observer(self.on_result)
        container.run_task_solver(command,request.request_id)


        
    def on_result(self,request_id,is_correct):
        print("got result",request_id,is_correct)
        # TODO: save to db
        

    def on_request_received(self,body):
        print("Got request")
        o = json.loads(body, object_hook=lambda d: SimpleNamespace(**d))
        self.run(BasicEvalRequest(o.requestId,o.taskId,o.inputAnswer))



