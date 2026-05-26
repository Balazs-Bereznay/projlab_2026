# Hókotrók – View és Controller implementációs specifikáció (Claude Code számára)

> **Cél:** A meglévő, kész **modell** rétegre (Java, `model` csomag) ráépíteni a **View** és **Controller** réteget MVC + push-alapú Observer mintával, hogy egy működő grafikus felület (Java Swing) jöjjön létre. A modellt **a lehető legkevésbé** szabad módosítani: csak az Observer-értesítéshez szükséges minimális kiegészítés engedélyezett.
>
> **Ez a dokumentum önmagában elegendő** a teljes View+Controller réteg implementálásához. Tartalmazza a modell pontos, megvalósításhoz használandó API-ját, minden új osztály teljes szerződését, az Observer-integráció pontos helyeit, a pályaformátumot és a 3 képernyő (főmenü, gameplay, bolt) pontos elrendezését.

---

## 0. Technológiai keret és projektstruktúra

- **Nyelv / build:** Java 17, Maven (a meglévő `pom.xml`-t használjuk; `source`/`target` = 17).
- **GUI keret:** **Java Swing** (nem JavaFX). Egyetlen külső függőség sem szükséges.
- **Rajzolás:** `paintComponent(Graphics)` felülírással, `Graphics2D`-vel. **Tilos** localStorage/böngészős API – ez tiszta desktop alkalmazás.
- **Csomagstruktúra (új kód):**

```
src/
├── model/                 # MEGLÉVŐ – csak minimális Observer-kiegészítés
├── view/
│   ├── JatekAblak.java        # JFrame főablak (CardLayout: menü / játék / overlay bolt)
│   ├── MenuPanel.java         # Főmenü (játékosbeállítás)
│   ├── PalyaPanel.java        # Pálya kirajzolás + kattintáskezelés
│   ├── HUDPanel.java          # Állapotsáv (kassza, erőforrás, aktív játékos…)
│   ├── AkcioSor.java          # Alsó gombsor (lépés, takarít, kör vége, bolt)
│   ├── BoltPanel.java         # Bolt nézet (kategória + kínálat + részletek)
│   ├── PalyaLayout.java       # Csomópont → képernyőpozíció leképezés + fájl I/O
│   ├── Pont.java              # (x,y) értékosztály
│   ├── Megfigyelo.java        # interfész: frissit()
│   ├── View.java              # abstract: modellElem + frissit() + kirajzol(Graphics2D)
│   ├── CsomopontView.java
│   ├── UtView.java
│   ├── SavView.java
│   ├── UtegysegView.java
│   ├── JarmuView.java         # abstract
│   ├── HokotroView.java
│   ├── BuszView.java
│   └── AutoView.java
├── controller/
│   ├── JatekController.java   # vezérlési központ
│   ├── BemenetKezelo.java     # MouseAdapter + KeyListener → controller hívások
│   ├── Fazis.java             # enum: TERVEZES, SZIMULACIO, BOLT
│   └── JatekIndito.java       # main() – grafikus belépési pont
└── (model.Megfigyelheto.java) # ÚJ interfész a model csomagban (ld. 8. szakasz)
```

> **A régi konzolos belépési pont (`model.Prototipus.main`) változatlan marad.** A grafikus mód külön `controller.JatekIndito.main()` osztályból indul. A két belépési pont egymástól független.

---

## 1. A játék rövid működése (kontextus a fejlesztőnek)

A **Hókotrók** körökre osztott, kooperatív, lokális multiplayer szimulációs játék. A játékosok hókotrókkal és buszokkal tisztítják a havas/jeges úthálózatot. Cél: a közlekedés fenntartása, hogy minél kevesebb NPC autó akadjon el. A játék akkor ér véget (vereség), ha a be nem ért autók száma túllép egy küszöböt (`Nyilvantarto.nemBeertAutokLimit`, alapból 15).

**Egy kör menete (a grafikus felületen):**
1. **TERVEZES fázis** – az aktív játékos a pályára kattintva kijelöli a saját irányítható járművei (`Hokotro`, `Busz`) útvonalát (útegységek listája), illetve takaríthat, boltolhat. A bolt és a fejlesztések ebben a fázisban érhetők el.
2. **Kör vége** gombbal a vezérlés a következő játékoshoz kerül; ha mindenki tervezett, jön a:
3. **SZIMULACIO fázis** – a `Prototipus.szimulacioTick()` lefuttatja az időjárást (havazás, sóolvasztás) és lépteti az összes járművet (NPC autók, buszok, hókotrók). A View-k az Observer-értesítések hatására frissülnek.
4. Vissza a TERVEZES fázisba a következő körre.

**Pálya elemei (gráf, koordináták nélkül a modellben):** csomópontok (`Csomopont`), utak (`Ut`, két csomópont között), sávok (`Sav`, egyirányú útegység-láncolat), útegységek (`Utegyseg`, a legkisebb egység: hó, jég, só, zúzalék, blokkolt, jármű állapotokkal).

---

## 2. A modell pontos API-ja (ezt használja a View/Controller)

> **Ez a szakasz a hiteles referencia.** Minden metódusnév és szignatúra a tényleges `model` csomag kódjából származik. A View kizárólag **getterekkel** kérdezi le a kirajzoláshoz szükséges adatot; a Controller hívja a **művelet-** és **setter-metódusokat**.

### 2.1 `model.Utegyseg` (a legtöbbet rajzolt elem)

**Állapot-getterek (rajzoláshoz – ezeket kérdezi az `UtegysegView`):**
- `int getHoMagassag()` – hóréteg vastagsága (0 = nincs hó). Elakadási küszöb: `Utegyseg.getHoElakadasKuszob()` = 15.
- `int getJegMagassag()` – jégréteg vastagsága.
- `int getSoMennyiseg()` – kiszórt só mennyisége.
- `boolean getZuzalek()` – van-e zúzalék rajta.
- `boolean getJeges()` – jeges-e a felület.
- `boolean getBlokkolt()` – járhatatlan-e (baleset/mély hó). (Régi diagramokon „foglalt”.)
- `int getLetaposottsag()` – tapodottsági számláló.
- `double getMegcsuszasEsely()`.
- `Jarmu getJarmu()` – a rajta álló jármű, vagy `null`. **A jármű kirajzolásához ezt NEM a View nézi**, hanem fordítva: a `JarmuView` a `jarmu.getUtegyseg()`-ből tudja a pozícióját (ld. 6.5).
- `Utegyseg getKovetkezoUtegyseg()`, `getBalUtegyseg()`, `getJobbUtegyseg()` – szomszédok a rajzoláshoz/láncbejáráshoz.
- `Sav getSav()` – melyik sávhoz tartozik.

**Statikus küszöbök (megjelenítési logikához):**
- `Utegyseg.getHoElakadasKuszob()` = 15, `getLetaposottsagKuszob()` = 5, `getBefedesKuszob()` = 5.

**Műveletek (a Controller NEM hív rajtuk közvetlenül normál játékban – ezek a modell belső léptetéséből futnak):** `havazas(int)`, `sozas(int)`, `jegesedes()`, `taposodas()`, `jegtores()`, `soOlvasztas()`, `tisztulas()`, `boolean megcsuszas()`, `boolean ralep(Jarmu)`.

### 2.2 `model.Jarmu` (abstract) és leszármazottai

**Közös getterek (`JarmuView`-hoz):**
- `Utegyseg getUtegyseg()` – **a jármű aktuális pozíciója** (ezen az útegységen áll). Ez a View pozíciójának forrása.
- `int getSebesseg()`, `int getTapadas()`.
- `boolean getMegcsuszott()`.
- `List<Ut> getKijeloltUtvonal()` – a jármű útvonala (utak).
- `Nyilvantarto getNyilvantarto()`.
- *(Balesethez/elakadáshoz nincs publik getter `baleset`/`elakadt`-ra a `Jarmu`-ban; ezek `protected` mezők. Ld. 8.3 – kell egy getter, vagy a `JarmuView` a blokkolt útegységből következtet. Javaslat: a 8.3 szerint adjunk hozzá `isBaleset()` / `isElakadt()` gettert.)*

**Közös műveletek:** `void lep()`, `void csuszik()`, `void baleset()`, `void elakad()`, `void sikeresLepes(Utegyseg)`, `boolean savValtas(String irany)` (irány: `"bal"`/`"jobb"`/`"-l"`/`"-r"`), `Jarmu keresPartner()`, `void addKijeloltUt(Ut)`, `void removeKijeloltUt(Ut)`.

**`model.Hokotro extends Jarmu implements Iranyithato`:**
- `Fej getFej()`, `void setFej(Fej)` – a felszerelt fej (rajzoláshoz: `fej.getClass().getSimpleName()` adja a típusnevet, pl. „Sopro”).
- `int getZuzalekMennyiseg()`, `int getZuzalekLimit()`.
- `void takarit()` – **a Controller ezt hívja a „takarít” gombra** (a fej `hasznal()`-ja az aktuális útegységen + bevétel a kasszába).
- `void setKijeloltUtegysegek(List<Utegyseg>)` – **Iranyithato**; a TERVEZES fázisban kijelölt útvonal beállítása.
- `static int getBevetel()` – a takarítás bevétele (alap 10).

**`model.Busz extends Jarmu implements Iranyithato`:**
- `Csomopont getVegallomas1()`, `getVegallomas2()`, `List<Csomopont> getMegallokLista()`, `getErintettLista()`, `int getBevetel()`.
- `void megalloErintese(Csomopont)`, `int jutalomKiszamitasa()`.
- `void setKijeloltUtegysegek(List<Utegyseg>)` – **Iranyithato**.

**`model.Auto extends Jarmu implements RendszerIranyitott`:** (NPC – a játékos NEM irányítja)
- `Csomopont getKezdopont()`, `getCelpont()`, `int getUtonToltottIdo()`.
- `List<Ut> utvonalKereses(Terkep)` / `void utvonalKeres(Terkep)`, `boolean nemErBe()`.

