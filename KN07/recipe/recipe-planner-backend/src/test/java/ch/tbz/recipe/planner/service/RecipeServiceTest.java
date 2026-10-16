package ch.tbz.recipe.planner.service;

import ch.tbz.recipe.planner.domain.Recipe;
import ch.tbz.recipe.planner.entities.RecipeEntity;
import ch.tbz.recipe.planner.mapper.RecipeEntityMapper;
import ch.tbz.recipe.planner.repository.RecipeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RecipeServiceTest {

    @Mock
    private RecipeRepository repository;

    private RecipeService service;
    private RecipeEntity lasagne;

    @BeforeEach
    void setUp() {
        service = new RecipeService(Mappers.getMapper(RecipeEntityMapper.class), repository);
        lasagne = new RecipeEntity(UUID.randomUUID(), "Lasagne", "Lecker", "lasagne.jpg", List.of());
    }

    @Test
    void getRecipes() {
        when(repository.findAll()).thenReturn(List.of(lasagne));

        List<Recipe> recipes = service.getRecipes();

        assertThat(recipes).hasSize(1);
        assertThat(recipes.get(0).getName()).isEqualTo("Lasagne");
    }

    @Test
    void getRecipeById() {
        when(repository.findById(lasagne.getId())).thenReturn(Optional.of(lasagne));

        assertThat(service.getRecipeById(lasagne.getId()).getName()).isEqualTo("Lasagne");
    }

    @Test
    void getUnknownRecipeReturnsNull() {
        UUID unknownId = UUID.randomUUID();
        when(repository.findById(unknownId)).thenReturn(Optional.empty());

        assertThat(service.getRecipeById(unknownId)).isNull();
    }

    @Test
    void addRecipe() {
        when(repository.save(any(RecipeEntity.class))).thenReturn(lasagne);

        Recipe created = service.addRecipe(new Recipe(null, "Lasagne", "Lecker", "lasagne.jpg", List.of()));

        assertThat(created.getId()).isEqualTo(lasagne.getId());
        verify(repository).save(any(RecipeEntity.class));
    }

    @Test
    void updateRecipe() {
        when(repository.findById(lasagne.getId())).thenReturn(Optional.of(lasagne));
        when(repository.save(any(RecipeEntity.class))).thenAnswer(call -> call.getArgument(0));

        Recipe updated = service.updateRecipe(lasagne.getId(), new Recipe(null, "Lasagne Neu", "Noch besser", "", List.of()));

        assertThat(updated.getId()).isEqualTo(lasagne.getId());
        assertThat(updated.getName()).isEqualTo("Lasagne Neu");
    }

    @Test
    void updateUnknownRecipeReturnsNull() {
        UUID unknownId = UUID.randomUUID();
        when(repository.findById(unknownId)).thenReturn(Optional.empty());

        assertThat(service.updateRecipe(unknownId, new Recipe())).isNull();
        verify(repository, never()).save(any(RecipeEntity.class));
    }
}
