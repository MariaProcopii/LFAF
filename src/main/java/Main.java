import automaton.*;
import grammar.*;

import java.util.*;

public class Main {
    public static void main(String[] args) {

        String[] vn = {"S", "B", "C"}; //non terminal variables
        String[] vt = {"a", "b", "c"};  //terminal variables
        String[] prodKey = {"S", "B", "C", "C", "C", "B"};  //left side of production
        String[] prodVal = {"aB", "aC", "bB", "c", "aS", "bB"};  //right side of production
        String startSymbol = "S";

        Grammar grammar = new Grammar(vn, vt, prodKey, prodVal, startSymbol);


//        System.out.println(grammar.generateWords(5)); // set of generated strings

        FiniteAutomaton finiteAutomaton = grammar.toFiniteAutomaton("F");  // converting an object of type grammar.Grammar
                                                                       // to one of type Finite Automaton

//        finiteAutomaton.printTransitions(); // visualize formed transitions set from provided grammar
//        finiteAutomaton.wordIsValid("abac");  // check the string (function also return a boolean)
//        finiteAutomaton.wordIsValid("abababaaaa");

// -------------------------Lab2---------------------
        HashSet<String> q = new HashSet<>(List.of("q0", "q1", "q2"));
        HashSet<String> alphabet = new HashSet<>(List.of("a", "b"));
        String finalState = "q2";
        String initialState = "q0";

        FiniteAutomaton finiteAutomaton1 = new FiniteAutomaton(q, alphabet, initialState, finalState);
        finiteAutomaton1.setTransition("q0", "a", "q0");
        finiteAutomaton1.setTransition("q0", "b", "q0");
        finiteAutomaton1.setTransition("q0", "a", "q1");
        finiteAutomaton1.setTransition("q1", "b", "q2");
        finiteAutomaton1.setTransition("q1", "a", "q0");
        finiteAutomaton1.setTransition("q2", "b", "q2");


        Grammar grammar1 = finiteAutomaton1.toGrammar();
        grammar.grammarType();
        grammar1.grammarType();

//        System.out.println(finiteAutomaton1.getTransitions());
//        System.out.println(finiteAutomaton1.getPossibleStates());
//        System.out.println(finiteAutomaton1.getAlphabet());

        finiteAutomaton.isNFA(); //FA from first lab - should be deterministic
        finiteAutomaton1.isNFA(); // New FA - should be non-deterministic
    }
}
