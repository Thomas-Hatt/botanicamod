package botanicamod.actions;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.orbs.Frost;

public class EssenceOfFrostAction extends AbstractGameAction {

        public EssenceOfFrostAction(int potency) {
            this.duration = Settings.ACTION_DUR_FAST;
            this.amount = potency;
        }

        public void update() {
            if (this.duration == Settings.ACTION_DUR_FAST) {
                for(int i = 0; i < AbstractDungeon.player.orbs.size(); ++i) {
                    for(int j = 0; j < this.amount; ++j) {
                        AbstractDungeon.player.channelOrb(new Frost());
                    }
                }

                if (Settings.FAST_MODE) {
                    this.isDone = true;
                    return;
                }
            }

            this.tickDuration();
        }
    }