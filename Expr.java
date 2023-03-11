import java.util.ArrayList;
import java.util.List;

public class Expr implements Simplify,Parsable,Collapse {

    private final List<Term> terms;
    private final Lexer lexer;
    private JointList jl = null;

    public Expr(Lexer lexer) {
        this.lexer = lexer;
        terms = new ArrayList<>();
        parse();
    }

    //deep copy
    public Expr(Expr other) {
        this.lexer = other.lexer;
        terms = new ArrayList<>();
        for (Term term : other.terms) {
            terms.add(term.clone());
        }
    }

    public void parse() {
        addTerm();
        while (lexer.isSign()) {
            addTerm();
        }
    }

    private void addTerm() {
        boolean isNegative = false;
        if (lexer.isSign()) {
            isNegative = "-".equals(lexer.sign());
        }
        Term term = new Term(isNegative,lexer);
        if (term.isZero()) {
            return;
        } else {
            terms.add(term);
        }
    }

    public Expr clone() {
        return new Expr(this);
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Expr) {
            Expr obj = (Expr) o;
            if (obj.terms.size() != terms.size()) {
                return false;
            }
            for (Term t1 : obj.terms) {
                if (excludeTerm(t1)) {
                    return false;
                }
            }
            for (Term t2 : terms) {
                if (obj.excludeTerm(t2)) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    private boolean excludeTerm(Term t) {
        for (Term term : terms) {
            //System.err.println("before Expr excludeTerm");
            if (t.equals(term)) {
                return false;
            }
        }
        return true;
    }

    public boolean isZero() {
        for (Term term : terms) {
            if (!term.isZero()) {
                return false;
            }
        }
        return true;
    }

    public boolean isOne() {
        boolean flag = false;
        for (Term t : terms) {
            if (t.isOne()) {
                if (flag) {
                    return false;
                } else {
                    flag = true;
                }
            } else if (t.isZero()) {
                continue;
            } else {
                return false;
            }
        }
        return true;
    }

    @Override
    public String simplify() {
        return collapse().simplify();
    }

    @Override
    public JointList collapse() {
        if (jl == null) {
            JointList result = new JointList();
            for (Term term : terms) {
                result.addAll(term.collapse());
            }
            jl = result;
            return result;
        } else {
            return jl;
        }
    }

    @Override
    public String toString() {
        return simplify();
    }

    public boolean onlyNumber() {
        return collapse().onlyNumber();
    }

    public boolean onlyPower() {
        return collapse().onlyPower();
    }

    public JointList derive(String var) {
        if (jl == null) {
            collapse();
        }
        return jl.derive(var);
    }
}
