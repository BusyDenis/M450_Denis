# Bonus – Feature: Input-Validierung

## Feature-Beschreibung

Beim Erstellen eines Studenten werden die Eingaben geprüft, im Frontend **und** im Backend.

**Backend (Spring Boot)**
- `name` darf nicht leer sein (`@NotBlank`)
- `email` darf nicht leer sein und muss eine gültige E-Mail sein (`@NotBlank`, `@Email`)
- Bei ungültigen Daten antwortet `POST /students` mit **400 Bad Request**, und es wird nichts gespeichert
- Neue Dependency: `spring-boot-starter-validation`

**Frontend (Angular)**
- E-Mail-Feld prüft das Format (`email`-Validator)
- Meldung "Email is not valid", Submit-Button ist gesperrt
- Wenn das Backend trotzdem einen Fehler schickt, erscheint eine rote Fehlermeldung statt dass nichts passiert

**Tests**
- `StudentControllerTest` (MockMvc): gültiger Student → 200, leerer Name → 400, falsche E-Mail → 400
- Newman: "Ungültige E-Mail gibt 400"
- Playwright: "Ungültige E-Mail kann nicht abgeschickt werden"

## Zeit

| | Zeit |
|---|---|
| Schätzung | 45 Minuten (1 Lektion) |
| Tatsächlich | ca. 60 Minuten |

| Schritt | Geschätzt | Tatsächlich |
|---|---|---|
| Backend Validierung | 10 min | 10 min |
| Frontend Validierung + Fehlermeldung | 15 min | 15 min |
| Tests schreiben | 15 min | 25 min |
| Doku | 5 min | 10 min |

## Reflexion

- Backend und Frontend gingen schnell, weil Spring und Angular die Validierung schon eingebaut haben.
- Unterschätzt habe ich die Tests. Der Controller-Test ist zuerst fehlgeschlagen, weil beim Start der App 5 Beispiel-Studenten gespeichert werden und der Mock diese Aufrufe mitgezählt hat. Mit `reset(...)` vor jedem Test war das Problem gelöst.
- Das alte Spring Boot musste ich zuerst aktualisieren, damit es mit Java 21 läuft. Das war nicht eingeplant.
- Learning: Für Tests und unerwartete Probleme etwa 30 % Puffer einrechnen.
