window.addEventListener('DOMContentLoaded', event => {

    // Toggle the side navigation
    const sidebarToggle = document.body.querySelector('#sidebarToggle');
    if (sidebarToggle) {
        sidebarToggle.addEventListener('click', event => {
            event.preventDefault();
            document.body.classList.toggle('sb-sidenav-toggled');
            localStorage.setItem('sb|sidebar-toggle', document.body.classList.contains('sb-sidenav-toggled'));
        });
    }
});



// chat join code
$(document).ready(function(){
    $("#JointoChat").click(function () {
        var url = $("#URLjoin").val().trim();
        if(url != null) window.open(url,"_blank");
        $("#URLjoin").val("");
    });

    $("#URLjoin").focus(function() {
        $(this).keypress(function (e) {
            if (e.which === 13) {
                var url = $(this).val().trim();
                if (url !== "") {
                    window.open(url, "_blank");
                    $(this).val("");
                }
            }
        });
    });
});

// chatbot code
$(document).ready(function () {
    $('.answeringSign').hide();
    $.ajax({
        url: `/bot/loadchat`, // Replace with your Spring controller endpoint
        type: 'GET', // Use GET request
        success: function (response) {
            response.forEach((e) => {
                e.isFromChat ?
                    $("#chatbox").append(`<div class="GPTanswer mt-3">${e.context}</div>`)
                    :
                    $("#chatbox").append(`<div class="userask mt-3">${e.context}</div>`)
                ;
            });
        },
        error: function (xhr, status, error) {
            // Handle errors (if needed)
            console.error('Error:', error);
        }
    });

    $('#toggleBtn').click(function(){
        $('#chatCard').slideToggle(300);
        $("#chatbox").scrollTop($("#chatbox")[0].scrollHeight);
    });




    $('#send-btn').click(function (event) {
        var message = $('#message-input').val();
        message = message.replaceAll("[^a-zA-Z0-9:/._-]", "");
        if(message != '') {
            $('#message-input').val('');
            $("#chatbox").append(`<div class="userask mt-3">${message}</div>`);
            $("#chatbox").scrollTop($("#chatbox")[0].scrollHeight);
            $('.answeringSign').show();
            // Send the value to the server using AJAX

            $.ajax({
                url: `/bot/chat?prompt=${message}`, // Replace with your Spring controller endpoint
                type: 'GET', // Use GET request
                success: function (response) {
                    $("#chatbox").append(`<div class="GPTanswer mt-3">${response}</div>`);
                    $('.answeringSign').hide();
                    $("#chatbox").scrollTop($("#chatbox")[0].scrollHeight);
                },
                error: function (xhr, status, error) {
                    // Handle errors (if needed)
                    console.error('Error:', error);
                }
            });
        }
    });

    $('#message-input').on('keyup', function(e) {
        if (e.which === 13) {
            $('#send-btn').click();
        }
    });
});