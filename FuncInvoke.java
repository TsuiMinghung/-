import java.util.ArrayList;
import java.util.List;

public class FuncInvoke extends VarFactor {
    private final Lexer lexer;
    private String name;
    private List<Expr> paras;
    private Expr expr;

    public FuncInvoke(Lexer lexer) {
        this.lexer = lexer;
        parse();

    }

    //used for clone
    public FuncInvoke(FuncInvoke other) {
        this.lexer = other.lexer;
        this.name = other.name;
        paras = new ArrayList<>();
        for (Expr f : other.paras) {
            paras.add(f.clone());
        }
        this.expr = other.expr.clone();
    }

    @Override
    public void parse() {
        name = lexer.funcName();
        lexer.leftParent();
        paras = new ArrayList<>();
        paras.add(new Expr(lexer));
        FunctionTemplate ft = FunctionTemplate.getFT(name);
        for (int i = 1; i < ft.getParaNum(); ++i) {
            lexer.comma();
            paras.add(new Expr(lexer));
        }
        lexer.rightParent();
        expr = new Expr(new Lexer("(" + ft.paraReplace(paras) + ")"));
    }

    @Override
    public FuncInvoke clone() {
        return new FuncInvoke(this);
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof FuncInvoke) {
            FuncInvoke obj = (FuncInvoke) o;
            if (obj.name.equals(name)) {
                for (int i = 0; i < paras.size();++i) {
                    if (!paras.get(i).equals(obj.paras.get(i))) {
                        return false;
                    }
                }
                return true;
            }
            return false;
        }
        return false;
    }

    @Override
    public JointList collapse() {
        return expr.collapse();
    }

    @Override
    public String simplify() {
        return expr.simplify();
    }

    @Override
    public boolean isZero() {
        return expr.isZero();
    }

    @Override
    public boolean isOne() {
        return expr.isOne();
    }
}
