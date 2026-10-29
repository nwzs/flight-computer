# Dev log

## 2026-10-28: Phase 0 kickoff

- Created this repo with the planned layout.
- Picked the rocket: **Estes Orbitron (EST7002)**. BT-60 airframe (42 mm), 24 mm mount, clear payload section, plastic fins (fast build), 122 g. Recommended motors C11-3 to D12-7. Sold in the GTA (Big Boys With Cool Toys, Mississauga).
- Built a first OpenRocket model from published specs plus estimated geometry, then simulated C11/D12 at 45/75/100 g payloads, calm and 4.5 m/s wind (see `sim/README.md`).
  - Chose the **D12-5**. Tier 1 apogee ~159 m, peak ~11.7 g, landing ~5.2 m/s.
  - The ±32 g IMU and the 3 g launch-detect threshold both check out.
- Wrote `docs/requirements.md`.

## 2026-10-29: Tier 1 parts ordered

**Did:**
- Ordered Orbitron kit, 2× D12-5 two-packs, wadding and cement (in-store pickup)
- Ordered electronics from DigiKey (LSM6DSO32, BMP388, W25Q128 flash, LiPo charger), Universal-Solder (2× Black Pill, ST-Link) and Amazon (LiPo, JST-PH pigtails)

**Problems / decisions:**
- BMP390 breakout out of stock at Adafruit, DigiKey and Mouser → using BMP388 for Tier 1. Same Bosch register map; driver will accept both chip IDs. Tier 2 still uses BMP390.
- Adafruit LiPo unavailable in Canada → generic protected 500 mAh 602040 cell. Its plug is 1.25 mm, so I'll splice on a JST-PH 2.0 pigtail.

**Numbers:** ~$288 CAD spent so far (budget estimate was ~$287 with tax).

**Next:** DME online module and Creatron run by Nov 8; pick up the kit and measure the payload bay.
