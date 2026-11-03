// Einheiten aus dem Backend (Enum) so anzeigen, wie man sie im Alltag schreibt
const units = {
    GRAM: 'g',
    KILOGRAM: 'kg',
    LITRE: 'l',
    DECILITRE: 'dl',
    PIECE: 'Stk.',
};

function formatUnit(unit) {
    if (!unit) {
        return '';
    }
    // unbekannte Einheit einfach so lassen, damit man trotzdem etwas sieht
    return units[unit] || unit;
}

export default formatUnit;
