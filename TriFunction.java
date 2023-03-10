public abstract class TriFunction extends VarFactor {

    public abstract boolean isZero();

    public abstract boolean isOne();

    public abstract TriFunction clone();

    public abstract boolean bodyEquals(TriFunction triFunction);

    public abstract void multiply(TriFunction triFunction);

    @Override
    public JointList collapse() {
        JointList result = new JointList();
        result.addJoint(new Joint(this));
        return result;
    }
}
