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
            let htmlContent = '';
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

            $(`.${where}`).html(htmlContent);
        }
    });
}

function handleYoutube(keyword){
    $.ajax({
        url: "/youtube/list",
        type: "GET",
        data: {
            keyword: keyword.trim()
        },
        success: function(response) {
            let htmlContent = '';

            response.forEach(function(info) {
                htmlContent += `
<div class="card h-25 text-white bg-dark me-3" style="max-width: 560px;">
    <div class="card-header">${info.title}</div>
    <div class="card-body d-flex justify-content-center">
        <div style="width: 100%; height: 315px; max-width: 560px;">
            <iframe width="100%" height="100%" src="//youtube.com/embed/${info.videoId}" frameborder="0" allowfullscreen></iframe>
        </div>
    </div>
    <div class="card-footer">
        <a style="color:white;" href="https://www.youtube.com/watch?v=${info.videoId}" target="_blank">Original</a>
    </div>
</div>`;
            });

            $('#youtubeSection').html(htmlContent);
        }
    });
}