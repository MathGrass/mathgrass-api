import psycopg2


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
            
    def get_basic_eval_request_data(self,task_id):
        # get graph id
        tcur = self.conn.cursor()
        tcur.execute("SELECT * FROM tasks WHERE id = " + str(task_id))
        tasks = self.get_cursor_elements_as_dicts(tcur)
        try:
            task = next(tasks)
        except:
            print("task not found")
            return None
        task_template_id = task["task_template_id"]
        graph_id = task["graph_id"]

        # get graph
        gcur = self.conn.cursor()
        gcur.execute("SELECT * FROM graphs WHERE id = " + str(graph_id))
        graphs = self.get_cursor_elements_as_dicts(gcur)
        try:
            graph = next(graphs)
        except:
            print("graph not found")
            return None

        #TODO: create graph string
        
        # get template
        ttcur = self.conn.cursor()
        ttcur.execute("SELECT * FROM tasktemplates WHERE id = " + str(task_template_id))
        task_templates = self.get_cursor_elements_as_dicts(ttcur)
        try:
            task_template = next(task_templates)
        except:
            print("task template not found")
            return None
        task_solver_id = task_template["task_solver_id"]

        # get solver
        tscur = self.conn.cursor()
        tscur.execute("SELECT * FROM tasksolvers WHERE id = " + str(task_solver_id))
        task_solvers = self.get_cursor_elements_as_dicts(tscur)
        try:
            task_solver = next(task_solvers)
        except:
            print("task solver not found")
            return None
        script = task_solver["execution_descriptor"]


        return BasicEvalRequestData(graph,script)

    
    def get_cursor_elements_as_dicts(self,cursor):
        colnames = [desc[0] for desc in cursor.description]
        for row in cursor.fetchall():
            d={}
            for i in range(len(colnames)):
                d[colnames[i]]=row[i]
            yield d
        


    def add_evaluation_result(request_id, task_id,answer_is_true):
        pass

