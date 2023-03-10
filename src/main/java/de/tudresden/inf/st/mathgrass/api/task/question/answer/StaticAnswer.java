package de.tudresden.inf.st.mathgrass.api.task.question.answer;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import java.util.List;

@Entity
public class StaticAnswer extends Answer {
    private String answer;

    @ElementCollection
    private List<String> predefinedAnswers;

    @ElementCollection
    public List<String> getPredefinedAnswers() {
        return predefinedAnswers;
    }

    public void setPredefinedAnswers(List<String> predefinedAnswers) {
        this.predefinedAnswers = predefinedAnswers;
    }

    public String getAnswer() {
        return this.answer;
    }

    public void setAnswer(String answer){
        this.answer = answer;
    }
}
