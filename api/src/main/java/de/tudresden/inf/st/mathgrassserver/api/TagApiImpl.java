package de.tudresden.inf.st.mathgrassserver.api;

import de.tudresden.inf.st.mathgrassserver.apiModel.TagApi;
import de.tudresden.inf.st.mathgrassserver.model.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TagApiImpl implements TagApi {

    @Override
    public ResponseEntity<Tag> getTagById(Long id) {
        return null;
    }
}
