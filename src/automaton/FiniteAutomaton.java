package automaton;

import java.util.ArrayList;
import java.util.HashSet;

public class FiniteAutomaton
{
    private HashSet<Character> possibleStates;
    private HashSet<Character> alphabet;
    private final ArrayList<Transition> transitions;
    private final char initialState;
    private final char finalState;

    public FiniteAutomaton(HashSet<Character> vn,
                           HashSet<Character> vt,
                           char initialState,
                           char finalState
                           ){

        transitions = new ArrayList<>();
        possibleStates = new HashSet<>(vn);
        possibleStates.add('F');
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

    public char getFinalState() {
        return finalState;
    }

    public boolean wordIsValid(String word){
    boolean valid = false;
    char currentState = initialState;

    for(int i = 0; i < word.length(); i++){
        for(Transition tr : transitions){
            if(tr.getCurrentState() == currentState &&
               tr.getTransitionLabel() == word.charAt(i)){

                currentState = tr.getNextState();
                break;
            }
        }
    }
    if(currentState == finalState){
        valid = true;
        System.out.println("\nWord ["+ word +"] is valid");
    }
    else{
        System.out.println("\nWord ["+ word +"] is not valid");
    }
    return valid;
    }
}
