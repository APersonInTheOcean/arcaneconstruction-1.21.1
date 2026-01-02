package darkdefault.arcaneconstruction.spells.SpellCrafting;

import darkdefault.arcaneconstruction.ArcaneConstruction;

import java.util.ArrayList;

public class Spell {
    public enum Form {
        PROJECTILE,SELF,AOE;
    }
    public String sequence;
    Form form;
    ArrayList<SpellModule> modules = new ArrayList<>();

    public Spell(String spellSequence){
        this.sequence = spellSequence;
        createForm();
        dissectSpell();
    }
    public void dissectSpell(){
        StringBuilder modToAdd = new StringBuilder();
        for (int i = 0;i < this.sequence.length(); i++){
            if(this.sequence.charAt(i) == ArcaneConstruction.summationString.toLowerCase().charAt(0)) {
                SpellModule newMod = new SpellModule(modToAdd.toString());
                this.modules.add(newMod);
                modToAdd.setLength(0);
            }
            else {
                modToAdd.append(this.sequence.charAt(i));
            }

        }
        SpellModule newMod = new SpellModule(modToAdd.toString());
        this.modules.add(newMod);

    }

    public void createForm(){

        char prefix = this.sequence.charAt(0);

        if (prefix == ArcaneConstruction.invocationString.toLowerCase().charAt(0)) {
            this.form = Form.SELF;

        } else if (prefix == ArcaneConstruction.conjurationString.toLowerCase().charAt(0)) {
            this.form = Form.PROJECTILE;

        } else if (prefix == ArcaneConstruction.evocationString.toLowerCase().charAt(0)) {
            this.form = Form.AOE;
        }

        this.sequence = this.sequence.substring(1);


    }
    public Form getForm(){
        return this.form;
    }
    public String getFormEffectText(){
        switch (this.form) {
            case SELF:
                return "a spell on yourself ";
            case PROJECTILE:
                return "a projectile ";
            case AOE:
                return "a spell around yourself ";
        }
        return "error";
    }
    public ArrayList<SpellModule> getModules() {
        return modules;
    }

    public int getManaCost(){
        int mana = 0;
        for(SpellModule m: getModules()){
            mana += m.getManaCost();
        }
        return mana;
    }

}
