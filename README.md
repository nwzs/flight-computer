# Flight Computer

A model-rocket flight computer that logs barometric altitude and 6-axis IMU data from the pad to landing, built in three tiers: an off-the-shelf perfboard logger, then a custom 4-layer PCB, then telemetry.

> **Status:** Phase 0 (requirements and sim). No hardware built yet.

<!-- Add a photo of the board/rocket here once one exists -->

## Tier status

| Tier | What it is | Status |
|---|---|---|
| 1 | WeAct Black Pill (STM32F411) + Adafruit BMP390 / LSM6DSO32 / W25Q128 breakouts on perfboard | Parts not yet ordered |
| 2 | Custom 4-layer PCB, JLCPCB-assembled (`hardware/fc-v1`) | Not started |
| 3 | LoRa telemetry (RFM95W, DNP footprint on Tier 2), then deployment control | Not started |

## Specs (v1, passive logger)

| | |
|---|---|
| MCU | STM32F411CEU6, 25 MHz HSE, USB-C (CDC log dump) |
| IMU | LSM6DSO32, ±32 g / ±2000 dps, 400–1000 Hz in flight |
| Barometer | BMP390, ±3 Pa relative (~±0.25 m), 50–100 Hz in flight |
| Storage | W25Q128JV 16 MB SPI NOR |
| Power | 1S LiPo 500 mAh, AP2112K-3.3 LDO, external arming switch |
| Vehicle | Estes Orbitron (BT-60, 24 mm mount) on Estes D12-5 |

## Predicted flight (OpenRocket, estimated geometry, 75 g payload, D12-5, calm)

Apogee ~159 m (523 ft) · peak accel ~11.7 g · max speed ~53 m/s · apogee at ~6.1 s · landing ~5.2 m/s. See [`sim/`](sim/README.md).

## Repo layout

```
hardware/   KiCad projects (tier1-perfboard wiring, fc-v1 PCB)
fab/        Gerbers, BOM, CPL, schematic PDF, iBOM per ordered revision
firmware/   STM32 C firmware
tools/      Python: USB log dump, decode, plotting
sim/        OpenRocket .ork files + exported flight profiles
data/       Raw logs + decoded CSVs, one folder per flight/test
docs/       Requirements, dev log, bring-up checklist, errata, test reports
```

## Results

_Flight data and sim-vs-measured plots go here after the first flight._
