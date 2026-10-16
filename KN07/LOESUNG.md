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

Partner: ______________________

| Thema | Meine Lösung | Lösung Partner | Feedback aus dem Review |
|---|---|---|---|
| State im Formular | Ein `useState` pro Feld | | |
| Zutaten | Liste mit eigenem `key` pro Zeile | | |
| Add und Edit | Gemeinsame Komponente `RecipeForm` | | |
| Fehler | Meldung bei leerem Namen und Backend-Fehler | | |
| Backend Update | Prüft zuerst, ob das Rezept existiert, sonst 404 | | |
| Tests | Jest (Frontend), MockMvc + Mockito + Integrationstest (Backend) | | |

**Was ich gelernt habe**
- Zwei Leute lösen das gleiche Feature oft ganz unterschiedlich. Im Review sieht man andere Ansätze.
- Kleine PRs (Frontend und Backend getrennt) lassen sich viel schneller reviewen.
- Beim Review findet man Dinge, die man selbst übersieht, z.B. ungenutzte Imports oder fehlende Fehlerbehandlung.
- Kommentare im Review sollten konkret und freundlich sein: "Was hältst du davon, wenn ..." statt "Das ist falsch".
