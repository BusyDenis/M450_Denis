const { defineConfig } = require('@playwright/test');

module.exports = defineConfig({
  testDir: './tests',
  reporter: [['list'], ['html', { open: 'never' }]],
  use: {
    baseURL: 'http://localhost:4200',
    headless: false,
    launchOptions: { slowMo: 500 },
    screenshot: 'on',
    video: 'on'
  }
});
