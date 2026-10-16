import React, {useEffect, useState} from 'react';
import {useNavigate, useParams} from "react-router-dom";
import axios from "axios";
import RecipeForm from "../RecipeForm/RecipeForm";

const baseURL = "http://localhost:8080/api/recipes/recipe/";

function EditRecipe() {
    const {recipeId} = useParams();
    const navigate = useNavigate();
    const [recipe, setRecipe] = useState(null);
    const [error, setError] = useState('');

    useEffect(() => {
        axios.get(baseURL + recipeId)
            .then(response => setRecipe(response.data))
            .catch(() => setError('Rezept wurde nicht gefunden.'));
    }, [recipeId]);

    const saveRecipe = (changedRecipe) => {
        axios.put(baseURL + recipeId, changedRecipe)
            .then(() => navigate('/'))
            .catch(() => setError('Rezept konnte nicht gespeichert werden.'));
    }

    if (!recipe) {
        return <p className="m-3">{error || 'Laden...'}</p>
    }

    return <RecipeForm title="Edit Recipe" startRecipe={recipe} onSave={saveRecipe} error={error}/>
}

export default EditRecipe;
