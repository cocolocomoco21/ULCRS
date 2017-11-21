var gulp = require('gulp'),
    browserify = require('gulp-browserify'),
    concatCss = require('gulp-concat-css'),
    run = require('gulp-run');

var src = './process',
    app = './app';

gulp.task('js', function() {
  return gulp.src( src + '/js/*.js' )
    .pipe(browserify({
      transform: 'reactify',
      extensions: 'browserify-css',
      debug: true
    }))
    .on('error', function (err) {
      console.error('Error!', err.message);
    })
    .pipe(gulp.dest(app + '/js'));
});


gulp.task('html', function() {
  gulp.src( src + '/**/*.html');
});

gulp.task('css', function() {
  gulp.src( src + '/css/*.css')
  .pipe(concatCss('app.css'))
  .pipe(gulp.dest(app + '/css'));
});

gulp.task('fonts', function() {
    gulp.src('node_modules/bootstrap/dist/fonts/**/*')
    .pipe(gulp.dest(app + '/fonts'));
});

gulp.task('watch', ['serve'], function() {
  gulp.watch( src + '/js/**/*', ['js']);
  gulp.watch( src + '/css/**/*.css', ['css']);
  gulp.watch([ app + '/**/*.html'], ['html']);
});

gulp.task('serve', ['html', 'js', 'css'], function() {
  // verbosity: 3 here allows output to print console.logs
  // from Electron as soon as they come, rather than buffering
  // https://stackoverflow.com/questions/44393814/the-console-logs-of-an-electron-app-runned-by-gulp-are-not-shown
  run('electron app/main.js', {verbosity: 3}).exec().;

});

gulp.task('default', ['watch', 'fonts', 'serve']);
