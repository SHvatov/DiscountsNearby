ymaps.ready(init);
function init() {
    let myPlacemark, myMap = new ymaps.Map("map", {
        center: [55.76, 37.64],
        zoom: 7,
        controls: []
    }, {
        searchControlProvider: 'yandex#search'
    });

    function addPlacemark(coords) {
        if (myPlacemark) {
            myPlacemark.geometry.setCoordinates(coords);
        } else {
            myPlacemark = createPlacemark(coords);
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

    function createPlacemark(coords) {
        return new ymaps.Placemark(coords, {
            iconCaption: 'поиск...'
        }, {
            preset: 'islands#violetDotIconWithCaption',
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

    $('#location-btn').click(function () {
        ymaps.geocode($('#location-input').val(), {
            results: 1
        }).then(function (res) {
                console.log(res.geoObjects.get(0));
                if (!res.geoObjects.get(0)) {
                    console.log("errodfofdl");
                    showAlert("Неверный адрес, проверьте корректность ввода адреса!", "alert-warning");
                } else {
                    let firstGeoObject = res.geoObjects.get(0),

                        coords = firstGeoObject.geometry.getCoordinates(),
                        bounds = firstGeoObject.properties.get('boundedBy');
                    addPlacemark(coords);
                    myMap.setBounds(bounds, {
                        checkZoomRange: true
                    });
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