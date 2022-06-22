from abstract_evaluator import AbstractEvaluator


class BasicEvaluator(AbstractEvaluator):

    def run_eva(self,script_id, answer):
        return True

    def get_queue_name(self):
        return "basic-eva-tasks"