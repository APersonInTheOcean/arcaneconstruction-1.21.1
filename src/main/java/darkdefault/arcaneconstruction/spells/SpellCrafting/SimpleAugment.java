package darkdefault.arcaneconstruction.spells.SpellCrafting;

public class SimpleAugment implements Augment{

    @Override
    public String getSequence() {
        return "";
    }

    @Override
    public String getName() {
        return "";
    }

    @Override
    public int getManaCost() {
        return getSequence().length()*10;
    }
}
