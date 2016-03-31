grammar Fsmlp;
import Fsmll;

fsm : statedecl+ EOF ;
statedecl : 'initial'? 'state' stateid '{' transition* '}' ;
transition : event ('/' action)? ('->' stateid)? ';' ;
stateid : NAME ;
event : NAME ;
action : NAME;
