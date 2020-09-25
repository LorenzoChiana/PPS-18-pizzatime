
  

# PizzaTime

This project is a Battle Arena game with 2D fixed perspective graphics, written in Scala for educational purpose.

  

## Project description

Battle Arena game with 2D fixed perspective graphics. You play a character that moves inside a randomly-generated map, defeating enemies and completing levels.

The player moves from one fixed-dimension room to another as it advances levels. A (randomly generated) room can contain an arbitrary number of enemies, obstacles and power-ups; defeating all the enemies grants access through a door to the next level. Only a room at a time is visible on screen.

The goal of the game could be reaching a final room containing a special object that determines its end, or, alternatively, the completion of a certain number of rooms. In the former case, the probability of finding the special object is also random, but still dependent on some game conditions (like, for example, the reaching of a score threshold). However, the game could be played in a survival-like mode just to achieve a personal highest score related to the user, which is kept saved and shown in the ranking section of the menu. The random nature of the game implies an easily variable difficulty curve, making the gameplay depend also on luck, which can make it fun but also challenging at times.

The player's main weapon are bullets (tomatos) to be thrown at enemies; collectibles grant a score-related or life-related bonus.

The game allows to register different user profiles to save the game stats and compare them in a ranking.

  

## Continuous Integration

[![Build Status](https://travis-ci.com/giacomopasini5/PPS-18-pizzatime.svg?branch=master)](https://travis-ci.com/giacomopasini5/PPS-18-pizzatime)

  

## Getting Started

<Strong>Requirements</Strong>

In order to play the game you need to have any version of Java between 8 and 14 installed and Scala 2.13.2.

  

<Strong>How to run</Strong>

After the requirements are met, to play simply run the client .jar file provided with the latest release.

```

java -jar pps-18-pizzatime-1.0.jar

```

  

## How to play

Once you've launched the .jar file, the menu will be shown, from where you can start the game, look at the ranking or change the settings (user profile and difficulty).
Once you've started the game, the game map (arena) will show up, populated with all the entities for the first level.

You pick up collectibles by walking on them; obstacles are not transitables.
Once all the enemies in a room are defeated, a (randomly positioned) door will appear, granting access to the next level. The map size, all the entities' generation, and also the increase in difficulty with level progression, are related to the chosen difficulty.

<Strong>Commands</Strong>

WASD: Move around
Arrows: Shoot bullets

  

## Releases

It's possible to download the source code and all release executable jars at the following page: https://github.com/giacomopasini5/PPS-18-pizzatime/releases

  

## Sprint task board

Sprint task board is available at the following page: https://trello.com/b/gEC0EDm0

  

## Team members

Lorenzo Chiana (lorenzo.chiana@studio.unibo.it)

Meshua Galassi (meshua.galassi@studio.unibo.it)

Giada Gibertoni (giada.gibertoni@studio.unibo.it)

Giacomo Pasini (giacomo.pasini5@studio.unibo.it)
