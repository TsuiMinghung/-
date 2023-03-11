public class DiffFactor extends Factor {
    private final Lexer lexer;
    private Expr expr;
    private String var;
    private JointList jl;

    public DiffFactor(Lexer lexer) {
        this.lexer = lexer;
        parse();
        collapse();
    }


    //used for clone
    public DiffFactor(DiffFactor other) {
        this.lexer = other.lexer;
        expr = other.expr.clone();
        var = String.copyValueOf(other.var.toCharArray());
        jl = other.jl.clone();
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof DiffFactor) {
            DiffFactor obj = (DiffFactor) o;
            return var.equals(obj.var) && expr.equals(obj.expr);
        }
        return false;
    }

    @Override
    protected Factor clone() {
        return new DiffFactor(this);
    }

    @Override
    public String simplify() {
        if (jl == null) {
            collapse();
        }
        return jl.simplify();
    }

    @Override
    public void parse() {
        lexer.diff();
        var = lexer.variable();
        lexer.leftParent();
        expr = new Expr(lexer);
        lexer.rightParent();
    }

    @Override
    public boolean isZero() {
        if (jl == null) {
            collapse();
        }
        return jl.isZero();
    }

    @Override
    public boolean isOne() {
        if (jl == null) {
            collapse();
        }
        return jl.isOne();
    }

    @Override
    public String toString() {
        return simplify();
    }

    @Override
    public JointList collapse() {
        return expr.collapse().derive(var);
    }
}
