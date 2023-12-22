$(document).ready(function () {
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

    $(`[id^='commentDelBtn']`).click(function () {
        const feedId = this.id.slice('commentDelBtn'.length);
        $(`#commentDel${feedId}`).submit();
    })

    $(`[id^='replyDelBtn']`).click(function () {
        const feedId = this.id.slice('replyDelBtn'.length);
        $(`#replyDel${feedId}`).submit();
    })
})