
var Balabala = function(){};

Balabala.prototype.toast = function(arg) {
    cordova.exec(function(winParams){
        console.log(typeof winParams);
    }, function(err){
        console.log(err);
    }, 'Echo', 'toast', arg);
};

Balabala.prototype.understand = function() {
    var self = this;
    cordova.exec(function(winParams) {
        if (typeof winParams == 'object' && winParams["action"]) {
            var fn = self[winParams["action"]];
            if (typeof fn == 'function') {
                winParams['arg'] = winParams['arg'] || "";
                fn.apply(null, [winParams['arg']]);
            } else {
                console.log(winParams['action'] + " is not function.");
            }
        }
    }, function(err){
        this.error(err);
    }, 'Echo', 'understand', "understand");
};

Balabala.prototype.error = function(err) {
    console.log("--> Balabala onError called: " + err);
};

Balabala.prototype.beginOfSpeech = function() {
    console.log("--> Balabala onBeginOfSpeech called.");
};

Balabala.prototype.volumeChanged = function(volume) {
    console.log("--> Balabala onVolumeChanged called: " + volume);
};

Balabala.prototype.endOfSpeech = function() {
    console.log("--> Balabala onEndOfSpeech called.");
};

Balabala.prototype.result = function(text) {
    console.log("--> Balabala onResult called: " + text);
};

// 设置开始录音监听接口
Balabala.prototype.onBeginOfSpeech = function(callback) {
    if (typeof callback == 'function') {
        this.beginOfSpeech = callback;
    }
};

// 录音过程中说话声音改变是的监听接口
Balabala.prototype.onVolumeChanged = function(callback) {
    if (typeof callback == 'function') {
        this.volumeChanged = callback;
    }
};

// 这只结束录音的监听接口
Balabala.prototype.onEndOfSpeech = function(callback) {
    if (typeof callback == 'function') {
        this.endOfSpeech = callback;
    }
};

// 识别结束
Balabala.prototype.onResult = function(callback) {
    if (typeof callback == 'function') {
        this.result = callback;
    }
};

// 录音过程出现错误
Balabala.prototype.onError = function(callback) {
    if (typeof callback == 'function') {
        this.error = callback;
    }
};


if(!window.plugins){
    window.plugins = {};
}

if(!window.plugins.Balabala){
    window.plugins.Balabala = new Balabala();
}

module.exports = new Balabala();

