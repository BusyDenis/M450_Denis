# KN06 – Automatisiertes Testen und Deployen

Projekt: [recipe](recipe) (Backend: Spring Boot, Frontend: React)

```bash
# Backend (Port 8080)
cd recipe/recipe-planner-backend
mvn spring-boot:run

# Frontend (Port 3000)
cd recipe/recipe-planner-frontend
npm install
npm start
```

## Aufgabe 1 – Unit Testing

### Controller mit MockMvc

`RecipeControllerTest` testet alle Endpunkte. Der `RecipeService` wird mit `@MockBean` weggemockt.

| Test | Endpunkt | Erwartet |
|---|---|---|
| `getRecipesReturnsAllRecipes` | `GET /api/recipes` | 200, Liste mit 2 Rezepten |
| `getRecipesWithEmptyList` | `GET /api/recipes` | 200, leere Liste |
| `getRecipeById` | `GET /api/recipes/recipe/{id}` | 200, Rezept mit Zutaten |
| `getRecipeWithInvalidIdReturns400` | `GET /api/recipes/recipe/keine-uuid` | 400 |
| `addRecipe` | `POST /api/recipes` | 200, Service wurde aufgerufen |

### Mapper mit SoftAssertions

`RecipeEntityMapperTest` und `IngredientEntityMapperTest` prüfen das Mapping Entity ↔ Domain in beide Richtungen, auch Listen und `null`.

```java
SoftAssertions softly = new SoftAssertions();
softly.assertThat(recipe.getName()).isEqualTo("Lasagne");
softly.assertThat(recipe.getDescription()).isEqualTo("Lecker");
softly.assertThat(recipe.getIngredients()).hasSize(1);
softly.assertAll();
```

**Vorteil von SoftAssertions:** Bei normalen Assertions stoppt der Test beim ersten Fehler. Bei SoftAssertions werden **alle** Prüfungen ausgeführt und am Schluss bei `assertAll()` alle Fehler zusammen gemeldet. Bei einem Mapper mit vielen Feldern sieht man so sofort, welche Felder alle falsch gemappt sind, und muss den Test nicht mehrmals laufen lassen.

### Zusätzlich

`RecipeServiceTest` testet den Service, das `RecipeRepository` wird mit Mockito gemockt.

**Resultat:** 17 Tests, alle grün.

## Aufgabe 2 – Reports

Im `pom.xml` eingebaut:

| Plugin | Report | Ort |
|---|---|---|
| `maven-surefire-report-plugin` | Testresultate als HTML | `target/reports/surefire.html` |
| `jacoco-maven-plugin` | Code Coverage als HTML | `target/site/jacoco/index.html` |

Beide Reports werden bei `mvn test` automatisch erstellt.

## Aufgabe 3 – Pipeline

Pipeline mit **GitHub Actions**: [.github/workflows/recipe-backend.yml](../.github/workflows/recipe-backend.yml)

Wird bei jedem Push und Pull Request ausgelöst, wenn sich etwas im Backend ändert.

| Stage / Job | Was passiert |
|---|---|
| `build` | Java 21 installieren, `mvn compile` |
| `test` | `mvn test`, Testresultate werden in GitHub als *Check* "Unit Tests" angezeigt, Reports werden als Artefakt `test-reports` hochgeladen |
| `deploy-reports` | Nur auf `main`: Reports werden auf **GitHub Pages** veröffentlicht |

Reports pro Pipeline-Durchlauf:
- **Actions → Run → Artifacts → test-reports** (ZIP mit Test- und Coverage-Report)
- **Actions → Run → Unit Tests** (Testresultate direkt in GitHub)
- **GitHub Pages:** `https://busydenis.github.io/M450_Denis/` (Testreport) und `/coverage/` (JaCoCo)
