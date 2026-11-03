package ch.tbz.recipe.planner.controller;

import ch.tbz.recipe.planner.domain.ShoppingListItem;
import ch.tbz.recipe.planner.service.ShoppingListService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@CrossOrigin(origins = {"http://localhost:3000"})
public class ShoppingListController {

    private final ShoppingListService service;

    public ShoppingListController(ShoppingListService service) {
        this.service = service;
    }

    // Body: Liste von Rezept-ids, gleiche id zweimal = Rezept zweimal geplant
    @PostMapping("/api/shopping-list")
    public ResponseEntity<List<ShoppingListItem>> createShoppingList(@RequestBody List<UUID> recipeIds) {
        return new ResponseEntity<>(service.createShoppingList(recipeIds), HttpStatus.OK);
    }
}
