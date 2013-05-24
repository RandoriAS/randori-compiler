/** Compiled by the Randori compiler v0.2.2 on Sun Apr 07 17:27:53 EDT 2013 */


// ====================================================
// guice.utilities.GlobalUtilities
// ====================================================



$inherit = function(ce, ce2) {
if (typeof (Object.getOwnPropertyNames) == 'undefined') {

	for (var p in ce2.prototype)
		if (typeof (ce.prototype[p]) == 'undefined' || ce.prototype[p] == Object.prototype[p])
			ce.prototype[p] = ce2.prototype[p];
	for (var p in ce2)
		if (typeof (ce[p]) == 'undefined')
			ce[p] = ce2[p];
	ce.$baseCtor = ce2;

} else {

	var props = Object.getOwnPropertyNames(ce2.prototype);
	for (var i = 0; i < props.length; i++)
		if (typeof (Object.getOwnPropertyDescriptor(ce.prototype, props[i])) == 'undefined')
			Object.defineProperty(ce.prototype, props[i], Object.getOwnPropertyDescriptor(ce2.prototype, props[i]));

	for (var p in ce2)
		if (typeof (ce[p]) == 'undefined')
			ce[p] = ce2[p];
	ce.$baseCtor = ce2;

}

};

$createStaticDelegate = function(scope, func) {
	if (scope == null || func == null)
    return func;
var delegate = function () {
    return func.apply(scope, arguments);
};
delegate.func = func;
delegate.scope = scope;

return delegate;

	return null;
};


// ====================================================
// guice.binding.BindingFactory
// ====================================================

if (typeof guice == "undefined")
	var guice = {};
if (typeof guice.binding == "undefined")
	guice.binding = {};

guice.binding.BindingFactory = function(binder, typeDefinition) {
	this.scope = 0;
	this.binder = binder;
	this.typeDefinition = typeDefinition;
};

guice.binding.BindingFactory.prototype.to = function(dependency) {
	var abstractBinding = this.withDecoration(new guice.binding.TypeBinding(this.typeDefinition, new guice.reflection.TypeDefinition(dependency)));
	this.binder.addBinding(abstractBinding);
	return abstractBinding;
};

guice.binding.BindingFactory.prototype.toInstance = function(instance) {
	var abstractBinding = this.withDecoration(new guice.binding.InstanceBinding(this.typeDefinition, instance));
	this.binder.addBinding(abstractBinding);
	return abstractBinding;
};

guice.binding.BindingFactory.prototype.toProvider = function(providerType) {
	var abstractBinding = this.withDecoration(new guice.binding.ProviderBinding(this.typeDefinition, new guice.reflection.TypeDefinition(providerType)));
	this.binder.addBinding(abstractBinding);
	return abstractBinding;
};

guice.binding.BindingFactory.prototype.inScope = function(scope) {
	this.scope = scope;
	return this;
};

guice.binding.BindingFactory.prototype.withDecoration = function(abstractBinding) {
	if (this.scope == 2) {
		abstractBinding = new guice.binding.decorator.ContextDecorator(abstractBinding);
	} else if (this.scope == 1) {
		abstractBinding = new guice.binding.decorator.SingletonDecorator(abstractBinding);
	}
	return abstractBinding;
};

guice.binding.BindingFactory.className = "guice.binding.BindingFactory";

guice.binding.BindingFactory.getClassDependencies = function(t) {
	var p;
	p = [];
	p.push('guice.binding.decorator.ContextDecorator');
	p.push('guice.reflection.TypeDefinition');
	p.push('guice.binding.InstanceBinding');
	p.push('guice.binding.TypeBinding');
	p.push('guice.binding.decorator.SingletonDecorator');
	p.push('guice.binding.ProviderBinding');
	return p;
};

guice.binding.BindingFactory.injectionPoints = function(t) {
	var p;
	switch (t) {
		case 0:
			p = [];
			p.push({n:'binder', t:'guice.binding.Binder'});
			p.push({n:'typeDefinition', t:'guice.reflection.TypeDefinition'});
			break;
		default:
			p = [];
			break;
	}
	return p;
};


// ====================================================
// guice.GuiceJs
// ====================================================

if (typeof guice == "undefined")
	var guice = {};

