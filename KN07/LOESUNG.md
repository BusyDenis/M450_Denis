# KN07 – Code Reviews

Projekt: [recipe](recipe) (gleiches Projekt wie in KN06)

## Branches und Pull Requests

| Branch | Basis | Inhalt | PR |
|---|---|---|---|
| `feature/add-recipe` | `main` | Aufgabe 1: Rezept hinzufügen (Frontend) | PR 1 |
| `feature/edit-recipe-backend` | `main` | Aufgabe 2: Rezept bearbeiten, `PUT`-Endpunkt (Backend) | PR 2 |
| `feature/edit-recipe-frontend` | `feature/add-recipe` | Aufgabe 2: Rezept bearbeiten (Frontend) | PR 3 |

Die PR-Texte zum Einfügen stehen in [PULL_REQUESTS.md](PULL_REQUESTS.md).

## Aufgabe 1 – Rezept hinzufügen (Frontend)

- Formular war vorher nicht verbunden: Eingaben wurden nirgends gespeichert, Submit hat nichts gemacht
- Jetzt: Name, Beschreibung, Bild-URL und Zutaten werden im State gespeichert
- Zutaten hinzufügen und entfernen
- Submit schickt `POST /api/recipes` und springt zurück zur Übersicht
- Fehlermeldung, wenn der Name leer ist oder das Backend einen Fehler meldet
- Tests: `AddRecipe.test.js` (4 Tests, axios gemockt)

## Aufgabe 2 – Rezept bearbeiten (Frontend + Backend)

**Backend**
- `PUT /api/recipes/recipe/{id}` → 200 mit geändertem Rezept, 404 wenn es das Rezept nicht gibt
- `RecipeService.updateRecipe(...)`
- Tests: Controller (MockMvc), Service (Mockito), Integrationstest mit echter H2-Datenbank → 22 Tests grün

**Frontend**
- Button "Edit Details" auf jeder Karte öffnet `/edit/{id}`
- Formular wird mit den Daten vom Backend gefüllt, Speichern mit `PUT`
- Formular aus Aufgabe 1 in eine gemeinsame Komponente `RecipeForm` ausgelagert, damit Add und Edit denselben Code benutzen
- Tests: `EditRecipe.test.js` (3 Tests) + `AddRecipe.test.js` → 7 Tests grün

## Review-Checkliste (aus der Theorie)

| Punkt | Worauf achten |
|---|---|
| Kommentare | Zu viele oder zu wenige? Erklärt der Code sich selbst? |
| Naming | Einheitlich und verständlich? (`saveRecipe`, `updateIngredient`) |
| Duplizierter Code | Gibt es Code, der zweimal vorkommt? |
| Lesbarkeit | Kurze Funktionen, keine tiefe Verschachtelung |
| Fehlerbehandlung | Was passiert, wenn das Backend nicht erreichbar ist? |
| Tests | Gibt es Tests? Testen sie das Richtige? |
| Toter Code | Ungenutzte Imports oder Variablen (z.B. `useForm` war importiert, aber nicht benutzt) |
| Grösse | PR klein genug (200–400 Zeilen)? |

## Erkenntnisse

Reviewer: Moreno (kein fester Partner, Moreno hat meine PRs reviewt)

Moreno ist aus einer anderen Gruppe und hat das Feature nicht selbst gebaut. Darum gibt es keine Partner-Lösung zum Vergleichen, in der Tabelle steht stattdessen, was ich nach seinem Review geändert habe.

