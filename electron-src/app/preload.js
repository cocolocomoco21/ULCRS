process.once('loaded', ()=> {
    if (process.env.NODE_ENV === 'test') {
        window.electronRequire = require;
        global.process = process;
    }
});