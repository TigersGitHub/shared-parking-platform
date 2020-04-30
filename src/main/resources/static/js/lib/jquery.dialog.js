!function ($) {
	//1:error警告框,弹出错误提示的登录框
	$.showErr = function(str, func) {
	    // 调用show方法
	    BootstrapDialog.show({
	        type : BootstrapDialog.TYPE_DANGER,
	        title : '错误 ',
	        message : str,
	        size : BootstrapDialog.SIZE_SMALL,//size为小，默认的对话框比较宽
	        buttons : [ {// 设置关闭按钮
	            label : '关闭',
	            action : function(dialogItself) {
	                dialogItself.close();
	            }
	        } ],
	        // 对话框关闭时带入callback方法
	        onhide : func
	    });
	};
	//2、confirm确认选择框
	$.showConfirm = function(str, funcok, funcclose) {
	    BootstrapDialog.confirm({
	        title : '确认',
	        message : str,
	        type : BootstrapDialog.TYPE_WARNING, // <-- Default value is
	        // BootstrapDialog.TYPE_PRIMARY
	        closable : true, // <-- Default value is false，点击对话框以外的页面内容可关闭
	        draggable : true, // <-- Default value is false，可拖拽
	        btnCancelLabel : '取消', // <-- Default value is 'Cancel',
	        btnOKLabel : '确定', // <-- Default value is 'OK',
	        btnOKClass : 'btn-warning', // <-- If you didn't specify it, dialog type
	        size : BootstrapDialog.SIZE_SMALL,
	        // 对话框关闭的时候执行方法
	        onhide : funcclose,
	        callback : function(result) {
	            // 点击确定按钮时，result为true
	            if (result) {
	                // 执行方法
	                funcok.call();
	            }
	        }
	    });
	};
	//3、Success提示框
	$.showSuccessTimeout = function(str, func) {
	    BootstrapDialog.show({
	        type : BootstrapDialog.TYPE_SUCCESS,
	        title : '成功 ',
	        message : str,
	        size : BootstrapDialog.SIZE_SMALL,
	        buttons : [ {
	            label : '确定',
	            action : function(dialogItself) {
	                dialogItself.close();
	            }
	        } ],
	        // 指定时间内可自动关闭
	        onshown : function(dialogRef) {
	            setTimeout(function() {
	                dialogRef.close();
	            }, YUNM._set.timeout);
	        },
	        onhide : func
	    });
	};
	//4、封装showDialog方法。
	$.showDialog = function(str, theme, size, errfunc,okfunc) {
		var type = BootstrapDialog.TYPE_DEFAULT;
		if(theme&&theme!=null){
			switch(theme){
				case "text-default":
					type = BootstrapDialog.TYPE_DEFAULT;
					break;
				case "text-info":
					type = BootstrapDialog.TYPE_INFO;
					break;
				case "text-primary":
					type = BootstrapDialog.TYPE_PRIMARY;
					break;
				case "text-success":
					type = BootstrapDialog.TYPE_SUCCESS;
					break;
				case "text-warning":
					type = BootstrapDialog.TYPE_WARNING;
					break;
				case "text-danger":
					type = BootstrapDialog.TYPE_DANGER;
					break;
			}
		}else{
			type = BootstrapDialog.TYPE_DEFAULT;
		}
		if(size==null){
			size = BootstrapDialog.SIZE_NORMAL;
		}
	    // 调用show方法
	    BootstrapDialog.show({
	        type : type,
	        title : '错误 ',
	        message : str,
	        size : size,//BootstrapDialog.SIZE_SMALL,//size为小，默认的对话框比较宽
	        buttons : [ {
                label: '确定',
                action: okfunc
            },{// 设置关闭按钮
	            label : '关闭',
	            action:function(dialogItself) {
	                dialogItself.close();
	            }
	        } ],
	        // 对话框关闭时带入callback方法
	        onhide : errfunc
	    });
	};
	//5.️️封装ajaxIframeDialog方法。
	$.ajaxIframeDialog = function(title,url, func) {
		 $.ajax({
             type : 'GET',
             url : url,
             cache : false,
             success : function(response) {
            	 BootstrapDialog.show({
         	        type : BootstrapDialog.TYPE_DEFAULT,
         	        title : title,
         	        message :function(dialog) {
        	            var $message = $('<div></div>');
        	            $message.html(response);
        	            return $message;
        	        },
         	        size : BootstrapDialog.SIZE_SMALL,
         	        buttons : [ {
         	            label : '关闭',
         	            action : function(dialogItself) {
         	                dialogItself.close();
         	            }
         	        } ],
         	        // 指定时间内可自动关闭
         	        onshown : function(dialogRef) {
         	            setTimeout(function() {
         	                dialogRef.close();
         	            }, 3000);
         	        },
         	        onhide : func
         	    });
             }
		 });
	    
	};
	//6.️封装ajaxTodialog方法。
	$.fn.extend({
	    ajaxTodialog : function() {
	        return this.click(function(event) {
	            var $this = $(this);
	            //YUNM.debug("ajaxTodialog" + $this.selector);
	            var title = $this.attr("title") || $this.text();
	            var url=$this.attr("href");
	            $.ajax({
	                type : 'GET',
	                url : url,
	                cache : false,
	                success : function(response) {
	                	if (manipulating&&manipulating == 1) {
	                	    ajaxDialog = new BootstrapDialog({
	                	    	type: BootstrapDialog.TYPE_DEFAULT,
	                	        message : function(dialog) {
	                	            var $message = $('<div></div>');
	                	            $message.html(response);

	                	            return $message;
	                	        },
	                	        // 找到自定义页面上x号进行绑定close事件
	                	        onshown : function(dialogRef) {
	                	            var $button = dialogRef.getModalContent().find('button[data-widget="remove"]');
	                	            $button.on('click', {
	                	                dialogRef : dialogRef
	                	            }, function(event) {
	                	                event.data.dialogRef.close();
	                	            });
	                	        },
	                	    });
	                	    ajaxDialog.realize();
	                	    ajaxDialog.getModalHeader().hide();// header不要
	                	    ajaxDialog.getModalFooter().hide();// footer也不要
	                	    ajaxDialog.getModalBody().css('padding', 0);// 无填充
	                	    ajaxDialog.open();
	                	}else{
	                		ajaxDialog = BootstrapDialog.show({
	                			type: BootstrapDialog.TYPE_DEFAULT,
		                        message : function(dialog) {
		                            var $message = $('<div></div>');
		                            $message.html(response);// 把传回来的页面作为message返回

		                            return $message;
		                        },
		                        title : title,
	                		})
	                	}
	                }
	            });
	            event.preventDefault();
	            return false;
	        });
	    },
	});
	
	/**
     * WebFinal Is Support Html5
     * @name 		webfinalIsSupportHtml5
     * @version	1.0
     * @example	if($.isSupportHtml5()){...}
     */
    $.isSupportHtml5 = function() {
   	 var a = document.createElement("canvas");
   	 return (a.getContext && a.getContext("2d"));
    };
    /**
     * WebFinal Is Support WebSocket
     * @name 		webfinalIsSupportWebSocket
     * @version	1.0
     * @example	if($.isSupportWebSocket()){...}
     */
    $.isSupportWebSocket = function() {
   	 window.WebSocket = window.WebSocket || window.MozWebSocket;
   	 if (!window.WebSocket) {
   		 return false;
   	 }
   	 return true;
    };
    /**
	  * Webfinal Is Same Domain
	  * @name 		webfinalIsSameDomain
	  * @url 		URL参数
	  * @version 	1.0
	  * @example	if($.isSameDomain("http://127.0.0.1")){...}
	  */
	 $.isSameDomain = function(url) {
		if (url.indexOf('http://') == 0 || url.indexOf('https://') == 0) {
			var thisDomain = document.location.protocol + '://' + document.location.host + '/';
			if (url.indexOf(thisDomain) == -1) {
				return false;
			}
		}
		return true;
	 };
    /**
     * WebFinal Ajax
     * @name		webfinalAjax
     * @url 		发送请求的地址
     * @data 		发送到服务器的数据，数组存储，如：{"date": new Date().getTime(), "state": 1}
     * @async 		默认值: true。默认设置下，所有请求均为异步请求。如果需要发送同步请求，请将此选项设置为 false。
     *       		注意，同步请求将锁住浏览器，用户其它操作必须等待请求完成才可以执行。
     * @type 		请求方式("POST" 或 "GET")， 默认为 "GET"
     * @dataType 	预期服务器返回的数据类型，常用的如：xml、html、json、text
     * @successfn 	成功回调函数
     * @errorfn 	失败回调函数
     * @version	1.0
     * @example	$.webfinalAjax("http://127.0.0.1",{name:'name',value:'igdp'},true,"get","json",function(){
     * 				//success
     * 			},function(){
     * 				//error
     * 			});
     */
    $.webfinalAjax = function(url, data, async, type, dataType, successfn, errorfn) {
        async = (async==null || async=="" || typeof(async)=="undefined")? "true" : async;
        type = (type==null || type=="" || typeof(type)=="undefined")? "post" : type;
        dataType = (dataType==null || dataType=="" || typeof(dataType)=="undefined")? "json" : dataType;
        data = (data==null || data=="" || typeof(data)=="undefined")? {"date": new Date().getTime()} : data;
        $.ajax({
            type: type,
            async: async,
            data: data,
            url: url,
            dataType: dataType,
            success: function(d){
           	 if (successfn && $.isFunction(successfn)) {
           		 successfn(d);
           	 }
            },
            error: function(e){
           	 if (errorfn && $.isFunction(errorfn)) {
           		 errorfn(e);
           	 }
            }
        });
    };
    /**
     * WebFinal Ajax Post
     * @name 		webfinalPost
     * @url 		发送请求的地址
     * @data 		发送到服务器的数据，数组存储，如：{"date": new Date().getTime(), "state": 1}
     * @successfn 	成功回调函数
     * @errorfn	失败回调函数
     * @version	1.0
     * @example	$.webfinalPost("http://127.0.0.1",{name:'name',value:'igdp'},function(){
     * 				//success
     * 			},function(){
     * 				//error
     * 			});
     */
    $.webfinalPost=function(url, data, successfn,errorfn) {
        data = (data==null || data=="" || typeof(data)=="undefined")? {"date": new Date().getTime()} : data;
        $.ajax({
            type: "post",
            data: data,
            url: url,
            dataType: "json",
            success: function(d){
           	if (successfn && $.isFunction(successfn)) {
           		 successfn(d);
            	}
            },
            error: function(e){
           	if (errorfn && $.isFunction(errorfn)) {
           		errorfn(e);
           	}
            }
        });
    };
    /**
     * WebFinal Ajax Get
     * @name		webfinalGet
     * @url 		发送请求的地址
     * @data 		发送到服务器的数据，数组存储，如：{"date": new Date().getTime(), "state": 1}
     * @dataType 	预期服务器返回的数据类型，常用的如：xml、html、json、text
     * @successfn 	成功回调函数
     * @errorfn 	失败回调函数
     * @version	1.0
     * @example	$.webfinalPost("http://127.0.0.1",{name:'name',value:'igdp'},function(){
     * 				//success
     * 			},function(){
     * 				//error
     * 			});
     */
    $.webfinalGet=function(url, data, successfn, errorfn) {
        data = (data==null || data=="" || typeof(data)=="undefined")? {"date": new Date().getTime()} : data;
        $.ajax({
            type: "get",
            data: data,
            url: url,
            dataType: "json",
            success: function(d){
           	 if (successfn && $.isFunction(successfn)) {
           		 successfn(d);
           	 }
            },
            error: function(e){
           	 if (errorfn && $.isFunction(errorfn)) {
           		 errorfn(e);
           	 }
            }
        });
    };
    /**
     * Webfinal Format Date
     * @name 		webfinalFormatDate
     * @value		日期时间值
     * @format		格式化信息
     * @version	1.0
     * @example	$.webfinalFormatDate(new Date(),'yyyy-MM-dd');
     */
    $.webfinalFormatDate = function(value,format) {
	   	 if (value != null) {
	   		var c = null;
	   		if (value.time) {
	   			c = new Date(value.time);
	   		} else {
	   			c = new Date(value);
	   		}
	   		if (c != null) {
	   			if(format == null){
	   				return c.format("yyyy-MM-dd");
	   			}else{
	   				return c.format(format);
	   			}
	   		}
	   	 }
	   	 return "";
 	 };
 	 /**
 	  * Webfinal Format Number
 	  * @name		webfinalFormatNumber
 	  * @s			数字
 	  * @n			格式
 	  * @version	1.0
 	  * @example	$.webfinalFormatNumber("12345.675910", 3),返回12,345.676 
 	  */
 	 $.webfinalFormatNumber = function(s,n){
 		n = n > 0 && n <= 20 ? n : 2;
 		s = parseFloat((s + "").replace(/[^\d\.-]/g, "")).toFixed(n) + "";
 		var l = s.split(".")[0].split("").reverse(), r = s.split(".")[1];
 		t = "";
 		for (i = 0; i < l.length; i++) {
 			t += l[i] + ((i + 1) % 3 == 0 && (i + 1) != l.length ? "," : "");
 		}
 		//var d1 = number.toExponential(2);	//d1的值为5.65e+1
 		//var d2 = number.toFixed(2);		//d2的值为56.45
 		//var d3 = number.toPrecision(2);	//d3的值为56
 		return t.split("").reverse().join("") + "." + r;
 	 };
 	 /** 
 	  * Webfinal Format Currency 
 	  * @name 		webfinalFormatCurrency
 	  * @num 		数值(Number或者String) 
 	  * @version 	1.0
 	  * @example	$.webfinalFormatCurrency('1,567.45'); 壹仟伍佰陆拾柒元肆角伍分
 	  */
 	 $.webfinalFormatCurrency = function(num) {  
 		 var cnNums = new Array("零", "壹", "贰", "叁", "肆", "伍", "陆", "柒", "捌", "玖"); //汉字的数字
 		 var cnIntRadice = new Array("", "拾", "佰", "仟"); //基本单位
 		 var cnIntUnits = new Array("", "万", "亿", "兆"); //对应整数部分扩展单位
 		 var cnDecUnits = new Array("角", "分", "毫", "厘"); //对应小数部分单位
 		 var cnInteger = "整"; //整数金额时后面跟的字符
 		 var cnIntLast = "元"; //整型完以后的单位
 		 var maxNum = 999999999999999.9999; //最大处理的数字
 		 var IntegerNum; //金额整数部分
 		 var DecimalNum; //金额小数部分
 		 var ChineseStr = ""; //输出的中文金额字符串
 		 var parts; //分离金额后用的数组，预定义
 		 if (money == "") {
 			 return "";
 		 }
 		 money = parseFloat(money);
 		 if (money >= maxNum) {
 			 alert('超出最大处理数字');
 			 return "";
 		 }
 		 if (money == 0) {
 			 ChineseStr = cnNums[0] + cnIntLast + cnInteger;
 			 return ChineseStr;
 		 }
 		 money = money.toString(); //转换为字符串
 		 if (money.indexOf(".") == -1) {
 			 IntegerNum = money;
 			 DecimalNum = '';
 		 } else {
 			 parts = money.split(".");
 			 IntegerNum = parts[0];
 			 DecimalNum = parts[1].substr(0, 4);
 		 }
 		 if (parseInt(IntegerNum, 10) > 0) { //获取整型部分转换
 			 var zeroCount = 0;
 			 var IntLen = IntegerNum.length;
 			 for (var i = 0; i < IntLen; i++) {
 				 var n = IntegerNum.substr(i, 1);
 				 var p = IntLen - i - 1;
 				 var q = p / 4;
 				 var m = p % 4;
 				 if (n == "0") {
 					 zeroCount++;
 				 } else {
 					 if (zeroCount > 0) {
 						 ChineseStr += cnNums[0];
 					 }
 					 zeroCount = 0; //归零
 					 ChineseStr += cnNums[parseInt(n)] + cnIntRadice[m];
 				 }
 				 if (m == 0 && zeroCount < 4) {
 					 ChineseStr += cnIntUnits[q];
 				 }
 			 }
 			 ChineseStr += cnIntLast;
 			 //整型部分处理完毕
 		 }
 		 if (DecimalNum != '') { //小数部分
 			 var decLen = DecimalNum.length;
 			 for (var i = 0; i < decLen; i++) {
 				 var n = DecimalNum.substr(i, 1);
 				 if (n != '0') {
 					 ChineseStr += cnNums[Number(n)] + cnDecUnits[i];
 				 }
 			 }
 		 }
 		 if (ChineseStr == '') {
 			 ChineseStr += cnNums[0] + cnIntLast + cnInteger;
 		 } else if (DecimalNum == '') {
 			 ChineseStr += cnInteger;
 		 }
 		 return ChineseStr;
 	 };
 	 /**
 	  * Webfinal String to Json
 	  * @name 		webfinalStr2Json
 	  * @str		json字符串
 	  * @version	1.0
 	  * @example	var json = $.webfinalStr2Json('{}');
 	  */
 	 $.webfinalStr2Json = function(str) {
 		var json = eval("(" + str + ")");
 		return json;
 	 };
 	 /**
 	  * Webfinal JSON to String
 	  * @name 		webfinalJson2String
 	  * @str		json字符串
 	  * @version	1.0
 	  * @example	var json = $.webfinalStr2Json('{}');
 	  */
 	 $.webfinalJson2String = function(json){
 		var aToStr = JSON.stringify(json); 
 		return aToStr;
 	 }
 	 /**
 	  * Webfinal String to Number
 	  * @name		webfinalStr2Number
 	  * @strNumber	字符串
 	  * @version	1.0
 	  * @example	var num = $.webfinalStr2Number(new Date());
 	  */
 	 $.webfinalStr2Number = function(strNumber){
 		 return +strNumber;
 	 }
 	 /**
	  * Webfinal String Left Trim
	  * @name 		webfinalStringltrim	
	  * @str		字符串
	  * @version 	1.0
	  * @example	var st = $.webfinalStringltrim(str);
	  */
	 $.webfinalStringltrim = function(str) {
       return str.replace(/^\s*/,"");
	 };
	 /**
	  * Webfinal String Right Trim
	  * @name 		webfinalStringrtrim
	  * @str  		字符串
	  * @version 	1.0
	  * @example	var st = $.webfinalStringrtrim(str);
	  */
	 $.webfinalStringrtrim = function(str) {
       return str.replace(/\s*$/,"");
	 };
    /**
     * Webfinal Scroll Bar
     * @name 		webfinalScrollBar
     * @version 	1.0
     * @example	$("#divFrame").webfinalScollBar();
     */
    $.fn.webfinalScrollBar = function() {
   	 var isWebkit =  /webkit/.test(navigator.userAgent.toLowerCase());
   	 var scrollId = "#"+$(this).attr("id");
   	 if (!isWebKit) {
   		 $(scrollId).niceScroll({
   			cursorwidth : "5px",
   			cursorborder : "0",
   			scrollspeed : 40,
   			mousescrollstep : 40,
   			autohidemode : true,
   			horizrailenabled : false
   		 });
   	 } else {
   		 $(scrollId).css("overflow-y", "auto");
   	 }
    };
    /**
     * Webfinal Barcode
     * @name		webfinalBarcode	
     * @data		条形码数据
     * @type		条形码类型
     * @settings	条形码参数
     * @version	1.0
     */
	 $.fn.webfinalBarcode = function (data, type, settings) {
		 
	 };
	 
	 /**
     * Webfinal QRcode
     * @name		webfinalQRcode
     * @cavanId 	二维码div
     * @data	 	二维码数据链接
     * @settings 	二维码参数
     * @version	1.0
     */
	 $.fn.webfinalQRcode = function (cavanId, data, settings) {
		 
	 };
	 /**
	  * Webfinal Editor
	  * @name		webfinalEditor
	  * @editorId	编辑器ID
	  * @settings	编辑器参数
	  * @version	1.0
	  * @plugin		...
	  * @example	
	  */
	 $.fn.webfinalEditor = function(editorId,settings){
		 
	 };
	 /**
	  * Webfinal Ztree
	  * @name		webfinalZtree
	  * @settings	ztree参数
	  * @list		数据集合
	  * @map		显示参数对象
	  * @callback	单击事件回调函数
	  * @version	1.0
	  * @plugin		zTree core v3.5.23
	  * @example	$("#treeId").webfinalZtree({},'jsonstr',{title:'NAME',key:'ID',pkey:"PARENT_ID",type:'TYPE'},funciton(){clickevent});
	  */
	 $.fn.webfinalZtree = function(settings,data,map,callback){
		 var zTree;
		 if(settings==null){
			settings = {};
			settings.view = {
				showLine: true
			};
		 }
		 if(settings.check==null){
			 settings.check =  {
				enable:false,
				chkStyle: "checkbox",
				chkboxType: { "Y": "", "N": "" },
				radioType: "all"
						
			};
		 }
		 if(settings.data == null){
			 settings.data = {
				key:{
					title:"NAME",
					name:"NAME",
				},
				simpleData: {
					enable: true,
					idKey:"ID",
					pIdKey:"PARENT_ID",
					rootPId:"",
					type:"TYPE"
				}
			 };
		 }else{
			 //map<key.xxxx>,map<title.xxx>,map<pkey.xxx>,map<type,''>
			 if(map&&map!=undefined){
				 settings.data = {
					key:{
						title:map.title,
						name:map.title,
					},
					simpleData: {
						enable: true,
						idKey:map.key,
						pIdKey:map.pkey,
						rootPId:"",
						type:map.type
					}
				 }; 
			 }
		 }
		 if(settings.callback == null){
			settings.callback = {
				onClick: onClick
			};
		 }else{
			if(callback&&$.isFunction(callback)){
				settings.callback = {
					onClick: callback
				};
			}
		 }
		 //判断数据是否存在
		 if(data&&data!=undefined){
			//格式化数据
			var zNodes = $.webfinalStr2Json(data);
			//初始化树组件
			$.fn.zTree.init($(this),setting,zNodes);
			//返回树对象
			zTree = $.fn.zTree.getZTreeObj($(this).attr("id"));
		 }
		 return zTree;
	 };
	 /**
	  * Webfinal Validator
	  * @name		webfinalValidator
	  * @formId		表单ID
	  * @callback	回调参数
	  * @verion		1.0
	  * @plugin		nice validator
	  * @example	$("#formId").webfinalValidator(function(){验证成功操作});
	  */
	 $.fn.webfinalValidator = function(callback){
		 $(this).unbind("valid.form");
		 $(this).bind("valid.form", function() {
			 if (callback && $.isFunction(callback)) {
				 callback();
			 }
		 });
	 };
	 /**
	  * Webfinal Upload dfsupload文件上传
	  * @callback	回调函数
	  * @fileID		文件ID
	  * @fileNameId 文件名称
	  * @config		文件参数
	  * @version	1.0
	  * @plugin		plupload
	  * @example	$("#fileId").webfinalUpload();
	  */
	 $.fn.webfinalUpload = function(uploadId,callback, fileId, fileNameId,config){
	 	if (uploadId == null) {
	 		uploadId = "uploader";
	 	}
	 	if (fileId == null) {
	 		fileId = "#file_id";
	 	}
	 	if (fileNameId == null) {
	 		fileNameId = "#file_name";
	 	}
	 	if(config==null){
	 		config = {};
	 	}
	 	if(config.url==null){
	 		config.url = $.webfinalRootPath('upload/dfsupload');
	 	}
	 	if(config.max_file_size==null){
	 		config.max_file_size = '10mb';
	 	}
	 	if(config.mime_types==null){
	 		config.mime_types = [ {
	 			title : "Office files",
	 			extensions : "doc,docx,xls,xlsx,ppt,pptx"
	 		}, {
	 			title : "Image files",
	 			extensions : "jpg,gif,png,jpeg,"
	 		}, {
	 			title : "PDF files",
	 			extensions : "pdf"
	 		}, {
	 			title : "Zip files",
	 			extensions : "zip"
	 		} , {
	 			title : "Other files",
	 			extensions : "txt"
	 		} ];
	 	}
	 	var uploader = new plupload.Uploader(
	 			{
	 				runtimes : 'html5,flash,silverlight,html4',
	 				browse_button : uploadId, // you can pass an id...'uploader'
	 				url : config.url,
	 				flash_swf_url : $.webfinalRootPath('/static/js/upload/Moxie.swf'),
	 				silverlight_xap_url : $.webfinalRootPath('/static/js/upload/Moxie.xap'),
	 				filters : {
	 					max_file_size : config.max_file_size,
	 					mime_types : config.mime_types,
	 					prevent_duplicates : true
	 				// 不允许选取重复文件
	 				},
	 				init : {
	 					PostInit : function() {
	 					},
	 					FilesAdded : function(up, files) {
	 						plupload.each(files, function(file) {
	 							$(fileId).val(file.id);
	 							$(fileNameId).val(file.name);
	 						});
	 						var file = $(fileNameId).val();
	 						if (file != null && file.length > 1) {
	 							uploader.start(); // 调用实例对象的start()方法开始上传文件，当然你也可以在其他地方调用该方法
	 						} else {
	 							$.webfinalAlert({
	 								title : "<@webfinal.m 'webfinal.app.warning'/>",
	 								message : "<font color='red'><@webfinal.m 'webfinal.app.selectFile'/></font>"
	 							});
	 						}
	 					},
	 					UploadProgress : function(up, file) {
	 						// var wid = $(fileNameId).width()*(file.percent/100);
	 						// $("#sample").css("border-bottom:1px solid blue");
	 						// document.getElementById(file.id).getElementsByTagName('b')[0].innerHTML
	 						// = '<span>' + file.percent + "%</span>";
	 					},
	 					FileUploaded : function(up, file, responseObject) {
	 						if (responseObject.status == 200) {
	 							var response = JSON.parse(responseObject.response);
	 							if (response.code == "0000") {
	 								//网络错误
	 								$.webfinalAlertError("网络错误：<font color='red'>"
	 												+ response.msg + "</font>");
	 							} else if(response.code == "0002"){
	 								$.webfinalAlertError("格式错误：<font color='red'>"
	 										+ response.msg + "</font>");
	 							}else if(response.code == "0009"){
	 								$.webfinalAlertError("错误：<font color='red'>"
	 										+ response.msg + "</font>");
	 							}else {
	 								$.webfinalAlertSuccess("<font color='green'>恭喜您，上传成功！</font>");
	 								if(callback && $.isFunction(callback)){
	 									callback(response);
	 								}
	 							}
	 							// alert(responseObject.response);
	 						} else {
	 							$.webfinalAlertError("<font color='red'>对不起，上传失败！</font>");
	 						}
	 					},
	 					Error : function(up, err) {
	 						$.webfinalAlertError("Error #" + err.code + ": "+ err.message);
	 					}
	 				}
	 			});
	 	uploader.init();
	 	return uploader;
	 };
	 /**
	  * Webfinal Spinner
	  * @name 		webfinalSpinner
	  * @version	1.0
	  * @plugin		none
	  * @example	$("spiner").webfinalSpinner();
	  */
	 $.fn.webfinalSpinner = function(){
	 	$('.spinner .btn:first-of-type').on('click', function() {
	 		var input = $(this).parent().parent().find("input");
	 		var val = parseInt($(input).val())+1;
	 		$(input).val(val);
	 	    //$('.spinner input').val(parseInt($('.spinner input').val(), 10) + 1);
	 	});
	 	$('.spinner .btn:last-of-type').on('click', function() {
	 		var input = $(this).parent().parent().find("input");
	 		var val = parseInt($(input).val())-1;
	 		$(input).val(val);
	 	    //$('.spinner input').val(parseInt($('.spinner input').val(), 10) - 1);
	 	});
	 };
	 /**
	  * Webfinal Dialog
	  * @name		webfinaShowlDialog
	  * @settings	对话框参数
	  * @version	1.0
	  * @plugin		artDialog
	  * @example	$.webfinalDialog({title:"title",content:''});
	  */
	 $.webfinalShowDialog = function(settings) {
		var b = dialog(settings);
		b.show()
		return b;
	 };
	 /**
	  * Webfinal Model Dialog
	  * @name		webfinalModalDialog
	  * @settings	对话框参数
	  * @version	1.0
	  * @plugin		artDialog
	  * @example	$.webfinalModalDialog({title:'',content:''});
	  */
	 $.webfinalModalDialog = function(settings) {
		var b = BootstrapDialog.show(settings);
		return b;
	 };
	 /**
	  * Webfinal Alert
	  * @name		webfinalAlert
	  * @settings	对话款参数
	  * @version	1.0
	  * @plugin		artDialog
	  * @example	$.webfinalAlert({width});
	  */
	 $.webfinalAlert = function(settings) {
		if (settings.title == null) {
			settings.title = "系统提示";
		}
		return $.webfinalShowDialog(settings);
	 };
	 /**
	  * Webfinal Alert Success
	  * @name 		webfinalAlertSuccess
	  * @content 	对话框参数
	  * @version 	1.0
	  * @plugin		artDialog
	  * @example	$.webfinalAlertSuccess("OK");
	  */
	 $.webfinalAlertSuccess = function(content) {
		var a = {};
		a.type = BootstrapDialog.TYPE_SUCCESS;
		a.title = "操作成功";
		a.message = "<div style='padding-bottom:10px;text-align:left;color:green;'><i class='fa fa-check' style='font-size:120%;'></i>&nbsp;"+ content + "</div>";
		a.closable = false;
		a.buttons = [{
            label: '关闭',
            icon: 'fa fa-close',
            action : function(dialogItself) {
	                dialogItself.close();
	         }
        }];
		return $.webfinalModalDialog(a);
	 };
	 /**
	  * Webfinal Alert Error
	  * @name		webfinalAlertError	
	  * @content	对话框内容
	  * @version	1.0
	  * @plugin		artDialog
	  * @example	$.webfinalAlertError("Error!");
	  */
	 $.webfinalAlertError = function(content) {
		 var a = {};
			a.type = BootstrapDialog.TYPE_WARNING;
			a.title = "操作失败";
			a.message = "<div style='padding-bottom:10px;text-align:left;color:red;'><i class='fa fa-close' style='color:red;font-size:120%;'></i>&nbsp;"
				+ content + "</div>";
			a.closable = false;
			a.buttons = [{
	            label: '关闭',
	            icon: 'fa fa-close',
	            action : function(dialogItself) {
		                dialogItself.close();
		         }
	        }];
		return $.webfinalModalDialog(a)
	 };
    /**
     * Webfinal Alert Error
     * @name		webfinalAlertError
     * @content	对话框内容
     * @version	1.0
     * @plugin		artDialog
     * @example	$.webfinalAlertError("Error!");
     */
    $.webfinalAlertErrorNoCloseWindow = function(content) {
        var a = {};
        a.type = BootstrapDialog.TYPE_WARNING;
        a.title = "操作失败";
        a.message = "<div style='padding-bottom:10px;text-align:left;color:red;'><i class='fa fa-close' style='color:red;font-size:120%;'></i>&nbsp;"
            + content + "</div>";
        a.closable = false;
        a.buttons = [{
            label: '关闭',
            icon: 'fa fa-close',
            action : function(dialogItself) {
                //dialogItself.close();
            }
        }];
        return $.webfinalModalDialog(a)
    };
	 /**
	  * Webfinal Tips
	  * @name		webfinalTips
	  * @content	对话框内容
	  * @time		显示时间(毫秒)
	  * @version	1.0
	  * @plugin		artDialog
	  * @example	$.webfinalTips("hello",2000);
	  */
	 $.webfinalTips = function(content, time) {
		 var b = BootstrapDialog.show({message : content});
		 if (time == null) {
			 time = 2000;
		 }
		 setTimeout(function() {
			 b.close();
		 }, time);
	 };
	 /**
	  * Webfinal Dialog
	  * @name 		webfinalDialog
	  * @settings	参数设置
	  * @version	1.0
	  * @plugin		artDialog
	  * @example	$.webfinalDialog({title:'',content:''});
	  */
	 $.webfinalDialog = function(settings) {
		 if (settings.title == null) {
			settings.title = "系统提示"
		 }
		 if (settings.okValue == null) {
			settings.okValue = "确定"
		 }
		 if (settings.cancelValue == null) {
			settings.cancelValue = "取消"
		 }
		 if (settings.width == null) {
			settings.width = 300
		 }
		 var a = settings.url;
		 if (settings.iframe == null || settings.iframe == false) {
			settings.url = null
		 }
		 var c = dialog(settings);
		 if (a != null) {
			if (!a.startWith("http")) {
				//a = $.webfinalRootPath(a);
			}
			if (settings.iframe == null || settings.iframe == false) {
				if (settings.param == null) {
					settings.param = {}
				} else {
					settings.param = $.toJSON(settings.param)
				}
				$.ajax({
					url : a,
					async : false,
					data : settings.param,
					success : function(d) {
						c.content(d);
					},
					cache : false
				});
			}
		}
		if (settings.lock == true) {
			c.showModal()
		} else {
			c.show()
		}
		return c;
	 };
	 /**
	  * WebFinal Template
	  * @name		webfinalTemplate
	  * @templateId 模板ID
	  * @contentId  内容ID
	  * @data 		模板数据
	  * @version 	1.0
	  * @plugin		artTemplate
	  * @example	$("#xxxxxId").webfinalTemplate({name:'name',value:'igdp'},"contentId")
	  */
	 $.fn.webfinalTemplate = function(data,contentId,callback){
		 template.config("escape", false);
		 var templateId = $(this).attr("id");
		 var html = template(templateId, data);
		 if(contentId == null || contentId=="" || typeof(contentId)=="undefined"){
			 contentId = "#"+templateId+"-content";
		 }else{
			 if(!contentId.startWith("#")){
				 contentId = "#"+contentId;
			 }
		 }
		 $(contentId).empty().html(html);
		 if (callback && $.isFunction(callback)) {
			 //成功回调
			 callback(data);
		 }
		 //$("#"+contentId).empty().append(html);
	 };
	 /**
	  * Webfinal Table
	  * @name 		webfinalTable
	  * @settings 	表格ID
	  * @action		操作refresh,destory...
	  * @version 	1.0
	  * @plugin		bootstrapTable
	  * @example	$("#tableId").webfinalTable({...});或者$("#tableId").webfinalTable({...},"refresh");
	  */
	 $.fn.webfinalTable = function(param,action,config){
		 if(config == null){
			 config = {
				silent: true,  //刷新事件必须设置
			 };
		 }
		 if(action==null){
			 if(param==null){
				 $(this).bootstrapTable('refresh');
			 }else{
				 $(this).bootstrapTable('refresh', param);
			 }
		 }else{
			 $(this).bootstrapTable(action,param);
		 }
	 };
	 /**
	  * 表格事件增删改查其他显示
	  * @param add
	  * @param remove
	  * @param edit
	  * @param query
	  * @param other
	  * @returns
	  */
	 $.webfinalTableActionFormater = function(add,remove,edit,query,other){
		 var actionButtonGroup = "";
		 //查询按钮
		 var queryBtn =  '<a class="table-action-query" href="javascript:void(0)" title="查看">'
	     			+'<i class="fa fa-list-alt dzzw-color-info"></i>'
	     			+'</a>';
		 if(query!=""&&query!=null){
			 if(query!="query"){
				 queryBtn = queryBtn.replace("查看",query);
			 }
			 actionButtonGroup = actionButtonGroup+queryBtn;
		 }
		 //新增按钮
		 var addBtn =  '<a class="table-action-add" href="javascript:void(0)" title="新增">'
	     			+'<i class="fa fa-plus dzzw-color-warning"></i>'
	     			+'</a>';
		 if(add!=""&&add!=null){
			 if(add!="新增"){
				 addBtn = addBtn.replace("新增",add);
			 }
			 actionButtonGroup = actionButtonGroup+addBtn;
		 }
		 //编辑按钮
		 var editBtn = '<a class="table-action-edit dzzw-color-primary" href="javascript:void(0)" title="编辑" style="margin-left:10px;">'
				 +'<i class="fa fa-edit"></i>'
				 +'</a>';
		 if(edit!=""&&edit!=null){
			 if(edit!="编辑"){
				 editBtn = editBtn.replace("编辑",edit);
			 }
			 actionButtonGroup = actionButtonGroup+editBtn;
		 }
		 //删除按钮
		 var deleteBtn = '<a class="table-action-remove dzzw-color-danger ml10" href="javascript:void(0)" style="margin-left:10px;" title="删除" >'
					   + '<i class="fa fa-remove"></i>'
					   + '</a>';
		 if(remove!=""&&remove!=null){
			 if(remove!="删除"){
				 deleteBtn = deleteBtn.replace("删除",remove);
			 }
			 actionButtonGroup = actionButtonGroup+deleteBtn;
		 }
		 //其他自定义操作
		 if(other!=""&&other!=null){
			 actionButtonGroup = actionButtonGroup+other;
		 }
		 return actionButtonGroup;
	 };
	 /**
	  * 表格按钮事件回调
	  * @param addFun
	  * @param removeFun
	  * @param editFun
	  * @param queryFun
	  * @param otherFun
	  * @returns
	  */
	 $.webfinalTableActionEvents = function(addFun,removeFun,editFun,queryFun,otherFun){
		 return {
			    	'click .table-action-query': function (e, value, row, index) {
			    		if (queryFun && $.isFunction(queryFun)) {
			    			//查询回调
			    			queryFun(value,row,index);
			    		}
			    	},
			    	'click .table-action-add': function (e, value, row, index) {
			    		if (addFun && $.isFunction(addFun)) {
			    			//新增回调
			    			addFun(value,row,index);
			    		}
			    	},
			    	'click .table-action-edit': function (e, value, row, index) {
			    		if (editFun && $.isFunction(editFun)) {
			    			//编辑回调
			    			editFun(value,row,index);
			    		}
			    	},
			    	'click .table-action-remove': function (e, value, row, index) {
			    		if (removeFun && $.isFunction(removeFun)) {
			    			//删除回调
			    			removeFun(value,row,index);
			    		}
			    	},
			    	'click .table-action-other': function (e, value, row, index) {
			    		if (otherFun && $.isFunction(otherFun)) {
			    			//其他回调
			    			otherFun(value,row,index);
			    		}
			    	}
				};
	 };
	 /**
	  * Webfinal Pagination
	  * @name 		webfinalPagination
	  * @config		分页参数{url:'ds/query',page:1,size:10,total:0,viewPages:10,templateId:"templateId"}
	  * @version 	1.0
	  * @plugin		bootstrapPagination
	  * @example	$("#pageId").webfinalPagination({url,page:1,size:10,viewPages:10,templateId:"templateId",xname:xxx,xxx:...});
	  */
	 $.fn.webfinalPagination = function(config,callback){
		// page Id
		var pageId = $(this).attr("id");
		//页码参数
		if(config.page == null){
			//初始化页码
			config.page = 1;
		}
		//页面大小
		if(config.size == null){
			//初始化页面大小
			config.size = 10;
		}
		//可显示页面
		if(config.viewPages == null){
			config.viewPages = 10;
		}
		//总数据量
		//if(config.total == null){
		//	config.total = 0;
		//}
		//发送数据请求
		$.webfinalPost(config.url,config,function(data){
			var templateId = config.templateId;
			if(templateId!=null&&!templateId.startWith("#")){
				templateId = "#"+templateId;
			}
			//设置数据模板
			$(templateId).webfinalTemplate(data.data);
			if (callback && $.isFunction(callback)) {
				 //成功回调
				 callback(data.data);
			}
			//Page参数配置
			var options = {
				//size:"large",
				bootstrapMajorVersion:3,
				currentPage: config.page,
	    		numberOfPages: config.viewPages,
	    		totalPages: data.data.total,
	    		itemTexts: function (type, page, current) {
	    			switch (type) {
			        	case "first":
			        		return "首页";
			        	case "prev":
			        		return "上一页";
			        	case "next":
			        		return "下一页";
			        	case "last":
			        		return "末页";
			        	case "page":
			        		return page;
	    			}
	    		},
	    		onPageClicked: function (event, originalEvent, type, page) {
	    			//重置新页码,局部刷新
	    			config.page = page;
	    			$.webfinalPost(config.url,config,function(datap){
	    				var tepId = config.templateId;
	    				if(tepId!=null&&!tepId.startWith("#")){
	    					tepId = "#"+tepId;
	    				}
	    				//设置数据模板
	    				$(tepId).webfinalTemplate(datap.data);
	    				if (callback && $.isFunction(callback)) {
	    					 //成功回调
	    					 callback(datap.data);
	    				}
	    			},function(err){
	    				$.webfinalAlertError(err);
	    			});
	    		}
			};
			if(!pageId.startWith("#")){
				pageId = "#"+pageId;
			}
			if(data.data.total>0){
				$(pageId).bootstrapPaginator(options);
			}else{
				$(pageId).attr("style","display:none;");
			}
		},function(e){
			$.webfinalAlertError(e);
		});
		
	 };
	 /**
	  * Webfinal Load
	  * @name 		webfinalLoad
	  * @loaderId	页面加载器ID
	  * @url		装载的URL地址
	  * @callback 	回调函数
	  * @data		加载参数
	  * @version	1.0
	  * @plugin		none
	  * @example	$("divId").webfinalLoad("www.inspur.com",function(){},{name:'name'});
	  */
	 $.fn.webfinalLoad = function(url,callback,data){
		 if(data==null || data=="" || typeof(data)=="undefined"){
			 $(this).load(url, callback);
		 }else{
			 $(this).load(url, data, callback);
		 }
	 };
	 /**
	  * Webfinal Date Picker
	  * @name 		webfinalDatePicker
	  * @setting	参数设置
	  * @version	1.0
	  * @plugin 	bootstrapDatepicker
	  * @example	$("#dateDiv").webfinalDatePicker({});
	  */
	 $.fn.webfinalDatePicker = function(setting) {
			if (setting == null) {
				setting = {}
			}
			if (setting.format == null) {
				setting.format = "yyyy-mm-dd"
			}
			setting.autoclose = true;
			setting.language = "zh-CN";
			$(this).datetimepicker(setting);
		};
	 /**
	  * Webfinal Form Submit
	  * @name 		webfinalSubmit
	  * @url		URL地址
	  * @successfn	成功回调
	  * @errorfn	失败回调
	  * @version 	1.0
	  * @plugin		nice validator
	  * @example	$("formId").webfinalSubmit("www.inspur.com",function(){},function(){});
	  */
	 $.fn.webfinalSubmit = function(url, successfn, errorfn) {
			var a = false;
			$(this).unbind("valid.form");
			$(this).bind("valid.form", function() {
				a = true;
				$.ajax({
					url : url,
					type : "POST",
					cache : false,
					async : false,
					dataType : "json",
					data : $(this).serialize(),
					success : function(data) {
						if (data && data.state == 1) {
							if (successfn && $.isFunction(successfn)) {
								//成功回调
								successfn(data);
							}
						} else {
							if (errorfn && $.isFunction(errorfn)) {
								//失败回调
								errorfn(data);
							}else{
								$.webfinalAlertError(data.msg);
							}
						}
					},
					error : function(e) {
						if (errorfn && $.isFunction(errorfn)) {
							//成功回调
							errorfn(e);
						}else{
							$.webfinalAlertError("对不起，您提交数据信息失败！");
						}
					}
				})
			});
			$(this).trigger("submit");
			return a
	 };
	 /**
	  * Webfinal Form Submit with no valid
	  * @name 		webfinalSubmit
	  * @url		URL地址
	  * @successfn	成功回调
	  * @errorfn	失败回调
	  * @version 	1.0
	  * @plugin		nice validator
	  * @example	$("formId").webfinalSubmitWithNoValid("www.inspur.com",function(){},function(){});
	  */
	 $.fn.webfinalSubmitWithNoValid = function(url, successfn, errorfn) {
			var a = true;
				$.ajax({
					url : $.webfinalRootPath(url),
					type : "POST",
					cache : false,
					async : false,
					dataType : "json",
					data : $(this).serialize(),
					success : function(data) {
						if (data && data.state == 1) {
							if (successfn && $.isFunction(successfn)) {
								//成功回调
								successfn(data);
							}
						} else {
							if (errorfn && $.isFunction(errorfn)) {
								//失败回调
								errorfn(data);
							}else{
								$.webfinalAlertError(data.msg);
							}
						}
					},
					error : function(e) {
						if (errorfn && $.isFunction(errorfn)) {
							//成功回调
							errorfn(e);
						}else{
							$.webfinalAlertError("对不起，您提交数据信息失败！");
						}
					}
				})
			$(this).trigger("submit");
			return a
	 };
	 /**
	  * WebFinal 测试
	  */
	 $.fn.webfinalTest = function(){
		 alert($(this).attr("id")+"test:"+ (new Date()).format("yyyy-MM-dd HH:mm:ss"));
	 };
	 
	 /**
	  * Webfinal JSON对象转化URL参数
	  * @name		webfinalJson2Query
	  * @param		JSO
	  * @version	1.0
	  * @example	$.webfinalJson2Query({name:'name',value:'igdp'})
	  */
	 $.webfinalJson2Query=function(JSO) {
		 if (typeof JSO == 'object') {
			 var str = '';
			 for (var name in JSO) {
				 str += '&' + encodeURIComponent(name) + '=' + encodeURIComponent(JSO[name]);
			 }
			 return str.slice(1);
		 }
		 return "";
	 };
	 /**
	  * Webfinal Query参数转化为JSON对象
	  * @name		webfinalQuery2Json
	  * @param		查询URL参数
	  * @version	1.0
	  * @example	$.webfinalQuery2Json("&name=name&value=igdp");
	  */
	 $.webfinalQuery2Json = function(query) {
	 	if (typeof query == 'string') {
			var obj = {};
			var nvs = query.split('&');
			var len = nvs.length;
			for (var i = 0; i < len; i++) {
				var nv = nvs[i].replace(/^\s+/, '').replace(/\s+$/, '');
				if (nv) {
					nv = nv.split('=');
					obj[decodeURIComponent(nv[0])] = decodeURIComponent(nv[1]);
				}
			}
			return obj;
	 	}
	 	return null;
	 };
	 /**
	  * Webfinal Root Path
	  * @name		webfinalRootPath
	  * @url		URL
	  * @version 	1.0
	  * @example	var uri = $.webfinalRootPath("/ds/query");
	  */
	 $.webfinalRootPath = function(url){
		 if((!url.startWith("http")) && (!url.startWith('/'))){
			 url = "/"+url;
		 }
		 //init context path
		 return DEFAULT_CONTEXT_PATH + url;
	 };
	 /**
	  * Webfinal Child Menu
	  * @name		webfinalChildMenu
	  * @menuId		菜单ID
	  * @menuArray	菜单集合
	  * @version 	1.0
	  * @example	$.webfinalChildMenu("aaa",[{},{}]);
	  */
	 $.webfinalChildMenu = function(menuId,menuArray){
	 	var menu = {};
	 	var secArray = [];
	 	var thirdArray = [];
	 	//二级菜单
	 	for(var i = 0; i < menuArray.length; i++){
	 		if(menuArray[i].PARENT_CODE==menuId){
	 			menu = menuArray[i];
	 			thirdArray = [];
	 			//三级菜单
	 			for(var j = 0; j< menuArray.length; j++){
	 				if(menuArray[j].PARENT_CODE==menu.ID){
	 					thirdArray.push(menuArray[j]);
	 				}
	 			}
	 			menu.list = thirdArray;
	 			menu.childs = thirdArray.length;
	 			secArray.push(menu);
	 		}
	 	}
	 	return secArray;
	 };
	 /**
	  * Webfinal Iframe Window
	  * @name		webfinalIframeWindow
	  * @url		iframe url
	  * @version 	1.0
	  * @example	$.webfinalIframeWindow("http://aaa");
	  */
	 $.webfinalIframeWindow = function(url){
		 window.top.document.getElementById("main-frame").src= url;
	 };
	 /**
	  * Webfinal Login
	  * @name		webfinalLogin
	  * @callback   function
	  * @version 	1.0
	  * @example	var dg = $.webfinalLogin();
	  */
	 $.webfinalLogin = function(callback){
		//顶层弹出对话框
		return top.dialog({
				id: 'dzzw-login-dialog',
				title: 'loading..',
				padding: 15,
				url: $.webfinalRootPath("login?param=dialog"),
				//quickClose: true,
				//data:'fsfas',//{name:$('#i-username').val(),pwd:$("#i-password").val()}, // 给 iframe 的数据
				onshow: function () {
					//console.log('onshow');
				},
				oniframeload: function () {
					this.title('用户登录');
				},
				onclose: function () {
					//判断是否登录成功
					var result = this.returnValue;
					if(result == '1' || result == 1){
						//刷新页面
						window.location.reload();
					}
				},
				onremove: function () {
					//console.log('onremove');
				}
		}).showModal();
	 };
}(jQuery);

