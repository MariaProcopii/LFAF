package automaton;

import grammar.*;

import java.util.*;
import java.util.regex.*;

public class FiniteAutomaton
{
    private HashSet<String> possibleStates;
    private HashSet<String> alphabet;
    private ArrayList<Transition> transitions;
    private final String initialState;
    private final String finalState;

    public FiniteAutomaton(HashSet<String> vn,
                           HashSet<String> vt,
                           String initialState,
                           String finalState
    ){

        transitions = new ArrayList<>();
        possibleStates = new HashSet<>(vn);
        possibleStates.add(finalState);
        alphabet = new HashSet<>(vt);
        this.initialState = initialState;
        this.finalState = finalState;
    }
    public ArrayList<Transition> getTransitions() {
        return transitions;
    }
    public HashSet<String> getPossibleStates() {
        return possibleStates;
    }
    public HashSet<String> getAlphabet() {
        return alphabet;
    }

    public void setTransitions(Transition transition) {
        transitions.add(transition);
    }
    public void setTransition(String currentState, String transitionLabel, String nextState) {
        transitions.add(new Transition(currentState, transitionLabel, nextState));
    }

    public void printTransitions(){
        System.out.println("\nTransitions set: ");
        for(Transition el: transitions){
            System.out.println(el.toString());
        }
    }

    public String getFinalState() {
        return finalState;
    }

    public boolean wordIsValid(String word){
        boolean valid = false;
        String currentState = initialState;

        myBreakLabel:
        for(int i = 0; i < word.length(); i++){
            int count = 0;
            for(Transition tr : transitions){
                count++;
                if(tr.getCurrentState().equals(currentState) &&
                        tr.getTransitionLabel().equals(String.valueOf(word.charAt(i)))){
                    currentState = tr.getNextState();
                    valid = true;
                    break;
                }
                else{
                    valid = false;
                    if(count == transitions.size()){
                        break myBreakLabel;
                    }
                }
            }
        }
        if(valid && currentState.equals(finalState)){
            System.out.println("\nWord ["+ word +"] is valid");
        }
        else{
            System.out.println("\nWord ["+ word +"] is not valid");
        }
        return valid;
    }

    public Grammar toGrammar(){

        ArrayList<String> prodKey = new ArrayList<>();
        ArrayList<String> prodVal = new ArrayList<>();
        for(Transition tr: transitions){
            prodKey.add(tr.getCurrentState());
            prodVal.add(tr.getTransitionLabel() + tr.getNextState());
        }

        String[] pk = prodKey.toArray(new String[0]);
        String[] pv = prodVal.toArray(new String[0]);
        String[] ps = possibleStates.toArray(new String[0]);
        String[] alph = alphabet.toArray(new String[0]);

        return new Grammar(ps, alph, pk, pv, initialState);
    }

    public void isNFA(){
        Grammar grammar = this.toGrammar();
        HashMap<String, ArrayList<String>> production = grammar.getProductions();

        for(ArrayList<String> states : production.values()){
            if(states.size() > alphabet.size()){
                System.out.println("FA type: Non-deterministic FA");
                return;
            }
        }
        System.out.println("FA type: Deterministic FA");
    }

    public void convertToDFA(){
        ArrayList<Transition> newTransitions = new ArrayList<>();
        Grammar grammar = this.toGrammar();
        Stack<String> stack = new Stack<>();
        ArrayList<String> analyzed = new ArrayList<>(); //already included in states hashMap
        stack.add(initialState);

        while (!stack.empty()){
            HashMap<String, ArrayList<String>> states = new HashMap<>();
            String term = stack.pop();
            analyzed.add(term);
            ArrayList<String> prodList = new ArrayList<>();

            if(possibleStates.contains(term)){ //state formed just from one term. Ex: q1
                prodList = grammar.getProductions().get(term); //list of transition label and possible from that transition state
            }
            else {
                HashSet<String> unique = new HashSet<>();
                String pattern = "q\\d+"; //  extract strings that match the pattern "q" followed by one or more digits
                Pattern p = Pattern.compile(pattern);
                Matcher m = p.matcher(term);

                while (m.find()) {
                    String match = m.group();
                    unique.addAll(grammar.getProductions().get(match));  //reunion between found terms
                }
                prodList.addAll(unique);
                Collections.sort(prodList);
            }
            for(String element : prodList){
                String trLabel = element.substring(0, 1);
                String transition = element.substring(1); // now we can use q0 or A for representation of non-terminals

                if(!states.containsKey(trLabel)){
                states.put(trLabel, new ArrayList<>());
                }
                states.get(trLabel).add(transition);
            }

            for(String label : alphabet){
                String newNextState;
                if(!states.isEmpty()){
                    newNextState = String.join("", states.get(label)); //form new state trans
                }
                else{
                    newNextState = "empty";
                }
                newTransitions.add(new Transition(term, label , newNextState));

                if(!analyzed.contains(newNextState) && !newNextState.equals("empty")){ //add state to stack if it wasn't analyzed yet
                    stack.add(newNextState);
                    analyzed.add(newNextState);
                }
            }
        }
        transitions = newTransitions;
    }
}