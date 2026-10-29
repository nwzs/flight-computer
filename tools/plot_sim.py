"""Plot an OpenRocket flight-profile CSV from sim/ (altitude, vertical velocity, acceleration).

Usage: python tools/plot_sim.py sim/orbitron-tier1_D12-5_calm.csv [more.csv ...]
Needs: pip install pandas matplotlib
"""
import sys
import pandas as pd
import matplotlib.pyplot as plt

G = 9.80665

def main(paths):
    fig, ax = plt.subplots(3, 1, sharex=True, figsize=(9, 8))
    for p in paths:
        d = pd.read_csv(p)
        label = p.split("/")[-1].replace(".csv", "")
        ax[0].plot(d.time_s, d.altitude_m, label=label)
        ax[1].plot(d.time_s, d.vel_vertical_ms, label=label)
        ax[2].plot(d.time_s, d.acc_total_ms2 / G, label=label)
    ax[0].set_ylabel("Altitude AGL (m)")
    ax[1].set_ylabel("Vertical velocity (m/s)")
    ax[2].set_ylabel("Total accel (g)")
    ax[2].set_xlabel("Time (s)")
    for a in ax:
        a.grid(alpha=0.3)
    ax[0].legend()
    fig.tight_layout()
    plt.show()

if __name__ == "__main__":
    if len(sys.argv) < 2:
        sys.exit(__doc__)
    main(sys.argv[1:])
