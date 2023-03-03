package de.tudresden.inf.st.mathgrass.api.database.entity;

import javax.persistence.*;

/**
 * This class represents feedback, which can be given to an evaluated task.
 */
@Table(name = "feedbacks")
@Entity
public class FeedbackEntity {
        /**
         * ID of feedback.
         */
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        private Long id;

        /**
         * Content of feedback.
         */
        @Column
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
