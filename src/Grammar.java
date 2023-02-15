import java.util.*;
import automaton.*;
public class Grammar {
    private final HashSet<Character> nonTerminalVariables = new HashSet<>();
    private final HashSet<Character> terminalVariables = new HashSet<>();
    private final HashMap<Character, ArrayList<String>> productions = new HashMap<>();
    private char startSymbol;
    private int count;

    public void setCount(int count) {
        this.count = count;
    }

    public void setNonTerminalVariables(Character s){
        nonTerminalVariables.add(s);
    }
    public void setTerminalVariables(Character s){
        terminalVariables.add(s);
    }
    public void setProductions(Character key, String val) {
        if(!productions.containsKey(key)){
            productions.put(key, new ArrayList<>());
        }
        productions.get(key).add(val);
    }
    public void setStartSymbol(char startSymbol) {
        this.startSymbol = startSymbol;
    }

    public ArrayList<String> generateWords(){
        ArrayList<String> result = new ArrayList<>();
        Random random = new Random();

        while(result.size()  < count){
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
        System.out.println("\n\n" + "------------------------------------------------\n");
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
//        finiteAutomaton.printTransitions();
        return finiteAutomaton;
    };
}
