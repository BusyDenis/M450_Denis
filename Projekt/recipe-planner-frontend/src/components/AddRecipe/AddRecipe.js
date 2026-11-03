import React from 'react';
import {useNavigate} from "react-router-dom";
import axios from "axios";
import RecipeForm from "../RecipeForm/RecipeForm";

const baseURL = "http://localhost:8080/api/recipes";

// ausserhalb der Komponente, damit es bei jedem Render dasselbe Objekt ist (sonst setzt der useEffect im Formular alles zurueck)
const emptyRecipe = {};

function AddRecipe() {
    const navigate = useNavigate();

    const saveRecipe = (recipe) => {
        return axios.post(baseURL, recipe)
            .then(() => navigate('/'));
    }

    return <RecipeForm title="Add Recipe" startRecipe={emptyRecipe} onSave={saveRecipe}/>
}

export default AddRecipe;
