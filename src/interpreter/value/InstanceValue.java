package interpreter.value;

import interpreter.util.Instance;

public class InstanceValue extends Value<Instance> {

    private Instance value;

    public InstanceValue(Instance value) {
        this.value = value;
    }

    @Override
    public Instance value() {
        return value;
    }

}
