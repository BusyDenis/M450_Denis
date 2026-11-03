package ch.tbz.recipe.planner.integration;

import ch.tbz.recipe.planner.domain.Unit;
import ch.tbz.recipe.planner.entities.IngredientEntity;
import ch.tbz.recipe.planner.entities.RecipeEntity;
import ch.tbz.recipe.planner.repository.RecipeRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional // Testdaten werden nach jedem Test wieder zurueckgerollt
class ShoppingListIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RecipeRepository repository;

    @Autowired
    private EntityManager entityManager;

    @Test
    void shoppingListSumsIngredientsFromRealDatabase() throws Exception {
        RecipeEntity pancakes = repository.save(new RecipeEntity(null, "Pancakes", "", "", new ArrayList<>(List.of(
                new IngredientEntity(null, "Mehl", "", Unit.GRAM, 200),
                new IngredientEntity(null, "Milch", "", Unit.DECILITRE, 3)))));
        RecipeEntity zopf = repository.save(new RecipeEntity(null, "Zopf", "", "", new ArrayList<>(List.of(
                new IngredientEntity(null, "mehl", "Weissmehl", Unit.GRAM, 500),
                new IngredientEntity(null, "Butter", "", Unit.GRAM, 50)))));

        // in die DB schreiben und Cache leeren, damit wirklich aus der DB gelesen wird
        entityManager.flush();
        entityManager.clear();

        mockMvc.perform(post("/api/shopping-list")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("[\"" + pancakes.getId() + "\", \"" + zopf.getId() + "\"]"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3))
                .andExpect(jsonPath("$[0].name").value("Butter"))
                .andExpect(jsonPath("$[0].amount").value(50))
                .andExpect(jsonPath("$[1].name").value("Mehl"))
                .andExpect(jsonPath("$[1].unit").value("GRAM"))
                .andExpect(jsonPath("$[1].amount").value(700))
                .andExpect(jsonPath("$[2].name").value("Milch"))
                .andExpect(jsonPath("$[2].unit").value("DECILITRE"))
                .andExpect(jsonPath("$[2].amount").value(3));
    }

    @Test
    void unknownRecipeReturns404WithRealDatabase() throws Exception {
        mockMvc.perform(post("/api/shopping-list")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("[\"" + UUID.randomUUID() + "\"]"))
                .andExpect(status().isNotFound());
    }
}
