import grammar.*;
import grammarConversion.*;

import java.util.*;

public class Main {
    public static void main(String[] args) {

        String[] vn = {"S","A", "B", "C", "E"}; //non terminal variables
        String[] vt = {"a", "b"};  //terminal variables
        String[] prodKey = {"S", "S", "A", "A", "A", "B", "B", "B", "C", "C", "E"};  //left side of production
        String[] prodVal = {"bAC", "B", "a", "aS", "bCaCb", "AC", "bS", "aAa", "", "AB", "BA"};  //right side of production
        String startSymbol = "S";

        Grammar grammar = new Grammar(vn, vt, prodKey, prodVal, startSymbol);
        Grammar grammar1 = ToCNF.getCopyModGrammar(grammar); //works with the copy of provided grammar
        System.out.println(grammar.getProductions());
        System.out.println(grammar1.getProductions());
//        ToCNF.modifyGrammar(grammar);
        System.out.println(grammar.getProductions());
    }
}