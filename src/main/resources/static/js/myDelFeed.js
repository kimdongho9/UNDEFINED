$(document).ready(function() {

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
})
