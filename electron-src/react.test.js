let React = require('react');
let renderer = require('react-test-renderer');
let path = require('path');
let spectron = require('spectron');
let assert = require('assert');
let Application = require('spectron').Application;

describe('application launch', function () {
    let app = undefined;
    beforeEach(function () {
        app = new Application({
            path: '/Users/xu/Desktop/ULCRS/ULCRS/electron-src/node_modules/.bin/electron',
            args: ['/Users/xu/Desktop/ULCRS/ULCRS/electron-src/app/main.js']
        });
        return app.start()
    });

    afterEach(function () {
        if (app && app.isRunning()) {
            return app.stop()
        }
    });

    it('shows a correct window', function () {
        app.client.getWindowCount().then(function (count) {
            assert.equal(count, 1)
        });
        app.client.getSelectedText('#header').then(function (str) {
            assert.equal(str, "ULCRS")
        })
    });

    it('click on a button', function () {
        let but = app.client.element("#load");
        but.click();
        assert(1,1)
    })
});