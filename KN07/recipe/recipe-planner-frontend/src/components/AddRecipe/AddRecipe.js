import React, {useState} from 'react';
import './AddRecipe.css';
import {Form, Button, Col, Row, Alert} from 'react-bootstrap';
import {useNavigate} from "react-router-dom";
import axios from "axios";
import AddIngredient from "../AddIngredient/AddIngredient";

const baseURL = "http://localhost:8080/api/recipes";

function AddRecipe() {
    const navigate = useNavigate();
    const [name, setName] = useState('');
    const [description, setDescription] = useState('');
    const [imageUrl, setImageUrl] = useState('');
    const [ingredients, setIngredients] = useState([]);
    const [nextKey, setNextKey] = useState(1);
    const [error, setError] = useState('');

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

    const saveRecipe = (event) => {
        event.preventDefault();

        if (name.trim() === '') {
            setError('Bitte einen Namen eingeben.');
            return;
        }

        const recipe = {
            name: name,
            description: description,
            imageUrl: imageUrl,
            ingredients: ingredients.map(ingredient => ({
                name: ingredient.name,
                unit: ingredient.unit,
                amount: Number(ingredient.amount),
                comment: ingredient.comment
            }))
        };

        axios.post(baseURL, recipe)
            .then(() => navigate('/'))
            .catch(() => setError('Rezept konnte nicht gespeichert werden.'));
    }

    return (
        <div className="bg">
            <Form className="m-3" onSubmit={saveRecipe}>
                <h1 className="h3 bg-dark text-bg-primary mt-2">Add Recipe</h1>
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

export default AddRecipe;
