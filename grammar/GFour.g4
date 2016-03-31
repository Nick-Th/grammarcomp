grammar GFour;

gfile   : gdecl gimp prule* EOF ;
gdecl   : 'grammar' NAME ';' ;
gimp    : 'import' NAME ';' ;
prule   : plhs ':' prhs ';' ;
plhs    : NONTERMINAL;
prhs    : (nt
            |option
            |plus
            |star
            |tok
            )+ ;

tok     : TOKEN;

option  : '(' nt ')?'| nt '?' ;

plus    : '(' nt ')+'| nt '+' ;

star    : '(' nt ')*'| nt '*' ;

nt      : (n|t)+ ;

n       : NONTERMINAL;

t       : TERMINAL;

TERMINAL : '\'' .+? '\'' ;
TOKEN : ('A'..'Z')+ ;
NONTERMINAL : ('a'..'z')+  ;
NAME : ('A'..'Z') ('a'..'z')* ;
WS : [ \t\n\r]+ -> skip ;
