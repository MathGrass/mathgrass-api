import os
import docker
import paramiko

class DockerManager:


    def __init__(self,max_containers):
        self.client = docker.from_env()
        self.docker_containers = []
        self.occupied_containers = []
        # TODO: start docker containers to be ready and save their names
        self.docker_containers.append("musing_brattain")

    
    def run_eval(self,script,params,request_id):
        # TODO: check if enough docker - otherwise queue (should not happen because the msg bus will balance load)
        free_containers = [c for c in self.docker_containers if c not in self.occupied_containers]
        if not free_containers:
            print("No free container")
            #TODO: queue request
            return
        selection = free_containers.pop()
        self.occupied_containers.append(selection)
        #container = self.client.containers.get(selection)


        # ssh = paramiko.client.SSHClient()
        # ssh.connect("localhost")
        # _stdin, _stdout, _stderr = ssh.exec_command("docker exec <container id> /bin/bash -c pwd")
        conn_string = f"docker exec -it {selection} /bin/bash && sage -c '{script.strip()}'"
        stream = os.popen(conn_string)
        output = stream.read()
        print("result=",output)

        self.on_docker_finished_task(request_id,output)

        


    def on_docker_finished_task(self,request,result):
        #TODO: remove container
        #TODO: store result
        pass

    
    