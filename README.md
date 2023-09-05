# CSCI435-skillstest
To use this program, first put the XML and PNG files in the same folder as Tool.java. Open Command Line and navigate to the folder containing Tool.java.
To compile, enter the command 'javac Tool.java' in the command prompt.

To run the program, enter 'java Tool <XML file name> <PNG file name>' as shown here:
java Tool com.apalon.ringtones.xml com.apalon.ringtones.png
The annotated screenshot will be saved to the same folder as the rest of the files.


My solution to this assignment was to parse the XML file and identify which components were able to be interacted with by the user. I did this by using the "clickable" attribute in each of the nodes representing a component in the application. I then identified the coordinate pair bounds for each of these components and placed them sequentially in a List. I chose not to use a Map for the coordinate pairs because there was no way to guarantee that two components would not have the same upper left coordinates. After identifying the coordinates for the bounds of each leaf-level component, I used these coordinate pairs to draw the yellow rectangles highlighting them.
