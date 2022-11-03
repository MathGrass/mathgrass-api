package de.tudresden.inf.st.mathgrassserver.custom;

import de.tudresden.inf.st.mathgrassserver.api.EvaluatorApiImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class TaskRunner {

    @Autowired
    EvaluatorApiImpl evaluatorApiImpl;

    @Test
    void testSingleTask() {
        while (true) {
            evaluatorApiImpl.runTask((long)23,"6");
        }

    }
}
