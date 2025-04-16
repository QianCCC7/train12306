SessionStorage = {
    get: function (key) {
        // sessionStorage是H5自带的变量，使用getItem可以获取会话缓存
        // 还有一个会话缓存是localStorage，区别在于：sessionStorage是对当前会话有用，而localStorage不止针对当前会话
        var v = sessionStorage.getItem(key);
        if (v && typeof (v) !== "undefined" && v !== "undefined") {
            return JSON.parse(v);
        }
    },
    set: function (key, data) {
        // 原生的sessionStorage只能操作字符串，所以需要转成string，这样才能放到session缓存里
        sessionStorage.setItem(key, JSON.stringify(data));
    },
    remove: function (key) {
        sessionStorage.removeItem(key);
    },
    clearAll: function () {
        sessionStorage.clear();
    },
};