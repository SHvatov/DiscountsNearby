$(document).ready(function () {
    switchButtons(2);
    $('#div-topTen').css("display", "none");
    $('#div-cats').css("display", "none");
});

let myShop;
let myCats;
let myGoods;
let myGoodsByCats;

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

function initSupermarketsPage(data) {
    myShop = data.shop;
    myCats = data.categories;
    myGoods = data.goods;
    myGoodsByCats = data.goodsByCategories;
    let shopName = myShop === "LENTA"
        ? "Лента"
        : "Окей";
    $("#nav-btn-2").text("Магазины " + shopName);
}

function getWeight(weight) {
    return weight === "Not stated" ? "-" : weight;
}

function getPriceWithoutDiscount(myGood) {
    return (myGood.price / ((100 - myGood.discount) / 100)).toFixed(2);
}

function clickTopTenBtn() {
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
            ? $('#li-pict-topTen-' + i).attr("src", "https://www.okeydostavka.ru" + (myGoods[i - 1].pathToPicture))
            : $('#li-pict-topTen-' + i).attr("src", myGoods[i - 1].pathToPicture.replace("?preset=thumbnail", ""));
        $('#li-name-topTen-' + i).text(myGoods[i - 1].name);
        $('#li-cat-topTen-' + i).text("Категория: " + getCategoryName(myGoods[i - 1].goodCategory));
        $('#li-weight-topTen-' + i).text("Вес/объем: " + getWeight(myGoods[i - 1].weight));
        $('#li-with-price-topTen-' + i).text("Цена со скидкой: " + myGoods[i - 1].price + " руб");
        $('#li-without-price-topTen-' + i).text("Цена без скидки: " + getPriceWithoutDiscount(myGoods[i - 1]) + " руб");
        $('#li-sale-topTen-' + i).text("-" + myGoods[i - 1].discount + "%");
    }
}

function clickCatsBtn() {
    for (let i = 1; i <= 10; i++) {
        $('#li-elem-topCats-' + i).css("display", "none");
    }

    $('#div-topTen').css("display", "none");
    $('#div-cats').css("display", "block");
    $('#btn-topTen').removeClass("active");
    $('#btn-cats').addClass("active");

    $('#cats-btns').empty();

    for (let i = 0; i < myCats.length; i++) {
        $('#cats-btns').append(' <button class="btn btn-outline-secondary" id="cats-btn-' + i + '" type="button"></button>\n');
        $('#cats-btn-' + i).text(getCategoryName(myCats[i]));
        $('#cats-btn-' + i).click(function () {

            for (let k = 0; k < myCats.length; k++) {
                $('#cats-btn-' + k).removeClass("active");
            }
            $('#cats-btn-' + i).addClass("active");
            let cat = myCats[i];
            let catGoods = myGoodsByCats[cat];
            let catGoodsCount = catGoods.length;
            for (let z = 1; z <= 10; z++) {
                $('#li-elem-topCats-' + z).css("display", "block");
            }

            for (let z = catGoodsCount + 1; z <= 10; z++) {
                $('#li-elem-topCats-' + z).css("display", "none");
            }

            for (let j = 1; j <= catGoodsCount; j++) {
                (myShop === "OKEY")
                    ? $('#li-pict-topCats-' + j).attr("src", "https://www.okeydostavka.ru" + catGoods[j - 1].pathToPicture)
                    : $('#li-pict-topCats-' + j).attr("src", catGoods[j - 1].pathToPicture.replace("?preset=thumbnail", ""));
                $('#li-name-topCats-' + j).text(catGoods[j - 1].name);
                $('#li-cat-topCats-' + j).text("Категория: " + getCategoryName(catGoods[j - 1].goodCategory));
                $('#li-weight-topCats-' + j).text("Вес/объем: " + getWeight(catGoods[j - 1].weight));
                $('#li-with-price-topCats-' + j).text("Цена со скидкой: " + catGoods[j - 1].price + " руб");
                $('#li-without-price-topCats-' + j).text("Цена без скидки: " + getPriceWithoutDiscount(catGoods[j - 1]) + " руб");
                $('#li-sale-topCats-' + j).text("-" + catGoods[j - 1].discount + "%");
            }

        });
    }
}

$('#btn-topTen').click(function () {
    clickTopTenBtn()
});

$('#btn-cats').click(function () {
    clickCatsBtn()
});