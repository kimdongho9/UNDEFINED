function convertDateFormat(inputDate) {
    const date = new Date(inputDate);
    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, '0');
    const day = String(date.getDate()).padStart(2, '0');
    const hours = String(date.getHours()).padStart(2, '0');
    const minutes = String(date.getMinutes()).padStart(2, '0');

    const formattedDate = `${year}-${month}-${day} ${hours}:${minutes}`;
    return formattedDate;
}

function handlePagination(keyword, start, where){
    $.ajax({
        url: "/naver/news",
        type: "GET",
        data: {
            keyword: keyword,
            start: start
        },
        success: function(response) {
            let htmlContent = ''; // Variable to store HTML content

            // Loop through the response data and generate HTML for each item
            response.forEach(function(info) {
                var date = convertDateFormat(info.pubDate);
                htmlContent += `<tr class="table-primary text-center align-middle">
                                    <td>${info.title}</td>
                                    <td>${info.description}</td>
                                    <td>${date}</td>
                                    <td>
                                        <a href="${info.originallink}" target="_blank" class="card-link">Original</a>
                                        <br>
                                        <a href="${info.link}" target="_blank" class="card-link text-success">Naver</a>
                                    </td>
                                </tr>`;
            });

            // Replace the content of 'infoTable' with the generated HTML
            $(`.${where}`).html(htmlContent);
        }
    });
}