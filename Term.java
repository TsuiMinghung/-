import java.util.ArrayList;
import java.util.List;

public class Term implements Simplify,Parsable,Collapse {
    private List<Factor> factors;
    private Joint joint;
    private JointList jl = null;
    private final Lexer lexer;

    public Term(Lexer lexer) {
        this.lexer = lexer;
        factors = new ArrayList<>();
        joint = new Joint();
        parse();
        collapse();
    }

    //used for clone
    public Term(Term other) {
        this.lexer = other.lexer;
        joint = other.joint.clone();
        factors = new ArrayList<>();
        for (Factor f : other.factors) {
            factors.add(f.clone());
        }
        jl = other.jl.clone();
    }

    public Term(boolean isNegative, Lexer lexer) {
        this.lexer = lexer;
        factors = new ArrayList<>();
        joint = new Joint();
        if (isNegative) {
            joint.negate();
        }
        parse();
        collapse();
    }

    @Override
    public void parse() {
        if (lexer.isSign()) {
            if (lexer.sign().equals("-")) {
                joint.negate();
            }
        }
        addFactor();
        while (lexer.isStar()) {
            lexer.star();
            addFactor();
        }
    }

    //return true if factor is zero and set this to zero
    public void addFactor() {
        Factor f = Factor.getInstance(lexer);
        if (f instanceof Constant) {
            joint.multiplyConstant((Constant) f);
        } else if (f instanceof TriFunction) {
            joint.multiplyTri((TriFunction) f);
        } else if (f instanceof Power) {
            joint.multiplyPow((Power) f);
        } else {
            factors.add(f);
        }
    }

    public Term clone() {
        return new Term(this);
    }

    public boolean isZero() {
        return jl.isZero();
    }

    public boolean isNegative() {
        return jl.get(0).isNegative();
    }

    @Override
    public String simplify() {
        StringBuilder result = new StringBuilder(jl.get(0).simplify());
        for (int i = 1;i < jl.size();++i) {
            result.append(jl.get(i).isNegative() ? "" : "+");
            result.append(jl.get(i).simplify());
        }
        return result.toString();
    }

    @Override
    public JointList collapse() {
        if (jl != null) {
            return jl;
        }
        JointList result = new JointList();
        result.addJoint(joint.clone());
        for (Factor f : factors) {
            result = JointList.multiply(result,f.collapse());
        }
        jl = result;
        return result;
    }

    @Override
    public String toString() {
        return simplify();
    }

    public boolean isOne() {
        return jl.isOne();
    }

    public boolean equals(Term other) {
        return collapse().equals(other.collapse());
    }

    public JointList derive(String var) {
        return jl.derive(var);
    }
}
