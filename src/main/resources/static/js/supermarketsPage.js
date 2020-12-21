$(document).ready(function () {
    switchButtons(2);
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