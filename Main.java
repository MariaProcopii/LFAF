import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;

public class Main {
    public static void main(String[] args) {
        Grammar grammar = new Grammar();

        char[] vn = {'S', 'B', 'C'};
        char[] vt = {'a', 'b', 'c'};
        char[] prodKey = {'S', 'B', 'C', 'C', 'C', 'B'};
        String[] prodVal = {"aB", "aC", "bB", "c", "aS", "bB"};

        for(int i = 0; i < prodKey.length; i++){
            grammar.setProductions(prodKey[i], prodVal[i]);
        }
        for(int i = 0; i < vn.length; i++){
            grammar.setNonTerminalVariables(vn[i]);
            grammar.setTerminalVariables(vt[i]);
        }
        grammar.setStartSymbol('S');


        System.out.println(grammar.generateWords() );

    }
}