guice.GuiceJs = function(dynamicClassBaseUrl) {
	if (arguments.length < 1) {
		dynamicClassBaseUrl = "generated/";
	}
	this.dynamicClassBaseUrl = dynamicClassBaseUrl;
};

guice.GuiceJs.prototype.createInjector = function(module) {
	var hashMap = {};
	var binder = new guice.binding.Binder(hashMap);
	var loader = new guice.loader.SynchronousClassLoader(new XMLHttpRequest(), this.dynamicClassBaseUrl);
	var classResolver = new guice.resolver.ClassResolver(loader);
	if (module != null) {
		module.configure(binder);
	}
	var injector = new guice.Injector(binder, classResolver);
	binder.bind(guice.Injector).toInstance(injector);
	binder.bind(guice.resolver.ClassResolver).toInstance(classResolver);
	binder.bind(guice.loader.SynchronousClassLoader).toInstance(loader);
	return injector;
};

guice.GuiceJs.prototype.configureInjector = function(injector, module) {
	injector.configureBinder(module);
};

guice.GuiceJs.className = "guice.GuiceJs";

guice.GuiceJs.getClassDependencies = function(t) {
	var p;
	p = [];
	p.push('guice.binding.Binder');
	p.push('guice.resolver.ClassResolver');
	p.push('guice.loader.SynchronousClassLoader');
	p.push('guice.Injector');
	return p;
};

guice.GuiceJs.injectionPoints = function(t) {
	var p;
	switch (t) {
		case 0:
			p = [];
			p.push({n:'dynamicClassBaseUrl', t:'String'});
			break;
		default:
			p = [];
			break;
	}
	return p;
};


// ====================================================
// guice.resolver.ClassResolver
// ====================================================

if (typeof guice == "undefined")
	var guice = {};
if (typeof guice.resolver == "undefined")
	guice.resolver = {};

guice.resolver.ClassResolver = function(loader) {
	this.loader = loader;
};

guice.resolver.ClassResolver.prototype.resolveClassName = function(qualifiedClassName) {
	var type = this.findDefinition(qualifiedClassName);
	if (type == null) {
		var classDefinition = this.loader.loadClass(qualifiedClassName);
		this.resolveParentClassFromDefinition(qualifiedClassName, classDefinition);
		this.addDefinition(classDefinition);
		type = this.findDefinition(qualifiedClassName);
		if (type == null) {
			throw new Error(qualifiedClassName + " does not contain required injection information ");
		}
		var td = new guice.reflection.TypeDefinition(type);
		if (!td.get_builtIn()) {
			this.resolveClassDependencies(td);
		}
	}
	return new guice.reflection.TypeDefinition(type);
};

guice.resolver.ClassResolver.prototype.resolveClassDependencies = function(type) {
	var classDependencies = type.getClassDependencies();
	for (var i = 0; i < classDependencies.length; i++) {
		this.resolveClassName(classDependencies[i]);
	}
};

guice.resolver.ClassResolver.prototype.resolveParentClassFromDefinition = function(qualifiedClassName, classDefinition) {
	var inheritString = "\\$inherit\\(";
	inheritString += qualifiedClassName;
	inheritString += ",\\s*(.*?)\\)";
	var inheritResult = classDefinition.match(inheritString);
	if (inheritResult != null) {
		this.resolveClassName(inheritResult[1]);
	}
};

guice.resolver.ClassResolver.prototype.findDefinition = function(qualifiedClassName) {
	var nextLevel = window;
	var failed = false;
	var path = qualifiedClassName.split(".");
	for (var i = 0; i < path.length; i++) {
		nextLevel = nextLevel[path[i]];
		if (!nextLevel) {
			failed = true;
			break;
		}
	}
	if (failed) {
		return null;
	}
	return nextLevel;
};

guice.resolver.ClassResolver.prototype.addDefinition = function(definitionText) {
var globalEval = (function () {

    var isIndirectEvalGlobal = (function (original, Object) {
        try {
            // Does `Object` resolve to a local variable, or to a global, built-in `Object`,
            // reference to which we passed as a first argument?
            return (1, eval)('Object') === original;
        }
        catch (err) {
            // if indirect eval errors out (as allowed per ES3), then just bail out with `false`
            return false;
        }
    })(Object, 123);

    if (isIndirectEvalGlobal) {

        // if indirect eval executes code globally, use it
        return function (expression) {
            return (1, eval)(expression);
        };
    }
    else if (typeof window.execScript !== 'undefined') {

        // if `window.execScript exists`, use it
        return function (expression) {
            return window.execScript(expression);
        };
    }

    // otherwise, globalEval is `undefined` since nothing is returned
})();

globalEval(definitionText);

};

