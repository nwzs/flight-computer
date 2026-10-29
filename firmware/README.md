# Firmware

STM32F411 (STM32CubeIDE / HAL). Planned layout: `Core/` (generated), `drivers/` (BMP390, LSM6DSO32, W25Q128), `app/` (state machine, logger, USB CDC dump).

State machine: PAD → BOOST → COAST → DESCENT → LANDED. The "fake flight" test replays `sim/orbitron-tier1_D12-5_calm.csv` through the state machine.
