$(document).ready(function() {
    // sidebar toggle
    const sidebarToggle = $('#sidebarToggle');
    if (sidebarToggle) {
        // if (localStorage.getItem('sb|sidebar-toggle') === 'true') {
        //     document.body.classList.toggle('sb-sidenav-toggled');
        // }
        sidebarToggle.click(function (event) {
            event.preventDefault();
            $(`body`).toggleClass('sb-sidenav-toggled');
            localStorage.setItem('sb|sidebar-toggle', $(`body`).hasClass('sb-sidenav-toggled'));
        })
    }

    $('.deleteAll').click(function() {
        if(confirm("휴지통을 비우시겠습니까?")) {
            location.href='/trash/deleteAll';
        }
    })

    $(`[id^='restoreBtn']`).click(function() {
        if(confirm("해당 피드글을 복원하시겠습니까?")) {
            $(`#formRestore${this.id.slice('restoreBtn'.length)}`).submit();
        }
    })

    $(`[id^='deleteBtn']`).click(function() {
        if(confirm("해당 피드글을 삭제하시겠습니까?")) {
            $(`#formDelete${this.id.slice('deleteBtn'.length)}`).submit();
        }
    })

    $(`[id^='more']`).click(function () {
        console.log(this);
        const feedId = this.id.slice('more'.length);
        const content = $(`.feedText${feedId}`);

        let data;

        if (content.text().length > 100) {
            data = {
                isShort: true,  //짧은거 주셈
                feedId: feedId
            }
        } else {
            data = {
                isShort: false,  //긴거 주셈
                feedId: feedId
            }
        }

        $.ajax({
            type: "POST",
            url: "/community/shortContent",
            data: JSON.stringify(data),
            contentType: "application/json; charset=utf-8",
        }).done(response => {
            content.text(response);
            $(this).children(':first-child').text(data['isShort'] ? 'more...' : 'less...')
        })
    })

    ;(function() {
        $(`[id^='cmtBtn']`).each(function() {
            loadCommentByFeedId(this.id.slice('cmtBtn'.length));
        })
    })();

})

function loadCommentByFeedId(feedId) {
    $.ajax({
        url: "/comment/list?feedId=" + feedId,
        type: "GET",
        cache: false,
        success: function (data, status) {
            if (status === "success") {
                if (data.status !== "OK") {
                    console.log(data.status);
                    return;
                }
                buildComment(data, feedId);
            }
        }
    })
}

function buildComment(result, feedId) {
    $(`#cmt_cnt${feedId}`).text(result.count);

    const out = [];

    result.data.forEach(comment => {
        // let commentId = comment.commentId;
        let content = comment.commentContent.trim();
        let regdate = comment.regDate;

        // let user_id = comment.user.id;
        let nickname = comment.user.name;

        let row;
        row = `
            <div class="mb-1">
                <span style="font-size: 0.9em""><strong>${nickname}</strong>  <small>${regdate}</small></span>
                <br>
                <span style="font-size: 0.8em">${content}</span>
            </div>
        `
        out.push(row);
        out.push(...buildReply(comment.replyList));
    });

    $(`#commentBody${feedId}`).html(out.join("\n"));
}

function buildReply(replyList) {
    const out = [];
    replyList.forEach(reply => {
        // let replyId = reply.commentId;
        let content = reply.commentContent.trim();
        let regdate = reply.regDate;

        // let user_id = reply.user.id;
        let nickname = reply.user.name;

        let row = `
            <div class="ms-4 mb-1">
                <span style="font-size: 0.9em""><strong>${nickname}</strong>  <small>${regdate}</small></span>
                <br>
                <span style="font-size: 0.8em">${content}</span>
            </div>
        `;

        out.push(row);
    })
    return out;
}


