# GUI Implementáció – Zúzmaraváros

## Áttekintés

Ez a dokumentum leírja a `view` és `controller` csomagok implementációját,
amelyek a meglévő `model` csomag fölé épülnek MVC architektúra szerint.

---

## Új fájlok

| Fájl | Csomag | Leírás |
|------|--------|--------|
| `src/controller/JatekController.java` | `controller` | Fő játékvezérlő |
| `src/view/JatekAblak.java` | `view` | Főablak (JFrame) |
| `src/view/TerkeploPanel.java` | `view` | Térképrajzoló panel |
| `src/view/InfoPanel.java` | `view` | Erőforrás-kijelző |
| `src/view/JarmuPanel.java` | `view` | Jármű-kiválasztó oldalsáv |
| `src/view/BoltDialog.java` | `view` | Bolt ablak |
| `src/Main.java` | _(default)_ | GUI belépési pont |

---

## Modell módosítások

A modell fájlok **nem változtak**. Az egyetlen változás:
- `Bolt.setNyilvantarto()` és `Bolt.setSoszoroAr()` (már meglévő metódusok) kerülnek hívásra a controllerből.

---

## Indítás

```bash
mvn compile
java -cp target/classes Main
```

Vagy az IDE-ből: `Main.main()`.

---

## Architektúra

```
Main
 └─ JatekAblak (JFrame)
     ├─ [NORTH]  Toolbar (gombok)
     ├─ [WEST]   JarmuPanel (jármű-lista)
     ├─ [CENTER] TerkeploPanel (térkép)
     └─ [SOUTH]  InfoPanel (erőforrások)
         │
         └─── JatekController (Observer pattern)
                  │
                  └─── model.* (modell változatlan)
```

### JatekController

A controller kezeli:
- **Játékállapot**: `Fazis` enum (`TERVEZES` / `SZIMULACIO` / `JATEK_VEGE`)
- **Térkép létrehozása**: 4 csomópont, 4 út (négyzet topológia), 2 sávos utak
- **Pozíció-számítás**: Az útegységek megjelenítési pozícióit a csomópontok
  közötti vektorok alapján számítja ki (merőleges eltolással sávonként)
- **Körök kezelése**: `korBefejezes()` → szimulációs fázis; `TICKS_PER_KOR` tick után → tervezési fázis
- **Útvonal-tervezés**: `tervekMap` tárolja a tervezett útegységlistákat jármű szerint
- **Szimuláció**: `szimulacioTick()` hívja a havazást, mozgatást, balesetellenőrzést
- **Observer**: `addAllapotValtozoListener(Runnable)` értesíti a view-t

### TerkeploPanel

- **Rajzolás**: `paintComponent()` → utak → útegységek → csomópontok → járművek
- **Kattintás-kezelés**: `mouseClicked()` megkeresi a kattintáshoz legközelebbi útegységet
  (15px sugarú körön belül) és meghívja `controller.utegysegKijelol(ue)`
- **Színkód**:
  - Tiszta aszfalt: szürke-zöld
  - Havas út: kékes-fehér (arányos a hómagas sággal)
  - Blokkolt (mély hó): sötétkék
  - Jeges: cián
  - Sózott: halványsárga
  - Kijelölt (útvonal): sárga keret
  - H = Hókotró (narancs), B = Busz (zöld), A = Autó NPC (sárga)

### Koordináta-rendszer

A modell topológiai (koordinátáktól mentes), ezért a megjelenítési pozíciókat
a controller számítja ki és tárolja el:
- Csomópontok: manuálisan megadott pixel-koordináták (négyzet layout)
- Útegységek: az út két végpontja közötti vektor mentén egyenlő közönként elosztva,
  sávonként merőlegesen eltolva

---

## Körök menete

### Tervezési fázis
1. A játékos kiválaszt egy járművet a bal oldalsávból (kattintással)
2. A térképen bal egérgombbal kattint útegységekre → ezek kerülnek a tervbe
3. Sárga keret jelzi a kijelölt útegységeket
4. A „Kör befejezése →" gomb elindítja a szimulációt

### Szimulációs fázis
- **Tick (1 lépés)**: manuálisan lép egyet a szimulációban
- **▶ Auto szimuláció**: 600 ms-onként automatikusan tickel, stop gombbal leállítható
- Minden tickben:
  1. Havazás (HAVAZAS_PER_TICK = 1 egység/tick) + sóolvasztás
  2. Irányítható járművek mozgatása a tervezett útvonalon (`ralep()`)
  3. Hókotró takarít (`takarit()`) az aktuális útegységen
  4. NPC autók mozgatása (`lep()`)
  5. Balesetek keresése (`balesetetKeres()`)
  6. Játék vége ellenőrzés

A TICKS_PER_KOR tick után visszatér a tervezési fázisba.

### Bolt
- Só (+10 egység), Biokerozin (+5 egység) vásárolható
- Hókotró fejek cserélhetők (Seprő, Hányó, Jégtörő, Sószóró)
- Busz fejlesztések (sebesség, tapadás, hozam)

---

## Demo térkép

```
  A(megálló)--ut1[2sáv,5ue]--B(célpont)
      |                          |
   ut4[4ue]                  ut2[4ue]
      |                          |
  D(célpont)--ut3[2sáv,5ue]--C(megálló)
```

- **Hókotró**: ut1 első sávjának első útegységén indul
- **Busz**: ut3 első sávján indul, végállomások: A és C
- **NPC autó**: D csomópontból indul B felé (BFS útvonal: D→A→B vagy D→C→B)

---

## Ismert korlátok (prototípus)

1. A `Nyilvantarto` getterek konzolra írnak (`System.out.println`) – ez a modell
   prototípus-viselkedése, a GUI-ban elfogadott működés.
2. Az NPC autók (`Auto.lep()`) a sáv végére érve elakadnak (a modell `lep()` metódusa
   nem kezeli a csomóponti átmenetet override-ban).
3. A busz `megalloErintese()` csak akkor hívódik meg, ha a jármű a `Csomopont.jarmuErkezik()`
   útján halad át – a controller-alapú mozgatásnál ez nem automatikus.
4. Egyszerre csak 1 játékos kezelése implementált.
