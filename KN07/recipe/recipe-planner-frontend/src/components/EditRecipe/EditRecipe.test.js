import {render, screen, fireEvent, waitFor} from '@testing-library/react';
import {MemoryRouter, Route, Routes} from "react-router-dom";
import axios from "axios";
import EditRecipe from "./EditRecipe";

jest.mock('axios', () => ({get: jest.fn(), put: jest.fn()}));

const pizza = {
    id: 'abc-123',
    name: 'Pizza',
    description: 'Mit Kaese',
    imageUrl: 'pizza.jpg',
    ingredients: [{id: 'i-1', name: 'Mehl', unit: 'GRAMM', amount: 500, comment: ''}]
};

const renderPage = () => render(
    <MemoryRouter initialEntries={['/edit/abc-123']}>
        <Routes>
            <Route path="/edit/:recipeId" element={<EditRecipe/>}/>
            <Route path="/" element={<p>Startseite</p>}/>
        </Routes>
    </MemoryRouter>
);

beforeEach(() => {
    axios.get.mockReset();
    axios.put.mockReset();
});

test('loads recipe into the form', async () => {
    axios.get.mockResolvedValue({data: pizza});
    renderPage();

    expect(await screen.findByDisplayValue('Pizza')).toBeInTheDocument();
    expect(screen.getByDisplayValue('Mit Kaese')).toBeInTheDocument();
    expect(screen.getByDisplayValue('Mehl')).toBeInTheDocument();
    expect(axios.get).toHaveBeenCalledWith('http://localhost:8080/api/recipes/recipe/abc-123');
});

test('saves changed recipe with PUT', async () => {
    axios.get.mockResolvedValue({data: pizza});
    axios.put.mockResolvedValue({data: {}});
    renderPage();

    fireEvent.change(await screen.findByDisplayValue('Pizza'), {target: {value: 'Pizza Margherita'}});
    fireEvent.click(screen.getByText('Submit'));

    await waitFor(() => expect(axios.put).toHaveBeenCalledTimes(1));
    expect(axios.put.mock.calls[0][0]).toBe('http://localhost:8080/api/recipes/recipe/abc-123');
    expect(axios.put.mock.calls[0][1].name).toBe('Pizza Margherita');
    expect(axios.put.mock.calls[0][1].ingredients).toEqual([{name: 'Mehl', unit: 'GRAMM', amount: 500, comment: ''}]);
    expect(await screen.findByText('Startseite')).toBeInTheDocument();
});

test('shows message when recipe does not exist', async () => {
    axios.get.mockRejectedValue(new Error('404'));
    renderPage();

    expect(await screen.findByText('Rezept wurde nicht gefunden.')).toBeInTheDocument();
});
