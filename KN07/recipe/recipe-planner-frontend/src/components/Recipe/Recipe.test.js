import {render, screen} from '@testing-library/react';
import {MemoryRouter} from "react-router-dom";
import Recipe from "./Recipe";

test('edit button has a real link to the edit page', () => {
    render(<MemoryRouter><Recipe id="abc-123" title="Pizza" description="Mit Kaese" image="pizza.jpg"/></MemoryRouter>);

    expect(screen.getByRole('button', {name: 'Edit Details'})).toHaveAttribute('href', '/edit/abc-123');
});
