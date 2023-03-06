package grammar;

import java.util.*;
import automaton.*;
public class Grammar {
    private final HashSet<String> nonTerminalVariables = new HashSet<>();
    private final HashSet<String> terminalVariables = new HashSet<>();
    private final HashMap<String, ArrayList<String>> productions = new HashMap<>();
    private final String startSymbol;

    public Grammar(String[] vn, String[] vt, String[] prodKey,
                   String[] prodVal, String startSymbol){

        nonTerminalVariables.addAll(Arrays.asList(vn));
        terminalVariables.addAll(Arrays.asList(vt));
        genProductions(prodKey, prodVal);
        this.startSymbol = startSymbol;
    }

    public HashMap<String, ArrayList<String>> getProductions() {
        return productions;
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

    public FiniteAutomaton toFiniteAutomaton(String finalState){
        FiniteAutomaton finiteAutomaton = new FiniteAutomaton(nonTerminalVariables,
                                              terminalVariables,
                                              startSymbol, finalState);
        for(String key: productions.keySet()){
            for(String element: productions.get(key)){
                if(element.length() < 2){
                    finiteAutomaton.setTransitions(new Transition(key,String.valueOf(element.charAt(0)),
                                                   finiteAutomaton.getFinalState()));
                }
                else{
                    finiteAutomaton.setTransitions(new Transition(key,String.valueOf(element.charAt(0)),
                                                    element.substring(1)));
                }
            }
        }
        return finiteAutomaton;
    }

    public void grammarType(){
        boolean isRegular = true;
        boolean isContextFree = true;

        //Check if production is not of the form A -> BaB or q0 -> q01q0
        for(String key : productions.keySet()){
            if(key.length() > 1 && !nonTerminalVariables.contains(key)){
                isContextFree = false;
                isRegular = false;
            }
        }

        for(ArrayList<String> list : productions.values()){
            for(String element : list){

                //check the chase when we have non-terminal of the form q1, q0 -> aq1
                boolean isTwoSymbol = nonTerminalVariables.contains(element.substring(1));

                // Check if production is of the form A -> aB or A -> a
                if(element.length() > 2 && !isTwoSymbol){
                    System.out.println(element.substring(1));
                    isRegular = false;
                }

                else if(element.length() == 2 || isTwoSymbol){
                    String first = String.valueOf(element.charAt(0));
                    String second = element.substring(1);
                    if(!terminalVariables.contains(first) || !nonTerminalVariables.contains(second)){
                        isRegular = false;
                    }
                }

                else {
                    String symbol = String.valueOf(element.charAt(0));

                    // Check if production is of the form A -> a
                    if(!terminalVariables.contains(symbol)){
                        isRegular = false;
                    }
                }
            }
        }

        if(isRegular){
            System.out.println("Regular");
        }
        else if(isContextFree){
            System.out.println("Context-free");
        }
        else {
            System.out.println("Context-sensitive");
        }
    }
}
