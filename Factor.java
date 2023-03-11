
public abstract class Factor implements Simplify, Parsable, Collapse {
    public abstract boolean equals(Object o);

    protected abstract Factor clone();

    public abstract String simplify();

    public abstract void parse();

    public abstract boolean isZero();

    public abstract boolean isOne();

    public static Factor getInstance(Lexer lexer) {
        if (lexer.current().equals("(")) {
            return new ExprFactor(lexer);
        } else if (Character.isDigit(lexer.current().charAt(0)) || "+-".contains(lexer.current())) {
            return new Constant(lexer);
        } else if (lexer.current().equals("d")) {
            return new DiffFactor(lexer);
        } else {
            return VarFactor.getInstance(lexer);
        }
    }

    @Override
    public String toString() {
        return simplify();
    }
}
