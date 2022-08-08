package de.tudresden.inf.st.mathgrassserver.api;

import de.tudresden.inf.st.mathgrassserver.apiModel.TagApi;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TagApiImpl implements TagApi {

    @Override
    public ResponseEntity<Void> getTagById(Long id) {
        return null;
    }
}
