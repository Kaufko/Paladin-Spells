## [1.1.1] - 2026-08-14

### Fixed
- Fixed wrong duration for Sworn Protector on 1.21
- Fixed wrong cooldown on Bulwark to 45s on 1.21
- Server crashing due to loading a client only class
- 
### Changed
- Removed WIP from 2 spells, since they are released
- Nerfed Bulwark effect duration

---

## [1.1.0] - 2026-08-14

### Changed
- Duration scaling of Bulwark
- Duration scaling of Sworn Protector
- Sworn protector icon to look better

### Fixed
- Fixed sworn protector icon name
- Bulwark being too loud
- Clicking on Sworn Protector in an Inscription Table exited out due to missing caster value

### Removed
- Unnecessary logging

---

## [1.0.0] - 2026-08-1

### Added
- Ported to 1.21.1 NeoForge
- Added icon for Bedrock Skin

---

## [0.5] - 2026-07-27

### Fixed
- Fixed Bedrock Skin root effect.
- Fixed Ram distance calculation.
- Refactored Bedrock Skin damage mitigation scaling.

### Changed
- Adjusted Ram distance scaling.
- Removed unnecessary slowing effects from Bedrock Skin.
- Reduced Bedrock Skin duration.
- Removed distance text from Ram.

### Added
- Added temporary WIP icons.

### Removed
- Removed unnecessary logging.

---

## [0.4] - 2026-07-21

### Added
- Added **Bedrock Skin** (WIP).
- Added **Ram**.
- Added Ram sound effects.
- Added particle effects to taunted enemies (currently Angry Villager particles until custom particles are implemented).

### Changed
- Centered the Taunt spell icon.
- Renamed custom language format to **ISS** (normalized).
- Revamped Sworn Protector scaling.

---

## [0.342] - 2026-07-04

### Changed
- Removed the unnecessary Patchouli dependency.
- Fixed localization entries for Sworn Protector and Bulwark.
- Revamped scaling for Bulwark and Sworn Protector.

---

## [0.33] - 2026-07-02

### Fixed
- Fixed **Taunt** not working with hostile entities that do not extend `Monster` (e.g., Cataclysm mobs).
- Fixed **Sworn Protector** not triggering correctly.
- Fixed **Bulwark** description text.

### Added
- Added missing localization entries.

---

## [0.32] - 2026-06-30

### Added

#### Icons
- Sworn Protector
- Taunt (WIP)
- Ram

### Changed
- Added a hostile mob check to **Taunt**.
- Updated **Bulwark** description text to reflect actual values.
- Reduced **Bulwark** mana cost per level from **15** to **10**.

---

## [0.31] - 2026-06-30

### Added

#### Spells
- **Bulwark** — Increases your armor based on your current armor and spell power.
- **Taunt** — Taunts all enemies within range, forcing them to target you.
- **Sworn Protector** — Redirects a percentage of damage taken by nearby players to you.

#### WIP Spells
- **Bedrock Skin** — Immobilizes you while providing defensive buffs, including Armor, Max Health, and Resistance.
- **Ram** — Dash through enemies at short range, with effectiveness scaling from armor.

#### Other
- Added a mod logo.
