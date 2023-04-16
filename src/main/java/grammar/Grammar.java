package grammar;

import java.util.*;
import automaton.*;
public class Grammar {
    private HashSet<String> nonTerminalVariables = new HashSet<>();
    private HashSet<String> terminalVariables = new HashSet<>();
    private HashMap<String, ArrayList<String>> productions = new HashMap<>();
    private final String startSymbol;

    public Grammar(String[] vn, String[] vt, String[] prodKey,
                   String[] prodVal, String startSymbol){

        nonTerminalVariables.addAll(Arrays.asList(vn));
        terminalVariables.addAll(Arrays.asList(vt));
        genProductions(prodKey, prodVal);
        this.startSymbol = startSymbol;
    }


    public Grammar(Grammar other) { // Copy constructor
        this.nonTerminalVariables = new HashSet<>(other.nonTerminalVariables);
        this.terminalVariables = new HashSet<>(other.terminalVariables);
        this.productions = copyHashMapWithArrayListValues(other.productions);
        this.startSymbol = other.startSymbol;
    }

    // Method is used to make the copy of hashMap. We can't just do a new instance
    // of HashMap as we ded with HashSet because values are
    // represented as ArrayList and we need to iterate through them
    private HashMap<String, ArrayList<String>> copyHashMapWithArrayListValues(HashMap<String, ArrayList<String>> original) {
        HashMap<String, ArrayList<String>> copy = new HashMap<>();
        for (Map.Entry<String, ArrayList<String>> entry : original.entrySet()) {
            ArrayList<String> originalList = entry.getValue();
            ArrayList<String> copyList = new ArrayList<>(originalList);
            copy.put(entry.getKey(), copyList);
        }
        return copy;
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

    public HashSet<String> getNonTerminalVariables() {
        return nonTerminalVariables;
    }

    public HashSet<String> getTerminalVariables() {
        return terminalVariables;
    }

    public String getStartSymbol() {
        return startSymbol;
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
                if(element.length() < 2 ){
                    finiteAutomaton.setTransitions(new Transition(key, element,
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

                if(element.length() == 0){
                    System.out.println("Grammar type: Unrestricted grammar Type 0");
                    return;
                }

                //check the chase when we have non-terminal of the form q1, q0 -> aq1
                boolean isTwoSymbol = nonTerminalVariables.contains(element.substring(1));

                // Check if production is not of the form A -> aB or A -> a
                if(element.length() > 2 && !isTwoSymbol){
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

                    // Check if production is not of the form A -> a
                    if(!terminalVariables.contains(symbol)){
                        isRegular = false;
                    }
                }
            }
        }

        if(isRegular){
            System.out.println("Grammar type: Regular Type 3");
        }
        else if(isContextFree){
            System.out.println("Grammar type: Context-free Type 2");
        }
        else {
            System.out.println("Grammar type: Context-sensitive Type 1");
        }
    }
}