import time
from tkinter.filedialog import test
from basic_evaluator import BasicEvaluator
from docker_manager import DockerManager

from rabbitmq_client import MessageQueueMiddleware, build_answer_queue_msg, parse_eva_request

ALL_EVALUATORS = [BasicEvaluator]

BROKER_HOST = "127.0.0.1"
MAX_CONTAINERS = 3

def main():
    print("Starting Evaluator")
    # TODO: connect to database


    # initializing docker containers
    docker_manager = DockerManager(MAX_CONTAINERS)
    test_docker_manager(docker_manager)

    # create message queue middleware instance
    msg_queue_middleware = MessageQueueMiddleware(BROKER_HOST)

    # map queues to evaluators
    for evaluator in ALL_EVALUATORS:
        instance = evaluator()
        queue_name = instance.get_queue_name()

        def on_result(request_id,is_correct):
            print("got result",request_id,is_correct)
            queue_msg = build_answer_queue_msg(request_id,is_correct)
            msg_queue_middleware.publish(queue_name + "-answers",queue_msg)

        def on_request_received(ch, method, properties, body):
            request_id, script_id, answer = parse_eva_request(body)
            is_correct = instance.run_eva(script_id,answer)
            on_result(request_id,is_correct)
        
        msg_queue_middleware.consume(queue_name,on_request_received)
        #time.sleep(2)
        #on_request_received("","","",{"request":1234, "task" : "abfeiuhgj", "answer" : "345"})

def test_docker_manager(docker_manager):
        script = """
def a(x):
    print(x==1)
"""
        docker_manager.run_eval(script,2,3)


if __name__ == '__main__':
    main()