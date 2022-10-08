import os
import docker as dockerLib
import paramiko

from docker_container import DockerContainer

class DockerManager:


    def __init__(self,max_active_containers,ready_container_amount):
        self.docker = dockerLib.from_env()
        self.ready_containers = []
        self.occupied_containers = []
        self.max_active_containers = max_active_containers
        self.ready_container_amount = ready_container_amount

        self.prepare_containers()
        
    
    def prepare_containers(self):
        ready = len(self.ready_containers)
        if ready<self.ready_container_amount:
            creation_amount = min(self.ready_container_amount-ready,self.max_active_containers-(len(self.occupied_containers)+ready))
            for _ in range(creation_amount):
                self.ready_containers.append(self.create_container_in_registry())
                pass
            
            #TODO: remove following test line
            self.ready_containers.append(DockerContainer("musing_brattain"))
        

    def create_container_in_registry(self):
        #TODO: implement
        return DockerContainer("")

    def remove_container_from_registry(self,container):
        #TODO: remove container.name
        pass

    def allocate_container(self):
        # TODO: check if enough docker - otherwise queue (should not happen because the msg bus will balance load)
        if not self.ready_containers:
            print("No free container")
            #TODO: queue request
            return
        if len(self.occupied_containers)>=self.max_active_containers:
            print("All available ")
        selection = self.ready_containers.pop()
        self.occupied_containers.append(selection)
        self.prepare_containers()
        return selection

    def finishContainer(self,container):
        # remove container
        self.occupied_containers.remove(container)

        # remove docker container
        self.remove_container_from_registry(container)

        # check if another container should be prepared
        self.prepare_containers()
    
    
    