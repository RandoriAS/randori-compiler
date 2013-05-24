/** Compiled by the Randori compiler v0.2.1 on Sat Mar 30 08:15:09 EDT 2013 */


// ====================================================
// randori.service.parser.AbstractParser
// ====================================================

if (typeof randori == "undefined")
	var randori = {};
if (typeof randori.service == "undefined")
	randori.service = {};
if (typeof randori.service.parser == "undefined")
	randori.service.parser = {};

randori.service.parser.AbstractParser = function() {
};

randori.service.parser.AbstractParser.className = "randori.service.parser.AbstractParser";

randori.service.parser.AbstractParser.getClassDependencies = function(t) {
	var p;
	return [];
};

randori.service.parser.AbstractParser.injectionPoints = function(t) {
	return [];
};

// ====================================================
// randori.async.Promise
// ====================================================

if (typeof randori == "undefined")
	var randori = {};
if (typeof randori.async == "undefined")
	randori.async = {};

randori.async.Promise = function() {
	this.reason = null;
	this.state = 0;
	this.thenContracts = null;
	this.value = null;
	this.thenContracts = [];
};

randori.async.Promise.PENDING =0;

randori.async.Promise.REJECTED =1;

randori.async.Promise.FULLFILLED =2;

randori.async.Promise.prototype.isFunction = function(obj) {
	return !!(obj && obj.constructor && obj.call && obj.apply);
};

randori.async.Promise.prototype.then = function(onFulfilled, onRejected) {
	if (arguments.length < 2) {
		if (arguments.length < 1) {
			onFulfilled = null;
		}
		onRejected = null;
	}
	var promise = new randori.async.Promise();
	if (!this.isFunction($createStaticDelegate(this, onFulfilled))) {
		onFulfilled = null;
	}
	if (!this.isFunction($createStaticDelegate(this, onRejected))) {
		onRejected = null;
	}
	var thenContract = {fullfilledHandler:onFulfilled, rejectedHandler:onRejected, promise:promise};
	this.thenContracts.push(thenContract);
	var that = this;
	if (this.state == 2) {
		setTimeout(function() {
			that.fullfill(this.value);
		}, 1);
	} else if (this.state == 1) {
		setTimeout(function() {
			that.internalReject(this.reason);
		}, 1);
	}
	return promise;
};

randori.async.Promise.prototype.resolve = function(response) {
	if (this.state == 0) {
		this.value = response;
		this.fullfill(response);
	}
};

randori.async.Promise.prototype.fullfill = function(response) {
	this.state = 2;
	while (this.thenContracts.length > 0) {
		var thenContract = this.thenContracts.shift();
		if (thenContract.fullfilledHandler != null) {
			try {
				var callBackResult = thenContract.fullfilledHandler(response);
				if (callBackResult && callBackResult.then != null) {
					var returnedPromise = callBackResult;
					returnedPromise.then(function(innerResponse) {
						thenContract.promise.resolve(innerResponse);
					}, function(innerReason) {
						thenContract.promise.reject(innerReason);
					});
				} else {
					thenContract.promise.resolve(callBackResult);
				}
			} catch (error) {
				thenContract.promise.reject(error);
			}
		} else {
			thenContract.promise.resolve(response);
		}
	}
};

randori.async.Promise.prototype.reject = function(reason) {
	if (this.state == 0) {
		this.reason = reason;
		this.internalReject(reason);
	}
};

randori.async.Promise.prototype.internalReject = function(reason) {
	this.state = 1;
	while (this.thenContracts.length > 0) {
		var thenContract = this.thenContracts.shift();
		if (thenContract.rejectedHandler != null) {
			try {
				var callBackResult = thenContract.rejectedHandler(reason);
				if (callBackResult && callBackResult.then != null) {
					var returnedPromise = callBackResult;
					returnedPromise.then(function(innerResponse) {
						thenContract.promise.resolve(innerResponse);
					}, function(innerReason) {
						thenContract.promise.reject(innerReason);
					});
				} else {
					thenContract.promise.resolve(callBackResult);
				}
			} catch (error) {
				thenContract.promise.reject(error);
			}
		} else {
			thenContract.promise.reject(reason);
		}
	}
};

randori.async.Promise.className = "randori.async.Promise";

randori.async.Promise.getClassDependencies = function(t) {
	var p;
	return [];
};

randori.async.Promise.injectionPoints = function(t) {
	return [];
};

// ====================================================
// randori.bus.AbstractEventBus
// ====================================================

if (typeof randori == "undefined")
	var randori = {};
if (typeof randori.bus == "undefined")
	randori.bus = {};

randori.bus.AbstractEventBus = function() {
};

randori.bus.AbstractEventBus.className = "randori.bus.AbstractEventBus";

randori.bus.AbstractEventBus.getClassDependencies = function(t) {
	var p;
	return [];
};

randori.bus.AbstractEventBus.injectionPoints = function(t) {
	return [];
};

// ====================================================
// randori.timer.Timer
// ====================================================

if (typeof randori == "undefined")
	var randori = {};
if (typeof randori.timer == "undefined")
	randori.timer = {};

randori.timer.Timer = function(delay, repeatCount) {
	this._repeatCount = 0;
	this._currentCount = 0;
	this.intervalID = 0;
	this.timerComplete = null;
	this.timerTick = null;
	this._delay = 0;
	if (arguments.length < 2) {
		repeatCount = 0;
	}
	this._delay = delay;
	this._repeatCount = repeatCount;
	this._currentCount = 0;
	this.intervalID = -1;
	this.timerTick = new randori.signal.SimpleSignal();
	this.timerComplete = new randori.signal.SimpleSignal();
};

randori.timer.Timer.prototype.get_delay = function() {
	return this._delay;
};

randori.timer.Timer.prototype.get_repeatCount = function() {
	return this._repeatCount;
};

randori.timer.Timer.prototype.get_currentCount = function() {
	return this._currentCount;
};

randori.timer.Timer.prototype.onTimerTick = function() {
	this._currentCount++;
	this.timerTick.dispatch(this, this._currentCount);
	if (this._currentCount == this._repeatCount) {
		this.timerComplete.dispatch(this);
	}
	this.stop();
};

randori.timer.Timer.prototype.start = function() {
	if (this.intervalID != -1) {
		this.stop();
	}
	this.intervalID = setInterval($createStaticDelegate(this, this.onTimerTick), this.get_delay());
};

randori.timer.Timer.prototype.stop = function() {
	if (this.intervalID != -1) {
		clearInterval(this.intervalID);
	}
	this.intervalID = -1;
};

randori.timer.Timer.prototype.reset = function() {
	this._currentCount = 0;
	this.stop();
};

randori.timer.Timer.className = "randori.timer.Timer";

randori.timer.Timer.getClassDependencies = function(t) {
	var p;
	p = [];
	p.push('randori.signal.SimpleSignal');
	return p;
};

randori.timer.Timer.injectionPoints = function(t) {
	var p;
	switch (t) {
		case 0:
			p = [];
			p.push({n:'delay', t:'int'});
			p.push({n:'repeatCount', t:'int'});
			break;
		default:
			p = [];
			break;
	}
	return p;
};


// ====================================================
// randori.startup.RandoriModule
// ====================================================

if (typeof randori == "undefined")
	var randori = {};
if (typeof randori.startup == "undefined")
	randori.startup = {};

randori.startup.RandoriModule = function() {
guice.GuiceModule.call(this);
};

randori.startup.RandoriModule.prototype.configure = function(binder) {
	binder.bind(randori.styles.StyleExtensionMap).inScope(1).to(randori.styles.StyleExtensionMap);
	binder.bind(randori.i18n.AbstractTranslator).to(randori.i18n.NoOpTranslator);
};

$inherit(randori.startup.RandoriModule, guice.GuiceModule);

randori.startup.RandoriModule.className = "randori.startup.RandoriModule";

randori.startup.RandoriModule.getClassDependencies = function(t) {
	var p;
	p = [];
	p.push('randori.i18n.NoOpTranslator');
	p.push('randori.i18n.AbstractTranslator');
	p.push('randori.styles.StyleExtensionMap');
	return p;
};

randori.startup.RandoriModule.injectionPoints = function(t) {
	var p;
	switch (t) {
		case 1:
			p = guice.GuiceModule.injectionPoints(t);
			break;
		case 2:
			p = guice.GuiceModule.injectionPoints(t);
			break;
		case 3:
			p = guice.GuiceModule.injectionPoints(t);
			break;
		default:
			p = [];
			break;
	}
	return p;
};


// ====================================================
// randori.service.AbstractService
// ====================================================

if (typeof randori == "undefined")
	var randori = {};
if (typeof randori.service == "undefined")
	randori.service = {};

randori.service.AbstractService = function(xmlHttpRequest) {
	this.xmlHttpRequest = xmlHttpRequest;
};

randori.service.AbstractService.prototype.createUri = function(protocol, host, port, path) {
	var uri = "";
	if ((protocol != null) && (host != null)) {
		uri += (protocol + ":\/\/" + host);
	}
	if (port != null) {
		uri = uri + ":" + port;
	}
	uri = uri + "\/" + path;
	return uri;
};

randori.service.AbstractService.prototype.modifyHeaders = function(request) {
};

randori.service.AbstractService.prototype.sendRequest = function(verb, uri) {
	var promise = new randori.async.Promise();
	this.xmlHttpRequest.open(verb, uri, true);
	this.xmlHttpRequest.onreadystatechange = function(evt) {
		var request = evt.target;
		if (request.readyState == 4) {
			if (request.status == 200) {
				promise.resolve(request.responseText);
			} else {
				promise.reject(request.statusText);
			}
		}
	};
	this.modifyHeaders(this.xmlHttpRequest);
	this.xmlHttpRequest.send();
	return promise;
};

