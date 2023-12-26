$(document).ready(function () {

    isLike(logged_id, post_id)
        .then(response => {
            if (response) {
                $(".favorite:nth-child(1)").addClass('visually-hidden');
            } else {
                $(".favorite:nth-child(2)").addClass('visually-hidden');
            }
        })

    $("#btnDel").click(function (event) {
        event.preventDefault();
        let answer = confirm("삭제하시겠습니까?");
        if (answer) {
            $("form[name='frmDelete']").submit();
        }
    });


    $(".favorite").off().click(function (e) {
        e.stopPropagation();

        $(this).siblings('span').removeClass('visually-hidden');
        $(this).addClass('visually-hidden');

        if ($(this).attr("data-favorite") === "on") {
            $.ajax({
                url: "/study/Delfavor",
                type: "POST",
                data: {
                    userid: logged_id,
                    postid: post_id
                },
                success: function (response) {
                    result = response;
                }
            });

        } else {
            $.ajax({
                url: "/study/Savefavor",
                type: "POST",
                data: {
                    userid: logged_id,
                    postid: post_id
                },
                success: function (response) {
                    result = response;
                }
            });
        }

    });



});

async function isLike(userid, postid) {
    let result;
    await $.ajax({
        url: "/study/favorbyuserid",
        type: "GET",
        data: {
            userid: userid,
            postid: postid
        },
        success: function (response) {
            result = response;
        }
    });
    return result;
}
