package com.grammarcomp.generation;

import java.util.ArrayList;

/**
 * Created by nick on 26.03.16.
 */
public class Combination implements GrammarElement {

    private boolean isOptional;
    private boolean isStar;
    private boolean isPlus;
    private String name;
    private ArrayList<GrammarElement> elements;

    Combination() {
        this.isOptional = false;
        this.isStar = false;
        this.isPlus = false;
        this.name = "";
        elements = new ArrayList<GrammarElement>();
    }

    Combination(String name) {
        this.isOptional = false;
        this.isStar = false;
        this.isPlus = false;
        this.name = name;
        elements = new ArrayList<GrammarElement>();
    }

    @Override
    public boolean isOptional() {
        return this.isOptional;
    }

    @Override
    public void setOptional() {
        this.isOptional = true;
    }

    @Override
    public boolean isStar() {
        return this.isStar;
    }

    @Override
    public void setStar() {
        this.isStar = true;
    }

    @Override
    public boolean isPlus() {
        return this.isPlus;
    }

    @Override
    public void setPlus() {
        this.isPlus = true;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return this.name;
    }

    public void addGrammarElement(GrammarElement ge) {
        this.elements.add(ge);
        this.name += ge.getName();
    }

    public ArrayList<GrammarElement> getElements() {
        return this.elements;
    }
}