### 2.3 Pálya-elemek

**`model.Terkep`:**
- `ArrayList<Ut> getElLista()` – összes út.
- `ArrayList<Csomopont> getCsomopontLista()` – összes csomópont.
- `ArrayList<Ut> utvonalTervezes(Csomopont kezdo, Csomopont veg)` – BFS legrövidebb út (NPC-hez).

**`model.Csomopont`:**
- `boolean getCelpont()`, `boolean getBuszmegallo()`, `String getAzonosito()`, `ArrayList<Ut> getUtLista()`.
- `void jarmuErkezik(Jarmu)`, `void jarmuTavozik(Jarmu)` (modell-belső léptetés).

**`model.Ut`:**
- `Csomopont getVegpont1()`, `getVegpont2()`, `ArrayList<Sav> getSavok()`, `boolean getAlagut()`, `int utHossz()`.

**`model.Sav`:**
- `Utegyseg getElsoUtegyseg()` – a sáv első útegysége (innen láncolható végig `getKovetkezoUtegyseg()`-gel).
- `Csomopont getVegCsomopont()` – a sáv melyik csomópontba vezet (menetirány meghatározása).

### 2.4 `model.Nyilvantarto` (a HUD adatforrása)

- `int getPenz()` – közös kassza.
- `int getSo()`, `int getBiokerozin()`.
- `int getNemBeertAutokSzama()`, `int getNemBeertAutokLimit()` (alap 15).
- `boolean isJatekVege()`.
- *(növelő/csökkentő metódusok: `penzNovel/penzLevon/soNovel/soLevon/biokerozinNovel/biokerozinLevon/nemBeertAutokNovel` – ezeket a modell és a Bolt hívja, nem a View.)*

### 2.5 `model.Bolt` (a BoltPanel modellje; árak + vásárlási műveletek)

**Ár-getterek (a kínálat kilistázásához):**
`getSoAr()`, `getBiokerozinAr()`, `gethokotroAr()`, `getSeproAr()`, `getHanyoAr()`, `getJegtoroAr()`, `getSoszoroAr()`, `getSarkanyAr()`, `getZuzalekAr()`, `getZuzalekszoroAr()`, `getSebessegfejlesztesAr()`, `getTapadasfejlesztesAr()`, `getHozamfejlesztesAr()`.

**Vásárlási műveletek (a Controller ezeket hívja – mindegyik ellenőrzi a pénzt a `Nyilvantarto`-n):**
- Fej-csere egy hókotróra: `soproVasarol(Hokotro)`, `hanyoVasarol(Hokotro)`, `jegtoroVasarol(Hokotro)`, `soszoroVasarol(Hokotro)`, `sarkanyVasarol(Hokotro)`, `zuzalekszoroVasarol(Hokotro)`.
- Erőforrás: `soVasarol(int mennyiseg)`, `biokerozinVasarol(int mennyiseg)`, `zuzalekVasarol(Hokotro, int mennyiseg)`.
- Új hókotró: `hokotroVasarol(Jatekos, Hokotro)`.
- Buszfejlesztés: `sebessegFejlesztes(Busz, int novelesMerteke)`, `tapadasFejlesztes(Busz, int)`, `hozamFejlesztes(Busz, int)`.

> **FIGYELEM – fej-vásárlás API kétértelműség:** a `Bolt.soproVasarol(Hokotro)` egy meglévő `Hokotro`-ra cserél fejet, nem hoz létre új hókotrót. A `hokotroVasarol(Jatekos, Hokotro)` **hozzáad** egy hókotrót a játékos flottájához (a paraméter-hókotrót). A Controllernek ezt a két esetet külön kezelnie kell (ld. 7.7).

### 2.6 `model.Jatekos`

- `List<Iranyithato> getFlotta()` – a játékos járművei (csak `Hokotro`/`Busz`, mert azok `Iranyithato`-k; az `Auto` NPC nincs benne).
- `Bolt getBolt()`, `Nyilvantarto getNyilvantarto()`.

> **Megjegyzés:** a `getFlotta()`/`getNyilvantarto()` jelenleg `System.out.println`-t ír minden híváskor (debug). Ez működést nem befolyásol, csak zajos a konzol. **Ne** javítsd a specifikáció keretében, hacsak külön nem kérjük.

### 2.7 `model.Prototipus` + `ObjektumKatalogus` (a játékállapot gazdája)

A `Prototipus` a teljes játékállapotot egy `ObjektumKatalogus`-ban tartja, és parancssoros felülettel rendelkezik. A grafikus mód ezt használja **háttér-állapottárolóként**.

Elérhető publikus metódusok a `Prototipus`-on:
- `void beolvasFajlbol(String fajlnev)` – parancsfájl (pályatopológia) betöltése.
- `void allapotMentese(String fajlnev)` – állapot mentése.
- `void parancsSorFeldolgoz()` – a betöltött parancssor végrehajtása.
- *(belső: `szimulacioTick(int n)` – jelenleg `private`! Ld. 8.4: kell egy publikus `szimulacio()` vagy `szimulacioTick` átállítása `public`-ra, hogy a Controller hívhassa.)*

> **KRITIKUS LÁTHATÓSÁGI PROBLÉMÁK (ld. 8. szakasz a megoldásokkal):**
> 1. Az `ObjektumKatalogus` osztály **package-private** (`class ObjektumKatalogus`, nincs `public`) – a `controller`/`view` csomag **nem éri el**. → Kell egy publikus hozzáférés a `Prototipus`-on keresztül (pl. `List<Utegyseg> getUtegysegek()`, `getJarmuvek()` stb.), vagy a katalógus `public`-ká tétele.
> 2. A `Soszoro` osztály **package-private** – ha a View típus szerint akarná megkülönböztetni a fejet, nem éri el. Megoldás: a fej típusát `getClass().getSimpleName()` String-gel azonosítjuk, nem `instanceof`-fal.
> 3. A `szimulacioTick(int)` **private** – a Controllernek publikus belépési pont kell a szimulációhoz.

---

## 3. Architektúra és Observer mechanizmus

### 3.1 Réteghatárok (kötelező betartani)

- **Modell** (`model` csomag): nem tud a megjelenítésről. **Nincs** `draw()` / `kirajzol()` benne. Nem ismer szín/ikon/méret fogalmat. Csak annyit tehet, hogy állapotváltozáskor `ertesit()`-tel jelez a feliratkozott megfigyelőknek.
- **View** (`view` csomag): kizárólag a modell **gettereit** hívja a kirajzoláshoz. Nem hív modell-műveletet (nincs benne üzleti logika). Nem ismeri a Controllert (kivéve azokat a paneleket, amelyek listener-eken keresztül a Controllernek delegálnak – ott a Controller referenciát tartanak).
- **Controller** (`controller` csomag): a nyers GUI-eseményeket (egér, billentyű, gomb) értelmezi a játékkontextusban (fázis, aktív játékos), és csak utána hív modell-műveletet. A modell soha nem lát GUI-eseményt.

### 3.2 Push-alapú Observer minta

A modell köralapú, a változások jól lokalizált eseményekhez (lépés, havazás, takarítás, vásárlás) kötődnek. Ezért **push** értesítés: a modell az állapotváltozás végén egyszer hív `ertesit()`-et, és az érintett View-k frissülnek.

**Két új típus (a megvalósítás helye kétféle lehet – ld. 8.1):**

```java
// view/Megfigyelo.java
package view;
public interface Megfigyelo {
    void frissit();   // a megfigyelt modellobjektum változásakor lefutó művelet
}

// model/Megfigyelheto.java  (a MODELL csomagban, mert a modell osztályok implementálják)
package model;
public interface Megfigyelheto {
    void addObserver(Megfigyelo m);
    void removeObserver(Megfigyelo m);
    void ertesit();
}
```

> **Csomag-probléma:** a `model.Megfigyelheto` interfész a `view.Megfigyelo`-ra hivatkozik → a `model` csomagnak importálnia kéne a `view`-t, ami **körkörös függőség** és réteg-sértés. **Megoldás (kötelező):** a `Megfigyelo` interfészt egy **rétegfüggetlen, közös helyre** tesszük. Javasolt: hozz létre egy `common` (vagy `observer`) csomagot mindkét interfésznek:
>
> ```
> common/Megfigyelo.java       (interface frissit())
> common/Megfigyelheto.java    (interface addObserver/removeObserver/ertesit)
> ```
>
> Így a `model` csak a `common`-ra hivatkozik (lefelé/oldalra mutató függőség, nem a `view`-ra), a `view` osztályai pedig `implements common.Megfigyelo`. **Ezt a `common` csomagos megoldást használd.** A dokumentum a továbbiakban a rövid `Megfigyelo`/`Megfigyelheto` neveket írja, ezek a `common` csomagban élnek.

**A megfigyelő-lista kezelése a modell oldalon.** Mivel sok modell-osztály lesz `Megfigyelheto`, és nem akarunk minden osztályban duplikálni, **két lehetőség**:

- **(A) ajánlott:** egy `protected final List<Megfigyelo> megfigyelok = new ArrayList<>()` mezőt és a három metódust (`addObserver`/`removeObserver`/`ertesit`) minden érintett modellosztályba bemásoljuk (Java-ban nincs többszörös öröklés, és a modellosztályoknak már van ősosztályuk/interfészük). Az `ertesit()` törzse mindenhol azonos:
  ```java
  @Override public void addObserver(Megfigyelo m){ if(m!=null && !megfigyelok.contains(m)) megfigyelok.add(m); }
  @Override public void removeObserver(Megfigyelo m){ megfigyelok.remove(m); }
  @Override public void ertesit(){ for (Megfigyelo m : new ArrayList<>(megfigyelok)) m.frissit(); }
  ```
  (Az `ertesit()` másolaton iterál, hogy a `frissit()` közben történő fel-/leiratkozás ne dobjon `ConcurrentModificationException`-t.)

