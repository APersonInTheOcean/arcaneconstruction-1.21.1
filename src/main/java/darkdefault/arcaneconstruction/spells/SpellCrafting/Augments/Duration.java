package darkdefault.arcaneconstruction.spells.SpellCrafting.Augments;

import darkdefault.arcaneconstruction.spells.SpellCrafting.Augment;
import darkdefault.arcaneconstruction.spells.SpellCrafting.SimpleAugment;

public class Duration extends SimpleAugment {
    @Override
    public String getSequence() {
        return "ci";
    }

    @Override
    public String getName() {
        return "Duration";
    }
}
