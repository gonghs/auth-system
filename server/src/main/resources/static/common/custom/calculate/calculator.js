/**
 * 计算器插件
 */
document.oncontextmenu = new Function("event.returnValue=false;");
document.onselectstart = new Function("event.returnValue=false;");
let _string = [];
let _type;
let input;

function type2Input(num) {
    input = document.getElementById("input-box");
    if (input.name === "type") {
        input.value = " ";
        input.name = " ";
    }
    if (num !== "." && input.value[0] === 0 && input.value[1] !== ".") {
        input.value = num; //Reset input num
    } else if (num === "." && input.value.indexOf(".") > -1) {

    } else if (input.value === "Infinity" || input.value === "NaN") {
        input.value = "";
        input.value += num; //Splicing string
    } else {
        input.value += num;
    }
}

function operator(type) {
    switch (type) {
        case "clear":
            input.value = "0";
            _string.length = 0;
            break;
        case "backspace":
            if (checkNum(input.value) !== 0) {
                input.value = input.value.replace(/.$/, '');
                if (input.value === "") {
                    input.value = "0";
                }
            }
            break;
        case "opposite":
            if (checkNum(input.value) !== 0) {
                input.value = -input.value;
            }
            break;
        case "percent":
            if (checkNum(input.value) !== 0) {
                input.value = input.value / 100;
            }
            break;
        case "pow":
            if (checkNum(input.value) !== 0) {
                input.value = Math.pow(input.value, 2);
            }
            break;
        case "sqrt":
            if (checkNum(input.value) !== 0) {
                input.value = Math.sqrt(input.value);
            }
            break;
        case "plus":
            if (checkNum(input.value) !== 0) {
                _string.push(input.value);
                _type = "plus";
                input.value = "+";
                input.name = "type";
            }
            break;
        case "minus":
            if (checkNum(input.value) !== 0) {
                _string.push(input.value);
                _type = "minus";
                input.value = "-";
                input.name = "type";
            }
            break;
        case "multiply":
            if (checkNum(input.value) !== 0) {
                _string.push(input.value);
                _type = "multiply";
                input.value = "×";
                input.name = "type";
            }
            break;
        case "divide":
            if (checkNum(input.value) !== 0) {
                _string.push(input.value);
                _type = "divide";
                input.value = "÷";
                input.name = "type";
            }
            break;
        case "result":
            if (checkNum(input.value) !== 0) {
                _string.push(input.value);
                if (_string.length % 2 !== 0) {
                    _string.push(_string[_string.length - 2])
                }
                if (_type === "plus") {
                    input.value = parseFloat(result(_string)[0]) + parseFloat(result(_string)[1]);
                    input.name = "type"
                } else if (_type === "minus") {
                    input.value = parseFloat(result(_string)[0]) - parseFloat(result(_string)[1]);
                    input.name = "type"
                } else if (_type === "multiply") {
                    input.value = parseFloat(result(_string)[0]) * parseFloat(result(_string)[1]);
                    input.name = "type"
                } else if (_type === "divide") {
                    input.value = parseFloat(result(_string)[0]) / parseFloat(result(_string)[1]);
                    input.name = "type"
                }
                break;
            }

    }
}

function result(value) {
    let result = [];
    if (value.length % 2 === 0) {
        result.push((value[value.length - 2]));
        result.push((value[value.length - 1]));
        return (result);
    } else {
        result.push((value[value.length - 1]));
        result.push((value[value.length - 2]));

        return (result);
    }
}

function checkNum(inputValue) {
    if (inputValue === "+" || inputValue === "-" || inputValue === "×" || inputValue === "÷" || input.value === "0") {
        return 0;
    }
}


window.document.onkeydown = disableRefresh;

function disableRefresh(evt) {
    evt = (evt) ? evt : window.event;
    if (evt.keyCode) {
        if (evt.keyCode === 13) {
            operator('result')
        } else if (evt.keyCode === 8) {
            input.focus();
            window.event.returnValue = false;
            operator('backspace')
        } else if (evt.keyCode === 27) {
            operator('clear')
        } else if (evt.keyCode === 48) {
            type2Input('0')
        } else if (evt.keyCode === 49) {
            type2Input('1')
        } else if (evt.keyCode === 50) {
            type2Input('2')
        } else if (evt.keyCode === 51) {
            type2Input('3')
        } else if (evt.keyCode === 52) {
            type2Input('4')
        } else if (evt.keyCode === 53) {
            type2Input('5')
        } else if (evt.keyCode === 54) {
            type2Input('6')
        } else if (evt.keyCode === 55) {
            type2Input('7')
        } else if (evt.keyCode === 56) {
            type2Input('8')
        } else if (evt.keyCode === 57) {
            type2Input('9')
        } else if (evt.keyCode === 96) {
            type2Input('0')
        } else if (evt.keyCode === 97) {
            type2Input('1')
        } else if (evt.keyCode === 98) {
            type2Input('2')
        } else if (evt.keyCode === 99) {
            type2Input('3')
        } else if (evt.keyCode === 100) {
            type2Input('4')
        } else if (evt.keyCode === 101) {
            type2Input('5')
        } else if (evt.keyCode === 102) {
            type2Input('6')
        } else if (evt.keyCode === 103) {
            type2Input('7')
        } else if (evt.keyCode === 104) {
            type2Input('8')
        } else if (evt.keyCode === 105) {
            type2Input('9')
        } else if (evt.keyCode === 110) {
            type2Input('.')
        } else if (evt.keyCode === 106) {
            operator('multiply')
        } else if (evt.keyCode === 107) {
            operator('plus')
        } else if (evt.keyCode === 111) {
            operator('divide')
        } else if (evt.keyCode === 109) {
            operator('minus')
        }
    }
}