- **(B) ahol lehet ősosztály:** a `Jarmu` abstract osztályba egyszer beletesszük (a `Hokotro`/`Busz`/`Auto` örökli). A `Utegyseg`, `Csomopont`, `Nyilvantarto`, `Bolt` viszont külön kapja meg (nincs közös ős). 

Használd a **(B)** megközelítést a `Jarmu`-nál (kód-duplikáció csökkentése), és az **(A)** mintát a többi (`Utegyseg`, `Csomopont`, `Nyilvantarto`, `Bolt`) osztálynál.

### 3.3 Az értesítési elv összefoglalva

1. **Egy modell-elemhez egy View tartozik.** Minden megfigyelt modellobjektumhoz (pl. egy konkrét `Utegyseg`) egy `View` példány felel meg. 100 útegység → 100 `UtegysegView`, mindegyik csak a saját elemére iratkozik fel.
2. **Az értesítés csak jelzés, nem adat.** Az `ertesit()` a feliratkozott `frissit()`-eket hívja. A View ezután a modell **gettereivel** kérdezi le, amire a kirajzoláshoz szüksége van.
3. **A `frissit()` tipikus törzse** a panel `repaint()`-jét kéri (a tényleges `kirajzol(Graphics2D)` a `paintComponent`-ből fut majd). Mivel a View-elemek (pl. `UtegysegView`) nem önálló Swing-komponensek, hanem a `PalyaPanel` rajzolja ki őket, a `frissit()` a befoglaló panelt jelzi újrarajzolásra. Lásd 6.x a pontos mechanizmusért.

### 3.4 Szálkezelés (EDT)

- Minden Swing-művelet az **Event Dispatch Thread**-en fut. A `JatekIndito.main()` `SwingUtilities.invokeLater(...)`-ben építi fel a felületet.
- A szimulációs léptetés (`tick`) gyors és szinkron → maradhat az EDT-n (egyszerű implementáció). Ha később lassúnak bizonyul, `SwingWorker`-be tehető, de **most ne** bonyolítsd ezzel.
- Az Observer `ertesit()`-ek az EDT-ről hívódnak (gombnyomás / léptetés mind EDT), így a `frissit()` → `repaint()` lánc szálbiztos.


---

## 4. A három képernyő pontos elrendezése (a mockupok alapján)

A `JatekAblak` egy `CardLayout`-tal vált a **Főmenü** és a **Játéknézet** között. A **Bolt** a játéknézet fölött **modális overlay**-ként (vagy modális `JDialog`-ként) jelenik meg.

Ablakméret: indításkor `1200 x 760` px, `setMinimumSize`-zal védve. Középre helyezve (`setLocationRelativeTo(null)`).

### 4.1 Főmenü – `MenuPanel` (lásd: fomenu.png)

```
┌──────────────────────────────────────────────────────────────┐
│                          Hókotrók                              │   ← nagy cím, középre, ~48pt
│                                                                │
│  ┌─────────────┐   ┌────── 1. játékos ──────┐ ... ┌─ n. ──┐    │
│  │játékosok    │   │ [:neve              ]  │     │[:neve]│    │
│  │száma  [ ▼ ] │   │ szerep [ Buszvezető ▼] │     │ szerep│    │
│  │  1 / 2 / 3… │   │        ( Hókotró )     │     │  ...  │    │
│  └─────────────┘   └────────────────────────┘     └───────┘    │
│                                                                │
│                      ┌──────────────┐                          │
│                      │   Új játék    │                         │   ← zöld gomb
│                      └──────────────┘                          │
│                      ┌──────────────┐                          │
│                      │   Kilépés     │                         │   ← zöld gomb
│                      └──────────────┘                          │
└──────────────────────────────────────────────────────────────┘
```

**Komponensek és viselkedés:**
- **Cím:** „Hókotrók”, nagy `JLabel`, vízszintesen középre.
- **Játékosszám-választó:** `JComboBox<Integer>` (1..maxJatekos, pl. 1..4). Értékének változásakor dinamikusan annyi **játékos-kártya** jelenik meg.
- **Játékos-kártya (×N):** keretezett panel (`TitledBorder` „k. játékos” felirattal). Tartalma:
  - `JTextField` a játékos nevének (placeholder `:neve`),
  - `JComboBox<String>` a szereppel: `{"Buszvezető", "Hókotró"}`. (Ez azt vezérli, hogy a játékos kezdő járműve busz vagy hókotró – ld. 7.2 a modell-leképezésért.)
- **Új játék gomb** (zöld): a Controller `ujJatek(...)`-ját hívja a beállított játékos-konfigurációval → betölti a pályát, létrehozza a játékosokat/járműveket, vált a játéknézetre.
- **Kilépés gomb** (zöld): `System.exit(0)`.
- A kártyák egy `JPanel`-ben, `FlowLayout` vagy `GridLayout(1, N)` elrendezésben; ha sok játékos van, `JScrollPane`.

> **A 11. fejezet `MenuPanel` osztálya egyszerűbb** (csak `ujJatekGomb`, `betoltGomb`, `kilepesGomb`). A mockup viszont a **játékos-konfigurációt** is a főmenübe teszi. **A mockup az irányadó:** a `MenuPanel`-be kerül a játékosszám + kártyák + „Új játék” + „Kilépés”. A „betöltés” (korábbi pálya/állapot) opcionális; ha megtartod, tedd egy kisebb „Betöltés” gombként a „Kilépés” mellé.

### 4.2 Játéknézet (lásd: gameplay.png)

`BorderLayout` a játéknézet-konténerben:
- **NORTH:** felső állapotsáv (a HUD egyik fele): bal „Eltelt körök száma: x”, közép „Aktív játékos: <név>”, jobb „Járművek k/n”.
- **CENTER:** `PalyaPanel` (a pálya kirajzolása).
- **EAST:** `HUDPanel` jobb oldali oszlop (kassza, erőforrások, nem beért autók, aktív fej, Bolt gomb, Útvonal infó).
- **SOUTH:** `AkcioSor` (alsó gombok).

```
┌───────────────────────────────────────────────────────────────────────┐
│ Eltelt körök: x      Aktív játékos: Gipsz Jakab        Járművek 1/4      │  ← NORTH (HUD felső)
├──────────────────────────────────────────────────┬────────────────────┤
│                                                    │ Kassza: x mennyiség │
│              [ PÁLYA – PalyaPanel ]                │ Só: x  Biokerozin:y │
│   csomópontok (H/M/W körök), utak, sávok,          │ Zúzalék: z          │
│   útegységek hó/jég/só/zúzalék/blokkolt állapottal,│ nem beért: x/15 db  │
│   járművek (H = hókotró, B = busz, NPC autó),      │ aktív Fej: Söprő    │
│   kijelölt útvonal (lila nyilak)                   │ ┌────────────────┐  │
│                                                    │ │      Bolt      │  │  ← EAST (HUD jobb)
│                                                    │ └────────────────┘  │
│                                                    │ Útvonal:            │
│                                                    │  :kijelölt jármű    │
│                                                    │  :jármű tulajdonosa │
│                                                    │  {u1,u2,…un}        │
├──────────────────────────────────────────────────┴────────────────────┤
│ [Útvonal kijelölés]              [takarít]   [Kör vége]                  │  ← SOUTH (AkcioSor)
└───────────────────────────────────────────────────────────────────────┘
```

**Megjelölések a pályán (egyszerű, nem „csilivili” – ahogy a feladat engedi):**
- **Csomópont:** kör, benne a `getAzonosito()` betűje/száma. Célpont/buszmegálló külön színnel vagy kerettel (pl. célpont = telt kör, buszmegálló = dupla keret). A H/M/W betűk a mockupban csomópont-azonosítók.
- **Útegység:** kis négyzet a sávban. Színkód az állapot szerint (ld. 6.4): tiszta = világos, hó = fehéres/szürke árnyalat a magassággal, jég = kék, sózott = más árnyalat, zúzalék = pöttyös/sötétebb, blokkolt = piros keret/áthúzás.
- **Jármű:** betűs jelölés négyzetben az útegység fölött: `H` = hókotró, `B` = busz, NPC autó = kis színes négyzet (pl. piros). Baleset = külön jelzés (pl. piros X vagy keret).
- **Sáv menetiránya:** kis nyíl a sáv mentén (`getVegCsomopont()` felé).
- **Kijelölt útvonal (TERVEZES):** a kijelölt útegységek kiemelése (pl. lila keret/nyíl), ahogy a mockup mutatja.
- **Alagút:** `Ut.getAlagut()==true` → szaggatott körvonal vagy más jelölés.

### 4.3 Bolt – `BoltPanel` (lásd: bolt.png)

Modális overlay a játéknézet fölött. Fejléc „Bolt” + jobb felül „X” (bezár) gomb.

```
┌──────────────────────────────────────────────────────────── [X] ┐
│  Bolt                                                            │
│                                                                  │
│   Kategória                 Kínálat                              │
│  ┌────────────┐    ┌──────────────────────────┬────────┐         │
│  │ Fejek      │    │ Söprő fej                │  50 Ft │  ▲      │
│  │ Erőforrás  │    │ Hányó fej                │  80 Ft │  │      │
│  │ Buszfejl.  │    │ Jégtörő fej              │ 100 Ft │  ▼      │
│  │ Új jármű   │    │ Sószóró fej              │ 150 Ft │         │
│  └────────────┘    │ Sárkány fej              │ 300 Ft │         │
│                    └──────────────────────────┴────────┘         │
│  ┌────────────────────────────────────────────────────────────┐ │
│  │ Kiválasztva: Söprő fej                                      │ │
│  │ "A havat és a feltört jeget a jobb oldali sávba tolja át…"   │ │
│  │ Felszerelendő hókotró:  [ Hokotro 1     ▼ ]                  │ │
│  └────────────────────────────────────────────────────────────┘ │
│        ┌───────────┐                      ┌───────────┐          │
│        │  Megvesz   │                     │   Mégse    │         │
│        └───────────┘                      └───────────┘          │
└──────────────────────────────────────────────────────────────────┘
```

