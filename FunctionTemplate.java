import java.util.List;
import java.util.HashMap;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Map;

public class FunctionTemplate {

    private static final Map<String, FunctionTemplate> NAME2FT = new HashMap<>();

    private String name;
    private int paraNum;
    private final String input;
    private String body;
    private final List<String> formatParas;

    public static FunctionTemplate getFT(String name) {
        return NAME2FT.get(name);
    }

    public static void addFT(FunctionTemplate ft) {
        NAME2FT.put(ft.getName(),ft);
    }

    public FunctionTemplate(String s) {
        input = s.replaceAll("\\s+", "");
        formatParas = new ArrayList<>();
        parse();
    }

    private void parse() {
        assert (input.indexOf('=') != -1);
        assert ("fgh".contains(input.substring(0,1)));
        name = input.substring(0,1);
        paraNum = input.substring(1,input.indexOf('=')).split(",").length;
        formatParas.addAll(Arrays.asList(input.substring(2, input.indexOf(')')).split(",")));
        body = new Expr(new Lexer(input.substring(input.indexOf('=') + 1))).simplify();
    }

    public int getParaNum() {
        return paraNum;
    }

    public String getName() {
        return name;
    }

    public String paraReplace(List<Expr> paras) {
        assert (paras.size() == paraNum);
        HashMap<String,String> paraMap = new HashMap<>();
        for (int i = 0;i < paras.size();++i) {
            paraMap.put(formatParas.get(i),"(" + paras.get(i).simplify() + ")");
        }
        String result = String.copyValueOf(body.toCharArray());
        int i = 0;
        while (i < result.length()) {
            String current = String.valueOf(result.charAt(i));
            if ("xyz".contains(current)) {
                result = result.substring(0,i) + paraMap.get(current) +
                        (i + 1 < result.length() ? result.substring(i + 1) : "");
                i += paraMap.get(current).length();
            } else {
                ++i;
            }
        }
        return result;
    }
}
