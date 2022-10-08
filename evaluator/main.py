import time
from tkinter.filedialog import test
from basic_evaluator import BasicEvalRequest, BasicEvaluator
from docker_manager import DockerManager

from rabbitmq_client import MessageQueueMiddleware

ALL_EVALUATORS = [BasicEvaluator]

BROKER_HOST = "127.0.0.1"
MAX_ACTIVE_CONTAINERS = 100
READY_CONTAINERS = 3

def main():
    print("Starting Evaluator")
    # TODO: connect to database


    # initializing docker containers
    docker_manager = DockerManager(MAX_ACTIVE_CONTAINERS,READY_CONTAINERS)
    # test_docker_manager(docker_manager)

    # create message queue middleware instance
    msg_queue_middleware = MessageQueueMiddleware(BROKER_HOST)

    # map queues to evaluators
    for evaluator in ALL_EVALUATORS:
        instance = evaluator(docker_manager)
        queue_name = instance.get_queue_name()

        def on_request_received(ch, method, properties, body):
            instance.on_request_received(body)
    
        msg_queue_middleware.consume(queue_name,on_request_received)
        



if __name__ == '__main__':
    main()