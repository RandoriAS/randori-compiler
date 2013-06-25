/** Compiled by the Randori compiler v0.2.4.27 on Tue Jun 25 12:54:12 EDT 2013 */


// ====================================================
// services.parser.GenericJsonParser
// ====================================================

if (typeof services == "undefined")
	var services = {};
if (typeof services.parser == "undefined")
	services.parser = {};

services.parser.GenericJsonParser = function() {
	randori.service.parser.AbstractParser.call(this);
};

services.parser.GenericJsonParser.prototype.parseResult = function(result) {
	var json = JSON.parse(result);
	return json;
};

$inherit(services.parser.GenericJsonParser, randori.service.parser.AbstractParser);

services.parser.GenericJsonParser.className = "services.parser.GenericJsonParser";

services.parser.GenericJsonParser.getRuntimeDependencies = function(t) {
	var p;
	return [];
};

services.parser.GenericJsonParser.getStaticDependencies = function(t) {
	var p;
	return [];
};

services.parser.GenericJsonParser.injectionPoints = function(t) {
	var p;
	switch (t) {
		case 1:
			p = randori.service.parser.AbstractParser.injectionPoints(t);
			break;
		case 2:
			p = randori.service.parser.AbstractParser.injectionPoints(t);
			break;
		case 3:
			p = randori.service.parser.AbstractParser.injectionPoints(t);
			break;
		default:
			p = [];
			break;
	}
	return p;
};

