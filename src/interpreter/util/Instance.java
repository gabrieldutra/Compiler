package interpreter.util;

public class Instance extends Memory implements Cloneable {

    public Instance() {
    }
    
    public Instance dup() {
        try {
            return (Instance) this.clone();
        } catch (CloneNotSupportedException ex) {
        }
        return null;
    }

}
