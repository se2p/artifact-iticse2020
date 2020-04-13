# Replication Package

This is the replication package for the paper "Common Bugs in Scratch Programs".

This package holds the raw data produced by LitterBox and our manual evaluation, as well as the scripts used to
create the result table presented in our paper.

## Raw data

- 'data/classification' contains a file with the ids of classified projects and the result for each project, and a summary file

- 'data/projects' contains two files with the ids of projects used to answer RQ1 with LitterBox

- 'data/results' contains the results of LitterBox

Due to the size of our data set we are not able to host it here on GitHub. If you are interested in our dataset
we kindly ask you to contact us at fraedric@fim.uni-passau.de

## Reproducing the results table

To reproduce the results table execute the evaluation script with:

'python3 evaluation.py'

## Reproducing the raw results from the data set

To reproduce the raw results (i.e. evaluation of the individual projects) first build LitterBox as follows:

```
cd software/LitterBox
mvn compile
mvn install
```

Afterwards you can execute LitterBox to produce the results as follows:

```
java -jar target/Litterbox-1.0.jar -projectlist ../../data/data/projects/no_remix.csv \
    -projectout ../../dataset \
    -output <path to result file>
    -detectors \
    ambCustBlSign,ambParamName,ambParamNameStrct,cllWithoutDef,compLit,custBlWithForever,custBlWithTerm\
    ,endlRec,exprTouchColor,foreverInLoop,illParamRefac,messNeverSent,messNeverRec,mssBackdrSwitch,mssCloneCll\
    ,mssCloneInit,mssEraseAll,mssLoopSens,mssPenDown,mssPenUp,mssTerm,noWorkScript,orphParam\
    ,paramOutScope,posEqCheck,recClone,sameVarDiffSprite,stuttMove
```
