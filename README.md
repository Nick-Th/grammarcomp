# Comparison of systematically mutated CFG
The Idea behind this project is find out which differences in CFG's can be detected. 
For this reason the FSML-Grammar is systematically mutated. 
* * -> +
* * -> ?
* + -> *
* + -> ?
* ? -> *
* ? -> +
* terminals -> 

After this a parser for FSML is created with Antlr4. And from each mutated grammar, testdata for branch coverage and production 
coverage is generated.
The testdata is parsed by the FSML-parser and each error that occurs while parsing is written to a log file.

## Example
Clone the Repository to your machine.

`git clone https://github.com/Nick-Th/grammarcomp.gi`

Change your working directory to grammarcomp.

`cd grammarcomp`

Build and Execute the project by running the compare bash script

`./compare`

Maybe you have to make the script executable by typing

`chmod +x compare`



