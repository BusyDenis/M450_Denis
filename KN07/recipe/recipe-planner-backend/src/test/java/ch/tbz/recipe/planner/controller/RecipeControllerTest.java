package ch.tbz.recipe.planner.controller;

import ch.tbz.recipe.planner.domain.Ingredient;
import ch.tbz.recipe.planner.domain.Recipe;
import ch.tbz.recipe.planner.domain.Unit;
import ch.tbz.recipe.planner.mapper.RecipeEntityMapper;
import ch.tbz.recipe.planner.repository.RecipeRepository;
import ch.tbz.recipe.planner.service.RecipeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RecipeController.class)
class RecipeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RecipeService recipeService;

    @MockBean
    private RecipeEntityMapper recipeEntityMapper;

    @MockBean
    private RecipeRepository recipeRepository;

    private Recipe lasagne;

    @BeforeEach
    void setUp() {
        Ingredient tomato = new Ingredient(UUID.randomUUID(), "Tomato", "The big ones", Unit.PIECE, 5);
        lasagne = new Recipe(UUID.randomUUID(), "Lasagne", "Lecker", "lasagne.jpg", List.of(tomato));
    }

    @Test
    void getRecipesReturnsAllRecipes() throws Exception {
        Recipe pizza = new Recipe(UUID.randomUUID(), "Pizza", "Mit Kaese", "pizza.jpg", List.of());
        when(recipeService.getRecipes()).thenReturn(List.of(lasagne, pizza));

        mockMvc.perform(get("/api/recipes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("Lasagne"))
                .andExpect(jsonPath("$[1].name").value("Pizza"));
    }

    @Test
    void getRecipesWithEmptyList() throws Exception {
        when(recipeService.getRecipes()).thenReturn(List.of());

        mockMvc.perform(get("/api/recipes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void getRecipeById() throws Exception {
        when(recipeService.getRecipeById(lasagne.getId())).thenReturn(lasagne);

        mockMvc.perform(get("/api/recipes/recipe/" + lasagne.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Lasagne"))
                .andExpect(jsonPath("$.ingredients[0].name").value("Tomato"))
                .andExpect(jsonPath("$.ingredients[0].unit").value("PIECE"));
    }

    @Test
    void getRecipeWithInvalidIdReturns400() throws Exception {
        mockMvc.perform(get("/api/recipes/recipe/keine-uuid"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void addRecipe() throws Exception {
        when(recipeService.addRecipe(any(Recipe.class))).thenReturn(lasagne);

        mockMvc.perform(post("/api/recipes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "name": "Lasagne",
                                  "description": "Lecker",
                                  "imageUrl": "lasagne.jpg",
                                  "ingredients": [
                                    { "name": "Tomato", "comment": "The big ones", "unit": "PIECE", "amount": 5 }
                                  ]
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(lasagne.getId().toString()))
                .andExpect(jsonPath("$.name").value("Lasagne"));

        verify(recipeService).addRecipe(any(Recipe.class));
    }
}
