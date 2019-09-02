package algat.model.lesson.block;

import javafx.scene.Node;

/**
 * Assembles the JavaFx hierarchy graphically representing this content
 * and return the base Node of the said hierarchy
 */
public interface BlockContent {
    Node assemble();
}
