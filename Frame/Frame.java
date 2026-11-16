package Frame;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import Temp.label;

public abstract class Frame {
    private label name;
    private List<Access> formalAccesses;

    protected Frame(label name, List<Access> formals) {
        if (name == null) {
            throw new IllegalArgumentException("Frame name cannot be null");
        }
        if (formals == null) {
            throw new IllegalArgumentException("formals cannot be null");
        }
        this.name = name;
        this.formalAccesses = new ArrayList<>(formals);
    }

    public label name() {
        return name;
    }

    public List<Access> formals() {
        return Collections.unmodifiableList(formalAccesses);
    }

    public abstract Access allocLocal(boolean escape);
}

