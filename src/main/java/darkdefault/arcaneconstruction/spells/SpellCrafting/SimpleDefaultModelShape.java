package darkdefault.arcaneconstruction.spells.SpellCrafting;

public abstract class SimpleDefaultModelShape extends DefaultModelShape {

    //Simple just means that it doesn't interrupt any on... events.

    @Override
    public boolean shouldInterruptOnBlockHit() {
        return false;
    }

    @Override
    public boolean shouldInterruptOnEntityHit() {
        return false;
    }

    @Override
    public boolean shouldInterruptOnSelfCast() {
        return false;
    }
}
