import java.util.*;
import automaton.*;
public class Grammar {
    private final HashSet<String> nonTerminalVariables = new HashSet<>();
    private final HashSet<String> terminalVariables = new HashSet<>();
    private final HashMap<String, ArrayList<String>> productions = new HashMap<>();
    private final String startSymbol;

    public Grammar(String[] vn, String[] vt, String[] prodKey,
                   String[] prodVal, String startSymbol){

        genNonTerminalVariables(vn);
        genTerminalVariables(vt);
        genProductions(prodKey, prodVal);
        this.startSymbol = startSymbol;
    }

    public void genNonTerminalVariables(String[] vn){
        for (String c : vn) {
            nonTerminalVariables.add(c);
        }
    }
    public void genTerminalVariables(String[] vt){
        for (String c : vt) {
            terminalVariables.add(c);
        }
    }
    public void genProductions(String[] prodKey, String[] prodVal) {
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
            Stack<String> stack = new Stack<>();
            StringBuilder stringBuilder = new StringBuilder();

            stack.add(startSymbol);
            System.out.print("\n" + startSymbol + " ---> ");

            while(!stack.isEmpty()){
                String term = stack.pop();

                if(nonTerminalVariables.contains(term)){
                    ArrayList<String> tempArrayRes = productions.get(term);
                    String tempRes = tempArrayRes.get(random.nextInt(tempArrayRes.size()));
                    System.out.print(stringBuilder + tempRes + " ---> ");

                    for(int i = tempRes.length() - 1; i >= 0; i--){
                        stack.add(String.valueOf(tempRes.charAt(i)));
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
                                              startSymbol, "F");
        for(String key: productions.keySet()){
            for(String element: productions.get(key)){
                if(element.length() < 2){
                    finiteAutomaton.setTransitions(new Transition(key,String.valueOf(element.charAt(0)),
                                                   finiteAutomaton.getFinalState()));
                }
                else{
                    finiteAutomaton.setTransitions(new Transition(key,String.valueOf(element.charAt(0)),
                                                    String.valueOf(element.charAt(1))));
                }
            }
        }
        return finiteAutomaton;
    }
}
