$(document).ready(function () {

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
})
