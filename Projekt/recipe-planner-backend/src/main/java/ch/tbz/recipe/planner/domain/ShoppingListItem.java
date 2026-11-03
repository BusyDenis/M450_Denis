package ch.tbz.recipe.planner.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// Ein Eintrag auf der Einkaufsliste, z.B. "Mehl, 500 GRAM"
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ShoppingListItem {

    private String name;
    private Unit unit;
    private int amount;

}
