$(document).ready(function () {
    switchButtons(4);

    for (let i = 1; i <= 10; i++) {
        $('#list-btn-' + i).click(function () {
            sr = $('#list-btn-' + i).val();
            $('#list-btn').text($('#list-btn-' + i).text());
        });
    }
});

$('#edit-on-msg').click(function () {
    clickEditOnMsgBtn()
});

$('#switch-on-msg').click(function () {
    clickSwitchOnMsgBtn()
});

$('#cancel-cat').click(function () {
    clickCancelCatBtn()
});

$('#save-sr').click(function () {
    srFun(true)
});

$('#cancel-sr').click(function () {
    srFun(false)
});

$('#save-cat').click(function () {
    clickSaveCatBtn()
});

$('#yes-btn').click(function () {
    clickYesBtn()
});

let f = false;

let curUser;

let catCount;

let cats = [];

let sr = 2500;

function getRadiusName(r) {
    switch (r) {
        case 500 :
            return "500 метров";
        case 1000 :
            return "1.0 километр";
        case 1500 :
            return "1.5 километра";
        case 2000 :
            return "2.0 километра";
        case 2500 :
            return "2.5 километра";
        case 3000 :
            return "3.0 километра";
        case 3500 :
            return "3.5 километра";
        case 4000 :
            return "4.0 километра";
        case 4500 :
            return "4.5 километра";
        case 5000 :
            return "5.0 километров";
    }
}

function initSettings(user) {
    let preferences = user.preferences;

    if (!preferences) {
        $("#check-msg").prop("checked", false);
        $('#list-btn').text("2.5 километра");
    } else {
        preferences.notificationsEnabled ? $("#check-msg").prop("checked", true)
            : $("#check-msg").prop("checked", false);
        $('#list-btn').text(getRadiusName(preferences.searchRadius));
    }
}

function initSettingsPage(data) {
    cats = data.categories;
    catCount = data.categories.length;
    curUser = data.user;
    initSettings(curUser);
}

function getCategoryName(category) {
    let name;
    switch (category) {
        case "BEER" :
            name = "Алкоголь";
            break;

        case "MEAT" :
            name = "Мясо";
            break;

        default :
            name = "Товар без категории"
            break;
    }
    return name;
}


function clickEditOnMsgBtn() {
    let favouriteCategories = curUser.preferences.favouriteCategories;
    for (let i = 0; i < catCount; i++) {
        let cat = getCategoryName(cats[i]);
        let row = i + 1;
        if (!f) {
            $('#cat-body').append('<tr>\n' +
                '        <th scope="row">' + row + '</th>\n' +
                '        <td>' + cat + '</td>\n' +
                '        <td><input class="form-check-input" type="checkbox" value="" id="check' + i + '"></td>\n' +
                '    </tr>');

        }

        if (favouriteCategories && favouriteCategories.includes(cats[i])) {
            $('#check' + i).prop("checked", true);
        }
    }
    f = true;

    $('#editCat').modal('show');
}

function clickSwitchOnMsgBtn() {
    for (let i = 0; i < catCount; i++) {
        let cat = getCategoryName(cats[i]);
        let row = i + 1;
        if (!f) {
            $('#cat-body').append('<tr>\n' +
                '        <th scope="row">' + row + '</th>\n' +
                '        <td>' + cat + '</td>\n' +
                '        <td><input class="form-check-input" type="checkbox" value="" id="check' + i + '"></td>\n' +
                '    </tr>');
        }
        $('#check' + i).prop("checked", false);
    }

    f = true;

    $('#editCat').modal('show');
}

function clickCancelCatBtn() {
    for (let i = 0; i < catCount; i++) {
        $('#check' + i).prop("checked", false);
    }
}

function userToStr(user) {
    return user.id + ":" + user.preferences.searchRadius + ":"
        + user.preferences.notificationsEnabled + ":" + user.preferences.favouriteCategories.toString()
}

function clickSaveCatBtn() {
    let fc = [];

    for (let i = 0; i < catCount; i++) {
        if ($('#check' + i).prop("checked") === true) {
            fc.push(cats[i]);
        }
    }

    let searchRadius = 2500;

    if (curUser.preferences) {
        searchRadius = curUser.preferences.searchRadius;
    }

    curUser.preferences = {
        notificationsEnabled: fc.length > 0,
        favouriteCategories: fc,
        searchRadius: searchRadius
    };

    $.ajax({
        url: 'http://localhost:3030/api/users/update/' + userToStr(curUser),
        type: 'GET',
        success: function (dat) {
            location.reload();
        }
    });
}

function clickYesBtn() {
    let fc = [];

    for (let i = 0; i < catCount; i++) {
        if ($('#check' + i).prop("checked") === true) {
            fc.push(cats[i]);
        }
    }

    let searchRadius = 2500;

    if (curUser.preferences) {
        searchRadius = curUser.preferences.searchRadius;
    }

    curUser.preferences = {
        notificationsEnabled: false,
        favouriteCategories: fc,
        searchRadius: searchRadius
    };

    $.ajax({
        url: 'http://localhost:3030/api/users/update/' + userToStr(curUser),
        type: 'GET',
        success: function (dat) {
            location.reload();
        }
    });
}


function srFun(flag) {
    let fc = [];

    for (let i = 0; i < catCount; i++) {
        if ($('#check' + i).prop("checked") === true) {
            fc.push(cats[i]);
        }
    }
    let ne = fc.length > 0;
    if (curUser.preferences) {
        ne = curUser.preferences.notificationsEnabled;
    }

    let searchRadius;
    if (flag)
        searchRadius = sr;
    else if (curUser.preferences) {
        searchRadius = curUser.preferences.searchRadius;
    } else {
        searchRadius = 2500;
    }
    $('#list-btn').text(getRadiusName(searchRadius));

    curUser.preferences = {
        notificationsEnabled: ne,
        favouriteCategories: fc,
        searchRadius: searchRadius
    };

    $.ajax({
        url: 'http://localhost:3030/api/users/update/' + userToStr(curUser),
        type: 'GET',
        success: function (dat) {
            location.reload();
        }
    });
}

