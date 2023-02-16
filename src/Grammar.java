import java.util.*;
import automaton.*;
public class Grammar {
    private final HashSet<Character> nonTerminalVariables = new HashSet<>();
    private final HashSet<Character> terminalVariables = new HashSet<>();
    private final HashMap<Character, ArrayList<String>> productions = new HashMap<>();
    private final char startSymbol;

    public Grammar(char[] vn, char[] vt, char[] prodKey,
                   String[] prodVal, char startSymbol){

        genNonTerminalVariables(vn);
        genTerminalVariables(vt);
        genProductions(prodKey, prodVal);
        this.startSymbol = startSymbol;
    }

    public void genNonTerminalVariables(char[] vn){
        for (char c : vn) {
            nonTerminalVariables.add(c);
        }
    }
    public void genTerminalVariables(char[] vt){
        for (char c : vt) {
            terminalVariables.add(c);
        }
    }
    public void genProductions(char[] prodKey, String[] prodVal) {
        for(int i = 0; i < prodKey.length; i++){

            if(!productions.containsKey(prodKey[i])){
                productions.put(prodKey[i], new ArrayList<>());
            }
            productions.get(prodKey[i]).add(prodVal[i]);
            }
    }

    public ArrayList<String> generateWords(int wordsAmount){
        ArrayList<String> result = new ArrayList<>();
        Random random = new Random();

        System.out.println("\nProcess of strings formation:");
        while(result.size()  < wordsAmount){
            Stack<Character> stack = new Stack<>();
            StringBuilder stringBuilder = new StringBuilder();

            stack.add(startSymbol);
            System.out.print("\n" + startSymbol + " ---> ");

            while(!stack.isEmpty()){
                char term = stack.pop();

                if(nonTerminalVariables.contains(term)){
                    ArrayList<String> tempArrayRes = productions.get(term);
                    String tempRes = tempArrayRes.get(random.nextInt(tempArrayRes.size()));
                    System.out.print(stringBuilder + tempRes + " ---> ");

                    for(int i = tempRes.length() - 1; i >= 0; i--){
                        stack.add(tempRes.charAt(i));
                    }
                }
                else{
                    stringBuilder.append(term);
                }
            }
            result.add(stringBuilder.toString());
            System.out.print("[" + stringBuilder + "]");
        }
        System.out.print("\n\n" + "Final set of strings: ");
        return result;
    }

    public FiniteAutomaton toFiniteAutomaton(){
        FiniteAutomaton finiteAutomaton = new FiniteAutomaton(nonTerminalVariables,
                                              terminalVariables,
                                              startSymbol, 'F');
        for(char key: productions.keySet()){
            for(String element: productions.get(key)){
                if(element.length() < 2){
                    finiteAutomaton.setTransitions(new Transition(key,element.charAt(0),
                                                   finiteAutomaton.getFinalState()));
                }
                else{
                    finiteAutomaton.setTransitions(new Transition(key,element.charAt(0),
                                                   element.charAt(1)));
                }
            }
        }
        return finiteAutomaton;
    }
}
