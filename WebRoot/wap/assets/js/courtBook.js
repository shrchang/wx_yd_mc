var verticalArr = [];

$(window).on("load",function(){
	var touchType = ('createTouch' in document) ? 'tap' : 'click';
	var selectCourts = {},selectCourts2 = {};
	var utils = {
		updateSum: function(){
			var sum = 0;
			$.each(selectCourts, function(i,v){
				sum += v;
			});
            var cent = '';
            var str = '';
            $('#select_court_info').html('');
            $.each(selectCourts2, function(i,v){
                var contents = v.split(",");
                str += "<li><div>"+contents[1]+"</div><div>"+contents[0]+"</div></li>"
			});
            var total = sum ? sum + "元 " : "";
			$('.J_submit div:nth-of-type(1)').html(total+"立即预定 选择更多优惠");
            $('.book-orderinfo').html(str);

            if($('.book-orderinfo li').size()>0){
            	$(".book-orderinfo").removeClass('hide');
            	$(".book-tip").addClass('hide');
            }else{
				$('.J_submit div:nth-of-type(1)').html("请选择场地");
            	$(".book-orderinfo").addClass('hide');
            	$(".book-tip").removeClass('hide');
            }
		}
	};

	var bindDOM = function(){
		$('.book-list').on("click", 'li', function(){
			if(g.bNopay > 0){
				$(".book-noPaySprite").removeClass("hide");
				return;
			}
			var el = $(this);
			var curGid = el.attr('goodsid');
			if(el.hasClass('disable')){
				showToast("该场次不可预定");
				return;
			}
			if(el.hasClass('sold')){
				showToast("该场次已出售");
				return;
			}
			verticalArr = [];

			//exist in the array, delete it
			if(selectCourts[curGid]!=undefined){
				delete selectCourts[curGid];
                delete selectCourts2[curGid];
				el.addClass('available');
				el.removeClass('selected');
				//最小起订时间限制
				if(minHour>1){
					var bindId = el.attr('bind_id'); //打包关联ID
					var col = el.parent();
					$('li[bind_id='+bindId+']').each(function(){
						var goodsId = $(this).attr('goodsid');
						delete selectCourts[goodsId];
                        delete selectCourts2[goodsId];
        				$(this).addClass('available');
        				$(this).removeClass('selected');
					});
				}
                
                //打包处理
			} else {
			    var selectNum=1;
                $.each(selectCourts, function(i,v){
    				selectNum++;
    			});
                if (selectNum > 4){
                    //utils.showError('最多选四个场次');
                    showToast("最多选择四个场次");
                    return;
                }
   				
   				//最小起订时间限制
   				if(minHour>1){
   					var currentIndex = el.index();
					var enableDown = true;
					var enableUp = true;
					var col = el.parent();
					//判断上下时段是否符合选择
					for(var i=1;i<minHour;i++){
						if(!col.find('li').eq(currentIndex+i).hasClass('available')) enableDown = false;
						if(!col.find('li').eq(currentIndex-i).hasClass('available')) enableUp = false;
					}
					if(enableDown==false && enableUp==false){
						showToast('不足两小时，无法预订');
						return;
					}
					//自动选择相邻的场地
					var bindId = 'bind_'+curGid;
					el.attr('bind_id',bindId);//设置打包关联ID
					for(i=1;i<minHour;i++){
						var nextInd = enableDown==true ? currentIndex+i : currentIndex-i;//设置方向往上或往下
						var nextTarget = col.find('li').eq(nextInd)
						var goodsId = nextTarget.attr('goodsid');
						selectCourts[goodsId] = parseInt(nextTarget.attr("price"));
		                selectCourts2[goodsId] = nextTarget.attr('course_content');
		                nextTarget.removeClass('available');
		   				nextTarget.addClass('selected');
		   				nextTarget.attr('bind_id',bindId);
					}
   				}
                
   				//push it to the array
   				selectCourts[curGid] = parseInt(el.attr("price"));
                selectCourts2[curGid] = el.attr('course_content');
   				el.removeClass('available');
   				el.addClass('selected');
			} 

			$(".book-list ul").each(function(i,item){
				$(".book-area li").eq(i).removeClass("active");
				var ul = $(item);
				$("li",ul).each(function(j,val){
					var li = $(val);
					if(li.hasClass("selected")){
						if(!$(".book-area li").eq(i).hasClass("active")){
							$(".book-area li").eq(i).addClass("active");
						}
						verticalArr.push(j);
					}
				})
			})

			$(".book-time li").removeClass("active");
			for(var k in verticalArr){
				$(".book-time li").eq(verticalArr[k]).addClass("active");
				$(".book-time li").eq(verticalArr[k]+1).addClass("active");
			}
			utils.updateSum();
		});
		
		$('.J_submit').on("click", function(e){
			e.stopPropagation();
			e.preventDefault();
			if(g.bNopay > 0){
				$(".book-noPaySprite").removeClass("hide");
				return;
			}
            var gids = [];
		    $.each(selectCourts, function(i,v){
  				gids.push(i);
   			});
            if (gids.length > 0){
                //跳转确认页面
            	var roomIdArray = [];
            	var dateArray = [];
            	$.each(selectCourts2,function(i,v){
            		var contents = v.split(",");
            		var date = new Date(Number(contents[3]));
            		var dateStr = date.getFullYear()+"-"+(date.getMonth()+1)+"-"+date.getDate();
            		roomIdArray.push(contents[2]);
            		dateArray.push(dateStr);
            	});
            	var timeRangeIds = gids.join('|');
            	var roomIds = roomIdArray.join('|');
            	var bookingDates = dateArray.join('|');
            	$('#roomIds').val(roomIds);
		    	$('#bookingDates').val(bookingDates);
		    	$('#timeRanges').val(timeRangeIds);
		    	
                $('#bookingForm').submit();
            }else{
                showToast("请选择场次");
            }
		});
	};

	bindDOM();

	$('.J_selectDay.active').trigger(touchType);
})