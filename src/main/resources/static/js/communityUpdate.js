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
        if(confirm("해당 피드를 수정(게시)하시겠습니까??")){
            setDelList();
            $('.writeForm').submit();
        }
    })

    $('.tempBtn').click(function() {
        $('#feedState').attr('value', 'temp');
        setDelList();
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

    // Example Usage
    const imagePaths = [];
    const filenames = [];

    console.log(flist);
    console.log(typeof flist);

    flist.forEach(file => {
        imagePaths.push(`/upload/${file.filename}`);
        filenames.push(file.filename);
    })

    const dataTransfer = new DataTransfer();
    loadImagesFromFilepaths(imagePaths, filenames, dataTransfer, function (files) {
        console.log('Files:', files);

        $('#formFile')[0].files = dataTransfer.files;
        const out = [];
        const originList = {};
        Array.from($('#formFile')[0].files)
            .forEach((file, idx) => {
                originList[file.name] = file;
                out.push( `
                <div class="d-flex justify-content-between mb-0">
                    <p>${file.name} (${Math.round(file.size / 1024)} KB)</p>
                    <span class="text-danger cursor-pointer" data-file-id="${flist[idx].id}" data-file-name="${file.name}"><i class="fa-solid fa-x"></i></span>
                </div>
                `);
            })

        $('#fileList').html($('#fileList').html() + out.join('\n'));
        addDelete($('#formFile'));
        setOriginFileList(originList);

    });

    $('#formFile').change(function() {
        showFileList(this);
    })
})

const fileList = {};

function setOriginFileList(originList) {
    Object.assign(fileList, originList);
}

// 추가된 파일
const newFileList = {}
function showFileList(obj) {
    const dataTransfer1 = new DataTransfer();
    const dataTransfer2 = new DataTransfer();
    const out = [];

    Array.from($(obj)[0].files)
        .filter(file => fileList[file.name] === undefined)
        .forEach(file => {
            fileList[file.name] = file;
            newFileList[file.name] = file;
            out.push( `
                <div class="d-flex justify-content-between mb-0">
                    <p>${file.name} (${Math.round(file.size / 1024)} KB)</p>
                    <span class="text-danger cursor-pointer" data-file-id="0" data-file-name="${file.name}"><i class="fa-solid fa-x"></i></span>
                </div>
            `);
        })

    for(const file in fileList) {
        dataTransfer1.items.add(fileList[file]);
    }

    for(const file in newFileList) {
        dataTransfer2.items.add(newFileList[file]);
    }

    $(obj)[0].files = dataTransfer1.files;
    $(obj).prev()[0].files = dataTransfer2.files;

    $('#fileList').html($('#fileList').html() + out.join('\n'));
    addDelete(obj);
}

const deleteId = [];
function addDelete(obj) {
    $('[data-file-name]').click(function() {
        const fileName = $(this).attr('data-file-name');
        const fileId = $(this).attr('data-file-id');

        if(fileId !== '0') {
            deleteId.push(Number(fileId));
        }

        delete fileList[fileName];
        delete newFileList[fileName];

        const dataTransfer1 = new DataTransfer();

        Array.from($(obj)[0].files)
            .filter(file => file.name !== fileName)
            .forEach(file => {
                dataTransfer1.items.add(file);
            })

        const dataTransfer2 = new DataTransfer();
        Array.from($(obj).prev()[0].files)
            .filter(file => file.name !== fileName)
            .forEach(file => {
                dataTransfer2.items.add(file);
            })

        $(obj).prev()[0].files = dataTransfer2.files;
        $(obj)[0].files = dataTransfer1.files;
        $(this).parent().remove();

    })
}

function loadImagesFromFilepaths(filepaths, filenames, dataTransfer, callback) {
    const loadedFiles = [];

    function loadImage(filepath, filename, totalFiles) {
        const img = new Image();

        img.onload = function () {
            // Create a canvas to draw the image
            const canvas = document.createElement('canvas');
            const ctx = canvas.getContext('2d');
            canvas.width = img.width;
            canvas.height = img.height;
            ctx.drawImage(img, 0, 0, img.width, img.height);

            // Convert the canvas content to a Blob
            canvas.toBlob(function (blob) {
                // Create a File object from the Blob
                const file = new File([blob], filename);

                // Add the File object to the DataTransfer object
                dataTransfer.items.add(file);

                // Add the File object to the loadedFiles array
                loadedFiles.push(file);

                // If all files are loaded, execute the callback
                if (loadedFiles.length === totalFiles) {
                    callback(loadedFiles);
                }
            });
        };

        img.onerror = function (error) {
            console.error('Error loading image:', error);
            // Handle the error if needed
        };

        img.src = filepath;
    }

    // Load each image in the array
    filepaths.forEach((filepath, index) => {
        loadImage(filepath, filenames[index], filepaths.length);
    });
}

function setDelList() {
    $('#deleteList').val(deleteId);
}