/**
 * 字符串以参数开始
 */
String.prototype.startWith=function(str){
	var reg=new RegExp("^"+str);     
	return reg.test(this);        
};
/**
 * 字符串以参数结尾
 */
String.prototype.endWith=function(str){     
	var reg=new RegExp(str+"$");     
	return reg.test(this);        
};
/**
 * 增加一个名为trim的函数作为String构造函数的原型对象的一个方法。
 */
String.prototype.trim = function(){
	//用正则表达式将前后空格,用空字符串替代。
	return this.replace(/(^"s*)|("s*$)/g,"");
};
/**
 * 字符串替换函数
 */
String.prototype.replaceAll  = function(s1,s2){
	//这里的gm是固定的，g可能表示global，m可能表示multiple。 
	return this.replace(new RegExp(s1,"gm"),s2);
};
/**
 * 字符串是否为空
 */
String.prototype.isNullorEmpty=function(){
	if((this==null||this==""||this==undefined||this=="undefined"))
		return true;
	else
		return false;
};
/**
 * 替换所有空格
 */
String.prototype.replaceBlank = function(){
	//这里是替换所有空格，如需替换其他则在正则里写上即可 
	var reg = /\s/g; 
	this.replace(reg,"");
};
/**
 * 将HTML转义为实体
 */
