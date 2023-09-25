async function loadWeatherData(hotelId){

    let response = await fetch('/jsonWeather?hotelId='+hotelId, {method:'get'});
    let json = await response.json();
    console.log(json);
    let tlabel = "Temperature";
    let wlabel = "Wind Speed"
    document.getElementById("tlabel").innerHTML = tlabel;
    document.getElementById("wlabel").innerHTML = wlabel;

    let weather = json.temperature;
    let windSpeed =json.windspeed;

    document.getElementById("temperature").innerHTML = weather;
    document.getElementById("windspeed").innerHTML = windSpeed;


}