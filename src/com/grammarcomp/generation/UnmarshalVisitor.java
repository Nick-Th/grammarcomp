package com.grammarcomp.generation;

import com.grammarcomp.mutation.GFourBaseVisitor;
import com.grammarcomp.mutation.GFourParser;

import java.lang.StringBuilder;

public class UnmarshalVisitor extends
        GFourBaseVisitor<Grammar> {

    private Grammar g;
    private String e = "";

    @Override
    public Grammar visitNt(GFourParser.NtContext ctx) {
        visitChildren(ctx);
        return null;
    }


    // create new Nonterminal-Object
    // if it is encapsulated by *+? -> set this option
    @Override
    public Grammar visitN(GFourParser.NContext ctx) {
        GrammarElement tmp = new Nonterminal(ctx.getText());
        if(e.equals("plus")){
            tmp.setPlus();
        }
        if(e.equals("star")){
            tmp.setStar();
        }
        if(e.equals("option")){
            tmp.setOptional();
        }
        e = "";
        g.addElementToLastRule(tmp);
        return null;
    }


    // create new terminal-Object
    // if it is encapsulated by *+? -> set this option
    @Override
    public Grammar visitT(GFourParser.TContext ctx) {
        GrammarElement tmp = new Terminal(ctx.getText());
        if(e.equals("plus")){
            tmp.setPlus();
        }
        if(e.equals("star")){
            tmp.setStar();
        }
        if(e.equals("option")){
            tmp.setOptional();
        }
        e = "";
        g.addElementToLastRule(tmp);
        return null;
    }


    // set + for next element
    @Override
    public Grammar visitPlus(GFourParser.PlusContext ctx) {
        e = "plus";

        // if more than 1 grandchild -> more than 1 terminal|nonterminal in the parentheses -> create Combination obj
        if(ctx.getChild(1).getChildCount() > 1){

            Combination tmp = new Combination();
            tmp.setPlus();
            for(int i = 0; i < ctx.getChild(1).getChildCount(); i++){
                if(ctx.getChild(1).getChild(i).getText().startsWith("'")) {
                    GrammarElement ge = new Terminal(ctx.getChild(1).getChild(i).getText());
                    tmp.addGrammarElement(ge);
                }else{
                    GrammarElement ge = new Nonterminal(ctx.getChild(1).getChild(i).getText());
                    tmp.addGrammarElement(ge);
                }
            }
            g.addElementToLastRule(tmp);
        }
        else {
            visitChildren(ctx);
        }
        e = "";
        return null;
    }

    @Override
    public Grammar visitGimp(GFourParser.GimpContext ctx) {
        visitChildren(ctx);
        return null;
    }

    @Override
    public Grammar visitGdecl(GFourParser.GdeclContext ctx) {
        visitChildren(ctx);
        return null;
    }

    @Override
    public Grammar visitPlhs(GFourParser.PlhsContext ctx) {
        g.addRule(ctx.getText());
        return null;
    }

    // set * for next element
    @Override
    public Grammar visitStar(GFourParser.StarContext ctx) {
        e = "star";
        // if more than 1 grandchild -> more than 1 terminal|nonterminal in the parentheses -> create Combination obj
        if(ctx.getChild(1).getChildCount() > 1){
            Combination tmp = new Combination();
            tmp.setStar();
            for(int i = 0; i < ctx.getChild(1).getChildCount(); i++){
                if(ctx.getChild(1).getChild(i).getText().startsWith("'")) {
                    GrammarElement ge = new Terminal(ctx.getChild(1).getChild(i).getText());
                    tmp.addGrammarElement(ge);
                }else{
                    GrammarElement ge = new Nonterminal(ctx.getChild(1).getChild(i).getText());
                    tmp.addGrammarElement(ge);
                }
            }
            g.addElementToLastRule(tmp);
        }
        else {
            visitChildren(ctx);
        }
        e = "";
        return null;
    }

    @Override
    public Grammar visitTok(GFourParser.TokContext ctx) {
        visitChildren(ctx);
        return null;
    }

    @Override
    public Grammar visitPrhs(GFourParser.PrhsContext ctx) {
        visitChildren(ctx);
        return null;
    }

    // set ? for next element
    @Override
    public Grammar visitOption(GFourParser.OptionContext ctx) {
        e = "option";
        // if more than 1 grandchild -> more than 1 terminal|nonterminal in the parentheses -> create Combination obj
        if(ctx.getChild(1).getChildCount() > 1){
            Combination tmp = new Combination();
            tmp.setOptional();
            for(int i = 0; i < ctx.getChild(1).getChildCount(); i++){
                if(ctx.getChild(1).getChild(i).getText().startsWith("'")) {
                    GrammarElement ge = new Terminal(ctx.getChild(1).getChild(i).getText());
                    tmp.addGrammarElement(ge);
                }else{
                    GrammarElement ge = new Nonterminal(ctx.getChild(1).getChild(i).getText());
                    tmp.addGrammarElement(ge);
                }
            }
            g.addElementToLastRule(tmp);
        }
        else {
            visitChildren(ctx);
        }
        e = "";
        return null;
    }

    @Override
    public Grammar visitPrule(GFourParser.PruleContext ctx) {
        visitChildren(ctx);
        return null;
    }

    // create Grammar object -> every GrammarElement are saved in it
    // Grammar has map that maps lhs string to rhs element-list for each rule
    @Override
    public Grammar visitGfile(GFourParser.GfileContext ctx) {
        g = new Grammar();
        visitChildren(ctx);
        return g;
    }

}