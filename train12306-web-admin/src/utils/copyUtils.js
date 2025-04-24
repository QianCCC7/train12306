// 深拷贝
export function deepCopy(obj) {
    let newObj;
    // 如果是对象或者数组就继续递归克隆
    if (typeof (obj) == 'object' && obj !== null) {
        newObj = obj instanceof Array ? [] : {};
        for (let i in obj) {
            newObj[i] = deepCopy(obj[i])
        }
    } else {
        newObj = obj;
    }

    return newObj;
}