$(document).ready(function () {
    switchButtons(2);
    $('#div-topTen').css("display", "none");
    $('#div-cats').css("display", "none");
});

let myShop;
let myCats;
let myGoods;

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

let initDataPage = function (data) {
    myShop = data.shop;
    myCats = data.categories;
    myGoods = data.goods;
    let shopName = myShop === "LENTA"
        ? "Лента"
        : "Окей";
    $("#nav-btn-2").text("Магазины " + shopName);
}

function getWeight(weight) {
    return weight === "Not stated" ? "-" : weight;
}

function getPriceWithout(myGood) {
    return (myGood.price / (myGood.discount / 100)).toFixed(2);
}

function getDiscount(discount) {
    return 100 - discount;
}

$('#btn-topTen').click(function () {
    $('#div-topTen').css("display", "block");
    $('#div-cats').css("display", "none");
    $('#btn-topTen').addClass("active");
    $('#btn-cats').removeClass("active");

    for (let i = 1; i <= 10; i++) {
        $('#li-elem-topTen-' + i).css("display", "block");
    }

    let goodsCount = myGoods.length;

    for (let i = goodsCount + 1; i <= 10; i++) {
        $('#li-elem-topTen-' + i).css("display", "none");
    }

    for (let i = 1; i <= goodsCount; i++) {
        $('#li-pict-topTen-' + i).attr("src", "https://www.okeydostavka.ru/wcsstore/OKMarketCAS/cat_entries/609900/609900_fullimage.jpg");
        $('#li-name-topTen-' + i).text(myGoods[i - 1].name);
        $('#li-cat-topTen-' + i).text("Категория: " + getCategoryName(myGoods[i - 1].goodCategory));
        $('#li-weight-topTen-' + i).text("Вес/объем: " + getWeight(myGoods[i - 1].weight));
        $('#li-with-price-topTen-' + i).text("Цена со скидкой: " + myGoods[i - 1].price + " руб");
        $('#li-without-price-topTen-' + i).text("Цена без скидки: " + getPriceWithout(myGoods[i - 1]) + " руб");
        $('#li-sale-topTen-' + i).text("-" + getDiscount(myGoods[i - 1].discount) + "%");
    }
});

$('#btn-cats').click(function () {
    $('#div-topTen').css("display", "none");
    $('#div-cats').css("display", "block");
    $('#btn-topTen').removeClass("active");
    $('#btn-cats').addClass("active");
});