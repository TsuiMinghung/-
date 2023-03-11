
public class Sin extends TriFunction {
    private final Lexer lexer;
    private int exp;
    private Expr factor;

    public Sin(Lexer lexer) {
        this.lexer = lexer;
        parse();
    }

    //used for clone
    public Sin(Sin other) {
        this.lexer = other.lexer;
        this.exp = other.exp;
        this.factor = other.factor.clone();
    }

    public Sin(Expr factor,int exp) {
        this.lexer = null;
        this.factor = factor;
        this.exp = exp;
    }

    public Sin(Expr factor) {
        this.lexer = null;
        exp = 1;
        this.factor = factor;
    }

    @Override
    public void parse() {
        lexer.sin();
        lexer.leftParent();
        factor = new Expr(lexer);
        lexer.rightParent();
        exp = lexer.tryExp();
    }

    @Override
    public boolean isZero() {
        return factor.isZero();
    }

    @Override
    public boolean isOne() {
        return exp == 0;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Sin) {
            Sin obj = (Sin) o;
            return obj.exp == exp && obj.factor.equals(factor);
        }
        return false;
    }

    public TriFunction clone() {
        return new Sin(this);
    }

    @Override
    public boolean bodyEquals(TriFunction o) {
        if (o instanceof Sin) {
            Sin obj = (Sin) o;
            return obj.factor.equals(factor);
        }
        return false;
    }

    @Override
    public void multiply(TriFunction triFunction) {
        assert (bodyEquals(triFunction));
        exp += ((Sin) triFunction).exp;
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
        return "sin(" + tmp + ")" + (exp == 1 ? "" : "**" + exp);
    }

    public Cos toCos() {
        return new Cos(factor.clone());
    }

    public Sin lessOne() {
        return new Sin(this.factor.clone(),exp - 1);
    }

    public Sin basicSin() {
        return new Sin(this.factor.clone(),1);
    }

    public JointList derive(String var) {
        JointList result = new JointList();
        if (exp == 0) {
            result.addJoint(new Joint(Constant.zero()));
        } else if (exp == 1) {
            result.addJoint(new Joint(toCos()));
            result = JointList.multiply(result,factor.derive(var));
        } else {
            Joint lhs = new Joint(basicSin());
            Joint rhs = new Joint(lessOne());
            result.addJoint(lhs);
            result = JointList.multiply(result,rhs.derive(var));
            result.addAll(JointList.multiply(new JointList(rhs),lhs.derive(var)));
        }
        return result;
    }
}
