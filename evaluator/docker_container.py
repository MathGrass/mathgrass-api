import os

class DockerContainer:

    def __init__(self,name):
        self.result_observers = []
        self.name = name

    def upload_script_file(self, content):
        #TODO: implement
        pass

    def run_task_solver(self,graph,answer,request_id):
        #TODO: implement
        # ssh = paramiko.client.SSHClient()
        # ssh.connect("localhost")
        # _stdin, _stdout, _stderr = ssh.exec_command("docker exec <container id> /bin/bash -c pwd")


        # conn_string = f"docker exec -it {selection} /bin/bash && sage -c '{script.strip()}'"
        # stream = os.popen(conn_string)
        # output = stream.read()

        self.call_observers(request_id,True)
        

    def add_result_observer(self,o):
        self.result_observers.append(o)

    def call_observers(self,request_id, result):
        for o in self.result_observers:
            o(request_id,result)
