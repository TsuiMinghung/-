public class Cos extends TriFunction {
    private final Lexer lexer;
    private int exp;
    private Expr factor;

    public Cos(Lexer lexer) {
        this.lexer = lexer;
        parse();
    }

    //used for clone
    public Cos(Cos other) {
        this.lexer = other.lexer;
        exp = other.exp;
        factor = other.factor.clone();
    }

    public TriFunction clone() {
        return new Cos(this);
    }

    @Override
    public void parse() {
        lexer.cos();
        lexer.leftParent();
        factor = new Expr(lexer);
        lexer.rightParent();
        exp = lexer.tryExp();
    }

    @Override
    public boolean isZero() {
        return false;
    }

    @Override
    public boolean isOne() {
        if (exp == 0) {
            return true;
        }
        return factor.isZero();
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Cos) {
            Cos other = (Cos) o;
            return exp == other.exp && factor.equals(other.factor);
        }
        return false;
    }

    @Override
    public boolean bodyEquals(TriFunction o) {
        if (o instanceof Cos) {
            Cos other = (Cos) o;
            return factor.equals(other.factor);
        }
        return false;
    }

    @Override
    public void multiply(TriFunction triFunction) {
        assert (bodyEquals(triFunction));
        exp += ((Cos) triFunction).exp;
    }

    @Override
    public String simplify() {
        if (exp == 0) {
            return "1";
        }
        String tmp;
        if (factor.onlyNumber() || factor.onlyPower()) {
            tmp = factor.simplify();
        } else {
            tmp = "(" + factor.simplify() + ")";
        }
        return "cos(" + tmp + ")" + (exp == 1 ? "" : "**" + exp);
    }
}
