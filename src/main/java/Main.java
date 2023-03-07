import automaton.*;
import grammar.*;

import java.sql.SQLOutput;
import java.util.*;
import java.util.regex.*;

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


        Grammar grammar1 = finiteAutomaton1.toGrammar(); //converting FiniteAutomaton to Grammar

        grammar1.grammarType(); //checking the grammar type

        finiteAutomaton1.isNFA(); // This FA should be non-deterministic

        finiteAutomaton1.convertToDFA(); //Convert NFA to DFA

//        System.out.println(finiteAutomaton1.getTransitions()); //visualize new transitions. Not required

        finiteAutomaton1.isNFA();  // Now this should be deterministic

    }
}