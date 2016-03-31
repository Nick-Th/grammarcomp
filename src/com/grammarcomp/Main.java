package com.grammarcomp;

import com.grammarcomp.generation.UnmarshalVisitor;
import com.grammarcomp.mutation.*;
import com.grammarcomp.generation.*;
import com.grammarcomp.parser.*;
import org.antlr.v4.runtime.ANTLRFileStream;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.*;
import java.util.ArrayList;
import java.lang.StringBuilder;
import java.io.*;


public class Main {

    public static void main(String[] args) throws Exception {
        System.out.println("Parse reference grammar ...");
        System.out.println();
        ANTLRFileStream input = new ANTLRFileStream("grammar/Fsmlp.g4");
        GFourLexer lexer = new GFourLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        GFourParser parser = new GFourParser(tokens);

        System.out.println("Generate mutations from reference grammar ...");
        System.out.println();
        ParseTree tree = parser.gfile();
        ArrayList<ArrayList<StringBuilder>> mutations = new MutationVisitor().visit(tree);

        /*
        * If Directory for mutated Grammars does not exist create it
        * */
        String mutationPath = "grammar/mutations";
        File mDir = new File(mutationPath);
        if(! mDir.exists()){
            mDir.mkdir();
        }

        /*
        * If Directory for testdata does not exist create it
        * */
        String testDataPath = "testData";
        File tdDir = new File(testDataPath);
        if(! tdDir.exists()){
            tdDir.mkdir();
        }

        /*
        * If Directory for result does not exist create it
        * */
        String resultPath = "result";
        File rDir = new File(resultPath);
        if(! rDir.exists()){
            rDir.mkdir();
        }

        String filename;


        // create File for each mutated Grammar
        for(int i = 0; i < mutations.size(); i++) {
            for (int j = 0; j < mutations.get(i).size(); j++) {
                filename = mutationPath+"/";
                switch (i) {
                    case 0:
                        filename += "option_to_star";
                        break;
                    case 1:
                        filename += "option_to_plus";
                        break;

                    case 2:
                        filename += "star_to_option";
                        break;

                    case 3:
                        filename += "star_to_plus";
                        break;

                    case 4:
                        filename += "plus_to_option";
                        break;

                    case 5:
                        filename += "plus_to_star";
                        break;
                    case 6:
                        filename += "remove_keyword";

                }

                filename += "_" + j;
                FileWriter out = new FileWriter(filename);
                out.write(mutations.get(i).get(j).toString());
                out.close();
            }
        }
        System.out.println("Mutated Grammars written to "+mutationPath+"!");
        System.out.println();

        System.out.println("Generate testdata ...");
        System.out.println();
        for(File file: mDir.listFiles()){
            if(!file.isDirectory()){
                String mutatedFile = mutationPath+"/"+file.getName();
                ANTLRInputStream input2 = new ANTLRFileStream(mutatedFile);
                GFourLexer lexer2 = new GFourLexer(input2);
                CommonTokenStream tokens2 = new CommonTokenStream(lexer2);
                GFourParser parser2 = new GFourParser(tokens2);

                ParseTree tree2 = parser2.gfile();
                Grammar g = new UnmarshalVisitor().visit(tree2);

                // create File for testdata with production coverage
                FileWriter outpc = new FileWriter(tdDir+"/"+file.getName()+"_pc");
                outpc.write(g.generatePC());
                outpc.close();

                // create File for testdata with branch coverage
                FileWriter outbc = new FileWriter(tdDir+"/"+file.getName()+"_bc");
                outbc.write(g.generateBC());
                outbc.close();
            }
        }
        System.out.println("testdata written to "+testDataPath+"!");
        System.out.println();

        System.out.println("Parse testdata and write logfile ...");
        System.out.println();
        StringBuilder errors = new StringBuilder();

        // parse every testdata-file with parser generated from reference grammar
        for(File file: tdDir.listFiles()){
            if(!file.isDirectory()){
                String testdata = testDataPath+"/"+file.getName();
                ANTLRInputStream input2 = new ANTLRFileStream(testdata);
                FsmlpLexer lexer2 = new FsmlpLexer(input2);
                CommonTokenStream tokens2 = new CommonTokenStream(lexer2);
                FsmlpParser parser2 = new FsmlpParser(tokens2);

                // remove BasicErrorListener and add LogErrorListener
                LogErrorListener el = new LogErrorListener();
                parser2.removeErrorListeners();
                parser2.addErrorListener(el);


                ParseTree tree2 = parser2.fsm();
                errors.append(el.getErrorString());

            }
        }

        // get Errorstring from LogErrorListener and write it to file
        FileWriter outr = new FileWriter(rDir+"/ErrorLog.txt");
        outr.write(errors.toString());
        outr.close();

        System.out.println("logfile written to "+resultPath+"!");
        System.out.println();
        System.out.println("Finished!");



    }
}