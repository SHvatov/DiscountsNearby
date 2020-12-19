let switchButtons = function (buttonNumber) {
    let buttons = $('.nav-btn');
    let button = $("#nav-btn-" + buttonNumber);
    button[0].classList.add("active");
    for (let i = 0; i < 4; i++) {
        if (i !== buttonNumber - 1) buttons[i].classList.remove("active");
    }
}

$('#nav-btn-1').click(function () {
    switchButtons(1);
})

$('#nav-btn-2').click(function () {
    switchButtons(2);
})

$('#nav-btn-3').click(function () {
    switchButtons(3);
})

$('#nav-btn-4').click(function () {
    switchButtons(4);
})