import {render, screen, fireEvent, waitFor} from '@testing-library/react';
import {MemoryRouter} from "react-router-dom";
import axios from "axios";
import AddRecipe from "./AddRecipe";

jest.mock('axios', () => ({post: jest.fn()}));

const renderPage = () => render(<MemoryRouter><AddRecipe/></MemoryRouter>);

beforeEach(() => {
    axios.post.mockReset();
});

test('shows error when name is empty', () => {
    renderPage();

    fireEvent.click(screen.getByText('Submit'));

    expect(screen.getByText('Bitte einen Namen eingeben.')).toBeInTheDocument();
    expect(axios.post).not.toHaveBeenCalled();
});

test('sends recipe with ingredient to backend', async () => {
    axios.post.mockResolvedValue({data: {}});
    renderPage();

    fireEvent.change(screen.getByLabelText('Recipe Name:'), {target: {value: 'Pizza'}});
    fireEvent.change(screen.getByLabelText('Description:'), {target: {value: 'Mit Kaese'}});
    fireEvent.click(screen.getByText('Add Ingredient'));
    fireEvent.change(screen.getByLabelText('Ingredient name'), {target: {value: 'Mehl'}});
    fireEvent.change(screen.getByLabelText('Unit'), {target: {value: 'GRAMM'}});
    fireEvent.change(screen.getByLabelText('Quantity'), {target: {value: '500'}});
    fireEvent.click(screen.getByText('Submit'));

    await waitFor(() => expect(axios.post).toHaveBeenCalledTimes(1));
    expect(axios.post.mock.calls[0][1]).toEqual({
        name: 'Pizza',
        description: 'Mit Kaese',
        imageUrl: '',
        ingredients: [{name: 'Mehl', unit: 'GRAMM', amount: 500, comment: ''}]
    });
});

test('removes ingredient', () => {
    renderPage();

    fireEvent.click(screen.getByText('Add Ingredient'));
    fireEvent.click(screen.getByText('x'));

    expect(screen.queryByLabelText('Ingredient name')).not.toBeInTheDocument();
});

test('shows error when backend fails', async () => {
    axios.post.mockRejectedValue(new Error('500'));
    renderPage();

    fireEvent.change(screen.getByLabelText('Recipe Name:'), {target: {value: 'Pizza'}});
    fireEvent.click(screen.getByText('Submit'));

    expect(await screen.findByText('Rezept konnte nicht gespeichert werden.')).toBeInTheDocument();
});
