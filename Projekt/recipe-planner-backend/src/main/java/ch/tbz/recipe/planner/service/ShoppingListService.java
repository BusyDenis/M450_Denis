package ch.tbz.recipe.planner.service;

import ch.tbz.recipe.planner.domain.ShoppingListItem;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ShoppingListService {

    private final RecipeService recipeService;

    public ShoppingListService(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    // TODO: noch nicht implementiert, zuerst kommen die Tests (TDD)
    public List<ShoppingListItem> createShoppingList(List<UUID> recipeIds) {
        return List.of();
    }
}