**Komponensek és viselkedés:**
- **Kategória lista** (`JList<String>` bal oldalt): `{ "Fejek", "Erőforrás", "Buszfejlesztés", "Új jármű" }`.
- **Kínálat lista/táblázat** (jobb oldalt, görgethető): a kiválasztott kategória elemei névvel és árral. `JList` egyéni renderer-rel, vagy `JTable` 2 oszloppal (név, ár). Tartalom kategóriánként:
  - **Fejek:** Söprő (`getSeproAr()`), Hányó (`getHanyoAr()`), Jégtörő (`getJegtoroAr()`), Sószóró (`getSoszoroAr()`), Sárkány (`getSarkanyAr()`), Zúzalékszóró (`getZuzalekszoroAr()`).
  - **Erőforrás:** Só (`getSoAr()` /egység), Biokerozin (`getBiokerozinAr()` /egység), Zúzalék (`getZuzalekAr()` /egység). Itt mennyiség is megadható (lásd lent).
  - **Buszfejlesztés:** Sebesség (`getSebessegfejlesztesAr()`), Tapadás (`getTapadasfejlesztesAr()`), Hozam (`getHozamfejlesztesAr()`).
  - **Új jármű:** Hókotró (`gethokotroAr()`).
- **Részletek panel** (alul, kék): mutatja a kiválasztott áru nevét, rövid leírását, és egy **célválasztó comboboxot**:
  - Fej/Zúzalék vásárlásnál: „Felszerelendő hókotró” – a játékos hókotróinak listája (`Hokotro` példányok a flottából).
  - Buszfejlesztésnél: „Fejlesztendő busz” – a játékos buszainak listája.
  - Erőforrásnál (só/biokerozin): mennyiség `JSpinner` (1..N).
  - Új járműnél: nincs célválasztó (új hókotró jön létre).
- **Megvesz gomb** (zöld): a Controller `vasarol(...)`-ját hívja a kiválasztott áruval és célponttal (ld. 7.7).
- **Mégse / X gomb**: bezárja a boltot, vissza TERVEZES fázisba.

> **A részlet-leírások szövegei** (pl. „A havat és a feltört jeget a jobb oldali sávba tolja át…”) statikus, a fej működését bemutató stringek a `BoltPanel`-ben (nem a modellből jönnek). Adj rövid, 1 mondatos leírást minden áruhoz.


---

## 5. Controller réteg

### 5.1 `controller.Fazis` (enum)

```java
public enum Fazis {
    TERVEZES,    // útvonal-kijelölés, takarítás, bolt elérhető
    SZIMULACIO,  // járművek léptetése, modell-szimuláció fut
    BOLT         // a bolt felület aktív (a TERVEZES-en belüli modális állapot)
}
```

### 5.2 `controller.JatekController` (a vezérlési központ)

**Felelősség:** összeköti a `Prototipus`-t (modellállapot), a `JatekAblak`-ot (GUI), az aktuális játékost, a fázist és a jármű-nézeteket. Implementálja a `Megfigyelo`-t (hogy a globális frissítésekre is reagálhasson, pl. játék vége).

**Attribútumok:**
- `private Prototipus proto;` – a meglévő modell-állapot gazdája.
- `private JatekAblak ablak;`
- `private List<Jatekos> jatekosok;` – a játékosok körönkénti sorrendben.
- `private int aktualisJatekosIndex;` – melyik játékos van soron.
- `private Jatekos aktualisJatekos;` – kényelmi referencia.
- `private Fazis aktualisFazis;`
- `private Terkep terkep;` – a betöltött pálya.
- `private PalyaLayout layout;` – a pályaelem-pozíciók.
- `private Map<Jarmu, JarmuView> jarmuViewk;` – jármű → nézet nyilvántartás (új jármű/balesetkor frissítendő).
- `private Iranyithato kijeloltJarmu;` – épp melyik jármű útvonalát szerkeszti a játékos (TERVEZES-ben).
- `private List<Utegyseg> kijeloltUtegysegek;` – az épp készülő útvonal.

**Metódusok (a 11. fejezet + a mockup alapján):**

| Metódus | Mit csinál |
|---|---|
| `ujJatek(List<JatekosKonfig> konfig)` | A főmenü „Új játék”-ára. Pálya betöltése (`palyaBetolt`), játékosok + kezdő járművek létrehozása a konfig alapján, `osszekot()`, fázis = TERVEZES, váltás a játéknézetre. |
| `palyaBetolt(String fajlnev)` | A pálya topológiáját betölti a `proto.beolvasFajlbol(...)` + `parancsSorFeldolgoz()` úton; a `PalyaLayout.betolt(layoutFajl)` a pozíciókat. Beállítja a `terkep` referenciát (ld. 8.2 – getter kell a Prototipusból). |
| `osszekot()` | Létrehozza és összekapcsolja a modellobjektumokhoz tartozó nézeteket (minden `Utegyseg`-hez `UtegysegView`, `Csomopont`-hoz `CsomopontView`, `Jarmu`-höz a megfelelő `JarmuView`), és feliratkoztatja őket (`addObserver`). A `HUDPanel`-t a `Nyilvantarto`-ra iratja fel. (Ld. 11.4.2 szekvencia.) |
| `utegysegValasztva(Utegyseg ue)` | Pályakattintásra (TERVEZES). Hozzáadja `ue`-t a `kijeloltUtegysegek`-hez és kéri a `PalyaPanel` újrarajzolását. |
| `kijeloltJarmuValt(Iranyithato j)` | Beállítja, melyik jármű útvonalát szerkesztjük. |
| `utvonalVeglegesit()` | A „kész” jelzésre (pl. dupla kattintás vagy Enter): `kijeloltJarmu.setKijeloltUtegysegek(kijeloltUtegysegek)`, majd üríti a `kijeloltUtegysegek`-et. |
| `korVegeKattintas()` | Lezárja az aktuális játékos tervezését; ha van még játékos, `kovetkezoJatekos()`; ha mindenki kész, `szimulacioLepes()`. |
| `kovetkezoJatekos()` | Vezérlés a következő játékoshoz; HUD frissítés (aktív játékos). |
| `lepesKattintas()` | (opcionális, ha külön „lépés” gomb van) egy szimulációs elemi lépés. |
| `takaritKattintas()` | Az aktuálisan kijelölt **hókotró** `takarit()` hívása, majd `ertesit()` az érintett útegységen/nyilvántartón. |
| `boltNyit()` | Fázis = BOLT, `ablak.mutatBolt()`, a `BoltPanel`-t felkészíti az aktuális játékossal. |
| `boltBezar()` | Fázis = TERVEZES, `ablak.mutatJatek()`. |
| `vasarol(String kategoria, String aru, Object cel, int mennyiseg)` | A bolti „Megvesz”-re. A kategória+áru+cél alapján a megfelelő `Bolt.*Vasarol(...)`-t hívja (ld. 7.7). Vásárlás után a `Bolt`/`Nyilvantarto`/`Hokotro` `ertesit()`- je frissíti a HUD-ot és a boltot. |
| `szimulacioLepes()` | A SZIMULACIO fázist futtatja: fázis = SZIMULACIO, `AkcioSor.fazisFrissites(SZIMULACIO)` (gombok tiltása), majd a `proto` szimulációs tick-je (ld. 8.4). A léptetés Observer-értesítései frissítik a View-kat. A végén fázis = TERVEZES, `aktualisJatekosIndex=0`, HUD frissítés, körök számláló++. Ha `Nyilvantarto.isJatekVege()` → játék vége képernyő/üzenet. |
| `allapotMent(String fajlnev)` | `proto.allapotMentese(fajlnev)`. |
| `frissit()` | `Megfigyelo` – globális modellváltozásra (pl. játék vége) reagál. |

**Fázis-szabályok (kötelező betartani):**
- **TERVEZES:** engedélyezett: pálya-kattintás (útvonal), „takarít”, „Bolt”, „Kör vége”. Tiltott: szimulációs léptetés (az automatikus a kör végén fut).
- **SZIMULACIO:** minden játékos-input tiltva; csak a léptetés fut, View-k frissülnek; a végén automatikus visszaváltás TERVEZES-re.
- **BOLT:** csak a bolti műveletek aktívak; a pálya-kattintás és a „Kör vége” tiltva, amíg a bolt nyitva van.

> **`JatekosKonfig`** egy egyszerű segéd-rekord/osztály a Controllerben (vagy a `view`-ban): `{ String nev; String szerep; }` ahol szerep ∈ {„Buszvezető”,„Hókotró”}.

### 5.3 `controller.BemenetKezelo`

**Felelősség:** a nyers egér-/billentyű-eseményeket fordítja Controller-hívásokká. `extends MouseAdapter implements KeyListener`. Egy `JatekController controller` referenciát tart.

- `mouseClicked(MouseEvent e)`: a `PalyaPanel.utegysegKattintas(e.getX(), e.getY())`-nal megállapítja az érintett `Utegyseg`-et; ha van és a fázis TERVEZES, hívja `controller.utegysegValasztva(ue)`-t. Dupla kattintás → `controller.utvonalVeglegesit()`.
- `keyPressed(KeyEvent e)`: gyorsbillentyűk → játékműveletek. Javasolt kiosztás:
  - `SPACE` / `Enter` = Kör vége (`korVegeKattintas`),
  - `T` = takarít (`takaritKattintas`),
  - `B` = Bolt nyit/zár (`boltNyit`/`boltBezar`),
  - `ESC` = Bolt bezár / kijelölés megszakítás.

> A `BemenetKezelo`-t a `PalyaPanel` (és/vagy a `JatekAblak`) regisztrálja egér-/billentyű-figyelőként. A gombok (`AkcioSor`, `MenuPanel`, `BoltPanel`) viszont `ActionListener`-eken keresztül **közvetlenül** a Controller megfelelő metódusát hívják – ezeket nem a `BemenetKezelo` közvetíti.

