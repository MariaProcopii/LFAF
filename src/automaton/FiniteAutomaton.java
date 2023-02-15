package automaton;

import java.util.ArrayList;
import java.util.HashSet;

public class FiniteAutomaton
{
    private HashSet<Character> possibleStates;
    private HashSet<Character> alphabet;
    private ArrayList<Transition> transitions;
    private char initialState;
    private char finalState;

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
    public HashSet<Character> getPossibleStates() {
        return possibleStates;
    }
    public HashSet<Character> getAlphabet() {
        return alphabet;
    }
    public void setTransitions(Transition transition) {
        transitions.add(transition);
    }

    public ArrayList<Transition> getTransitions() {
        return transitions;
    }
    public void printTransitions(){
        for(Transition el: transitions){
            System.out.println(el.toString());
        }
    }

    public char getInitialState() {
        return initialState;
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
        if(currentState == 'F'){
            valid = true;
            System.out.println("Word is valid");
        }
        else{
            System.out.println("Word is not valid");
        }
        return valid;
        };
}
