package botanicamod.actions;

import com.badlogic.gdx.Gdx;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class DragonHeartAction extends AbstractGameAction {

    @Override
    public void update() {
        // Check to see if the player's hand is empty
        if (!AbstractDungeon.player.hand.isEmpty()) {
            // Select a random card from the player's hand
            AbstractCard card = AbstractDungeon.player.hand.getRandomCard(true);

            // If the card is not null, retain the card for the next turn
            if (card != null) {
                Gdx.app.log("MirrorShard", "Card selected to retain: " + card.name);
                card.retain = true; // Set the retain flag to true
            } else {
                Gdx.app.log("MirrorShard", "No card found in hand");
            }
        } else {
            Gdx.app.log("MirrorShard", "Player hand is empty");
        }
        isDone = true;
    }
}