<div align="center">
  <div>
    <img src='https://takutotofu.s-ul.eu/kDrJCxzY' alt='thermodynamics'/>
  </div>
  <h3 align="center">A JavaFX School Project</h3>

  <div align="center">
     This project has been developped during my second year at <a href='https://www.helb-prigogine.be/' target="_blank"><b>HELB - Ilya Prigogine</b></a> 2023-2024.
  </div>
  </br>
</div>

## 📋 <a name="table">Table of Contents</a>

1. 👋 [Introduction](#introduction)
2. 🚶‍➡️ [Basic Features](#basic-features)
    - [Control Interface](#control-interface)
    - [Cell Types](#cell-types)
3. ☀️ [ThermoSystem Types](#thermosystem-types)
    - [ThermoSystemManual](#thermosystemmanual)
    - [ThermoSystemTarget](#thermosystemtarget)
4. 📄 [Design Patterns Analysis](#design-patterns-analysis)
5. 📉 [Limitations](#limitations)
6. 🧠 [Conclusion](#conclusion)
7. 🖥️ [Installation](#installation)

## 👋 <a name="introduction">Introduction</a>

In the Java IV programming course, we were tasked with creating a project using the JavaFX Framework on a Maven base, adhering to various software architecture principles such as GRAS and GoF design patterns. This application models a thermodynamic system where heat exchanges occur within a simulation, featuring a rectangular space filled with cells, including heat sources and dead cells. A control interface was implemented to interact with the system.

## 🚶‍➡️ <a name="basic-features">Basic Features</a>

### Control Interface

Implemented using JavaFX and following the MVC design pattern:
- **ThermoView.java**: Main view of the application.
- **ThermoController.java**: Controller managing user interactions.
- **ThermoModel.java**: Represents the underlying data structure.

#### Interface Overview
- **Grid (4x5 cells)**: Displays temperature and color-coded heat levels. Heat sources and dead cells are distinguishable.
- **Cell Configuration**: Clicking a cell opens a configuration interface to set temperature or mark as a dead cell.
- **Control Panel**: 
    - **Heat Source Management**: Activate/deactivate heat sources.
    - **Simulation Controls**: Start, pause, and reset the simulation.
    - **Status Display**: Shows time, cost, external temperature, and average temperature.

### Cell Types

- **Cell**: Base class representing a system cell.
    - Attributes: `x`, `y`, `temperature`, `id`
    - Implements: `Observable`
- **HeatSourceCell**: Inherits from `Cell`, represents a heat source.
    - Attributes: `heatTemperature`, `isActivated`
- **DeadCell**: Inherits from `Cell`, represents a dead cell with fixed attributes.

## ☀️ <a name="thermosystem-types">ThermoSystem Types</a>

### ThermoSystemManual

Represents the manual heating mode:
- **Simulation Method**: Recalculates each cell's temperature based on neighboring cells and external temperature.

### ThermoSystemTarget

Represents the target heating mode aiming for an average temperature of 20°C:
- **Simulation Method**: Inherits from `ThermoSystemManual`, adjusts heat sources to reach the target temperature.

## 📄 <a name="design-patterns-analysis">Design Patterns Analysis</a>

The application is built on an MVC architecture for maintainability and clarity:
- **Observer Pattern**: Used for updating cell temperatures and reflecting changes in the view.
- **Strategy Pattern**: Allows switching between different heating modes (`ThermoSystemManual` and `ThermoSystemTarget`).
- **Factory Pattern**: Utilized in `CellFactory.java` for creating cell instances.

## 📉 <a name="limitations">Limitations</a>

- **Temperature Input**: Heat source temperatures must be positive and below 100°C to avoid application crashes. Error messages guide the user for valid inputs.

## 🧠 <a name="conclusion">Conclusion</a>

This project has been a valuable learning experience in applying design patterns and developing Java applications with graphical interfaces using JavaFX. The implementation adheres to MVC, Observer, Strategy, and Factory patterns, ensuring a robust and maintainable application structure. Future work could further enhance the application's functionality and stability.

## <a name="installation">🖥️ Installation</a>

Download the main archive and unzip the `HELBThermo`.
<div align='center'>  
  <img src='https://takutotofu.s-ul.eu/Ylhza7xj' alt='tuto1' />
</div>

### 🪟 For Windows

You need to download <a href='https://gluonhq.com/products/javafx/'><b>JavaFX library</b></a>, extract the folder into `C:\Program Files\Java` (create folder if needed) and
edit `run.bat` file to set the path of your JavaFX lib folder :

<div align='center'>
  <img src='https://takutotofu.s-ul.eu/fTJeZWxE' alt='tuto2' />
</div>

### 🐧 For Linux

You need to run this command :

```bash
sudo apt-get install openjfx
````

or download it on this <a href='https://gluonhq.com/products/javafx/'><b>site</b></a> and extract it into `${JAVA_HOME}/lib/`.

So use the `run.bash` instead.
