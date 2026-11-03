import formatUnit from "./formatUnit";

describe('formatUnit', () => {
    it.each([
        ['GRAM', 'g'],
        ['KILOGRAM', 'kg'],
        ['LITRE', 'l'],
        ['DECILITRE', 'dl'],
        ['PIECE', 'Stk.'],
    ])('zeigt %s als %s an', (unit, expected) => {
        expect(formatUnit(unit)).toBe(expected);
    });

    it('gibt eine unbekannte Einheit unveraendert zurueck', () => {
        expect(formatUnit('TEELOEFFEL')).toBe('TEELOEFFEL');
    });

    it('gibt einen leeren Text zurueck, wenn keine Einheit da ist', () => {
        expect(formatUnit(undefined)).toBe('');
    });
});
