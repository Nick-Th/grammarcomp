package com.grammarcomp.parser;


import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;

public class LogErrorListener extends BaseErrorListener{
    StringBuilder result;

    public LogErrorListener(){
        result = new StringBuilder();
    }

    @Override
    public void syntaxError(Recognizer<?,?> recognizer, Object offendingSymbol, int line, int charPositionInLine,
                            String msg, RecognitionException e){

        // format Error and save in StringBuilder
        String source = recognizer.getInputStream().getSourceName();
        result.append("<<<<<<<<<<<<<<<<<<<>>>>>>>>>>>>>>>>>>>>>\n");
        result.append(source + ":" + line+":" + charPositionInLine + ":\n");
        result.append(msg + "\n");
    }

    public StringBuilder getErrorString(){
        return result;
    }

}
