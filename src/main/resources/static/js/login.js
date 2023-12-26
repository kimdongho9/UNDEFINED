window.addEventListener('DOMContentLoaded', async () => {
    var confirm_number;
    const target = document.querySelector('#welcome');

    function blink() {
        target.classList.toggle("cursorEffect");
    }

    const blinkInterval = setInterval(blink, 200);

    const str = "Welcome to Undefined!!";
    const arr = str.split("");

    // sleep 함수 구현
    function sleep(msec) {
        return new Promise(resolve => setTimeout(resolve, msec));
    }

    async function printWelcome(arr) {
        if (arr.length > 0) {
            target.textContent += arr.shift();
            await sleep(40);
            printWelcome(arr);
        } else {
            clearInterval(blinkInterval);
            target.classList.remove("cursorEffect");
            callLoginOption();
        }
    }
    if ($('.Kakao input[name="provider"]').val()) {

        $('.Kakao').removeClass('visually-hidden');
        $('#commandRoot').text('C:₩Undefined₩Login₩KakaoLogion₩EmailVerify');

        await forKakao();
    }else{
        printWelcome(arr);
    }


    // print Login Option
    const loginOptionStr = [
        "--- Choose one for Login ---",
        "1. user",
        "2. kakao",
        "3. google",
        "4. naver",
        "5. register",
        "Input your choice : "
    ];

    async function callLoginOption() {
        document.querySelector('#commandRoot').innerHTML = 'C:&#8361;Undefined&#8361;Login&#8361;Select&#8361;Options >';
        for (let x in loginOptionStr) {
            let loginText = document.querySelector(`#loginText${x}`);
            loginText.textContent = "";
            for (let c of loginOptionStr[x]) {
                loginText.textContent += c;
                await sleep(20);
            }
        }
        const inputOption = document.getElementById('loginOption');
        inputOption.classList.remove("hidden");
        inputOption.focus();
        inputOption.addEventListener('keyup', async function (event) {
            if (event.key == `Enter`) {
                inputOption.removeEventListener('keyup', arguments.callee);
                switch (inputOption.value) {
                    case '1': {  // user login
                        document.querySelector('#commandRoot').innerHTML = 'C:&#8361;Undefined&#8361;Login&#8361;User >';
                        inputOption.value = '';
                        inputUserId();
                        break;
                    }
                    case '2': {

                        let loginText = document.querySelector(`#oAuthOption`);
                        loginText.textContent = "";
                        for (let c of "Kakao Login oAuth Processing...") {
                            loginText.textContent += c;
                            await sleep(20);
                        }
                        inputOption.value = '';
                        $("#Kakao").get(0).click();
                        break;
                    }
                    case '3': {
                        let loginText = document.querySelector(`#oAuthOption`);
                        loginText.textContent = "";
                        for (let c of "Google Login oAuth Processing...") {
                            loginText.textContent += c;
                            await sleep(20);
                        }
                        inputOption.value = '';
                        window.location.href = '/oauth2/authorization/google';
                        break;

                    }
                    case '4': {
                        let loginText = document.querySelector(`#oAuthOption`);
                        loginText.textContent = "";
                        for (let c of "Naver Login oAuth Processing...") {
                            loginText.textContent += c;
                            await sleep(20);
                        }
                        inputOption.value = '';
                        window.location.href = '/oauth2/authorization/naver';
                        break;

                    }
                    case '5': {
                        document.querySelector('#commandRoot').innerHTML = 'C:&#8361;Undefined&#8361;Login&#8361;Register >';
                        inputOption.value = '';
                        register();
                        break;

                    }
                    default: {
                        wrongOption();
                    }
                }
            }
        })
    }


    async function wrongOption() {
        const dangerInterval = setInterval(divDanger, 200);
        setTimeout(() => {
            clearInterval(dangerInterval);
            document.querySelector('.cardBody').classList.remove('border-danger');
        }, 1800)

        const wrongOption = document.getElementById('wrongOption');
        wrongOption.textContent = "";
        for (let c of "Wrong Option. Choose the right option.") {
            wrongOption.textContent += c;
            await sleep(15);
        }
        await sleep(1000);

        // 초기화2
        resetOption();
        callLoginOption();
    }


    async function inputUserId() {
        document.querySelector('.selectLoginOption').classList.add('visually-hidden')
        document.querySelector('.userInput').classList.remove('visually-hidden')


        // input user id
        let userText0 = document.querySelector(`#userText0`);
        userText0.textContent = "";
        for (let c of "Input your User Name : ") {
            userText0.textContent += c;
            await sleep(20);
        }
        const userInput = document.querySelector('.userInput input[name="username"]');
        userInput.classList.remove("hidden");
        userInput.focus();

        userInput.addEventListener('keyup', async function (event) {
            if (event.key == `Enter`) {
                userInput.removeEventListener('keyup', arguments.callee);
                if (userInput.value) inputUserPw();
                else {
                    userText0.textContent = "";
                    for (let c of "Type User Name Correctly: ") {
                        userText0.textContent += c;
                        await sleep(20);
                    }
                }
            } else if (event.key == `Escape` || (event.ctrlKey && event.key == 'c')) {
                userInput.removeEventListener('keyup', arguments.callee);
                document.querySelector('.userInput').classList.add('visually-hidden');
                userText0.textContent = "";
                userInput.value = "";
                userInput.classList.add("hidden");

                resetOption();
                document.querySelector('.selectLoginOption').classList.remove('visually-hidden')
                callLoginOption();
            }
        })
    }

    function resetOption() {
        const wrongOption = document.getElementById('wrongOption');
        wrongOption.textContent = "";
        for (let x = 0; x < loginOptionStr.length; x++) {
            document.querySelector(`#loginText${x}`).textContent = "";
        }
        document.getElementById('loginOption').value = "";
        document.getElementById('loginOption').classList.add("hidden");
    }

    async function inputUserPw() {
        let userText1 = document.querySelector(`#userText1`);

        userText1.textContent = "";
        for (let c of "Input your password : ") {
            userText1.textContent += c;
            await sleep(20);
        }

        const userPwInput = document.querySelector('.userInput input[name="password"]');
        userPwInput.classList.remove("hidden");
        userPwInput.focus();

        userPwInput.addEventListener('keyup', function (event) {
            if (event.key == 'Enter') {
                userPwInput.removeEventListener('keyup', arguments.callee);
                //ajax로 로그인 검사 하기
                    $.ajax({
                        url: "/user/login",
                        type: "POST",
                        data:{
                            username: $('.userInput input[name="username"]').val(),
                            password: $('.userInput input[name="password"]').val()
                        },
                        success: async function (data, b, c) {
                            if (data == "Failed") {
                                failUserLogin();

                            } else {
                                let successText = document.querySelector(`#successText`);

                                successText.textContent = "";
                                for (let c of "Now You Are Loggined!") {
                                    successText.textContent += c;
                                    await sleep(20);
                                }
                                await sleep(1000);

                                // reset
                                successText.textContent = "";
                                document.querySelector(`#userText1`).textContent = "";
                                document.querySelector(`#userText0`).textContent = "";

                                const userId = document.querySelector('.userInput input[name="username"]');
                                userId.value = "";
                                userId.classList.add("hidden");

                                const userPw = document.querySelector('.userInput input[name="password"]');
                                userPw.value = "";
                                userPw.classList.add("hidden");


                                location.href = "/";
                            }
                        },
                        });




            } else if (event.key == `Escape` || (event.ctrlKey && event.key == 'c')) {
                userPwInput.removeEventListener('keyup', arguments.callee);
                document.querySelector('.userInput').classList.add('visually-hidden');
                document.querySelector(`#userText0`).textContent = "";
                document.querySelector('.userInput input[name="username"]').value = "";
                document.querySelector('.userInput input[name="username"]').classList.add("hidden");
                userText1.textContent = "";
                userPwInput.value = "";
                userPwInput.classList.add("hidden");

                resetOption();
                document.querySelector('.selectLoginOption').classList.remove('visually-hidden')
                callLoginOption();
            }
        })
    }

    async function failUserLogin() {
        let failText = document.querySelector(`#failText`);

        const dangerInterval = setInterval(divDanger, 200);
        setTimeout(() => {
            clearInterval(dangerInterval);
            document.querySelector('.cardBody').classList.remove('border-danger');
        }, 2000)

        failText.textContent = "";
        for (let c of "Wrong user id or password. Retry Input.") {
            failText.textContent += c;
            await sleep(20);
        }
        await sleep(1000);

        // reset
        failText.textContent = "";
        document.querySelector(`#userText1`).textContent = "";
        document.querySelector(`#userText0`).textContent = "";

        const userId = document.querySelector('.userInput input[name="username"]');
        userId.value = "";
        userId.classList.add("hidden");

        const userPw = document.querySelector('.userInput input[name="password"]');
        userPw.value = "";
        userPw.classList.add("hidden");

        // inputUserId
        inputUserId();
    }

    function divDanger() {
        const loginWindow = document.querySelector('.cardBody');
        loginWindow.classList.toggle("border-danger");
    }

    function register() {
        document.querySelector('.selectLoginOption').classList.add('visually-hidden');
        document.querySelector('.register').classList.remove('visually-hidden');
        registerId();
    }

    async function registerId() {
        const rid = document.getElementById('registerText0');
        rid.textContent = '';

        for (let c of "Input your User Name : ") {
            rid.textContent += c;
            await sleep(20);
        }

        const registerId = document.querySelector('.register input[name="username"]');
        registerId.classList.remove('hidden');
        registerId.focus();

        registerId.addEventListener('keyup', function (event) {
            if (event.key == `Enter`) {
                registerId.removeEventListener('keyup', arguments.callee);
                // 중복
                var id = registerId.value;
                $.ajax({
                    type: "GET",
                    url: "/user/isDuplicateUsername",
                    data: {id: id},
                }).done(async (response) => { // 중복되면 1
                    result = response;
                    if (result) {  // 중복된 아이디
                        duplicateId();
                    } else {  // 패스워드 입력창으로
                        registerId.disabled = true;
                        const successId = document.getElementById('availableId');
                        successId.classList.remove('visually-hidden');
                        successId.textContent = '';

                        for (let c of "This Id is available.") {
                            successId.textContent += c;
                            await sleep(20);
                        }
                        registerName();
                    }
                });
            } else if (event.key == `Escape` || (event.ctrlKey && event.key == 'c')) {
                registerId.removeEventListener('keyup', arguments.callee);

                resetRegister();
                resetOption();
                document.querySelector('.selectLoginOption').classList.remove('visually-hidden')
                callLoginOption();
            }
        });

    }

    // 아이디 중복 검사 메소드
    // true 면 중복
    // false 면 사용가능

    async function duplicateId() {
        const dId = document.getElementById('duplicateId');
        dId.classList.remove('visually-hidden');
        dId.textContent = '';

        for (let c of "This UserName has been already used.") {
            dId.textContent += c;
            await sleep(20);
        }

        await sleep(1000);

        dId.classList.add('visually-hidden');
        dId.textContent = '';

        resetRegister();
        register();
    }

    async function registerName() {

        const rid = document.getElementById('registerText01');
        rid.textContent = '';

        for (let c of "Input your NickName for Undefined : ") {
            rid.textContent += c;
            await sleep(20);
        }

        const registerName = document.querySelector('.register input[name="name"]');
        registerName.classList.remove('hidden');
        registerName.focus();

        registerName.addEventListener('keyup', function (event) {
            if (event.key == `Enter`) {
                registerName.removeEventListener('keyup', arguments.callee);
                // 중복
                var name = $('.register input[name="name"]').val();
                $.ajax({
                    type: "GET",
                    url: "/user/isDuplicateName",
                    data: {
                        name: name
                    }
                }).done((response) => { // 중복되면 1
                    if (response == 1) {  // 중복된 이름
                        duplicateName();
                    } else {  // 패스워드 입력창으로
                        registerName.disabled = true;
                        registerPw();
                    }
                });

            } else if (event.key == `Escape` || (event.ctrlKey && event.key == 'c')) {
                registerName.removeEventListener('keyup', arguments.callee);

                resetRegister();
                resetOption();
                document.querySelector('.selectLoginOption').classList.remove('visually-hidden')
                callLoginOption();
            }
        });

    }

    async function duplicateName() {
        const dId = document.getElementById('duplicateName');
        dId.classList.remove('visually-hidden');
        dId.textContent = '';

        for (let c of "This Name has been already used.") {
            dId.textContent += c;
            await sleep(20);
        }

        await sleep(1000);

        dId.classList.add('visually-hidden');
        dId.textContent = '';

        registerNameFail();
    }

    function registerNameFail() {
        const rpw = document.getElementById('registerText01');
        const registerName1 = document.querySelector('.register input[name="name"]');
        rpw.textContent = "";
        registerName1.value = "";
        registerName1.classList.add("hidden");

        registerName();
    }


    async function registerPw() {
        const successId = document.getElementById('availableName');
        successId.classList.remove('visually-hidden');
        successId.textContent = '';

        for (let c of "This Name is available.") {
            successId.textContent += c;
            await sleep(20);
        }

        const rpw = document.getElementById('registerText1');
        rpw.textContent = '';

        for (let c of "Input your Password : ") {
            rpw.textContent += c;
            await sleep(20);
        }

        const registerPw = document.querySelector('.register input[name="password"]');
        registerPw.classList.remove('hidden');
        registerPw.focus();

        registerPw.addEventListener('keyup', async function (event) {
            if (event.key == `Enter`) {
                registerPw.removeEventListener('keyup', arguments.callee);
                registerPw.disabled = true;
                // 패스워드 재입력창
                registerPwAgain();

            } else if (event.key == `Escape` || (event.ctrlKey && event.key == 'c')) {
                registerPw.removeEventListener('keyup', arguments.callee);

                resetRegister();
                resetOption();
                document.querySelector('.selectLoginOption').classList.remove('visually-hidden')
                callLoginOption();
            }
        })
    }

    async function registerPwAgain() {
        const rpwa = document.getElementById('registerText2');
        rpwa.textContent = '';

        for (let c of "Input your Password Again : ") {
            rpwa.textContent += c;
            await sleep(20);
        }

        const registerPwAgain = document.querySelector('.register input[name="re_password"]');
        registerPwAgain.classList.remove('hidden');
        registerPwAgain.focus();

        registerPwAgain.addEventListener('keyup', function (event) {
            const im = isMatch();
            if (event.key == `Enter`) {
                registerPwAgain.removeEventListener('keyup', arguments.callee);
                if (im) {
                    registerPwAgain.disabled = true;
                    registerEmail();

                } else {
                    registerPWAgainFail();
                }
            } else if (event.key == `Escape` || (event.ctrlKey && event.key == 'c')) {
                registerPwAgain.removeEventListener('keyup', arguments.callee);
                resetRegister();
                resetOption();
                document.querySelector('.selectLoginOption').classList.remove('visually-hidden');
                callLoginOption();
            }
        });

    }

    function registerPWAgainFail() {
        const notMatch = document.getElementById('notMatchPw');
        const match = document.getElementById('matchPw');
        notMatch.classList.add('visually-hidden');
        match.classList.add('visually-hidden');


        const rpw = document.getElementById('registerText1');
        const registerPw1 = document.querySelector('.register input[name="password"]');
        rpw.textContent = "";
        registerPw1.value = "";
        registerPw1.classList.add("hidden");

        const rpwa = document.getElementById('registerText2');
        const registerPwAgain = document.querySelector('.register input[name="re_password"]');
        rpwa.textContent = "";
        registerPwAgain.value = "";
        registerPwAgain.classList.add("hidden");
        registerPw();
    }


    async function registerEmail() {
        const rgemail = document.getElementById('registerText3');
        rgemail.textContent = '';
        for (let c of "Input your Email : ") {
            rgemail.textContent += c;
            await sleep(20);
        }

        const registerEmail = document.querySelector('.register input[name="email"]');
        registerEmail.classList.remove('hidden');
        registerEmail.focus();
        confirm_number = Math.floor(Math.random() * 1000000);
        console.log(confirm_number);

        registerEmail.addEventListener('keyup', async function (event) {
            if (event.key == `Enter`) {
                registerEmail.removeEventListener('keyup', arguments.callee);
                registerEmail.disabled = true;
                await Emailverify();
            } else if (event.key == `Escape` || (event.ctrlKey && event.key == 'c')) {
                registerEmail.removeEventListener('keyup', arguments.callee);
                resetRegister();
                resetOption();
                document.querySelector('.selectLoginOption').classList.remove('visually-hidden');
                callLoginOption();
            }

        });

    }

    function isMatchCode() {

        const code = document.querySelector('.register input[name="verify"]').value;

        const notMatch = document.getElementById('notMatchCode');
        const match = document.getElementById('matchCode');

        if (code == '') {
            notMatch.classList.add('visually-hidden');
            match.classList.add('visually-hidden');
            return false;
        } else {
            if (code == confirm_number) {
                notMatch.classList.add('visually-hidden');
                match.classList.remove('visually-hidden');
                return true;
            } else {
                notMatch.classList.remove('visually-hidden');
                match.classList.add('visually-hidden');
                return false;
            }
        }
    }

    async function Emailverify() {
        console.log('verify시작');
        const regVerify = document.getElementById('registerText4');
        regVerify.textContent = '';
        for (let c of "Enter your Verify Code : ") {
            regVerify.textContent += c;
            await sleep(20);
        }

        $.ajax({
            url: "/verify",
            type: "post",
            data: {
                address: $(".register input[name='email']").val().trim(),
                title: "Thanks For Registering UNDEFINED! Please Confirm Your Security Code!",
                message: '<h1>Thanks For Registering UNDEFINED!</h1>' +
                    '<p>Please Confirm Your Security Code:</p>' +
                    '<h2>' + confirm_number + '</h2>'
            }
        });


        const verifyinput = document.querySelector('.register input[name="verify"]');
        verifyinput.classList.remove('hidden');
        verifyinput.focus();
        verifyinput.addEventListener('keyup', function (event) {
            const imc = isMatchCode();
            if (event.key == `Enter`) {
                if (imc) {
                    verifyinput.removeEventListener('keyup', arguments.callee);

                    var username = $('.register input[name="username"]').val();
                    var name = $('.register input[name="name"]').val();
                    var password = $('.register input[name="password"]').val();
                    var email = $('.register input[name="email"]').val();

                    $.ajax({
                        url: "/user/register",
                        type: "POST",
                        data: {
                            username: username,
                            name: name,
                            password: password,
                            email: email,
                        },
                        success: function (data) {
                            console.log('성공');
                            location.href = "/user/login";
                        }
                    });
                } else {
                    console.log(confirm_number);
                    console.log(`wrong!`);
                }

            } else if (event.key == `Escape` || (event.ctrlKey && event.key == 'c')) {
                verifyinput.removeEventListener('keyup', arguments.callee);
                resetRegister();
                resetOption();
                document.querySelector('.selectLoginOption').classList.remove('visually-hidden');
                callLoginOption();
            }

        });


    }


    function isMatch() {
        const originPw = document.querySelector('.register input[name="password"]').value;
        const againPw = document.querySelector('.register input[name="re_password"]').value;

        const notMatch = document.getElementById('notMatchPw');
        const match = document.getElementById('matchPw');

        if (againPw === '') {
            notMatch.classList.add('visually-hidden');
            match.classList.add('visually-hidden');
            return false;
        } else {
            if (originPw === againPw) {
                notMatch.classList.add('visually-hidden');
                match.classList.remove('visually-hidden');
                return true;
            } else {
                notMatch.classList.remove('visually-hidden');
                match.classList.add('visually-hidden');
                return false;
            }
        }
    }

    function resetRegister() {
        document.querySelector('.register').classList.add('visually-hidden');
        $("input").prop('disabled', false);
        const registerId = document.querySelector('.register input[name="username"]');
        const rid = document.getElementById('registerText0');
        rid.textContent = "";
        registerId.value = "";
        registerId.classList.add("hidden");
        $('#registerText01').text('');
        $('#availableName').text('');
        $('#duplicateName').text('');
        const registerName = document.querySelector('.register input[name="name"]');
        const rname = document.getElementById('registerText0');
        rname.textContent = "";
        if (registerName.value != null) registerName.value = "";
        registerName.classList.add("hidden");

        const successId = document.getElementById('availableId');
        successId.classList.add('visually-hidden');
        successId.textContent = '';

        const dId = document.getElementById('duplicateId');
        dId.classList.add('visually-hidden');
        dId.textContent = '';

        const notMatch = document.getElementById('notMatchPw');
        const match = document.getElementById('matchPw');
        notMatch.classList.add('visually-hidden');
        match.classList.add('visually-hidden');


        const rpw = document.getElementById('registerText1');
        const registerPw = document.querySelector('.register input[name="password"]');
        rpw.textContent = "";
        registerPw.value = "";
        registerPw.classList.add("hidden");

        const rpwa = document.getElementById('registerText2');
        const registerPwAgain = document.querySelector('.register input[name="re_password"]');
        rpwa.textContent = "";
        if (registerPwAgain.value != null) registerPwAgain.value = "";
        registerPwAgain.classList.add("hidden");
        $('#registerText3').text('');
        $('input[name="email"]').val('');
        $('input[name="email"]').addClass('hidden');
        $('#registerText4').text('');
        $('#verify').val('');
        $('#verify').addClass('hidden');
    }


    async function forKakao() {
        const kakao = document.getElementById('registerText5');
        kakao.textContent = '';
        for (let c of "Input Your Email : ") {
            kakao.textContent += c;
            await sleep(20);
        }
        const registerEmail = document.querySelector('.Kakao input[name="email"]');
        registerEmail.classList.remove('visually-hidden');
        registerEmail.focus();
        confirm_number = Math.floor(Math.random() * 1000000);
        console.log(confirm_number);

        registerEmail.addEventListener('keyup', async function (event) {
            if (event.key == `Enter`) {
                registerEmail.removeEventListener('keyup', arguments.callee);
                registerEmail.disabled = true;
                await EmailVerifyKakao();
            } else if (event.key == `Escape` || (event.ctrlKey && event.key == 'c')) {
                registerEmail.removeEventListener('keyup', arguments.callee);
                resetRegister();
                resetOption();
                document.querySelector('.selectLoginOption').classList.remove('visually-hidden');
                callLoginOption();
            }


        });

    }

    async function EmailVerifyKakao() {
        const regVerify = document.getElementById('registerText6');
        regVerify.textContent = '';
        for (let c of "Enter your Verify Code : ") {
            regVerify.textContent += c;
            await sleep(20);
        }

        $.ajax({
            url: "/verify",
            type: "POST",
            data: {
                address: $(".Kakao input[name='email']").val().trim(),
                title: "Thanks For Registering UNDEFINED! Please Confirm Your Security Code!",
                message: '<h1>Thanks For Registering UNDEFINED!</h1>' +
                    '<p>Please Confirm Your Security Code:</p>' +
                    '<h2>' + confirm_number + '</h2>'
            }
        });


        const verifyinput = document.querySelector('.Kakao input[name="verify"]');
        verifyinput.classList.remove('visually-hidden');
        verifyinput.focus();
        verifyinput.addEventListener('keyup', function (event) {
            const imck = isMatchCodeKakao();
            if (event.key == `Enter`) {
                if (imck) {
                    verifyinput.removeEventListener('keyup', arguments.callee);

                    var username = $('.Kakao input[name="username"]').val();
                    var name = $('.Kakao input[name="name"]').val();
                    var password = $('.Kakao input[name="password"]').val();
                    var email = $('.Kakao input[name="email"]').val();
                    var provider = $('.Kakao input[name="provider"]').val();
                    var providerId = $('.Kakao input[name="providerId"]').val();

                    $.ajax({
                        url: "/oauth2/register",
                        type: "POST",
                        data: {
                            username: username,
                            name: name,
                            password: password,
                            email: email,
                            provider: provider,
                            providerId: providerId
                        },
                        success: async function (data) {
                            let loginText = document.querySelector(`#KakaoNotice`);
                            loginText.textContent = "";
                            for (let c of "You Are Loggined IN!! directing to the Main Page...") {
                                loginText.textContent += c;
                                await sleep(20);
                            }
                            location.href = "/";
                        }
                    })
                } else {
                    console.log(confirm_number);
                    console.log(`wrong!`);
                }

            } else if (event.key == `Escape` || (event.ctrlKey && event.key == 'c')) {
                verifyinput.removeEventListener('keyup', arguments.callee);
                resetRegister();
                resetOption();
                document.querySelector('.selectLoginOption').classList.remove('visually-hidden');
                callLoginOption();
            }

        });

        function isMatchCodeKakao() {

            const code = document.querySelector('.Kakao input[name="verify"]').value;

            const notMatch = document.getElementById('notMatchKakao');
            const match = document.getElementById('matchKakao');

            if (code == '') {
                notMatch.classList.add('visually-hidden');
                match.classList.add('visually-hidden');
                return false;
            } else {
                if (code == confirm_number) {
                    notMatch.classList.add('visually-hidden');
                    match.classList.remove('visually-hidden');
                    return true;
                } else {
                    notMatch.classList.remove('visually-hidden');
                    match.classList.add('visually-hidden');
                    return false;
                }
            }
        }


    }

});