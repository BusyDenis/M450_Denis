import {render, screen, fireEvent, waitFor, within} from '@testing-library/react';
import axios from "axios";
import Planer from "./Planer";

jest.mock('axios', () => ({get: jest.fn(), post: jest.fn()}));

const recipes = [
    {
        id: 'r-1',
        name: 'Pizza',
        description: 'Mit Kaese',
        imageUrl: '',
        ingredients: [{id: 'i-1', name: 'Mehl', comment: '', unit: 'GRAM', amount: 250}]
    },
    {
        id: 'r-2',
        name: 'Salat',
        description: 'Frisch',
        imageUrl: '',
        ingredients: [{id: 'i-2', name: 'Tomate', comment: '', unit: 'PIECE', amount: 3}]
    }
];

const createButton = () => screen.getByRole('button', {name: 'Einkaufsliste erstellen'});

beforeEach(() => {
    axios.get.mockReset();
    axios.post.mockReset();
});

test('loads and shows all recipes', async () => {
    axios.get.mockResolvedValue({data: recipes});
    render(<Planer/>);

    expect(screen.getByRole('heading', {name: 'Wochenplaner'})).toBeInTheDocument();
    expect(await screen.findByText('Pizza')).toBeInTheDocument();
    expect(screen.getByText('Salat')).toBeInTheDocument();
    expect(screen.getByLabelText('Pizza Anzahl')).toHaveTextContent('0');
    expect(axios.get).toHaveBeenCalledWith('http://localhost:8080/api/recipes');
});

test('button is disabled while nothing is planned', async () => {
    axios.get.mockResolvedValue({data: recipes});
    render(<Planer/>);
    await screen.findByText('Pizza');

    expect(createButton()).toBeDisabled();
    expect(screen.getByLabelText('Pizza einmal weniger')).toBeDisabled();

    fireEvent.click(screen.getByLabelText('Pizza einmal mehr'));
    expect(createButton()).toBeEnabled();

    fireEvent.click(screen.getByLabelText('Pizza einmal weniger'));
    expect(screen.getByLabelText('Pizza Anzahl')).toHaveTextContent('0');
    expect(createButton()).toBeDisabled();
});

test('posts the planned ids and shows the shopping list', async () => {
    axios.get.mockResolvedValue({data: recipes});
    axios.post.mockResolvedValue({
        data: [
            {name: 'Mehl', unit: 'GRAM', amount: 500},
            {name: 'Tomate', unit: 'PIECE', amount: 3}
        ]
    });
    render(<Planer/>);
    await screen.findByText('Pizza');

    fireEvent.click(screen.getByLabelText('Pizza einmal mehr'));
    fireEvent.click(screen.getByLabelText('Pizza einmal mehr'));
    fireEvent.click(screen.getByLabelText('Salat einmal mehr'));
    fireEvent.click(createButton());

    expect(await screen.findByRole('heading', {name: 'Einkaufsliste'})).toBeInTheDocument();
    expect(axios.post).toHaveBeenCalledWith('http://localhost:8080/api/shopping-list', ['r-1', 'r-1', 'r-2']);

    const rows = within(screen.getByRole('table')).getAllByRole('row');
    expect(rows).toHaveLength(3);
    expect(within(rows[0]).getAllByRole('columnheader').map(cell => cell.textContent)).toEqual(['Zutat', 'Menge', 'Einheit']);
    expect(within(rows[1]).getAllByRole('cell').map(cell => cell.textContent)).toEqual(['Mehl', '500', 'g']);
    expect(within(rows[2]).getAllByRole('cell').map(cell => cell.textContent)).toEqual(['Tomate', '3', 'Stk.']);
});

test('shows error when shopping list can not be created', async () => {
    axios.get.mockResolvedValue({data: recipes});
    axios.post.mockRejectedValue(new Error('404'));
    render(<Planer/>);
    await screen.findByText('Pizza');

    fireEvent.click(screen.getByLabelText('Pizza einmal mehr'));
    fireEvent.click(createButton());

    expect(await screen.findByText('Einkaufsliste konnte nicht erstellt werden.')).toBeInTheDocument();
    expect(screen.queryByRole('table')).not.toBeInTheDocument();
    await waitFor(() => expect(createButton()).toBeEnabled());
});

test('shows error when recipes can not be loaded', async () => {
    axios.get.mockRejectedValue(new Error('500'));
    render(<Planer/>);

    expect(await screen.findByText('Rezepte konnten nicht geladen werden.')).toBeInTheDocument();
    expect(createButton()).toBeDisabled();
});
