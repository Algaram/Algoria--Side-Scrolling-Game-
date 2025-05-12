# 🚀 Algoria

*Algoria* is a Java-based 2D space shooter game where you navigate through a dangerous galaxy, collecting valuable items while dodging deadly obstacles. The fate of the universe is in your hands!

---

## 🌌 About

In *Algoria*, you control a spaceship that must fly through space, collecting valuable items and avoiding hazards. The game features a scrolling environment where new entities continuously appear from the right side of the screen.

---

## ✨ Features

- Dynamic scrolling space environment  
- Multiple types of collectibles, obstacles, and power-ups  
- Projectile system with mouse-aim functionality  
- Special effects for explosions, shields, and targeting  
- Intuitive controls for all skill levels  

---

## 🎮 How to Play

### 🎯 Game Objective

- Reach a score of **400 points** to win  
- Preserve your **HP** by avoiding obstacles  
- Collect and strategically use **power-ups**  
- Use **projectiles** to destroy obstacles from a distance  

### 🕹️ Controls

| Key            | Action                                        |
|----------------|-----------------------------------------------|
| `Arrow Keys`   | Move the spaceship (Up, Down, Left, Right)    |
| `Space`        | Fire projectile straight ahead                |
| `Mouse Click`  | Fire projectile toward clicked location       |
| `F`            | Activate Shield power-up (if available)       |
| `G`            | Activate Magnet power-up (if available)       |
| `P`            | Pause/Unpause game                            |
| `Enter`        | Advance past splash screen                    |
| `Escape`       | Quit game                                     |
| `D`            | Toggle debug mode                             |
| `-`            | Decrease game speed                           |
| `=`            | Increase game speed                           |

---

## 🧱 Game Elements

### 💎 Collectibles

- **Regular Point Orbs** – Bright blue; increase score by 20  
- **HP Point Stars** – Golden stars; increase score by 20 and restore 1 HP  

### ☄️ Obstacles

- **Blackholes** – Reduce HP by 1  
- **Electrical Fields** – Reduce HP by 1 and score by 50  
- **Fireballs** – Move at variable speeds; avoid on sight  

### 🔋 Power-Ups

- **Shield** – Temporarily invulnerable  
- **Magnet** – Attracts nearby collectibles  
- **Score Boost** – Doubles all point values temporarily  

---

## 💻 Installation

### 🔧 Prerequisites

- Java Development Kit (JDK) 8 or higher  
- Git (optional)  

### ⚙️ Setup Instructions

```bash
# 1. Clone the repository
git clone https://github.com/yourusername/algoria.git
cd algoria

# 2. Compile the game
javac *.java

# 3. Run the game
java SSGLauncher
