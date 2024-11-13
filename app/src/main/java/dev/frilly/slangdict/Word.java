package dev.frilly.slangdict;

import java.util.ArrayList;
import java.util.List;

/**
 * A data class for the "word" object.
 */
public class Word {

    public String word = "null";
    public List<String> definition = new ArrayList<>();
    public boolean locked = false;
    public boolean favorite = false;

}
