$(function () {

    for(var skill of arr) {
        const idx = skill.skillName.indexOf('.');
        const skillName = idx !== -1 ? skill.skillName.slice(0, idx) : skill.skillName;

        $(`#${skillName}`).addClass('visually-hidden');
        const delBtn = `
            <span class="ms-2 cursor-pointer"><i class="fa-solid fa-x"></i></span>
        `
        const inputSelctHtml = `
            <nav class="badge rounded-pill bg-secondary ms-0 me-1 align-self-center delOption my-0" style="border: 1px solid;" data-option-name=${skill.skillName}>
                ${skill.skillName}
                ${delBtn}
            </nav>
        `
        $('#selectedSkillList').html($('#selectedSkillList').html() + inputSelctHtml);

        addDelete();
        checkAllOption();
    }

    $('.updateComp').click(function (){
        var skills = '';
        for(var x of $('.delOption')){
            skills += x.innerText;
        }
        //console.log(skills);
        $('input[name="skills"]').val(skills.trim());
        $('form[name="sanggon"]').submit();
    });




    $('.datepicker').datepicker({
        format: 'yyyy-mm-dd', // 연/월/일 형식으로 설정
        language: 'ko',
        width: 100
    });




    $('.dropdown-item').click(function() {
        $(this).addClass("visually-hidden");
        const selectText = $(this).text();
        const delBtn = `
            <span class="ms-2 cursor-pointer"><i class="fa-solid fa-x"></i></span>
        `
        const inputSelctHtml = `
            <nav class="badge rounded-pill bg-secondary ms-0 me-1 align-self-center delOption my-0" style="border: 1px solid;" data-option-name=${selectText}>
                ${selectText}
                ${delBtn}
            </nav>
        `
        $('#selectedSkillList').html($('#selectedSkillList').html() + inputSelctHtml);

        addDelete();
        checkAllOption();
    });

});

function addDelete() {
    $('.delOption').click(function(e) {
        e.preventDefault();
        let optionName = $(this).attr('data-option-name');
        const idx = optionName.indexOf('.');
        optionName = optionName.slice(0, idx === -1 ? optionName.length : idx);

        $(`#${optionName}`).removeClass("visually-hidden");
        $(this).remove();
        checkAllOption();
    });
}

// 모든 메뉴를 선택하게 되면, 드롭다운 메뉴 disable
function checkAllOption() {
    // 드롭다운 아이템의 갯수와
    // 드롭다운 아이템의 걸려있는 visually-hidden 의 갯수를 비교
    // 두 갯수가 일치하면 disable
    // 일치하지 않으면 able
    const selectedCnt = $('#selectedSkillList').children('nav').length;
    const optionCnt = $('#skillList').children('div').length;

    if(selectedCnt === optionCnt) {
        $('#skillList').addClass('visually-hidden');
    } else {
        $('#skillList').removeClass('visually-hidden');
    }
}
