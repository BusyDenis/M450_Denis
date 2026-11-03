import React, {useEffect, useState} from 'react';
import './Planer.css';
import axios from "axios";
import {Alert, Button, ListGroup, Table} from "react-bootstrap";
import formatUnit from "./formatUnit";

const recipesURL = "http://localhost:8080/api/recipes";
const shoppingListURL = "http://localhost:8080/api/shopping-list";

function Planer() {
    const [recipes, setRecipes] = useState(null);
    // wie oft jedes Rezept diese Woche geplant ist, z.B. {"abc-123": 2}
    const [counts, setCounts] = useState({});
    const [shoppingList, setShoppingList] = useState(null);
    const [error, setError] = useState('');
    const [isCreating, setIsCreating] = useState(false);

    useEffect(() => {
        axios.get(recipesURL)
            .then(response => setRecipes(response.data))
            .catch(() => setError('Rezepte konnten nicht geladen werden.'));
    }, []);

    const countOf = (recipe) => counts[recipe.id] || 0;

    const changeCount = (recipe, difference) => {
        const newCount = Math.max(0, countOf(recipe) + difference);
        setCounts({...counts, [recipe.id]: newCount});
        // alte Liste passt nicht mehr zum Plan, darum weg damit
        setShoppingList(null);
    }

    const plannedTotal = Object.values(counts).reduce((sum, count) => sum + count, 0);

    const createShoppingList = () => {
        // jede id so oft mitschicken, wie das Rezept geplant ist (2x Pizza = 2x die id)
        const ids = (recipes || []).flatMap(recipe => Array(countOf(recipe)).fill(recipe.id));

        setError('');
        setIsCreating(true);
        axios.post(shoppingListURL, ids)
            .then(response => setShoppingList(response.data))
            .catch(() => setError('Einkaufsliste konnte nicht erstellt werden.'))
            .finally(() => setIsCreating(false));
    }

    return (
        <div className="Planer m-3">
            <h1 className="h3 mt-2">Wochenplaner</h1>
            {error && <Alert variant="danger">{error}</Alert>}

            {recipes && recipes.length === 0 && <p>Noch keine Rezepte vorhanden.</p>}

            {recipes && recipes.length > 0 &&
                <ListGroup className="mb-3">
                    {recipes.map(recipe =>
                        <ListGroup.Item key={recipe.id} className="d-flex justify-content-between align-items-center">
                            <div>
                                <div className="fw-bold">{recipe.name}</div>
                                <small className="text-muted">{recipe.description}</small>
                            </div>
                            <div className="d-flex align-items-center">
                                <small className="text-muted me-2">wie oft diese Woche</small>
                                <Button variant="outline-secondary" size="sm"
                                        aria-label={recipe.name + ' einmal weniger'}
                                        disabled={countOf(recipe) === 0}
                                        onClick={() => changeCount(recipe, -1)}>-</Button>
                                <span className="Planer-count" aria-label={recipe.name + ' Anzahl'}>{countOf(recipe)}</span>
                                <Button variant="outline-secondary" size="sm"
                                        aria-label={recipe.name + ' einmal mehr'}
                                        onClick={() => changeCount(recipe, 1)}>+</Button>
                            </div>
                        </ListGroup.Item>
                    )}
                </ListGroup>
            }

            <Button variant="primary" disabled={plannedTotal === 0 || isCreating} onClick={createShoppingList}>
                Einkaufsliste erstellen
            </Button>
            <span className="ms-2 text-muted">{plannedTotal} Mahlzeiten geplant</span>

            {shoppingList &&
                <div className="mt-4">
                    <h2 className="h4">Einkaufsliste</h2>
                    <Table striped bordered size="sm">
                        <thead>
                        <tr>
                            <th>Zutat</th>
                            <th>Menge</th>
                            <th>Einheit</th>
                        </tr>
                        </thead>
                        <tbody>
                        {shoppingList.map(item =>
                            <tr key={item.name + '-' + item.unit}>
                                <td>{item.name}</td>
                                <td>{item.amount}</td>
                                <td>{formatUnit(item.unit)}</td>
                            </tr>
                        )}
                        </tbody>
                    </Table>
                </div>
            }
        </div>
    );
}

export default Planer;
