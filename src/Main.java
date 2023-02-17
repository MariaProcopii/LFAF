import automaton.*;
public class Main {
    public static void main(String[] args) {

        char[] vn = {'S', 'B', 'C'}; //non terminal variables
        char[] vt = {'a', 'b', 'c'};  //terminal variables
        char[] prodKey = {'S', 'B', 'C', 'C', 'C', 'B'};  //left side of production
        String[] prodVal = {"aB", "aC", "bB", "c", "aS", "bB"};  //right side of production
        char startSymbol = 'S';

        Grammar grammar = new Grammar(vn, vt, prodKey, prodVal, startSymbol);


        System.out.println(grammar.generateWords(5)); // set of generated strings

        FiniteAutomaton finiteAutomaton = grammar.toFiniteAutomaton();  // converting an object of type Grammar
                                                                       // to one of type Finite Automaton

//        finiteAutomaton.printTransitions(); // visualize formed transitions set from provided grammar
        finiteAutomaton.wordIsValid("aac");  // check the string (function also return a boolean)
        finiteAutomaton.wordIsValid("abababaaaacac");
    }
}
