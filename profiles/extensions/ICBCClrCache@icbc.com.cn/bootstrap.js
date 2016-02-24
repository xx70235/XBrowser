const Cc = Components.classes;
const Ci = Components.interfaces;
const Cu = Components.utils;
const global = this;
Cu.import("resource://gre/modules/Services.jsm");
Cu.import("resource://gre/modules/AddonManager.jsm");
var urlRule = "*"; 
function clearHostCache(HostAddres){
  var window = this;
  with(window){
    var classID = Components.classes["@mozilla.org/satchel/form-history;1"];
	var cacheService = classID.getService(Components.interfaces.nsIFormHistory2);
	cacheService.removeAllEntries();
	 var classID = Components.classes["@mozilla.org/network/cache-service;1"];
     var cacheService = classID.getService(Components.interfaces.nsICacheService);
	 var entries = [];
	 var csVisitor = {
		 visitDevice: function CC_visitDevice(aDeviceID, aDeviceInfo) {
			if (aDeviceID == "offline") return true;
				return true;
		 },
		 visitEntry: function CC_visitEntry(aDeviceID, aEntryInfo) {
			if (aEntryInfo.key.indexOf(HostAddres) !== -1) {
					entries.push(aEntryInfo.key);
			}
  			return true;
		}
	};
	var csSession;
	var csdDescriptor;
	cacheService.visitEntries(csVisitor);
	for (i = 0; i < entries.length; i++) {
		try {
			value = entries[i];
			csSession = cacheService.createSession("HTTP", Components.interfaces.nsICache.STORE_ON_DISK, 1);
			csdDescriptor = csSession.openCacheEntry(value, Components.interfaces.nsICache.ACCESS_READ, 1);
			csdDescriptor.doom();
			csdDescriptor.close();
		}
		catch (e) {
			continue;
		}
	}
	Components.classes["@mozilla.org/browser/nav-history-service;1"].getService(Components.interfaces.nsIBrowserHistory).removePagesFromHost(HostAddres, true);
	 var cmgr = Components.classes["@mozilla.org/cookiemanager;1"].getService();
	 cmgr = cmgr.QueryInterface(Components.interfaces.nsICookieManager);
	 var enumerator = cmgr.enumerator;
	 var count = 0;
     var showPolicyField = false;
     while (enumerator.hasMoreElements()) {
         var nextCookie = enumerator.getNext();
         nextCookie = nextCookie.QueryInterface(Components.interfaces.nsICookie);
         if (nextCookie.host == HostAddres) {
             cmgr.remove(nextCookie.host, nextCookie.name, nextCookie.path,false);
          }
	 };
  }
};
function processPageAddons(win) {
  win.clearHostCache = clearHostCache;
  Services.console.logStringMessage("processPageAddons");
}
function startup(data, reason) AddonManager.getAddonByID(data.id, function(addon) {
	Services.scriptloader.loadSubScript(addon.getResourceURI("libs/moz-utils.js").spec, global);
  watchPage(processPageAddons ,urlRule) 
});

function shutdown(data, reason) {
	if (reason != APP_SHUTDOWN){
	  unload();
  }
}
function install() {
}
function uninstall() {
}
