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

    $('.deleteAll').click(function() {
        if(confirm("임시저장글을 모두 삭제하시겠습니까? ( 복원 불가능 )")) {
            location.href='/temp/deleteAll';
        }
    })


    $(`[id^="editBtn"]`).click(function () {
        const feedId = this.id.slice('editBtn'.length);
        $(`#formEdit${feedId}`).submit();
    })

    $(`[id^="deleteBtn"]`).click(function () {
        const feedId = this.id.slice('deleteBtn'.length);
        if (confirm("해당 피드글을 삭제하시겠습니까?")) {
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


})
