import automaton.*;
import grammar.*;

import java.util.*;

public class Main {
    public static void main(String[] args) {

        //look forward in Lab1 class to run the program for the first lab

        //program for lab2
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

        grammar1.grammarType();

        finiteAutomaton1.isNFA(); // New FA - should be non-deterministic

    }
}