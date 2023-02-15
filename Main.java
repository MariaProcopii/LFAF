import automaton.*;
public class Main {
    public static void main(String[] args) {
        Grammar grammar = new Grammar();
        FiniteAutomaton finiteAutomaton;

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
        grammar.setCount(5);

        System.out.println(grammar.generateWords());
        finiteAutomaton = grammar.toFiniteAutomaton();
//        finiteAutomaton.printTransitions();
        finiteAutomaton.wordIsValid("abababaaaa");
    }
}
