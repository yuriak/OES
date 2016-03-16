/**
 * Created by S.F.S on 2016/3/6.
 */
$(function () {


    var oMarkList = $("[resulttypeid='1']");
    for (var i = 0; i < oMarkList.length; i++) {
        var oMarkBar = oMarkList.eq(i).find('.mark-bar');
        showSlider(oMarkBar);
    }
    /**
     *
     * 滑动评价(手机端)
     * @param oMarkBar
     */
    function showSlider(oMarkBar) {
        var line = oMarkBar.children('.mark-bar-line'),
            progress = oMarkBar.children('.mark-bar-progress'),
            title = oMarkBar.children('.mark-grade'),
            oSlider = oMarkBar.children('.slider'),
            min = oMarkBar.children('.min'),
            max = oMarkBar.children('.max');

        var lineLeft = parseFloat(line.css('margin-left'));
        var lineWidth = parseFloat(line[0].offsetWidth);
        var percent = lineWidth / 100;

      //保持分数与滑动条位置同步
        var gradeDefault=parseInt(title.text());
        oSlider.css('left', gradeDefault*percent+lineLeft);
        progress.css('width', gradeDefault*percent);
        
        min.css('left', lineLeft - 15);
        max.css('left', lineLeft + lineWidth + 15);

        oSlider[0].addEventListener("touchstart", touchStart, true);
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
            if (moveX > lineLeft && moveX < (lineWidth + lineLeft)) {
                oSlider.css('left', moveX);
                progress.css('width', moveX - lineLeft);

                var sliderLeft = oSlider.css('left');
                var reCurrent = parseFloat(sliderLeft) - lineLeft;
                var grade = reCurrent / percent;
                if (reCurrent <= 1) {
                    grade = 0;
                }
                if (grade > 99) {
                    grade = 100;
                }
                var realGrade = Math.round(grade);
                title.attr('value', realGrade);
                title.text(realGrade + '');
            }
        }


    }



    /*/!**
     *
     * 滑动评价1
     *
     *!/

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
        $('#progress1').css('width', moveX1 - lineLeft1);
        if (moveX1 > lineLeft1 && moveX1 < (markBarWidth1 + lineLeft1)) {
            oSlider1.style.left = moveX1 + 'px';
            var reCurrent1 = parseFloat(oSlider1.style.left) - lineLeft1;
            var grade1 = reCurrent1 / percent1;
            if (reCurrent1 <= 1) {
                grade1 = 0;
            }
            if (grade1 > 99) {
                grade1 = 100;
            }
            var realGrade1 = Math.round(grade1);
            $('#title1').attr('value', realGrade1);
            document.getElementById('title1').innerHTML = realGrade1 + '';
        }
    }*/



});