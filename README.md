

---

```markdown
# 🐉 Wumpus World: Audio Edition  
*A tactical, graphical implementation of the classic AI problem “Hunt the Wumpus,” built in Java.*

Wumpus World: Audio Edition is a fully interactive cave-exploration game featuring fog-of-war, directional movement, immersive sound design, and a complete graphical user interface. Use your senses, avoid deadly traps, and defeat the fearsome Wumpus!

---

## 🎮 Game Overview

You are an explorer entering a mysterious 4×4 cave system. Your mission:

- **Find the Gold**
- **Slay the Wumpus**

But be careful—hidden **Pits** will kill you instantly. You must rely on sensory clues like **Breeze** and **Stench** to survive and deduce what lies ahead.

---

## ✨ Features

### 🎭 Fog of War  
Unvisited rooms remain hidden until the player explores them.

### 🧭 Tactical Movement  
Tank-style directional movement:  
- Press an arrow key **once** to turn.  
- Press the **same key again** to move forward.

### 🔊 Audio System  
- Looping background music  
- Footstep, wall bump, arrow shooting, and gold pickup sounds  
- Special **Wumpus Cry** sound when the monster is slain

### 🧠 Sensory Feedback  
- **Breeze** → A Pit is adjacent  
- **Stench** → The Wumpus is adjacent

### 🎨 Custom Asset Management  
High-quality sprites for the player, monsters, environment tiles, UI icons, and more.

---

## 📂 Project Structure

Your project folder **must** follow this structure:



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
├── background.wav
├── arrow.wav
├── bump.wav
├── click.wav
├── coin.wav
├── die.wav
├── win.wav
└── wumpus-cry.wav



---

## 🚀 How to Run

### **Prerequisites**
- Java Development Kit (**JDK 8+**) installed

### **Steps**
```bash
# 1. Navigate to the project folder
cd WumpusProject

# 2. Compile the game
javac WumpusGame.java

# 3. Run it
java WumpusGame
````

---

## 🕹️ Controls

| Key        | Action                                        |
| ---------- | --------------------------------------------- |
| Arrow Keys | Turn or move forward (tank-style movement)    |
| Space Bar  | Shoot an arrow in the direction you're facing |
| R          | Restart the game                              |

---

## 📜 Rules & Logic

### **Movement**

* Move one square at a time
* Walking into a wall plays a **Bump** sound

### **Hazards**

* **Pits:** 2–3 placed randomly; stepping on one = *instant Game Over*
* **Wumpus:** Walking into its tile is lethal—unless it was just killed

### **Combat**

* You have **one arrow**
* Shooting hits the tile directly in front of you
* If the Wumpus is there, it dies and you hear its scream

### **Winning the Game**

To win, you must:

1. **Collect the Gold**, **AND**
2. **Kill the Wumpus**

Only then will the victory screen appear.

---

## 🛠️ Customization

You may replace any file inside the `assets/` folder with your own custom images or audio.
**Important:** Keep the filenames unchanged to avoid loading errors.

---

## 💡 Built With

* **Java Swing** for GUI
* **WAV-based audio system**
* Classic *Hunt the Wumpus* logic reimagined in a modern tactical format

---

