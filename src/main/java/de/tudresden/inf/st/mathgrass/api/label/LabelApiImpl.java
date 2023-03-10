package de.tudresden.inf.st.mathgrass.api.label;

import de.tudresden.inf.st.mathgrass.api.apiModel.LabelApi;
import de.tudresden.inf.st.mathgrass.api.common.AbstractApiElement;
import de.tudresden.inf.st.mathgrass.api.model.LabelDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

/**
 * This class can create and load {@link LabelDTO}s.
 */
@RestController
public class LabelApiImpl extends AbstractApiElement implements LabelApi {
    /**
     * LabelDTO repository.
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
    public ResponseEntity<LabelDTO> createLabel(LabelDTO label) {
        Label labelEntity = new Label();
        labelEntity.setValue(label.getLabel());
        labelEntity = labelRepository.save(labelEntity);
        label.setId(labelEntity.getId());

        return ok(label);
    }

    /**
     * Get a LabelDTO from the database from specified ID.
     *
     * @param id ID of label to get
     * @return Response containing label
     */
    @Override
    public ResponseEntity<LabelDTO> getLabelById(Long id) {
        // check if label exists
        Optional<Label> optLabel = labelRepository.findById(id);

        if (optLabel.isPresent()) {
            LabelDTO label = new LabelDTO().label(optLabel.get().getValue());
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
    public ResponseEntity<List<LabelDTO>> getLabels() {
        return ok(new LabelTransformer().toDtoList(labelRepository.findAll()));
    }
}