String.prototype.escapeHTML = function (){
	var s = "";
	if(this.isNullorEmpty()) return "";
	s = this.replace(/&/g, "&amp;");
	s = s.replace(/</g, "&lt;");
	s = s.replace(/>/g, "&gt;");
    s = s.replace(/ /g, "&nbsp;");
    s = s.replace(/\‘/g, "&#39;");	
    s = s.replace(/\"/g, "&quot;");
    s = s.replace(/\n/g, "<br>");
    return s;
};
/**
 * 将实体转回为HTML
 */
String.prototype.unescapeHtml=function(){
    var s = "";
    if(this.isNullorEmpty()) return "";
    s = this.replace(/&amp;/g, "&");
    s = s.replace(/&lt;/g, "<");
    s = s.replace(/&gt;/g, ">");
    s = s.replace(/&nbsp;/g, " ");
    s = s.replace(/&#39;/g, "\‘");
    s = s.replace(/&quot;/g, "\"");
    s = s.replace(/<br>/g, "\n");
    return s;
};
/**
 * HTML编码
 */
String.prototype.htmlEncode = function(){ 
	return this.replace(/&/g, "&amp;").replace(/>/g, "&gt;").replace(/</g, "&lt;").replace(/"/g, "&quot;"); 
};
/**
 * HTML解码
 */
String.prototype.htmlDecode = function(){ 
	return this.replace(/&amp;/g, "&").replace(/&gt;/g, ">").replace(/&lt;/g, "<").replace(/&quot;/g, '"'); 
};

/**
 * 1. 对Date的扩展，将 Date 转化为指定格式的String
 * 2. 月(M)、日(d)、小时(h)、分(m)、秒(s)、季度(q) 可以用 1-2 个占位符，
 * 3. 年(y)可以用 1-4 个占位符，毫秒(S)只能用 1 个占位符(是 1-3 位的数字) 
 * Example:
 * (new Date()).format("yyyy-MM-dd hh:mm:ss.S") ==> 2006-07-02 08:09:04.423
 * (new Date()).format("yyyy-M-d h:m:s.S")      ==> 2006-7-2 8:9:4.18  
 */
Date.prototype.format = function(fmt){
	var o = { 
			"M+" : this.getMonth()+1,                 //月份 
			"d+" : this.getDate(),                    //日 
			"h+" : this.getHours(),                   //小时 
			"m+" : this.getMinutes(),                 //分 
			"s+" : this.getSeconds(),                 //秒 
			"q+" : Math.floor((this.getMonth()+3)/3), //季度 
			"S"  : this.getMilliseconds()             //毫秒 
	}; 
	if(/(y+)/.test(fmt))
		fmt=fmt.replace(RegExp.$1, (this.getFullYear()+"").substr(4 - RegExp.$1.length)); 
	for(var k in o) 
	 if(new RegExp("("+ k +")").test(fmt)) 
		 fmt = fmt.replace(RegExp.$1, (RegExp.$1.length==1) ? (o[k]) : (("00"+ o[k]).substr((""+ o[k]).length))); 
	return fmt; 
};