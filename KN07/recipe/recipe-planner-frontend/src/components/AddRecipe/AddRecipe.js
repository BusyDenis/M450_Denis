import React, {useState} from 'react';
import {useNavigate} from "react-router-dom";
import axios from "axios";
import RecipeForm from "../RecipeForm/RecipeForm";

const baseURL = "http://localhost:8080/api/recipes";

function AddRecipe() {
    const navigate = useNavigate();
    const [error, setError] = useState('');

    const saveRecipe = (recipe) => {
        axios.post(baseURL, recipe)
            .then(() => navigate('/'))
            .catch(() => setError('Rezept konnte nicht gespeichert werden.'));
    }

    return <RecipeForm title="Add Recipe" startRecipe={{}} onSave={saveRecipe} error={error}/>
}

export default AddRecipe;
