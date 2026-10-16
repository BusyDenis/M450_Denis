const { test, expect } = require('@playwright/test');

test('Startseite zeigt Logo und Buttons', async ({ page }) => {
  await page.goto('/');

  await expect(page.getByAltText('Responsive image')).toBeVisible();
  await expect(page.getByText('List Students')).toBeVisible();
  await expect(page.getByText('Add Students')).toBeVisible();
});

test('Liste zeigt die Studenten vom Backend', async ({ page }) => {
  await page.goto('/');
  await page.getByText('List Students').click();

  await expect(page).toHaveURL(/students/);
  await expect(page.getByRole('cell', { name: 'Jonas', exact: true })).toBeVisible();
  await expect(page.getByText('ann@tbz.ch')).toBeVisible();
});

test('Submit Button ist ohne Eingabe deaktiviert', async ({ page }) => {
  await page.goto('/addstudents');

  await expect(page.getByRole('button', { name: 'Submit' })).toBeDisabled();
  await expect(page.getByText('Name is required')).toBeVisible();
});

test('Neuen Studenten hinzufuegen', async ({ page }) => {
  const name = 'Tester' + Date.now();

  await page.goto('/');
  await page.getByText('Add Students').click();

  await page.getByLabel('Name').fill(name);
  await page.getByLabel('Email').fill(name.toLowerCase() + '@tbz.ch');
  await page.getByRole('button', { name: 'Submit' }).click();

  await expect(page).toHaveURL(/students/);
  await expect(page.getByRole('cell', { name: name, exact: true })).toBeVisible();
});

test('Ungueltige E-Mail kann nicht abgeschickt werden', async ({ page }) => {
  await page.goto('/addstudents');

  await page.getByLabel('Name').fill('Max');
  await page.getByLabel('Email').fill('keine-email');

  await expect(page.getByText('Email is not valid')).toBeVisible();
  await expect(page.getByRole('button', { name: 'Submit' })).toBeDisabled();
});