### 5.4 `controller.JatekIndito` (grafikus belépési pont)

```java
public class JatekIndito {
    public static void main(String[] args) {
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); }
        catch (Exception ignored) {}
        SwingUtilities.invokeLater(() -> {
            Prototipus proto = new Prototipus();
            JatekController controller = new JatekController(proto);
            JatekAblak ablak = new JatekAblak(controller);
            controller.setAblak(ablak);     // vagy konstruktorba átadva
            ablak.mutatMenu();
            ablak.setVisible(true);
        });
    }
}
```


---

## 6. View réteg – osztályonkénti szerződés

> **Közös elv:** a View-elemek (`CsomopontView`, `UtView`, `SavView`, `UtegysegView`, `JarmuView` leszármazottai) **nem önálló Swing-komponensek**. Ezeket a `PalyaPanel` rajzolja ki a `paintComponent`-jéből, a megfelelő sorrendben (utak → sávok → útegységek → csomópontok → járművek → kijelölések). A `frissit()`-jük a befoglaló `PalyaPanel.repaint()`-jét kéri. A `HUDPanel` és `BoltPanel` viszont valódi `JPanel`-ek, saját `paintComponent`/komponens-tartalommal.

### 6.1 `view.Pont` (értékosztály)

```java
public class Pont {
    public int x;
    public int y;
    public Pont(int x, int y) { this.x = x; this.y = y; }
}
```

### 6.2 `view.PalyaLayout`

A modell **koordináta nélküli** (gráf). A grafikának pozíció kell. A `PalyaLayout` rendeli a csomópontokhoz a képernyőpozíciót, a modell ettől **nem módosul**.

- **Attribútumok:**
  - `private Map<Csomopont, Pont> csomopontPoziciok;`
  - `private int mertekSzelesseg;` – egy útegység rajzolt alapmérete (pl. 18 px).
- **Metódusok:**
  - `Pont getPozicio(Csomopont cs)` – csomópont képernyőpozíciója.
  - `void setPozicio(Csomopont cs, Pont p)`.
  - `void betolt(String fajl)` – a `palya_layout.txt`-ből (ld. 9. szakasz) tölti a pozíciókat. A csomópontokat az **azonosító** (`Csomopont.getAzonosito()`) alapján párosítja a `terkep` csomópontjaihoz. (Ehhez a `betolt` kapja meg a `Terkep`-et is, vagy a Controller hívja párosítva – javaslat: `void betolt(String fajl, Terkep terkep)`.)
  - `void ment(String fajl)` – pozíciók mentése.
  - **Segéd (útegység-pozíció számítás):** mivel az útegységek egy `Ut` két végpontja közti `Sav`-ban láncolódnak, az `UtegysegView`/`UtView` pozícióját a `PalyaLayout` számolja két csomópont-pozícióból: az út egyenesét felosztja az `utHossz()` szerint, a párhuzamos sávokat merőleges eltolással helyezi egymás mellé. Tedd ezt egy `Pont getUtegysegPozicio(Ut ut, int savIndex, int egysegIndex)` segédmetódusba, hogy a rajzolás és a kattintás-találat (`tartalmaz`) ugyanazt a geometriát használja (ne duplikálódjon).

### 6.3 `view.Megfigyelo` / `common.Megfigyelo` + `view.View` (abstract)

```java
// common/Megfigyelo.java
public interface Megfigyelo { void frissit(); }

// view/View.java
public abstract class View implements Megfigyelo {
    protected Megfigyelheto modellElem;     // a megfigyelt modellobjektum
    public View(Megfigyelheto elem) { this.modellElem = elem; }
    @Override public void frissit() { /* tipikusan a befoglaló panel repaint()-je */ }
    public abstract void kirajzol(Graphics2D g);
}
```

> A konkrét `frissit()` viselkedés: a View kap egy referenciát a befoglaló `PalyaPanel`-re (konstruktorban vagy setterrel), és a `frissit()` ennek `repaint()`-jét hívja. Egyszerűbb alternatíva: a modell `ertesit()`-je helyett a `PalyaPanel` egyetlen megfigyelőként a teljes panelt rajzolja újra – de a 11. fejezet az „egy elem – egy View” elvet írja elő, ezért a View-nként `repaint()` a követendő (a Swing a `repaint()`-eket úgyis összevonja).

### 6.4 `view.UtegysegView`

- **Ősosztály:** `View`. **Megfigyelő:** a saját `Utegyseg`-ére.
- **Attribútumok:**
  - `private Utegyseg utegyseg;`
  - `private Rectangle befoglaloTeglalap;` – a képernyőn elfoglalt téglalap (a `PalyaLayout` geometriájából).
- **Metódusok:**
  - `void kirajzol(Graphics2D g)` – kirajzolja az útegység aktuális állapotát a `befoglaloTeglalap`-ba. **Színkód (getterekből):**
    - `getBlokkolt()` → piros keret / áthúzás (mindenek fölött).
    - `getJeges()` vagy `getJegMagassag()>0` → kék kitöltés (sötétebb nagyobb `getJegMagassag()`-nél).
    - `getHoMagassag()>0` → fehér/szürke kitöltés (sötétebb nagyobb hónál); ha eléri `getHoElakadasKuszob()`-öt, vastagabb jelzés.
    - `getZuzalek()` → pöttyös/szemcsés overlay.
    - `getSoMennyiseg()>0` → halvány sárgás árnyalat / só-jelzés.
    - alapeset (tiszta) → világos semleges szín.
  - `boolean tartalmaz(int x, int y)` – igaz, ha a képernyőpont a `befoglaloTeglalap`-on belül van (kattintás-találat).
  - `void frissit()` – a `PalyaPanel.repaint()`-jét kéri.

### 6.5 `view.JarmuView` (abstract) + `HokotroView` / `BuszView` / `AutoView`

- **`JarmuView` (abstract) extends `View`:**
  - `protected Jarmu jarmu;`
  - `void kirajzol(Graphics2D g)` – abstract.
  - **Pozíció:** a jármű mindig a `jarmu.getUtegyseg()`-en áll → a kirajzoláshoz az **adott útegység képernyőpozícióját** kell lekérni (a `PalyaPanel`/`PalyaLayout` geometriájából). Ezért a `JarmuView` kap referenciát a `PalyaPanel`-re vagy a `PalyaLayout`-ra a pozíció-feloldáshoz. **Ne** tárolj fix pozíciót a járműben – minden rajzoláskor a `getUtegyseg()`-ből kérdezd le, mert a jármű mozog.
- **`HokotroView`:** `private Hokotro hokotro;` – `kirajzol`: „H” betűs négyzet az útegység fölött + a felszerelt fej jelzése (`hokotro.getFej().getClass().getSimpleName()` rövidítése, pl. „Sö”, „Há”, „Jé”, „Só”, „Sá”, „Zú”).
- **`BuszView`:** `private Busz busz;` – `kirajzol`: „B” betűs négyzet.
- **`AutoView`:** `private Auto auto;` – `kirajzol`: kis piros négyzet (NPC); baleset esetén külön jelzés.
- **Baleset/elakadás jelzés:** ld. 8.3 – ha hozzáadod az `isBaleset()`/`isElakadt()` gettereket, a `JarmuView` ezekből rajzol baleset-jelet; ha nem, az útegység `getBlokkolt()` állapotából következtethetsz (kevésbé pontos).

### 6.6 `view.CsomopontView`

- `private Csomopont csomopont; private int x, y;` (a `PalyaLayout`-ból).
- `void kirajzol(Graphics2D g)` – kör + `csomopont.getAzonosito()` felirat. `getCelpont()` → telt/kiemelt; `getBuszmegallo()` → dupla keret vagy „megálló” jelzés.
- `void frissit()` – `PalyaPanel.repaint()`.

### 6.7 `view.UtView` és `view.SavView`

- **`UtView`:** `private Ut ut; private List<SavView> savViewk;` – `kirajzol`: az utat (vonalat a két csomópont-pozíció között) és a hozzá tartozó sávokat rajzolja. `getAlagut()` → szaggatott. (`Ut` jelenleg **nem** `Megfigyelheto` – ld. 8.3: nem feltétlenül kell azzá tenni, mert az út statikus topológia; az `UtView` lehet „passzív”, csak a `PalyaPanel` rajzolja, nem iratkozik fel. **Döntés:** az `UtView`/`SavView` ne legyen Observer, csak rajzoló segéd.)
- **`SavView`:** `private Sav sav; private List<UtegysegView> utegysegViewk;` – a sávot és a benne lévő útegységeket fogja össze; a sáv menetirány-nyilát rajzolja a `sav.getVegCsomopont()` felé.

### 6.8 `view.PalyaPanel`

- **Ősosztály:** `JPanel`. **Interfész:** `MouseListener` (a `BemenetKezelo`-n keresztül).
- **Attribútumok:**
  - `private PalyaLayout layout;`
  - `private Map<Csomopont, CsomopontView> csomopontViewk;`
  - `private Map<Utegyseg, UtegysegView> utegysegViewk;`
  - `private Map<Jarmu, JarmuView> jarmuViewk;`
  - `private List<UtView> utViewk;` (és/vagy `SavView`-k)
  - `private List<Utegyseg> kijeloltUtegysegek;` – a játékos által épp kijelölt útvonal (a Controllertől kapja megjelenítésre).
- **Metódusok:**
  - `void paintComponent(Graphics g)` – sorrend: utak → sávok → útegységek → csomópontok → járművek → kijelölt útvonal kiemelése. Mindegyikhez a megfelelő View `kirajzol((Graphics2D)g)`-jét hívja.
  - `Utegyseg utegysegKattintas(int x, int y)` – végigkérdezi az `UtegysegView`-kat `tartalmaz(x,y)`-nal, és visszaadja az elsőt, amelyik tartalmazza a pontot (vagy `null`).
  - `void setKijeloltUtegysegek(List<Utegyseg> lista)` + `repaint()`.
  - `void ujraRajzol()` → `repaint()`.
  - A `csomopontViewk`/`utegysegViewk`/`jarmuViewk` map-eket a Controller `osszekot()`-ja tölti fel (vagy a Controller adja át a panelnek).

