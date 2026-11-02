import React, {useEffect, useRef, useState} from 'react';
import '../AddRecipe/AddRecipe.css';
import {Form, Button, Col, Row, Alert} from 'react-bootstrap';
import AddIngredient from "../AddIngredient/AddIngredient";

// Eindeutiger Key fuer jede Zutat. crypto.randomUUID() gibt es in Jest (jsdom) nicht,
// darum ein Zaehler, der nie zurueckgesetzt wird und so keinen Key doppelt vergibt.
let lastKey = 0;
const newKey = () => 'ingredient-' + (++lastKey);

// Zutaten aus der DB behalten ihre id als Key, alle anderen bekommen einen neuen Key
const withKeys = (ingredients) =>
    (ingredients || []).map(ingredient => ({...ingredient, key: ingredient.id || newKey()}));

function RecipeForm({title, startRecipe, onSave, error}) {
    const [name, setName] = useState(startRecipe.name || '');
    const [description, setDescription] = useState(startRecipe.description || '');
    const [imageUrl, setImageUrl] = useState(startRecipe.imageUrl || '');
    const [ingredients, setIngredients] = useState(() => withKeys(startRecipe.ingredients));
    const [nameError, setNameError] = useState('');
    const [isSubmitting, setIsSubmitting] = useState(false);

    // Formular neu fuellen, wenn ein anderes Rezept uebergeben wird
    // (beim ersten Render ist es schon im useState drin, darum dann nichts machen)
    const loadedRecipe = useRef(startRecipe);
    useEffect(() => {
        if (loadedRecipe.current === startRecipe) {
            return;
        }
        loadedRecipe.current = startRecipe;
        setName(startRecipe.name || '');
        setDescription(startRecipe.description || '');
        setImageUrl(startRecipe.imageUrl || '');
        setIngredients(withKeys(startRecipe.ingredients));
    }, [startRecipe]);

    const addIngredient = () => {
        setIngredients([...ingredients, {key: newKey(), name: '', unit: 'PIECE', amount: '', comment: ''}]);
    }

    const updateIngredient = (changedIngredient) => {
        setIngredients(ingredients.map(ingredient =>
            ingredient.key === changedIngredient.key ? changedIngredient : ingredient
        ));
    }

    const removeIngredient = (removedIngredient) => {
        setIngredients(ingredients.filter(ingredient => ingredient.key !== removedIngredient.key));
    }

    const save = (event) => {
        event.preventDefault();

        if (isSubmitting) {
            return;
        }

        if (name.trim() === '') {
            setNameError('Bitte einen Namen eingeben.');
            return;
        }

        setIsSubmitting(true);
        onSave({
            name: name,
            description: description,
            imageUrl: imageUrl,
            ingredients: ingredients.map(ingredient => ({
                name: ingredient.name,
                unit: ingredient.unit,
                amount: Number(ingredient.amount),
                comment: ingredient.comment || ''
            }))
        }).finally(() => setIsSubmitting(false));
    }

    return (
        <div className="bg">
            <Form className="m-3" onSubmit={save}>
                <h1 className="h3 bg-dark text-bg-primary mt-2">{title}</h1>
                {nameError && <Alert variant="danger">{nameError}</Alert>}
                {error && <Alert variant="danger">{error}</Alert>}
                <Form.Group className="mb-1" controlId="recipeName">
                    <Form.Label>Recipe Name:</Form.Label>
                    <Form.Control placeholder="Name" value={name} onChange={e => setName(e.target.value)}/>
                </Form.Group>
                <Form.Group className="mb-1" controlId="recipeDescription">
                    <Form.Label>Description:</Form.Label>
                    <Form.Control placeholder="Description" value={description} onChange={e => setDescription(e.target.value)}/>
                </Form.Group>
                <Form.Group className="mb-1 mb-5" controlId="recipeImageUrl">
                    <Form.Label>Image URL:</Form.Label>
                    <Form.Control placeholder="URL" value={imageUrl} onChange={e => setImageUrl(e.target.value)}/>
                </Form.Group>
                <Row>
                    <Col>Ingredient</Col>
                    <Col>Unit</Col>
                    <Col>Quantity</Col>
                    <Col xs={1}></Col>
                </Row>
                <hr/>
                {ingredients.map(ingredient =>
                    <AddIngredient
                        key={ingredient.key}
                        ingredient={ingredient}
                        updateIngredient={updateIngredient}
                        removeIngredient={removeIngredient}
                    />
                )}
                <Button variant="warning" onClick={addIngredient} className="mt-1">Add Ingredient</Button>
                <Button variant="primary" type="submit" className="mb-5" disabled={isSubmitting}>Submit</Button>
            </Form>
        </div>
    )
}

export default RecipeForm;
