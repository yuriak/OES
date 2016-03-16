/**
 * Created by S.F.S on 2016/3/6.
 */
$(function () {


    /**
     * 获取学生列表
     *
     */
    var token = Cookies.get('token');
    var currentEvaluationID = Cookies.get('currentEvaluationID');
    var userID = Cookies.get('userID');
    var evaluationStatus;
    $.ajax({
        type: 'POST',
        url: STUDENTGETRECEIVERLIST,
        contentType: "application/json",
        data: {
            'token': token,
            'evaluationID': currentEvaluationID
        },
        success: function (data) {
            var code = data.code,
                errMsg = data.errMsg,
                reason = data.reason,
                success = data.success;
            if (code === 1) {
                if (success === true) {
                    //成功后执行的业务
                    var receiverType = data.receiverType;
                    var oList = $('.my-student-dialog').find('ul').eq(0);
                    //获取当前评价项的状态（上课，下课还是未上课）
                    $.ajax({
                        type: 'POST',
                        async:false,
                        url:GETEVALUATIONSTATUS,
                        contentType: "application/json",
                        data: {
                            'token':token,
                            'evaluationID':currentEvaluationID
                        },
                        beforeSend: function () {
                            //$('#my-loading').modal('open');
                            // 禁用按钮防止重复提交
                        },
                        success: function (data) {
                            var code = data.code,
                                errMsg = data.errMsg,
                                reason = data.reason,
                                success = data.success;
                            if (code === 1) {
                                if (success === true) {
                                    //成功后执行的业务
                                    evaluationStatus = data.evaluationStatus;
                                    //Cookies.set('currentEvaluationStatus',evaluationStatus,{path:'/'});
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

                    var receiverList;
                    if (receiverType == GROUP_RECEIVER) { //组评价
                        receiverList = data.receiverList;

                        for (var i = 0; i < receiverList.length; i++) {
                            var receiverStatus = receiverList[i].receiverStatus,
                                groupID = receiverList[i].groupID,
                                groupNumber = receiverList[i].groupNumber,
                                receiverID = receiverList[i].evaluationReceiverID,
                                isMyGroup = receiverList[i].isMyGroup;

                            if (isMyGroup) {
                                oList.prepend("<li class='aasfs'><a class='my-mark-student' isme='true'  value='" + receiverID + "' receiverStatus='1'>" +"第"+ groupNumber + "组"+"<span class='my-marked am-badge am-badge-danger' >自己组</span>" + "</a></li>");
                            } else { //不是自己组时
                                if (receiverStatus == 1 || evaluationStatus==1) {   //已打分的
                                    oList.prepend("<li class='aasfs'><a class='my-mark-student'  value='" + receiverID + "' receiverStatus='1'>" +"第"+ groupNumber +"组"+ "<span class='my-marked am-badge am-badge-danger' >查看结果</span>" + "</a></li>");
                                } else {
                                    oList.append("<li class='aasfs'><a class='my-mark-student' value='" + receiverID + "' receiverStatus='0'>" +"第"+ groupNumber +"组"+ "</a></li>");
                                }
                            }

                            /*if (i == 0) {
                             handlerResult(receiverStatus, groupID, groupNumber);
                             }*/

                        }
                        for (var i = receiverList.length - 1; i >= 0; i--) { //将最上边的已打分的学生默认显示在打分页面
                            var receiverStatus = receiverList[i].receiverStatus,
                                groupID = receiverList[i].evaluationReceiverID,
                                groupNumber = receiverList[i].groupNumber;

                            if (receiverStatus == 1) {
                                handlerResult(receiverStatus, groupID, groupNumber);
                                break;
                            }
                        }


                    } else if (receiverType == PERSON_RECEIVER) { //个人评价
                        receiverList = data.receiverList;

                        for (var i = 0; i < receiverList.length; i++) {
                            var receiverStatus = receiverList[i].receiverStatus,
                                studentID = receiverList[i].studentID,
                                studentName = receiverList[i].studentName,
                                receiverID = receiverList[i].evaluationReceiverID,
                                isMe = receiverList[i].isMe;
                            if (isMe) {
                                oList.prepend("<li class='aasfs'><a class='my-mark-student' isme='true' value='" + receiverID + "' receiverStatus='1'>" + studentName + "<span class='my-marked am-badge am-badge-danger'>查看结果</span></a></li>");
                            } else {
                                if (receiverStatus == 1) {  //自己已评价的人
                                    oList.prepend("<li class='aasfs'><a class='my-mark-student' value='" + receiverID + "' receiverStatus='1'>" + studentName + "<span class='my-marked am-badge am-badge-danger'>查看结果</span></a></li>");
                                } else { //自己还未评价的人
                                    oList.append("<li class='aasfs'><a class='my-mark-student' value='" + receiverID + "' receiverStatus='0'>" + studentName + "</a></li>");
                                }
                            }

                        }
                        for (var i = receiverList.length - 1; i >= 0; i--) { //将最上边的已打分的学生默认显示在打分页面
                            var receiverStatus = receiverList[i].receiverStatus,
                                studentID = receiverList[i].evaluationReceiverID,
                                studentName = receiverList[i].studentName;

                            if (receiverStatus == 1) {
                                handlerResult(receiverStatus, studentID, studentName);
                                break;
                            }
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
            //$('#my-loading').modal('close');

        },
        error: function () {
            showServiceError();
        },
        dataType: 'json'
    });

    /**
     *
     * 选择学生进行评价或查案结果
     *
     */
    $(document).on('touchstart', '.aasfs', handlerResult);
    function handlerResult(defaultReceiverStatus, defaultStudentID, defaultStudentName) {
        $('.field-panel').find('.field-content-all').eq(0).empty();
        var receiverStatus;
        var studentID;
        var studentName;
        if (defaultReceiverStatus != null && defaultStudentID != null) {
            receiverStatus = defaultReceiverStatus;
            studentID = defaultStudentID;
            studentName = defaultStudentName;
        } else {
            receiverStatus = $(this).children().attr('receiverstatus');
            studentID = $(this).children().attr('value');
            isMeOrMyGroup = $(this).children().attr('isme');  //是否是自己或自己组
            studentName = $(this).children().text().split(' ')[0];
            $('.my-student-dialog').css('display', 'none');
        }
        $('button.student-select').eq(0).text(studentName);
        $('button.student-field-submit').eq(0).attr('student-id', studentID);

        if (receiverStatus == 1 || evaluationStatus==1||isMeOrMyGroup === 'true') {  //点击自己已经评价的学生就获得结果页面
            $.ajax({
                type: 'POST',
                url: STUDENTGETRESULT,
                contentType: "application/json",
                data: {
                    'token': token,
                    'receiverID': studentID
                },
                beforeSend: function () {
                    //$('#my-loading').modal('open');
                },
                success: function (data) {
                    var code = data.code,
                        errMsg = data.errMsg,
                        reason = data.reason,
                        success = data.success;
                    if (code === 1) {
                        if (success === true) {
                            //成功后执行的业务
                            var resultList = data.resultList;
                            /*var displayer = $('.field-panel').eq(0);
                             displayer.remove();*/

                            var displayer = $('.field-content-all').eq(0);
                            displayer.empty();
                            displayer.next().css('display', 'none');
                            for (var i = 0; i < resultList.length; i++) {
                                var fieldContent = resultList[i].fieldContent,
                                    fieldType = resultList[i].fieldType;

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
                                                textTemplate.find('div.text-top').eq(0).append("<span class='student-aa'><a class='result-text-sfs'>" + text + "</a></span>");
                                            } else {
                                                textTemplate.find('div.more').eq(0).append("<span class='student-aa'><a class='result-text-sfs'>" + text + "</a></span>");
                                            }
                                        }
                                        displayer.eq(0).append(textTemplate);//加入到DOM
                                        break;
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
                    //$('#my-loading').modal('close');

                },
                error: function () {
                    showServiceError();
                },
                dataType: 'json'
            });

        } else if (receiverStatus == 0) { //点击自己还没有评价的学生，获得评价页面
            $.ajax({
                type: 'POST',
                async: false,
                url: GETEVALUATIONFIELDLIST,
                contentType: "application/json",
                data: {
                    'token': token,
                    'evaluationID': currentEvaluationID
                },
                success: function (data) {
                    var code = data.code,
                        errMsg = data.errMsg,
                        reason = data.reason,
                        success = data.success;
                    if (code === 1) {
                        if (success === true) {
                            //成功后执行的业务
                            //var evaluationID = data.evaluationID,
                            /*var displayer = $('.result-all').eq(0);
                             displayer.remove();*/
                            var displayer = $('.field-content-all').eq(0);
                            displayer.empty();

                            var evaluationFiledList = data.evaluationFieldList;
                            for (var i = 0; i < evaluationFiledList.length; i++) {  //遍历每一个评价字段
                                var evaluationFieldID = evaluationFiledList[i].evaluationFieldID,
                                    fieldContent = evaluationFiledList[i].fieldContent,
                                    resultTypeID = evaluationFiledList[i].resultTypeID,
                                    resultTypeName = evaluationFiledList[i].resultTypeName,
                                    optionTitleList = evaluationFiledList[i].optionTitleList;


                                if (resultTypeID == VALUE_RESULT) {  //打分
                                    var template = $('#template-value').children().eq(0).clone();
                                    template.attr('evaluation-field-id', evaluationFieldID);
                                    template.find('h6').text(i + 1 + '.' + fieldContent + '(' + resultTypeName + ')');

                                    displayer.eq(0).append(template);
                                } else if (resultTypeID == SINGLE_OPTION_RESULT) {  //单选
                                    var template = $('#template-single-option').children().eq(0).clone();
                                    template.attr('evaluation-field-id', evaluationFieldID);
                                    template.find('h6').text(i + 1 + '.' + fieldContent + '(' + resultTypeName + ')');
                                    for (var j = 0; j < optionTitleList.length; j++) {//遍历每一个评价字段中的选项
                                        var optionKey = optionTitleList[j].optionKey,
                                            optionTitleContent = optionTitleList[j].optionTitleContent;
                                        if (j == 0) {

                                            template.find('.radio-all').eq(0).append("<label class='am-radio'><input type='radio' name='radio"+i+"' checked='checked' value='" + optionKey + "' data-am-ucheck>" + optionTitleContent + "</label>");
                                        } else {

                                            template.find('.radio-all').eq(0).append("<label class='am-radio'><input type='radio' name='radio"+i+"' value='" + optionKey + "' data-am-ucheck>" + optionTitleContent + "</label>");
                                        }

                                    }
                                    displayer.eq(0).append(template);


                                } else if (resultTypeID == MULTIPLE_OPTION_RESULT) { //多选
                                    var template = $('#template-multiple-option').children().eq(0).clone();
                                    template.attr('evaluation-field-id', evaluationFieldID);
                                    template.find('h6').text(i + 1 + '.' + fieldContent + '(' + resultTypeName + ')')
                                    for (var j = 0; j < optionTitleList.length; j++) {//遍历每一个评价字段中的选项
                                        var optionKey = optionTitleList[j].optionKey,
                                            optionTitleContent = optionTitleList[j].optionTitleContent;

                                        template.find('.checkbox-all').eq(0).append("<label class=\'am-checkbox\'><input type=\'checkbox\' value=\'" + optionKey + "\' data-am-ucheck>" + optionTitleContent + " </label>");
                                        /*var node=$("<label class=\'am-checkbox\'><input type=\'checkbox\' value=\'" + optionKey + "\' data-am-ucheck>" + optionTitleContent + " </label>");
                                         template.find('.checkbox-all').get(0).appendChild(node[0]);*/
                                    }
                                    displayer.eq(0).append(template);
                                    //$('.field-panel').find('.field-content-all').get(0).appendChild(template[0]);

                                } else if (resultTypeID == TEXT_RESULT) { //文本
                                    var template = $('#template-text').children().eq(0).clone();
                                    template.attr('evaluation-field-id', evaluationFieldID);
                                    template.find('h6').text(i + 1 + '.' + fieldContent + '(' + resultTypeName + ')')
                                    template.find('.my-field-group').eq(0).append("<textarea id=\"evaluation-content\" class=\"my-height\" maxlength=\"100\" rows=\"5\" required placeholder=\"评价内容100字以内\"></textarea>");
                                    displayer.eq(0).append(template);
                                    //$(document).trigger("create");
                                }

                            }

                            displayer.next().css('display', 'block');
                            $('body').append("<script src='assets/js/amazeui.min.js'></script>");
                            $('body').append("<script src='assets/js/slider.js'></script>");

                        } else {
                            showErrMsg(reason);
                        }
                    } else {
                        showErrMsg(errMsg);
                    }
                },
                error: function () {
                    showServiceError();
                },
                dataType: 'json'
            });


        }


    }

    /**
     * 提交评价
     */
    $('.student-field-submit').click(function () {
        var studentID = $(this).attr('student-id');

        var data = [];

        var fieldGroup = $('.field-content-all').children('.my-field-group');
        //获取学生评价结果
        for (var i = 0; i < fieldGroup.length; i++) {
            var resultTypeID = fieldGroup.eq(i).attr('resulttypeid'),
                evaluationFieldID = fieldGroup.eq(i).attr('evaluation-field-id');
            var resultContent;
            switch (parseInt(resultTypeID)) {
                case VALUE_RESULT:
                    resultContent = fieldGroup.eq(i).find('code.mark-grade').eq(0).attr('value');
                    break;
                case SINGLE_OPTION_RESULT:
                    resultContent = fieldGroup.eq(i).find("input[type='radio']:checked").eq(0).attr('value');
                    break;

                case MULTIPLE_OPTION_RESULT:
                    var multiple = fieldGroup.eq(i).find("input[type='checkbox']");
                    var result = '';
                    for (var j = 0; j < multiple.length; j++) {
                        if (multiple.eq(j).is(':checked')) {
                            result = result + multiple.eq(j).attr('value');
                        } else {                        //多选中没有选择的有#号表示
                            result = result + '#';
                        }
                    }
                    resultContent = result;
                    break;

                case TEXT_RESULT:
                    resultContent = fieldGroup.eq(i).find("textarea").eq(0).val();
                    break;

            }
            data[i] = {
                'resultTypeID': parseInt(resultTypeID),
                'evaluationFieldID': parseInt(evaluationFieldID),
                'resultContent': resultContent
            };

        }
        console.log(JSON.stringify(data));
        var isContinue = checkForm();
        if (isContinue == false) {
            return false;
        }
        function checkForm() {
            var boo = true; //提交按钮是否可以使用
            for (var i = 0; i < data.length; i++) {
                var content = data[i].resultContent;
                if (content === null || content === "" || content === '####') {
                    boo = false;
                }
            }
            if (!boo) {
                alert('每一项都必须评价')
                return false;
            }
        }

        $.ajax({
            type: 'POST',
            url: STUDENTSETRESULT,
            contentType: "application/json",
            data: {
                'token': token,
                'senderID': userID,
                'receiverID': studentID,
                'resultContent': JSON.stringify(data)
            },
            beforeSend: function () {
                //$('#my-loading').modal('open');
                // 禁用按钮防止重复提交


            },
            success: function (data) {
                var code = data.code,
                    errMsg = data.errMsg,
                    reason = data.reason,
                    success = data.success;
                if (code === 1) {
                    if (success === true) {
                        //成功后执行的业务
                        alert('评价成功！')
                        window.location.reload();

                    } else {
                        showErrMsg(reason);
                    }
                } else {
                    showErrMsg(errMsg);
                }
            },
            complete: function () {
                //$('#my-loading').modal('close');

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
        $(this).children().css('white-space', 'normal');
    });

    /**
     *点击更多，显示所有文本
     */
    $(document).on('touchstart', '.more-sfs', function () {
        $(this).nextAll('.more').eq(0).css('display', 'block');
    });


    /**
     *
     * 学生选择
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
    });
    $('#mask').click(function () {
        $('.my-student-dialog').css('display', 'none');
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