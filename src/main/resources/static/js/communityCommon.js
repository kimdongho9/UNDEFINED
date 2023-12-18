$(document).ready(function() {

    $(`[id^="img"]`).off().click(function() {
        const id = $(this).attr('data-feed-id');
        const size = Number($(this).attr('data-photo-cnt'));
        let idx = Number($(this).attr('data-photo-index'));

        let img = '';

        for(let i = 0; i < size; i++) {
            const src = $(`#mySwiper${id} img[data-photo-index='${idx}']`).attr('src');
            img += `
                <div class="swiper-slide modal-swiper-slide">
                    <img src="${src}" alt="">
                </div>
            `
            idx = idx + 1 >= size ? 0 : idx + 1;
        }

        $(`.modalImg${id}`).append(`
            <div class="modal">
                <span id="closeModal">&times;</span>
                <div class="modal-content">
                    <div class="swiper modal-swiper modalSwiper">
                        <div class="swiper-wrapper">
                          ${img}
                        </div>
                    </div>
                    <div class="swiper-button-next modalNext"></div>
                    <div class="swiper-button-prev modalPrev"></div>
                </div>
            </div>
        `);
        addCloseModal();
        initSwiper();
    })
})

function addCloseModal() {
    $(`#closeModal`).click(function() {
        $(this).parent().remove();
    })
}

function initSwiper() {
    var swiper = new Swiper(".modalSwiper", {
        slidesPerView: 1,
        spaceBetween: 30,
        loop: true,
        navigation: {
            nextEl: ".modalNext",
            prevEl: ".modalPrev",
        },
    });
}