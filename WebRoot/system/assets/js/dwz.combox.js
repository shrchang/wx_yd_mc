/**
 * @author Roger Wu
 * 网络修改版：解决点击 combox 自动跳到登录界面
 */

(function($){
  var allSelectBox = [];
  var killAllBox = function(bid){
    $.each(allSelectBox, function(i){
      if (allSelectBox[i] != bid) {
        if (!$("#" + allSelectBox[i])[0]) {
          $("#op_" + allSelectBox[i]).remove();
          //allSelectBox.splice(i,1);
        } else {
          $("#op_" + allSelectBox[i]).css({ height: "", width: "" }).hide();
        }
        $(document).unbind("click", killAllBox);
      }
    });
  };
  
  $.extend($.fn, {
    comboxSelect: function(options){
      var op = $.extend({ selector: ">a" }, options);
      
      return this.each(function(){
        var box = $(this);
        var selector = $(op.selector, box);

        allSelectBox.push(box.attr("id"));
        $(op.selector, box).click(function(){
          var options = $("#op_"+box.attr("id"));
          if (options.is(":hidden")) {
            if(options.height() > 300) {
              options.css({height:"300px",'overflow-y':"scroll", 'overflow-x':"visible"});//by LZ
            }
            var top = box.offset().top+box[0].offsetHeight-0; // by LZ 原-50
            if(top + options.height() > $(window).height() - 20) {
              top =  $(window).height() - 20 - options.height();
            }
            options.css({top:top,left:box.offset().left}).show();
            killAllBox(box.attr("id"));
            $(document).click(killAllBox);
          } else {
            $(document).unbind("click", killAllBox);
            killAllBox();
          }
          return false;
        });
        $("#op_"+box.attr("id")).find(">li").comboxOption(selector, box);   
      });
    },
    comboxOption: function(selector, box){
      return this.each(function(){
        $(">a", this).click(function(){
          var $this = $(this);
          $this.parent().parent().find(".selected").removeClass("selected");
          $this.addClass("selected");
          selector.text($this.text());
          
          var $input = $("select", box);
          if ($input.val() != $this.attr("value")) {
            $input.val($this.attr("value")).trigger("refChange").trigger("change");
            removeComboxErr($input);
            //$input.trigger("click"); // by LZ 触发click, 在ie6下会使select值丢失
          }
        });
      });
    },
    combox:function(){
      /* 清理下拉层 */
      var _selectBox = [];
      $.each(allSelectBox, function(i){ 
        if ($("#" + allSelectBox[i])[0]) {
          _selectBox.push(allSelectBox[i]);
        } else {
          $("#op_" + allSelectBox[i]).remove();
        }
      });
      allSelectBox = _selectBox;
      
      return this.each(function(i){
        var $this = $(this).removeClass("combox");
        var name = $this.attr("name");
        //var value= $this.attr("value");
        var value = $this.find("option:selected").val();
        var label = $("option[value='" + value + "']",$this).text(); // by LZ 加了单引号
        var ref = $this.attr("ref");
        var refUrl = $this.attr("refUrl") || "";
        
        // by LZ 增加css宽度限制
        var style = "";
        var width = $this.attr("width") || null;
        if (width) {
          width = parseInt(width) - 28;
          style = "width:"+width+"px;overflow:hidden";
        }

        var cid = $this.attr("id") || Math.round(Math.random()*10000000);
        var select = '<div class="combox"><div id="combox_'+ cid +'" class="select"' + (ref?' ref="' + ref + '"' : '') + '>';
        select += '<a href="javascript:" class="'+$this.attr("class")+'" name="' + name +'" value="' + value + '" style="'+style+'">' + label +'</a></div></div>'; // by LZ
        var options = '<ul class="comboxop" id="op_combox_'+ cid +'">';
        $("option", $this).each(function(){
          var option = $(this);
          options +="<li><a class=\""+ (value==option[0].value?"selected":"") +"\" href=\"javascript:;\" value=\"" + option[0].value + "\">" + option[0].text + "</a></li>";
        });
        options +="</ul>";
        
        $("body").append(options);
        $this.after(select);
        $("div.select", $this.next()).comboxSelect().append($this);
        
        if (ref && refUrl) {
          
          $this.unbind("refChange").bind("refChange", function(event){
            var $ref = $("#"+ref);
            if ($ref.size() == 0) return false;
            $.ajax({
              type:'GET', dataType:"json", url:refUrl.replace("{value}", $this.find("option:selected").val()), cache: false,
              data:{},
              success: function(json){
                if (!json) return;
                var html = '';
  
                $.each(json, function(i){
                  if (json[i] && json[i].length > 1){
                    html += '<option value="'+json[i][0]+'">' + json[i][1] + '</option>';
                  }
                });
                
                var $refCombox = $ref.parents("div.combox:first");
                $ref.html(html).insertAfter($refCombox);
                $refCombox.remove();
                $ref.trigger("refChange").trigger("change").combox();
              },
              error: DWZ.ajaxError
            });
          });
        }
        
      });
    }
  });
})(jQuery);

/**
 * 设置combox的值
 */
function setComboxValue(select, val) {
  var $select = $(select);
  var $option = null;
  if (val != null) {
    $option = $select.find("option[value='"+val+"']");
  } else { // 当传入val==null时，默认选择第1个选项
    $option = $select.find("option:first");
    val = $option.val();
  }
  $option.attr("selected", true);
  var $div = $select.parents('div.select:first');
  $div.find("a").text($option.text());
  $("ul#op_"+$div.attr("id")+" li a").removeClass("selected").filter("[value='"+val+"']").addClass("selected");
}

function addComboxErr(select) {
  var $select = $(select);
  $select.addClass("error");
  $select.parents(".combox:first").addClass("comboxErr"); 
}

function removeComboxErr(select) {
  var $select = $(select);
  $select.removeClass("error");
  $select.parents(".combox:first").removeClass("comboxErr"); 
}