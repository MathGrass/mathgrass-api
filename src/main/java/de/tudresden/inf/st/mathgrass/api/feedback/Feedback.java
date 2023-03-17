package de.tudresden.inf.st.mathgrass.api.feedback;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * This class represents feedback, which can be given to an evaluated task.
 */

@Entity
public class Feedback {
        /**
         * ID of feedback.
         */
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        private Long id;

        /**
         * Content of feedback.
         */

        private String content;

        public Long getId() {
                return id;
        }

        public void setId(Long id) {
                this.id = id;
        }

        public String getContent() {
                return content;
        }

        public void setContent(String content) {
                this.content = content;
        }
}
