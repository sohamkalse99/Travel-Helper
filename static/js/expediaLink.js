async function storeLink(link){
    let response = await fetch('/insertLinks?link='+link, {method:'get'});
    let text = await response.text();
    console.log(text);
}