### 6.9 `view.HUDPanel`

- **Ősosztály:** `JPanel`. **Interfész:** `Megfigyelo` (feliratkozik a `Nyilvantarto`-ra).
- **Attribútumok:** `private Nyilvantarto nyilvantarto; private Jatekos aktualisJatekos;` + a kijelzett `JLabel`-ek (kassza, só, biokerozin, zúzalék-összesítő, nem beért autók `x/limit`, aktív fej, körök száma, aktív játékos neve, „Útvonal” infó).
- **Metódusok:**
  - `void frissit()` – a `Nyilvantarto` getterekből frissíti a `JLabel`-eket, majd `repaint()`. (Vagy `paintComponent`-ben rajzol – de label-alapú egyszerűbb.)
  - `void setAktualisJatekos(Jatekos j)`, `void setKorokSzama(int k)`, `void setAktivFej(String fejNev)`, `void setUtvonalInfo(...)` – a Controller hívja.
- A „Bolt” gomb a HUD jobb oldalán is lehet (a mockup szerint), vagy az `AkcioSor`-ban (a 11. fejezet szerint). **Tedd a HUD-ba** a mockuphoz hűen, `ActionListener`-rel a `controller.boltNyit()`-ra.

### 6.10 `view.AkcioSor`

- **Ősosztály:** `JPanel`. **Interfész:** `ActionListener`.
- **Attribútumok:** `private JatekController controller; private JButton utvonalKijelolesGomb, takaritGomb, korVegeGomb;` (és opc. `lepesGomb`). A „Bolt” gomb lehet itt vagy a HUD-ban.
- **Metódusok:**
  - `void fazisFrissites(Fazis f)` – az aktuális fázis szerint engedélyezi/tiltja a gombokat (SZIMULACIO alatt minden tiltva; BOLT alatt csak a bolti zár aktív; TERVEZES alatt minden aktív).
  - A gombok `ActionListener`-jei a `controller` megfelelő metódusát hívják (`utvonalVeglegesit`/útvonal-mód, `takaritKattintas`, `korVegeKattintas`).

### 6.11 `view.BoltPanel`

- **Ősosztály:** `JPanel` (modális overlay / `JDialog` tartalma). **Interfész:** `Megfigyelo` (feliratkozik a `Bolt`-ra, hogy vásárlás után frissüljön).
- **Attribútumok:** `private Bolt bolt; private JatekController controller; private Jatekos aktualisJatekos; private JList<String> kategoriaLista; private JList<...> aruLista; private JPanel reszletekPanel;` + cél-választó komponensek (hókotró-combo, busz-combo, mennyiség-spinner).
- **Metódusok:**
  - `void mutat()` / `void rejt()` – megjeleníti/elrejti a boltot.
  - `void frissit()` – a `Bolt` ár-getterekből és a `Nyilvantarto`-ból frissíti a kínálatot és a kiírt egyenleget.
  - `void setAktualisJatekos(Jatekos j)` – beállítja, kinek a flottájából tölti a cél-comboboxokat.
  - A kategória-választás frissíti az áru-listát; az áru-választás a részletek panelt és a megfelelő cél-választót; a „Megvesz” a `controller.vasarol(kategoria, aru, cel, mennyiseg)`-et hívja; a „Mégse”/„X” a `controller.boltBezar()`-t.

### 6.12 `view.MenuPanel`

- **Ősosztály:** `JPanel`. **Interfész:** `ActionListener`.
- **Attribútumok:** `private JatekController controller; private JComboBox<Integer> jatekosSzamCombo; private List<PlayerCard> kartyak; private JButton ujJatekGomb, kilepesGomb;` (és opc. `betoltGomb`). Egy belső `PlayerCard` komponens: `JTextField nevMezo; JComboBox<String> szerepCombo;`.
- **Metódusok:**
  - `void frissitKartyak(int n)` – a játékosszám változásakor `n` kártyát jelenít meg.
  - Az „Új játék” `ActionListener`-je összegyűjti a kártyák (név, szerep) adatait egy `List<JatekosKonfig>`-ba, és `controller.ujJatek(konfig)`-ot hív. A „Kilépés” `System.exit(0)`.

### 6.13 `view.JatekAblak`

- **Ősosztály:** `JFrame`. Tartalmazza a menüt és a játéknézetet `CardLayout`-tal; a boltot overlay-ként.
- **Attribútumok:** `private JatekController controller; private MenuPanel menuPanel; private PalyaPanel palyaPanel; private HUDPanel hudPanel; private AkcioSor akcioSor; private BoltPanel boltPanel;`
- **Metódusok:**
  - `void mutatMenu()` – a menü kártyát mutatja.
  - `void mutatJatek()` – a játéknézetet (`PalyaPanel`+`HUDPanel`+`AkcioSor`) mutatja.
  - `void mutatBolt()` – a `BoltPanel`-t overlay-ként/modálisan megjeleníti.
  - `void ujraRajzol()` – teljes újrarajzolás.
  - Getterek a panelekhez, hogy a Controller `osszekot()`-ja hozzájuk férjen.


---

## 7. Use-case → modell-leképezés (minden funkció elérhetővé tétele)

> A cél, hogy **a modell összes funkciója** elérhető legyen a GUI-ról. Az alábbi táblázatok megadják, melyik GUI-művelet melyik modell-hívásra fordul. A Controller a fázist és az aktív játékost figyelembe veszi.

### 7.1 Játék indítása / pálya betöltése
| GUI | Controller | Modell-hívás |
|---|---|---|
| „Új játék” gomb | `ujJatek(konfig)` | `proto.beolvasFajlbol(pályafájl)` → `proto.parancsSorFeldolgoz()`; `layout.betolt(layoutFájl, terkep)`; játékos+jármű létrehozás (7.2); `osszekot()` |
| „Betöltés” (opc.) | `palyaBetolt(fájl)` | `proto.beolvasFajlbol(...)` + `parancsSorFeldolgoz()` |

### 7.2 Játékos és kezdő jármű létrehozása (a főmenü konfigból)
A főmenü minden játékos-kártyájához egy `Jatekos` jön létre, és a **szerep** szerint egy kezdő jármű:
- szerep „Hókotró” → `new Hokotro()` (alap `Sopro` fejjel a konstruktorból), felvéve a játékos flottájába és a térkép egy kezdő útegységére.
- szerep „Buszvezető” → `new Busz()`, a flottába + egy útegységre, végállomásokkal.
- Minden járműhöz `jarmu.setNyilvantarto(kozosNyilvantarto)`; a játékoshoz `jatekos.setBolt(bolt)` és `jatekos.setNyilvantarto(kozosNyilvantarto)`.
- A `Bolt` és `Nyilvantarto` **közös** minden játékosnak (kooperatív játék, közös kassza).

> A kezdő járművek útegységre helyezése: `utegyseg.ralep(jarmu)` vagy közvetlen `jarmu.setUtegyseg(ue)` + `ue.setJarmu(jarmu)`. A pálya kezdőpozíciói a `palya_layout.txt`/pályafájl részei lehetnek.

### 7.3 Útvonal kijelölése (TERVEZES)
| GUI | Controller | Modell |
|---|---|---|
| jármű kiválasztása (kattintás a járműre / lista) | `kijeloltJarmuValt(j)` | – |
| útegységre kattintás | `utegysegValasztva(ue)` | `kijeloltUtegysegek.add(ue)` |
| „kész” (dupla katt / Enter / „Útvonal kijelölés” gomb vége) | `utvonalVeglegesit()` | `((Iranyithato)j).setKijeloltUtegysegek(kijeloltUtegysegek)` |

> A `Hokotro`/`Busz` az `Iranyithato`-n keresztül kapja az útegység-listát. Az NPC `Auto` útvonalát a **rendszer** tervezi (`utvonalKereses(terkep)`), a játékos nem.

### 7.4 Takarítás
| GUI | Controller | Modell |
|---|---|---|
| „takarít” gomb (aktív hókotró kijelölve) | `takaritKattintas()` | `hokotro.takarit()` → a fej `hasznal(utegyseg)`-e + siker esetén `nyilvantarto.penzNovel(Hokotro.getBevetel())` |

A `takarit()` után az érintett `Utegyseg` és a `Nyilvantarto` `ertesit()`-je frissíti a pálya- és HUD-nézetet.

### 7.5 Kör vége / következő játékos / szimuláció
| GUI | Controller | Modell |
|---|---|---|
| „Kör vége” gomb | `korVegeKattintas()` | ha van még játékos: `kovetkezoJatekos()`; különben `szimulacioLepes()` |
| (automatikus a kör végén) | `szimulacioLepes()` | `proto` szimulációs tick (8.4): útegységek `soOlvasztas()`+`havazas(1)`, `Auto.lep()` (+`nemErBe()`), `Busz.lep()`, `Hokotro.lep()`, `Nyilvantarto.ellenorizJatekVege()` |

### 7.6 Bolt megnyitása/bezárása
| GUI | Controller | Modell |
|---|---|---|
| „Bolt” gomb | `boltNyit()` | fázis=BOLT, `boltPanel.setAktualisJatekos(aktualisJatekos)`, `ablak.mutatBolt()` |
| „Mégse”/„X” | `boltBezar()` | fázis=TERVEZES, `ablak.mutatJatek()` |

### 7.7 Vásárlás a boltban (`vasarol(kategoria, aru, cel, mennyiseg)`)
A Controller a kategória+áru alapján választ:

**Fejek** (cel = kiválasztott `Hokotro` a flottából):
| Áru | Modell-hívás |
|---|---|
| Söprő | `bolt.soproVasarol((Hokotro)cel)` |
| Hányó | `bolt.hanyoVasarol((Hokotro)cel)` |
| Jégtörő | `bolt.jegtoroVasarol((Hokotro)cel)` |
| Sószóró | `bolt.soszoroVasarol((Hokotro)cel)` |
| Sárkány | `bolt.sarkanyVasarol((Hokotro)cel)` |
| Zúzalékszóró | `bolt.zuzalekszoroVasarol((Hokotro)cel)` |

