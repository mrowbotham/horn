
var runJasmine = function(notifier) {
  var jasmineEnv = jasmine.getEnv();
  jasmineEnv.updateInterval = null;
  var reporter = new jasmine.JUnitDirectReporter(notifier);
  jasmineEnv.addReporter(reporter);
  jasmineEnv.execute();
  return reporter;
};