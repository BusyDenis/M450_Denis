package ch.tbz.recipe.planner.integration;

import ch.tbz.recipe.planner.domain.Ingredient;
import ch.tbz.recipe.planner.domain.Recipe;
import ch.tbz.recipe.planner.domain.Unit;
import ch.tbz.recipe.planner.exception.ResourceNotFoundException;
import ch.tbz.recipe.planner.service.RecipeService;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional // Testdaten werden nach jedem Test wieder zurueckgerollt
class UpdateRecipeIntegrationTest {

    @Autowired
    private RecipeService service;

    @Autowired
    private EntityManager entityManager;

    @Test
    void updateRecipeWithRealDatabase() {
        Ingredient tomato = new Ingredient(null, "Tomato", "", Unit.PIECE, 5);
        Recipe created = service.addRecipe(new Recipe(null, "Pasta", "Alt", "", List.of(tomato)));

        Ingredient cheese = new Ingredient(null, "Kaese", "Parmesan", Unit.GRAM, 100);
        Recipe changed = new Recipe(null, "Pasta", "Neu", "", List.of(cheese));
        service.updateRecipe(created.getId(), changed);

        // in die DB schreiben und Cache leeren, damit wirklich aus der DB gelesen wird
        entityManager.flush();
        entityManager.clear();

        Recipe loaded = service.getRecipeById(created.getId());
        assertThat(loaded.getDescription()).isEqualTo("Neu");
        assertThat(loaded.getIngredients()).hasSize(1);
        assertThat(loaded.getIngredients().get(0).getName()).isEqualTo("Kaese");
    }

    @Test
    void updateUnknownRecipeWithRealDatabase() {
        Recipe changed = new Recipe(null, "Gibt es nicht", "", "", List.of());

        assertThatThrownBy(() -> service.updateRecipe(UUID.randomUUID(), changed))
                .isInstanceOf(ResourceNotFoundException.class);
    }
}
