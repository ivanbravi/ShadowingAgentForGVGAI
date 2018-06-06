# ShadowingAgentForGVGAI
This repository contains the source code used for the experiments described in the paper "Shallow decision-making analysis in General Video Game Playing" by Ivan Bravi, Jialin Liu, Diego Perez-Liebana and Simon Lucas.

# Code
The GVGAI code that implements the Shadowing Agent is **coming soon** to the repo!
It will also contain the sample agents described in the paper except the Genetic Programming MCTS agent.

# Plots

The plots are relative to the five games described in the paper: : Aliens, Brainman, Camelrace, Racebet2, Zenpuzzle;
On the y axis you can find the main agents (the ones actually playing the game) while on the x axis there are the shadow agents. The label of the main agent also indicates between squared brackets the win percentage of that agent.

## Games
+ Aliens: a game loosely modelled on the Atari 2600’s Space Invaders, the agent on the bottom of the screen has to shoot the incoming alien spaceships from above avoiding their blasts;
+ Brainman: the objective of the game is for the player to reach the exit, the player can collect diamonds to get points and push keys into doors to open them;
+ Camelrace: the player, controlling a camel, has to reach the finish line before the other camels whose behaviour is part of the design of the game;
+ Racebet2: in the game there are few camels racing toward the finish line, each has a unique colour, in order to win the game the agent has to position the avatar on the camel with a specific colour;
+ Zenpuzzle: the level has two different types of floor tiles, one that can be always stepped on and a special type that can be stepped on no more than once. The agent has to step on all the special tiles in order to win the game;

## Pure Agreement plots
The paper provides a single example for the Pure Agreement plot, in the folder plots/PureAgreement you can find the plots for all the games described in the paper.
The brighter the colour the higher the agreement while the darker the lower.

## Decision Similarity plots
Similarly to the previous kind of plots, the paper contains just an example of Decision Similarity plot, all the plots can be found in the folder plots/DecisionSimilarity.
In the plots the darker the colour the higher the probability of selecting actions is similar, instead, the darker the more different. The measure uses the symmetric KL-Divergence metric.
