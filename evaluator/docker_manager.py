import os
from typing import Container
import docker as dockerLib


from docker_container import DockerContainer

class DockerManager:


    def __init__(self,max_active_containers,ready_container_amount):
        self.docker = dockerLib.from_env()
        self.ready_containers = []
        self.occupied_containers = []
        self.max_active_containers = max_active_containers
        self.ready_container_amount = ready_container_amount

        # pull image
        print("pulling image")
        self.docker.images.pull("sagemath/sagemath")

        # init ready containers
        self.prepare_containers()
        
    
    def prepare_containers(self):
        ready = len(self.ready_containers)
        if ready<self.ready_container_amount:
            creation_amount = min(self.ready_container_amount-ready,self.max_active_containers-(len(self.occupied_containers)+ready))
            for _ in range(creation_amount):
                self.ready_containers.append(self.create_container_in_registry())
                pass

    def create_container_in_registry(self):
        do = self.docker.containers.create("sagemath/sagemath")
        return DockerContainer(do.name,self.docker)

    def remove_container_from_registry(self,container):
        container.vanish()

    def allocate_container(self):
        # check if enough docker - otherwise queue (should not happen because the msg bus will balance load)
        if not self.ready_containers:
            print("No free container")
            #TODO: queue request
            return
        if len(self.occupied_containers)>=self.max_active_containers:
            print("All available ")
        selection = self.ready_containers.pop()
        self.occupied_containers.append(selection)
        selection.add_result_observer(lambda x,y: self.finishContainer(selection))
        self.prepare_containers()
        return selection

    def finishContainer(self,container):
        # remove container
        self.occupied_containers.remove(container)

        # remove docker container
        self.remove_container_from_registry(container)

        # check if another container should be prepared
        self.prepare_containers()

    def clear_all_containers(self):
        for c in self.ready_containers:
            c.vanish()
    
    
    