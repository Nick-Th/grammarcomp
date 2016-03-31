package com.grammarcomp.generation;

/**
 * Created by nick on 25.03.16.
 */
public interface GrammarElement {

    public boolean isOptional();
    public void setOptional();
    public boolean isStar();
    public void setStar();
    public boolean isPlus();
    public void setPlus();

    public void setName(String name);
    public String getName();
}