guice.resolver.ClassResolver.className = "guice.resolver.ClassResolver";

guice.resolver.ClassResolver.getClassDependencies = function(t) {
	var p;
	p = [];
	p.push('guice.reflection.TypeDefinition');
	return p;
};

guice.resolver.ClassResolver.injectionPoints = function(t) {
	var p;
	switch (t) {
		case 0:
			p = [];
			p.push({n:'loader', t:'guice.loader.SynchronousClassLoader'});
			break;
		default:
			p = [];
			break;
	}
	return p;
};


// ====================================================
// guice.loader.SynchronousClassLoader
// ====================================================

if (typeof guice == "undefined")
	var guice = {};
if (typeof guice.loader == "undefined")
	guice.loader = {};

guice.loader.SynchronousClassLoader = function(xmlHttpRequest, dynamicClassBaseUrl) {
	this.xmlHttpRequest = xmlHttpRequest;
	this.dynamicClassBaseUrl = dynamicClassBaseUrl;
};

guice.loader.SynchronousClassLoader.prototype.loadClass = function(qualifiedClassName) {
	var classNameRegex = new RegExp("\\.", "g");
	var potentialURL = qualifiedClassName.replace(classNameRegex, "\/");
	potentialURL = this.dynamicClassBaseUrl + potentialURL;
	potentialURL += ".js";
	this.xmlHttpRequest.open("GET", potentialURL, false);
	this.xmlHttpRequest.send();
	if (this.xmlHttpRequest.status == 404) {
		throw new Error("Cannot continue, missing required class " + qualifiedClassName);
	}
	return (this.xmlHttpRequest.responseText + "\n\/\/@ sourceURL=" + potentialURL);
};

guice.loader.SynchronousClassLoader.className = "guice.loader.SynchronousClassLoader";

guice.loader.SynchronousClassLoader.getClassDependencies = function(t) {
	var p;
	return [];
};

guice.loader.SynchronousClassLoader.injectionPoints = function(t) {
	var p;
	switch (t) {
		case 0:
			p = [];
			p.push({n:'xmlHttpRequest', t:'XMLHttpRequest'});
			p.push({n:'dynamicClassBaseUrl', t:'String'});
			break;
		default:
			p = [];
			break;
	}
	return p;
};


// ====================================================
// guice.Injector
// ====================================================

if (typeof guice == "undefined")
	var guice = {};

guice.Injector = function(binder, classResolver) {
	this.binder = binder;
	this.classResolver = classResolver;
};

guice.Injector.prototype.getInstance = function(dependency) {
	return this.resolveDependency(new guice.reflection.TypeDefinition(dependency));
};

guice.Injector.prototype.getInstanceByDefinition = function(dependencyTypeDefinition) {
	return this.resolveDependency(dependencyTypeDefinition);
};

guice.Injector.prototype.getBinding = function(typeDefinition) {
	return this.binder.getBinding(typeDefinition);
};

guice.Injector.prototype.buildClass = function(typeDefinition) {
	var instance;
	if (typeDefinition.get_builtIn()) {
		instance = typeDefinition.constructorApply(null);
	} else {
		var constructorPoints = typeDefinition.getConstructorParameters();
		instance = this.buildFromInjectionInfo(typeDefinition, constructorPoints);
		var fieldPoints = typeDefinition.getInjectionFields();
		this.injectMemberPropertiesFromInjectionInfo(instance, fieldPoints);
		var methodPoints = typeDefinition.getInjectionMethods();
		this.injectMembersMethodsFromInjectionInfo(instance, methodPoints);
	}
	return instance;
};

guice.Injector.prototype.injectMembers = function(instance) {
	var constructor = instance.constructor;
	var typeDefinition = new guice.reflection.TypeDefinition(constructor);
	var fieldPoints = typeDefinition.getInjectionFields();
	this.injectMemberPropertiesFromInjectionInfo(instance, fieldPoints);
	var methodPoints = typeDefinition.getInjectionMethods();
	this.injectMembersMethodsFromInjectionInfo(instance, methodPoints);
};

