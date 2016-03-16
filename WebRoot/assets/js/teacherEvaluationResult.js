/**
 * Created by S.F.S on 2016/3/6.
 */
document.write("<script src='assets/js/configure.js'></script>");
$(function () {










    /**
     *
     * 滑动评价1
     *
     */

    var lineLeft1 = parseFloat($('#line1').css('margin-left'));
    var oSlider1 = document.getElementById('btn1');
    var oMarkBar1 = document.getElementById('line1');
    var oMin1 = document.getElementById('min1');
    var oMax1 = document.getElementById('max1');

    var markBarWidth1 = parseFloat(oMarkBar1.offsetWidth);
    var percent1 = markBarWidth1 / 100;
    oMin1.style.left = lineLeft1 - 15 + 'px';
    oMax1.style.left = lineLeft1 + markBarWidth1 + 20 + 'px';


    oSlider1.addEventListener("touchstart", touchStart1, true);
    function touchStart1(event1) {
        //console.log(event.target);//event.target 总是指向第一次点击的对象，输出oSlider对象
        //console.log(event.currentTarget); //event.currentTarget总是指向当前的对象，输出oSlider对象
        var thisSlider1 = event1.target;
        thisSlider1.addEventListener("touchmove", touchMove1, true);
    }
    function touchMove1(event1) {
        //console.log(event.target);//event.target 总是指向第一次点击的对象，输出oSlider对象
        //console.log(event.currentTarget); //event.currentTarget总是指向当前的对象，输出document对象
        var touch1 = event1.touches[0];
        var moveX1 = touch1.clientX;
        $('#progress1').css('width',moveX1-lineLeft1);
        if (moveX1 > lineLeft1 && moveX1 < (markBarWidth1 + lineLeft1)) {
            oSlider1.style.left = moveX1 + 'px';
            var reCurrent1 = parseFloat(oSlider1.style.left) - lineLeft1;
            var grade1 = reCurrent1 / percent1;
            if (reCurrent1 <= 1) {
                grade1 = 0;
            }
            if (grade1 > 99) {
                grade1= 100;
            }
            var realGrade1 = Math.round(grade1);
            $('#title1').attr('value', realGrade1);
            document.getElementById('title1').innerHTML = realGrade1+'';
        }
    }



    /**
     *
     * 滑动评价2
     *
     */

    var lineLeft = parseFloat($('#line2').css('margin-left'));
    var oSlider = document.getElementById('btn2');
    var oMarkBar = document.getElementById('line2');
    var oMin = document.getElementById('min2');
    var oMax = document.getElementById('max2');

    var markBarWidth = parseFloat(oMarkBar.offsetWidth);
    var percent = markBarWidth / 100;
    oMin.style.left = lineLeft - 15 + 'px';
    oMax.style.left = lineLeft + markBarWidth + 20 + 'px';

    oSlider.addEventListener("touchstart", touchStart, true);
    function touchStart(event) {
        //console.log(event.target);//event.target 总是指向第一次点击的对象，输出oSlider对象
        //console.log(event.currentTarget); //event.currentTarget总是指向当前的对象，输出oSlider对象
        var thisSlider = event.target;
        thisSlider.addEventListener("touchmove", touchMove, true);
    }
    function touchMove(event) {
        //console.log(event.target);//event.target 总是指向第一次点击的对象，输出oSlider对象
        //console.log(event.currentTarget); //event.currentTarget总是指向当前的对象，输出document对象
        var touch = event.touches[0];
        var moveX = touch.clientX;
        $('#progress2').css('width',moveX-lineLeft);
        if (moveX > lineLeft && moveX < (markBarWidth + lineLeft)) {
            oSlider.style.left = moveX + 'px';
            var reCurrent = parseFloat(oSlider.style.left) - lineLeft;
            var grade = reCurrent / percent;
            if (reCurrent <= 1.4) {
                grade = 0;
            }
            if (grade > 99) {
                grade = 100;
            }
            var realGrade = Math.round(grade);
            $('#title2').attr('value', realGrade);
            document.getElementById('title2').innerHTML = realGrade+'';
        }
    }

    /**
     *
     * 弹出学生列表
     *
     */

    var cHeight = document.documentElement.clientHeight;
    var cWidth = document.documentElement.clientWidth;
    var dialogHeight = $('.my-student-dialog').height();
    var top = (cHeight - dialogHeight) / 2;

    $('#mask').css({
        'position': 'fixed',
        'width': cWidth,
        'height': cHeight,
        'top': 0 + 'px',
        'left': 0 + 'px',
        'z-index': 0
    });
    if (dialogHeight < cHeight) {
        $('.my-student-dialog').css('top', top);
    } else {
        $('.my-student-dialog').css('top', 0);
    }


    $('.student-select').click(function () {
        $('.my-student-dialog').css('display', 'block');
    })
    $('#mask').click(function () {
        $('.my-student-dialog').css('display', 'none');

    })

});