import json
import psycopg2

from model.graph_model import Edge, Graph, Vertex


class BasicEvalRequestData:
    def __init__(self, graph, script):
        self.graph = graph;
        self.script = script;

class Database:
    def __init__(self):
        try:
            self.conn = psycopg2.connect(
                host="localhost",
                database="mathgrass_db")
            print("connected to database")


        except (Exception, psycopg2.DatabaseError) as error:
            print(error)
            if self.conn is not None:
                self.conn.close()
                print("database connection closed");
            
    def _get_task_template_and_graph_id(self, task_id):
        tcur = self.conn.cursor()
        tcur.execute("SELECT * FROM tasks WHERE id = " + str(task_id))
        tasks = self.get_cursor_elements_as_dicts(tcur)
        try:
            task = next(tasks)
        except:
            print("task not found")
            return None
        return task["task_template_id"],task["graph_id"]
   
    def _get_task_solver_id(self,task_template_id):
        ttcur = self.conn.cursor()
        ttcur.execute("SELECT * FROM tasktemplates WHERE id = " + str(task_template_id))
        task_templates = self.get_cursor_elements_as_dicts(ttcur)
        try:
            task_template = next(task_templates)
        except:
            print("task template not found")
            return None
        return task_template["task_solver_id"]


    def _get_execution_descriptor(self, task_solver_id):
        tscur = self.conn.cursor()
        tscur.execute("SELECT * FROM tasksolvers WHERE id = " + str(task_solver_id))
        task_solvers = self.get_cursor_elements_as_dicts(tscur)
        try:
            task_solver = next(task_solvers)
        except:
            print("task solver not found")
            return None
        return task_solver["execution_descriptor"]

    def _get_graph(self,graph_id):
        gcur = self.conn.cursor()
        gcur.execute("SELECT * FROM graphs WHERE id = " + str(graph_id))
        graphs = self.get_cursor_elements_as_dicts(gcur)
        graph = None
        try:
            graph = next(graphs)
        except:
            print("graph solver not found")
            return None

        #create graph string
        vertices = self._get_vertices(graph_id)
        edges = self._get_edges(graph_id)

        vertex_dict = {}
        for vertex in vertices:
            vertex_dict[vertex["id"]] = Vertex(vertex["id"],vertex["label"],vertex["x"],vertex["y"])
        
        edge_obj_list = []
        for edge in edges:
            edge_obj_list.append(Edge(vertex_dict[edge["v1_id"]],vertex_dict[edge["v2_id"]],edge["label"]))

        return Graph(graph["id"],graph["label"],vertex_dict.values(),edge_obj_list)
            
    
    def _get_edges(self,graph_id):
        ecur = self.conn.cursor()
        ecur.execute("SELECT * FROM graphs_edges AS ge INNER JOIN edges AS e ON e.id = ge.edges_id WHERE graph_entity_id = " + str(graph_id))
        edges = self.get_cursor_elements_as_dicts(ecur)
        return edges


    def _get_vertices(self,graph_id):
        vcur = self.conn.cursor()
        vcur.execute("SELECT * FROM graphs_vertices AS gv INNER JOIN vertices AS v ON v.id = gv.vertices_id WHERE graph_entity_id = " + str(graph_id))
        vertices = self.get_cursor_elements_as_dicts(vcur)
        return vertices


    def get_basic_eval_request_data(self,task_id):

        task_template_id,graph_id = self._get_task_template_and_graph_id(task_id)

        task_solver_id = self._get_task_solver_id(task_template_id)
        script = self._get_execution_descriptor(task_solver_id)
        graph = self._get_graph(graph_id)
        

        return BasicEvalRequestData(graph,script)

    
    def get_cursor_elements_as_dicts(self,cursor):
        colnames = [desc[0] for desc in cursor.description]
        for row in cursor.fetchall():
            d={}
            for i in range(len(colnames)):
                d[colnames[i]]=row[i]
            yield d
        


    def add_evaluation_result(self,request_id ,answer_is_true, time):
        # update task result with id = request_id (table name is taskresults)
        cur = self.conn.cursor()
        answer_true = "true" if answer_is_true else "false"
        sql = "UPDATE taskresults SET answer_true = %s, evaluation_date = %s WHERE id = %s"
        cur.execute(sql,(str(answer_true),str(time),str(request_id)))
        self.conn.commit()