guice.Injector.prototype.buildFromInjectionInfo = function(dependencyTypeDefinition, constructorPoints) {
	var args = [];
	for (var i = 0; i < constructorPoints.length; i++) {
		args[i] = this.resolveDependency(this.classResolver.resolveClassName(constructorPoints[i].t));
	}
	return dependencyTypeDefinition.constructorApply(args);
};

guice.Injector.prototype.injectMemberPropertiesFromInjectionInfo = function(instance, fieldPoints) {
	for (var i = 0; i < fieldPoints.length; i++) {
		instance[fieldPoints[i].n] = this.resolveDependency(this.classResolver.resolveClassName(fieldPoints[i].t));
	}
};

guice.Injector.prototype.injectMembersMethodsFromInjectionInfo = function(instance, methodPoints) {
	for (var i = 0; i < methodPoints.length; i++) {
		var methodPoint = methodPoints[i];
		var args = [];
		for (var j = 0; j < methodPoint.p.length; j++) {
			var parameterPoint = methodPoint.p[j];
			args[j] = this.resolveDependency(this.classResolver.resolveClassName(parameterPoint.t));
		}
		var action = instance[methodPoints[i].n];
		action.apply(instance, args);
	}
};

guice.Injector.prototype.resolveDependency = function(typeDefinition) {
	var abstractBinding = null;
	if (!typeDefinition.get_builtIn()) {
		abstractBinding = this.getBinding(typeDefinition);
	}
	var instance;
	if (abstractBinding != null) {
		instance = abstractBinding.provide(this);
	} else {
		instance = this.buildClass(typeDefinition);
	}
	return instance;
};

guice.Injector.className = "guice.Injector";

guice.Injector.getClassDependencies = function(t) {
	var p;
	p = [];
	p.push('guice.reflection.TypeDefinition');
	return p;
};

guice.Injector.injectionPoints = function(t) {
	var p;
	switch (t) {
		case 0:
			p = [];
			p.push({n:'binder', t:'guice.binding.Binder'});
			p.push({n:'classResolver', t:'guice.resolver.ClassResolver'});
			break;
		default:
			p = [];
			break;
	}
	return p;
};


// ====================================================
// guice.ChildInjector
// ====================================================

if (typeof guice == "undefined")
	var guice = {};

guice.ChildInjector = function(binder, classResolver, parentInjector) {
	guice.Injector.call(this, binder, classResolver);
	this.parentInjector = parentInjector;
	binder.bind(guice.Injector).toInstance(this);
};

guice.ChildInjector.prototype.configureBinder = function(module) {
	if (module != null) {
		module.configure(this.binder);
	}
};

guice.ChildInjector.prototype.getBinding = function(typeDefinition) {
	var abstractBinding = this.binder.getBinding(typeDefinition);
	if (abstractBinding == null) {
		abstractBinding = this.parentInjector.getBinding(typeDefinition);
	}
	return abstractBinding;
};

$inherit(guice.ChildInjector, guice.Injector);

guice.ChildInjector.className = "guice.ChildInjector";

guice.ChildInjector.getClassDependencies = function(t) {
	var p;
	p = [];
	p.push('guice.Injector');
	return p;
};

guice.ChildInjector.injectionPoints = function(t) {
	var p;
	switch (t) {
		case 0:
			p = [];
			p.push({n:'binder', t:'guice.binding.Binder'});
			p.push({n:'classResolver', t:'guice.resolver.ClassResolver'});
			p.push({n:'parentInjector', t:'guice.Injector'});
			break;
		case 1:
			p = guice.Injector.injectionPoints(t);
			break;
		case 2:
			p = guice.Injector.injectionPoints(t);
			break;
		case 3:
			p = guice.Injector.injectionPoints(t);
			break;
		default:
			p = [];
			break;
	}
	return p;
};


// ====================================================
// guice.utilities.InjectionDecorator
// ====================================================

if (typeof guice == "undefined")
	var guice = {};
if (typeof guice.utilities == "undefined")
	guice.utilities = {};

