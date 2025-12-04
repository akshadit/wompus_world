Wumpus World: Audio Edition

A tactical, graphical implementation of the classic AI problem "Hunt the Wumpus," built in Java. This version features fog-of-war exploration, directional movement, sound effects, and a complete graphical user interface.

🎮 Game Overview

You are an explorer entering a dark cave (4x4 grid). Your mission is twofold:

Find the Gold hidden somewhere in the cave.

Kill the Wumpus, a fearsome monster lurking in the dark.

But beware! The cave is filled with Pits that will kill you instantly. You must use your sensors to deduce where the dangers lie.

✨ Features

Fog of War: Unvisited areas are hidden by walls until you explore them.

Tactical Movement: "Tank controls" style—press an arrow key once to turn, press again to move.

Audio System:

Background music loops during gameplay.

Sound effects for footsteps, wall bumps, shooting arrows, and collecting gold.

A unique "Wumpus Cry" when the monster is slain.

Sensors:

Breeze: Indicates a Pit is in an adjacent square.

Stench: Indicates the Wumpus is in an adjacent square.

Asset Management: Custom sprites for directional player movement and environment.

📂 Project Structure

To run this game, your project folder must look exactly like this:

WumpusProject/
│
├── WumpusGame.java          # Main game source code
├── README.md                # This file
│
└── assets/                  # Resource folder
    ├── images/
    │   ├── player_facing_to_up.png
    │   ├── player_facing_to_down.png
    │   ├── player_facing_to_left.png
    │   ├── player_facing_to_right.png
    │   ├── wumpus.png
    │   ├── gold.png
    │   ├── hole.png
    │   ├── floor.png
    │   ├── floor_gold.png
    │   ├── wall.png
    │   ├── game_over.jpg
    │   ├── controls.png
    │   ├── arrow-icon.png
    │   └── score-icon.png
    │
    └── audio/
        ├── background.wav   # Loops during game
        ├── arrow.wav        # Shooting sound
        ├── bump.wav         # Hitting a wall
        ├── click.wav        # Movement step sound
        ├── coin.wav         # Gold pickup
        ├── die.wav          # Game over sound (pit/eaten)
        ├── win.wav          # Victory sound
        └── wumpus-cry.wav   # Death sound for Wumpus


🚀 How to Run

Prerequisites

Java Development Kit (JDK) installed (Version 8 or higher).

Steps

Clone or Download this repository.

Open your terminal or command prompt.

Navigate to the project folder.

Compile the code:

javac WumpusGame.java


Run the game:

java WumpusGame


🕹️ Controls

Key

Action

Arrow Keys

Turn (if facing different direction) or Move (if facing forward).

Space Bar

Shoot Arrow in the direction you are facing.

R

Restart the game.

📜 Rules & Logic

Movement: You can only move one square at a time. Hitting a wall plays a "Bump" sound.

Hazards:

Pits: 2 to 3 pits are randomly placed. Walking into one is Game Over.

Wumpus: There is 1 Wumpus. Walking into its square is Game Over (unless you just killed it).

Shooting: You have 1 Arrow. Shooting it checks the square directly in front of you. If the Wumpus is there, it dies (you hear a scream).

Winning: You must Collect the Gold AND Kill the Wumpus to trigger the victory screen.

🛠️ Customization

You can swap out any image or audio file in the assets folder with your own, provided you keep the filenames exactly the same.

Created with Java Swing.