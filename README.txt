# Algoria

Algoria is a Java-based 2D space shooter game where you navigate through a dangerous galaxy, collecting valuable items while dodging deadly obstacles. The fate of the universe is in your hands!

## 🌌 About

In Algoria, you control a spaceship that must navigate through space, collecting valuable items while avoiding dangerous space obstacles! The game features a scrolling environment where new entities continuously appear from the right side of the screen.

## ✨ Features

- Dynamic scrolling space environment
- Multiple types of collectibles, obstacles, and power-ups
- Projectile system with mouse-aim functionality
- Special effects for explosions, shields, and targeting
- Intuitive controls for all skill levels

## 🎮 How to Play

### Game Objective
- 🎯 Reach a score of 400 points to win
- ❤️ Preserve your HP by avoiding dangerous obstacles
- 🔮 Collect and strategically use power-ups
- 💥 Use projectiles to destroy obstacles from a distance

### Controls

| Key | Action |
|-----|--------|
| **Arrow Keys** | Move the spaceship (Up, Down, Left, Right) |
| **Space** | Fire projectile straight ahead |
| **Mouse Click** | Fire projectile toward clicked location |
| **F** | Activate Shield power-up (if available) |
| **G** | Activate Magnet power-up (if available) |
| **P** | Pause/Unpause game |
| **Enter** | Advance past splash screen |
| **Escape** | Quit game |
| **D** | Toggle debug mode |
| **-** | Decrease game speed |
| **=** | Increase game speed |

## 🚀 Game Elements

### Collectibles
- **Regular Point Orbs**: Bright blue orbs that increase score by 20 points
- **HP Point Stars**: Golden stars that increase score by 20 points and restore 1 HP

### Obstacles
- **Blackholes**: Dark vortexes that reduce HP by 1 on collision
- **Electrical Fields**: Pulsating energy fields that reduce HP by 1 and decrease score by 50 points
- **Fireballs**: Flaming projectiles with variable speeds that must be avoided

### Power-Ups
- **Shield**: Creates a protective barrier making the player temporarily invulnerable
- **Magnet**: Creates a force field that attracts nearby collectibles
- **Score Boost**: Emits a golden aura that doubles all point values

## 💻 Installation

### Prerequisites
- Java Development Kit (JDK) 8 or higher
- Git (optional)

### Setup Instructions

1. Clone the repository:
   ```
   git clone https://github.com/yourusername/algoria.git
   cd algoria
   ```

2. Compile the game:
   ```
   javac *.java
   ```

3. Run the game:
   ```
   java SSGLauncher
   ```

## 🧠 Strategy Tips

1. **Power-Up Management**: Save power-ups for challenging situations rather than using them immediately
2. **Distance Combat**: Use projectiles to clear paths through obstacle fields
3. **Shield Priority**: The shield is most valuable when navigating through dense obstacle clusters
4. **Magnet Efficiency**: Activate the magnet when multiple collectibles are nearby but difficult to reach
5. **Cooldown Awareness**: Keep track of your projectile cooldown to avoid being defenseless

## 📁 Project Structure

```
algoria/
├── src/
│   ├── SSGLauncher.java       # Entry point
│   ├── RamirezGame.java       # Main game mechanics
│   ├── SimpleGame.java        # Base game logic
│   ├── SSGEngine.java         # Engine framework
│   ├── Entity.java            # Base entity class
│   ├── Player.java            # Player controls
│   ├── Collect.java           # Collectible items
│   ├── SpecialCollect.java    # Enhanced collectibles
│   ├── Avoid.java             # Obstacle entities
│   ├── SpecialAvoid.java      # Dangerous obstacles
│   ├── PowerUp.java           # Power-up mechanics
│   ├── Projectile.java        # Projectile system
│   └── Window.java            # Graphics rendering
├── media_files/               # Game assets
├── LICENSE                    # Project license
└── README.md                  # This file
```

## 🎨 Credits

### Assets Used
- **Background**: [Free Planets in Space Pixel Game Background Pack](https://craftpix.net/freebies/free-planets-in-space-pixel-game-background-pack/)
- **VFX 1**: [Particle FX by Ragnapixel](https://ragnapixel.itch.io/particle-fx)
- **VFX 2**: [VFX Free Pack by CodeManu](https://codemanu.itch.io/vfx-free-pack)
- **Spaceships**: [2D Space Game Pack by Gisha](https://gisha.itch.io/2d-space-game-pack)

## 📄 License

This project is licensed under the MIT License - see the LICENSE file for details.