guice.utilities.InjectionDecorator = function() {
};

guice.utilities.InjectionDecorator.prototype.decorateObject = function(dependency, className) {
	var injectableType = dependency;
	injectableType.injectionPoints = defaultInjectionPoints;
	injectableType.getClassDependencies = getClassDependencies;
	injectableType.className = className;
};

guice.utilities.InjectionDecorator.defaultInjectionPoints = function(t) {
};

guice.utilities.InjectionDecorator.getClassDependencies = function() {
	return [];
};

guice.utilities.InjectionDecorator.className = "guice.utilities.InjectionDecorator";

guice.utilities.InjectionDecorator.getClassDependencies = function(t) {
	var p;
	return [];
};

guice.utilities.InjectionDecorator.injectionPoints = function(t) {
	return [];
};

// ====================================================
// guice.binding.provider.AbstractProvider
// ====================================================

if (typeof guice == "undefined")
	var guice = {};
if (typeof guice.binding == "undefined")
	guice.binding = {};
if (typeof guice.binding.provider == "undefined")
	guice.binding.provider = {};

guice.binding.provider.AbstractProvider = function() {
};

guice.binding.provider.AbstractProvider.prototype.get = function() {
	return null;
};

guice.binding.provider.AbstractProvider.className = "guice.binding.provider.AbstractProvider";

guice.binding.provider.AbstractProvider.getClassDependencies = function(t) {
	var p;
	return [];
};

guice.binding.provider.AbstractProvider.injectionPoints = function(t) {
	return [];
};

// ====================================================
// guice.InjectionClassBuilder
// ====================================================

if (typeof guice == "undefined")
	var guice = {};

guice.InjectionClassBuilder = function(injector, classResolver) {
	this.injector = injector;
	this.classResolver = classResolver;
};

guice.InjectionClassBuilder.prototype.buildClass = function(className) {
	var type = this.classResolver.resolveClassName(className);
	return this.injector.getInstanceByDefinition(type);
};

guice.InjectionClassBuilder.className = "guice.InjectionClassBuilder";

guice.InjectionClassBuilder.getClassDependencies = function(t) {
	var p;
	return [];
};

guice.InjectionClassBuilder.injectionPoints = function(t) {
	var p;
	switch (t) {
		case 0:
			p = [];
			p.push({n:'injector', t:'guice.Injector'});
			p.push({n:'classResolver', t:'guice.resolver.ClassResolver'});
			break;
		default:
			p = [];
			break;
	}
	return p;
};


// ====================================================
// guice.binding.Binder
// ====================================================

if (typeof guice == "undefined")
	var guice = {};
if (typeof guice.binding == "undefined")
	guice.binding = {};

guice.binding.Binder = function(hashMap) {
	this.hashMap = hashMap;
};

guice.binding.Binder.prototype.getBinding = function(typeDefinition) {
	return this.hashMap[typeDefinition.getClassName()];
};

guice.binding.Binder.prototype.addBinding = function(abstractBinding) {
	this.hashMap[abstractBinding.getTypeName()] = abstractBinding;
};

guice.binding.Binder.prototype.bind = function(type) {
	var typeDefinition = new guice.reflection.TypeDefinition(type);
	var existingBinding = this.getBinding(typeDefinition);
	if (existingBinding != null) {
		if (existingBinding.getScope() == 1) {
			throw new Error("Overriding bindings for Singleton Scoped injections is not allowed.");
		}
	}
	return new guice.binding.BindingFactory(this, typeDefinition);
};

guice.binding.Binder.className = "guice.binding.Binder";

guice.binding.Binder.getClassDependencies = function(t) {
	var p;
	p = [];
	p.push('guice.reflection.TypeDefinition');
	p.push('guice.binding.BindingFactory');
	return p;
};

guice.binding.Binder.injectionPoints = function(t) {
	var p;
	switch (t) {
		case 0:
			p = [];
			p.push({n:'hashMap', t:'Object'});
			break;
		default:
			p = [];
			break;
	}
	return p;
};


// ====================================================
// guice.binding.AbstractBinding
// ====================================================

if (typeof guice == "undefined")
	var guice = {};
if (typeof guice.binding == "undefined")
	guice.binding = {};

guice.binding.AbstractBinding = function() {
};

