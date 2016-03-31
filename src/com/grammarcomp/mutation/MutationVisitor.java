package com.grammarcomp.mutation;

import java.util.ArrayList;
import java.lang.StringBuilder;

public class MutationVisitor extends
        GFourBaseVisitor<ArrayList<ArrayList<StringBuilder>>> {

    // variables to save the grammar and the mutations
    private StringBuilder original = new StringBuilder();
    private ArrayList<ArrayList<StringBuilder>> mutations;



    // Nonterminal | Terminal -> visitChildren
    @Override
    public ArrayList<ArrayList<StringBuilder>> visitNt(GFourParser.NtContext ctx) {
        visitChildren(ctx);
        return null;
    }

    // Nonterminal -> append Name to every mutation and original
    @Override
    public ArrayList<ArrayList<StringBuilder>> visitN(GFourParser.NContext ctx) {
        for(int i = 0;i < mutations.size(); i++){
            for(int j = 0; j < mutations.get(i).size(); j++){
                mutations.get(i).get(j).append(ctx.getChild(0));
                mutations.get(i).get(j).append(" ");
            }
        }
        original.append(ctx.getChild(0));
        original.append(" ");


        return null;
    }

    // Terminal -> append to each mutation and original
    //          -> create one new mutation where this terminal is missing,
    // by copying the original before terminal gets appended
    @Override
    public ArrayList<ArrayList<StringBuilder>> visitT(GFourParser.TContext ctx) {
        for(int i = 0;i < mutations.size(); i++){
            for(int j = 0; j < mutations.get(i).size(); j++){
                mutations.get(i).get(j).append(ctx.getChild(0));
                mutations.get(i).get(j).append(" ");
            }
        }

        mutations.get(6).add(new StringBuilder());
        mutations.get(6).get(mutations.get(6).size()-1).append(original.toString());
        mutations.get(6).get(mutations.get(6).size()-1).append(" ");

        original.append(ctx.getChild(0));
        original.append(" ");


        return null;
    }

    // plus -> append "(" ... ")+" to each mutation and original
    // create one mutations where + -> * and + -> ?
    @Override
    public ArrayList<ArrayList<StringBuilder>> visitPlus(GFourParser.PlusContext ctx) {
        for(int i = 0; i < mutations.size(); i++){
            for( int j = 0; j < mutations.get(i).size(); j++){
                mutations.get(i).get(j).append("(");
            }
        }
        original.append("(");
        visitChildren(ctx);
        for(int i = 0; i < mutations.size(); i++){
            for( int j = 0; j < mutations.get(i).size(); j++){
                mutations.get(i).get(j).append(")+ ");
            }
        }
        mutations.get(4).add(new StringBuilder());
        mutations.get(5).add(new StringBuilder());
        mutations.get(4).get(mutations.get(4).size()-1).append(original.toString());
        mutations.get(4).get(mutations.get(4).size()-1).append(")? ");
        mutations.get(5).get(mutations.get(5).size()-1).append(original.toString());
        mutations.get(5).get(mutations.get(5).size()-1).append(")* ");
        original.append(")+ ");
        return null;
    }


    // import -> append to each mutation and original
    @Override
    public ArrayList<ArrayList<StringBuilder>> visitGimp(GFourParser.GimpContext ctx) {
        for(int i = 0; i < ctx.getChildCount(); i++){
            original.append(ctx.getChild(i));
            original.append(" ");
        }
        original.append("\n\n");

        return null;
    }

    // grammar declaration -> append to each mutation and original
    @Override
    public ArrayList<ArrayList<StringBuilder>> visitGdecl(GFourParser.GdeclContext ctx) {
        for(int i = 0; i < ctx.getChildCount(); i++){
            original.append(ctx.getChild(i));
            original.append(" ");
        }
        original.append("\n");

        return null;
    }


    // Rule lhs -> One Nonterminal -> append to each mutation and original
    @Override
    public ArrayList<ArrayList<StringBuilder>> visitPlhs(GFourParser.PlhsContext ctx) {
        for(int i = 0; i < mutations.size(); i++){
            for( int j = 0; j < mutations.get(i).size(); j++){
                mutations.get(i).get(j).append(ctx.getChild(0));
                mutations.get(i).get(j).append(" : ");
            }
        }

        original.append(ctx.getChild(0));
        original.append(" : ");

        return null;
    }

    // kleeve-star -> append "(" ... ")*" to each mutation and original
    // create one mutations where * -> + and * -> ?
    @Override
    public ArrayList<ArrayList<StringBuilder>> visitStar(GFourParser.StarContext ctx) {
        for(int i = 0; i < mutations.size(); i++){
            for( int j = 0; j < mutations.get(i).size(); j++){
                mutations.get(i).get(j).append("(");
            }
        }
        original.append("(");
        visitChildren(ctx);
        for(int i = 0; i < mutations.size(); i++){
            for( int j = 0; j < mutations.get(i).size(); j++){
                mutations.get(i).get(j).append(")* ");
            }
        }
        mutations.get(2).add(new StringBuilder());
        mutations.get(3).add(new StringBuilder());
        mutations.get(2).get(mutations.get(2).size()-1).append(original.toString());
        mutations.get(2).get(mutations.get(2).size()-1).append(")? ");
        mutations.get(3).get(mutations.get(3).size()-1).append(original.toString());
        mutations.get(3).get(mutations.get(3).size()-1).append(")+ ");
        original.append(")* ");
        return null;
    }

    @Override
    public ArrayList<ArrayList<StringBuilder>> visitTok(GFourParser.TokContext ctx) {
        original.append(ctx.getChild(0));
        for(int i = 0; i < mutations.size(); i++){
            for( int j = 0; j < mutations.get(i).size(); j++){
                mutations.get(i).get(j).append(ctx.getChild(0));
            }
        }
        return null;
    }

    @Override
    public ArrayList<ArrayList<StringBuilder>> visitPrhs(GFourParser.PrhsContext ctx) {
        visitChildren(ctx);
        return null;
    }


    // optional -> append "(" ... ")?" to each mutation and original
    // create one mutations where ? -> + and ? -> *
    @Override
    public ArrayList<ArrayList<StringBuilder>> visitOption(GFourParser.OptionContext ctx) {
        for(int i = 0; i < mutations.size(); i++){
            for( int j = 0; j < mutations.get(i).size(); j++){
                mutations.get(i).get(j).append("(");
            }
        }
        original.append("(");
        visitChildren(ctx);
        for(int i = 0; i < mutations.size(); i++){
            for( int j = 0; j < mutations.get(i).size(); j++){
                mutations.get(i).get(j).append(")? ");
            }
        }
        mutations.get(0).add(new StringBuilder());
        mutations.get(1).add(new StringBuilder());
        mutations.get(0).get(mutations.get(0).size()-1).append(original.toString());
        mutations.get(0).get(mutations.get(0).size()-1).append(")* ");
        mutations.get(1).get(mutations.get(1).size()-1).append(original.toString());
        mutations.get(1).get(mutations.get(1).size()-1).append(")+ ");
        original.append(")? ");
        return null;
    }

    // after each rule -> linebreak
    @Override
    public ArrayList<ArrayList<StringBuilder>> visitPrule(GFourParser.PruleContext ctx) {
        visitChildren(ctx);
        for(int i = 0; i < mutations.size(); i++){
            for(int j = 0; j < mutations.get(i).size(); j++){
                mutations.get(i).get(j).append(";\n");
            }
        }
        original.append(";\n");
        return null;
    }



    @Override
    public ArrayList<ArrayList<StringBuilder>> visitGfile(GFourParser.GfileContext ctx) {
        mutations = new ArrayList<>();
	    /*
	    0 = ? -> *
	    1 = ? -> +
	    2 = * -> ?
	    3 = * -> +
	    4 = + -> ?
	    5 = + -> *
	    6 = "terminal" ->
	    */

        // initialize the list
        for(int i = 0; i < 7; i++)
        {
            mutations.add(new ArrayList<StringBuilder>());
        }
        visitChildren(ctx);

        // remove Empty parentheses from optional, kleeve-star, plus where terminal inside was removed
        for(int i = 0; i < mutations.get(6).size(); i++){
            removeEmptyPars(mutations.get(6).get(i));
        }
        return mutations;
    }


    // remove empty parenthesis "( )+" "( )?" "( )*"
    public static void removeEmptyPars(StringBuilder b){
        for(int i = 0; i < 3; i++) {
            int index = -1;
            String emptyOption = " ( )?";
            String emptyPlus   = " ( )+";
            String emptyStar   = " ( )*";
            switch(i){
                case 0:
                    index = b.indexOf(emptyOption);
                    break;
                case 1:
                    index = b.indexOf(emptyPlus);
                    break;
                case 2:
                    index = b.indexOf(emptyStar);
                    break;
            }
            while(index != -1){
                b.replace(index, index+emptyStar.length(), "");
                switch(i){
                    case 0:
                        index = b.indexOf(emptyOption);
                        break;
                    case 1:
                        index = b.indexOf(emptyPlus);
                        break;
                    case 2:
                        index = b.indexOf(emptyStar);
                        break;
                }
            }
        }
    }

}