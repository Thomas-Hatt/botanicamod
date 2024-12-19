# Version 0.0.2 - 11/15/2024

## New Features
- Stylized `ToDo.md`.
- Created `UpdateNotes.md`.
- Added 1 new relic: Thorned Crown (boss relic).
- Changed `RelicStrings.json` to reflect my added relics.
- Created the programming outlines for the following relics:
    1. Glimmering Orb
    2. Dragon Heart
    3. Flask of Duplication

# Version 0.0.3 - 11/16/2024

## New Features
- Updated `ToDo.md`
- Changed the `ID` of each relic to be more in line with Slay the Spire's formatting.
- Added 2 new boss relics:
- 1. Flask of Duplication. Upon pickup, gain 2 copies of #yNormality. Duplicate every card in your deck and gain 10 gold for each card duplicated.
- 2. Dragon Heart - Gain 2 Strength and 2 Dexterity at the start of each combat. Retain a random card at the end of your turn
- Changed `RelicStrings.json` to reflect my added relics.
- Created the programming outlines for the following relics:
  1. Gambler's Debt

# Version 0.0.4 - 11/17/2024
### I had a very short amount of time for today, so I only created outlines for new relics
## New Features
- Updated `ToDo.md`
- Created the programming outlines for the following relics:
- 1. Purging Stone
- 2. Alchemist's Mask
- 3. Jester's Belt
- 4. Sky's Death Spell
- 5. Rainbow Cape
- 6. Reality Shard

# Version 0.0.5 - 11/18/2024
## New Features
- Updated `ToDo.md`
- Added 2 new Boss Relics:
- 1. Gambler's debt - Card rewards have 2 extra cards, but 1 random card is disguised as a curse.
- 2. Glimmering Orb - The first time you play 5 Power Cards each combat, gain 1 permanent Strength


- Created the programming outlines for the following relics:
- 1. Tapinella
## Fixed Bugs
Flask of duplication is now a Boss Relic instead of an Uncommon Relic

## Known Bugs
- Gambler's Debt is not removing the card the player is supposed to receive when a curse replaces that card in the deck

# Version 0.0.5 #2 - 11/19/2024
This was made shortly after I published Version 0.0.5 (It's 12:31 A.M. so I decided to label it as 11/19/2024).
- Fixed bug where Gambler's Debt was not removing the card the player is supposed to receive when a curse replaces that card in the deck
- Improved logic for adding the random curse to the player's deck

# Version 0.0.6 - 11/19/2024
## New Features
- Updated `ToDo.md`
- Created the programming outlines for the following relics:
- 1. Cerberus' Mask - Upon pickup, obtain all Face Relics. Lose 1 Energy at the start of your turn.
- Added 1 new Shop relic:
- 1. Tapinella - All Dexterity is converted to Strength
- Moved the following relics to become Boss Relics:
- 1. Jester's Belt
- Moved the following relics to become Shop Relics:
- 1. Tapinella
- 2. Sky's Death Spell
- 3. Alchemist's Mask

## Known Bugs
- Gambler's Debt does not trigger Darkstone Periapt

## Future features I want to implement
- For Gambler's Debt, I want to mark 3 cards that are known to be safe (i.e., won't give a curse if the player chooses that card).
- This would dynamically scale if the player has fewer card rewards, for instance.

# Version 0.0.7 - 11/20/2024
## New Features
- Updated `ToDo.md`
- Deleted excess imports in the individual classes for relics
- Added 1 new Uncommon relic:
- 1. Divider - Every 4 turns, add a random skill into your hand.
- Added 1 new Rare relic:
- 1. Nebula - If you end your turn without Block, your next skill is played twice.
- Moved the following relics to become Shop Relics:
- 1. Gambler's Debt

# Version 0.0.8 - 11/21/2024
## New Features
- Updated `ToDo.md`
- Added 1 new Boss relic:
- 1. Jester's Belt - Gain 1 Energy at the start of each turn. Gain 1 Dexterity for every 4 cards played in a turn, but lose 2 Dexterity if you play a Power card. Dexterity is now capped at 4.

# Version 0.0.9 - 11/22/2024
## New Features
- Updated `ToDo.md`
- Created `Credits.md`
- Created `FutureFeatures.md`
- Created `KnownBugs.md`
- Removed unnecessary code
- Added 1 new Rare relic:
- 1. Narcissus (Ironclad Specific) - Start each combat with Limit Break. It has Purge and costs 0.

