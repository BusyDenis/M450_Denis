package ch.tbz.recipe.planner.mapper;

import ch.tbz.recipe.planner.domain.Ingredient;
import ch.tbz.recipe.planner.domain.Unit;
import ch.tbz.recipe.planner.entities.IngredientEntity;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.UUID;

class IngredientEntityMapperTest {

    private final IngredientEntityMapper mapper = Mappers.getMapper(IngredientEntityMapper.class);

    @Test
    void entityToDomain() {
        IngredientEntity entity = new IngredientEntity(UUID.randomUUID(), "Milch", "Vollmilch", Unit.DECILITRE, 3);

        Ingredient ingredient = mapper.entityToDomain(entity);

        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(ingredient.getId()).isEqualTo(entity.getId());
        softly.assertThat(ingredient.getName()).isEqualTo("Milch");
        softly.assertThat(ingredient.getComment()).isEqualTo("Vollmilch");
        softly.assertThat(ingredient.getUnit()).isEqualTo(Unit.DECILITRE);
        softly.assertThat(ingredient.getAmount()).isEqualTo(3);
        softly.assertAll();
    }

    @Test
    void domainToEntity() {
        Ingredient ingredient = new Ingredient(UUID.randomUUID(), "Zucker", "Rohzucker", Unit.KILOGRAMM, 1);

        IngredientEntity entity = mapper.domainToEntity(ingredient);

        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(entity.getId()).isEqualTo(ingredient.getId());
        softly.assertThat(entity.getName()).isEqualTo("Zucker");
        softly.assertThat(entity.getComment()).isEqualTo("Rohzucker");
        softly.assertThat(entity.getUnit()).isEqualTo(Unit.KILOGRAMM);
        softly.assertThat(entity.getAmount()).isEqualTo(1);
        softly.assertAll();
    }

    @Test
    void listMapping() {
        List<IngredientEntity> entities = List.of(
                new IngredientEntity(UUID.randomUUID(), "Ei", "", Unit.PIECE, 2),
                new IngredientEntity(UUID.randomUUID(), "Wasser", "", Unit.LITRE, 1));

        List<Ingredient> ingredients = mapper.entitiesToDomains(entities);
        List<IngredientEntity> back = mapper.domainsToEntities(ingredients);

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(ingredients).hasSize(2);
            softly.assertThat(ingredients.get(0).getName()).isEqualTo("Ei");
            softly.assertThat(ingredients.get(1).getUnit()).isEqualTo(Unit.LITRE);
            softly.assertThat(back).isEqualTo(entities);
        });
    }

    @Test
    void nullStaysNull() {
        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(mapper.entityToDomain(null)).isNull();
            softly.assertThat(mapper.domainToEntity(null)).isNull();
            softly.assertThat(mapper.entitiesToDomains(null)).isNull();
            softly.assertThat(mapper.domainsToEntities(null)).isNull();
        });
    }
}
