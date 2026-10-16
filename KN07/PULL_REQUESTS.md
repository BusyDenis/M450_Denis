# Pull Request Texte

## PR 1 – Rezept hinzufügen (Frontend)

**Branch:** `feature/add-recipe` → `main`

### Was wurde gemacht
- Formular "Add Recipe" funktioniert jetzt: Name, Beschreibung, Bild-URL und Zutaten werden gespeichert
- Zutaten können hinzugefügt und entfernt werden (Name, Einheit, Menge)
- Submit schickt das Rezept per `POST /api/recipes` ans Backend und zeigt danach die Übersicht
- Fehlermeldung bei leerem Namen oder wenn das Backend einen Fehler zurückgibt
- Ungenutzten Code entfernt (`useForm`, `watch`, `listId`, `propTypes`)

### Testen
1. Backend und Frontend starten
2. "Add Recipes" öffnen, Rezept mit einer Zutat erfassen, Submit
3. Neues Rezept erscheint in "Browse"
4. `npm test` → 4 Tests grün

---

## PR 2 – Rezept bearbeiten (Backend)

**Branch:** `feature/edit-recipe-backend` → `main`

### Was wurde gemacht
- Neuer Endpunkt `PUT /api/recipes/recipe/{recipeId}`
- Gibt das geänderte Rezept zurück (200) oder 404, wenn es das Rezept nicht gibt
- `RecipeService.updateRecipe(id, recipe)`

### Testen
- `mvn test` → 22 Tests grün
- Neue Tests: `updateRecipe`, `updateUnknownRecipeReturns404` (Controller), `updateRecipe`, `updateUnknownRecipeReturnsNull` (Service), `UpdateRecipeIntegrationTest` (echte H2-Datenbank)

---

## PR 3 – Rezept bearbeiten (Frontend)

**Branch:** `feature/edit-recipe-frontend` → `main` (baut auf PR 1 auf)

### Was wurde gemacht
- Button "Edit Details" öffnet `/edit/{id}`
- Formular wird mit dem bestehenden Rezept gefüllt und mit `PUT` gespeichert
- Formular aus PR 1 in `RecipeForm` ausgelagert, damit Add und Edit denselben Code benutzen

### Testen
1. PR 2 muss im Backend gemerged sein
2. In "Browse" bei einem Rezept auf "Edit Details" klicken, Namen ändern, Submit
3. Übersicht zeigt den neuen Namen
4. `npm test` → 7 Tests grün
