import os
from docker import DockerClient 
import paramiko


class DockerContainer:

    def __init__(self,name,docker_client: DockerClient):
        self.result_observers = []
        self.name = name
        self.docker_client = docker_client
        self.phy_container = self.docker_client.containers.get(self.name)

    def upload_script_file(self, content):
        #TODO: implement
        pass

    def run_task_solver(self,graph,answer,request_id):
        
        #phy_container.exec_run("echo 333")
        #a = phy_container.attach()
        #TODO: implement
        #ssh = paramiko.client.SSHClient()
        #ssh.connect("localhost")
        #_stdin, _stdout, _stderr = ssh.exec_command(f"docker exec {self.name} /bin/bash -c pwd")


        # conn_string = f"docker exec -it {selection} /bin/bash && sage -c '{script.strip()}'"
        # stream = os.popen(conn_string)
        # output = stream.read()

        self.call_observers(request_id,True)
        

    def add_result_observer(self,o):
        self.result_observers.append(o)

    def call_observers(self,request_id, result):
        for o in self.result_observers:
            o(request_id,result)

    def vanish(self):
        self.phy_container.stop()
        self.phy_container.remove()
        pass
