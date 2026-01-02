package darkdefault.arcaneconstruction.spells.SpellCrafting;

import java.util.ArrayList;
import java.util.Objects;

public class SpellModule {

    Shape moduleShape;
    ArrayList<Augment> moduleAugments = new ArrayList<>();

    SpellModule(String sequence){

        setShapeFromString(sequence);
        setAugmentsFromString(sequence);
    }

    public static Boolean isValidShape(String sequence){
        for (Shape s: SpellCraftingRegistry.getRegisteredShapes()) {
            if (Objects.equals(s.getSequence(), sequence)) {
                return true;
            }
        }
        return false;
    }
    public static Shape getShapeType(String sequence){
        for (Shape s: SpellCraftingRegistry.getRegisteredShapes()) {
            if (Objects.equals(s.getSequence(), sequence)) {
                return s;
            }
        }
        return null;
    }
    public static Boolean isValidAugment(String sequence){
        for (Augment a: SpellCraftingRegistry.getRegisteredAugments()) {
            if (Objects.equals(a.getSequence(), sequence)) {
                return true;
            }
        }
        return false;
    }
    public static Augment getAugmentType(String sequence){
        for (Augment a: SpellCraftingRegistry.getRegisteredAugments()) {
            if (Objects.equals(a.getSequence(), sequence)) {
                return a;
            }
        }
        return null;
    }
    
    private void setShapeFromString(String sequence) {
        StringBuilder stringBuilder = new StringBuilder(sequence);
        for (int i = sequence.length()-1; i>= 0; i--){
            stringBuilder.setLength(i+1);

             if(isValidShape(stringBuilder.toString())){
                this.moduleShape = getShapeType(stringBuilder.toString());

                break;
            }
        }
    }


    public Shape getShape(){
        return this.moduleShape;
    }

    public String removeShapeCharInString(String sequence, Shape shape){

        return sequence.substring(shape.getSequence().length());
    }

    private void setAugmentsFromString(String sequenceWithShape){
        String sequence = removeShapeCharInString(sequenceWithShape,this.moduleShape);
        while (!sequence.isEmpty()) {
            StringBuilder stringBuilder = new StringBuilder(sequence);
            boolean found = false;

            // Try the longest prefix first
            for (int i = sequence.length(); i > 0; i--) {
                stringBuilder.setLength(i); // keep first i characters

                if (isValidAugment(stringBuilder.toString())) {
                    // Found the longest valid augment at the start
                    this.moduleAugments.add(getAugmentType(stringBuilder.toString()));

                    // Remove the matched augment from the sequence
                    sequence = sequence.substring(i);
                    found = true;
                    break; // restart while loop with remaining sequence
                }
            }

            if (!found) {
                // If no valid augment found at all, skip the first character to avoid infinite loop
                sequence = sequence.substring(1);
            }
        }

    }

    public ArrayList<Augment> getAugments(){
        return this.moduleAugments;
    }

    public int getManaCost(){
        int mana = 0;
        mana += getShape().getManaCost();
        for (Augment a : this.getAugments()) {
            mana += a.getManaCost();
        }

        return mana;
    }

    public static class SpellCraftingRegistry {

        private static final ArrayList<Shape> shapes = new ArrayList<>();
        private static final ArrayList<Augment> augments = new ArrayList<>();

        public static void registerShape(Shape shape){
            if (!shapes.contains(shape)){
                shapes.add(shape);

            }
        }
        public static void registerAugment(Augment augment){
            if (!augments.contains(augment)){
                augments.add(augment);

            }
        }

        public static ArrayList<Shape> getRegisteredShapes(){
            return shapes;
        }
        public static ArrayList<Augment> getRegisteredAugments(){
            return augments;
        }
    }
}
