$(document).ready(function () {

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

    // search button
    const searchBtn = $('#searchSubmit');
    searchBtn.click(function (event) {
        $('#searchForm').submit();
    })

    $(`[id^="likeBtn"]`).click(function () {
        if(logged_id === 0) return;  //modal 창을 띄워줄까?
        // console.log(this);
        const feedId = this.id.slice('likeBtn'.length);
        // console.log(feedId);
        let data;
        if ($(this).hasClass('like')) { // 좋아요를 누른 상태
            data = {
                isLike: false,
                feedId: feedId,
                userId: logged_id
            }
        } else {
            data = {
                isLike: true,
                feedId: feedId,
                userId: logged_id
            }
        }

        $.ajax({
            type: "POST",
            url: "/rest/like",
            data: data,
        }).done((response) => {
            $(this).find('.likeIcon').toggleClass('visually-hidden');
            $(this).toggleClass("like");
            $(`#likeCnt` + feedId).text(response);
        });
    })

    $(`[id^='addComment']`).click(function () {
        const feedId = this.id.slice('addComment'.length);
        if ($(`#isReply${feedId}`).hasClass('visually-hidden')) {
            $(`#commentFrm${feedId}`).submit();
        }
    })

    $(`[id^='cancelComment']`).click(function () {
        const feedId = this.id.slice('addComment'.length);
        $(`#placehold${feedId}`).value('');
    })

    $(`[id^="editBtn"]`).click(function () {
        const feedId = this.id.slice('editBtn'.length);
        $(`#formEdit${feedId}`).submit();
    })

    $(`[id^="deleteBtn"]`).click(function () {
        const feedId = this.id.slice('deleteBtn'.length);
        if (confirm("해당 피드글을 휴지통으로 보내시겠습니까??")) {
            $(`#formDelete${feedId}`).submit();
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

    $(`[id^='commentDelBtn']`).click(function () {
        const feedId = this.id.slice('commentDelBtn'.length);
        $(`#commentDel${feedId}`).submit();
    })

    $(`[id^='replyDelBtn']`).click(function () {
        const feedId = this.id.slice('replyDelBtn'.length);
        $(`#replyDel${feedId}`).submit();
    })

    ;(function() {
        $(`[id^='cmtBtn']`).each(function() {
            const feedId = this.id.slice('cmtBtn'.length);
            loadCommentByFeedId(feedId);
        })
    })();

    $(`[id^='btn_comment']`).click(function() {

        const feedId = this.id.slice('btn_comment'.length);
        let data;
        if(!$(`#isReply${feedId}`).hasClass('visually-hidden')) { // 댓글
            data = {
                "feedId": feedId,
                "content": $(this).siblings('input').val(),
                "userId": logged_id,
                "parentId": $(`#isReply${feedId}`).attr('data-parent-id')
            }
        } else {
            data = {
                "feedId": feedId,
                "content": $(this).siblings('input').val(),
                "userId": logged_id,
            }
        }

        $.ajax({
            url: "/comment/write",
            type: "POST",
            cache: false,
            data: data,
            success: function (data, status) {
                if(status === 'success') {
                    if (data.status !== 'OK') {
                        console.log(data.status);
                        return ;
                    }
                    $(`#input_comment${feedId}`).val('');
                    loadCommentByFeedId(feedId);
                }
            }
        })
    })
})

function loadCommentByFeedId(feedId) {
    $.ajax({
        url: "/comment/list?feedId=" + feedId + "&userId=" + logged_id,
        type: "GET",
        cache: false,
        success: function (data, status) {
            if (status === "success") {
                if (data.status !== "OK") {
                    console.log(data.status);
                    return;
                }
                buildComment(data, feedId);
                addDelete(feedId);
                addReply(feedId);
            }
        }
    })
}

function buildComment(result, feedId) {
    $(`#cmt_cnt${feedId}`).text(result.count);

    const out = [];

    result.data.forEach(comment => {
        let commentId = comment.commentId;
        let content = comment.commentContent.trim();
        let regdate = comment.regDate;

        let user_id = comment.user.id;
        let nickname = comment.user.name;

        // 삭제버튼 여부: 작성자 본인인 경우만 삭제 버튼 보이게 하기
        // 일단 user1
        const delBtn = (user_id !== logged_id) ? '' : `
            <span  data-cmtdel-id="${commentId}">
                <i class="btn fa-solid fa-delete-left text-danger" data-bs-toggle="tooltip"
                               title="삭제"></i>
            </span>
        `;

        // 대댓글 여부
        const replyBtn = (logged_id === 0) ? '' : `
            <span sec:authorize="isAuthenticated()" class="cursor-pointer text-info" data-parent-id="${commentId}" data-reply-nickname="${nickname}" data-reply-content="${content}">
                답급달기
            </span>
        `

        let row;

        row = `
            <div class="mb-1">
                <span style="font-size: 0.9em""><strong>${nickname}</strong>  <small>${regdate}</small>  <small>${replyBtn}</small>   ${delBtn}</span>
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
        let replyId = reply.commentId;
        let content = reply.commentContent.trim();
        let regdate = reply.regDate;

        let user_id = reply.user.id;
        let nickname = reply.user.name;

        // 삭제버튼 여부: 작성자 본인인 경우만 삭제 버튼 보이게 하기
        // 일단 user1
        const delBtn = (user_id !== logged_id) ? '' : `
            <span data-cmtdel-id="${replyId}">
                <i class="btn fa-solid fa-delete-left text-danger" data-bs-toggle="tooltip"
                               title="삭제"></i>
            </span>
        `;

        let row = `
            <div class="ms-4 mb-1">
                <span style="font-size: 0.9em""><strong>${nickname}</strong>  <small>${regdate}</small>${delBtn}</span>
                <br>
                <span style="font-size: 0.8em">${content}</span>
            </div>
        `;

        out.push(row);
    })
    return out;
}

function addDelete(feedId) {
    $("[data-cmtdel-id]").click(function(e) {
        e.preventDefault();
        if(!confirm("댓글을 삭제하시겠습니까?")) return;

        const commentId = $(this).attr("data-cmtdel-id");

        $.ajax({
            url: "/comment/delete",
            type: "POST",
            cache: false,
            data: {"commentId" : commentId},
            success: function(data, status) {
                if(status === "success") {
                    if(data.status !== "OK") {
                        console.log(data.status);
                        return ;
                    }
                    loadCommentByFeedId(feedId);
                }
            }
        })
    })
}

function addReply(feedId) {
    $('[data-reply-nickname]').click(function() {
        const nickname = $(this).attr('data-reply-nickname');
        let content = $(this).attr('data-reply-content');

        content = content.length > 10 ? content.split(10) + '...' : content;

        const $isReply = $(`#isReply${feedId}`);
        $isReply.removeClass('visually-hidden');
        $isReply.find('.col-11').text(`${nickname} : ${content} 에 대한 답글을 작성 중...`)
        $isReply.attr('data-parent-id', $(this).attr('data-parent-id'));
        $(`#input_comment${feedId}`).attr('placeholder', '답글을 입력하시오.');
    })

    $(`[id^='cancelReply']`).click(function() {
        $(this).parent('div').addClass('visually-hidden');
        $(`#input_comment${feedId}`).attr('placeholder', '댓글을 입력하시오.');
    })
}
