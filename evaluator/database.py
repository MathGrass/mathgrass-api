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
            


    def get_task_solver_by_id(self,solver_id):
        cur = self.conn.cursor()
        res = cur.execute("SELECT * FROM tasksolver WHERE id = " + str(solver_id))
        return res