randori.service.AbstractService.prototype.sendRequestFull = function(verb, protocol, host, port, path) {
	return this.sendRequest(verb, this.createUri(protocol, host, port, path));
};

randori.service.AbstractService.className = "randori.service.AbstractService";

randori.service.AbstractService.getClassDependencies = function(t) {
	var p;
	p = [];
	p.push('randori.async.Promise');
	return p;
};

randori.service.AbstractService.injectionPoints = function(t) {
	var p;
	switch (t) {
		case 0:
			p = [];
			p.push({n:'xmlHttpRequest', t:'XMLHttpRequest'});
			break;
		default:
			p = [];
			break;
	}
	return p;
};


// ====================================================
// randori.content.ContentLoader
// ====================================================

if (typeof randori == "undefined")
	var randori = {};
if (typeof randori.content == "undefined")
	randori.content = {};

randori.content.ContentLoader = function(contentCache, xmlHttpRequest) {
	this.contentCache = contentCache;
	randori.service.AbstractService.call(this, xmlHttpRequest);
};

randori.content.ContentLoader.prototype.synchronousFragmentLoad = function(fragmentURL) {
	var cachedContent = this.contentCache.getCachedHtmlForUri(fragmentURL);
	if (cachedContent != null) {
		return cachedContent;
	}
	this.xmlHttpRequest.open("GET", fragmentURL, false);
	this.xmlHttpRequest.send();
	if (this.xmlHttpRequest.status == 404) {
		throw new Error("Cannot continue, missing required content " + fragmentURL);
	}
	return this.xmlHttpRequest.responseText;
};

randori.content.ContentLoader.prototype.asynchronousLoad = function(fragmentURL) {
	return this.sendRequest("GET", fragmentURL).then(function(value) {
		return value;
	});
};

$inherit(randori.content.ContentLoader, randori.service.AbstractService);

randori.content.ContentLoader.className = "randori.content.ContentLoader";

randori.content.ContentLoader.getClassDependencies = function(t) {
	var p;
	return [];
};

