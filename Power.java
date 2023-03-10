
public class Power extends VarFactor {
    private String base;
    private int exp;
    private final Lexer lexer;

    public Power(Lexer lexer) {
        this.lexer = lexer;
        exp = 1;
        parse();
    }

    public Power(Power other) {
        this.lexer = other.lexer;
        exp = other.exp;
        base = String.copyValueOf(other.base.toCharArray());
    }

    public String getBase() {
        return base;
    }

    public void multiply(Power other) {
        assert (base.equals(other.base));
        exp += other.exp;
    }

    @Override
    public void parse() {
        base = lexer.variable();
        exp = lexer.tryExp();
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Power) {
            Power other = (Power) o;
            return other.base.equals(base) && other.exp == exp;
        }
        return false;
    }

    public boolean baseEquals(String otherBase) {
        return base.equals(otherBase);
    }

    public Power clone() {
        return new Power(this);
    }

    public boolean isOne() {
        return exp == 0;
    }

    @Override
    public JointList collapse() {
        JointList result = new JointList();
        result.addJoint(new Joint(this));
        return result;
    }

    @Override
    public String simplify() {
        if (exp == 0) {
            return "1";
        }
        /*else if (exp == 2) {
            return base + "*" + base;
        }*/
        return base + (exp == 1 ? "" : "**" + exp);
    }

    @Override
    public boolean isZero() {
        return false;
    }
}
