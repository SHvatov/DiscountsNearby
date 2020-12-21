$(document).ready(function () {
    switchButtons(2);
    $('#div-topTen').css("display", "none");
    $('#div-cats').css("display", "none");
});

let myShop;
let myCats;
let myGoods;


let initDataPage = function (data) {
    myShop = data.shop;
    myCats = data.categories;
    myGoods = data.goods;
    let shopName = myShop === "LENTA"
        ? "Лента"
        : "Окей";
    $("#nav-btn-2").text("Магазины " + shopName);
}

$('#btn-topTen').click(function () {
    $('#div-topTen').css("display", "block");
    $('#div-cats').css("display", "none");
    $('#btn-topTen').addClass("active");
    $('#btn-cats').removeClass("active");
});

$('#btn-cats').click(function () {
    $('#div-topTen').css("display", "none");
    $('#div-cats').css("display", "block");
    $('#btn-topTen').removeClass("active");
    $('#btn-cats').addClass("active");
});