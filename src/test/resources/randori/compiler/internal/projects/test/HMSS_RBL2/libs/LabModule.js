/** Compiled by the Randori compiler v0.2.4.27 on Tue Jun 25 12:54:17 EDT 2013 */


// ====================================================
// mediator.LabMediator
// ====================================================

if (typeof mediator == "undefined")
	var mediator = {};

mediator.LabMediator = function() {
	this.targetList = null;
	this.labService = null;
	randori.behaviors.AbstractMediator.call(this);
};

mediator.LabMediator.prototype.onRegister = function() {
	this.labService.get().then($createStaticDelegate(this, this.handleResult));
};

mediator.LabMediator.prototype.handleResult = function(result) {
	this.targetList.set_data(result);
};

$inherit(mediator.LabMediator, randori.behaviors.AbstractMediator);

mediator.LabMediator.className = "mediator.LabMediator";

mediator.LabMediator.getRuntimeDependencies = function(t) {
	var p;
	return [];
};

mediator.LabMediator.getStaticDependencies = function(t) {
	var p;
	return [];
};

mediator.LabMediator.injectionPoints = function(t) {
	var p;
	switch (t) {
		case 1:
			p = randori.behaviors.AbstractMediator.injectionPoints(t);
			p.push({n:'labService', t:'services.LabService', r:0, v:null});
			break;
		case 2:
			p = randori.behaviors.AbstractMediator.injectionPoints(t);
			break;
		case 3:
			p = randori.behaviors.AbstractMediator.injectionPoints(t);
			p.push({n:'targetList', t:'randori.behaviors.SimpleList'});
			break;
		default:
			p = [];
			break;
	}
	return p;
};


// ====================================================
// services.LabService
// ====================================================

if (typeof services == "undefined")
	var services = {};

services.LabService = function(xmlHttpRequest, config, targets) {
	this.path = null;
	randori.service.AbstractService.call(this, xmlHttpRequest);
	this.config = config;
	this.targets = targets;
	this.path = "assets\/data\/targets.txt";
};

services.LabService.prototype.get = function() {
	var promise = this.sendRequest("GET", this.path);
	var parserPromise = promise.then($createStaticDelegate(this.targets, this.targets.parseResult));
	return parserPromise;
};

$inherit(services.LabService, randori.service.AbstractService);

services.LabService.className = "services.LabService";

services.LabService.getRuntimeDependencies = function(t) {
	var p;
	p = [];
	p.push('services.parser.GenericJsonParser');
	return p;
};

services.LabService.getStaticDependencies = function(t) {
	var p;
	return [];
};

services.LabService.injectionPoints = function(t) {
	var p;
	switch (t) {
		case 0:
			p = [];
			p.push({n:'xmlHttpRequest', t:'XMLHttpRequest'});
			p.push({n:'config', t:'randori.service.ServiceConfig'});
			p.push({n:'targets', t:'services.parser.GenericJsonParser'});
			break;
		case 1:
			p = randori.service.AbstractService.injectionPoints(t);
			break;
		case 2:
			p = randori.service.AbstractService.injectionPoints(t);
			break;
		case 3:
			p = randori.service.AbstractService.injectionPoints(t);
			break;
		default:
			p = [];
			break;
	}
	return p;
};

