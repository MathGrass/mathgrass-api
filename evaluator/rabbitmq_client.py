from subprocess import call
import pika
import threading

class MessageQueueMiddleware:

    def __init__(self,broker_host):
        connection = pika.BlockingConnection(
        pika.ConnectionParameters(host=broker_host))
        print("Connected to message queue")
        self.channel = connection.channel()

    def consume(self,queue, callback):
        self.channel.queue_declare(queue=queue)
        thread = threading.Thread(target=self._inner_consume,args=(queue,callback))
        thread.start()
        print("Consuming Started")

    def _inner_consume(self,queue,callback):
        self.channel.basic_consume(queue=queue, on_message_callback=callback, auto_ack=True)
        print(f'Waiting for messages on queue "{queue}"')
        self.channel.start_consuming()


    def publish(self,queue,msg):
        self.channel.basic_publish(exchange='',routing_key=queue,body=msg)
        print("Published",msg,"on",queue)


def build_answer_queue_msg(request_id,is_correct):
    return {
        "request" : request_id,
        "is_correct": is_correct
    }

