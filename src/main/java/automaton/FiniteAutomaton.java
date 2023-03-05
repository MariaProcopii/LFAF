package automaton;

import java.util.ArrayList;
import java.util.HashSet;

public class FiniteAutomaton
{
    private HashSet<String> possibleStates;
    private HashSet<String> alphabet;
    private final ArrayList<Transition> transitions;
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

    public void setTransitions(Transition transition) {
        transitions.add(transition);
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
        int count = 1;
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
    if(valid && currentState == finalState){
        System.out.println("\nWord ["+ word +"] is valid");
    }
    else{
        System.out.println("\nWord ["+ word +"] is not valid");
    }
    return valid;
    }
}
