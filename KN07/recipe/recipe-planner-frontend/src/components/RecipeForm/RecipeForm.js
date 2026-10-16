import React, {useState} from 'react';
import '../AddRecipe/AddRecipe.css';
import {Form, Button, Col, Row, Alert} from 'react-bootstrap';
import AddIngredient from "../AddIngredient/AddIngredient";

function RecipeForm({title, startRecipe, onSave, error}) {
    const [name, setName] = useState(startRecipe.name || '');
    const [description, setDescription] = useState(startRecipe.description || '');
    const [imageUrl, setImageUrl] = useState(startRecipe.imageUrl || '');
    const [ingredients, setIngredients] = useState(
        (startRecipe.ingredients || []).map((ingredient, index) => ({...ingredient, key: index + 1}))
    );
    const [nextKey, setNextKey] = useState(ingredients.length + 1);
    const [nameError, setNameError] = useState('');

    const addIngredient = () => {
        setIngredients([...ingredients, {key: nextKey, name: '', unit: 'PIECE', amount: '', comment: ''}]);
        setNextKey(nextKey + 1);
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

        if (name.trim() === '') {
            setNameError('Bitte einen Namen eingeben.');
            return;
        }

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
        });
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
                <Button variant="primary" type="submit" className="mb-5">Submit</Button>
            </Form>
        </div>
    )
}

export default RecipeForm;
