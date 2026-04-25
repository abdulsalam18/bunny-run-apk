var StartApp = {

    init: function () {
        cordova.exec(null, null, "StartAppPlugin", "init", []);
    },

    showBanner: function () {
        cordova.exec(null, null, "StartAppPlugin", "showBanner", []);
    },

    hideBanner: function () {
        cordova.exec(null, null, "StartAppPlugin", "hideBanner", []);
    },

    showInterstitial: function () {
        cordova.exec(null, null, "StartAppPlugin", "showInterstitial", []);
    },

    showReward: function (cb) {
        cordova.exec(cb, null, "StartAppPlugin", "showReward", []);
    },

    more: function () {
        cordova.exec(null, null, "StartAppPlugin", "more", []);
    },

    share: function () {
        cordova.exec(null, null, "StartAppPlugin", "share", []);
    }
};