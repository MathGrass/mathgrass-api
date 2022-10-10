import psycopg2

class Database:
    def __init__(self):
        try:
            self.conn = psycopg2.connect(
                host="localhost",
                database="mathgrass_db")
            print("Connected to database")


        except (Exception, psycopg2.DatabaseError) as error:
            print(error)
            if self.conn is not None:
                self.conn.close()
                print("Database connection closed");
            


    def get_task_solver_script_by_id(self,solver_id):
        #cur = self.conn.cursor()
        #res = cur.execute("SELECT * FROM tasksolver WHERE id = " + str(solver_id))
        #return res
        return "print(True)"

    def get_graph_from_task(self,task_id):
        # # get graph id
        # tcur = self.conn.cursor()
        # tres = tcur.execute("SELECT * FROM task WHERE id = " + str(task_id))
        # print(tres)

        # # get graph
        # gcur = self.conn.cursor()
        # gres = gcur.execute("SELECT * FROM task WHERE id = " + str(task_id))
        # #return res
        return "{}"

    def add_evaluation_result(request_id, task_id,answer_is_true):
        pass

