$(document).ready(function () {
    switchButtons(3);
});

let goods;
let goodName;
let user;

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

function getWeight(weight) {
    return weight === "Not stated" ? "-" : weight;
}

function getPriceWithout(price, disc) {
    return (price / ((100 - disc) / 100)).toFixed(2);
}

function getValues(i) {
    /*let disc;

    switch (i) {
        case 1:
            disc = 10;
            break;
        case 2:
            disc = 12;
            break;
        case 3:
            disc = 15;
            break;
        case 4:
            disc = 21;
            break;
        case 5:
            disc = 3;
            break;
    }


    $('#best-price-price-without-' + i).text("Цена без скидки: " + getPriceWithout(goods[i - 1].price, disc) + " руб");
    $('#best-price-sale-' + i).text("-" + disc + "%");
     */
    $('#best-price-price-without-' + i).text("Цена без скидки: " + getPriceWithout(goods[i - 1].price, goods[i - 1].discount) + " руб");
    $('#best-price-sale-' + i).text("-" + goods[i - 1].discount + "%");
}

function getShopInfo(i, sn) {
    if (sn === "ОКЕЙ") {
        if (window.OkeyAdr) {
            $('#best-price-address-' + i).text("Адрес магазина: " + window.OkeyAdr);
        }
        if (window.OkeyDist) {
            $('#best-price-dir-' + i).text("Расстояние до магазина: " + window.OkeyDist + " км");
        }
    } else {
        if (window.LentaAdr) {
            $('#best-price-address-' + i).text("Адрес магазина: " + window.LentaAdr);
        }
        if (window.LentaDist) {
            $('#best-price-dir-' + i).text("Расстояние до магазина: " + window.LentaDist + " км");
        }
    }
}

function showGoods() {
    for (let i = 1; i <= 5; i++) {
        $('#best-price-item-' + i).css("display", "block");
        (goods[i - 1].pathToPicture.indexOf("/wcsstore/OKMarketCAS") != -1)
            ? $('#best-price-pict-' + i).attr("src", "https://www.okeydostavka.ru" + (goods[i - 1].pathToPicture))
            : $('#best-price-pict-' + i).attr("src", goods[i - 1].pathToPicture.replace("?preset=thumbnail", ""));
        $('#best-price-name-' + i).text(goods[i - 1].name);
        $('#best-price-cat-' + i).text("Категория: " + getCategoryName(goods[i - 1].goodCategory));
        $('#best-price-weight-' + i).text("Вес/объем: " + getWeight(goods[i - 1].weight));
        $('#best-price-price-' + i).text("Цена со скидкой: " + goods[i - 1].price.toFixed(2) + " руб");
        getValues(i);

        let sn = (
            (goods[i - 1].pathToPicture.indexOf("/wcsstore/OKMarketCAS") !== -1))
            ? "ОКЕЙ"
            : "Лента";
        $('#best-price-shop-' + i).text("Название магазина: " + sn);

        getShopInfo(i, sn);

    }

}

let initDP = function (data) {
    for (let i = 1; i <= 5; i++) {
        $('#best-price-item-' + i).css("display", "none");
    }
    goods = data.goods;
    goodName = data.goodName;
    user = data.user;
    console.log(goods);
    if (goodName) {
        $('#good-name-input').val(goodName);
        showGoods();
    }
}

function showAlert(message, alerttype) {
    $('#alert-placeholder').append('<div id="alertdiv" class="alert ' + alerttype + '"><a class="close" data-dismiss="alert">×</a><span>' + message + '</span></div>')
    setTimeout(function () {
        $("#alertdiv").remove();
    }, 5000);
}

$('#best-price-btn').click(function () {
    if ($('#good-name-input').val() === "") {
        showAlert("Введите название товара!", "alert-warning");
    } else {
        let uid = 0;
        if (user) {
            uid = user.id;
        }
        let gn = $('#good-name-input').val();
        console.log("9999999999999");
        console.log(window.myVar);
        showAlert("Поиск товаров начался! Подождите, пожалуйста", "alert-warning");
        window.location.replace("/api/supermarkets/bestPrice/" + gn + "/" + uid);
    }
});