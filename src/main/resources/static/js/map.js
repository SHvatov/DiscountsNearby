const radius = 2500;
const api = "8f3a3023-ff31-419d-9ea8-1a55f0894184";

ymaps.ready(init);

function init() {
    let shopPlacemarks = [], myPlacemark, myMap = new ymaps.Map("map", {
        center: [55.76, 37.64],
        zoom: 7,
        controls: []
    }, {
        searchControlProvider: 'yandex#search'
    });

    function getLentaInfo() {
        alert("vjkvjfk");
    }

    function getOkeyInfo() {

    }

    let mapHeight = $('#map').height();
    $('#shop-list').height(mapHeight);
    $('#shop-list-placeholder').hide();

    function addMyPlacemark(coords) {
        if (myPlacemark) {
            myPlacemark.geometry.setCoordinates(coords);
        } else {
            myPlacemark = createMyPlacemark(coords);
            myMap.geoObjects.add(myPlacemark);

            myPlacemark.events.add('dragend', function () {
                getAddress(myPlacemark.geometry.getCoordinates());
            });
        }
        getAddress(coords);
    }

    myMap.events.add('click', function (e) {
        let coords = e.get('coords');
        addMyPlacemark(coords);
    });

    function createMyPlacemark(coords) {
        return new ymaps.Placemark(coords, {
            iconCaption: 'поиск...'
        }, {
            preset: 'islands#violetDotIconWithCaption',
            draggable: true
        });
    }

    function createShopPlacemark(coords, iconCaption) {
        return new ymaps.Placemark(coords, {
            iconCaption: iconCaption
        }, {
            preset: 'islands#redIcon',
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
            for (let i = 0; i < data.features.length; i++) {
                let placemark = createShopPlacemark(data.features[i].geometry.coordinates.reverse(), 'Лента');
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
                        '                    <button type="button" class="btn btn-info" onclick="getLentaInfo()">Выбрать</button>\n' +
                        '                </li>'
                    );
                    $('#len1_' + i).text("Адрес: " + data.features[i].properties.CompanyMetaData.address);
                    if (data.features[i].properties.CompanyMetaData.Hours)
                        $('#len2_' + i).text("Режим работы: " + data.features[i].properties.CompanyMetaData.Hours.text);
                    else
                        $('#len2_' + i).text("Режим работы: ежедневно, круглосуточно");
                    $('#len3_' + i).text("Расстояние до магазина: " + radius / 1000 + " км");
                    $.ajax({
                        url: 'http://localhost:3030/supermarkets/allCategoriesData',
                        type: 'POST',
                        data: {
                            supermarketCode: "LENTA",
                            elementsToFetch: 5,
                            discountOnly: true
                        },
                        success: function (d) {
                            for (let j = 0; j < d.length; j++) {
                                $('#lenGoods' + i).append('<li id="lenGoodsItem' + i + j + '"></li>');
                                $('#lenGoodsItem' + i + j).text(d[j].name + " - " + d[j].price + " руб. - " + d[j].discount + "%");
                            }
                        }
                    });
                }
                addedPlacemark.remove(objectsInsideCircle).removeFromMap(myMap);
            }
            myMap.geoObjects.remove(circle);
        });
        $.getJSON("https://search-maps.yandex.ru/v1/?text=ОКЕЙ&bbox=" + searchBounds[0] + "~" + searchBounds[1] + "&rspn=1type=biz&lang=ru_RU&apikey=" + api, function (data) {
            let circle = new ymaps.Circle([coords, radius], {}, {
                geodesic: true
            });
            myMap.geoObjects.add(circle);
            for (let i = 0; i < data.features.length; i++) {
                let placemark = createShopPlacemark(data.features[i].geometry.coordinates.reverse(), 'О\'кей');
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
                        '                    <button type="button" class="btn btn-info" onclick="getOkeyInfo()">Выбрать</button>\n' +
                        '                </li>'
                    );
                    $('#ok1_' + i).text("Адрес: " + data.features[i].properties.CompanyMetaData.address);
                    if (data.features[i].properties.CompanyMetaData.Hours)
                        $('#ok2_' + i).text("Режим работы: " + data.features[i].properties.CompanyMetaData.Hours.text);
                    else
                        $('#ok2_' + i).text("Режим работы: ежедневно, круглосуточно");
                    $('#ok3_' + i).text("Расстояние до магазина: " + radius / 1000 + " км");
                    $.ajax({
                        url: 'http://localhost:3030/supermarkets/allCategoriesData',
                        type: 'POST',
                        data: {
                            supermarketCode: "OKEY",
                            elementsToFetch: 5,
                            discountOnly: true
                        },
                        success: function (d) {
                            for (let j = 0; j < d.length; j++) {
                                $('#okGoods' + i).append('<li id="okGoodsItem' + i + j + '"></li>');
                                $('#okGoodsItem' + i + j).text(d[j].name + " - " + d[j].price + " руб. - " + d[j].discount + "%");
                            }
                        }
                    });
                }
                addedPlacemark.remove(objectsInsideCircle).removeFromMap(myMap);
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
                    addMyPlacemark(coords);
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