# Simulink Viewer 
The aim of this project is to develop a software tool that can read Simulink MDL files and display their contents in a user-friendly way using a Java-based graphical user interface (GUI). Simulink is a popular simulation and modeling environment used in various industries, including automotive, aerospace, and electronics. The Simulink MDL files contain the model information, which is used to simulate and analyze the system behavior.
The tool allows to load Simulink MDL files and view their contents in a hierarchical structure, navigate through the model components and see their properties and connections.
## Contributors
|**Name**| **ID** | 
|--|--|
| Adham Khaled Abdelmaqsoud Ali | 2000066 |
| Ali Mohsen Yehia Ateya | 2000289 |
| Maged Mohamed abdelghaffar | 2001951 |
| Ali Mahmoud Abdulhalim Mohamed | 2000469 |
| Ahmed Mohamed Abd El Rehem | 1808471 |
## Notes to Run the Program
* You can just download the Run folder and run the program using SimulinkViewer.bat batch file.
* You can download the java files and run the program using Run.bat batch file.
* You can download the java files and run the program using any IDE or using cmd.
## Key Features
* The program takes input .mdl file and displays its content in a user-friendly interface.
![image](https://i.imgur.com/EowB938.png)
* You can zoom in or out in the diagram and drag the screen to different parts.</br>
![image](https://i.imgur.com/L8OLrHH.png)
* You can save the diagram as a png photo on your device.</br>
![image](https://i.imgur.com/6p9yr1O.png)
* You can open multiple taps at the same time and navigate through them.</br>
![image](https://i.imgur.com/bSG6btz.png)
* Support to various Simulink block types.</br>
![image](https://i.imgur.com/t2NVHZm.png)
## Features to be added & Bugs to be fixed
* Adding help screen for new users.</br>
![image](https://i.imgur.com/DwUDpvI.png)
* calculating the y position of ports precisely to be similar to Simulink.</br>
![image](https://i.imgur.com/Z12Fr0d.png)
* Highlighting the blocks that are selected with blue border like Simulink.</br>
![image](https://i.imgur.com/XUa4EsH.png)
* Adding auto fit to screen and center the diagram upon opening the file.
* Adding support to more block types and to specific changes in some blocks.
#### Side problem :
when using javafx 8 the diagram gets drawn over the toolbar when it is dragged, this problem is easily solved in javafx 11 and newer versions, but we couldn't create the jar file with the newer version of javafx so this problem still remains.</br>
| ![image](https://i.imgur.com/N6gd49a.png) | ![image](https://i.imgur.com/jKSZZM6.png) |
|--|--|
| with javafx 8 | with javafx 19 |
## Hierarchy of the Program
#### the program is divided into three main packages:
* blocksPackage
* linesPackage
* userInterfacePackage
### Each one of them consists of some classes as follows:
#### blocksPackage:
* Block class : this class is used to store all the info about each block in the diagram such as block type, name, position, etc..., also it has a draw function that takes argument Pane and draws the block on it.
* BuildBlocks class : this class has one method called parse, it takes argument an xml file that contains the info of the blocks, it extracts this info and then construct and return an array of type Block.
#### linesPackage:
* Arrow class : this class is used to store all the info about each line in the diagram such as source, destination, branches, etc..., it also has a draw function that takes argument Pane and draws the line with its branches recursively on it.
* BuildLines class : this class also has a method called parse that takes argument an xml file, and it calls a recursive method createLine that recursively create lines and branch lines inside each line. then the parse method returns an array of type Arrow that contains all lines in the xml file.
#### userInterfacePackage:
* ButtonAndLabel class : this class extends from VBox, it is used to make an image and label in one element easily.
* CustomMenuBar class : this class extends from MenuBar, it is used to make a menu bar with the style and layout needed for the program.
* CustomToolBar class : this class extends from ToolBar, it is also used to make a tool bar with the style and layout needed for the program.
* CustomCanvas class : this class extends from Pane, this class is used to create a different pane for each file the user opens to store each diagram in a separate object.
### The Main class:
* This is the class that extends from Application, this is where the main flow of the program is written using all the classes mentioned before. 
