package automaton;

import grammar.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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
}