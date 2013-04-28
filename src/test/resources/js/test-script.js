var doIt = function(a,b) {
    return $.map([a, b], function(value, index) {
        return index+' -> '+value;
    });
};