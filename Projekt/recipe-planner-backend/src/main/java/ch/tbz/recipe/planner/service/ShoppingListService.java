package ch.tbz.recipe.planner.service;

import ch.tbz.recipe.planner.domain.Ingredient;
import ch.tbz.recipe.planner.domain.Recipe;
import ch.tbz.recipe.planner.domain.ShoppingListItem;
import ch.tbz.recipe.planner.domain.Unit;
import ch.tbz.recipe.planner.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ShoppingListService {

    private final RecipeService recipeService;

    public ShoppingListService(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    /**
     * Erstellt die Einkaufsliste fuer die geplanten Rezepte.
     * Kommt eine id mehrmals vor, wird das Rezept auch mehrmals gezaehlt.
     */
    public List<ShoppingListItem> createShoppingList(List<UUID> recipeIds) {
        // LinkedHashMap, damit bei gleichen Zutaten der Name vom ersten Vorkommen bleibt
        Map<ItemKey, ShoppingListItem> itemsByKey = recipeIds.stream()
                .map(this::loadRecipe)
                .flatMap(recipe -> ingredientsOf(recipe).stream())
                .collect(Collectors.toMap(
                        ShoppingListService::keyOf,
                        ShoppingListService::toItem,
                        ShoppingListService::sum,
                        LinkedHashMap::new));

        return itemsByKey.values().stream()
                .sorted(Comparator.comparing(ShoppingListItem::getName, String.CASE_INSENSITIVE_ORDER))
                .toList();
    }

    private Recipe loadRecipe(UUID recipeId) {
        Recipe recipe = recipeService.getRecipeById(recipeId);
        if (recipe == null) {
            throw new ResourceNotFoundException("Rezept mit id " + recipeId + " nicht gefunden");
        }
        return recipe;
    }

    private static List<Ingredient> ingredientsOf(Recipe recipe) {
        return recipe.getIngredients() == null ? List.of() : recipe.getIngredients();
    }

    private static ItemKey keyOf(Ingredient ingredient) {
        return new ItemKey(ingredient.getName().trim().toLowerCase(Locale.ROOT), ingredient.getUnit());
    }

    private static ShoppingListItem toItem(Ingredient ingredient) {
        return new ShoppingListItem(ingredient.getName().trim(), ingredient.getUnit(), ingredient.getAmount());
    }

    private static ShoppingListItem sum(ShoppingListItem first, ShoppingListItem second) {
        return new ShoppingListItem(first.getName(), first.getUnit(), first.getAmount() + second.getAmount());
    }

    // Gleiche Zutat = gleicher Name (getrimmt, ohne Gross-/Kleinschreibung) und gleiche Einheit
    private record ItemKey(String name, Unit unit) {
    }
}
