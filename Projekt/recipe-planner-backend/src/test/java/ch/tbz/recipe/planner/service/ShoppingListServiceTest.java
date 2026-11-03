package ch.tbz.recipe.planner.service;

import ch.tbz.recipe.planner.domain.Ingredient;
import ch.tbz.recipe.planner.domain.Recipe;
import ch.tbz.recipe.planner.domain.ShoppingListItem;
import ch.tbz.recipe.planner.domain.Unit;
import ch.tbz.recipe.planner.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ShoppingListServiceTest {

    @Mock
    private RecipeService recipeService;

    private ShoppingListService service;

    @BeforeEach
    void setUp() {
        service = new ShoppingListService(recipeService);
    }

    @Test
    void singleRecipeReturnsAllIngredients() {
        Recipe pancakes = recipeWith(
                ingredient("Mehl", Unit.GRAM, 200),
                ingredient("Eier", Unit.PIECE, 3));

        List<ShoppingListItem> list = service.createShoppingList(List.of(pancakes.getId()));

        assertThat(list).containsExactly(
                new ShoppingListItem("Eier", Unit.PIECE, 3),
                new ShoppingListItem("Mehl", Unit.GRAM, 200));
    }

    @Test
    void sameIngredientInTwoRecipesIsSummed() {
        Recipe pancakes = recipeWith(ingredient("Mehl", Unit.GRAM, 200));
        Recipe zopf = recipeWith(ingredient("Mehl", Unit.GRAM, 500));

        List<ShoppingListItem> list = service.createShoppingList(List.of(pancakes.getId(), zopf.getId()));

        assertThat(list).containsExactly(new ShoppingListItem("Mehl", Unit.GRAM, 700));
    }

    @Test
    void nameIsComparedTrimmedAndIgnoringCase() {
        // Anzeigename = getrimmter Name vom ersten Vorkommen
        Recipe pancakes = recipeWith(ingredient("  Mehl ", Unit.GRAM, 200));
        Recipe zopf = recipeWith(ingredient("mehl", Unit.GRAM, 500));

        List<ShoppingListItem> list = service.createShoppingList(List.of(pancakes.getId(), zopf.getId()));

        assertThat(list).containsExactly(new ShoppingListItem("Mehl", Unit.GRAM, 700));
    }

    @Test
    void sameNameWithDifferentUnitStaysSeparate() {
        Recipe pancakes = recipeWith(ingredient("Milch", Unit.DECILITRE, 3));
        Recipe gratin = recipeWith(ingredient("Milch", Unit.LITRE, 1));

        List<ShoppingListItem> list = service.createShoppingList(List.of(pancakes.getId(), gratin.getId()));

        assertThat(list).containsExactlyInAnyOrder(
                new ShoppingListItem("Milch", Unit.DECILITRE, 3),
                new ShoppingListItem("Milch", Unit.LITRE, 1));
    }

    @Test
    void sameRecipeTwiceCountsTwice() {
        Recipe pancakes = recipeWith(
                ingredient("Mehl", Unit.GRAM, 200),
                ingredient("Eier", Unit.PIECE, 3));

        List<ShoppingListItem> list = service.createShoppingList(List.of(pancakes.getId(), pancakes.getId()));

        assertThat(list).containsExactly(
                new ShoppingListItem("Eier", Unit.PIECE, 6),
                new ShoppingListItem("Mehl", Unit.GRAM, 400));
    }

    @Test
    void itemsAreSortedAlphabeticallyIgnoringCase() {
        Recipe kuchen = recipeWith(
                ingredient("zucker", Unit.GRAM, 100),
                ingredient("Butter", Unit.GRAM, 250),
                ingredient("apfel", Unit.PIECE, 4));

        List<ShoppingListItem> list = service.createShoppingList(List.of(kuchen.getId()));

        assertThat(list).extracting(ShoppingListItem::getName)
                .containsExactly("apfel", "Butter", "zucker");
    }

    @Test
    void emptyRequestReturnsEmptyList() {
        List<ShoppingListItem> list = service.createShoppingList(List.of());

        assertThat(list).isEmpty();
        verifyNoInteractions(recipeService);
    }

    @Test
    void recipeWithoutIngredientsIsIgnored() {
        Recipe ohneZutaten = recipeWith();
        Recipe nullZutaten = new Recipe(UUID.randomUUID(), "Leer", "", "", null);
        when(recipeService.getRecipeById(nullZutaten.getId())).thenReturn(nullZutaten);
        Recipe pancakes = recipeWith(ingredient("Mehl", Unit.GRAM, 200));

        List<ShoppingListItem> list = service.createShoppingList(
                List.of(ohneZutaten.getId(), nullZutaten.getId(), pancakes.getId()));

        assertThat(list).containsExactly(new ShoppingListItem("Mehl", Unit.GRAM, 200));
    }

    @Test
    void unknownRecipeThrowsNotFound() {
        UUID unknownId = UUID.randomUUID();
        when(recipeService.getRecipeById(unknownId)).thenReturn(null);

        assertThatThrownBy(() -> service.createShoppingList(List.of(unknownId)))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    // Hilfsmethode: Rezept erstellen und im Mock hinterlegen
    private Recipe recipeWith(Ingredient... ingredients) {
        Recipe recipe = new Recipe(UUID.randomUUID(), "Rezept", "", "", List.of(ingredients));
        when(recipeService.getRecipeById(recipe.getId())).thenReturn(recipe);
        return recipe;
    }

    private Ingredient ingredient(String name, Unit unit, int amount) {
        return new Ingredient(null, name, "", unit, amount);
    }
}
