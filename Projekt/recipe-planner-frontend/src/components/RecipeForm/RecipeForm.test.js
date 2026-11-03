import {render, screen} from '@testing-library/react';
import RecipeForm from "./RecipeForm";

test('fills form again when startRecipe changes', () => {
    const {rerender} = render(<RecipeForm title="Edit Recipe" startRecipe={{name: 'Pizza'}} onSave={jest.fn()}/>);
    expect(screen.getByLabelText('Recipe Name:')).toHaveValue('Pizza');

    const lasagne = {
        name: 'Lasagne',
        description: 'Lecker',
        imageUrl: '',
        ingredients: [{id: 'i-1', name: 'Tomate', unit: 'PIECE', amount: 3, comment: ''}]
    };
    rerender(<RecipeForm title="Edit Recipe" startRecipe={lasagne} onSave={jest.fn()}/>);

    expect(screen.getByLabelText('Recipe Name:')).toHaveValue('Lasagne');
    expect(screen.getByLabelText('Description:')).toHaveValue('Lecker');
    expect(screen.getByLabelText('Ingredient name')).toHaveValue('Tomate');
});
