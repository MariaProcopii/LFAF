import automaton.FiniteAutomaton;
import grammar.Grammar;

import java.util.HashSet;
import java.util.List;

public class Lab2 {
    public static void main(String[] args) {
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


        Grammar grammar1 = finiteAutomaton1.toGrammar(); //converting FiniteAutomaton to Grammar

        System.out.println("Production list of converted grammar: " + grammar1.getProductions());  //production list

        grammar1.grammarType(); //checking the grammar type

        System.out.println("\n-----Before conversion-----");
        finiteAutomaton1.isNFA(); // This FA should be non-deterministic

        finiteAutomaton1.convertToDFA(); //Convert NFA to DFA

//        System.out.println(finiteAutomaton1.getTransitions()); //visualize new transitions. Not required

        System.out.println("\n-----After conversion-----");
        finiteAutomaton1.isNFA();  // Now this should be deterministic
    }
}
