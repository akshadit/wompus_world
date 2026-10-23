# 🐉 Wumpus World: Audio Edition

[![Java](https://img.shields.io/badge/Java-17%2B-ED8B00?style=flat&logo=openjdk)](https://www.oracle.com/java/)
[![GUI](https://img.shields.io/badge/GUI-Java_Swing-orange)](https://docs.oracle.com/javase/tutorial/uiswing/)
[![Audio](https://img.shields.io/badge/Audio-Spatial_WAV_Engine-blue)](https://docs.oracle.com/javase/tutorial/sound/)
[![License](https://img.shields.io/badge/License-MIT-green.svg)](LICENSE)

> **A tactical, graphical implementation of the classic Artificial Intelligence benchmark "Hunt the Wumpus" featuring fog-of-war, directional tank movement, and immersive spatial audio.**

---

## 🎮 Game Overview

You are an intrepid explorer entering a dangerous $4 \times 4$ subterranean cave system. Your objective:

1. 💰 **Recover the Hidden Gold**
2. 🏹 **Slay the Dreaded Wumpus**

Beware! Bottomless **Pits** will swallow you instantly, and the Wumpus lurks in the shadows. To survive and prevail, you must utilize sensory cues—such as feeling a **Breeze** or smelling a putrid **Stench**—to logically deduce the hazards in adjoining chambers.

---

## ✨ Features

- 🎭 **Fog of War**: Unvisited cave chambers remain shrouded in darkness until you boldly step into them.
- 🧭 **Tactical Movement**: Authentic tank-style navigation:
  - Press an arrow key **once** to pivot and face that direction.
  - Press the **same arrow key again** to step forward into the chamber.
- 🔊 **Dynamic Audio Engine**:
  - Atmospheric looping background cave music.
  - Spatial audio cues: footsteps, wall collisions, arrow whooshes, coin pickups, and the terrifying **Wumpus Death Cry**.
- 🧠 **Sensory Deduction System**:
  - 💨 **Breeze** $\rightarrow$ Indicates a bottomless Pit is in an adjacent chamber.
  - 🦨 **Stench** $\rightarrow$ Warns that the ravenous Wumpus is nearby.
  - ✨ **Glitter** $\rightarrow$ You are standing directly on the chamber containing the Gold!

---

## 🎯 Controls & Mechanics

| Key / Action | Function |
| :--- | :--- |
| **Arrow Keys** (Single Tap) | Turn player to face North, South, East, or West |
| **Arrow Keys** (Double Tap) | Advance forward into the faced room |
| **Spacebar** | Shoot your single enchanted arrow in the direction you are facing |
| **G** | Pick up Gold when standing on a glittering chamber |
| **C** | Toggle on-screen Controls cheat sheet |

> ⚠️ **Note:** You carry only **one arrow**! If you shoot and miss the Wumpus, your arrow is lost forever.

---

## 🏆 Victory Conditions

To achieve true victory, you must:
1. **Retrieve the Gold** (`G` key when standing on the gold tile), **AND**
2. **Slay the Wumpus** by shooting an arrow straight into its chamber.

---

## 🚀 How to Compile & Play

### Prerequisites
- **Java Development Kit (JDK)** 17 or higher
- Working sound output

### Compile & Run
```bash
# Clone the repository
git clone https://github.com/akshadit/wompus_world.git
cd wompus_world

# Compile the Java source code
javac WumpusGame.java

# Launch the game
java WumpusGame
```

---

## 🎨 Assets & Customization

The game assets are structured modularly inside the `assets/` directory:
- `assets/audio/`: Sound effects (`arrow.wav`, `bump.wav`, `coin.wav`, `win.wav`, `game-over.wav`, `background.wav`)
- `assets/images/`: Sprites (`floor.png`, `gold.png`, `floor_gold.png`, `arrow-icon.png`, `controls.png`)

You can replace any asset with your own custom artwork or sound, keeping the original filenames intact.

---

## 📄 License

Distributed under the MIT License.