guice.binding.AbstractBinding.prototype.provide = function(injector) {
	return null;
};

guice.binding.AbstractBinding.prototype.getTypeName = function() {
	return null;
};

guice.binding.AbstractBinding.prototype.getScope = function() {
	return -1;
};

guice.binding.AbstractBinding.className = "guice.binding.AbstractBinding";

guice.binding.AbstractBinding.getClassDependencies = function(t) {
	var p;
	return [];
};

guice.binding.AbstractBinding.injectionPoints = function(t) {
	return [];
};

// ====================================================
// guice.binding.decorator.ContextDecorator
// ====================================================

if (typeof guice == "undefined")
	var guice = {};
if (typeof guice.binding == "undefined")
	guice.binding = {};
if (typeof guice.binding.decorator == "undefined")
	guice.binding.decorator = {};

guice.binding.decorator.ContextDecorator = function(sourceBinding) {
	this.instance = null;
	guice.binding.AbstractBinding.call(this);
	this.sourceBinding = sourceBinding;
};

guice.binding.decorator.ContextDecorator.prototype.getTypeName = function() {
	return this.sourceBinding.getTypeName();
};

guice.binding.decorator.ContextDecorator.prototype.getScope = function() {
	return 2;
};

guice.binding.decorator.ContextDecorator.prototype.provide = function(injector) {
	if (this.instance == null) {
		this.instance = this.sourceBinding.provide(injector);
	}
	return this.instance;
};

$inherit(guice.binding.decorator.ContextDecorator, guice.binding.AbstractBinding);

guice.binding.decorator.ContextDecorator.className = "guice.binding.decorator.ContextDecorator";

guice.binding.decorator.ContextDecorator.getClassDependencies = function(t) {
	var p;
	return [];
};

guice.binding.decorator.ContextDecorator.injectionPoints = function(t) {
	var p;
	switch (t) {
		case 0:
			p = [];
			p.push({n:'sourceBinding', t:'guice.binding.AbstractBinding'});
			break;
		case 1:
			p = guice.binding.AbstractBinding.injectionPoints(t);
			break;
		case 2:
			p = guice.binding.AbstractBinding.injectionPoints(t);
			break;
		case 3:
			p = guice.binding.AbstractBinding.injectionPoints(t);
			break;
		default:
			p = [];
			break;
	}
	return p;
};


// ====================================================
// guice.binding.decorator.SingletonDecorator
// ====================================================

if (typeof guice == "undefined")
	var guice = {};
if (typeof guice.binding == "undefined")
	guice.binding = {};
if (typeof guice.binding.decorator == "undefined")
	guice.binding.decorator = {};

guice.binding.decorator.SingletonDecorator = function(sourceBinding) {
	guice.binding.decorator.ContextDecorator.call(this, sourceBinding);
};

guice.binding.decorator.SingletonDecorator.prototype.getScope = function() {
	return 1;
};

$inherit(guice.binding.decorator.SingletonDecorator, guice.binding.decorator.ContextDecorator);

guice.binding.decorator.SingletonDecorator.className = "guice.binding.decorator.SingletonDecorator";

guice.binding.decorator.SingletonDecorator.getClassDependencies = function(t) {
	var p;
	return [];
};

guice.binding.decorator.SingletonDecorator.injectionPoints = function(t) {
	var p;
	switch (t) {
		case 0:
			p = [];
			p.push({n:'sourceBinding', t:'guice.binding.AbstractBinding'});
			break;
		case 1:
			p = guice.binding.decorator.ContextDecorator.injectionPoints(t);
			break;
		case 2:
			p = guice.binding.decorator.ContextDecorator.injectionPoints(t);
			break;
		case 3:
			p = guice.binding.decorator.ContextDecorator.injectionPoints(t);
			break;
		default:
			p = [];
			break;
	}
	return p;
};


// ====================================================
// guice.binding.InstanceBinding
// ====================================================

if (typeof guice == "undefined")
	var guice = {};
if (typeof guice.binding == "undefined")
	guice.binding = {};

guice.binding.InstanceBinding = function(typeDefinition, instance) {
	guice.binding.AbstractBinding.call(this);
	this.typeDefinition = typeDefinition;
	this.instance = instance;
};

