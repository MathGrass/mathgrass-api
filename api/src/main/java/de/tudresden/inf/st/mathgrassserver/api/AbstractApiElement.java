package de.tudresden.inf.st.mathgrassserver.api;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

public abstract class AbstractApiElement {
    protected void checkExistence(Long id, JpaRepository<?,Long> repository ) {
        if (!repository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    protected <T>ResponseEntity<T> ok(T body) {
        return ResponseEntity.ok(body);
    }

    protected ResponseEntity<Void> ok() {
        return ResponseEntity.ok().build();
    }

    protected <T>ResponseEntity<T> notFound() {
        throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    protected void illegalArgs() {
        throw new ResponseStatusException(HttpStatus.FORBIDDEN);
    }
}
