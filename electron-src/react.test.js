let React = require('react');
let renderer = require('react-test-renderer');
let path = require('path');
let spectron = require('spectron');
let assert = require('assert');
let Application = require('spectron').Application;
let main_path = path.resolve(__dirname,'app/main.js');

describe('application launch', function () {
    let app = undefined;
    beforeEach(function () {
        app = new Application({
            path: path.resolve(__dirname,'node_modules/.bin/electron'),
            args: [main_path]
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
        // No selected text
        app.client.getSelectedText().then(function (str) {
            assert.equal(str, "")
        })
    });

});