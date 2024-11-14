package botanicamod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.badlogic.gdx.Gdx;

public class MirrorShardAction extends AbstractGameAction {

    @Override
    public void update() {
        // Check to see if the player's hand is empty
        if (!AbstractDungeon.player.hand.isEmpty()) {
            // Select a random card
            AbstractCard card = AbstractDungeon.player.hand.getRandomCard(true);
            // If the card is not null, add the card to the end of your hand
            if (card != null) {
                Gdx.app.log("MirrorShard", "Card selected: " + card.name);
                AbstractDungeon.actionManager.addToBottom(new MakeTempCardInHandAction(card.makeStatEquivalentCopy()));
            } else {
                Gdx.app.log("MirrorShard", "No card found in hand");
            }
        } else {
            Gdx.app.log("MirrorShard", "Player hand is empty");
        }
        isDone = true;
    }
}
