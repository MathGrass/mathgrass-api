package de.tudresden.inf.st.mathgrassserver.database.entity;

import javax.persistence.*;
@Table
@Entity(name = "feedback")
public class FeedbackEntity {

        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        private Long id;

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
