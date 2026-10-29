import net.sf.openrocket.startup.*;
import net.sf.openrocket.plugin.PluginModule;
import net.sf.openrocket.document.*;
import net.sf.openrocket.rocketcomponent.*;
import net.sf.openrocket.rocketcomponent.position.AxialMethod;
import net.sf.openrocket.motor.*;
import net.sf.openrocket.simulation.*;
import net.sf.openrocket.file.openrocket.OpenRocketSaver;
import net.sf.openrocket.logging.*;
import java.util.*;
import java.io.*;

public class BuildSim {
  static double IN = 0.0254;
  public static void main(String[] a) throws Exception {
    double payloadKg = Double.parseDouble(a[0]);
    String out = a[1];
    String[] winds = (a.length > 2 ? a[2] : "0").split(",");
    GuiModule gm = new GuiModule();
    com.google.inject.Injector inj = com.google.inject.Guice.createInjector(gm, new PluginModule());
    Application.setInjector(inj);
    gm.startLoader();
    for (int k=0;;k++) { try { Application.getThrustCurveMotorSetDatabase().findMotors(null,null,"Estes","D12",Double.NaN,Double.NaN); break; } catch (Exception e) { if (k>120) throw e; Thread.sleep(1000);} }

    double R = 0.0416/2;  // BT-60 OD 41.6 mm
    Rocket rocket = new Rocket();
    rocket.setName("Estes Orbitron (EST7002) + flight computer, ESTIMATED geometry");
    AxialStage st = new AxialStage(); st.setName("Sustainer"); rocket.addChild(st);

    NoseCone nc = new NoseCone(Transition.Shape.OGIVE, 0.100, R);
    nc.setName("Nose cone (plastic, est. 100 mm)");
    nc.setAftShoulderLength(0.025); nc.setAftShoulderRadius(R-0.0008); nc.setAftShoulderThickness(0.0015);
    nc.setThickness(0.0015);
    st.addChild(nc);

    BodyTube pl = new BodyTube(0.127, R, 0.0006);
    pl.setName("Clear payload section (est. 127 mm)");
    st.addChild(pl);
    MassComponent fc = new MassComponent(0.090, R-0.004, payloadKg);
    fc.setName("Flight computer + battery + sled");
    fc.setAxialMethod(AxialMethod.MIDDLE); fc.setAxialOffset(0);
    pl.addChild(fc);
    TubeCoupler cp = new TubeCoupler(); cp.setName("Payload coupler");
    cp.setLength(0.038); cp.setOuterRadius(R-0.0008); cp.setThickness(0.0008);
    cp.setAxialMethod(AxialMethod.BOTTOM); cp.setAxialOffset(0.019);
    pl.addChild(cp);

    BodyTube bt = new BodyTube(0.443, R, 0.0005);
    bt.setName("Main body tube BT-60 (est. 443 mm)");
    st.addChild(bt);

    InnerTube mm = new InnerTube(); mm.setName("24 mm motor mount");
    mm.setOuterRadius(0.0249/2+0.0005); mm.setThickness(0.0005); mm.setLength(0.070);
    mm.setAxialMethod(AxialMethod.BOTTOM); mm.setAxialOffset(0);
    mm.setMotorMount(true); mm.setMotorOverhang(0.003);
    bt.addChild(mm);

    TrapezoidFinSet fins = new TrapezoidFinSet(3, 0.080, 0.030, 0.050, 0.050);
    fins.setName("Fins (plastic, shape ESTIMATED)"); fins.setThickness(0.0025);
    fins.setAxialMethod(AxialMethod.BOTTOM); fins.setAxialOffset(0);
    bt.addChild(fins);

    LaunchLug lug = new LaunchLug(); lug.setName("3/16 in launch lug");
    lug.setOuterRadius(0.0029); lug.setThickness(0.0004); lug.setLength(0.050);
    lug.setAxialMethod(AxialMethod.MIDDLE); lug.setAxialOffset(0);
    bt.addChild(lug);

    Parachute chute = new Parachute(); chute.setName("18 in parachute");
    chute.setDiameter(18*IN); chute.setCD(0.8);
    chute.setAxialMethod(AxialMethod.TOP); chute.setAxialOffset(0.05);
    bt.addChild(chute);

    MassComponent wad = new MassComponent(0.03, R-0.003, 0.004); wad.setName("Wadding + shock cord");
    wad.setAxialMethod(AxialMethod.TOP); wad.setAxialOffset(0.10); bt.addChild(wad);

    // Fit empty kit mass to Estes' 122 g (4.3 oz) with a correction mass placed at the fin can
    rocket.enableEvents();
    double kitNoPayload = rocket.getSelectedConfiguration() == null ? 0 : 0;
    double kitMass = sumMass(rocket) - payloadKg;
    double corr = 0.122 - kitMass;
    MassComponent fix = new MassComponent(0.05, R-0.003, Math.max(corr, 0));
    fix.setName(String.format("Mass correction to Estes 122 g spec (%.0f g)", corr*1000));
    fix.setAxialMethod(AxialMethod.BOTTOM); fix.setAxialOffset(-0.10);
    bt.addChild(fix);
    System.err.printf("kit components %.1f g, correction %.1f g, total w/o motor %.1f g%n", kitMass*1000, corr*1000, sumMass(rocket)*1000);

    OpenRocketDocument doc = OpenRocketDocumentFactory.createDocumentFromRocket(rocket);

    String[][] motors = {{"C11","3"},{"D12","3"},{"D12","5"},{"D12","7"}};
    System.out.println("motor\tliftoff_g\trod_exit_ms\tmax_v_ms\tmax_acc_g\tapogee_m\tapogee_ft\tt_apogee_s\tbest_delay_s\tdeploy_v_ms\tland_v_ms\tstab_cal_rod\tstab_cal_min_ascent");
    for (String ws : winds) { double windMs = Double.parseDouble(ws);
    for (String[] m : motors) {
      List<ThrustCurveMotor> found = Application.getThrustCurveMotorSetDatabase().findMotors(null, null, "Estes", m[0], Double.NaN, Double.NaN);
      ThrustCurveMotor mot = null;
      for (ThrustCurveMotor t : found) if (t.getManufacturer().getSimpleName().startsWith("Estes") && t.getCommonName().startsWith(m[0])) { mot = t; break; }
      if (mot == null) { System.err.println("no motor " + m[0] + " found=" + found.size()); continue; }
      FlightConfigurationId id = new FlightConfigurationId();
      rocket.createFlightConfiguration(id);
      MotorConfiguration mc = new MotorConfiguration(mm, id);
      mc.setMotor(mot); mc.setEjectionDelay(Double.parseDouble(m[1]));
      mm.setMotorConfig(mc, id);
      rocket.setSelectedConfiguration(id);
      Simulation sim = new Simulation(doc, rocket);
      sim.setFlightConfigurationId(id);
      sim.setName(m[0] + "-" + m[1] + (windMs > 0 ? String.format(" wind %.1f m/s", windMs) : " calm"));
      SimulationOptions o = sim.getOptions();
      o.setLaunchRodLength(36*IN); o.setLaunchRodAngle(0); o.setLaunchIntoWind(true);
      o.setWindSpeedAverage(windMs); o.setWindSpeedDeviation(windMs*0.2); o.setWindTurbulenceIntensity(windMs>0?0.1:0);
      o.setLaunchAltitude(200); o.setLaunchLatitude(43.7); o.setLaunchLongitude(-79.4);
      o.setLaunchTemperature(273.15+10);
      doc.addSimulation(sim);
      sim.simulate();
      FlightData d = sim.getSimulatedData();
      FlightDataBranch b = d.getBranch(0);
      // stability at rod clearance: first index where altitude > rod length
      List<Double> alt = b.get(FlightDataType.TYPE_ALTITUDE), stab = b.get(FlightDataType.TYPE_STABILITY);
      double sRod = Double.NaN;
      for (int i = 0; i < alt.size(); i++) if (alt.get(i) > 36*IN && stab.get(i)!=null && !Double.isNaN(stab.get(i))) { sRod = stab.get(i); break; }
      double sMin = Double.NaN; for (int i = 0; i < alt.size(); i++) { Double v = stab.get(i); if (v!=null && !Double.isNaN(v) && b.get(FlightDataType.TYPE_VELOCITY_TOTAL).get(i) > 10 && b.get(FlightDataType.TYPE_VELOCITY_Z).get(i) > 0) sMin = Double.isNaN(sMin)? v : Math.min(sMin, v); }
      if (m[0].equals("D12") && m[1].equals("5") && windMs == 0) {
        FlightDataType[] cols = {FlightDataType.TYPE_TIME, FlightDataType.TYPE_ALTITUDE, FlightDataType.TYPE_VELOCITY_Z, FlightDataType.TYPE_ACCELERATION_Z, FlightDataType.TYPE_ACCELERATION_TOTAL, FlightDataType.TYPE_AIR_PRESSURE, FlightDataType.TYPE_THRUST_FORCE};
        try (PrintWriter pw = new PrintWriter(out.replace(".ork", "_D12-5_calm.csv"))) {
          pw.println("time_s,altitude_m,vel_vertical_ms,acc_vertical_ms2,acc_total_ms2,air_pressure_pa,thrust_n");
          List<Double> t = b.get(FlightDataType.TYPE_TIME);
          for (int i = 0; i < t.size(); i++) { StringBuilder sb = new StringBuilder();
            for (int c = 0; c < cols.length; c++) { List<Double> L = b.get(cols[c]); Double v = (L == null || i >= L.size()) ? null : L.get(i);
              if (c > 0) sb.append(','); sb.append(v == null || Double.isNaN(v) ? "" : String.format(Locale.US, "%.4f", v)); }
            pw.println(sb); }
        }
      }
      double liftoff = sumMass(rocket) + mot.getLaunchMass();
      System.out.printf(Locale.US, "%s-%s w%.1f\t%.0f\t%.1f\t%.1f\t%.1f\t%.0f\t%.0f\t%.2f\t%.1f\t%.1f\t%.1f\t%.2f\t%.2f%n",
        m[0], m[1], windMs, liftoff*1000, d.getLaunchRodVelocity(), d.getMaxVelocity(), d.getMaxAcceleration()/9.80665,
        d.getMaxAltitude(), d.getMaxAltitude()/0.3048, d.getTimeToApogee(), d.getOptimumDelay(),
        d.getDeploymentVelocity(), d.getGroundHitVelocity(), sRod, sMin);
    }
    }
    StorageOptions so = new StorageOptions(); so.setSaveSimulationData(true);
    try (OutputStream os = new FileOutputStream(out)) {
      new OpenRocketSaver().save(os, doc, so, new WarningSet(), new ErrorSet());
    }
    System.exit(0);
  }
  static double sumMass(RocketComponent c) {
    double m = c.getMass();  // getMass returns component (incl. override) mass only
    for (RocketComponent ch : c.getChildren()) m += sumMass(ch);
    return m;
  }
}
