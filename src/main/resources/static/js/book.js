function likeBook(button) {
    var bookCard = $(button).closest('.card');
    var book = {
        title: bookCard.data('title'),
        author: bookCard.data('author'),
        publisher: bookCard.data('publisher'),
        image: bookCard.data('image'),
        description: bookCard.data('description'),
        link: bookCard.data('link'),
        pubdate: bookCard.data('pubdate'),
        discount: bookCard.data('discount'),
        isbn: bookCard.data('isbn')
    };
    $.ajax({
        url: "/user/booklikes",
        type: "POST",
        data: book,
        success: function (response) {
            console.log('success');
        }
    });
    $(button).addClass('visually-hidden');
    $(button).siblings('.btn-unlike').removeClass('visually-hidden');

}


function unlikeBook(button) {
    var bookCard = $(button).closest('.card');
    var book = {
        title: bookCard.data('title'),
        author: bookCard.data('author'),
        publisher: bookCard.data('publisher'),
        image: bookCard.data('image'),
        description: bookCard.data('description'),
        link: bookCard.data('link'),
        pubdate: bookCard.data('pubdate'),
        discount: bookCard.data('discount'),
        isbn: bookCard.data('isbn')
    };
    $.ajax({
        url: "/user/bookunlikes",
        type: "POST",
        data: book,
        success: function (response) {
            console.log('success');
        }
    });

    $(button).addClass('visually-hidden');
    $(button).siblings('.btn-like').removeClass('visually-hidden');

}

function FindBooks() {

    var searchQuery = $('#eventSearch').val();
    console.log(searchQuery);
    $.ajax({
        url: "/naver/books",
        type: "GET",
        data: {
            keyword: searchQuery.trim()
        },
        success: function (response) {

            if (response && response.length > 0) {
                var booksInfo = $('#booksinfo');

                const likeBtn
                    =
                    isLogged
                        ?
                        `<div class="btn-group mt-2" role="group">
                            <button onclick="likeBook(this)" class="btn btn-outline-primary btn-like">좋아요</button>
                            <button onclick="unlikeBook(this)" class="btn btn-outline-danger btn-unlike visually-hidden">좋아요 취소</button>
                        </div>`
                        : '';

                response.forEach(function (book) {

                    var bookDiv = `
                        <div class="card col-11 col-sm-5 col-xl-3 my-2 mx-1 px-0" data-isbn="${book.isbn}" data-title="${book.title}" data-author="${book.author}" data-publisher="${book.publisher}" data-image="${book.image}" data-description="${book.description}" data-link="${book.link}" data-pubdate="${book.pubdate}" data-discount="${book.discount}">
                            <h5 class="card-header">${book.title}</h5>
                            <div class="card-body">
                                <span class="card-title">${book.author}</span>
                                <small class="card-subtitle text-muted">${book.publisher}</small>
                                <br>
                                <img src="${book.image}" alt="no image" height="300" width="100%" style="object-fit: cover">
                            </div>
                            <div class="card-body">
                                <p class="card-text">${book.description}</p>
                            </div>
                            <div class="card-body">
                                <a href="${book.link}" class="card-link text-success" target="_blank">To Naver Shopping Link</a>
                            </div>
                            <div class="card-footer text-muted">
                                PUBLISHED DATE : ${book.pubdate}
                                <p class="float-end">PRICE : ₩${book.discount}</p>
                                ${likeBtn}
                            </div>
                        </div>
                        `;


                    booksInfo.append(bookDiv);
                });
            } else {

                $('#booksinfo').html('No books found.');
            }
        },
        error: function (xhr, status, error) {

            console.error(status + ": " + error);
        }
    });
}
