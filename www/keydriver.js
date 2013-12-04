/**
 * cordova KeyDriver plugin
 * Author: Sardar Yumatov (ja.doma@gmail.com)
 * Copyright (c) Oleksij Nesterov 2014
 */
 (function(cordova){
    var KeyDriver = function() {

    };

    WebIntent.prototype.broadcastKey = function(params, success, fail) {
        return cordova.exec(function(args) {
            success(args);
        }, function(err) {
            fail(err);
        }, 'KeyDirver', 'broadcastKey', [params]);
    };

    window.keydriver = new KeyDriver();
    window.plugins = window.plugins || {};
    window.plugins.keydriver = window.keydriver;
})(window.PhoneGap || window.Cordova || window.cordova);
