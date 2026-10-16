package ch.tbz.recipe.planner.integration;

import ch.tbz.recipe.planner.domain.Ingredient;
import ch.tbz.recipe.planner.domain.Recipe;
import ch.tbz.recipe.planner.domain.Unit;
import ch.tbz.recipe.planner.service.RecipeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class UpdateRecipeIntegrationTest {

    @Autowired
    private RecipeService service;

    @Test
    void updateRecipeWithRealDatabase() {
        Ingredient tomato = new Ingredient(null, "Tomato", "", Unit.PIECE, 5);
        Recipe created = service.addRecipe(new Recipe(null, "Pasta", "Alt", "", List.of(tomato)));

        Ingredient cheese = new Ingredient(null, "Kaese", "Parmesan", Unit.GRAMM, 100);
        Recipe changed = new Recipe(null, "Pasta", "Neu", "", List.of(cheese));
        service.updateRecipe(created.getId(), changed);

        Recipe loaded = service.getRecipeById(created.getId());
        assertThat(loaded.getDescription()).isEqualTo("Neu");
        assertThat(loaded.getIngredients()).hasSize(1);
        assertThat(loaded.getIngredients().get(0).getName()).isEqualTo("Kaese");
    }
}
