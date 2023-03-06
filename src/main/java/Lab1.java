import automaton.FiniteAutomaton;
import grammar.Grammar;

public class Lab1 {
    public static void main(String[] args) {

        String[] vn = {"S", "B", "C"}; //non terminal variables
        String[] vt = {"a", "b", "c"};  //terminal variables
        String[] prodKey = {"S", "B", "C", "C", "C", "B"};  //left side of production
        String[] prodVal = {"aB", "aC", "bB", "c", "aS", "bB"};  //right side of production
        String startSymbol = "S";

        Grammar grammar = new Grammar(vn, vt, prodKey, prodVal, startSymbol);


        System.out.println(grammar.generateWords(5)); // set of generated strings

        FiniteAutomaton finiteAutomaton = grammar.toFiniteAutomaton("F");  // converting an object of type Grammar
                                                                                   // to one of type FiniteAutomaton

        finiteAutomaton.printTransitions(); // visualize formed transitions set from provided grammar
        finiteAutomaton.wordIsValid("abac");  // check the string (function also return a boolean)
        finiteAutomaton.wordIsValid("ababaaaa");

    }
}