| Thema | Meine Lösung | Feedback aus dem Review | Was geändert wurde |
|---|---|---|---|
| State im Formular | Ein `useState` pro Feld | Positiv: kontrollierte Inputs und `aria-label`. Aber in `RecipeForm` wird `startRecipe` nur beim ersten Render übernommen (PR 3) | `useEffect` füllt das Formular neu, wenn ein anderes Rezept kommt. `AddRecipe` gibt immer dasselbe leere Rezept mit, sonst würde das Formular bei jedem Render zurückgesetzt |
| Zutaten | Liste mit eigenem `key` pro Zeile | `nextKey` bzw. `index + 1` als Key kann doppelte Keys geben, Vorschlag `crypto.randomUUID()` (PR 1 und 3) | Zutaten aus der DB nehmen ihre `id` als Key, neue bekommen einen Key aus einem Zähler, der nie zurückgesetzt wird (`crypto.randomUUID()` gibt es in Jest/jsdom nicht) |
| Einheiten | `PIECE`, `GRAMM`, `KILOGRAMM`, `LITRE`, `DECILITRE` wie im Backend | `GRAMM` ist Deutsch, die anderen Einheiten Englisch (PR 1) | `GRAM` und `KILOGRAM` im Backend-Enum und im Frontend gleich umbenannt, damit beide zusammenpassen |
| Add und Edit | Gemeinsame Komponente `RecipeForm` | Positiv: kein doppelter Code mehr. `AddRecipe` importiert `useState` unnötig (PR 3) | Fehler beim Speichern zeigt jetzt `RecipeForm` selbst, `AddRecipe` hat keinen State mehr und importiert nur noch `React` |
| Fehler / Submit | Meldung bei leerem Namen und Backend-Fehler | Man kann mehrmals auf Submit klicken und schickt dann mehrere POSTs (PR 1) | `isSubmitting`: Submit-Button ist gesperrt, solange der Request läuft, bei einem Fehler wieder frei |
| Navigation | "Edit Details" mit `navigate()` im `onClick` | Besser `<Link>`, dann geht "In neuem Tab öffnen" (PR 3) | `<Button as={Link} to=...>`, der Button hat jetzt ein echtes `href` |
| Backend Update | Prüft mit `findById`, ob das Rezept existiert, sonst `null` und 404 im Controller | `existsById` statt ganzes Rezept laden, Exception statt `null` (PR 2) | `existsById` im Repository, Service wirft `ResourceNotFoundException` mit `@ResponseStatus(NOT_FOUND)`, kein `null`-Check mehr im Controller |
| Tests | Jest (Frontend), MockMvc + Mockito + Integrationstest (Backend) | Positiv: gute Testabdeckung. Integrationstest soll mit `@Transactional` laufen, damit keine Testdaten liegen bleiben (PR 2) | `@Transactional` + `flush`/`clear` im Integrationstest, neue Tests für Submit, `RecipeForm`, Edit-Link und unbekanntes Rezept → Frontend 10, Backend 23 Tests grün |

**Was ich gelernt habe**
- Zwei Leute lösen das gleiche Feature oft ganz unterschiedlich. Im Review sieht man andere Ansätze.
- Kleine PRs (Frontend und Backend getrennt) lassen sich viel schneller reviewen.
- Beim Review findet man Dinge, die man selbst übersieht, z.B. ungenutzte Imports oder fehlende Fehlerbehandlung.
- Kommentare im Review sollten konkret und freundlich sein: "Was hältst du davon, wenn ..." statt "Das ist falsch".

**Was ich aus dem Review von Moreno gelernt habe**
- Jemand, der das Feature nicht selbst gebaut hat, schaut auf andere Sachen: Moreno hat vor allem Fälle gefunden, an die ich beim Bauen nicht gedacht habe (doppelter Submit, doppelte Keys, Formular wird nicht neu gefüllt).
- Review-Vorschläge zuerst prüfen und dann passend umsetzen: `crypto.randomUUID()` gibt es in den Jest-Tests nicht, darum habe ich einen Zähler genommen. `useState` in `AddRecipe` wurde noch gebraucht und fiel erst weg, als der Fehler ins `RecipeForm` gewandert ist.
- Eine kleine Änderung kann mehr betreffen als gedacht: `GRAMM` → `GRAM` musste im Backend und im Frontend gleich gemacht werden, und der neue `useEffect` hat das Formular zurückgesetzt, bis das leere Rezept ausserhalb der Komponente war. Gut, dass es Tests dafür gibt.
