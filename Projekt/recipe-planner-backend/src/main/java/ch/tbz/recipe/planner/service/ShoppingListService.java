package ch.tbz.recipe.planner.service;

import ch.tbz.recipe.planner.domain.Ingredient;
import ch.tbz.recipe.planner.domain.Recipe;
import ch.tbz.recipe.planner.domain.ShoppingListItem;
import ch.tbz.recipe.planner.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class ShoppingListService {

    private final RecipeService recipeService;

    public ShoppingListService(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    public List<ShoppingListItem> createShoppingList(List<UUID> recipeIds) {
        Map<String, ShoppingListItem> items = new LinkedHashMap<>();
        for (UUID recipeId : recipeIds) {
            Recipe recipe = recipeService.getRecipeById(recipeId);
            if (recipe == null) {
                throw new ResourceNotFoundException("Rezept mit id " + recipeId + " nicht gefunden");
            }
            if (recipe.getIngredients() == null) {
                continue;
            }
            for (Ingredient ingredient : recipe.getIngredients()) {
                String key = ingredient.getName().trim().toLowerCase() + "|" + ingredient.getUnit();
                ShoppingListItem item = items.get(key);
                if (item == null) {
                    items.put(key, new ShoppingListItem(ingredient.getName().trim(), ingredient.getUnit(), ingredient.getAmount()));
                } else {
                    item.setAmount(item.getAmount() + ingredient.getAmount());
                }
            }
        }
        List<ShoppingListItem> result = new ArrayList<>(items.values());
        result.sort(Comparator.comparing(ShoppingListItem::getName, String.CASE_INSENSITIVE_ORDER));
        return result;
    }
}
