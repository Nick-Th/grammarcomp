package com.grammarcomp.generation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;


public class Grammar {

    private HashMap<String, ArrayList<GrammarElement>> ruleList;
    private String rootRule;
    private ArrayList<String> optionalNames;
    private ArrayList<String> plusNames;
    private ArrayList<String> starNames;
    private ArrayList<Integer> optionalCounter;
    private ArrayList<Integer> plusCounter;
    private ArrayList<Integer> starCounter;


    // assume first rule is always root
    private boolean rootSet = false;
    private String lastRule;

    public Grammar(){
        this.ruleList = new HashMap<String, ArrayList<GrammarElement>>();
        optionalNames = new ArrayList<String>();
        plusNames = new ArrayList<String>();
        starNames = new ArrayList<String>();
        optionalCounter = new ArrayList<Integer>();
        plusCounter = new ArrayList<Integer>();
        starCounter = new ArrayList<Integer>();

    }

    // add a Rule to Rulelist rhs is empty
    public void addRule(String lhs){

        // if first Rule set Root
        if(!rootSet)
        {
            rootRule = lhs;
            rootSet = true;
        }
        this.ruleList.put(lhs,new ArrayList<GrammarElement>());
        this.lastRule = lhs;
    }

    // add an Element to rule
    // if Elements have * + ? properties the names are saved in a list
    // also a counter for each element is added to the corresponding counterlist
    // these are used for generating branch coverage
    public void addElementToRule(String lhs, GrammarElement element){

        // if rule does not exist now create it
        if(this.ruleList.get(lhs) == null){

            // if first Rule set Root
            if(!rootSet){
                rootRule = lhs;
                rootSet = true;
            }
            addRule(lhs);
        }
        this.ruleList.get(lhs).add(element);
        this.lastRule = lhs;
        if(element.isOptional() && !optionalNames.contains(element.getName())){
            optionalNames.add(element.getName());
            optionalCounter.add(0);
        }
        if(element.isPlus() && !plusNames.contains(element.getName())){
            plusNames.add(element.getName());
            plusCounter.add(0);
        }
        if(element.isStar() && !starNames.contains(element.getName())){
            starNames.add(element.getName());
            starCounter.add(0);
        }

    }


    // add Element to last Rule that was added
    public void addElementToLastRule(GrammarElement element){
        this.ruleList.get(lastRule).add(element);
        if(element.isOptional() && !optionalNames.contains(element.getName())){
            optionalNames.add(element.getName());
            optionalCounter.add(0);
        }
        if(element.isPlus() && !plusNames.contains(element.getName())){
            plusNames.add(element.getName());
            plusCounter.add(0);
        }
        if(element.isStar() && !starNames.contains(element.getName())){
            starNames.add(element.getName());
            starCounter.add(0);
        }
    }

    // return List for rhs of rootrule
    public ArrayList<GrammarElement> getRoot(){
        return this.ruleList.get(rootRule);
    }

    // return list for specified rule
    public ArrayList<GrammarElement> get(String lhs){
        return this.ruleList.get(lhs);
    }

    private int getValueOptional(String name){
        return this.optionalCounter.get(this.optionalNames.indexOf(name));
    }

    private int getValuePlus(String name){
        return this.plusCounter.get(this.plusNames.indexOf(name));
    }

    private int getValueStar(String name){
        return this.starCounter.get(this.starNames.indexOf(name));
    }


    // counters are incremented when the elements are used in generateBC
    // the counters help to decide if branch coverage is completed
    private void incOptionalCounterAt(String name){
        this.optionalCounter.set(this.optionalNames.indexOf(name),
                this.getValueOptional(name)+1);
    }

    private void incPlusCounterAt(String name){
        this.plusCounter.set(this.plusNames.indexOf(name),
                this.getValuePlus(name)+1);
    }

    private void incStarCounterAt(String name){
        this.starCounter.set(this.starNames.indexOf(name),
                this.getValueStar(name)+1);
    }


    // the value of the counter decides if an element is added 0,1 or two times to the testdata
    // this helps to generate testdata for each "branch"
    private int calculateLoopRuns(GrammarElement ge){
        int cnt = 1;
        String tmpNT = ge.getName();
        if(ge.isOptional()){
            if( getValueOptional(tmpNT)== 0){
                cnt = 0;
            }
            else{
                cnt = 1;
            }
            incOptionalCounterAt(tmpNT);
        }
        if(ge.isPlus()){
            if(getValuePlus(tmpNT) == 0){
                cnt = 2;
            }else{
                cnt = 1;
            }
            incPlusCounterAt(tmpNT);
        }
        if(ge.isStar()){
            if( getValueStar(tmpNT) == 0){
                cnt = 0;
            }else{
                if(getValueStar(tmpNT) == 2){
                    cnt = 2;
                }else{
                    cnt = 1;
                }
            }
            incStarCounterAt(tmpNT);
        }
        return cnt;
    }

    // iterates over the counters to see if each branch for the element is covered
    public boolean bcReached(){
        for(int i = 0; i < optionalCounter.size(); i++){
            if(optionalCounter.get(i) < 2){
                return false;
            }
        }
        for(int i = 0; i < plusCounter.size(); i++){
            if(plusCounter.get(i) < 2 ){
                return false;
            }
        }
        for(int i = 0; i < starCounter.size(); i++){
            if(starCounter.get(i) < 3){
                return false;
            }
        }
        return true;
    }


