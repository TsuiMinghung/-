import java.io.File;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.Scanner;

public class Main {
    private static void invokeDebug() {
        try {
            File input = new File("/home/alex/study/OO/U1/input.txt");
            Scanner scanner = new Scanner(input);
            int funcNum = Integer.parseInt(scanner.nextLine().replaceAll("\\s+",""));
            for (int i = 0; i < funcNum; ++i) {
                FunctionTemplate ft = new FunctionTemplate(scanner.nextLine());
                FunctionTemplate.addFT(ft);
            }
            Expr expr = new Expr(new Lexer(scanner.nextLine()));
            PrintWriter writer = new PrintWriter("/home/alex/study/OO/U1/output.txt");
            writer.println(expr.simplify());
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void run() {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        try {
            readInFuncTemplate(br);
            Expr expr = new Expr(new Lexer(br.readLine()));
            System.out.println(expr.simplify());
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    private static void readInFuncTemplate(BufferedReader br) {
        try {
            int funcNum = Integer.parseInt(br.readLine().replaceAll("\\s+",""));
            for (int i = 0; i < funcNum; ++i) {
                FunctionTemplate ft = new FunctionTemplate(br.readLine());
                FunctionTemplate.addFT(ft);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        if (args.length < 1) {
            run();
        } else if (args[0].equals("debug")) {
            invokeDebug();
        }
    }
}