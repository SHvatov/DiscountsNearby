$(document).ready(function () {
    switchButtons(3);
});

$('#best-price-btn').click(function () {
    clickOnBestPriceBtn()
});

let goods;
let goodName;
let user;

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

function getWeight(weight) {
    return weight === "Not stated" ? "-" : weight;
}

function getPriceWithoutDiscount(price, disc) {
    return (price / ((100 - disc) / 100)).toFixed(2);
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
        $('#best-price-price-without-' + i).text("Цена без скидки: " + getPriceWithoutDiscount(goods[i - 1].price, goods[i - 1].discount) + " руб");
        $('#best-price-sale-' + i).text("-" + goods[i - 1].discount + "%");

        let sn = (
            (goods[i - 1].pathToPicture.indexOf("/wcsstore/OKMarketCAS") !== -1))
            ? "ОКЕЙ"
            : "Лента";
        $('#best-price-shop-' + i).text("Название магазина: " + sn);
    }

}

function initBestPricePage(data) {
    for (let i = 1; i <= 5; i++) {
        $('#best-price-item-' + i).css("display", "none");
    }
    goods = data.goods;
    goodName = data.goodName;
    user = data.user;
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

function clickOnBestPriceBtn() {
    if ($('#good-name-input').val() === "") {
        showAlert("Введите название товара!", "alert-warning");
    } else {
        let uid = 0;
        if (user) {
            uid = user.id;
        }
        let gn = $('#good-name-input').val();
        showAlert("Поиск товаров начался! Подождите, пожалуйста", "alert-warning");
        window.location.replace("/api/supermarkets/bestPrice/" + gn + "/" + uid);
    }
}