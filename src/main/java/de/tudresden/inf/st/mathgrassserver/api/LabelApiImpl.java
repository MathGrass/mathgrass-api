package de.tudresden.inf.st.mathgrassserver.api;

import de.tudresden.inf.st.mathgrassserver.apiModel.LabelApi;
import de.tudresden.inf.st.mathgrassserver.database.entity.LabelEntity;
import de.tudresden.inf.st.mathgrassserver.database.repository.LabelRepository;
import de.tudresden.inf.st.mathgrassserver.model.Label;
import de.tudresden.inf.st.mathgrassserver.transform.TagTransformer;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

/**
 * This class can create and load {@link Label}s.
 */
@RestController
public class LabelApiImpl extends AbstractApiElement implements LabelApi {
    /**
     * Label repository.
     */
    final LabelRepository labelRepository;

    /**
     * Constructor.
     *
     * @param labelRepository label repository
     */
    public LabelApiImpl(LabelRepository labelRepository) {
        this.labelRepository = labelRepository;
    }

    /**
     * Create a label and save to database.
     *
     * @param label label instance to save
     * @return Response containing label
     */
    public ResponseEntity<Label> createLabel(Label label) {
        LabelEntity labelEntity = labelRepository.save(new TagTransformer().toEntity(label));
        label.setId(labelEntity.getId());

        return ok(label);
    }

    /**
     * Get a Label from the database from specified ID.
     *
     * @param id ID of label to get
     * @return Response containing label
     */
    @Override
    public ResponseEntity<Label> getLabelById(Long id) {
        // check if label exists
        Optional<LabelEntity> optLabel = labelRepository.findById(id);

        if (optLabel.isPresent()) {
            Label label = new TagTransformer().toDto(optLabel.get());
            return ok(label);
        } else {
            return notFound();
        }
    }

    /**
     * Get all tags available in the database.
     *
     * @return Response with list of tags
     */
    @Override
    public ResponseEntity<List<Label>> getLabels() {
        return ok(new TagTransformer().toDtoList(labelRepository.findAll()));
    }
}
