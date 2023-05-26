package de.tudresden.inf.st.mathgrass.api.admin.entity;

import javax.persistence.Embeddable;

@Embeddable
public class Hint {
    private String hint;
    private int hintsOrder;

    public Hint() {
    }

    @Override
    public String toString() {
        return "Hint{" +
                "hint='" + hint + '\'' +
                ", hintsOrder=" + hintsOrder +
                '}';
    }

    public String getHint() {
        return hint;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }

    public int getHintsOrder() {
        return hintsOrder;
    }

    public void setHintsOrder(int hintsOrder) {
        this.hintsOrder = hintsOrder;
    }

    public Hint(String hint, int hintsOrder) {
        this.hint = hint;
        this.hintsOrder = hintsOrder;
    }
}
