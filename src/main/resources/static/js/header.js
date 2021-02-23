function switchButtons(buttonNumber) {
    let buttons = $('.nav-btn');
    let button = $("#nav-btn-" + buttonNumber);
    button[0].classList.add("active");
    for (let i = 0; i < 4; i++) {
        if (i !== buttonNumber - 1) buttons[i].classList.remove("active");
    }
}