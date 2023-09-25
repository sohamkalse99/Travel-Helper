async function loadReviews(hotelId, username, isOnClick, isNext){
    console.log(isNext);
    let response = await fetch('/jsonReview?hotelId='+hotelId+'&isOnClick='+isOnClick+'&isNext='+isNext, {method:'get'});

    let json = await response.json();
    let jsonArray = json.reviews;
    console.log(json);

    let thead =
        "<tr><th>Review ID</th>"+
         "<th>Title</th>"+
          "<th>Text</th>"+
          "<th>Date</th>"+
          "<th>Edit/Delete</th>"+
          "</tr>";

    document.getElementById("thead").innerHTML = thead;
    let tbody = "";
    for(let i = 0;i<jsonArray.length;i++){
        tbody= tbody+
            "<tr><td>"+jsonArray[i].reviewId+"</td>"+
            "<td>"+jsonArray[i].title+"</td>"+
            "<td>"+jsonArray[i].reviewText+"</td>"+
            "<td>"+jsonArray[i].date+"</td>";
            if(jsonArray[i].user==username){
                tbody = tbody + "<td>"+
                 "<a href=/editReview?hotelId="+hotelId+"&reviewId="+jsonArray[i].reviewId+">Edit</a><br>"+
                 "<a href=/delete?hotelId="+hotelId+"&reviewId="+jsonArray[i].reviewId+">Delete</a>"
                 +"</td>";
            }
            tbody=tbody+ "</tr>";
    }
    document.getElementById("tbody").innerHTML = tbody;

    let nextButton = "";
    let previousButton = "";


    previousButton = "<button class=\"btn btn-primary mb-2\" onclick=\"loadReviews("+hotelId+",'"+username+"','"+false+"','"+false+"')\">previous</button>";
    document.getElementById("previousButton").innerHTML = previousButton;

    nextButton = "<button class=\"btn btn-primary mb-2\" onclick=\"loadReviews("+hotelId+",'"+username+"','"+false+"','"+true+"')\">next</button>";
    document.getElementById("nextButton").innerHTML = nextButton;

}