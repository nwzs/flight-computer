# Dev log

## 2026-10-28: Phase 0 kickoff

- Created this repo with the planned layout.
- Picked the rocket: **Estes Orbitron (EST7002)**. BT-60 airframe (42 mm), 24 mm mount, clear payload section, plastic fins (fast build), 122 g. Recommended motors C11-3 to D12-7. Sold in the GTA (Big Boys With Cool Toys, Mississauga).
- Built a first OpenRocket model from published specs plus estimated geometry, then simulated C11/D12 at 45/75/100 g payloads, calm and 4.5 m/s wind (see `sim/README.md`).
  - Chose the **D12-5**. Tier 1 apogee ~159 m, peak ~11.7 g, landing ~5.2 m/s.
  - The ±32 g IMU and the 3 g launch-detect threshold both check out.
- Wrote `docs/requirements.md`.

**Next:** buy the kit, measure the payload bay, order the Tier 1 electronics, start the DME online module.