# Version 0.1.0 - 11/23/2024
## New Features
- Updated `ToDo.md`
- Added new credits in `Credits.md`
- Fixed a typo for the relic `Glimmering Orb`
- Added 2 new Rare relics:
- 1. Silene - Heal 10% of damage taken at the end of combat.
- 2. Marigold - Gain gold equal to 25% of the damage dealt in combat, but only for the first 50 damage dealt. Additionally, for every 100 Gold gained from this effect, draw one card at the start of combat.

# Version 0.1.1 - 11/24/2024
## New Features
- Updated `ToDo.md`
- Added 1 new Rare relic:
- 1. Hemlock - Upon entering a Rest Site, Scry 1 for every 6 cards in your deck at the start of your next combat.

# Version 0.1.2 - 11/25/2024
## Today I added 2 relics suggested by Frost Prime's community!
## New Features
- Updated `ToDo.md`
- Added 2 new Uncommon relics:
1. Blue Ashes (as suggested by blueash7077!) - At the start of each combat, gain the Fire Breathing buff.
- - Blue Ashes Side Quest - Upon drawing 20 total Status Cards, gain the Fire Breathing+ buff instead.
3. Equinox (as suggested by Equi!) - Every time you play 10 Attacks, play the top card of your draw pile.

# Version 0.1.3 - 11/26/2024
## New Features
- Updated `ToDo.md`
- Added 2 new Uncommon relics:
- 1. Terrifying Trinket - At the start of turn 3, all Minions run away.
- 2. Cardoon - Every time you play 4 Attacks in a single turn, shuffle a Jack of All Trades into your draw pile.

# Version 0.1.4 - 11/27/2024
## Today was quite a successful day because I added my first few potions!
## New Features
- Updated `ToDo.md`
- Added 2 new Uncommon Potions:
- 1. Potion of A Thousand Cuts - Whenever you play a card, deal 1 (2) damage to ALL enemies.
- 2. Retain Potion - Retain your hand for X turns.
- Added 1 new Rare Potion:
- 1. Potion of Slowness - Apply slow.
Moved the following relic to become a Rare relic:
- 1. Narcissus

# Version 0.1.5 - 11/28/2024 (Thanksgiving Day!)
## New Features
- Updated `ToDo.md`
- Added 1 new Rare relic:
- 1. Sweater (The Defect Specific) - At the start of each combat, Channel 2 Frost. Gain 1 Focus every 3 turns.
- Added 1 new Common potion:
- 1. Potion of Vigor (Ironclad Specific) - Gain 8 Vigor.
- Added 2 new Uncommon potions:
- 1. Essence of Frost (The Defect Specific) - Channel 1 (2) Frost Orbs for each orb slot.
- 2. Essence of Lightning (The Defect Specific) - Channel 1 (2) Lightning Orbs for each orb slot.

# Version 0.1.6 - 12/16/2024
### I am back from my hiatus! My final exams for college took a lot of my time and energy
## New Features
- Updated `ToDo.md`
- Add 2 new Common relics (I somehow made it this far without having any):
- 1. Short Circuit - If you do not play any Skills during your turn, gain an extra Energy next turn.
- 2. Merchant's Robes - Upon entering a shop, obtain a Potion and a Card reward.
- Added 1 new Shop relic:
- 1. Alchemist's Mask - Right click to activate. Lose all gold. At the end of your turn, if you have 0 gold, obtain a random potion and add a random curse to your deck and draw pile.
## Fixed Bugs
Gambler's Debt now triggers Darkstone Periapt (recoded the relic to be more visually consistent, too.)

# Version 0.1.7 - 12/16/2024
## New Features
- Updated `ToDo.md`
- Added 1 new Uncommon relic:
- 1. Illusionist's Coin - At the start of turn 3, add a copy of Illusion Neutralize to your hand.
1. - - Illusionist's Coin Side Quest - Every 3rd time you play Illusion Neutralize, Draw 3 cards.
## Fixed Bugs
Fixed an issue where cards could not be used from the console.

# Version 0.1.8 - 12/18/2024
### I got really sick, and I'm still pretty sick :(
## New Features
- Updated `ToDo.md`
- Added 3 new Common relics:
1. Burning Stone - At the start of each combat, add a Burn to your discard pile. Whenever you draw a Burn, gain 2 Strength.
2. Quill - At the start of each combat, add a random 0 cost card to your hand, discard pile, and draw pile. 
3. Blossom - The first time your HP drops to 50% or below each combat, add a copy of Miracle to your hand.

# Version 0.1.9 - 12/19/2024
### I'm still pretty sick :(
## New Features
- Updated `ToDo.md`
- Added 1 new Rare relic:
1. Crystal - At the start of combat, gain 2 Crystallize.
- Added 1 new Boss relic:
1. Prismatic Box: Transform all Strikes and Defends into Uncommon cards. These cards can be of any color.