    // reset counters after bc
    private void resetCounters(){
        for(int i = 0; i < optionalCounter.size(); i++){
            optionalCounter.set(i,0);
        }
        for(int i = 0; i < plusCounter.size(); i++){
            plusCounter.set(i,0);
        }
        for(int i = 0; i < starCounter.size(); i++){
            starCounter.set(i,0);
        }
    }

    /*
        Generates a teststring for Production Coverage if all Productions are reachable from root rule,
        because it keeps every nonterminal regardless of it being enclosed by a kleeve-star or optional-operator.
        This only works, because no Grammars with decisions like (someNonterminal|someOtherNonterminal) are accepted.
        This implies also, that the teststring generated by this method reaches Nonterminal coverage.
        Also it is obvious that the teststring generated by this method reaches Trivial Coverage.
     */
    public String generatePC() {
        boolean finished = false;
        LinkedList<GrammarElement> tcList = new LinkedList<GrammarElement>();
        String tcTestString = "";
        for (GrammarElement ge : this.getRoot()) {
            if (ge instanceof Combination) {
                Combination c = (Combination) ge;
                for (GrammarElement e : c.getElements()) {
                    tcList.add(e);
                }
            } else {
                tcList.add(ge);
            }
        }


        while(!finished){
            for(int i = 0; i < tcList.size(); i++){
                if(tcList.get(i) instanceof Nonterminal){
                    String tmpNT = tcList.get(i).getName();
                    tcList.remove(i);
                    if(this.ruleList.get(tmpNT).size() == 0){
                        tcList.add(i,new Terminal("'placeholder'"));
                    } else {
                        for (int j = this.ruleList.get(tmpNT).size() - 1; j >= 0; j--) {
                            tcList.add(i, this.ruleList.get(tmpNT).get(j));
                        }
                    }
                    i = 0;
                    continue;
                }
                if(tcList.get(i) instanceof Combination){
                    Combination tmpC = (Combination) tcList.get(i);
                    ArrayList<GrammarElement> tmpCC = tmpC.getElements();
                    tcList.remove(i);
                    for (int j = tmpCC.size() - 1; j >= 0; j--) {
                        tcList.add(i, tmpCC.get(j));
                    }
                    i = 0;
                    continue;
                }
            }
            finished = true;


        }

        for(GrammarElement ge : tcList){
            if(ge instanceof Combination){
                Combination c = (Combination) ge;
                for(GrammarElement e : c.getElements()){
                    tcTestString += ge.getName().substring(1,ge.getName().length()-1);
                    tcTestString += " ";
                }
            }else {
                tcTestString += ge.getName().substring(1,ge.getName().length()-1);
                tcTestString += " ";
            }
        }
        return tcTestString;
    }


    /*
        generate testdata for branch coverage
     */
    public String generateBC(){
        boolean finished = false;
        LinkedList<GrammarElement> bcList = new LinkedList<GrammarElement>();
        String bcTestString = "";
        for (GrammarElement ge : this.getRoot()) {
            if (ge instanceof Combination) {
                Combination c = (Combination) ge;
                for (GrammarElement e : c.getElements()) {
                    bcList.add(e);
                }
            } else {
                bcList.add(ge);
            }
        }


        while(!finished){
            for(int i = 0; i < bcList.size(); i++){
                if(bcList.get(i) instanceof Nonterminal){
                    String tmpNT = bcList.get(i).getName();
                    int cnt = calculateLoopRuns(bcList.get(i));
                    bcList.remove(i);
                    for(int k = 0; k < cnt; k++) {
                        if (this.ruleList.get(tmpNT).size() == 0) {
                            bcList.add(i, new Terminal("'placeholder'"));
                        } else {
                            for (int j = this.ruleList.get(tmpNT).size() - 1; j >= 0; j--) {
                                bcList.add(i, this.ruleList.get(tmpNT).get(j));
                            }
                        }
                    }
                    i = 0;
                    continue;
                }
                if(bcList.get(i) instanceof Combination){
                    Combination tmpC = (Combination) bcList.get(i);
                    ArrayList<GrammarElement> tmpCC = tmpC.getElements();
                    int cnt = calculateLoopRuns(bcList.get(i));
                    bcList.remove(i);
                    for(int k = 0; k < cnt; k++) {
                        for (int j = tmpCC.size() - 1; j >= 0; j--) {
                            bcList.add(i, tmpCC.get(j));
                        }
                    }
                    i = 0;
                    continue;
                }
                if(bcList.get(i) instanceof Terminal){
                    GrammarElement tmpT = bcList.get(i);
                    int cnt = calculateLoopRuns(bcList.get(i));
                    for(int k = 1; k < cnt; k++){
                        bcList.add(i,tmpT);
                    }
                }

            }

            if(!bcReached()){
                for (GrammarElement ge : this.getRoot()) {
                    if (ge instanceof Combination) {
                        Combination c = (Combination) ge;
                        for (GrammarElement e : c.getElements()) {
                            bcList.add(e);
                        }
                    } else {
                        bcList.add(ge);
                    }
                }
                continue;
            }
            finished = true;

        }

        for(GrammarElement ge : bcList){
            if(ge instanceof Combination){
                Combination c = (Combination) ge;
                for(GrammarElement e : c.getElements()){
                    bcTestString += ge.getName().substring(1,ge.getName().length()-1);
                    bcTestString += " ";
                }
            }else {
                bcTestString += ge.getName().substring(1,ge.getName().length()-1);
                bcTestString += " ";
            }
        }

        resetCounters();
        return bcTestString;
    }
}
