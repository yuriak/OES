/**
 * Created by S.F.S on 2016/3/6.
 */
document.write("<script src='assets/js/configure.js'></script>");
$(function () {

    //获取可视区域的宽度和高度(无单位)
    var cHeight = document.documentElement.clientHeight;
    var cWidth = document.documentElement.clientWidth;
    //固定界面的宽高为屏幕宽高
    $('#mask2-sfs').css({'width': cWidth, 'height': cHeight});


    var token = Cookies.get('token');
    var currentLessonID = Cookies.get('currentLessonID');
    var courseID = Cookies.get('currentCourseID');

    var groupTemplateID = 0;   //0则是新建分组，其他为模板组ID************************
    var evaluationTemplateList;  //评价项模板
    /**
     * 自动调取所有评价项模板
     */
    $.ajax({
        type: 'POST',
        async:false,
        url: GETEVALUATIONTEMPLATEINFO,
        contentType: "application/json",
        data: {
            'token': token,
            'courseID': courseID
        },
        success: function (data) {
            var code = data.code,
                errMsg = data.errMsg,
                reason = data.reason,
                success = data.success;
            if (code === 1) {
                if (success === true) {
                    //成功后执行的业务
                    evaluationTemplateList = data.evaluationTemplateList;
                    for (var i = 0; i < evaluationTemplateList.length; i++) {
                        var evaluationTemplateID = evaluationTemplateList[i].evaluationTemplateID;
                        var evaluationTemplateName = evaluationTemplateList[i].evaluationTemplateName;
                        $('#field-template').append("<option value='" + evaluationTemplateID + "'>" + evaluationTemplateName + "</option>");
                    }
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

    /**
     *点击导入组模板
     */
    $('#import-group-template').click(function () {
    	
        $(this).parent().find('.top').removeAttr('hidden');
        var oGroupNumber = $('#total-group-number');  //input元素
        oGroupNumber.attr('disabled', 'disabled');
        $.ajax({        //获得这个课程全部的组模板
            type: 'POST',
            async: false,
            url: GETGROUPTEMPLATEINFO,
            contentType: "application/json",
            data: {
                'token': token,
                'courseID': courseID
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
                        var templateList = data.templateList;
                        $('#gtemplate').empty();
                        if (templateList!=0) {  //有组模板的时候
                            for (var i = 0; i < templateList.length; i++) {
                                var groupTemplateName = templateList[i].groupTemplateName,  //模板名称
                                    groupList = templateList[i].groupList;  //模板中所有的组
                                var oGroupTemplate = $('#template-group').children().clone();
                                oGroupTemplate.find('dt').children('span').text(i + 1 + '.' + groupTemplateName);

                                for (var j = 0; j < groupList.length; j++) {
                                    oGroupTemplate.find('input').attr('data-group-template-id', groupList[j].groupTemplateID);
                                    var groupNumber = groupList[j].groupNumber;
                                    var memberList = groupList[j].memberList;

                                    var oContent = oGroupTemplate.find('dd').children();
                                    var students = '';
                                    for (var k = 0; k < memberList.length; k++) {  //遍历一个组里的学生
                                        var studentName = memberList[k].studentName;
                                        if (k == 0) {
                                            students = studentName;
                                        } else {
                                            students = students + ',' + studentName;
                                        }
                                    }
                                    
                                    oContent.append("<div> <code>第<i>" + groupNumber + "</i>组:</code><span>" + students + "</span></div>");
                                }

//                                $('#button-tem-sfs').before(oGroupTemplate);
                                $('#gtemplate').append(oGroupTemplate);
                            }
                            
                            $('body').append("<script src='assets/js/amazeui.min.js'></script>");
                            $('.unique-connection').css('display', 'block');
                        } else{  //没有组模板的时候
                            alert('你这学期没有保存过模板！')
                            oGroupNumber.val('没有模板可以添加');
                        }
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



    });

    /**
     * 确认选择组模板
     */
    $('#confirm-sfs').click(function () {  //确认选择组模板
        var check = $('#group-template-show').find('input:checked');
        var templateID = check.attr('data-group-template-id');
        var title = check.parent().prev().text();
        $('#total-group-number').attr('placeholder', '你选择了:' + title);
        $('#total-group-number').val('');
        $('#total-group-number').attr('useTemplate','true');
        groupTemplateID = templateID;
        $('.unique-connection').css('display', 'none');
        
    });

    /**
     * 添加组模板时候的取消按钮
     */
    $('#cancel-sfs').click(function () {   //添加组模板时候的取消按钮
        $('.unique-connection').css('display', 'none');
        $('#total-group-number').val('');
        $('#total-group-number').attr('placeholder', '你取消了导入模板,请新建分组！');
        $('#total-group-number').attr('useTemplate','false');
    });

    /**
     * 新建分组
     */
    $('#new-group').click(function () {   //新建分组
        $(this).next().removeAttr('hidden');
        var total = $('#total-group-number');
        total.val('');
        total.attr('useTemplate','false');
        total.removeAttr('disabled');
        total.attr('placeholder', '请输入组数(数字)');
    });


    /**
     *
     * 点击select在option中放入evaluationTemplateID
     */
/*    for (var i = 0; i < evaluationTemplateList.length; i++) {
        var evaluationTemplateID = evaluationTemplateList[i].evaluationTemplateID;
        var evaluationTemplateName = evaluationTemplateList[i].evaluationTemplateName;
        $('#field-template').append("<option value='" + evaluationTemplateID + "'>" + evaluationTemplateName + "</option>");
    }*/
 /*   $('#field-template').bind('click', function () {
        $(this).empty();
        $(this).append("<option value='0'>新建</option>");

    });*/

    /**
     *
     * 是否导入评价模板
     *
     */
    $('#field-template').bind('change', function () {
        var selectEvaluationTemplateID = $('#field-template option:selected').val();   //为0的时候是新建评价标准
        if (selectEvaluationTemplateID == 0) {
            
        	var index=$(".field-top").length;
        	while(index>1){//防止删除了内置的模板
            	$(".field-top").eq(0).remove();
            	index=index-1;
            }
            
            var oEvaluation = $('#template-evaluation').children().clone();
            $('#field-template').after(oEvaluation);
        } else { //选择了模板的时候
        	var index=$(".field-top").length;
        	while(index>1){//防止删除了内置的模板
            	$(".field-top").eq(0).remove();
            	index=index-1;
            }
            for (var i = 0; i < evaluationTemplateList.length; i++) {
                var evaluationTemplateID = evaluationTemplateList[i].evaluationTemplateID;
                if (selectEvaluationTemplateID == evaluationTemplateID) {  //显示选中的评价标准字段的模板
                    var evaluationFieldList = evaluationTemplateList[i].evaluationFieldList;
                    var length = evaluationFieldList.length;
                    var place = $('.my-cun-new');

                    for (var j = 0; j < length; j++) {
                        var fieldContent = evaluationFieldList[j].fieldContent;
                        var resultType = evaluationFieldList[j].resultType;

                        switch (resultType) {
                            case VALUE_RESULT:
                                var oTemplate = $('#template-evaluation').children('.field-top').clone();
                                oTemplate.find('span').text(parseInt(j)+1  + '.');
                                oTemplate.find('input').val(fieldContent);
                                oTemplate.find('select').children("[value='0']").attr('selected', 'selected');
                                if (length == 1 || j == length - 1) {
                                    place.before(oTemplate);
                                } else {
                                    oTemplate.find('button').text('-');
                                    oTemplate.find('button').removeClass().addClass('field-delete add-button my-text-white am-btn am-btn-danger');
                                    place.before(oTemplate);
                                }
                                break;
                            case SINGLE_OPTION_RESULT:
                                var oTemplate = $('#template-evaluation').children('.field-top').clone();
                                var optionTitleList = evaluationFieldList[j].optionTitleList;
                                oTemplate.find('span').text(parseInt(j)+1 + '.');
                                oTemplate.find('input').val(fieldContent);
                                oTemplate.find('select').children("[value='1']").attr('selected', 'selected');

                                if (length == 1 || j == length - 1) {

                                } else {
                                    oTemplate.find('button').text('-');
                                    oTemplate.find('button').removeClass().addClass('field-delete add-button my-text-white am-btn am-btn-danger');
                                }


                                var len = optionTitleList.length;
                                for (var k = 0; k < len; k++) { //添加评价标准
                                    var _boo = true;
                                    var checkText; //代表A,B,C还是D
                                    switch (k) {
                                        case 0:
                                            checkText = 'A';
                                            break;
                                        case 1:
                                            checkText = 'B';
                                            break;
                                        case 2:
                                            checkText = 'C';
                                            break;
                                        case 3:
                                            checkText = 'D';
                                            break;
                                        case 4:
                                            checkText = 'E';
                                            break;
                                        case 5:
                                            checkText = 'F';
                                            break;

                                        case 6:
                                            checkText = 'F';
                                            break;
                                    }

                                    var content1 = optionTitleList[k].optionTitleContent;
                                    //var key=optionTitleList[k].optionKey;

                                    if (len == 1 || k == len - 1) {
                                        var oTemplateCheck = $('#template-check').children('.content-group').clone();
                                        oTemplateCheck.find('span').text(checkText);
                                        oTemplateCheck.find('span').attr('value', k);
                                        oTemplateCheck.find('input').val(content1);
                                        oTemplate.find('hr').before(oTemplateCheck);
                                    } else {
                                        var oTemplateCheck = $('#template-check').children('.content-group').clone();
                                        oTemplateCheck.find('span').text(checkText);
                                        oTemplateCheck.find('span').attr('value', k);
                                        oTemplateCheck.find('input').val(content1);

                                        oTemplateCheck.find('button').text('-');
                                        oTemplateCheck.find('button').removeClass().addClass("check-delete add-button my-text-white am-btn am-btn-danger");
                                        oTemplate.find('hr').before(oTemplateCheck);
                                    }
                                }

                                place.before(oTemplate);   //评价项插入
                                break;
                            case MULTIPLE_OPTION_RESULT:
                                var oTemplate = $('#template-evaluation').children('.field-top').clone();
                                var optionTitleList = evaluationFieldList[j].optionTitleList;
                                oTemplate.find('span').text(parseInt(j)+1+ '.');
                                oTemplate.find('input').val(fieldContent);
                                oTemplate.find('select').children("[value='2']").attr('selected', 'selected');

                                if (length == 1 || j == length - 1) {

                                } else {
                                    oTemplate.find('button').text('-');
                                    oTemplate.find('button').removeClass().addClass('field-delete add-button my-text-white am-btn am-btn-danger');
                                }


                                var len = optionTitleList.length;
                                for (var k = 0; k < len; k++) { //添加评价标准
                                    var _boo = true;
                                    var checkText; //代表A,B,C还是D
                                    switch (k) {
                                        case 0:
                                            checkText = 'A';
                                            break;
                                        case 1:
                                            checkText = 'B';
                                            break;
                                        case 2:
                                            checkText = 'C';
                                            break;
                                        case 3:
                                            checkText = 'D';
                                            break;
                                        case 4:
                                            checkText = 'E';
                                            break;
                                        case 5:
                                            checkText = 'F';
                                            break;

                                        case 6:
                                            checkText = 'F';
                                            break;
                                    }

                                    var content1 = optionTitleList[k].optionTitleContent;
                                    //var key=optionTitleList[k].optionKey;

                                    if (len == 1 || k == len - 1) {
                                        var oTemplateCheck = $('#template-check').children('.content-group').clone();
                                        oTemplateCheck.find('span').text(checkText);
                                        oTemplateCheck.find('span').attr('value', k);
                                        oTemplateCheck.find('input').val(content1);
                                        oTemplate.find('hr').before(oTemplateCheck);
                                    } else {
                                        var oTemplateCheck = $('#template-check').children('.content-group').clone();
                                        oTemplateCheck.find('span').text(checkText);
                                        oTemplateCheck.find('span').attr('value', k);
                                        oTemplateCheck.find('input').val(content1);

                                        oTemplateCheck.find('button').text('-');
                                        oTemplateCheck.find('button').removeClass().addClass("check-delete add-button my-text-white am-btn am-btn-danger");
                                        oTemplate.find('hr').before(oTemplateCheck);
                                    }
                                }

                                place.before(oTemplate);   //评价项插入
                                break;
                            case TEXT_RESULT:
                                var oTemplate = $('#template-evaluation').children('.field-top').clone();
                                oTemplate.find('span').text(parseInt(j)+1 + '.');
                                oTemplate.find('input').val(fieldContent);
                                oTemplate.find('select').children("[value='3']").attr('selected', 'selected');
                                if (length == 1 || j == length - 1) {
                                    place.before(oTemplate);
                                } else {
                                    oTemplate.find('button').text('-');
                                    oTemplate.find('button').removeClass().addClass('field-delete add-button my-text-white am-btn am-btn-danger');
                                    place.before(oTemplate);
                                }
                                break;
                        }
                    }
                }

                break;
            }
        }

    });


    /**
     * 提交
     */
    $('.addEvaluation').click(function () {  //提交表单

        var canSubmit = true; //提交按钮默认是不能用的(一步步检测表单的值后改变其值)            //检测表单（待完成。。。）


        var evaluationTitle = $('#field-title').val();
        var isGroup = $('#isDivide').is(':checked');  //是否分组
        var groupNumber = $('#total-group-number').val(); //0:选择了模板；！=0：是新建分组****************************************
        var saveAsEvaluationTemplate = $('#saveTemplate').is(':checked');  //是否保存为模板返回bool值
        var evaluationTemplateName = $('.template-name').children('input').val();

        var data = {};  //传给服务器的data数据
        data['courseID'] = courseID;
        data['lessonID'] = currentLessonID;
        data['token'] = token;
        data['isGroup'] = isGroup;
        data['groupNumber'] = groupNumber;
        data['evaluationTemplateName'] = evaluationTemplateName;
        data['evaluationTitle'] = evaluationTitle;
        data['saveAsEvaluationTemplate'] = saveAsEvaluationTemplate;


        var booo = checkForm();  //检测表单
        if (!booo) {
            alert('输入有误');
            return false;
        }

        function checkForm() {
            if (evaluationTitle.length == 0 || evaluationTitle.length == null) {
                canSubmit = false;
                alert('标题不能为空！');
                return false;
            }
            if (evaluationTitle.length >= 20) {
                canSubmit = false;
                alert('标题不能大于20个字符！');
                return false;
            }
            if (isGroup) {
            	var useTemp=$('#total-group-number').attr('useTemplate');
            	if(useTemp=='true'){
            		return true;
            	}
                if (groupNumber == 0 || groupNumber == null||groupNumber>=20) {
                    canSubmit = false;
                    alert('组数不能为0，不能为空，且不能大于20');
                    return false;
                }
            }
            return true;
        }

        var evaluationFieldList = [{}]; //准备取各个评价标准的值


        if (isGroup) {//如果是分组
        	data['groupTemplateID'] = 0;
        	if($('#total-group-number').attr('useTemplate')=='true'){
        		data['groupTemplateID'] = parseInt(groupTemplateID);
        		data['groupNumber']=0;
        	}
        }
        //新建评价标准和从其他模板导入(都一样，groupTemplateID自身就可以区别)
        var fieldList = $('form').find('.field-top');  //获得所有的评价项List

        for (var i = 0; i < fieldList.length; i++) {//evaluationFieldList开始取值
            var oFieldTitle = fieldList.eq(i).children('.field-group');
            var fieldContent = oFieldTitle.find('input').val();  //字段标题
            var oldFieldType = oFieldTitle.find('select option:selected').val();//单选或者多选等（1.2.3.4）
            var fieldType=parseInt(oldFieldType)+1;

            switch (fieldType) {  // 给evaluationFieldList赋值
                case VALUE_RESULT: //打分
                    evaluationFieldList[i] = {
                        'fieldContent': fieldContent,
                        'resultType': VALUE_RESULT
                    };
                    if (fieldContent.length == 0 || fieldContent.length >= 20) {
                        canSubmit = false;
                    }
                    break;
                case SINGLE_OPTION_RESULT: //单选和多选一样
                    var oOptionList = fieldList.eq(i).children('.content-group');

                    var optionTitleList = [{}];
                    for (var j = 0; j < oOptionList.length; j++) {   //选择项A.C.D等放入json数组中
                        var optionKey = oOptionList.eq(j).find('span').text();
                        var optionTitleContent = oOptionList.eq(j).find('input').val();
                        optionTitleList[j] = {
                            'optionKey': optionKey,
                            'optionTitleContent': optionTitleContent
                        }

                        if (optionTitleContent.length == 0 || optionTitleContent.length >= 20) {
                            canSubmit = false;
                        }
                    }

                    evaluationFieldList[i] = {   //封装每一个单选的评价标准
                        'fieldContent': fieldContent,
                        'resultType': SINGLE_OPTION_RESULT,
                        'optionTitleList': optionTitleList
                    };

                    break;

                case MULTIPLE_OPTION_RESULT: //多选
                    var oOptionList = fieldList.eq(i).children('.content-group');

                    var optionTitleList = [{}];
                    for (var j = 0; j < oOptionList.length; j++) {   //选择项A.C.D等放入json数组中
                        var optionKey = oOptionList.eq(j).find('span').text();
                        var optionTitleContent = oOptionList.eq(j).find('input').val();
                        optionTitleList[j] = {
                            'optionKey': optionKey,
                            'optionTitleContent': optionTitleContent
                        }

                        if (optionTitleContent.length == 0 || optionTitleContent.length >= 20) {
                            canSubmit = false;
                        }
                    }

                    evaluationFieldList[i] = {   //封装每一个单选的评价标准
                        'fieldContent': fieldContent,
                        'resultType': MULTIPLE_OPTION_RESULT,
                        'optionTitleList': optionTitleList
                    };

                    break;
                case TEXT_RESULT:  //文本
                    evaluationFieldList[i] = {
                        'fieldContent': fieldContent,
                        'resultType': TEXT_RESULT
                    };
                    if (fieldContent.length == 0 || fieldContent.length >= 20) {
                        canSubmit = false;
                    }
                    break;
            }
        }    //evaluationFieldList取值完毕

        if (!canSubmit) {
            alert('输入格式有误！');
            return false;
        }

        data['evaluationFieldList'] = JSON.stringify(evaluationFieldList);
        /**
         *
         * 提交数据
         *
         */
        $.ajax({
            type: 'POST',
            url: ADDEVALUATION,
            contentType: "application/json",
            data: data,
            success: function (data) {
                var code = data.code,
                    errMsg = data.errMsg,
                    reason = data.reason,
                    success = data.success;
                if (code === 1) {
                    if (success === true) {
                        //成功后执行的业务
                        alert('设置成功！');
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
     * 是否分组控制按钮
     */
    $('#isDivide').click(function () {
        var value = $('#isDivide').attr('value');
        if (value == 1) {
            $('#divide').css('display', 'none');
            $('#isDivide').attr('value', 0);
        } else {
            $('#divide').css('display', 'block');
            $('#isDivide').attr('value', 1);
        }
    });
    /**
     * 删除整个评价项字段
     */
    $(document).on('click', '.field-delete', function () {
        var oDel = $(this);
        var oParent = oDel.parents('.field-top');
        var oGrandParent = oDel.parents('form')
        oParent.remove();

        var oFieldTop = oGrandParent.find('div.field-top');
        var fieldTopLength = oFieldTop.length;

        for (var i = 0; i < fieldTopLength; i++) {
            oFieldTop.eq(i).find('span.number-left').text(i + 1 + '.');
        }


    });
    /**
     * 添加整个评价项字段
     */
    $(document).on('click', '.field-add', function () {
        var oAdd = $(this);
        var oParent = oAdd.parents('.field-top');
        var oTemplate = $('#template-evaluation').children('.field-top');
        var oTemplate1 = oTemplate.clone();
        oAdd.text('-');
        oAdd.removeClass().addClass('field-delete add-button my-text-white am-btn am-btn-danger');
        var text = oAdd.prevAll('span').text();
        var number = parseInt(text);
        var nextNumber = number + 1;

        oTemplate1.find('span').text(nextNumber + '.');
        oParent.after(oTemplate1);
    });
    /**
     * 删除评价标准的单选或多选项
     */
    $(document).on('click', '.check-delete', function () {
        var oDel = $(this);
        var oParent = oDel.parent();
        var oGrandParent = oParent.parent();
        var oBtn = oParent.next().children('button');
        var number = oParent.nextAll('.content-group').length;
        if (number != 1) {
            oParent.remove();
            oBtn.text('-');
            oBtn.removeClass().addClass("check-delete add-button my-text-white am-btn am-btn-danger");
        } else {
            oParent.remove();
        }

        var oFieldContent = oGrandParent.find('.content-group');
        var oFieldContentLength = oFieldContent.length;
        for (var i = 0; i < oFieldContentLength; i++) {
            var _oSpan = oFieldContent.eq(i).find('span');
            _oSpan.attr('value', i);
            switch (i) {
                case 0:
                    _oSpan.text('A');
                    break;
                case 1:
                    _oSpan.text('B');
                    break;
                case 2:
                    _oSpan.text('C');
                    break;
                case 3:
                    _oSpan.text('D');
                    break;
                case 4:
                    _oSpan.text('E');
                    break;
                case 5:
                    _oSpan.text('F');
                    break;
                case 6:
                    _oSpan.text('G');
                    break;
            }
        }
    });
    /**
     * 添加评价标准的单选或多选项
     */
    $(document).on('click', '.check-add', function () {
        var oAdd = $(this);
        var oParent = oAdd.parent();
        oAdd.text('-');
        oAdd.removeClass().addClass("check-delete add-button my-text-white am-btn am-btn-danger");
        //oAdd.prev().children('input').attr('disabled', 'disabled');
        var checkType = oAdd.prev().children('span').attr('value');//checkType=0,1,2,3,4分别代表A,B,C,D
        checkType = parseInt(checkType);
        var nextCheckText;
        var _boo = true;
        switch (checkType) {
            case 0:
                nextCheckText = 'B';
                break;
            case 1:
                nextCheckText = 'C';
                break;
            case 2:
                nextCheckText = 'D';
                break;
            case 3:
                nextCheckText = 'E';
                break;
            case 4:
                nextCheckText = 'F';
                break;
            case 5:
                nextCheckText = 'G';
                break;
            case 6:
                _boo = false;
                break;
        }
        if (_boo) {
            var oTemplate1 = $('#template-check').children('.content-group');
            var oTemplate = oTemplate1.clone();
            oTemplate.find('span').text(nextCheckText);
            oTemplate.find('span').attr('value', checkType + 1);
            oParent.after(oTemplate);
        } else {
            oAdd.text('+');
            oAdd.removeClass().addClass("check-add add-button my-text-white am-btn am-btn-secondary");
            //oAdd.prev().children('input').removeAttr('disabled');
            showErrMsg('最多只能添加7项！');
        }

    });
    /**
     * 下拉选择评价的样式（打分/单选/多选/文字评价）
     */
    $(document).on('change', 'select.type', function () {
        var oThis = $(this);
        var oParent = oThis.parent();
        var oNextAll = oParent.nextAll('.content-group');
        var selectedValue = oThis.children('option:selected').val();
        var intValue = parseInt(selectedValue);
        var oTemplate = $('#template-check').children().clone();
        switch (intValue) {
            case 1:
            case 2:
                if (oNextAll.length == 0) {
                    oParent.after(oTemplate);
                    break;
                } else break;
            default :
                if (oNextAll.length > 0) {
                    oNextAll.remove();
                    break;
                }

        }
    });

    /**
     * 选择保存为模板
     */
    $('#saveTemplate').click(function () {
        var value = $('#saveTemplate').attr('value');
        if (value == 1) {
            $('.template-name').css('display', 'block')
            $('#saveTemplate').attr('value', 0);
        } else {
            $('.template-name').css('display', 'none')
            $('#saveTemplate').attr('value', 1);
        }
    });


})
;