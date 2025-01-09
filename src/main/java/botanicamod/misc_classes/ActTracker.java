package botanicamod.misc_classes;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class ActTracker {
    public static int getCurrentAct() {
        if (AbstractDungeon.floorNum <= 17) {
            return 1;
        } else if (AbstractDungeon.floorNum <= 34) {
            return 2;
        } else if (AbstractDungeon.floorNum <= 51) {
            return 3;
        } else {
            return 4;
        }
    }
}