**Erőforrás:**
| Áru | Modell-hívás |
|---|---|
| Só (mennyiség) | `bolt.soVasarol(mennyiseg)` |
| Biokerozin (mennyiség) | `bolt.biokerozinVasarol(mennyiseg)` |
| Zúzalék (cel = `Hokotro`, mennyiség) | `bolt.zuzalekVasarol((Hokotro)cel, mennyiseg)` |

**Buszfejlesztés** (cel = kiválasztott `Busz`; a növelés mértéke a modellben a hívó adja, pl. 10):
| Áru | Modell-hívás |
|---|---|
| Sebesség | `bolt.sebessegFejlesztes((Busz)cel, 10)` |
| Tapadás | `bolt.tapadasFejlesztes((Busz)cel, 10)` |
| Hozam | `bolt.hozamFejlesztes((Busz)cel, 10)` |

**Új jármű:**
| Áru | Modell-hívás |
|---|---|
| Hókotró | `Hokotro uj = new Hokotro(); uj.setNyilvantarto(nyilvantarto); bolt.hokotroVasarol(aktualisJatekos, uj);` majd elhelyezés egy kezdő útegységre + `JarmuView` létrehozás + feliratkoztatás (`osszekot` részleges futtatása az új járműre). |

> Minden vásárlás után a `Bolt`/`Nyilvantarto`/`Hokotro` `ertesit()`-je frissíti a `BoltPanel`-t és a `HUDPanel`-t. **Új jármű** esetén a Controllernek a `jarmuViewk` map-be új `HokotroView`-t kell tennie és feliratkoztatnia.

### 7.8 Mentés (opcionális)
| GUI | Controller | Modell |
|---|---|---|
| „Mentés” (ha készítesz menüt) | `allapotMent(fájl)` | `proto.allapotMentese(fájl)` + `layout.ment(layoutFájl)` |

### 7.9 Lefedettségi ellenőrzés
A fenti táblázatok lefedik a modell **összes játékos-vezérelt** funkcióját: útvonal-kijelölés (Iranyithato), takarítás (fejek: söprő/hányó/jégtörő/sószóró/sárkány/zúzalékszóró működése a `takarit()`→`hasznal()` láncon át), erőforrás- és fej-vásárlás, buszfejlesztés, új hókotró, szimuláció (havazás, sóolvasztás, NPC/busz/hókotró léptetés, baleset, elakadás, jegesedés, be nem ért autók, játék vége). A nem játékos-vezérelt műveletek (pl. `Utegyseg.jegesedes()`, `taposodas()`, `Csomopont.jarmuErkezik()`) a szimulációs tick / `lep()` láncon át automatikusan futnak, és Observer-értesítéssel jelennek meg.


---

## 8. Szükséges modell-módosítások (minimális, pontos lista)

> Cél: a modellhez **a lehető legkevesebb** hozzányúlás. Az alábbiak az egyetlen engedélyezett változtatások. Minden más modellosztály **változatlan** marad.

### 8.1 Observer interfészek (új, `common` csomag)
Hozd létre:
- `common/Megfigyelo.java` – `void frissit();`
- `common/Megfigyelheto.java` – `void addObserver(Megfigyelo m); void removeObserver(Megfigyelo m); void ertesit();`

(A `common` csomag rétegfüggetlen; a `model` is és a `view` is hivatkozhat rá körkörös függőség nélkül.)

### 8.2 A `Prototipus` kiegészítése publikus hozzáférőkkel
Mivel az `ObjektumKatalogus` **package-private**, a Controller nem éri el. Adj a `Prototipus`-hoz publikus gettereket, amelyek a katalógusból szolgáltatják a listákat:
```java
public List<Utegyseg> getUtegysegek()   { return katalogus.osszesOfType(Utegyseg.class); }
public List<Csomopont> getCsomopontok()  { return katalogus.osszesOfType(Csomopont.class); }
public List<Auto> getAutok()             { return katalogus.osszesOfType(Auto.class); }
public List<Busz> getBuszok()            { return katalogus.osszesOfType(Busz.class); }
public List<Hokotro> getHokotrok()       { return katalogus.osszesOfType(Hokotro.class); }
public List<Nyilvantarto> getNyilvantartok() { return katalogus.osszesOfType(Nyilvantarto.class); }
public Terkep getTerkep() {
    List<Terkep> l = katalogus.osszesOfType(Terkep.class);
    return l.isEmpty() ? null : l.get(0);
}
```
> Ha a `Terkep` nem a katalógusban él (a kódban a `Terkep` `ProtoEntitas`, de a `tipusTerkepe`-ben nincs külön kezelve), akkor a Controller a `getCsomopontok()`/`getUtegysegek()` listákból építi fel a megjelenítést közvetlenül – **a `Terkep` getter elhagyható**, ha a nézet a katalógus-listákra épül. Ellenőrizd a tényleges katalógus-tartalmat futásidőben, és a könnyebb utat válaszd (listák a katalógusból).

### 8.3 Observer-képesség hozzáadása a megfigyelt modellosztályokhoz
Az alábbi osztályok kapják meg az `implements Megfigyelheto`-t és a három metódust (3.2 (A)/(B) szerint), **plusz** a megfelelő művelet-/setter-metódusok **végére** egy `ertesit()` hívást:

| Osztály | `ertesit()` hívás helye (a metódusok végén) |
|---|---|
| `Jarmu` (abstract – innen öröklik a leszármazottak) | `lep()`, `sikeresLepes()`, `csuszik()`, `baleset()`, `elakad()`, `savValtas()` végén |
| `Utegyseg` | `havazas()`, `sozas()`, `jegesedes()`, `taposodas()`, `jegtores()`, `soOlvasztas()`, `tisztulas()`, `ralep()`, `setBlokkolt()`, `setJarmu()` végén |
| `Csomopont` | `jarmuErkezik()`, `jarmuTavozik()` végén (ha a megjelenítését érinti) |
| `Nyilvantarto` | `penzNovel/penzLevon`, `soNovel/soLevon`, `biokerozinNovel/biokerozinLevon`, `nemBeertAutokNovel`, `setPenz/setSo/setBiokerozin` végén |
| `Bolt` | minden `*Vasarol(...)` és `sebesseg/tapadas/hozamFejlesztes(...)` végén |

**Plusz getterek a `Jarmu`-ban a baleset/elakadás megjelenítéséhez** (jelenleg `protected`, nincs getter):
```java
public boolean isBaleset() { return baleset; }
public boolean isElakadt() { return elakadt; }
```

> **Fontos:** az `ertesit()` hívások **csak jelzést** adnak, nem tartalmaznak vizuális információt – ez tiszteletben tartja a „modell ne tudjon a megjelenítésről” elvet. A modell akkor is helyesen működik, ha senki nincs feliratkozva (üres megfigyelő-lista).

### 8.4 A szimulációs léptetés publikussá tétele
A `Prototipus.szimulacioTick(int)` jelenleg `private`. Tedd **`public`**-ká, vagy adj egy publikus burkolót:
```java
public void szimulacio(int n) { szimulacioTick(n); }   // vagy: tedd a szimulacioTick-et public-ká
```
A Controller a `szimulacioLepes()`-ben ezt hívja (`proto.szimulacio(1)`).

> **Megfontolás a tick tartalmáról:** a jelenlegi `szimulacioTick` minden tickben `havazas(1)`-et csinál minden útegységen. Ez játékmenetileg lehet, hogy túl agresszív (folyamatos havazás). **Ne** módosítsd a tick logikáját ebben az iterációban; ha a játékmenet kiegyensúlyozása kell, az külön feladat. A grafikus réteg a meglévő tick-et használja.

### 8.5 NINCS más modell-módosítás
- A `Soszoro` package-private láthatóságát **ne** változtasd – a View `getClass().getSimpleName()`-mel kezeli a fejtípusokat, `instanceof` nélkül.
- A `System.out.println` debug-sorokat a modellben **ne** töröld (külön feladat lenne).

---

## 9. Pálya-layout formátum (`palya_layout.txt`)

A modell topológiáját a meglévő prototípus-parancsfájl adja (a `proto.beolvasFajlbol` ezt dolgozza fel: `create`, `assign`, `set` parancsok). A **grafikai pozíciókat** egy külön, kiegészítő fájl tárolja, **a modell érintése nélkül**. Formátum (soronként):

```
# komment sorok #-tal
# csomópont pozíciók: NODE <azonosito> <x> <y>
NODE 1 120 300
NODE 2 400 150
NODE 3 700 300
...
# globális paraméter:
SCALE 18          # mertekSzelesseg (útegység alapméret px)
```

- A `PalyaLayout.betolt(fajl, terkep)` beolvassa, és a `NODE <azonosito>` sort a `terkep` azonos `getAzonosito()`-jú csomópontjához párosítja → `csomopontPoziciok.put(cs, new Pont(x,y))`.
- A `SCALE` a `mertekSzelesseg`-et állítja.
- Ha egy csomóponthoz **nincs** pozíció a fájlban, adj fallback elrendezést (pl. körkörös/rács kiosztás a hiányzó csomópontoknak), hogy a pálya akkor is megjeleníthető legyen.
- A `ment(fajl)` ugyanebben a formátumban írja ki az aktuális pozíciókat.

> **Tesztelhetőség:** a modell régi (konzolos) tesztjei **nem** olvassák a layout-fájlt, így változatlanul futnak. A layout csak a grafikus mód kiegészítő bemenete.

A **kezdő pálya** (legalább 1 db) elkészítése: a meglévő prototípus-parancsfájlokból (`test/input/`) válassz/építs egy értelmes pályát, és készíts hozzá egy `palya_layout.txt`-t a csomópont-pozíciókkal. Helyezd a `resources/` vagy a projekt gyökér `palyak/` mappájába, és a `JatekController.ujJatek` alapból ezt töltse be.


