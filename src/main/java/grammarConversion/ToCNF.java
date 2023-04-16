package grammarConversion;
import grammar.Grammar;

import java.lang.reflect.Array;
import java.util.*;
import java.lang.*;

public class ToCNF{

    private final Grammar grammar;
    public ToCNF(Grammar gr) {
        this.grammar = new Grammar(gr);
    }

    private void rmEmptyProd(){
        HashSet<String> emptyProd = new HashSet<>(); //set of empty productions

        for(String key : grammar.getProductions().keySet()){
            ArrayList<String> prodList = grammar.getProductions().get(key);
            if(prodList.contains("")){
                prodList.remove("");
                emptyProd.add(key);
            }
        }

        for(String emptyP: emptyProd){
            for(String prodLeft : grammar.getProductions().keySet()){
                List<String> newProductions = new ArrayList<>(); //list with new production with all combination of found occurrences
                ArrayList<String> prodList = grammar.getProductions().get(prodLeft);
                for (int i = 0; i < prodList.size(); i++){
                    String s = prodList.get(i);
                    if(s.contains(emptyP)){
                        prodList.remove(s);
                        newProductions.addAll(newProdList(s, emptyP));
                        i--;
                    }
                }
                prodList.addAll(newProductions);
            }
        }
    }

    // creates list with new production with all combination of found occurrences
    private ArrayList<String> newProdList(String s, String emptyP){
        ArrayList<String> newProductions = new ArrayList<>();
        ArrayList<Integer> occurrences = new ArrayList<>();
        for(int i = 0; i < s.length(); i++){
            if(emptyP.charAt(0) == s.charAt(i)){
                occurrences.add(i);
            }
        }
        List<List<Integer>> combinations = generateCombinations(occurrences);
        for(List<Integer> positions : combinations){
            newProductions.add(removeCharsAtPositions(s, positions));
        }
        newProductions.add(s); //add the initial production without removed symbols
        return newProductions;
    }

    // creates combination of all occurrences of emptyProduction in the given production
    private List<List<Integer>> generateCombinations(ArrayList<Integer> nums) {
        List<List<Integer>> result = new ArrayList<>();
        backtrack(result, new ArrayList<>(), nums, 0);
        return result;
    }

    //backtracking is used to create the combinations
    private static void backtrack(List<List<Integer>> result, List<Integer> tempList, ArrayList<Integer> nums, int start) {
        if (tempList.size() > 0) {
            result.add(new ArrayList<>(tempList));
        }
        for (int i = start; i < nums.size(); i++) {
            tempList.add(nums.get(i));
            backtrack(result, tempList, nums, i + 1);
            tempList.remove(tempList.size() - 1);
        }
    }

    //removers the emptyTransaction symbol found at a given position
    private String removeCharsAtPositions(String inputString, List<Integer> positions) {
        StringBuilder stringBuilder = new StringBuilder(inputString);

        // Sort the positions in descending order so that we don't have to adjust
        // the positions when we remove characters from the string.
        Collections.sort(positions, Collections.reverseOrder());
        for (int position : positions) {
            stringBuilder.deleteCharAt(position);
        }

        return stringBuilder.toString();
    }

    public void rmUnitProd(){
        for(String key : grammar.getProductions().keySet()){
            ArrayList<String> prodList = grammar.getProductions().get(key);
            for(String prod : prodList){
                if(prod.length() == 1 && Character.isUpperCase(prod.charAt(0))){ //chase when we have unit A -> B
                    prodList.remove(prod); // remove the found production Ex: B
                    prodList.addAll(grammar.getProductions().get(key)); // add the set of productions corresponding to deleted production B -> ...
                }
            }
        }
    }

    public Grammar getCopyModGrammar(){
        rmEmptyProd();
        return grammar;
    }
}