randori.content.ContentLoader.injectionPoints = function(t) {
	var p;
	switch (t) {
		case 0:
			p = [];
			p.push({n:'contentCache', t:'randori.content.ContentCache'});
			p.push({n:'xmlHttpRequest', t:'XMLHttpRequest'});
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


// ====================================================
// randori.dom.ExternalBehaviorFactory
// ====================================================

if (typeof randori == "undefined")
	var randori = {};
if (typeof randori.dom == "undefined")
	randori.dom = {};

randori.dom.ExternalBehaviorFactory = function() {
};

randori.dom.ExternalBehaviorFactory.prototype.createExternalBehavior = function(element, behaviorClassName, constructorFunction) {
	return null;
};

randori.dom.ExternalBehaviorFactory.className = "randori.dom.ExternalBehaviorFactory";

randori.dom.ExternalBehaviorFactory.getClassDependencies = function(t) {
	var p;
	return [];
};

randori.dom.ExternalBehaviorFactory.injectionPoints = function(t) {
	return [];
};

// ====================================================
// randori.styles.StyleExtensionMapEntry
// ====================================================

if (typeof randori == "undefined")
	var randori = {};
if (typeof randori.styles == "undefined")
	randori.styles = {};

randori.styles.StyleExtensionMapEntry = function() {
	this.hashMap = null;
	this.hashMap = {};
};

randori.styles.StyleExtensionMapEntry.prototype.addExtensionType = function(extensionType, extensionValue) {
	this.hashMap[extensionType] = extensionValue;
};

randori.styles.StyleExtensionMapEntry.prototype.hasExtensionType = function(extensionType) {
	return (this.hashMap[extensionType] != null);
};

randori.styles.StyleExtensionMapEntry.prototype.getExtensionValue = function(extensionType) {
	return this.hashMap[extensionType];
};

randori.styles.StyleExtensionMapEntry.prototype.clone = function() {
	var newEntry = new randori.styles.StyleExtensionMapEntry();
	this.mergeTo(newEntry);
	return newEntry;
};

randori.styles.StyleExtensionMapEntry.prototype.mergeTo = function(entry) {
	for (var extensionType in this.hashMap) {
		entry.addExtensionType(extensionType, this.hashMap[extensionType]);
	}
};

randori.styles.StyleExtensionMapEntry.className = "randori.styles.StyleExtensionMapEntry";

randori.styles.StyleExtensionMapEntry.getClassDependencies = function(t) {
	var p;
	return [];
};

randori.styles.StyleExtensionMapEntry.injectionPoints = function(t) {
	return [];
};

// ====================================================
// randori.dom.DomExtensionFactory
// ====================================================

if (typeof randori == "undefined")
	var randori = {};
if (typeof randori.dom == "undefined")
	randori.dom = {};

randori.dom.DomExtensionFactory = function(contentLoader, classResolver, externalBehaviorFactory) {
	this.contentLoader = contentLoader;
	this.classResolver = classResolver;
	this.externalBehaviorFactory = externalBehaviorFactory;
};

randori.dom.DomExtensionFactory.prototype.buildBehavior = function(classBuilder, element, behaviorClassName) {
	var behavior = null;
	var resolution = this.classResolver.resolveClassName(behaviorClassName);
	if (resolution.get_builtIn()) {
		behavior = this.externalBehaviorFactory.createExternalBehavior(element, behaviorClassName, resolution.get_type());
	} else {
		behavior = classBuilder.buildClass(behaviorClassName);
		behavior.provideDecoratedElement(element);
	}
	return behavior;
};

randori.dom.DomExtensionFactory.prototype.buildNewContent = function(element, fragmentURL) {
	jQuery(element).append(this.contentLoader.synchronousFragmentLoad(fragmentURL));
};

randori.dom.DomExtensionFactory.prototype.buildChildClassBuilder = function(classBuilder, element, contextClassName) {
	var module = classBuilder.buildClass(contextClassName);
	var injector = classBuilder.buildClass("guice.ChildInjector");
	var guiceJs = new guice.GuiceJs();
	guiceJs.configureInjector(injector, module);
	return injector.getInstance(guice.InjectionClassBuilder);
};

randori.dom.DomExtensionFactory.className = "randori.dom.DomExtensionFactory";

randori.dom.DomExtensionFactory.getClassDependencies = function(t) {
	var p;
	p = [];
	p.push('guice.InjectionClassBuilder');
	p.push('guice.GuiceJs');
	return p;
};

randori.dom.DomExtensionFactory.injectionPoints = function(t) {
	var p;
	switch (t) {
		case 0:
			p = [];
			p.push({n:'contentLoader', t:'randori.content.ContentLoader'});
			p.push({n:'classResolver', t:'guice.resolver.ClassResolver'});
			p.push({n:'externalBehaviorFactory', t:'randori.dom.ExternalBehaviorFactory'});
			break;
		default:
			p = [];
			break;
	}
	return p;
};


// ====================================================
// randori.content.ContentResolver
// ====================================================

if (typeof randori == "undefined")
	var randori = {};
if (typeof randori.content == "undefined")
	randori.content = {};

randori.content.ContentResolver = function(map) {
	this.map = map;
};

randori.content.ContentResolver.prototype.resolveContent = function(element) {
	var content = element.getAttribute("data-content");
	element.removeAttribute("data-content");
	if (content == null) {
	}
};

randori.content.ContentResolver.className = "randori.content.ContentResolver";

randori.content.ContentResolver.getClassDependencies = function(t) {
	var p;
	return [];
};

randori.content.ContentResolver.injectionPoints = function(t) {
	var p;
	switch (t) {
		case 0:
			p = [];
			p.push({n:'map', t:'randori.styles.StyleExtensionMap'});
			break;
		default:
			p = [];
			break;
	}
	return p;
};


// ====================================================
// randori.content.ContentParser
// ====================================================

if (typeof randori == "undefined")
	var randori = {};
if (typeof randori.content == "undefined")
	randori.content = {};

randori.content.ContentParser = function() {
};

randori.content.ContentParser.prototype.parse = function(content) {
	var bodyRegex = new RegExp("(<\/?)body", "gi");
	var sanitizedContent = content.replace(bodyRegex, "$1div");
	return sanitizedContent;
};

randori.content.ContentParser.className = "randori.content.ContentParser";

randori.content.ContentParser.getClassDependencies = function(t) {
	var p;
	return [];
};

randori.content.ContentParser.injectionPoints = function(t) {
	return [];
};

// ====================================================
// randori.template.TemplateBuilder
// ====================================================

if (typeof randori == "undefined")
	var randori = {};
if (typeof randori.template == "undefined")
	randori.template = {};

randori.template.TemplateBuilder = function() {
this.validTemplate = false;
this.templateAsString = null;
};

randori.template.TemplateBuilder.prototype.captureAndEmptyTemplateContents = function(rootTemplateNode) {
	this.templateAsString = rootTemplateNode.html();
	rootTemplateNode.empty();
	this.validTemplate = true;
};

randori.template.TemplateBuilder.prototype.returnFieldName = function(token) {
	return token.substr(1, token.length - 2);
};

randori.template.TemplateBuilder.prototype.renderTemplateClone = function(data) {
	var token;
	var field;
	var dereferencedValue;
	var keyRegex = new RegExp("\\{[\\w\\W]+?\\}", "g");
	var foundKeys = this.templateAsString.match(keyRegex);
	var output = this.templateAsString;
	if (foundKeys != null) {
		for (var j = 0; j < foundKeys.length; j++) {
			token = foundKeys[j];
			field = this.returnFieldName(token);
			if (field.indexOf(".") != -1) {
				dereferencedValue = this.resolveComplexName(data, field);
			} else if (field != "*") {
				dereferencedValue = data[field];
			} else {
				dereferencedValue = data;
			}
			output = output.replace(token, dereferencedValue);
		}
	}
	var fragmentJquery = jQuery("<div><\/div>");
	fragmentJquery.append(output);
	return fragmentJquery;
};

randori.template.TemplateBuilder.prototype.resolveComplexName = function(root, name) {
	var nextLevel = root;
	var path = name.split(".");
	for (var i = 0; i < path.length; i++) {
		nextLevel = nextLevel[path[i]];
		if (nextLevel == null) {
			return null;
		}
	}
	return nextLevel;
};

randori.template.TemplateBuilder.className = "randori.template.TemplateBuilder";

randori.template.TemplateBuilder.getClassDependencies = function(t) {
	var p;
	return [];
};

randori.template.TemplateBuilder.injectionPoints = function(t) {
	return [];
};

// ====================================================
// randori.behaviors.AbstractBehavior
// ====================================================

if (typeof randori == "undefined")
	var randori = {};
if (typeof randori.behaviors == "undefined")
	randori.behaviors = {};

randori.behaviors.AbstractBehavior = function() {
	this.viewElementIDMap = null;
	this.viableInjectionPoints = null;
	this.injectedPoints = null;
	this.decoratedElement = null;
	this.decoratedNode = null;
	this.injectedPoints = [];
};

randori.behaviors.AbstractBehavior.prototype.hide = function() {
	this.decoratedNode.hide();
};

randori.behaviors.AbstractBehavior.prototype.show = function() {
	this.decoratedNode.show();
};

randori.behaviors.AbstractBehavior.prototype.getViewElementByID = function(id) {
	return this.viewElementIDMap[id];
};

randori.behaviors.AbstractBehavior.prototype.onPreRegister = function() {
	if (this.viableInjectionPoints == null) {
		this.viableInjectionPoints = this.getBehaviorInjectionPoints();
	}
};

randori.behaviors.AbstractBehavior.prototype.onRegister = function() {
};

randori.behaviors.AbstractBehavior.prototype.onDeregister = function() {
};

randori.behaviors.AbstractBehavior.prototype.injectPotentialNode = function(id, node) {
	if ((id != null) && (this.viableInjectionPoints != null) && (this.viableInjectionPoints[id] != null)) {
		delete this.viableInjectionPoints[id];
		var instance = this;
		instance[id] = node;
		this.injectedPoints.push(id);
	}
};

randori.behaviors.AbstractBehavior.prototype.provideDecoratedElement = function(element) {
	this.decoratedElement = element;
	this.decoratedNode = jQuery(this.decoratedElement);
	this.onPreRegister();
};

randori.behaviors.AbstractBehavior.prototype.verifyAndRegister = function() {
	for (var id in this.viableInjectionPoints) {
		if (this.viableInjectionPoints[id] == "req") {
			var instance = this;
			var typeDefinition = new guice.reflection.TypeDefinition(instance.constructor);
			alert(typeDefinition.getClassName() + " requires a [View] element with the id of " + id + " but it could not be found");
			throw new Error(typeDefinition.getClassName() + " requires a [View] element with the id of " + id + " but it could not be found");
			return;
		}
		delete this.viableInjectionPoints[id];
	}
	this.viableInjectionPoints = null;
	this.onRegister();
};

randori.behaviors.AbstractBehavior.prototype.removeAndCleanup = function() {
	var instance = this;
	var injection;
	this.onDeregister();
	for (var i = 0; i < this.injectedPoints.length; i++) {
		injection = instance[this.injectedPoints[i]];
		if ((injection != null) && (injection.removeAndCleanup != null)) {
			injection.removeAndCleanup();
		}
	}
	this.injectedPoints = [];
};

randori.behaviors.AbstractBehavior.prototype.getBehaviorInjectionPoints = function() {
	var instance = this;
	var map = {};
	var typeDefinition = new guice.reflection.TypeDefinition(instance.constructor);
	var viewPoints = typeDefinition.getViewFields();
	for (var i = 0; i < viewPoints.length; i++) {
		if (viewPoints[i].r == 0) {
			map[viewPoints[i].n] = "opt";
		} else {
			map[viewPoints[i].n] = "req";
		}
	}
	return map;
};

randori.behaviors.AbstractBehavior.className = "randori.behaviors.AbstractBehavior";

randori.behaviors.AbstractBehavior.getClassDependencies = function(t) {
	var p;
	p = [];
	p.push('guice.reflection.TypeDefinition');
	return p;
};

randori.behaviors.AbstractBehavior.injectionPoints = function(t) {
	return [];
};

// ====================================================
// randori.behaviors.AbstractRenderer
// ====================================================

if (typeof randori == "undefined")
	var randori = {};
if (typeof randori.behaviors == "undefined")
	randori.behaviors = {};

randori.behaviors.AbstractRenderer = function() {
randori.behaviors.AbstractBehavior.call(this);
};

randori.behaviors.AbstractRenderer.prototype.setData = function(data) {
};

$inherit(randori.behaviors.AbstractRenderer, randori.behaviors.AbstractBehavior);

randori.behaviors.AbstractRenderer.className = "randori.behaviors.AbstractRenderer";

randori.behaviors.AbstractRenderer.getClassDependencies = function(t) {
	var p;
	return [];
};

randori.behaviors.AbstractRenderer.injectionPoints = function(t) {
	var p;
	switch (t) {
		case 1:
			p = randori.behaviors.AbstractBehavior.injectionPoints(t);
			break;
		case 2:
			p = randori.behaviors.AbstractBehavior.injectionPoints(t);
			break;
		case 3:
			p = randori.behaviors.AbstractBehavior.injectionPoints(t);
			break;
		default:
			p = [];
			break;
	}
	return p;
};


// ====================================================
// randori.behaviors.list.DataRendererProvider
// ====================================================

if (typeof randori == "undefined")
	var randori = {};
if (typeof randori.behaviors == "undefined")
	randori.behaviors = {};
if (typeof randori.behaviors.list == "undefined")
	randori.behaviors.list = {};

randori.behaviors.list.DataRendererProvider = function(data) {
	this.data = data;
	randori.behaviors.AbstractBehavior.call(this);
};

randori.behaviors.list.DataRendererProvider.prototype.onDeregister = function() {
	this.data = null;
};

randori.behaviors.list.DataRendererProvider.prototype.injectPotentialNode = function(id, node) {
	var behavior = node;
	if (behavior.setData != null) {
		behavior.setData(this.data);
	}
};

$inherit(randori.behaviors.list.DataRendererProvider, randori.behaviors.AbstractBehavior);

randori.behaviors.list.DataRendererProvider.className = "randori.behaviors.list.DataRendererProvider";

randori.behaviors.list.DataRendererProvider.getClassDependencies = function(t) {
	var p;
	return [];
};

randori.behaviors.list.DataRendererProvider.injectionPoints = function(t) {
	var p;
	switch (t) {
		case 0:
			p = [];
			p.push({n:'data', t:'Object'});
			break;
		case 1:
			p = randori.behaviors.AbstractBehavior.injectionPoints(t);
			break;
		case 2:
			p = randori.behaviors.AbstractBehavior.injectionPoints(t);
			break;
		case 3:
			p = randori.behaviors.AbstractBehavior.injectionPoints(t);
			break;
		default:
			p = [];
			break;
	}
	return p;
};


// ====================================================
// randori.behaviors.SimpleList
// ====================================================

if (typeof randori == "undefined")
	var randori = {};
if (typeof randori.behaviors == "undefined")
	randori.behaviors = {};

randori.behaviors.SimpleList = function(domWalker) {
	this.template = null;
	this.templateBuilder = null;
	this._renderFunction = null;
	this._data = null;
	randori.behaviors.AbstractBehavior.call(this);
	this.domWalker = domWalker;
};

randori.behaviors.SimpleList.prototype.get_renderFunction = function() {
	return this._renderFunction;
};

randori.behaviors.SimpleList.prototype.set_renderFunction = function(value) {
	this._renderFunction = value;
};

randori.behaviors.SimpleList.prototype.get_data = function() {
	return this._data;
};

randori.behaviors.SimpleList.prototype.set_data = function(value) {
	this._data = value;
	this.renderList();
};

randori.behaviors.SimpleList.prototype.renderList = function() {
	var row;
	var div = jQuery("<div><\/div>");
	if ((this.get_data() == null) || (this.get_data().length == 0)) {
		this.showNoResults();
		return;
	}
	if (!this.templateBuilder.validTemplate && this.get_renderFunction() == null)
		return;
	if (this.templateBuilder.validTemplate) {
		for (var i = 0; i < this.get_data().length; i++) {
			var drp = new randori.behaviors.list.DataRendererProvider(this.get_data()[i]);
			row = this.templateBuilder.renderTemplateClone(this.get_data()[i]).children();
			this.domWalker.walkDomFragment(row[0], drp);
			row.addClass("randoriListItem");
			div.append(row);
		}
	} else if (this.get_renderFunction() != null) {
		for (var j = 0; j < this.get_data().length; j++) {
			row = this.get_renderFunction()(j, this.get_data()[j]);
			this.domWalker.walkDomFragment(row[0], this);
			row.addClass("randoriListItem");
			div.append(row);
		}
	}
	this.decoratedNode.empty();
	this.decoratedNode.append(div.children());
};

randori.behaviors.SimpleList.prototype.onPreRegister = function() {
	randori.behaviors.AbstractBehavior.prototype.onPreRegister.call(this);
	this.templateBuilder.captureAndEmptyTemplateContents(this.decoratedNode);
};

randori.behaviors.SimpleList.prototype.onRegister = function() {
	this.renderList();
};

randori.behaviors.SimpleList.prototype.onDeregister = function() {
	this.set_data(null);
	this.decoratedNode.empty();
};

randori.behaviors.SimpleList.prototype.showLoading = function() {
	var output = "<div style=\"height:100%; width:100%;\"><div style=\"text-align:center;width:100%;top:60%;position:absolute\">Loading...<\/div><\/div>";
	this.decoratedNode.html(output);
};

randori.behaviors.SimpleList.prototype.showNoResults = function(visible) {
	if (arguments.length < 1) {
		visible = true;
	}
	var output = "<div style=\"height:100%; width:100%;\"><div style=\"text-align:center;width:100%;top:60%;position:absolute\">No Items Found<\/div><\/div>";
	this.decoratedNode.html(output);
};

$inherit(randori.behaviors.SimpleList, randori.behaviors.AbstractBehavior);

randori.behaviors.SimpleList.className = "randori.behaviors.SimpleList";

randori.behaviors.SimpleList.getClassDependencies = function(t) {
	var p;
	p = [];
	p.push('randori.behaviors.list.DataRendererProvider');
	return p;
};

randori.behaviors.SimpleList.injectionPoints = function(t) {
	var p;
	switch (t) {
		case 0:
			p = [];
			p.push({n:'domWalker', t:'randori.dom.DomWalker'});
			break;
		case 1:
			p = randori.behaviors.AbstractBehavior.injectionPoints(t);
			p.push({n:'templateBuilder', t:'randori.template.TemplateBuilder', r:0, v:null});
			break;
		case 2:
			p = randori.behaviors.AbstractBehavior.injectionPoints(t);
			break;
		case 3:
			p = randori.behaviors.AbstractBehavior.injectionPoints(t);
			p.push({n:'template', r:0});
			break;
		default:
			p = [];
			break;
	}
	return p;
};


// ====================================================
// randori.behaviors.List
// ====================================================

if (typeof randori == "undefined")
	var randori = {};
if (typeof randori.behaviors == "undefined")
	randori.behaviors = {};

randori.behaviors.List = function(walker) {
	this.listChanged = null;
	this._selectedItem = null;
	this._selectedIndex = 0;
	randori.behaviors.SimpleList.call(this, walker);
	this.listChanged = new randori.signal.SimpleSignal();
};

randori.behaviors.List.prototype.get_selectedItem = function() {
	return this._data[this._selectedIndex];
};

randori.behaviors.List.prototype.set_selectedItem = function(value) {
	if (this._data == null) {
		return;
	}
	for (var i = 0; i < this._data.length; i++) {
		if (value == this._data[i]) {
			this.set_selectedIndex(i);
			break;
		}
	}
};

randori.behaviors.List.prototype.get_selectedIndex = function() {
	return this._selectedIndex;
};

randori.behaviors.List.prototype.set_selectedIndex = function(value) {
	this._selectedIndex = value;
	this.decoratedNode.children().removeClass("selected");
	if (this._data && this._data.length >= value) {
		if (value > -1 && value < this.decoratedNode.children().length) {
			this.decoratedNode.children().eq(value).addClass("selected");
			this.listChanged.dispatch(value, this.get_data()[value]);
		}
	}
};

randori.behaviors.List.prototype.onRegister = function() {
	randori.behaviors.SimpleList.prototype.onRegister.call(this);
	this.decoratedNode.delegate(".randoriListItem", "click", $createStaticDelegate(this, this.onItemClick));
};

randori.behaviors.List.prototype.renderList = function() {
	randori.behaviors.SimpleList.prototype.renderList.call(this);
	this.set_selectedIndex(0);
};

randori.behaviors.List.prototype.onItemClick = function(e) {
	var targetJq = jQuery(e.currentTarget);
	var index = targetJq.index();
	this.set_selectedIndex(index);
};

$inherit(randori.behaviors.List, randori.behaviors.SimpleList);

randori.behaviors.List.className = "randori.behaviors.List";

randori.behaviors.List.getClassDependencies = function(t) {
	var p;
	p = [];
	p.push('randori.signal.SimpleSignal');
	return p;
};

randori.behaviors.List.injectionPoints = function(t) {
	var p;
	switch (t) {
		case 0:
			p = [];
			p.push({n:'walker', t:'randori.dom.DomWalker'});
			break;
		case 1:
			p = randori.behaviors.SimpleList.injectionPoints(t);
			break;
		case 2:
			p = randori.behaviors.SimpleList.injectionPoints(t);
			break;
		case 3:
			p = randori.behaviors.SimpleList.injectionPoints(t);
			break;
		default:
			p = [];
			break;
	}
	return p;
};


// ====================================================
// randori.behaviors.viewStack.MediatorCapturer
// ====================================================

if (typeof randori == "undefined")
	var randori = {};
if (typeof randori.behaviors == "undefined")
	randori.behaviors = {};
if (typeof randori.behaviors.viewStack == "undefined")
	randori.behaviors.viewStack = {};

randori.behaviors.viewStack.MediatorCapturer = function() {
	this._mediator = null;
	randori.behaviors.AbstractBehavior.call(this);
};

randori.behaviors.viewStack.MediatorCapturer.prototype.get_mediator = function() {
	return this._mediator;
};

randori.behaviors.viewStack.MediatorCapturer.prototype.onDeregister = function() {
	this._mediator = null;
};

randori.behaviors.viewStack.MediatorCapturer.prototype.injectPotentialNode = function(id, node) {
	var behavior = node;
	if (this._mediator == null && behavior.setViewData != null) {
		this._mediator = behavior;
	}
};

$inherit(randori.behaviors.viewStack.MediatorCapturer, randori.behaviors.AbstractBehavior);

randori.behaviors.viewStack.MediatorCapturer.className = "randori.behaviors.viewStack.MediatorCapturer";

randori.behaviors.viewStack.MediatorCapturer.getClassDependencies = function(t) {
	var p;
	return [];
};

randori.behaviors.viewStack.MediatorCapturer.injectionPoints = function(t) {
	var p;
	switch (t) {
		case 1:
			p = randori.behaviors.AbstractBehavior.injectionPoints(t);
			break;
		case 2:
			p = randori.behaviors.AbstractBehavior.injectionPoints(t);
			break;
		case 3:
			p = randori.behaviors.AbstractBehavior.injectionPoints(t);
			break;
		default:
			p = [];
			break;
	}
	return p;
};


// ====================================================
// randori.behaviors.AbstractMediator
// ====================================================

if (typeof randori == "undefined")
	var randori = {};
if (typeof randori.behaviors == "undefined")
	randori.behaviors = {};

randori.behaviors.AbstractMediator = function() {
randori.behaviors.AbstractBehavior.call(this);
};

randori.behaviors.AbstractMediator.prototype.setViewData = function(viewData) {
};

$inherit(randori.behaviors.AbstractMediator, randori.behaviors.AbstractBehavior);

randori.behaviors.AbstractMediator.className = "randori.behaviors.AbstractMediator";

randori.behaviors.AbstractMediator.getClassDependencies = function(t) {
	var p;
	return [];
};

randori.behaviors.AbstractMediator.injectionPoints = function(t) {
	var p;
	switch (t) {
		case 1:
			p = randori.behaviors.AbstractBehavior.injectionPoints(t);
			break;
		case 2:
			p = randori.behaviors.AbstractBehavior.injectionPoints(t);
			break;
		case 3:
			p = randori.behaviors.AbstractBehavior.injectionPoints(t);
			break;
		default:
			p = [];
			break;
	}
	return p;
};


// ====================================================
// randori.behaviors.template.TemplateRenderer
// ====================================================

if (typeof randori == "undefined")
	var randori = {};
if (typeof randori.behaviors == "undefined")
	randori.behaviors = {};
if (typeof randori.behaviors.template == "undefined")
	randori.behaviors.template = {};

randori.behaviors.template.TemplateRenderer = function(domWalker, templateBuilder) {
	this._data = null;
	randori.behaviors.AbstractBehavior.call(this);
	this.domWalker = domWalker;
	this.templateBuilder = templateBuilder;
};

randori.behaviors.template.TemplateRenderer.prototype.get_data = function() {
	return this._data;
};

randori.behaviors.template.TemplateRenderer.prototype.set_data = function(value) {
	if (value == this._data)
		return;
	this._data = value;
	this.renderMessage();
};

randori.behaviors.template.TemplateRenderer.prototype.onPreRegister = function() {
	randori.behaviors.AbstractBehavior.prototype.onPreRegister.call(this);
	this.templateBuilder.captureAndEmptyTemplateContents(this.decoratedNode);
};

randori.behaviors.template.TemplateRenderer.prototype.renderMessage = function() {
	var newNode = this.templateBuilder.renderTemplateClone(this.get_data());
	this.decoratedNode.html(newNode.html());
	this.domWalker.walkDomChildren(this.decoratedElement, this);
};

randori.behaviors.template.TemplateRenderer.prototype.onDeregister = function() {
	this.set_data(null);
	this.decoratedNode.empty();
};

$inherit(randori.behaviors.template.TemplateRenderer, randori.behaviors.AbstractBehavior);

randori.behaviors.template.TemplateRenderer.className = "randori.behaviors.template.TemplateRenderer";

randori.behaviors.template.TemplateRenderer.getClassDependencies = function(t) {
	var p;
	return [];
};

randori.behaviors.template.TemplateRenderer.injectionPoints = function(t) {
	var p;
	switch (t) {
		case 0:
			p = [];
			p.push({n:'domWalker', t:'randori.dom.DomWalker'});
			p.push({n:'templateBuilder', t:'randori.template.TemplateBuilder'});
			break;
		case 1:
			p = randori.behaviors.AbstractBehavior.injectionPoints(t);
			break;
		case 2:
			p = randori.behaviors.AbstractBehavior.injectionPoints(t);
			break;
		case 3:
			p = randori.behaviors.AbstractBehavior.injectionPoints(t);
			break;
		default:
			p = [];
			break;
	}
	return p;
};


// ====================================================
// randori.behaviors.ViewStack
// ====================================================

if (typeof randori == "undefined")
	var randori = {};
if (typeof randori.behaviors == "undefined")
	randori.behaviors = {};

randori.behaviors.ViewStack = function(contentLoader, contentParser, domWalker, viewChangeAnimator) {
	this._currentView = null;
	this.viewFragmentStack = null;
	this.mediators = null;
	randori.behaviors.AbstractBehavior.call(this);
	this.contentLoader = contentLoader;
	this.contentParser = contentParser;
	this.viewChangeAnimator = viewChangeAnimator;
	this.domWalker = domWalker;
	this.viewFragmentStack = [];
};

randori.behaviors.ViewStack.prototype.get_currentViewUrl = function() {
	return (this._currentView != null) ? this._currentView.data("url") : null;
};

randori.behaviors.ViewStack.prototype.hasView = function(url) {
	var fragment = null;
	var allFragments = this.decoratedNode.children();
	if (allFragments.length > 0) {
		fragment = allFragments.find("[data-url=\'" + url + "\']");
	}
	return ((fragment != null) && fragment.length > 0);
};

randori.behaviors.ViewStack.prototype.pushView = function(url) {
	var promise;
	var stack = this;
	var div = document.createElement('div');
	var fragment = jQuery(div);
	fragment.hide();
	fragment.css("width", "100%");
	fragment.css("height", "100%");
	fragment.css("position", "absolute");
	fragment.css("top", "0");
	fragment.css("left", "0");
	fragment.data("url", url);
	var that = this;
	promise = this.contentLoader.asynchronousLoad(url).then(function(result) {
		var content = that.contentParser.parse(result);
		fragment.html(content);
		fragment.attr("data-url", url);
		that.decoratedNode.append(div);
		var mediatorCapturer = new randori.behaviors.viewStack.MediatorCapturer();
		that.domWalker.walkDomFragment(div, mediatorCapturer);
		that.viewFragmentStack.push(fragment);
		var mediator = mediatorCapturer.get_mediator();
		that.mediators[url] = mediator;
		that.showView(that._currentView, fragment);
		return mediator;
	});
	return promise;
};

randori.behaviors.ViewStack.prototype.popView = function() {
	var oldView = this.viewFragmentStack.pop();
	if (oldView != null) {
		oldView.remove();
		var url = oldView.data("url");
		var mediator = this.mediators[url];
		if (mediator != null) {
			mediator.removeAndCleanup();
			delete this.mediators[url];
		}
	}
	if (this.viewFragmentStack.length > 0) {
		this._currentView = this.viewFragmentStack[this.viewFragmentStack.length - 1];
		if (this._currentView != null) {
			this._currentView.show();
		}
	} else {
		this._currentView = null;
	}
};

randori.behaviors.ViewStack.prototype.selectView = function(url) {
	if (this.get_currentViewUrl() != url) {
		var fragment = this.decoratedNode.children().filter("[data-url=" + url + "]");
		if (fragment == null) {
			throw new Error("Unknown View");
		}
		fragment.detach();
		this.decoratedNode.append(fragment);
		this.showView(this._currentView, fragment);
		this._currentView = fragment;
	}
};

randori.behaviors.ViewStack.prototype.showView = function(oldFragment, newFragment) {
	if (oldFragment != null) {
		oldFragment.hide();
	}
	if (newFragment != null) {
		newFragment.show();
	}
};

randori.behaviors.ViewStack.prototype.transitionViews = function(arrivingView, departingView, data) {
	if (arguments.length < 3) {
		data = null;
	}
	return null;
};

randori.behaviors.ViewStack.prototype.onRegister = function() {
	this.mediators = {};
	this.decoratedNode.empty();
};

randori.behaviors.ViewStack.prototype.onDeregister = function() {
	this.mediators = {};
	this.decoratedNode.empty();
};

$inherit(randori.behaviors.ViewStack, randori.behaviors.AbstractBehavior);

randori.behaviors.ViewStack.className = "randori.behaviors.ViewStack";

randori.behaviors.ViewStack.getClassDependencies = function(t) {
	var p;
	p = [];
	p.push('randori.behaviors.viewStack.MediatorCapturer');
	return p;
};

randori.behaviors.ViewStack.injectionPoints = function(t) {
	var p;
	switch (t) {
		case 0:
			p = [];
			p.push({n:'contentLoader', t:'randori.content.ContentLoader'});
			p.push({n:'contentParser', t:'randori.content.ContentParser'});
			p.push({n:'domWalker', t:'randori.dom.DomWalker'});
			p.push({n:'viewChangeAnimator', t:'randori.behaviors.viewStack.ViewChangeAnimator'});
			break;
		case 1:
			p = randori.behaviors.AbstractBehavior.injectionPoints(t);
			break;
		case 2:
			p = randori.behaviors.AbstractBehavior.injectionPoints(t);
			break;
		case 3:
			p = randori.behaviors.AbstractBehavior.injectionPoints(t);
			break;
		default:
			p = [];
			break;
	}
	return p;
};


// ====================================================
// randori.dom.ElementDescriptorFactory
// ====================================================

if (typeof randori == "undefined")
	var randori = {};
if (typeof randori.dom == "undefined")
	randori.dom = {};

randori.dom.ElementDescriptorFactory = function(styleExtensionManager) {
	this.styleExtensionManager = styleExtensionManager;
};

randori.dom.ElementDescriptorFactory.prototype.describeElement = function(element, possibleExtensions) {
	var entry = possibleExtensions.get(element);
	var descriptor = {context:element.getAttribute("data-context"), behavior:element.hasAttribute("data-mediator") ? element.getAttribute("data-mediator") : element.getAttribute("data-behavior"), fragment:element.getAttribute("data-fragment"), formatter:element.getAttribute("data-formatter"), validator:element.getAttribute("data-validator")};
	if (entry != null) {
		if (descriptor.context == null) {
			descriptor.context = entry.getExtensionValue("context");
		}
		if (descriptor.behavior == null) {
			descriptor.behavior = entry.hasExtensionType("mediator") ? entry.getExtensionValue("mediator") : entry.getExtensionValue("behavior");
		}
		if (descriptor.fragment == null) {
			descriptor.fragment = entry.getExtensionValue("fragment");
		}
		if (descriptor.formatter == null) {
			descriptor.formatter = entry.getExtensionValue("formatter");
		}
		if (descriptor.validator == null) {
			descriptor.validator = entry.getExtensionValue("validator");
		}
	}
	return descriptor;
};

randori.dom.ElementDescriptorFactory.className = "randori.dom.ElementDescriptorFactory";

randori.dom.ElementDescriptorFactory.getClassDependencies = function(t) {
	var p;
	return [];
};

randori.dom.ElementDescriptorFactory.injectionPoints = function(t) {
	var p;
	switch (t) {
		case 0:
			p = [];
			p.push({n:'styleExtensionManager', t:'randori.styles.StyleExtensionManager'});
			break;
		default:
			p = [];
			break;
	}
	return p;
};


// ====================================================
// randori.behaviors.viewStack.ViewChangeAnimator
// ====================================================

if (typeof randori == "undefined")
	var randori = {};
if (typeof randori.behaviors == "undefined")
	randori.behaviors = {};
if (typeof randori.behaviors.viewStack == "undefined")
	randori.behaviors.viewStack = {};

randori.behaviors.viewStack.ViewChangeAnimator = function() {
};

randori.behaviors.viewStack.ViewChangeAnimator.className = "randori.behaviors.viewStack.ViewChangeAnimator";

randori.behaviors.viewStack.ViewChangeAnimator.getClassDependencies = function(t) {
	var p;
	return [];
};

randori.behaviors.viewStack.ViewChangeAnimator.injectionPoints = function(t) {
	return [];
};

// ====================================================
// randori.service.ServiceConfig
// ====================================================

if (typeof randori == "undefined")
	var randori = {};
if (typeof randori.service == "undefined")
	randori.service = {};

randori.service.ServiceConfig = function() {
	this.protocol = null;
	this.host = null;
	this.port = null;
	this.debugMode = true;
	
};

randori.service.ServiceConfig.className = "randori.service.ServiceConfig";

randori.service.ServiceConfig.getClassDependencies = function(t) {
	var p;
	return [];
};

randori.service.ServiceConfig.injectionPoints = function(t) {
	return [];
};

// ====================================================
// randori.signal.SimpleSignal
// ====================================================

if (typeof randori == "undefined")
	var randori = {};
if (typeof randori.signal == "undefined")
	randori.signal = {};

randori.signal.SimpleSignal = function() {
	this.once = null;
	this.permanent = null;
	this.permanent = [];
	this.once = [];
};

randori.signal.SimpleSignal.prototype.findIndex = function(listener, array) {
	var index = -1;
	var length;
	var obj1;
	var obj2;
	obj1 = listener;
	length = array.length;
	for (var i = 0; i < array.length; i++) {
		obj2 = array[i];
		if (obj1 === obj2) {
			index = i;
			break;
		}
	}
	return -1;
};

randori.signal.SimpleSignal.prototype.add = function(listener) {
	this.permanent.push($createStaticDelegate(this, listener));
};

randori.signal.SimpleSignal.prototype.addOnce = function(listener) {
	this.once.push($createStaticDelegate(this, listener));
};

randori.signal.SimpleSignal.prototype.remove = function(listener) {
	var index;
	index = this.findIndex($createStaticDelegate(this, listener), this.once);
	if (index != -1) {
		this.once.splice(index, 1);
	} else {
		index = this.findIndex($createStaticDelegate(this, listener), this.permanent);
		if (index != -1) {
			this.permanent.splice(index, 1);
		}
	}
};

randori.signal.SimpleSignal.prototype.has = function(listener) {
	var index;
	index = this.findIndex($createStaticDelegate(this, listener), this.once);
	if (index != -1) {
		return true;
	}
	index = this.findIndex($createStaticDelegate(this, listener), this.permanent);
	if (index != -1) {
		return true;
	}
	return false;
};

randori.signal.SimpleSignal.prototype.dispatch = function(args) {
	var listener;
	while (this.once.length > 0) {
		listener = this.once.pop();
		listener.apply(this, arguments);
	}
	for (var i = 0; i < this.permanent.length; i++) {
		listener = this.permanent[i];
		listener.apply(this, arguments);
	}
};

randori.signal.SimpleSignal.className = "randori.signal.SimpleSignal";

randori.signal.SimpleSignal.getClassDependencies = function(t) {
	var p;
	return [];
};

randori.signal.SimpleSignal.injectionPoints = function(t) {
	return [];
};

// ====================================================
// randori.i18n.AbstractTranslator
// ====================================================

if (typeof randori == "undefined")
	var randori = {};
if (typeof randori.i18n == "undefined")
	randori.i18n = {};

randori.i18n.AbstractTranslator = function(translationResult) {
	this.translationResult = translationResult;
};

randori.i18n.AbstractTranslator.prototype.synchronousTranslate = function(domain, keys) {
	return null;
};

randori.i18n.AbstractTranslator.prototype.translate = function(domain, keys) {
};

randori.i18n.AbstractTranslator.className = "randori.i18n.AbstractTranslator";

randori.i18n.AbstractTranslator.getClassDependencies = function(t) {
	var p;
	return [];
};

randori.i18n.AbstractTranslator.injectionPoints = function(t) {
	var p;
	switch (t) {
		case 0:
			p = [];
			p.push({n:'translationResult', t:'randori.signal.SimpleSignal'});
			break;
		default:
			p = [];
			break;
	}
	return p;
};


// ====================================================
// randori.i18n.NoOpTranslator
// ====================================================

if (typeof randori == "undefined")
	var randori = {};
if (typeof randori.i18n == "undefined")
	randori.i18n = {};

randori.i18n.NoOpTranslator = function(translationResult) {
	randori.i18n.AbstractTranslator.call(this, translationResult);
};

randori.i18n.NoOpTranslator.prototype.synchronousTranslate = function(domain, keys) {
	if (console != null) {
		console.log("Requested to translate: " + domain + " " + keys);
	}
	return [];
};

randori.i18n.NoOpTranslator.prototype.translate = function(domain, keys) {
	if (console != null) {
		console.log("Requested to translate: " + domain + " " + keys);
	}
};

$inherit(randori.i18n.NoOpTranslator, randori.i18n.AbstractTranslator);

randori.i18n.NoOpTranslator.className = "randori.i18n.NoOpTranslator";

randori.i18n.NoOpTranslator.getClassDependencies = function(t) {
	var p;
	return [];
};

randori.i18n.NoOpTranslator.injectionPoints = function(t) {
	var p;
	switch (t) {
		case 0:
			p = [];
			p.push({n:'translationResult', t:'randori.signal.SimpleSignal'});
			break;
		case 1:
			p = randori.i18n.AbstractTranslator.injectionPoints(t);
			break;
		case 2:
			p = randori.i18n.AbstractTranslator.injectionPoints(t);
			break;
		case 3:
			p = randori.i18n.AbstractTranslator.injectionPoints(t);
			break;
		default:
			p = [];
			break;
	}
	return p;
};


// ====================================================
// randori.i18n.PropertyFileTranslator
// ====================================================

if (typeof randori == "undefined")
	var randori = {};
if (typeof randori.i18n == "undefined")
	randori.i18n = {};

randori.i18n.PropertyFileTranslator = function(translationResult, url, forceReload) {
	this.keyValuePairs = null;
	this.fileLoaded = false;
	if (arguments.length < 3) {
		forceReload = false;
	}
	randori.i18n.AbstractTranslator.call(this, translationResult);
	this.url = url;
	this.forceReload = forceReload;
	this.keyValuePairs = {};
};

randori.i18n.PropertyFileTranslator.prototype.synchronousTranslate = function(domain, keys) {
	if (!this.fileLoaded) {
		this.makeSynchronousRequest(this.url);
	}
	return this.provideTranslations(domain, keys);
};

randori.i18n.PropertyFileTranslator.prototype.translate = function(domain, keys) {
	if (!this.fileLoaded) {
		this.makeAsynchronousRequest(this.url, function() {
			var translations = this.provideTranslations(domain, keys);
			this.translationResult.dispatch(domain, translations);
		});
	} else {
		var translations = this.provideTranslations(domain, keys);
		this.translationResult.dispatch(domain, translations);
	}
};

randori.i18n.PropertyFileTranslator.prototype.provideTranslations = function(domain, keys) {
	var translations = [];
	var translation;
	for (var i = 0; i < keys.length; i++) {
		translation = {};
		translation.key = keys[i];
		translation.value = this.keyValuePairs[keys[i]];
		translations.push(translation);
	}
	return translations;
};

randori.i18n.PropertyFileTranslator.prototype.makeSynchronousRequest = function(url) {
	var request = new XMLHttpRequest();
	if (this.forceReload) {
		url = url + "?rnd=" + Math.random();
	}
	request.open("GET", url, false);
	request.send();
	if (request.status == 404) {
		alert("Required Content " + url + " cannot be loaded.");
		throw new Error("Cannot continue, missing required property file " + url);
	}
	this.parseResult(request.responseText);
};

randori.i18n.PropertyFileTranslator.prototype.makeAsynchronousRequest = function(url, fileLoaded) {
	var request = new XMLHttpRequest();
	if (this.forceReload) {
		url = url + "?rnd=" + Math.random();
	}
	request.open("GET", url, true);
	request.onreadystatechange = function(evt) {
		if (request.readyState == 4 && request.status == 200) {
			this.parseResult(request.responseText);
			fileLoaded();
		} else if (request.readyState >= 3 && request.status == 404) {
			alert("Required Content " + url + " cannot be loaded.");
			throw new Error("Cannot continue, missing required property file " + url);
		}
	};
	request.send();
};

randori.i18n.PropertyFileTranslator.prototype.parseResult = function(responseText) {
	var eachLine = new RegExp("[\\w\\W]+?[\\n\\r]+", "g");
	var eachLineResult = responseText.match(eachLine);
	this.fileLoaded = true;
	if (eachLineResult != null) {
		for (var i = 0; i < eachLineResult.length; i++) {
			this.parseLine(eachLineResult[i]);
		}
	}
};

randori.i18n.PropertyFileTranslator.prototype.parseLine = function(line) {
	if (line.length == 0) {
		return;
	}
	var isComment = new RegExp("^[#!]");
	var isCommentResult = line.match(isComment);
	if (isCommentResult != null) {
		return;
	}
	var tokenize = new RegExp("^(\\w+)\\s?=\\s?([\\w\\W]+?)[\\n\\r]+");
	var tokenizeResult = line.match(tokenize);
	var key;
	var strValue;
	var value;
	if (tokenizeResult != null && tokenizeResult.length == 3) {
		key = tokenizeResult[1];
		value = tokenizeResult[2];
		strValue = value;
		if (strValue.indexOf(",") != -1) {
			value = strValue.split(",");
		}
		this.keyValuePairs[key] = value;
	}
};

$inherit(randori.i18n.PropertyFileTranslator, randori.i18n.AbstractTranslator);

randori.i18n.PropertyFileTranslator.className = "randori.i18n.PropertyFileTranslator";

randori.i18n.PropertyFileTranslator.getClassDependencies = function(t) {
	var p;
	return [];
};

randori.i18n.PropertyFileTranslator.injectionPoints = function(t) {
	var p;
	switch (t) {
		case 0:
			p = [];
			p.push({n:'translationResult', t:'randori.signal.SimpleSignal'});
			p.push({n:'url', t:'String'});
			p.push({n:'forceReload', t:'Boolean'});
			break;
		case 1:
			p = randori.i18n.AbstractTranslator.injectionPoints(t);
			break;
		case 2:
			p = randori.i18n.AbstractTranslator.injectionPoints(t);
			break;
		case 3:
			p = randori.i18n.AbstractTranslator.injectionPoints(t);
			break;
		default:
			p = [];
			break;
	}
	return p;
};


// ====================================================
// randori.i18n.LocalizationProvider
// ====================================================

if (typeof randori == "undefined")
	var randori = {};
if (typeof randori.i18n == "undefined")
	randori.i18n = {};

randori.i18n.LocalizationProvider = function(translator) {
	this.internationalKey = new RegExp("\\[(labels|messages|reference)\\.\\w+\\]", "g");
	this.timer = null;
	this.pendingTranslations = null;
	this.translator = translator;
	this.timer = new randori.timer.Timer(10, 1);
	this.timer.timerComplete.add($createStaticDelegate(this, this.sendTranslationRequest));
	this.pendingTranslations = {};
};

randori.i18n.LocalizationProvider.prototype.getElementLocalizationComponents = function(textNode) {
	var textContent = textNode.nodeValue;
	var i18nResult = textContent.match(this.internationalKey);
	return i18nResult;
};

randori.i18n.LocalizationProvider.prototype.translateKeysSynchronously = function(domain, keys) {
	return this.translator.synchronousTranslate(domain, keys);
};

randori.i18n.LocalizationProvider.prototype.investigateTextNode = function(textNode) {
	var result = this.getElementLocalizationComponents(textNode);
	if (result != null) {
		for (var i = 0; i < result.length; i++) {
			this.requestTranslation(result[i], textNode);
		}
		this.scheduleTranslation();
	}
};

randori.i18n.LocalizationProvider.prototype.requestTranslation = function(expression, textNode) {
	var pendingTranslation = this.pendingTranslations[expression];
	if (pendingTranslation == null) {
		pendingTranslation = [];
		this.pendingTranslations[expression] = pendingTranslation;
	}
	pendingTranslation.push(textNode);
};

randori.i18n.LocalizationProvider.prototype.scheduleTranslation = function() {
	this.timer.reset();
	this.timer.start();
};

randori.i18n.LocalizationProvider.prototype.sendTranslationRequest = function(timer) {
	var domainLabels = {};
	var keyValuePair = new RegExp("\\[(labels|messages|reference)\\.(\\w+)\\]");
	var result;
	var domain;
	var key;
	for (var expression in this.pendingTranslations) {
		result = expression.match(keyValuePair);
		domain = result[1];
		key = result[2];
		if (domainLabels[domain] == null) {
			domainLabels[domain] = [];
		}
		domainLabels[domain].push(key);
	}
	for (var domainEntry in domainLabels) {
		this.translator.translate(domainEntry, domainLabels[domainEntry]);
	}
};

randori.i18n.LocalizationProvider.prototype.provideTranslation = function(domain, translations) {
	var expression;
	var nodes;
	for (var i = translations.length - 1; i >= 0; i--) {
		expression = "[" + domain + "." + translations[i].key + "]";
		nodes = this.pendingTranslations[expression];
		if (nodes != null) {
			for (var j = 0; j < nodes.length; j++) {
				this.applyTranslation(nodes[j], expression, translations[i].value);
			}
		}
		delete this.pendingTranslations[expression];
	}
};

randori.i18n.LocalizationProvider.prototype.applyTranslation = function(node, expression, translation) {
	var currentValue = node.nodeValue;
	var newValue = currentValue.replace(expression, translation);
	node.nodeValue = newValue;
};

randori.i18n.LocalizationProvider.className = "randori.i18n.LocalizationProvider";

randori.i18n.LocalizationProvider.getClassDependencies = function(t) {
	var p;
	p = [];
	p.push('randori.timer.Timer');
	return p;
};

randori.i18n.LocalizationProvider.injectionPoints = function(t) {
	var p;
	switch (t) {
		case 0:
			p = [];
			p.push({n:'translator', t:'randori.i18n.AbstractTranslator'});
			break;
		default:
			p = [];
			break;
	}
	return p;
};


// ====================================================
// randori.startup.RandoriBootstrap
// ====================================================

if (typeof randori == "undefined")
	var randori = {};
if (typeof randori.startup == "undefined")
	randori.startup = {};

randori.startup.RandoriBootstrap = function(rootNode) {
	this.rootNode = rootNode;
};

randori.startup.RandoriBootstrap.prototype.launch = function() {
	var guiceJs = new guice.GuiceJs();
	var injector = guiceJs.createInjector(new randori.startup.RandoriModule());
	var domWalker = injector.getInstance(randori.dom.DomWalker);
	domWalker.walkDomFragment(this.rootNode);
};

randori.startup.RandoriBootstrap.className = "randori.startup.RandoriBootstrap";

randori.startup.RandoriBootstrap.getClassDependencies = function(t) {
	var p;
	p = [];
	p.push('randori.startup.RandoriModule');
	p.push('randori.dom.DomWalker');
	p.push('guice.GuiceJs');
	return p;
};

randori.startup.RandoriBootstrap.injectionPoints = function(t) {
	var p;
	switch (t) {
		case 0:
			p = [];
			p.push({n:'rootNode', t:'Node'});
			break;
		default:
			p = [];
			break;
	}
	return p;
};


// ====================================================
// randori.dom.DomWalker
// ====================================================

if (typeof randori == "undefined")
	var randori = {};
if (typeof randori.dom == "undefined")
	randori.dom = {};

randori.dom.DomWalker = function(domExtensionFactory, classBuilder, elementDescriptorFactory, styleExtensionManager, localizationProvider) {
	this.extensionsToBeApplied = null;
	this.entryElement = null;
	this.domExtensionFactory = domExtensionFactory;
	this.classBuilder = classBuilder;
	this.elementDescriptorFactory = elementDescriptorFactory;
	this.styleExtensionManager = styleExtensionManager;
	this.localizationProvider = localizationProvider;
};

randori.dom.DomWalker.prototype.investigateLinkElement = function(element) {
	if (this.styleExtensionManager.parsingNeeded(element)) {
		this.styleExtensionManager.parseAndReleaseLinkElement(element);
		this.extensionsToBeApplied = this.styleExtensionManager.getExtensionsForFragment(this.entryElement);
	}
};

randori.dom.DomWalker.prototype.investigateDomElement = function(element, parentBehavior) {
	var currentBehavior = parentBehavior;
	var domWalker = this;
	var id = element.getAttribute("id");
	if (id != null) {
		element.removeAttribute("id");
	}
	var elementDescriptor = this.elementDescriptorFactory.describeElement(element, this.extensionsToBeApplied);
	if (elementDescriptor.context != null) {
		this.classBuilder = this.domExtensionFactory.buildChildClassBuilder(this.classBuilder, element, elementDescriptor.context);
		domWalker = this.classBuilder.buildClass("randori.dom.DomWalker");
	}
	if (elementDescriptor.behavior != null) {
		currentBehavior = this.domExtensionFactory.buildBehavior(this.classBuilder, element, elementDescriptor.behavior);
		if (parentBehavior != null) {
			parentBehavior.injectPotentialNode(id, currentBehavior);
		}
	} else {
		if (id != null && currentBehavior != null) {
			currentBehavior.injectPotentialNode(id, jQuery(element));
		}
	}
	if (elementDescriptor.fragment != null) {
		this.domExtensionFactory.buildNewContent(element, elementDescriptor.fragment);
		domWalker = this.classBuilder.buildClass("randori.dom.DomWalker");
	}
	domWalker.walkChildren(element, currentBehavior);
	if (currentBehavior != null && currentBehavior != parentBehavior) {
		currentBehavior.verifyAndRegister();
	}
};

randori.dom.DomWalker.prototype.investigateNode = function(node, parentBehavior) {
	if (node.nodeType == 1) {
		if (this.extensionsToBeApplied == null) {
			this.entryElement = node;
			this.extensionsToBeApplied = this.styleExtensionManager.getExtensionsForFragment(this.entryElement);
		}
		if (node.nodeName == "META") {
			return;
		}
		if (node.nodeName == "LINK") {
			this.investigateLinkElement(node);
		} else {
			this.investigateDomElement(node, parentBehavior);
		}
	} else if (node.nodeType == 3) {
		this.localizationProvider.investigateTextNode(node);
	} else {
		this.walkChildren(node, parentBehavior);
	}
};

randori.dom.DomWalker.prototype.walkChildren = function(parentNode, parentBehavior) {
	if (arguments.length < 2) {
		parentBehavior = null;
	}
	var node = parentNode.firstChild;
	if (this.extensionsToBeApplied == null && (parentNode.nodeType == 1)) {
		this.entryElement = parentNode;
		this.extensionsToBeApplied = this.styleExtensionManager.getExtensionsForFragment(this.entryElement);
	}
	while (node != null) {
		this.investigateNode(node, parentBehavior);
		node = node.nextSibling;
	}
};

randori.dom.DomWalker.prototype.walkDomChildren = function(parentNode, parentBehavior) {
	if (arguments.length < 2) {
		parentBehavior = null;
	}
	this.walkChildren(parentNode, parentBehavior);
	this.extensionsToBeApplied = null;
};

randori.dom.DomWalker.prototype.walkDomFragment = function(node, parentBehavior) {
	if (arguments.length < 2) {
		parentBehavior = null;
	}
	this.investigateNode(node, parentBehavior);
	this.extensionsToBeApplied = null;
};

randori.dom.DomWalker.className = "randori.dom.DomWalker";

randori.dom.DomWalker.getClassDependencies = function(t) {
	var p;
	return [];
};

randori.dom.DomWalker.injectionPoints = function(t) {
	var p;
	switch (t) {
		case 0:
			p = [];
			p.push({n:'domExtensionFactory', t:'randori.dom.DomExtensionFactory'});
			p.push({n:'classBuilder', t:'guice.InjectionClassBuilder'});
			p.push({n:'elementDescriptorFactory', t:'randori.dom.ElementDescriptorFactory'});
			p.push({n:'styleExtensionManager', t:'randori.styles.StyleExtensionManager'});
			p.push({n:'localizationProvider', t:'randori.i18n.LocalizationProvider'});
			break;
		default:
			p = [];
			break;
	}
	return p;
};


// ====================================================
// randori.styles.StyleExtensionMap
// ====================================================

if (typeof randori == "undefined")
	var randori = {};
if (typeof randori.styles == "undefined")
	randori.styles = {};

randori.styles.StyleExtensionMap = function() {
	this.hashMap = null;
	this.hashMap = {};
};

randori.styles.StyleExtensionMap.prototype.addCSSEntry = function(cssSelector, extensionType, extensionValue) {
	var attributes = this.hashMap[cssSelector];
	if (attributes == null) {
		attributes = new randori.styles.StyleExtensionMapEntry();
		this.hashMap[cssSelector] = attributes;
	}
	attributes.addExtensionType(extensionType, extensionValue);
};

randori.styles.StyleExtensionMap.prototype.hasBehaviorEntry = function(cssSelector) {
	return (this.hashMap[cssSelector] != null);
};

randori.styles.StyleExtensionMap.prototype.getExtensionEntry = function(cssSelector) {
	return this.hashMap[cssSelector];
};

randori.styles.StyleExtensionMap.prototype.getAllRandoriSelectorEntries = function() {
	var allEntries = [];
	for (var cssSelector in this.hashMap) {
		allEntries.push(cssSelector);
	}
	return allEntries;
};

randori.styles.StyleExtensionMap.className = "randori.styles.StyleExtensionMap";

randori.styles.StyleExtensionMap.getClassDependencies = function(t) {
	var p;
	p = [];
	p.push('randori.styles.StyleExtensionMapEntry');
	return p;
};

randori.styles.StyleExtensionMap.injectionPoints = function(t) {
	return [];
};

// ====================================================
// randori.styles.StyleExtensionManager
// ====================================================

if (typeof randori == "undefined")
	var randori = {};
if (typeof randori.styles == "undefined")
	randori.styles = {};

randori.styles.StyleExtensionManager = function(map) {
	this.map = map;
};

randori.styles.StyleExtensionManager.prototype.findChildNodesForSelector = function(elements, selectorArray) {
	var selector = selectorArray.shift();
	var newElements = [];
	var element;
	var nodes;
	var j = 0;
	if (selector.substr(0, 1) == ".") {
		var className = selector.substring(1);
		while (elements.length > 0) {
			element = elements.pop();
			if (element.classList.contains(className)) {
				newElements.push(element);
			}
			nodes = element.getElementsByClassName(className);
			for (j = 0; j < nodes.length; j++) {
				newElements.push(nodes[j]);
			}
		}
	} else {
		while (elements.length > 0) {
			element = elements.pop();
			nodes = element.getElementsByTagName(selector);
			for (j = 0; j < nodes.length; j++) {
				newElements.push(nodes[j]);
			}
		}
	}
	if (selectorArray.length > 0) {
		newElements = this.findChildNodesForSelector(newElements, selectorArray);
	}
	return newElements;
};

randori.styles.StyleExtensionManager.prototype.findChildNodesForCompoundSelector = function(element, selector) {
	var selectors = selector.split(" ");
	var ar = [];
	ar.push(element);
	var elements = this.findChildNodesForSelector(ar, selectors);
	return elements;
};

randori.styles.StyleExtensionManager.prototype.getExtensionsForFragment = function(element) {
	var hashmap = new randori.data.HashMap();
	var allEntries = this.map.getAllRandoriSelectorEntries();
	for (var i = 0; i < allEntries.length; i++) {
		var implementingNodes = this.findChildNodesForCompoundSelector(element, allEntries[i]);
		var extensionEntry;
		for (var j = 0; j < implementingNodes.length; j++) {
			var implementingElement = implementingNodes[j];
			var value = hashmap.get(implementingElement);
			if (value == null) {
				extensionEntry = this.map.getExtensionEntry(allEntries[i]);
				hashmap.put(implementingElement, extensionEntry.clone());
			} else {
				extensionEntry = this.map.getExtensionEntry(allEntries[i]);
				extensionEntry.mergeTo(value);
			}
		}
	}
	return hashmap;
};

randori.styles.StyleExtensionManager.prototype.parsingNeeded = function(link) {
	return (link.rel == "stylesheet\/randori");
};

randori.styles.StyleExtensionManager.prototype.resetLinkAndReturnURL = function(link) {
	link.rel = "stylesheet";
	return link.href;
};

randori.styles.StyleExtensionManager.prototype.resolveSheet = function(url) {
	var sheetRequest = new XMLHttpRequest();
	var behaviorSheet = "";
	var prefix;
	sheetRequest.open("GET", url, false);
	sheetRequest.send();
	if (sheetRequest.status == 404) {
		throw new Error("Cannot Find StyleSheet " + url);
	}
	var lastSlash = url.lastIndexOf("\/");
	prefix = url.substring(0, lastSlash);
	this.parseAndPersistBehaviors(sheetRequest.responseText);
};

randori.styles.StyleExtensionManager.prototype.parseAndPersistBehaviors = function(sheet) {
	var classSelector;
	var randoriVendorItemsResult;
	var randoriVendorItemInfoResult;
	var CSSClassSelectorNameResult;
	var allClassSelectors = new RegExp("^[\\w\\W]*?\\}", "gm");
	const RANDORI_VENDOR_ITEM_EXPRESSION = "\\s?-randori-([\\w\\W]+?)\\s?:\\s?[\"\']?([\\w\\W]+?)[\"\']?;";
	var anyVendorItems = new RegExp(RANDORI_VENDOR_ITEM_EXPRESSION, "g");
	var eachVendorItem = new RegExp(RANDORI_VENDOR_ITEM_EXPRESSION);
	var classSelectorName = new RegExp("^(.+?)\\s+?{", "m");
	var CSSClassSelectorName;
	var randoriVendorItemStr;
	var selectors = sheet.match(allClassSelectors);
	if (selectors != null) {
		for (var i = 0; i < selectors.length; i++) {
			classSelector = selectors[i];
			randoriVendorItemsResult = classSelector.match(anyVendorItems);
			if (randoriVendorItemsResult != null) {
				CSSClassSelectorNameResult = classSelector.match(classSelectorName);
				CSSClassSelectorName = CSSClassSelectorNameResult[1];
				for (var j = 0; j < randoriVendorItemsResult.length; j++) {
					randoriVendorItemStr = randoriVendorItemsResult[j];
					randoriVendorItemInfoResult = randoriVendorItemStr.match(eachVendorItem);
					this.map.addCSSEntry(CSSClassSelectorName, randoriVendorItemInfoResult[1], randoriVendorItemInfoResult[2]);
					if (console != null) {
						console.log(CSSClassSelectorName + " specifies a " + randoriVendorItemInfoResult[1] + " implemented by class " + randoriVendorItemInfoResult[2]);
					}
				}
			}
		}
	}
};

randori.styles.StyleExtensionManager.prototype.parseAndReleaseLinkElement = function(element) {
	this.resolveSheet(this.resetLinkAndReturnURL(element));
};

randori.styles.StyleExtensionManager.className = "randori.styles.StyleExtensionManager";

randori.styles.StyleExtensionManager.getClassDependencies = function(t) {
	var p;
	p = [];
	p.push('randori.data.HashMap');
	return p;
};

randori.styles.StyleExtensionManager.injectionPoints = function(t) {
	var p;
	switch (t) {
		case 0:
			p = [];
			p.push({n:'map', t:'randori.styles.StyleExtensionMap'});
			break;
		default:
			p = [];
			break;
	}
	return p;
};


// ====================================================
// randori.content.ContentCache
// ====================================================

if (typeof randori == "undefined")
	var randori = {};
if (typeof randori.content == "undefined")
	randori.content = {};

randori.content.ContentCache = function() {
};

randori.content.ContentCache.htmlMergedFiles ={};

randori.content.ContentCache.prototype.hasCachedFile = function(key) {
	return (randori.content.ContentCache.htmlMergedFiles[key] != null);
};

randori.content.ContentCache.prototype.getCachedFileList = function() {
	var contentList = [];
	for (var key in randori.content.ContentCache.htmlMergedFiles) {
		contentList.push(key);
	}
	return contentList;
};

randori.content.ContentCache.prototype.getCachedHtmlForUri = function(key) {
	if (randori.content.ContentCache.htmlMergedFiles[key] != null) {
		return randori.content.ContentCache.htmlMergedFiles[key];
	}
	return null;
};

randori.content.ContentCache.className = "randori.content.ContentCache";

randori.content.ContentCache.getClassDependencies = function(t) {
	var p;
	return [];
};

randori.content.ContentCache.injectionPoints = function(t) {
	return [];
};

// ====================================================
// randori.utilities.BehaviorDecorator
// ====================================================

if (typeof randori == "undefined")
	var randori = {};
if (typeof randori.utilities == "undefined")
	randori.utilities = {};

randori.utilities.BehaviorDecorator = function() {
};

randori.utilities.BehaviorDecorator.prototype.decorateObject = function(behavior) {
	var futureBehavior = behavior;
	futureBehavior.verifyAndRegister = verifyAndRegister;
	futureBehavior.provideDecoratedElement = provideDecoratedElement;
	futureBehavior.injectPotentialNode = injectPotentialNode;
	futureBehavior.removeAndCleanup = removeAndCleanup;
};

randori.utilities.BehaviorDecorator.verifyAndRegister = function() {
};

randori.utilities.BehaviorDecorator.removeAndCleanup = function() {
};

randori.utilities.BehaviorDecorator.provideDecoratedElement = function(element) {
};

randori.utilities.BehaviorDecorator.injectPotentialNode = function(id, node) {
};

randori.utilities.BehaviorDecorator.className = "randori.utilities.BehaviorDecorator";

randori.utilities.BehaviorDecorator.getClassDependencies = function(t) {
	var p;
	return [];
};

randori.utilities.BehaviorDecorator.injectionPoints = function(t) {
	return [];
};

// ====================================================
// randori.data.HashMap
// ====================================================

if (typeof randori == "undefined")
	var randori = {};
if (typeof randori.data == "undefined")
	randori.data = {};

randori.data.HashMap = function() {
	this.entries = null;
	this.entries = {};
};

randori.data.HashMap.prototype.getEntry = function(key) {
	var keyAsString = key;
	var entry = this.entries[keyAsString];
	var returnEntry = null;
	if (entry != undefined) {
		if (entry instanceof Array) {
			for (var i = 0; i < entry.length; i++) {
				if (entry[i].key == key) {
					returnEntry = entry[i];
					break;
				}
			}
		} else if (entry.key == key) {
			returnEntry = entry;
		}
	}
	return returnEntry;
};

randori.data.HashMap.prototype.get = function(key) {
	var entry = this.getEntry(key);
	return entry != null ? entry.value : null;
};

randori.data.HashMap.prototype.put = function(key, value) {
	var keyAsString = key;
	var entryLocation = this.entries[keyAsString];
	if (entryLocation == null) {
		this.entries[keyAsString] = {key:key, value:value};
	} else {
		var entry = this.getEntry(key);
		if (entry != undefined) {
			entry.value = value;
		} else if (entryLocation instanceof Array) {
			entryLocation.push({key:key, value:value});
		} else {
			var ar = [];
			ar[0] = entryLocation;
			ar[1] = {key:key, value:value};
			this.entries[keyAsString] = ar;
		}
	}
};

randori.data.HashMap.className = "randori.data.HashMap";

randori.data.HashMap.getClassDependencies = function(t) {
	var p;
	return [];
};

randori.data.HashMap.injectionPoints = function(t) {
	return [];
};
