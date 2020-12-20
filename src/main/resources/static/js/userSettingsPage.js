$(document).ready(function () {
    switchButtons(4);
});

let f = false;

let getCategoryName = function (category) {
    let name;
    switch (category) {
        case "BEER" :
            name = "Алкоголь";
            break;

        default :
            name = "Товар без категории"
            break;
    }
    return name;
}


$('#edit-on-msg').click(function () {
    if (!f) {
        $.ajax({
            url: 'http://localhost:3030/api/supermarkets/categories',
            type: 'GET',
            success: function (d) {
                for (let i = 0; i < d.length; i++) {
                    let cat = getCategoryName(d[i]);
                    let row = i + 1;
                    $('#cat-body').append('<tr>\n' +
                        '        <th scope="row">' + row + '</th>\n' +
                        '        <td>' + cat + '</td>\n' +
                        '        <td><input class="form-check-input" type="checkbox" value="" id="check' + i + '"></td>\n' +
                        '    </tr>');
                }
                f = true;
            }
        });
    }

    $('#editCat').modal('show');

});

$('#switch-on-msg').click(function () {


    if (!f) {
        $.ajax({
            url: 'http://localhost:3030/api/supermarkets/categories',
            type: 'GET',
            success: function (d) {
                for (let i = 0; i < d.length; i++) {
                    let cat = getCategoryName(d[i]);
                    let row = i + 1;
                    $('#cat-body').append('<tr>\n' +
                        '        <th scope="row">' + row + '</th>\n' +
                        '        <td>' + cat + '</td>\n' +
                        '        <td><input class="form-check-input" type="checkbox" value="" id="check' + i + '"></td>\n' +
                        '    </tr>');
                }
                f = true;
            }
        });
    }

    $('#editCat').modal('show');

});