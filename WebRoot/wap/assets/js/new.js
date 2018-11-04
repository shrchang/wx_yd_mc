$(document).ready(function() {
    //滚动先关
    mainHeight();
    function mainHeight() {
        var windowH = $(window).height();
        var headerH = $("#header").height();
        var footerH = $("#footer").height() + 30;
        $("#wrapper").height(windowH - headerH - footerH);
    }

    function onCompletion() {
        // Update here your DOM
        setTimeout(function() {
            myScroll.refresh();
        }, 0);
    };
    
    //动态控制宽度
    $("body").css({
        paddingBottom: $(".footer-container").height()
    });
    $(".main-box > .main").each(function() {
        var changdiWidth = $(this).find(".changdi").eq(0).width() + 10;
        var cdLength = $(this).find(".changdi").length;
        $(this).width(changdiWidth * cdLength);
        $(this).find(".changdi").each(function() {
            $(this).css("width", 96 / cdLength + "%");
        });
    });
    var mainWidth = $(window).width();
    var changdiLength = $(".main-box > .main > .mover-time > .changdi").length;
    var changdiWidth = $(".main-box > .main > .mover-time > .changdi").eq(0).width() + 10;
    var mainLength = mainWidth * $(".main-box > .main").length;
    $(".resize").width(changdiLength * changdiWidth - 25);
    $(".main-box").width(changdiLength * changdiWidth);
    $(".wrapper").width($(window).width());
    $(".zhe").width(changdiLength * changdiWidth);
    var daylist = $("#dayList");
    var offleft = daylist.offset().left;
    // console.log(offleft);
    //点击导航切换场地
    var venueMain = $("#scroller .main");
    // venueMain.hide();
    $("#tabs-list li").click(function(event) {
        onCompletion();
        mainHeight();
        var spanStatus = $(this).children().find('.current');
        var main = $("#scroller .main").eq($(this).index());
        if (spanStatus.hasClass('no')) {
            spanStatus.removeClass('no');
            spanStatus.addClass('yes');
            $(".resize, .zhe").width($(".resize, .zhe").width() + main.width());
            main.show();
            onCompletion();
        } else {
            spanStatus.addClass('no');
            spanStatus.removeClass('yes');
            $(".resize,.zhe").width($(".resize, .zhe").width() - main.width());
            main.hide();
            onCompletion();
        }
        var minlength = $("#tabs-list li a span.yes").length;
        if (minlength < 1) {
            $(".resize,.zhe").width($(".resize, .zhe").width() + main.width());
            main.show();
            onCompletion();
            spanStatus.removeClass('no');
            spanStatus.addClass('yes');
            alert("最少选择一个店铺！");
        }
    });
    //点击选择时间段
    var boxitem = $(".main .box");
    var thisP = $(".main .box.checked").data('price'); //获取价格
    $(".main .box").click(function(event) {
        onCompletion();
        mainHeight();
        var _this = $(this);
        // console.log(_this.parent('p').index());
        var thisId = $(this).attr('name'); //获取id
        var dataMain = $(this).parents('.main').data('dian'); //获取店名称
        var mainColor = $(this).parents('.main').data('color'); //获取店颜色标示
        var timeRange = $(this).data('timerange'); //获取时间段
        var roomId = $(this).data('roomid');
        var timerangestr = $(this).data('timerangestr');
        var price = $(this).data('price');
        var changdi = $(this).parents('.changdi').data('changdi'); //获取场地
        if ($(this).hasClass('checked')) {
            $(this).removeClass('checked');
            $(".changci").find('p[name=' + thisId + ']').attr("name", thisId).remove();
        } else {
            //控制条数
            if ($(".main .box.checked").length > 3) {
                //$(this).removeClass('checked');
                //$(".changci").find('p').last().remove();
                alert("最多选择四条");
                return;
            }
            var _index = _this.parent('p').index();
            var start = new Date();
            var d = start.getDate() + _index - 1;
            start.setDate(d);
            var weekArray = ['周日', '周一', '周二', '周三', '周四', '周五', '周六']; //定义星期数组
            var week = weekArray[start.format("w")];
            $(this).addClass('checked');
            $(".changci").append('<p style="border-left:4px solid #' + mainColor + ';padding-left: 4px;" name="' + thisId + '">' +
            '<span class="mainName">' + dataMain + '</span> ' +
            '<span class="time" roomId="'+roomId+'" bookingData="'+start.format("yyyy-MM-dd")+'">' + start.format("MM月dd日") + '（' + week + '）' + '</span> ' +
            '<span class="mainItem">' + changdi + '</span>' +
            '（<span class="timeRange" timerangestr="'+timerangestr+'">' + timeRange + '</span>）' +
            '<span style="color:red">￥'+price+'</span>'+
            '</p>');
        }
        //获取价格
        shops($(this));
    });
    //设置日期
    var date = new Date();
    var yearD = date.getFullYear(); //获取年份
    var monthD = date.getMonth() + 1; //获取月份
    var dayD = date.getDate(); //获取日数
    var weekDay = date.getDay(); //获取星期索引
    var weekArray = ['周日', '周一', '周二', '周三', '周四', '周五', '周六']; //定义星期数组
    var week = weekArray[weekDay]; //获取星期
    var dateLine = $(".main-box .main .mover-time .changdi");
    // console.log(dateLine);
    //TODO COLOR
    var colorData = ['f7941d','1d57ff','e0483d','80c269','7ecef4'];
    
    $(".main-box .main").each(function(index, el) {
    	$(this).attr('data-color',colorData[index]);
        $(this).children('.mover-time').find('.changdi').each(function(index, el) {
            $(this).find('p').each(function(index, el) {
                $(this).find('a').each(function(index, el) {
                    if ($(this).hasClass('box-dt')) {
                        $(this).unbind('click');
                        // alert("已经售出！");
                    }
                    if ($(this).hasClass('box-disable')) {
                        $(this).unbind('click');
                        // alert("已经售出！");
                    }
                })
            });
        });
    })
    var str = '<li class="datetitle">日<br /> 期</li>';
    for (var i = 0, day = parseInt(dayD); i < 30; i++) {
        if (day > 31) {
            day = 1;
        }
        str += '<li>' + day + '</li>';
        day++;
    }
    str += '<li class="cs"></li>';
    $('#dayList').append(str);
    
    
    
    //表单提交
    $(".pay").click(function(event){
    	var bookingDates = "";
    	var roomIds = "";
    	var timeranges = "";
    	$(".changci p").each(function(){  
		    bookingDates += $(this).find('.time').eq(0).attr('bookingData')+"|";  
		    roomIds += $(this).find('.time').eq(0).attr('roomId')+"|";  
		    timeranges += $(this).find('.timeRange').eq(0).attr('timerangestr')+"|";  
		});
    	if(roomIds==""){
    		alert("请选择需要预定的场次");
    		return;
    	}
    	$('#roomIds').val(roomIds);
    	$('#bookingDates').val(bookingDates);
    	$('#timeRanges').val(timeranges);
    	$("#bookingForm").submit();
    });
    
});
//计算总金额
var sum = 0;

function shops(o) {
    sum += parseInt(o.hasClass('checked') ? o.data('price') : -o.data('price')); //如果选中就加选中的那个复选框的值，否则就减去
    $("#price").text(sum);
}
//给每一项
var main = $(".main .box");
for (var i = 0; i < main.length; i++) {
    main[i].setAttribute("name", i);
}
//格式化时间方法
Date.prototype.format = function(format) {
    var o = {
        "M+": this.getMonth() + 1, //month 
        "d+": this.getDate(), //day 
        "h+": this.getHours(), //hour 
        "w+": this.getDay(),
        "m+": this.getMinutes(), //minute 
        "s+": this.getSeconds(), //cond 
        "q+": Math.floor((this.getMonth() + 3) / 3), //quarter 
        "S": this.getMilliseconds() //millisecond 
    }
    if (/(y+)/.test(format)) {
        format = format.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    }
    for (var k in o) {
        if (new RegExp("(" + k + ")").test(format)) {
            format = format.replace(RegExp.$1, RegExp.$1.length == 1 ? o[k] : ("00" + o[k]).substr(("" + o[k]).length));
        }
    }
    return format;
};