guice.binding.InstanceBinding.prototype.getTypeName = function() {
	return this.typeDefinition.getClassName();
};

guice.binding.InstanceBinding.prototype.getScope = function() {
	return 0;
};

guice.binding.InstanceBinding.prototype.provide = function(injector) {
	return this.instance;
};

$inherit(guice.binding.InstanceBinding, guice.binding.AbstractBinding);

guice.binding.InstanceBinding.className = "guice.binding.InstanceBinding";

guice.binding.InstanceBinding.getClassDependencies = function(t) {
	var p;
	return [];
};

guice.binding.InstanceBinding.injectionPoints = function(t) {
	var p;
	switch (t) {
		case 0:
			p = [];
			p.push({n:'typeDefinition', t:'guice.reflection.TypeDefinition'});
			p.push({n:'instance', t:'Object'});
			break;
		case 1:
			p = guice.binding.AbstractBinding.injectionPoints(t);
			break;
		case 2:
			p = guice.binding.AbstractBinding.injectionPoints(t);
			break;
		case 3:
			p = guice.binding.AbstractBinding.injectionPoints(t);
			break;
		default:
			p = [];
			break;
	}
	return p;
};


// ====================================================
// guice.binding.ProviderBinding
// ====================================================

if (typeof guice == "undefined")
	var guice = {};
if (typeof guice.binding == "undefined")
	guice.binding = {};

guice.binding.ProviderBinding = function(typeDefinition, providerTypeDefinition) {
	this.provider = null;
	guice.binding.AbstractBinding.call(this);
	this.typeDefinition = typeDefinition;
	this.providerTypeDefinition = providerTypeDefinition;
};

guice.binding.ProviderBinding.prototype.getTypeName = function() {
	return this.typeDefinition.getClassName();
};

guice.binding.ProviderBinding.prototype.getScope = function() {
	return 0;
};

guice.binding.ProviderBinding.prototype.provide = function(injector) {
	if (this.provider == null) {
		this.provider = injector.getInstanceByDefinition(this.providerTypeDefinition);
	}
	return this.provider.get();
};

$inherit(guice.binding.ProviderBinding, guice.binding.AbstractBinding);

guice.binding.ProviderBinding.className = "guice.binding.ProviderBinding";

guice.binding.ProviderBinding.getClassDependencies = function(t) {
	var p;
	return [];
};

guice.binding.ProviderBinding.injectionPoints = function(t) {
	var p;
	switch (t) {
		case 0:
			p = [];
			p.push({n:'typeDefinition', t:'guice.reflection.TypeDefinition'});
			p.push({n:'providerTypeDefinition', t:'guice.reflection.TypeDefinition'});
			break;
		case 1:
			p = guice.binding.AbstractBinding.injectionPoints(t);
			break;
		case 2:
			p = guice.binding.AbstractBinding.injectionPoints(t);
			break;
		case 3:
			p = guice.binding.AbstractBinding.injectionPoints(t);
			break;
		default:
			p = [];
			break;
	}
	return p;
};


// ====================================================
// guice.binding.TypeBinding
// ====================================================

if (typeof guice == "undefined")
	var guice = {};
if (typeof guice.binding == "undefined")
	guice.binding = {};

guice.binding.TypeBinding = function(typeDefinition, dependencyDefinition) {
	guice.binding.AbstractBinding.call(this);
	this.typeDefinition = typeDefinition;
	this.dependencyDefinition = dependencyDefinition;
};

guice.binding.TypeBinding.prototype.getTypeName = function() {
	return this.typeDefinition.getClassName();
};

guice.binding.TypeBinding.prototype.getScope = function() {
	return 0;
};

guice.binding.TypeBinding.prototype.provide = function(injector) {
	return injector.buildClass(this.dependencyDefinition);
};

$inherit(guice.binding.TypeBinding, guice.binding.AbstractBinding);

guice.binding.TypeBinding.className = "guice.binding.TypeBinding";

guice.binding.TypeBinding.getClassDependencies = function(t) {
	var p;
	return [];
};

