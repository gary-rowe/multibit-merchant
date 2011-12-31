// Extra String functionality
String.prototype.supplant = function(o) {
  return this.replace(/{([^{}]*)}/g, function(a, b) {
    var r = o[b];
    return typeof r === 'string' ? r : a;
  });
};
// Trim
String.prototype.trim = function() {
  return (this.replace(/^[\s\xA0]+/, "").replace(/[\s\xA0]+$/, ""))
};

// Starts with
String.prototype.startsWith = function(str) {
  return (this.match("^" + str) == str)
};

// Ends with
String.prototype.endsWith = function(str) {
  return (this.match(str + "$") == str)
};

// Object fields into tokenised string
String.prototype.supplant = function(o) {
  return this.replace(/{([^{}]*)}/g, function(a, b) {
    var r = o[b];
    return typeof r === 'string' ? r : a;
  });
};