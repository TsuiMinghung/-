public abstract class VarFactor extends Factor {
    public static VarFactor getInstance(Lexer lexer) {
        if (lexer.current().equals("sin")) {
            return new Sin(lexer);
        } else if (lexer.current().equals("cos")) {
            return new Cos(lexer);
        } else if ("xyz".contains(lexer.current())) {
            return new Power(lexer);
        } else {
            return new FuncInvoke(lexer);
        }
    }
}
