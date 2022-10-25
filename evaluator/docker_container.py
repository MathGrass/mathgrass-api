
from ast import Bytes
from distutils.dir_util import copy_tree
from io import BytesIO
import io
import tarfile
from docker import DockerClient


class ContentFile:
    def __init__(self,filepath,content):
        self.filepath = filepath
        self.content = content

class DockerContainer:

    def __init__(self,name,docker_client: DockerClient):
        self.result_observers = []
        self.name = name
        self.docker_client = docker_client
        self.phy_container = self.docker_client.containers.get(self.name)


    def upload_content_files(self, content_files):
        print("creating archive")
        fh = io.BytesIO()
        with tarfile.open(fileobj=fh, mode='w') as tar:
            for content_file in content_files:
                data = content_file.content.encode("utf-8")
                info = tarfile.TarInfo(content_file.filepath)
                info.size = len(data)
                tar.addfile(info, io.BytesIO(initial_bytes=data))
        self._upload_tar_file_inner(fh.getvalue())

    def _upload_tar_file_inner(self, tarfile):
        self.phy_container.start()
        print("uploading files")
        self.phy_container.put_archive(path="/home/sage/sage",data=tarfile)

    def run_task_solver(self,command,request_id):
        print("running commands")
        self.phy_container.start()

        a = self.phy_container.exec_run(cmd=command,workdir="/home/sage/sage",tty=True)
        output_log = str(a.output).split("\n")

        if not output_log:
            print("error: no output log from task request")
            return
        self.call_observers(request_id,output_log[-1] == 'True')
        

    def add_result_observer(self,o):
        self.result_observers.append(o)

    def call_observers(self,request_id, result):
        for o in self.result_observers:
            o(request_id,result)

    def vanish(self):
        try:
            self.phy_container.stop()
            self.phy_container.remove()
        except:
            pass
