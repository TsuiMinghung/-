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

    public Cos(Expr factor,int exp) {
        this.lexer = null;
        this.factor = factor;
        this.exp = exp;
    }

    public Cos(Expr factor) {
        this.lexer = null;
        this.factor = factor;
        this.exp = 1;
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

    public Sin toSin() {
        return new Sin(factor.clone());
    }

    public Cos lessOne() {
        return new Cos(this.factor.clone(),exp - 1);
    }

    public Cos basicCos() {
        return new Cos(this.factor.clone(),1);
    }

    public JointList derive(String var) {
        JointList result = new JointList();
        if (exp == 0) {
            result.addJoint(new Joint(Constant.zero()));
            return result;
        } else if (exp == 1) {
            Joint tmp = new Joint(toSin());
            tmp.multiplyConstant(Constant.minusOne());
            result.addJoint(tmp);
            result = JointList.multiply(result,factor.derive(var));
            return result;
        } else {
            Joint tmp = new Joint(basicCos().toSin());
            tmp.multiplyConstant(Constant.minusOne());
            result.addJoint(tmp);
            result = JointList.multiply(result,factor.derive(var));
            result = JointList.multiply(result,lessOne().derive(var));
            return result;
        }
    }
}
