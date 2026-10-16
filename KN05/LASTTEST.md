# Lasttest mit JMeter

## Tool

[Apache JMeter](https://jmeter.apache.org/) 5.6.3. Open Source, läuft mit Java und hat ein GUI zum Erstellen von Testplänen. Für den eigentlichen Lasttest benutzt man den Kommandozeilen-Modus, weil das GUI selbst viel Leistung braucht.

## Testplan

Datei: [load-tests/students-load-test.jmx](load-tests/students-load-test.jmx)

| Element | Einstellung |
|---|---|
| Thread Group (= Benutzer) | 100 Benutzer, Ramp-Up 10 Sekunden, je 20 Durchläufe |
| HTTP Request 1 | `GET /students` mit Assertion "Status 200" |
| HTTP Request 2 | `POST /students` mit JSON-Body und Header `Content-Type: application/json` |
| HTTP Standardwerte | `localhost:8081` |

Anzahl Benutzer und Durchläufe kann man beim Start ändern (`-Jusers=... -Jloops=...`).

## Ausführen

```bash
cd load-tests
jmeter -n -t students-load-test.jmx -Jusers=100 -Jloops=20 -l results.jtl -e -o report
```

- `-n` ohne GUI
- `-l` speichert alle Resultate
- `-e -o report` erstellt einen HTML-Report → `report/index.html`

## Resultat

| Request | Anzahl | Fehler | Ø Antwortzeit | 95 % unter | Max | Durchsatz |
|---|---|---|---|---|---|---|
| GET /students | 2000 | 0 % | 3.9 ms | 7 ms | 58 ms | 205 / s |
| POST /students | 2000 | 0 % | 2.0 ms | 4 ms | 15 ms | 206 / s |
| **Total** | **4000** | **0 %** | **2.9 ms** | **6 ms** | **58 ms** | **409 / s** |

**Fazit:** Das Backend hält 100 gleichzeitige Benutzer ohne Probleme aus. `GET` wird mit der Zeit etwas langsamer, weil durch die vielen `POST`s die Liste immer grösser wird und es keine Pagination gibt. Bei sehr vielen Einträgen wäre das ein Bottleneck.

## Funktionen von JMeter

| Funktion | Beschreibung |
|---|---|
| Thread Groups | Simulieren viele Benutzer gleichzeitig, mit Ramp-Up (langsam hochfahren) |
| Sampler | Requests für HTTP, JDBC (Datenbank), FTP, JMS, ... |
| Assertions | Prüfen Status-Code, Inhalt, JSON-Werte oder Antwortzeit |
| Listener | Zeigen Resultate als Tabelle, Baum oder Graph |
| Timer | Wartezeit zwischen Requests, damit es realistischer ist |
| CSV Data Set | Testdaten aus einer CSV-Datei lesen (z.B. viele verschiedene Benutzer) |
| HTML Dashboard | Fertiger Report mit Diagrammen (Antwortzeit über Zeit, Durchsatz, Fehler) |
| Recorder | Klicks im Browser aufnehmen und als Testplan speichern |

**Load Test vs. Stress Test:** Mit 100 Benutzern ist es ein Load Test (normale Last). Für einen Stress Test erhöht man die Benutzer so lange, bis Fehler auftreten, z.B. `-Jusers=2000`.

## Alternative: Postman

Postman hat seit Version 10 ebenfalls einen *Performance Test* im Collection Runner. Man wählt die Anzahl virtueller Benutzer und die Dauer. Einfacher als JMeter, aber weniger Einstellungen.
