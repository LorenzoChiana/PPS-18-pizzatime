
  

# PizzaTime

This porject is a battle arena game with 2D fixed view graphics written in Scala for educational purpose.

  

## Project description

Gioco di tipo battle arena con grafica 2D a visuale fissa, in cui il giocatore comanda un personaggio che si muove in una mappa generata casualmente, comprendente nemici ed eventuali ostacoli.

Il personaggio si muove all'interno di più stanze delle stesse dimensioni, avanzando da una stanza all'altra. Una stanza, generata casualmente, può comprendere un arbitrario numero di nemici, ostacoli, oggetti collezionabili e una porta; l'eliminazione di tutti i nemici presenti in una stanza permette l'attraversamento della porta verso la prossima stanza. Solo una stanza alla volta è visibile a schermo.

Lo scopo del gioco può essere il raggiungimento di una certa stanza che contiene un oggetto speciale che determina la fine della partita oppure, in una modalità alternativa, il superamento di un certo numero di stanze. Nel primo caso, probabilità di trovare la stanza finale è anch'essa casuale, ma può aumentare con il raggiungimento di certe condizioni (ad esempio il superamento di una soglia di punteggio o il ritrovamento di oggetti speciali). Un'ulteriore modalità potrebbe essere una partita senza limiti, dove l'obiettivo è semplicemente il miglioramento del proprio record di punteggio, visualizzato in una classifica. La natura casuale del gioco può determinare una curva di difficoltà molto variabile, visto il ruolo che la fortuna ha nel trovare la stanza finale o semplicemente una stanza più semplice da completare.

Il personaggio ha a disposizione come arma principale dei proiettili (pizze) da lanciare ai nemici; certi oggetti collezionabili possono rappresentare ulteriori armi oppure aiuti per proteggersi dai nemici.

Il gioco permette di registrare diversi profili con associate tutte le statistiche delle partite del giocatore, per poterle comparare con gli altri e in particolare per poter creare una classifica comprendente i punteggi di ognuno.

  

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

Una volta avviato il .jar vi si presenterà un menù, dove vi è; la possibilità di modificare le impostazioni di gioco accessibili tramite pulsante "Settings"; la possibilità di guardare la classifica di gioco tramite il pulsante "Player rankings"; e infine la possibilità di giocare tramite "Start game".

Una volta avviato il gioco verrete catapultati in un'arena di gioco con diversi ostacoli, bonus da collezionare e nemici da sconfiggere.

Per muovervi vi basterà usare i tasti WASD:

- W = movimento in su;

- S = movimento in giù;

- A = movimento a sinistra;

- D = movimento a destra.

I nemici potranno essere sconfitti sparandogli pomodori addosso, questo possibile alle freccie direzionali (ogni freccia corrisponderà ad uno sparo in tale direzione).

I vari bonus sparsi per la mappa si possono raccogliere camminandoci sopra e comporteranno un incremento della vita o del punteggio.

Una volta uccisi tutti i nemici si aprirà una porta in una posizione casuale tra i muri dell'arena di gioco, il passaggio in questa comporterrà l'inizio del livello successivo.

  

## Releases

It's possible to download the source code and all release executable jars at the following page: https://github.com/giacomopasini5/PPS-18-pizzatime/releases

  

## Sprint task board

Sprint task board is available at the following page: https://trello.com/b/gEC0EDm0

  

## Team members

Lorenzo Chiana (lorenzo.chiana@studio.unibo.it)

Meshua Galassi (meshua.galassi@studio.unibo.it)

Giada Gibertoni (giada.gibertoni@studio.unibo.it)

Giacomo Pasini (giacomo.pasini5@studio.unibo.it)