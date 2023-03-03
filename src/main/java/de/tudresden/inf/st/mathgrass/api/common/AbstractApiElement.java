package de.tudresden.inf.st.mathgrass.api.common;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

/**
 * This abstract class defines the layout for API elements.
 */
public abstract class AbstractApiElement {
    /**
     * Check whether the element exists in a repository.
     *
     * @param id ID of element
     * @param repository repository in which element should be stored
     * @throws ResponseStatusException if element doesn't exist
     */
    protected void checkExistence(Long id, JpaRepository<?,Long> repository) {
        if (!repository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Return an 'OK' response with body.
     *
     * @param body body to return
     * @return Response with body
     * @param <T> type of entity
     */
    protected <T>ResponseEntity<T> ok(T body) {
        return ResponseEntity.ok(body);
    }

    /**
     * Return an 'OK' response.
     *
     * @return Response
     */
    protected ResponseEntity<Void> ok() {
        return ResponseEntity.ok().build();
    }

    /**
     * Return a 'Not Found' response.
     *
     * @return Response
     * @param <T> type of entity
     */
    protected <T>ResponseEntity<T> notFound() {
        throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    /**
     * Throw a {@link ResponseStatusException} notifying about forbidden actions.
     */
    protected void illegalArgs() {
        throw new ResponseStatusException(HttpStatus.FORBIDDEN);
    }
}
