import java.math.BigInteger;

public class Constant extends Factor {

    private static final Constant ZERO = new Constant(BigInteger.ZERO);
    private static final Constant ONE = new Constant(BigInteger.ONE);

    public static Constant zero() {
        return ZERO.clone();
    }

    public static Constant one() {
        return ONE.clone();
    }

    private BigInteger value;
    private final Lexer lexer;

    private Constant(BigInteger value) {
        this.value = value;
        lexer = null;
    }

    public Constant(Lexer lexer) {
        this.lexer = lexer;
        parse();
    }

    //used for clone
    public Constant(Constant other) {
        lexer = other.lexer;
        value = new BigInteger(other.value.toString());
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Constant) {
            Constant other = (Constant) o;
            return other.value.equals(this.value);
        }
        return false;
    }

    @Override
    protected Constant clone() {
        return new Constant(this);
    }

    @Override
    public void parse() {
        value = new BigInteger(lexer.signedNumber());
    }

    @Override
    public String simplify() {
        return value.toString();
    }

    public void multiply(Constant other) {
        value = value.multiply(other.value);
    }

    public boolean isZero() {
        return value.equals(BigInteger.ZERO);
    }

    public boolean isOne() {
        return value.equals(BigInteger.ONE);
    }

    public void negate() {
        value = value.negate();
    }

    public void add(Constant other) {
        this.value = this.value.add(other.value);
    }

    public boolean isNegative() {
        return value.signum() < 0;
    }

    @Override
    public JointList collapse() {
        JointList result = new JointList();
        result.addJoint(new Joint(this));
        return result;
    }
}
