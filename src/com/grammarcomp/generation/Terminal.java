package com.grammarcomp.generation;

public class Terminal implements GrammarElement{

    private boolean isOptional;
    private boolean isStar;
    private boolean isPlus;
    private String name;

    Terminal() {
        this.isOptional = false;
        this.isStar = false;
        this.isPlus = false;
    }

    Terminal(String name) {
        this.isOptional = false;
        this.isStar = false;
        this.isPlus = false;
        this.name = name;
    }

    @Override
    public boolean isOptional() {
        return this. isOptional;
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
}
