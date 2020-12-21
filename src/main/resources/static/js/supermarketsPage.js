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

let getPathToPhoto = function (path) {
    let name;
    switch (path) {
        case "https://www.okeydostavka.ru/spb/alkogol-nye-napitki/pivo/pivo-zhiguli-barnoe-svetloe-4-9-0-5l-st-b-mpk" :
            name = "https://www.okeydostavka.ru/wcsstore/OKMarketCAS/cat_entries/609900/609900_fullimage.jpg";
            break;

        case "https://www.okeydostavka.ru/spb/alkogol-nye-napitki/pivo/pivo-praga-premium-pils-svetloe-pasterizovannoe-" :
            name = "https://www.okeydostavka.ru/wcsstore/OKMarketCAS/cat_entries/754060/754060_fullimage.jpg";
            break;

        case "https://www.okeydostavka.ru/spb/alkogol-nye-napitki/pivo/pivo-stella-artua-svetloe-pasteriz-bezalkogol-noe-" :
            name = "https://www.okeydostavka.ru/wcsstore/OKMarketCAS/cat_entries/452332/452332_fullimage.jpg";
            break;

        case "https://www.okeydostavka.ru/spb/alkogol-nye-napitki/pivo/pivo-shpaten-miunkhen-svetloe-5-2-0-5l-zh-b-saninbev" :
            name = "https://www.okeydostavka.ru/wcsstore/OKMarketCAS/cat_entries/701289/701289_fullimage.jpg";
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
    return (myGood.price / ((100 - myGood.discount) / 100)).toFixed(2);
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
        (myShop === "OKEY")
            ? $('#li-pict-topTen-' + i).attr("src", getPathToPhoto(myGoods[i - 1].pathToPicture))
            : $('#li-pict-topTen-' + i).attr("src", myGoods[i - 1].pathToPicture.replace("?preset=thumbnail", ""));
        $('#li-name-topTen-' + i).text(myGoods[i - 1].name);
        $('#li-cat-topTen-' + i).text("Категория: " + getCategoryName(myGoods[i - 1].goodCategory));
        $('#li-weight-topTen-' + i).text("Вес/объем: " + getWeight(myGoods[i - 1].weight));
        $('#li-with-price-topTen-' + i).text("Цена со скидкой: " + myGoods[i - 1].price + " руб");
        $('#li-without-price-topTen-' + i).text("Цена без скидки: " + getPriceWithout(myGoods[i - 1]) + " руб");
        $('#li-sale-topTen-' + i).text("-" + myGoods[i - 1].discount + "%");
    }
});

$('#btn-cats').click(function () {
    $('#div-topTen').css("display", "none");
    $('#div-cats').css("display", "block");
    $('#btn-topTen').removeClass("active");
    $('#btn-cats').addClass("active");

    for (let i = 0; i < myCats.length; i++) {
        $('#cats-btns').append(' <button class="btn btn-outline-secondary" id="cats-btn-' + i + '" type="button"></button>\n');
        $('#cats-btn-' + i).text(getCategoryName(myCats[i]));
    }
});