package de.tudresden.inf.st.mathgrassserver.api;

import de.tudresden.inf.st.mathgrassserver.apiModel.GraphApi;
import de.tudresden.inf.st.mathgrassserver.model.Graph;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GraphApiImpl implements GraphApi {

    @Override
    public ResponseEntity<Void> createGraph(Graph body) {
        return null;
    }

    @Override
    public ResponseEntity<Graph> getGraphById(Long graphId) {
        return null;
    }

}
