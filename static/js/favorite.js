async function addFavorite(hotelId, hotelName){
    let response = await fetch('/insertFavorites?hotelId='+hotelId+"&hotelName="+hotelName, {method:'get'});
    let text = await response.text();
       let original = "";
       let duplicate = "";
    if(text.trim() == 'false'){
        original = "<div class=\"alert alert-success\" role=\"alert\">Added to favorites</div>"
    }else{
         duplicate = "<div class=\"alert alert-danger\"role=\"alert\">Already added as favorite</div>"
    }
    document.getElementById("original").innerHTML = original;

    document.getElementById("duplicate").innerHTML = duplicate;

    console.log(text);
}