public class ExprFactor extends Factor {
    private Expr expr;
    private int exp;
    private final Lexer lexer;

    public ExprFactor(Lexer lexer) {
        this.lexer = lexer;
        exp = 1;
        parse();
    }

    //used for deep copy
    public ExprFactor(ExprFactor other) {
        this.lexer = other.lexer;
        exp = other.exp;
        expr = other.expr.clone();
    }

    public ExprFactor clone() {
        return new ExprFactor(this);
    }

    @Override
    public void parse() {
        lexer.leftParent();
        expr = new Expr(lexer);
        lexer.rightParent();
        exp = lexer.tryExp();
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof ExprFactor) {
            ExprFactor obj = (ExprFactor) o;
            return expr.equals(obj.expr) && exp == obj.exp;
        }
        return false;
    }

    @Override
    public JointList collapse() {
        if (exp == 0) {
            JointList result = new JointList();
            result.addJoint(new Joint(Constant.one()));
            return result;
        }
        JointList result = expr.collapse().clone();
        JointList tmp = expr.collapse().clone();
        for (int i = 2;i <= exp;++i) {
            result = JointList.multiply(result,tmp);
        }
        return result;
    }

    @Override
    public String simplify() {
        return collapse().simplify();
    }

    @Override
    public boolean isZero() {
        return expr.isZero();
    }

    @Override
    public boolean isOne() {
        if (exp == 0) {
            return true;
        }
        return expr.isOne();
    }
}
