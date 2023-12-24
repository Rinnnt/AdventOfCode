import z3

px, py, pz, vx, vy, vz, t0, t1, t2 = z3.Ints("px py pz vx vy vz t0 t1 t2")

s = z3.Solver()
s.add(px + t0 * vx == 176253337504656 + t0 * 190)
s.add(py + t0 * vy == 321166281702430 + t0 * 8)
s.add(pz + t0 * vz == 134367602892386 + t0 * 338)
s.add(px + t1 * vx == 230532038994496 + t1 * 98)
s.add(py + t1 * vy == 112919194224200 + t1 * 303)
s.add(pz + t1 * vz == 73640306314241 + t1 * 398)
s.add(px + t2 * vx == 326610633825237 + t2 * -67)
s.add(py + t2 * vy == 321507930209081 + t2 * -119)
s.add(pz + t2 * vz == 325769499763335 + t2 * -75)
s.check()
print(s.model().evaluate(px + py + pz))

