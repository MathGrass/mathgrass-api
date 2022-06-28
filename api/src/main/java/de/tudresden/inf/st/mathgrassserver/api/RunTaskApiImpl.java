package de.tudresden.inf.st.mathgrassserver.api;

import de.tudresden.inf.st.mathgrassserver.apiModel.RunTaskApi;
import de.tudresden.inf.st.mathgrassserver.model.Task;
import org.springframework.http.ResponseEntity;

public class RunTaskApiImpl implements RunTaskApi {

    @Override
    public ResponseEntity<Void> runTask(String id, String answer) {
        return null;
    }
}
