# Requirements (v1 passive logger)

Phase 0, 2026-10-28. Flight numbers come from `sim/` (estimated geometry, so re-check after the rocket is built).

## Mission

- Log barometric altitude and 6-axis IMU data from power-on to landing. Dump the logs over USB after recovery.
- Compare against OpenRocket (and optionally a commercial altimeter).
- Passive only: no pyro, no radio, no GPS in v1. The motor's ejection charge handles recovery.

## Vehicle

| Item | Value |
|---|---|
| Rocket | Estes Orbitron, EST7002 (BT-60 airframe, 42 mm OD, 24 mm mount, 18 in chute, 122 g kit) |
| Primary motor | Estes D12-5 (C11 not used with payload) |
| Launch | Club launch, 3/16 in rod (use the longest available) |

## Derived requirements

| ID | Requirement | Basis |
|---|---|---|
| R1 | Payload (board + battery + sled) ≤ 75 g for Tier 1, ≤ 45 g target for Tier 2, 100 g hard ceiling | Sim: rod exit drops to 12 m/s at 100 g |
| R2 | Fits the payload bay: ≤ 30 mm wide, ≤ 90 mm long (confirm against the measured bay) | BT-60 ID ≈ 40.5 mm. A 30 mm board leaves ~13 mm of height each side of centre |
| R3 | Accelerometer range ≥ ±16 g. ±32 g chosen | Sim peak 11.7 g (Tier 1) to 13.5 g (Tier 2), plus ejection/landing shocks |
| R4 | Altitude resolution ≤ 0.5 m over 0–300 m AGL | BMP390 ±3 Pa ≈ ±0.25 m. Expected pressure drop ~2 kPa |
| R5 | Launch detect: \|a\| > 3 g for ≥ 100 ms, confirmed by baro rise | Sim: specific force > 3 g from 0.10 to 1.57 s |
| R6 | Full-rate logging for ≥ 60 s after launch, plus a 2 s pre-launch buffer | Flight ≈ 35–40 s. At 500 Hz × 32 B that is ~1 MB of 16 MB |
| R7 | Battery runtime ≥ 2× the expected pad wait (target ≥ 2 h powered and logging at the pad rate) | Club pad waits can be long |
| R8 | Baro shielded from ejection gas and light; static vent holes in the payload section | Clear payload tube, sunlight on BMP390 |
| R9 | IMU axes labelled. One axis aligned with the rocket's long axis | Data analysis |
| R10 | Board failure must not endanger the rocket | Passive design; recovery is motor ejection |

## Open items (measure once the kit arrives)

- [ ] Payload bay inside diameter and usable length (and whether the clear section is BT-60 or larger)
- [ ] Built mass and CG with payload + motor → update `sim/*.ork`, re-check stability ≥ 1.5 cal in wind
- [ ] How the payload section attaches and comes off (bulkhead needed? shock-cord path?)
- [ ] Vent hole size and count for the payload section
