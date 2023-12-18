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

    $('.compBtn').click(function() {
        $('#feedState').attr('value', 'comp');
        $('.writeForm').submit();
    })

    $('.tempBtn').click(function() {
        $('#feedState').attr('value', 'temp');
        $('.writeForm').submit();
    })

    $('.cancelBtn').click(function() {
        history.back();
    })

    $('#formFile').change(function() {
        showFileList(this);
    })

    $('#feedTitle').keyup(function() {
        if($(this).val().length > 20) {
            $(this).val($(this).val().substring(0, 20));
        }
        $('#titleWordCnt').text($(this).val().length);
    })

    $('#feedContent').keyup(function() {
        if($(this).val().length > 500) {
            $(this).val($(this).val().substring(0, 500));
        }

        $('#contentWordCnt').text($(this).val().length);
    })

})

const fileList = {};

function showFileList(obj) {
    const dataTransfer = new DataTransfer();
    const out = [];

    Array.from($(obj)[0].files)
        .filter(file => fileList[file.name] === undefined)
        .forEach(file => {
            fileList[file.name] = file;
            out.push( `
                <div class="d-flex justify-content-between mb-0">
                    <p>${file.name} (${Math.round(file.size / 1024)} KB)</p>
                    <span class="text-danger cursor-pointer" data-file-name="${file.name}"><i class="fa-solid fa-x"></i></span>
                </div>
            `);
    })

    for(const file in fileList) {
        dataTransfer.items.add(fileList[file]);
    }

    $(obj)[0].files = dataTransfer.files;

    $('#fileList').html($('#fileList').html() + out.join('\n'));
    addDelete(obj);
}

function addDelete(obj) {
    $('[data-file-name]').click(function() {
        const fileName = $(this).attr('data-file-name');
        delete fileList[fileName];

        const dataTransfer = new DataTransfer();

        Array.from($(obj)[0].files)
            .filter(file => file.name !== fileName)
            .forEach(file => {
                dataTransfer.items.add(file);
        })

        $(obj)[0].files = dataTransfer.files;
        $(this).parent().remove();

    })
}