---

## 10. Kulcsfolyamatok (szekvenciák lépésről lépésre)

### 10.1 Grafikus felület inicializálása
1. `JatekIndito.main()` → `SwingUtilities.invokeLater`.
2. `new Prototipus()` (üres katalógus).
3. `new JatekController(proto)`.
4. `new JatekAblak(controller)` – létrejönnek a panelek (`MenuPanel`, `PalyaPanel`, `HUDPanel`, `AkcioSor`, `BoltPanel`), de a játéknézet még üres.
5. `controller.setAblak(ablak)`; `ablak.mutatMenu()`; `ablak.setVisible(true)`.

### 10.2 Új játék indítása (a főmenüből)
1. Felhasználó beállítja a játékosszámot és a kártyákat → „Új játék”.
2. `MenuPanel` → `controller.ujJatek(konfig)`.
3. Controller: `proto.beolvasFajlbol(pályafájl)` → `proto.parancsSorFeldolgoz()` (topológia létrejön a katalógusban).
4. `terkep`/csomópont- és útegység-listák lekérése a `proto` publikus getterein át (8.2).
5. `layout = new PalyaLayout(); layout.betolt("palya_layout.txt", terkep)`.
6. Játékosok + kezdő járművek létrehozása a konfigból (7.2); közös `Bolt`/`Nyilvantarto` bekötése.
7. `palyaPanel.setLayout(layout)` és a view-map-ek átadása.
8. `controller.osszekot()` (10.3).
9. `aktualisFazis = TERVEZES; aktualisJatekosIndex = 0;` HUD frissítés.
10. `ablak.mutatJatek()`.

### 10.3 Objektumok összekapcsolása (`osszekot()`)
1. Minden `Utegyseg ue`-hez: `new UtegysegView(ue, palyaPanel)`; `ue.addObserver(ueView)`; berakás a `utegysegViewk` map-be.
2. Minden `Csomopont cs`-hez: `new CsomopontView(cs, layout.getPozicio(cs))`; `cs.addObserver(csView)`; map-be.
3. Minden `Ut`-hoz: `new UtView(ut, ...)` (passzív, nem feliratkozó – csak rajzol).
4. Minden `Jarmu j`-hez: a konkrét `HokotroView`/`BuszView`/`AutoView` létrehozása; `j.addObserver(jView)`; `jarmuViewk` map-be.
5. `nyilvantarto.addObserver(hudPanel)`; `bolt.addObserver(boltPanel)`.

### 10.4 Útvonal kijelölése (egér, TERVEZES)
1. Kattintás → `BemenetKezelo.mouseClicked` → `palyaPanel.utegysegKattintas(x,y)` → `Utegyseg ue`.
2. Ha `ue != null` és `aktualisFazis==TERVEZES`: `controller.utegysegValasztva(ue)`.
3. Controller: `kijeloltUtegysegek.add(ue)`; `palyaPanel.setKijeloltUtegysegek(kijeloltUtegysegek)` → `repaint()`.
4. „kész” (dupla katt/Enter): `controller.utvonalVeglegesit()` → `((Iranyithato)kijeloltJarmu).setKijeloltUtegysegek(kijeloltUtegysegek)`; lista ürítése.

### 10.5 Bolt + vásárlás
1. „Bolt” → `controller.boltNyit()` → fázis=BOLT, `boltPanel.setAktualisJatekos(aktualisJatekos)`, `ablak.mutatBolt()`, `boltPanel.mutat()`.
2. Kategória/áru választás → `BoltPanel` frissíti a kínálatot, részleteket, cél-comboboxot (a játékos flottájából).
3. „Megvesz” → `controller.vasarol(kat, aru, cel, mennyiseg)` → a megfelelő `Bolt.*Vasarol(...)` (7.7).
4. A `Bolt`/`Nyilvantarto`/`Hokotro` `ertesit()`-je → `boltPanel.frissit()` (új egyenleg) + `hudPanel.frissit()`.
5. „Mégse”/„X” → `controller.boltBezar()` → fázis=TERVEZES, `ablak.mutatJatek()`.

### 10.6 Kör vége + szimuláció
1. „Kör vége” → `controller.korVegeKattintas()`.
2. Ha van még játékos: `kovetkezoJatekos()` (HUD aktív játékos frissül).
3. Ha mindenki kész: `szimulacioLepes()`:
   a. fázis=SZIMULACIO; `akcioSor.fazisFrissites(SZIMULACIO)` (gombok tiltása).
   b. `proto.szimulacio(1)` → útegységek `soOlvasztas()`+`havazas(1)`; `Auto.lep()` (+`nemErBe()` → törlés); `Busz.lep()`; `Hokotro.lep()`; `Nyilvantarto.ellenorizJatekVege()`.
   c. A léptetés közben a modellosztályok `ertesit()`-jei a View-kat frissítik (Observer).
   d. fázis=TERVEZES; `aktualisJatekosIndex=0`; körök++; HUD frissítés; `akcioSor.fazisFrissites(TERVEZES)`.
   e. Ha `nyilvantarto.isJatekVege()` → játék vége párbeszéd (`JOptionPane`), majd vissza a főmenübe.

---

## 11. Build és futtatás

A meglévő `pom.xml`-lel (Maven, Java 17):
```bash
mvn compile
# Grafikus mód:
mvn exec:java -Dexec.mainClass="controller.JatekIndito"
# (A régi konzolos prototípus változatlanul:)
mvn exec:java -Dexec.mainClass="model.Prototipus"
```
Build-eszköz nélkül:
```bash
find src -name "*.java" | xargs javac -d out/
java -cp out controller.JatekIndito
```

---

## 12. Megvalósítási checklist (a Claude Code ezt kövesse)

**Sorrend (alulról felfelé):**
1. `common/Megfigyelo.java`, `common/Megfigyelheto.java`.
2. Modell-kiegészítések (8.2–8.4): `Prototipus` publikus getterek + `szimulacio(int)`; `implements Megfigyelheto` + `ertesit()` hívások a `Jarmu`/`Utegyseg`/`Csomopont`/`Nyilvantarto`/`Bolt`-ban; `Jarmu.isBaleset()`/`isElakadt()`. **Más modellosztályt ne érints.**
3. `view/Pont.java`, `view/PalyaLayout.java` (+ a `palya_layout.txt` parser és egy mintapálya-layout).
4. `view/View.java` (abstract) és a konkrét nézetek: `CsomopontView`, `UtView`, `SavView`, `UtegysegView`, `JarmuView`(abstract), `HokotroView`, `BuszView`, `AutoView`.
5. Panelek: `PalyaPanel`, `HUDPanel`, `AkcioSor`, `BoltPanel`, `MenuPanel`.
6. `view/JatekAblak.java` (CardLayout: menü/játék; bolt overlay).
7. `controller/Fazis.java`, `controller/BemenetKezelo.java`, `controller/JatekController.java`, `controller/JatekIndito.java`.
8. Bekötés: listener-ek, `osszekot()`, fázis-szabályok.

**Minőségi kapuk (a kész kód feleljen meg ezeknek):**
- [ ] A modell **nem** importál `view`/`controller`/`common.Megfigyelo`-specifikus rajzlogikát; csak `ertesit()`-et hív, és csak a `common`-ra hivatkozik.
- [ ] Egy modell-elem → egy View; a View csak gettereket hív kirajzoláshoz.
- [ ] A Controller minden GUI-eseményt a fázis + aktív játékos kontextusában értelmez; a tiltott műveletek a tiltott fázisban nem futnak.
- [ ] Minden Swing-művelet EDT-n (`SwingUtilities.invokeLater` a `main`-ben).
- [ ] `JList`/`JComboBox` mögött megfelelő modell (`DefaultListModel`/`DefaultComboBoxModel`), nem nyers adat.
- [ ] Az `ertesit()` másolaton iterál (nincs `ConcurrentModificationException`).
- [ ] A `JarmuView` minden rajzoláskor a `jarmu.getUtegyseg()`-ből kéri a pozíciót (nem tárol fix pozíciót).
- [ ] A fej típusát `getClass().getSimpleName()` adja (nincs `instanceof Soszoro`, mert package-private).
- [ ] A 3 képernyő (főmenü, gameplay, bolt) elrendezése a mockupokat követi (4. szakasz).
- [ ] A program lefordul és elindul `controller.JatekIndito`-val; a régi `model.Prototipus` belépési pont változatlanul működik.
- [ ] Specifikus importok (nincs `import java.awt.*` wildcard a végleges kódban, ha a projekt konvenciója ezt kéri).

**Hiányzó döntések, amelyeket a Claude Code ésszerűen pótolhat (és jelezzen):**
- A jármű kiválasztásának pontos UI-ja a TERVEZES fázisban (kattintás a járműre vs. külön lista) – javaslat: kattintás a járművön álló útegységre kijelöli a járművet, utána a további kattintások az útvonal-útegységek.
- A pontos színpaletta és pixelméretek (a mockup arányait kövesse).
- A „kész útvonal” jelzés (dupla katt / Enter / külön gomb) – válassz egyet és legyen konzisztens.

---

## Függelék: a feldolgozott források
- **Modell forráskód:** GitHub `Balazs-Bereznay/projlab_2026`, `prototype` branch, `src/model/*.java` (teljes egészében átolvasva).
- **GUI tervek:** „11. Grafikus felület specifikációja” (a projekt `Gui_tervek` dokumentuma) – MVC + Observer architektúra, osztálykatalógus, szekvenciák.
- **Mockupok:** `fomenu.png` (főmenü), `gameplay.png` (játéknézet), `bolt.png` (bolt).
- **Korábbi terv:** a 11. fejezet a `Jatekallapot` helyett a `Prototipus` + `ObjektumKatalogus` osztályokra épül (ez a dokumentum is ezt követi).
