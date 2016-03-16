/**
 * Created by S.F.S on 2016/3/6.
 */
document.write("<script src='assets/js/configure.js'></script>");
$(function () {

    var token = Cookies.get('token');
    var evaluationID=Cookies.get('evaluationID');
    if (token == null || token == '') {
        showErrMsg("未登录，请先登录！");
        location.href = "login.html";
        return false;
    }
//获得被评价者的名单
    var receiverType;
    $.ajax({
        type: 'POST',
        url:TEACHERGETRECEIVERLIST,
        //url: 'http://127.0.0.1:8080/OES/login', //注意：绝对路径会涉及到跨域问题
        contentType: "application/json",
        data: {
            "token":token,
            "evaluationID":evaluationID
        },
        beforeSend: function () {},
        success: function (data) {
            var code = data.code,
                errMsg = data.errMsg,
                reason = data.reason,
                success = data.success;
            if (code === 1) {
                if (success === true) {
                    //判别是组还是个人
                    receiverType=data.receiverType;
                    var receiverList=data.receiverList;
                    var oList = $('.student-select-sfs').eq(0);
                    //组--渲染进去
                    if(receiverType==1){
                        for(var i=0;i<receiverList.length;i++){
                            oList.append( "<option value='" + receiverList[i].receiverID + "' >" + "第"+receiverList[i].groupNumber +"组"+ "</option>");
                        }
                    }else{//学生名单
                        for(var i=0;i<receiverList.length;i++){
                            oList.append( "<option value='" + receiverList[i].receiverID + "' >" + receiverList[i].studentName + "</option>");
                        }
                    }

                } else {
                    showErrMsg(reason);
                }
            } else {
                showErrMsg(errMsg);
            }
        },
        complete: function () {
        },
        error: function () {
            showServiceError();
        },
        dataType: 'json'
    });


    /**
     *
     * 被评价者列表
     *
     */
  /*  var cHeight = document.documentElement.clientHeight;
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
    });
    $('#mask').click(function () {
        $('.my-student-dialog').css('display', 'none');
    });*/


    /**
     *
     * 选择学生查看结果
     *
     */

    $(document).on('change','.student-select-sfs',function(){
    	
        var receiverName=$('.student-select-sfs option:selected').text();
        var receiverID=$('.student-select-sfs option:selected').attr("value");
        Cookies.set("currentReceiverID",receiverID);
      /*  $('button.student-select').eq(0).text(receiverName);
       
        $('.my-student-dialog').css("display","none");*/
        $('button.student-field-submit').eq(0).val(receiverID);
        $('.field-panel').removeAttr('hidden');

        /**
         *
         * 获得学生分数,并显示
         *
         */

        $.ajax({
            type: 'POST',
            url:GETEVALUATIONGRADE,
            contentType: "application/json",
            data: {
                "token":token,
                "receiverID":receiverID
            },
            beforeSend: function () {},
            success: function (data) {
                var code = data.code,
                    errMsg = data.errMsg,
                    reason = data.reason,
                    success = data.success;
                if (code === 1) {
                    if (success === true) {
                            var receiverType=data.receiverType;
                            var receiverID=data.receiverID;
                            var receiverName=data.receiverName;
                            var evaluationGrade=data.evaluationGrade;
                            $(".mark-grade").text(evaluationGrade+'');
                            $(".mark-grade").attr('value',evaluationGrade+'');
                    } else {
                        showErrMsg(reason);
                    }
                } else {
                    showErrMsg(errMsg);
                }
            },
            complete: function () {
            },
            error: function () {
                showServiceError();
            },
            dataType: 'json'
        });





        //显示评价结果
        $.ajax({
            type: 'POST',
            url:TEACHERGETRESULT,
            //url: 'http://127.0.0.1:8080/OES/login', //注意：绝对路径会涉及到跨域问题
            contentType: "application/json",
            data: {
                "token":token,
                "evaluationID":evaluationID,
                "receiverID":receiverID
            },
            beforeSend: function () {},
            success: function (data) {
                var code = data.code,
                    errMsg = data.errMsg,
                    reason = data.reason,
                    success = data.success;
                if (code === 1) {
                    if (success === true) {
                        //显示未评价的学生名单
                        var evaluatingStudentList=data.evaluatingStudentList;
                        //清空名单列表
                        $("#my-list-span").empty();
                        for(var j=0;j<evaluatingStudentList.length;j++){
                             $("#my-list-span").append("<code>"+evaluatingStudentList[j].studentName+"</code>");
                        }

                        //显示结果
                        var resultList = data.resultList;

                        var displayer = $('.field-content-all').eq(0);
                        displayer.empty();
                        displayer.next().css('display', 'none');



                        for (var i = 0; i < resultList.length; i++) {
                            var fieldContent = resultList[i].fieldContent,
                                fieldType = resultList[i].evaluationResultType;

                            switch (fieldType) {
                                case VALUE_RESULT:  //打分结果的显示
                                    var valueResult = resultList[i].valueResult;
                                    var maxValue = valueResult.maxValue,
                                        minValue = valueResult.minValue,
                                        averageValue = valueResult.averageValue,
                                        evaluatedStudentNumber = valueResult.evaluatedStudentNumber;
                                    var valueTemplate = $('#template-result-value').children().eq(0).clone();
                                    valueTemplate.find('.am-list-news-hd span').eq(0).text(i + 1 + '.' + fieldContent);
                                    valueTemplate.find('li.max-value i').eq(0).text(maxValue);
                                    valueTemplate.find('li.min-value i').eq(0).text(minValue);
                                    valueTemplate.find('li.average-value i').eq(0).text(averageValue);
                                    valueTemplate.find('li.total-number i').eq(0).text(evaluatedStudentNumber);
                                    displayer.eq(0).append(valueTemplate); //插入到HTML中

                                    break;
                                case SINGLE_OPTION_RESULT:  //单选结果
                                    var singleTemplate = $('#template-result-single').children().eq(0).clone();
                                    singleTemplate.find('span').eq(0).text(fieldContent);
                                    var singleOptionResult = resultList[i].singleOptionResult;
                                    var optionArray = [];//选项名称的数组
                                    var numberArray = [];//选择人数的数组
                                    var pieArray = [{}];
                                    for (var j = 0; j < singleOptionResult.length; j++) {   //开始遍历每个选项
                                        var optionKey = singleOptionResult[j].optionKey,
                                            optionTitleContent = singleOptionResult[j].optionTitleContent,
                                            selectedNumber = singleOptionResult[j].selectedNumber,
                                            selectedPercentage = singleOptionResult[j].selectedPercentage;
                                        optionArray[j] = optionTitleContent;
                                        numberArray[j] = parseInt(selectedNumber);
                                        pieArray[j] = {
                                            'value': parseInt(selectedPercentage),
                                            'name': optionTitleContent
                                        };

                                    }

                                    var timestamp = (new Date()).valueOf();
                                    var chart = singleTemplate.find('.single-histogram').eq(0);//柱状图的DOM
                                    chart.attr('single-id', timestamp + '');  //给每一个图表添加唯一标示

                                    var chartPie = singleTemplate.find('.single-pie').eq(0);//饼状图的DOM
                                    chartPie.attr('single-id', timestamp + '');  //给每一个图表添加唯一标示

                                    displayer.eq(0).append(singleTemplate);//先加入到DOM中

                                    var chartDOM = $('.single-histogram[single-id=' + timestamp + ']')[0];
                                    var chartPieDOM = $('.single-pie[single-id=' + timestamp + ']')[0]
                                    showHistogram(chartDOM, optionArray, numberArray);  //制作柱状图
                                    showPie(chartPieDOM, pieArray);


                                    break;
                                case MULTIPLE_OPTION_RESULT: //多选结果

                                    var multipleTemplate = $('#template-result-multiple').children().eq(0).clone();
                                    multipleTemplate.find('span').eq(0).text(fieldContent);
                                    var multipleOptionResult = resultList[i].multipleOptionResult;
                                    var optionArray = [];//选项名称的数组
                                    var numberArray = [];//选择人数的数组
                                    for (var j = 0; j < multipleOptionResult.length; j++) {   //开始遍历每个选项
                                        var optionKey = multipleOptionResult[j].optionKey,
                                            optionTitleContent = multipleOptionResult[j].optionTitleContent,
                                            selectedNumber = multipleOptionResult[j].selectedNumber;
                                        optionArray[j] = optionTitleContent;
                                        numberArray[j] = parseInt(selectedNumber);
                                    }
                                    var timestamp = (new Date()).valueOf();
                                    var chart = multipleTemplate.find('.multiple-histogram').eq(0);//柱状图的DOM
                                    chart.attr('multiple-id', timestamp + '');  //给每一个图表添加唯一标示

                                    displayer.eq(0).append(multipleTemplate);//先加入到DOM中
                                    var chartDOM = $('.multiple-histogram[multiple-id=' + timestamp + ']')[0];
                                    showHistogram(chartDOM, optionArray, numberArray);  //制作柱状图
                                    break;
                                case TEXT_RESULT:  //显示文本评价
                                    var textResultList = resultList[i].textResult;
                                    var textTemplate = $('#template-result-text').children().eq(0).clone();
                                    textTemplate.find('.title-sfs').eq(0).text(fieldContent);
                                    for (var j = 0; j < textResultList.length; j++) {  //前三条显示
                                        var text = textResultList[j].text;
                                        if (j < 3) {
                                            textTemplate.find('div.text-top').eq(0).append("<span class='student-aa'>" + text + "</span>");
                                        } else {
                                            textTemplate.find('div.more').eq(0).append("<span class='student-aa'>" + text + "</span>");
                                        }
                                    }
                                    displayer.eq(0).append(textTemplate);//加入到DOM
                                    break;
                            }

                        }
                        $('body').append("<script src='assets/js/amazeui.min.js'></script>");
                        $('body').append("<script src='assets/js/slider.js'></script>");
                    } else {
                        showErrMsg(reason);
                    }
                } else {
                    showErrMsg(errMsg);
                }
            },
            complete: function () {
            },
            error: function () {
                showServiceError();
            },
            dataType: 'json'
        });
        $('.my-green').removeAttr("hidden");

    });





    /**
     *
     * 教师为学生打分
     *
     */

    $(".student-field-submit").click(function(){
        var evaluationGrade= $(".mark-grade").html();
        var receiverID=Cookies.get("currentReceiverID");
        $.ajax({
            type: 'POST',
            url:UPDATEEVALUTIONGRADE,
            //url: 'http://127.0.0.1:8080/OES/login', //注意：绝对路径会涉及到跨域问题
            contentType: "application/json",
            data: {
                "token":token,
                "receiverID":receiverID,
                "evaluationGrade": evaluationGrade
            },
            beforeSend: function () {},
            success: function (data) {
                var code = data.code,
                    errMsg = data.errMsg,
                    reason = data.reason,
                    success = data.success;
                if (code === 1) {
                    if (success === true) {
                        showSuccessMsg("打分完毕！");
                    } else {
                        showErrMsg(reason);
                    }
                } else {
                    showErrMsg(errMsg);
                }
            },
            complete: function () {
            },
            error: function () {
                showServiceError();
            },
            dataType: 'json'
        });
    });
    
/**
     * 点击文本，显示完整的
     *
     */
    $(document).on('touchstart', '.student-aa', function () {
        $(this).css('white-space', 'normal');
    });

    /**
     *点击更多，显示所有文本
     */
    $(document).on('touchstart', '.more-sfs', function () {
        $(this).nextAll('.more').eq(0).css('display', 'block');
    });


    /**
     *
     *
     * 制作图表区域
     *
     *
     *
     */

    /**
     * 柱状图（已封装）
     * @param jsDOM
     * @param arrayOption
     * @param arrayNumber
     */
    function showHistogram(jsDOM, arrayOption, arrayNumber) {  //jsDOM:一个div元素，绘制的地方
        var myChart1 = echarts.init(jsDOM);        // 基于准备好的dom，初始化echarts实例
        var option = {    // 指定图表的配置项和数据
            tooltip: {//提示框
                trigger: 'axis'
            },
            grid: {//直角坐标系内绘图网格
                left: '3%',
                right: '4%',
                bottom: '3%',
                containLabel: true//grid 区域是否包含坐标轴的刻度标签
            },
            xAxis: [//直角坐标系 grid 中的 x 轴，
                {
                    type: 'value',//数值轴，适用于连续数据。
                    boundaryGap: [0, 0.01]//坐标轴两边留白策略，类目轴和非类目轴的设置和表现不一样。
                }
            ],
            yAxis: [//直角坐标系 grid 中的 y轴，
                {
                    type: 'category',//'category' 类目轴，适用于离散的类目数据，为该类型时必须通过 data 设置类目数据。
                    data: arrayOption
                }
            ],
            series: [//系列列表。每个系列通过 type 决定自己的图表类型
                {
                    name: '人数',
                    type: 'bar',//柱状图
                    data: arrayNumber
                }
            ]
        };
        myChart1.setOption(option);   // 使用刚指定的配置项和数据显示图表。
    }


    /**
     *
     * 饼状图(已封装)
     * @param jsDOM
     * @param jsonArray
     */
    function showPie(jsDOM, jsonArray) {
        var myChart2 = echarts.init(jsDOM);   // 基于准备好的dom，初始化echarts实例
        var option2 = { // 指定图表的配置项和数据
            tooltip: {
                trigger: 'item',
                formatter: "{a} <br/>{b} : {c} ({d}%)"
            },
            series: [
                {
                    type: 'pie',
                    radius: '55%',
                    center: ['50%', '60%'],
                    data: jsonArray,
                    itemStyle: {
                        emphasis: {
                            shadowBlur: 10,
                            shadowOffsetX: 0,
                            shadowColor: 'rgba(0, 0, 0, 0.5)'
                        }
                    }
                }
            ]
        };
        myChart2.setOption(option2);// 使用刚指定的配置项和数据显示图表。
    }






});