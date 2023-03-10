import java.util.ArrayList;
import java.util.List;

import static java.lang.System.exit;

public class Lexer {
    //T2::Lexer finish the part of function string replace

    private final List<String> tokens;
    private final String input;
    private int index;
    private int stringIndex;

    public Lexer(String s) {
        input = s.replaceAll("\\s+", "");
        tokens = new ArrayList<>();
        index = 0;
        stringIndex = 0;
        lex();
    }

    private void lex() {
        char c;
        while (stringIndex < input.length()) {
            c = input.charAt(stringIndex);
            if ("xyz()+-fgh,".indexOf(c) != -1) {
                tokens.add(Character.toString(c));
                ++stringIndex;
            } else if (c == '*') {
                if (input.charAt(stringIndex + 1) == '*') {
                    stringIndex += 2;
                    tokens.add("**");
                } else {
                    ++stringIndex;
                    tokens.add("*");
                }
            } else if ("sc".indexOf(c) != -1) {
                fetchTri();
            } else {
                fetchNumber();
            }
        }
    }

    private void fetchNumber() {
        StringBuilder sb = new StringBuilder();
        boolean flag = true;
        while (stringIndex < input.length() && Character.isDigit(input.charAt(stringIndex))) {
            char tmp = input.charAt(stringIndex++);
            if (tmp == '0' && flag) {
                continue;
            } else {
                flag = false;
            }
            sb.append(tmp);
        }
        if (sb.length() == 0) {
            sb.append("0");
        }
        tokens.add(sb.toString());
    }

    //current at 's' or 'c'
    //jump over sin or cos
    private void fetchTri() {
        tokens.add(input.substring(stringIndex,stringIndex + 3));
        stringIndex += 3;
    }

    public String current() {
        return index < tokens.size() ? tokens.get(index) : null;
    }

    public String peek(int step) {
        return (index + step) < tokens.size() ? tokens.get(index + step) : null;
    }

    public String next() {
        return index < tokens.size() ? tokens.get(index++) : null;
    }

    private String trySign() {
        if ("+-".contains(current())) {
            return next();
        }
        return "";
    }

    private String number() {
        assert (Character.isDigit(current().charAt(0)));
        return next();
    }

    public String signedNumber() {
        return trySign() + number();
    }
    
    public void leftParent() {
        assert (this.current().equals("("));
        this.next();
    }
    
    public void rightParent() {
        assert (this.current().equals(")"));
        this.next();
    }

    public void tryPlus() {
        if (current() != null && current().equals("+")) {
            next();
        }
    }
    
    public int tryExp() {
        if (current() != null && current().equals("**")) {
            next();
            tryPlus();
            return Integer.parseInt(next());
        }
        return 1;
    }

    private boolean isVar() {
        return "xyz".contains(current());
    }

    public String variable() {
        assert isVar();
        return next();
    }

    public void sin() {
        assert (current().equals("sin"));
        next();
    }

    public void cos() {
        assert (current().equals("cos"));
        next();
    }

    public String sign() {
        assert ("+-".contains(current()));
        return next();
    }

    public boolean isSign() {
        return current() != null && "+-".contains(current());
    }

    public boolean isStar() {
        return current() != null && "*".equals(current());
    }

    public void star() {
        assert (isStar());
        next();
    }

    public boolean isFuncName() {
        return current() != null && "fgh".contains(current());
    }

    public String funcName() {
        assert isFuncName();
        return next();
    }

    public void comma() {
        if (current().equals(",")) {
            next();
        } else {
            System.err.println("comma error");
            exit(1);
        }
    }
}
