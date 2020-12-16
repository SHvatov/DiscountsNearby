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
                objectsInsideCircle.setOptions('preset', 'islands#redIcon');
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
                objectsInsideCircle.setOptions('preset', 'islands#redIcon');
                addedPlacemark.remove(objectsInsideCircle).removeFromMap(myMap);
            }
            myMap.geoObjects.remove(circle);
        });
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