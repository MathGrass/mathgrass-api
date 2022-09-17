class InputAnswer:
    def __init__(self, request_id, task_solver_id, input_answer):
        self.request_id = request_id
        self.task_solver_id = task_solver_id
        self.input_answer = input_answer


    def __str__(self):
        return "InputAnswer: request_id=" + str(self.request_id)  + " task_solver_id=" + str(self.task_solver_id) + " input_answer=" + str(self.input_answer)