package ch.tbz.recipe.planner.controller;

import ch.tbz.recipe.planner.domain.ShoppingListItem;
import ch.tbz.recipe.planner.domain.Unit;
import ch.tbz.recipe.planner.exception.ResourceNotFoundException;
import ch.tbz.recipe.planner.mapper.RecipeEntityMapper;
import ch.tbz.recipe.planner.repository.RecipeRepository;
import ch.tbz.recipe.planner.service.ShoppingListService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ShoppingListController.class)
class ShoppingListControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ShoppingListService shoppingListService;

    // werden von der RecipePlannerApplication (CommandLineRunner) gebraucht
    @MockBean
    private RecipeEntityMapper recipeEntityMapper;

    @MockBean
    private RecipeRepository recipeRepository;

    @Test
    void createShoppingListReturnsItems() throws Exception {
        UUID pancakesId = UUID.randomUUID();
        UUID zopfId = UUID.randomUUID();
        when(shoppingListService.createShoppingList(List.of(pancakesId, zopfId))).thenReturn(List.of(
                new ShoppingListItem("Eier", Unit.PIECE, 3),
                new ShoppingListItem("Mehl", Unit.GRAM, 700)));

        mockMvc.perform(post("/api/shopping-list")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("[\"" + pancakesId + "\", \"" + zopfId + "\"]"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("Eier"))
                .andExpect(jsonPath("$[0].unit").value("PIECE"))
                .andExpect(jsonPath("$[0].amount").value(3))
                .andExpect(jsonPath("$[1].name").value("Mehl"))
                .andExpect(jsonPath("$[1].unit").value("GRAM"))
                .andExpect(jsonPath("$[1].amount").value(700));

        verify(shoppingListService).createShoppingList(List.of(pancakesId, zopfId));
    }

    @Test
    void createShoppingListWithEmptyList() throws Exception {
        when(shoppingListService.createShoppingList(List.of())).thenReturn(List.of());

        mockMvc.perform(post("/api/shopping-list")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("[]"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void unknownRecipeReturns404() throws Exception {
        when(shoppingListService.createShoppingList(anyList()))
                .thenThrow(new ResourceNotFoundException("Rezept nicht gefunden"));

        mockMvc.perform(post("/api/shopping-list")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("[\"" + UUID.randomUUID() + "\"]"))
                .andExpect(status().isNotFound());
    }
}
