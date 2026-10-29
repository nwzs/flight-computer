# OpenRocket simulations

Built with OpenRocket 23.09. Open either `.ork` file and press **Run all simulations** (the stored results show as "outdated" until you do).

| File | Payload mass | Use |
|---|---|---|
| `orbitron-tier1.ork` | 75 g (perfboard logger + LiPo + sled, conservative) | Tier 1 flights |
| `orbitron-tier2.ork` | 45 g (custom PCB + LiPo + sled) | Tier 2 flights |
| `*_D12-5_calm.csv` | n/a | Time series (altitude, vertical velocity/accel, total accel, air pressure, thrust) for firmware "fake flight" replay and for post-flight comparison |

## The geometry is ESTIMATED: update it after you build the rocket

The kit's published specs are 67 cm long, 42 mm diameter, 122 g, 24 mm mount, 18 in parachute. Everything else is a guess:

- nose cone 100 mm ogive, clear payload section 127 mm, main tube 443 mm
- 3 trapezoidal fins (root 80, tip 30, span 50, sweep 50 mm)
- a "mass correction" component tops the parts up to Estes' 122 g, placed at the aft end

**To do after building:** measure each part's length, the fin shape and count, the real built mass, and the **balance point (CG) with the payload and a motor installed**. Enter them, delete the mass-correction component, and re-run. CG and stability are the numbers most likely to change.

## Results (estimated geometry; 36 in rod; 200 m ASL; 10 °C)

### Tier 1 (75 g payload, 197 g without motor)

| Motor | Wind | Liftoff mass | Rod exit | Max speed | Peak accel | Apogee | To apogee | Best delay | Speed at ejection | Landing | Stability off rod |
|---|---|---|---|---|---|---|---|---|---|---|---|
| C11-3 | calm | 232 g | 11.8 m/s | 31 m/s | 8.6 g | 55 m | 3.7 s | 2.9 s | 1.0 m/s | 5.2 m/s | 3.6 cal |
| D12-3 | calm | 240 g | 13.2 m/s | 53 m/s | 11.7 g | 151 m | 5.3 s | 4.5 s | 14.8 m/s (early) | 5.2 m/s | 3.4 cal |
| **D12-5** | calm | 240 g | 13.2 m/s | 53 m/s | 11.7 g | **159 m** | 6.1 s | 4.5 s | **5.0 m/s** | 5.2 m/s | 3.4 cal |
| D12-7 | calm | 240 g | 13.2 m/s | 53 m/s | 11.7 g | 159 m | 6.1 s | 4.5 s | 17.0 m/s (late) | 5.2 m/s | 3.4 cal |
| **D12-5** | 4.5 m/s | 240 g | 13.2 m/s | 52 m/s | 11.7 g | 150 m | 6.0 s | 4.3 s | 9.6 m/s | 5.2 m/s | 2.0 cal |

### Tier 2 (45 g payload, 167 g without motor)

| Motor | Wind | Rod exit | Max speed | Peak accel | Apogee | To apogee | Best delay | Speed at ejection | Stability off rod |
|---|---|---|---|---|---|---|---|---|---|
| **D12-5** | calm | 13.0 m/s | 61 m/s | 13.3 g | **189 m** | 6.5 s | 4.8 s | 1.7 m/s | 2.6 cal |
| **D12-5** | 4.5 m/s | 13.0 m/s | 61 m/s | 13.5 g | 181 m | 6.4 s | 4.8 s | 6.0 m/s | 1.2 cal |

### Takeaways

- **Motor: D12-5.** Its 5 s delay matches the ~4.5 s optimum. A D12-3 ejects while still climbing at ~15 m/s, and a D12-7 ejects ~17 m/s on the way down.
- **Skip the C11** with a payload. It leaves the rod under 12 m/s and only reaches ~55 m.
- **Payload ceiling ~100 g.** At 100 g the rocket still reaches ~137 m on a D12-5, but rod exit drops to 12.0 m/s.
- **Rod exit is ~13 m/s on a 36 in rod.** That is a little under the usual ~15 m/s guideline, so use the club's longest rod (1.2 m+) and don't fly in gusty wind.
- **Fly with the payload installed.** In the empty-kit estimate (no payload) stability dropped below 1 cal. The payload is nose weight. Verify with the real CG.
- **Sensor and firmware numbers:** peak ~12–14 g (the ±32 g IMU has >2× margin). These are kinematic accelerations; the accelerometer measures specific force, so during boost it reads ~1 g more than the table. Specific force stays above 3 g from 0.1 to 1.57 s (launch-detect threshold OK). Burn 1.65 s. Apogee at ~6 s. Total flight ~35–40 s. Pressure drops ~1.9–2.3 kPa pad-to-apogee.

`scripts/BuildSim.java` regenerates these files from the OpenRocket 23.09 jar.
