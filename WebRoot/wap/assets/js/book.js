var g = {};

window.onload = function() {
	// touchType = ('createTouch' in document) ? 'tap' : 'click';
	g.initScroll();

    if(location.href.indexOf("dianping") > -1){
        $(".header .left,.header .right").addClass("hide");
    }

	g.scroll = new TouchScroll({
        id: 'wrapper',
        onscroll: function(){
        	g.l = $(".touchscrollelement").position().left; 
        	g.t = $(".touchscrollelement").position().top; 
        	g.areaObj.css("left",g.l);
        	g.timeObj.css("top",g.t);
        }
    })

    g.urlMsg = getURLInformation();
    $(".date-wrap a").each(function(i,item){
        if($(item).attr("data-t") == g.urlMsg.t){
            $("li",item).addClass('active')
            return false;
        }
    })
    if($(".date-wrap li.active").parent("a").index() > 3){
        $(".data-change").toggleClass("expend");
        
    }
    if($(".date-wrap li").size() == 7){
    	$(".data-change").css("background","none");
    }else if($(".date-wrap li").size() <= 4){
        $(".data-change").addClass("hide");
    }

    var loadInterval = setInterval(function() {
        if ($(".loading").attr("data-lock") == "1") {
            $(".loading").addClass("hide");
            $(".main").removeClass("hide");
            clearInterval(loadInterval);
        }
    }, 500);

    var scrollInterval = setInterval(function(){
    	var touchH = $(".touchscrollwrapper").height();
    	if(touchH == 0){
        	g.scroll.resize();
    	}
    	else{
			// setViewPos();
            clearInterval(scrollInterval);
    	}
    },100);

    $(".data-change").on("click", function() {
    	if($(".date-wrap").find("li").size() > 4){
    		var _this = $(this);
        	_this.toggleClass("expend");
            if($(".date-wrap li").size() < 7){
                if($(".data-change").hasClass('expend')){
                    $(".data-change").addClass("bg");
                }else{
                    $(".data-change").removeClass("bg");
                }
            }
    	}
    })

    if(minHour >= 2){
    	showHeadTip();
    }

    g.bNopay = parseInt($(".main").attr("data-due"));
}

g.remToPx = function(rem){
	var win = $(window).width();
	return 100/195*win*rem;
}

// 初始化场次时间样式
g.initScroll = function(){
	var oList = $(".book-list"),
		aUl = $("ul",oList),
	    ul = aUl.eq(0),
	   	wid = (g.remToPx(0.5)+1) * aUl.size();
	oList.css({"width":wid});
	g.areaObj = $(".book-area ul");
	g.areaObj.css("width",wid);
	g.timeObj = $(".book-time ul");
}

//显示toast
function showToast(errMsg) {
    $(".toast .toast-msg").text(errMsg);
    $(".toast").removeClass('hide');
    setTimeout(function(){
        $(".toast").animate({"opacity":0},300,function(){
            $(this).css("opacity",1).addClass("hide");
        })
    },1000);
}

// 显示头部提示
function showHeadTip(){
	$(".book-headTip").animate({"top":"0"},800,function(){
		setTimeout(function(){
			$(".book-headTip").animate({"top":"-0.6rem"},800)
		},2000);
	});
}


function getURLInformation() {
  var urlMsg = {}; //定义一个空对象urlMsg
  if (window.location.href.split('#')[0].split('?')[1]) {
      var urlSearch = window.location.href.split('#')[0].split('?')[1].split('&');
  }
  if (urlSearch) {
      for (var i = 0; i < urlSearch.length; i++) {
          urlMsg[urlSearch[i].split('=')[0]] = urlSearch[i].split('=')[1] || "";
      }
  }
  return urlMsg;
}