guice.binding.TypeBinding.injectionPoints = function(t) {
	var p;
	switch (t) {
		case 0:
			p = [];
			p.push({n:'typeDefinition', t:'guice.reflection.TypeDefinition'});
			p.push({n:'dependencyDefinition', t:'guice.reflection.TypeDefinition'});
			break;
		case 1:
			p = guice.binding.AbstractBinding.injectionPoints(t);
			break;
		case 2:
			p = guice.binding.AbstractBinding.injectionPoints(t);
			break;
		case 3:
			p = guice.binding.AbstractBinding.injectionPoints(t);
			break;
		default:
			p = [];
			break;
	}
	return p;
};


// ====================================================
// guice.GuiceModule
// ====================================================

if (typeof guice == "undefined")
	var guice = {};

guice.GuiceModule = function() {
};

guice.GuiceModule.prototype.configure = function(binder) {
};

guice.GuiceModule.className = "guice.GuiceModule";

guice.GuiceModule.getClassDependencies = function(t) {
	var p;
	return [];
};

guice.GuiceModule.injectionPoints = function(t) {
	return [];
};

// ====================================================
// guice.binding.Scope
// ====================================================

if (typeof guice == "undefined")
	var guice = {};
if (typeof guice.binding == "undefined")
	guice.binding = {};

guice.binding.Scope = function() {
	
};

guice.binding.Scope.Instance =0;

guice.binding.Scope.Singleton =1;

guice.binding.Scope.Context =2;

guice.binding.Scope.className = "guice.binding.Scope";

guice.binding.Scope.getClassDependencies = function(t) {
	var p;
	return [];
};

guice.binding.Scope.injectionPoints = function(t) {
	return [];
};

// ====================================================
// guice.reflection.TypeDefinition
// ====================================================

if (typeof guice == "undefined")
	var guice = {};
if (typeof guice.reflection == "undefined")
	guice.reflection = {};

guice.reflection.TypeDefinition = function(clazz) {
	this._builtIn = false;
	this._type = null;
	this._type = clazz;
	if (this.get_type().injectionPoints == null) {
		this._builtIn = true;
	}
};

guice.reflection.TypeDefinition.Constructor =0;

guice.reflection.TypeDefinition.Property =1;

guice.reflection.TypeDefinition.Method =2;

guice.reflection.TypeDefinition.View =3;

guice.reflection.TypeDefinition.prototype.get_type = function() {
	return this._type;
};

guice.reflection.TypeDefinition.prototype.get_builtIn = function() {
	return this._builtIn;
};

guice.reflection.TypeDefinition.prototype.getClassName = function() {
	var className = this._type.className;
	if (!className) {
		throw new Error("Class not does defined a usable className");
	}
	return className;
};

guice.reflection.TypeDefinition.prototype.getSuperClassName = function() {
	var className = this._type.superClassName;
	if (!className) {
		className = "Object";
	}
	return className;
};

guice.reflection.TypeDefinition.prototype.getClassDependencies = function() {
	return this.get_type().getClassDependencies();
};

guice.reflection.TypeDefinition.prototype.injectionPoints = function(injectionType) {
	return this.get_type().injectionPoints(injectionType);
};

guice.reflection.TypeDefinition.prototype.getInjectionMethods = function() {
	return this.injectionPoints(2);
};

guice.reflection.TypeDefinition.prototype.getInjectionFields = function() {
	return this.injectionPoints(1);
};

guice.reflection.TypeDefinition.prototype.getViewFields = function() {
	return this.injectionPoints(3);
};

guice.reflection.TypeDefinition.prototype.getConstructorParameters = function() {
	return this.injectionPoints(0);
};

guice.reflection.TypeDefinition.prototype.constructorApply = function(args) {
	var instance = null;
	if (this._builtIn) {
		instance = new (this.get_type())();
	} else {
		var f;
		var c;
		c = this.get_type();
		f = new Function();
		f.prototype = c.prototype;
		instance = new f();
		c.apply(instance, args);
		instance.constructor = c;
	}
	return instance;
};

guice.reflection.TypeDefinition.className = "guice.reflection.TypeDefinition";

guice.reflection.TypeDefinition.getClassDependencies = function(t) {
	var p;
	return [];
};

guice.reflection.TypeDefinition.injectionPoints = function(t) {
	var p;
	switch (t) {
		case 0:
			p = [];
			p.push({n:'clazz', t:'Class'});
			break;
		default:
			p = [];
			break;
	}
	return p;
};

