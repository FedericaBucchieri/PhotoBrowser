# Photo Browser - Lab 2
## Extra features implemented
| Feature | How it is implemented | How to test it |
|---------|-----------------------|----------------|
| Customizable Pen sizes and Color for StrokeAnnotations | each StokeAnnotation has two variables that store the stroke width and the color of that specific StokeAnnotation. The current value of the selected color and selected stroke width are hold by the PhotoComponentModel. Each changes is detected by the PhotoBrowser class. | Colors and Stroke widths can be chosen in the main Menu.               |
| Customizable Font and font Color for TextAnnotations | each TextAnnotation has two variables that store the font and the color of that specific TextAnnotations. The current value of the selected color and selected font are hold by the PhotoComponentModel. Each changes is detected by the PhotoBrowser class. | Fonts and Colors can be chosen in the main Menu.               |
| Allow editing of already-entered text: backspacing | Thanks to a KeyReleased listeners, everytime the user releases the Backspace key, a character is removed from the current TextAnnotation instance hold by the PhotoComponentModel. Special checks have been implemented to allow to delete always the last character excluding Back Slashes. | When in flipped mode, start to write a text and then you can use the backspace as much as possible.               |
| Saving and loading data | on WindowOpened, the applications reads a file and thanks to Deserialization procedures, it reads a PhotoState instance that is then used to create the relative PhotoComponent. On WindowClosing, the current state of the PhotoComponent displayed is turned into a PhotoState instance that is the Serialized and stored into the same file, previously erased and subscribed. | Inside the application resources, the file "storage.txt" will contain all the stored file data. This features can be tested throughout different session of the application.|




