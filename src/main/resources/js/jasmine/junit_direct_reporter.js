(function() {
    if (! jasmine) {
        throw new Exception("jasmine library does not exist in global namespace!");
    };

    window.Error = function(message, fileName, lineNumber) {
      this.message = message;
      this.fileName = fileName;
      this.lineNumber = lineNumber;
      try {
        throw new org.mozilla.javascript.EvaluatorException(message);
      } catch(e) {
        var stringWriter = new java.io.StringWriter();
        e.printStackTrace(new java.io.PrintWriter(stringWriter));
        this.stack = stringWriter.toString() + "";
      }
    };

    function descriptionChain(spec) {
      var fullName = [spec.description];
      for (var parentSuite = spec.suite; parentSuite; parentSuite = parentSuite.parentSuite) {
        fullName.unshift(parentSuite.description);
      }
      return fullName;
    };

    var JUnitDirectReporter = function(notifier) {
        this.notifier = notifier;
    };

    JUnitDirectReporter.prototype = {

        reportRunnerStarting: function(runner) {},

        reportSpecStarting: function(spec) {
            this.notifier.start(descriptionChain(spec));
        },

        reportSpecResults: function(spec) {
            var results = spec.results();
            if (results.skipped) {
                this.notifier.ignore(descriptionChain(spec));
                return;
            }

            if (results.passed()) {
                this.notifier.ok(descriptionChain(spec));
            } else {
                var items = results.getItems();
                var i = 0;
                var expectationResult;
                while (expectationResult = items[i++]) {
                    if (expectationResult.matcherName && ! expectationResult.passed()) {
                       this.notifier.comparisonFail(descriptionChain(spec), expectationResult.expected, expectationResult.actual, expectationResult.trace.stack);
                       return;
                    } else if (expectationResult.trace) {
                        this.notifier.fail(descriptionChain(spec), expectationResult.trace.stack);
                        return;
                    }
                }
            }
        },

        reportRunnerResults: function(runner) {},
    };

    // export public
    jasmine.JUnitDirectReporter = JUnitDirectReporter;
})();
