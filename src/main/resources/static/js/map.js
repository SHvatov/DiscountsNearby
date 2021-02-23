ymaps.ready(init);

const api = "8f3a3023-ff31-419d-9ea8-1a55f0894184";
let radius = 2500;
let lentaGoods;
let okeyGoods;
let us;


function initHomePage(data) {
    lentaGoods = data.lentaGoods;
    okeyGoods = data.okeyGoods;
    if (data.user && data.user.preferences)
        radius = data.user.preferences.searchRadius;
    us = data.user;
}

function init() {
    let shopPlacemarks = [], myPlacemark, myMap = new ymaps.Map("map", {
        center: [55.76, 37.64],
        zoom: 7,
        controls: []
    }, {
        searchControlProvider: 'yandex#search'
    });

    let mapHeight = $('#map').height();
    $('#shop-list').height(mapHeight);
    $('#shop-list-placeholder').hide();

    function addPlacemark(coords) {
        if (myPlacemark) {
            myPlacemark.geometry.setCoordinates(coords);
        } else {
            myPlacemark = createPlacemark(coords, 'поиск...', 'islands#violetDotIconWithCaption');
            myMap.geoObjects.add(myPlacemark);

            myPlacemark.events.add('dragend', function () {
                getAddress(myPlacemark.geometry.getCoordinates());
            });
        }
        getAddress(coords);
    }

    myMap.events.add('click', function (e) {
        let coords = e.get('coords');
        addPlacemark(coords);
    });

    function createPlacemark(coords, iconCaption, preset) {
        return new ymaps.Placemark(coords, {
            iconCaption: iconCaption
        }, {
            preset: preset,
            draggable: true
        });
    }

    function getAddress(coords) {
        myPlacemark.properties.set('iconCaption', 'поиск...');
        ymaps.geocode(coords).then(function (res) {
            let firstGeoObject = res.geoObjects.get(0);

            myPlacemark.properties
                .set({
                    iconCaption: [
                        firstGeoObject.getLocalities().length ? firstGeoObject.getLocalities() : firstGeoObject.getAdministrativeAreas(),
                        firstGeoObject.getThoroughfare() || firstGeoObject.getPremise()
                    ].filter(Boolean).join(', '),
                    balloonContent: firstGeoObject.getAddressLine()
                });
            $('#location-input').val(firstGeoObject.getAddressLine());
        });
    }

    function getShops(coords) {
        $('#shop-list').empty();
        let circle = new ymaps.Circle([coords, radius], {}, {
            geodesic: true
        });
        myMap.geoObjects.add(circle);
        let bounds = circle.geometry._bounds;
        myMap.setBounds(bounds, {
            checkZoomRange: true
        });
        myMap.panTo(coords, {
            delay: 1500
        });
        myMap.geoObjects.remove(circle);
        let searchBounds = myMap.getBounds();
        for (let i = 0; i < shopPlacemarks.length; i++) {
            myMap.geoObjects.remove(shopPlacemarks[i]);
        }
        shopPlacemarks = [];
        $.getJSON("https://search-maps.yandex.ru/v1/?text=ЛЕНТА&bbox=" + searchBounds[0].reverse() + "~" + searchBounds[1].reverse() + "&rspn=1type=biz&lang=ru_RU&apikey=" + api, function (data) {
            let circle = new ymaps.Circle([coords, radius], {}, {
                geodesic: true
            });
            myMap.geoObjects.add(circle);
            let adr = [];
            for (let i = 0; i < data.features.length; i++) {
                if (!adr.includes(data.features[i].properties.CompanyMetaData.address)) {
                    adr.push(data.features[i].properties.CompanyMetaData.address);
                    let placemark = createPlacemark(data.features[i].geometry.coordinates.reverse(), 'Лента', 'islands#redIcon');
                    shopPlacemarks.push(placemark);
                    let addedPlacemark = ymaps.geoQuery(placemark).addToMap(myMap);
                    let objectsInsideCircle = addedPlacemark.searchInside(circle);
                    if (objectsInsideCircle._objects.length > 0) {
                        objectsInsideCircle.setOptions('preset', 'islands#redIcon');
                        $('#shop-list').append('<li class="list-group-item">\n' +
                            '                    <p class="h3">Лента</p>\n' +
                            '                    <p id="len1_' + i + '"></p>\n' +
                            '                    <p id="len2_' + i + '"></p>\n' +
                            '                    <p id="len3_' + i + '"></p>\n' +
                            '                    <p>Топ скидок на товары:</p>\n' +
                            '                    <ul id="lenGoods' + i + '">\n' +
                            '                    </ul>\n' +
                            '                    <p></p>\n' +
                            '                    <a type="button" id="len_btn_' + i + '" class="btn btn-info" href="">Выбрать</a>\n' +
                            '                </li>\n'
                        );
                        $('#len1_' + i).text("Адрес: " + data.features[i].properties.CompanyMetaData.address);
                        if (data.features[i].properties.CompanyMetaData.Hours)
                            $('#len2_' + i).text("Режим работы: " + data.features[i].properties.CompanyMetaData.Hours.text);
                        else
                            $('#len2_' + i).text("Режим работы: ежедневно, круглосуточно");
                        $('#len3_' + i).text("Расстояние до магазина: " + (ymaps.coordSystem.geo.getDistance(coords, data.features[i].geometry.coordinates) / 1000).toFixed(2) + " км");
                        for (let j = 0; j < lentaGoods.length; j++) {
                            $('#lenGoods' + i).append('<li id="lenGoodsItem' + i + j + '"></li>');
                            $('#lenGoodsItem' + i + j).text(lentaGoods[j].name + " - " + lentaGoods[j].price + " руб. - " + lentaGoods[j].discount + "%");
                        }
                        if (us) {
                            document.getElementById("len_btn_" + i).href = "/api/supermarkets/LENTA/" + us.id;
                        } else {
                            document.getElementById("len_btn_" + i).href = "/api/supermarkets/LENTA/0";
                        }

                    }
                    addedPlacemark.remove(objectsInsideCircle).removeFromMap(myMap);
                }

            }
            myMap.geoObjects.remove(circle);
        });
        $.getJSON("https://search-maps.yandex.ru/v1/?text=ОКЕЙ&bbox=" + searchBounds[0] + "~" + searchBounds[1] + "&rspn=1type=biz&lang=ru_RU&apikey=" + api, function (data) {
            let circle = new ymaps.Circle([coords, radius], {}, {
                geodesic: true
            });
            myMap.geoObjects.add(circle);
            let adr = [];
            for (let i = 0; i < data.features.length; i++) {
                if (!adr.includes(data.features[i].properties.CompanyMetaData.address)) {
                    adr.push(data.features[i].properties.CompanyMetaData.address);

                    let placemark = createPlacemark(data.features[i].geometry.coordinates.reverse(), 'О\'кей', 'islands#redIcon');
                    shopPlacemarks.push(placemark);
                    let addedPlacemark = ymaps.geoQuery(placemark).addToMap(myMap);
                    let objectsInsideCircle = addedPlacemark.searchInside(circle);

                    if (objectsInsideCircle._objects.length > 0) {
                        objectsInsideCircle.setOptions('preset', 'islands#redIcon');
                        $('#shop-list').append('<li class="list-group-item">\n' +
                            '                    <p class="h3">Окей</p>\n' +
                            '                    <p id="ok1_' + i + '"></p>\n' +
                            '                    <p id="ok2_' + i + '"></p>\n' +
                            '                    <p id="ok3_' + i + '"></p>\n' +
                            '                    <p>Топ скидок на товары:</p>\n' +
                            '                    <ul id="okGoods' + i + '">\n' +
                            '                    </ul>\n' +
                            '                    <p></p>\n' +
                            '                    <a type="button" id="ok_btn_' + i + '" class="btn btn-info" href="">Выбрать</a>\n' +
                            '                </li>\n' +
                            '                <script th:inline="javascript">\n' +
                            '                   function getOkeyInfo() {\n' +
                            '                       alert("Нажатие на кнопку Окея");\n' +
                            '                   }\n' +
                            '                </script>\n'
                        );
                        $('#ok1_' + i).text("Адрес: " + data.features[i].properties.CompanyMetaData.address);
                        if (data.features[i].properties.CompanyMetaData.Hours)
                            $('#ok2_' + i).text("Режим работы: " + data.features[i].properties.CompanyMetaData.Hours.text);
                        else
                            $('#ok2_' + i).text("Режим работы: ежедневно, круглосуточно");
                        $('#ok3_' + i).text("Расстояние до магазина: " + (ymaps.coordSystem.geo.getDistance(coords, data.features[i].geometry.coordinates) / 1000).toFixed(2) + " км");

                        for (let j = 0; j < okeyGoods.length; j++) {
                            $('#okGoods' + i).append('<li id="okGoodsItem' + i + j + '"></li>');
                            $('#okGoodsItem' + i + j).text(okeyGoods[j].name + " - " + okeyGoods[j].price + " руб. - " + okeyGoods[j].discount + "%");
                        }
                        if (us) {
                            document.getElementById("ok_btn_" + i).href = "/api/supermarkets/OKEY/" + us.id;
                        } else {
                            document.getElementById("ok_btn_" + i).href = "/api/supermarkets/OKEY/0";
                        }

                    }
                    addedPlacemark.remove(objectsInsideCircle).removeFromMap(myMap);
                }
            }
            myMap.geoObjects.remove(circle);
        });
        $('#shop-list-placeholder').show();
    }

    $('#location-btn').click(function () {
        ymaps.geocode($('#location-input').val(), {
            results: 1
        }).then(function (res) {
                if (!res.geoObjects.get(0)) {
                    showAlert("Неверный адрес, проверьте корректность ввода адреса!", "alert-warning");
                } else {
                    let firstGeoObject = res.geoObjects.get(0),

                        coords = firstGeoObject.geometry.getCoordinates(),
                        bounds = firstGeoObject.properties.get('boundedBy');
                    addPlacemark(coords);
                    getShops(coords);
                }
            },
            function (err) {
                showAlert("Произошла ошибка при обращении к API карт. Пожалуйста, перезагрузите страницу.", "alert-danger");
            });

    });

    function showAlert(message, alerttype) {
        $('#alert-placeholder').append('<div id="alertdiv" class="alert ' + alerttype + '"><a class="close" data-dismiss="alert">×</a><span>' + message + '</span></div>')
        setTimeout(function () {
            $("#alertdiv").remove();
        }, 5000);
    }
}