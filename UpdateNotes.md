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
- Updated `ToDo.md`
- Added 2 new Uncommon relics:
1. Blue Ashes (as suggested by blueash7077!) [X] - At the start of each combat, gain the Fire Breathing buff.
- - Blue Ashes Side Quest [X] - Upon drawing 20 total Status Cards, gain the Fire Breathing+ buff instead.
3. Equinox (as suggested by equi31!) [X] - Every time you play 10 Attacks, play the top card of your draw pile.