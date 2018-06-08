<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>RRC信令分析</title>
<link rel="stylesheet" type="text/css" href="source/bootstrap-4/css/bootstrap.min.css" rel="stylesheet">
<link rel="stylesheet" type="text/css" href="source/bootstrap-table.css" rel="stylesheet">
<link rel="stylesheet" type="text/css" href="source/font-awesome-4.7.0/css/font-awesome.css" rel="stylesheet">
<link rel="stylesheet" type="text/css" href="source/kartik-v-bootstrap-fileinput-61c9523/css/fileinput.min.css" rel="stylesheet">
<script type="text/javascript" src="source/jquery-3.2.1.min.js"></script>
<script type="text/javascript" src="source/popper.min.js"></script>
<script type="text/javascript" src="source/bootstrap-4/js/bootstrap.min.js"></script>
<script type="text/javascript" src="source/bootstrap-table.js"></script>
<script type="text/javascript" src="source/bootstrap-table-zh-CN.js"></script>
<script type="text/javascript" src="source/kartik-v-bootstrap-fileinput-61c9523/js/fileinput.min.js"></script>
<script type="text/javascript" src="source/kartik-v-bootstrap-fileinput-61c9523/js/locales/zh.js"></script>
</head>
<body>
<div class="container-fluid">
	<div class="row">
		<div class="col-md-12">
			<div class="page-header">
				<h1>
					RRC信令分析 <small>无锡电信无线中心出品</small>
				</h1>
			</div>
			<nav class="navbar navbar-expand-lg navbar-light bg-light navbar-dark bg-dark">
				 
				<button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1">
					<span class="navbar-toggler-icon"></span>
				</button> 
				<div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
					<ul class="navbar-nav">
						<li class="nav-item dropdown active">
							 <a class="nav-link dropdown-toggle" href="http://example.com" id="navbarDropdownMenuLink" data-toggle="dropdown">数据</a>
							<div class="dropdown-menu" aria-labelledby="navbarDropdownMenuLink">
								 <a class="dropdown-item" href="#" data-toggle="modal" data-target="#exampleModal">导入文件</a> <a class="dropdown-item" href="#">导出文件</a> <a class="dropdown-item" href="#">未完</a>
								<div class="dropdown-divider">
								</div> <a class="dropdown-item" href="#">退出</a>
							</div>
						</li>
						<li class="nav-item active">
							 <a class="nav-link" href="#">图表</a>
						</li>
					</ul>
					<ul class="navbar-nav ml-md-auto">
						<li class="nav-item active">
							 <a class="nav-link" href="#">操作 <span class="sr-only">(current)</span></a>
						</li>
						<li class="nav-item dropdown">
							 <a class="nav-link dropdown-toggle" href="http://example.com" id="navbarDropdownMenuLink" data-toggle="dropdown">用户</a>
							<div class="dropdown-menu dropdown-menu-right" aria-labelledby="navbarDropdownMenuLink">
								 <a class="dropdown-item" href="#">注销</a> <a class="dropdown-item" href="#">Another action</a> <a class="dropdown-item" href="#">Something else here</a>
								<div class="dropdown-divider">
								</div> <a class="dropdown-item" href="#">Separated link</a>
							</div>
						</li>
					</ul>
				</div>
			</nav>
		</div>
	</div>
	<!-- unused -->
	<div class="row">
		<div class="col-md-12">
		</div>
	</div>
 
	<div class="row">
		<div class="col-md-12">
			<div id="toolbar">
		    </div>
			 <table id="table"
	           data-toolbar="#toolbar"
	           data-search="true"
	           data-show-refresh="true"
	           data-show-toggle="true"
	           data-show-columns="true"
	           data-detail-view="true"
	           data-detail-formatter="detailFormatter"
	           data-minimum-count-columns="2"
	           data-show-pagination-switch="true"
	           data-pagination="true"
	           data-id-field="id"
	           data-page-list="[10, 25, 50, 100, ALL]"
	           data-show-footer="false"
	           data-side-pagination="server"
	           data-url="/examples/bootstrap_table/data"
	           data-response-handler="responseHandler">
		    </table>
    	</div>
		<script>
		    var $table = $('#table'),
		        $remove = $('#remove'),
		        selections = [];
		    function initTable() {
		        $table.bootstrapTable({
		            height: getHeight(),
		            columns: [
		                [
		                    {
		                        field: 'state',
		                        checkbox: true,
		                        rowspan: 2,
		                        align: 'center',
		                        valign: 'middle'
		                    }, {
		                        title: 'Item ID',
		                        field: 'id',
		                        rowspan: 2,
		                        align: 'center',
		                        valign: 'middle',
		                        sortable: true,
		                        footerFormatter: totalTextFormatter
		                    }, {
		                        title: 'Item Detail',
		                        colspan: 3,
		                        align: 'center'
		                    }
		                ],
		                [
		                    {
		                        field: 'name',
		                        title: 'Item Name',
		                        sortable: true,
		                        footerFormatter: totalNameFormatter,
		                        align: 'center'
		                    }, {
		                        field: 'price',
		                        title: 'Item Price',
		                        sortable: true,
		                        align: 'center',
		                        footerFormatter: totalPriceFormatter
		                    }, {
		                        field: 'operate',
		                        title: 'Item Operate',
		                        align: 'center',
		                        events: operateEvents,
		                        formatter: operateFormatter
		                    }
		                ]
		            ]
		        });
		        // sometimes footer render error.
		        setTimeout(function () {
		            $table.bootstrapTable('resetView');
		        }, 200);
		        $table.on('check.bs.table uncheck.bs.table ' +
		                'check-all.bs.table uncheck-all.bs.table', function () {
		            $remove.prop('disabled', !$table.bootstrapTable('getSelections').length);
		            // save your data, here just save the current page
		            selections = getIdSelections();
		            // push or splice the selections if you want to save all data selections
		        });
		        $table.on('expand-row.bs.table', function (e, index, row, $detail) {
		            if (index % 2 == 1) {
		                $detail.html('Loading from ajax request...');
		                $.get('LICENSE', function (res) {
		                    $detail.html(res.replace(/\n/g, '<br>'));
		                });
		            }
		        });
		        $table.on('all.bs.table', function (e, name, args) {
		            console.log(name, args);
		        });
		        $remove.click(function () {
		            var ids = getIdSelections();
		            $table.bootstrapTable('remove', {
		                field: 'id',
		                values: ids
		            });
		            $remove.prop('disabled', true);
		        });
		        $(window).resize(function () {
		            $table.bootstrapTable('resetView', {
		                height: getHeight()
		            });
		        });
		    }
		    function getIdSelections() {
		        return $.map($table.bootstrapTable('getSelections'), function (row) {
		            return row.id
		        });
		    }
		    function responseHandler(res) {
		        $.each(res.rows, function (i, row) {
		            row.state = $.inArray(row.id, selections) !== -1;
		        });
		        return res;
		    }
		    function detailFormatter(index, row) {
		        var html = [];
		        $.each(row, function (key, value) {
		            html.push('<p><b>' + key + ':</b> ' + value + '</p>');
		        });
		        return html.join('');
		    }
		    function operateFormatter(value, row, index) {
		        return [
		            '<a class="like" href="javascript:void(0)" title="Like">',
		            '<i class="fa fa-heart-o"></i>',
		            '</a>  ',
		            '<a class="remove" href="javascript:void(0)" title="Remove">',
		            '<i class="fa fa-trash"></i>',
		            '</a>'
		        ].join('');
		    }
		    window.operateEvents = {
		        'click .like': function (e, value, row, index) {
		            alert('You click like action, row: ' + JSON.stringify(row));
		        },
		        'click .remove': function (e, value, row, index) {
		            $table.bootstrapTable('remove', {
		                field: 'id',
		                values: [row.id]
		            });
		        }
		    };
		    function totalTextFormatter(data) {
		        return 'Total';
		    }
		    function totalNameFormatter(data) {
		        return data.length;
		    }
		    function totalPriceFormatter(data) {
		        var total = 0;
		        $.each(data, function (i, row) {
		            total += +(row.price.substring(1));
		        });
		        return '$' + total;
		    }
		    function getHeight() {
		        return $(window).height() - $('h1').outerHeight(true);
		    }
		    $(function () {
		    	var location = (window.location+'').split('/'); 
				var basePath = location[0]+'//'+location[2]+'/'+location[3]; 
		        initTable();
		        initUpload("excelFile", "/ajax/upload");
			    function initUpload(ctrlName, uploadUrl) {
			        var control = $('#' + ctrlName);
			        control.fileinput({
			            language: 'zh', //设置语言
			            uploadUrl: uploadUrl, //上传的地址
			            uploadAsync: true, //默认异步上传
			            showCaption: true,//是否显示标题
			            showUpload: true, //是否显示上传按钮
			            browseClass: "btn btn-primary", //按钮样式
			            dropZoneEnabled: false,
			            //allowedFileExtensions: ["xls", "xlsx","csv"], //接收的文件后缀
			            maxFileCount: 10,//最大上传文件数限制
			            previewFileIcon: '<i class="glyphicon glyphicon-file"></i>',
			            showPreview: true, //是否显示预览
			            previewFileIconSettings: {
			                'docx': '<i ass="fa fa-file-word-o text-primary"></i>',
			                'xlsx': '<i class="fa fa-file-excel-o text-success"></i>',
			                'xls': '<i class="fa fa-file-excel-o text-success"></i>',
			                'pptx': '<i class="fa fa-file-powerpoint-o text-danger"></i>',
			                'jpg': '<i class="fa fa-file-photo-o text-warning"></i>',
			                'pdf': '<i class="fa fa-file-archive-o text-muted"></i>',
			                'zip': '<i class="fa fa-file-archive-o text-muted"></i>',
			            },
			            uploadExtraData: function () {
			                var extraValue = "test";
			                return {"excelType": extraValue};
			            }
			        });
			    }
			    $("#excelFile").on("fileuploaded", function (event, data, previewId, index) {
			        console.log(data);
			        if(data.response.success == true)
			        {
			            alert(data.files[index].name + "上传成功!");
			        //关闭
			            $(".close").click();
			        }
			        else{
			            alert(data.files[index].name + "上传失败!" + data.response.message);
			        //重置
			        $("#excelFile").fileinput("clear");
			        $("#excelFile").fileinput("reset");
			        $('#excelFile').fileinput('refresh');
			        $('#excelFile').fileinput('enable');
			        }
			    });
		    });
		    
		
		</script>
	</div>
</div>

	<!-- Modal -->
	<div class="modal fade" id="exampleModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
	  <div class="modal-dialog modal-lg" role="document">
	    <div class="modal-content">
	      <div class="modal-header">
	        <h5 class="modal-title" id="exampleModalLabel">导入Excel文件</h5>
	        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
	          <span aria-hidden="true">&times;</span>
	        </button>
	      </div>
	      <div class="modal-body">
	        <form id="importFile" name="importFile" class="form-horizontal" method="post"
		          enctype="multipart/form-data">
		        <div class="box-body">
		            <div>
		                <label class="control-label">请选择要导入的Excel文件：</label>
		                <input id="excelFile" name="excelFile" class="file-loading" type="file" multiple accept=".xls,.xlsx"
		                       > <br>
		            </div>
		        </div>
		    </form>
	      </div>
	      <div class="modal-footer">
	        <button type="button" class="btn btn-primary" data-dismiss="modal">关闭</button>
	      </div>
	    </div>
	  </div>
	</div>
</body>
</html>