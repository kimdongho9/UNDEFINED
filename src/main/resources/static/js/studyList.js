//클릭 했을 때 디테일로
$(document).ready(function(){



    $("[id^='detail'] .favorite:nth-child(2)").hide();

    $("[id^='detail']").each(function () {
        var postId = this.id.slice("detail".length);
        console.log(postId, logged_id);

            isLike(logged_id, postId)
                .then(response => {
                    if (response) {
                        //console.log('ㅇㅇ');
                        $("[id^='detail" + postId + "'] .favorite:nth-child(1)").hide();
                        $("[id^='detail" + postId + "'] .favorite:nth-child(2)").show();
                    } else {
                        //console.log('ㄴㄴ');
                        $("[id^='detail" + postId + "'] .favorite:nth-child(2)").hide();
                    }
                })
    });

    async function isLike(userid, postid) {
        let result ;
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

    $("[id^='detail']").click(function(){
        // 저거 타임리프로 id = "detail 1 " id="detail2"
        let postId = this.id.slice("detail".length);
        location.href = `/study/detail/${postId}`;
    });

    $("[id^='detail'] .favorite").off('click').click(function(e){
        e.stopPropagation();
        if(!logged_id){
            alert('로그인이 필요한 서비스입니다.');
            return;
        }
        $(this).siblings(".favorite").show();
        $(this).hide();

        let postid = $(this).parents("[data-id]").attr("data-id");

        if($(this).attr("data-favorite") === "on"){
            $.ajax({
                url: "/study/Delfavor",
                type: "POST",
                data: {
                    userid: logged_id,
                    postid: postid
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
                    postid: postid
                },
                success: function (response) {
                    result = response;
                }
            });
        }
    });

    //페이징
    $(function (){
        $("[name='pageRows']").change(function(){
            let frm = $("[name = 'frmPageRows']");
            frm.attr("method","POST")
            frm.attr("action","pageRows")
            frm.submit();
        })
    });

    $('#favorList').click(function(){
        // 가지고 있으면 즐겨찾기 리스트를 보여주고 있는 상태
        if(logged_id === 0) {
            alert("로그인이 되어있지 않습니다.")
            return;
        }

        if($(this).hasClass('favBtn')) {
            $(this).text("즐겨찾기");
            $('#skillForFavor').val($('#skillSearch').siblings('a').text());
            $('#favor').val(false);
            $('#favorFrm').submit();
        } else { // 전체 리스트일 때 즐찾을 클릭
            $(this).text("전체");
            $('#skillForFavor').val($('#skillSearch').siblings('a').text());
            $('#favor').val(true);
            $('#favorFrm').submit();
        }
        $(this).toggleClass('favBtn');
    });

    $('#skillSearch .dropdown-item').each(function(idx, elem) {
        $(elem).click(function() {
            $('#skillInput').val($(this).text());
            $('#skillFrm').submit();
        })
    })

});


