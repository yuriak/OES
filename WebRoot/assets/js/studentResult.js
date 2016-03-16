/**
 * Created by S.F.S on 2016/3/6.
 */
document.write("<script src='assets/js/configure.js'></script>");
$(function () {


    /*var token = Cookies.get('token');
    if (token == null || token == '') {
        showErrMsg("未登录，请先登录！");
        location.href = "login.html";
        return false;
    }

    $.ajax({
        type: 'POST',
        url:,
        //url: 'http://127.0.0.1:8080/OES/login', //注意：绝对路径会涉及到跨域问题
        contentType: "application/json",
        data: {},
        beforeSend: function () {
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


                } else {
                    showErrMsg(reason);
                }
            } else {
                showErrMsg(errMsg);
                window.location.reload(false);
            }
        },
        complete: function () {
        },
        error: function () {
            showServiceError();
        },
        dataType: 'json'
    });
*/
    var cHeight = document.documentElement.clientHeight;
    var cWidth = document.documentElement.clientWidth;
    var dialogHeight=$('.my-student-dialog').height();
    var top=(cHeight-dialogHeight)/2;

    $('#mask').css({'position':'fixed','width': cWidth, 'height': cHeight,'top':0+'px','left':0+'px','z-index':0});
    if(dialogHeight<cHeight){
        $('.my-student-dialog').css('top',top);
    }else{
        $('.my-student-dialog').css('top',0);
    }


    $('.student-select').click(function(){
        $('.my-student-dialog').css('display','block');
    })
    $('#mask').click(function(){
        $('.my-student-dialog').css('display','none');

    })

//图表js

    //柱状图：
    // 基于准备好的dom，初始化echarts实例
    var myChart1 = echarts.init(document.getElementById('main1'));
    // 指定图表的配置项和数据
    var option = {
        tooltip : {//提示框
            trigger: 'axis'
        },
        grid: {//直角坐标系内绘图网格
            left: '3%',
            right: '4%',
            bottom: '3%',
            containLabel: true//grid 区域是否包含坐标轴的刻度标签
        },
        xAxis : [//直角坐标系 grid 中的 x 轴，
            {
                type : 'value',//数值轴，适用于连续数据。
                boundaryGap : [0, 0.01]//坐标轴两边留白策略，类目轴和非类目轴的设置和表现不一样。
            }
        ],
        yAxis : [//直角坐标系 grid 中的 y轴，
            {
                type : 'category',//'category' 类目轴，适用于离散的类目数据，为该类型时必须通过 data 设置类目数据。
                data : ['A','B','C','D','E','F','G']
            }
        ],
        series : [//系列列表。每个系列通过 type 决定自己的图表类型
            {
                name:'人数',
                type:'bar',//柱状图
                data:[25, 38,  44, 41, 17, 8, 52]
            }
        ]
    };

    // 使用刚指定的配置项和数据显示图表。
    myChart1.setOption(option);


//饼图：
    // 基于准备好的dom，初始化echarts实例
    var myChart2 = echarts.init(document.getElementById('main2'));
    // 指定图表的配置项和数据
    var option2 = {
        tooltip : {
            trigger: 'item',
            formatter: "{a} <br/>{b} : {c} ({d}%)"
        },
        series : [
            {
                name: '访问来源',
                type: 'pie',
                radius : '55%',
                center: ['50%', '60%'],
                data:[
                    {value:335, name:'A'},
                    {value:310, name:'B'},
                    {value:234, name:'C'},
                    {value:635, name:'D'},
                    {value:548, name:'E'},
                    {value:848, name:'F'},
                    {value:1048, name:'G'}
                ],
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
    // 使用刚指定的配置项和数据显示图表。
    myChart2.setOption(option2);




    //柱状图：
    // 基于准备好的dom，初始化echarts实例
    var myChart1 = echarts.init(document.getElementById('main3'));
    // 指定图表的配置项和数据
    var option = {
        tooltip : {//提示框
            trigger: 'axis'
        },
        grid: {//直角坐标系内绘图网格
            left: '3%',
            right: '4%',
            bottom: '3%',
            containLabel: true//grid 区域是否包含坐标轴的刻度标签
        },
        xAxis : [//直角坐标系 grid 中的 x 轴，
            {
                type : 'value',//数值轴，适用于连续数据。
                boundaryGap : [0, 0.01]//坐标轴两边留白策略，类目轴和非类目轴的设置和表现不一样。
            }
        ],
        yAxis : [//直角坐标系 grid 中的 y轴，
            {
                type : 'category',//'category' 类目轴，适用于离散的类目数据，为该类型时必须通过 data 设置类目数据。
                data : ['A','B','C','D','E','F','G']
            }
        ],
        series : [//系列列表。每个系列通过 type 决定自己的图表类型
            {
                name:'人数',
                type:'bar',//柱状图
                data:[25, 38,  44, 41, 17, 8, 52]
            }
        ]
    };

    // 使用刚指定的配置项和数据显示图表。
    myChart1.setOption(option);




//限制字符个数
        $("span.my-list").each(function(){
            var maxWidth=18;
            if($(this).text().length>maxWidth){
                var html=$(this).text().substring(0,maxWidth);
                $(this).html(html+"...");
            }
        });
        var list=$("span.my-list");
        if(list.length=1){
               $("#my-span").attr("hidden","true");
        }else {
            for(var i=0;i<3;i++){
                //取前三加入进去，剩下不显示
            }
        }





});