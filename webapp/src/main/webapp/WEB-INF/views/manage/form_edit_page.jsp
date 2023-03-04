<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2018/08/07
  Time: 上午 11:05
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://www.oa.com/core" prefix="s" %>
<jsp:include page="/common/var.jsp"></jsp:include>
<html>
<head>
    <meta charset="utf-8">
    <jsp:include page="/common/var.jsp"></jsp:include>
    <jsp:include page="/common/meta.jsp"></jsp:include>
    <title>表单构造器</title>
    <jsp:include page="/common/css.jsp"></jsp:include>
    <style>

        #target {
            min-height: 400px;
            border: 1px solid #ccc;
            padding: 5px;
        }

        #target .component {
            border: 1px solid #fff;
        }

        .popover-content form {
            margin: 0 auto;
            width: 213px;
        }

        .popover-content form .btn {
            margin-right: 10px
        }

        .popover {
            max-width: 350px !important;
        }

        #source {
            min-height: 500px;
        }

    </style>
    <!--[if lt IE 9]>
    <script src="//html5shim.googlecode.com/svn/trunk/html5.js"></script>
    <![endif]-->
</head>

<body>


<div class="layui-fluid">
    <div class="layui-row">
        <div class="layui-col-md1">
            <a href="javascript:void(0)" onclick="editpage('${tab.formcmName}')">刷新</a>
        </div>
        <div class="layui-col-md1">
            <a href="javascript:void(0)" onclick="editback()">返回</a>
        </div>
        <div class="layui-col-md1">
            <a href="javascript:void(0)" onclick="editsave('${tab.formcmName}')">保存</a>
        </div>
    </div>
    <div class="layui-row layui-col-space15">
        <div class="layui-col-md6" style="overflow: scroll;height: 100%;">
            <div class="clearfix">
                <h3>表单维护-${tab.formcmTitle}</h3>
                <hr>
                <div id="build">
                    <form id="target" class="layui-form layui-form-pane">
                        <fieldset class="layui-elem" id="formtitle" data-method="offset" style="margin-top: 20px;">
                            <legend class="layui-field formtitle" id="tableId" style="min-width: 100px">${tab.formcmTitle}</legend>
                            <c:forEach items="${tablefields}" var="field">
                                ${field}
                            </c:forEach>
                        </fieldset>
                    </form>
                </div>
            </div>
        </div>

        <div class="layui-col-md5" style="overflow: scroll;height: 100%;">
            <h3>拖拽下面的组件到左侧</h3>
            <hr>
            <div class="tabbable">
                <form class="form-horizontal layui-form layui-form-pane" id="components">
                    <fieldset>
                        <div class="tab-content">
                            <div class="" id="tableselect">
                                <div class="layui-form-item component" data-method="offset">
                                    <label class="layui-form-label title" id="text_field" data-type="text">单行文本</label>
                                    <span class="layui-form-mid required" style="display:none;">*</span>
                                    <div class="layui-input-inline">
                                        <input type="text" id="text_field$value" maxlength="50"
                                               required lay-verify="required" placeholder="" autocomplete="off"
                                               class="layui-input">
                                    </div>
                                    <p class="layui-form-mid layui-word-aux" id="text_field$help">辅助说明</p>
                                </div>

                                <div class="layui-form-item component" data-method="offset">
                                    <label class="layui-form-label title" id="textarea_field" data-type="textarea">多行文本</label>
                                    <span class="layui-form-mid required" style="display:none;">*</span>
                                    <div class="layui-input-inline">
                                        <textarea id="textarea_field$value" maxlength="1024" placeholder=""
                                                  class="layui-textarea"></textarea>
                                    </div>
                                    <p class="layui-form-mid layui-word-aux" id="textarea_field$help">辅助说明</p>
                                </div>

                                <div class="layui-form-item component" data-method="offset" title="点击展开人员列表">
                                    <label class="layui-form-label title" id="user_field" data-type="user">单选人</label>
                                    <span class="layui-form-mid required" style="display:none;">*</span>
                                    <div class="layui-input-inline">
                                        <div id="user_field_Name" name="user_field" class="layui-input select-user" onclick="selectuser(this,'user')"></div>
                                        <input type="hidden" maxlength="50" id="user_field_Value" name="user_field_Value" lay-verify="required" class="layui-input">
                                    </div>
                                    <p class="layui-form-mid layui-word-aux" id="user_field$help">辅助说明</p>
                                </div>

                                <div class="layui-form-item component" data-method="offset" title="点击展开人员列表">
                                    <label class="layui-form-label title" id="users_field" data-type="users">多选人</label>
                                    <span class="layui-form-mid required" style="display:none;">*</span>
                                    <div class="layui-input-inline">
                                        <div id="users_field_Name" name="users_field" class="layui-input select-users" onclick="selectuser(this,'users')">
                                            <input type="hidden" maxlength="500" id="users_field_Value" name="users_field_Value"
                                                   required lay-verify="required" placeholder="" autocomplete="off"
                                                   class="layui-input" value="">
                                        </div>
                                    </div>
                                    <p class="layui-form-mid layui-word-aux" id="users_field$help">辅助说明</p>
                                </div>

                                <div class="layui-form-item component" data-method="offset" title="点击展开部门列表">
                                    <label class="layui-form-label title" id="dept_field" data-type="dept">单选部门</label>
                                    <span class="layui-form-mid required" style="display:none;">*</span>
                                    <div class="layui-input-inline">
                                        <div class="layui-input select-dept" onclick="selectdept(this,'dept')"></div>
                                        <input type="hidden" maxlength="200" id="dept_field_Value" name="dept_field_Value" lay-verify="required" class="layui-input">
                                    </div>
                                    <p class="layui-form-mid layui-word-aux" id="dept_field$help">辅助说明</p>
                                </div>

                                <div class="layui-form-item component" data-method="offset" title="点击展开部门列表">
                                    <label class="layui-form-label title" id="depts_field" data-type="depts">多选部门</label>
                                    <span class="layui-form-mid required" style="display:none;">*</span>
                                    <div class="layui-input-inline">
                                        <div class="layui-input select-depts" onclick="selectuser(this,'depts')">
                                            <input type="hidden" maxlength="200" id="depts_field_Value" name="users_field_Value"
                                                   required lay-verify="required" placeholder="" autocomplete="off"
                                                   class="layui-input" value="">
                                        </div>
                                    </div>
                                    <p class="layui-form-mid layui-word-aux" id="depts_field$help">辅助说明</p>
                                </div>

                                <div class="layui-form-item component" data-method="offset">
                                    <label class="layui-form-label title" id="form_field" data-type="form">携带添加(单)</label>
                                    <span class="layui-form-mid required" style="display:none;">*</span>
                                    <div class="layui-input-inline">
                                        <div id="form_field_Name" name="form_field" class="layui-input carry-add" onclick="selectform(this,'form','')">
                                            <input type="hidden" maxlength="200" id="form_field_Value" name="form_field_Value"
                                                   placeholder="写入携带表单的任务号，任务的第一个字段为显示名称" autocomplete="off" class="layui-input" value="">
                                        </div>
                                    </div>
                                    <p class="layui-form-mid layui-word-aux" id="form_field_help">辅助说明</p>
                                </div>


                                <div class="layui-form-item component" data-method="offset">
                                    <label class="layui-form-label title" id="forms_field" data-type="forms">携带添加(多)</label>
                                    <span class="layui-form-mid required" style="display:none;">*</span>
                                    <div class="layui-input-inline">
                                        <div id="forms_field_Name" name="forms_field" class="layui-input carry-adds" onclick="selectform(this,'forms','')">
                                            <input type="hidden" maxlength="200" id="forms_field_Value" name="forms_field_Value"
                                                   placeholder="写入携带表单的任务号，任务的第一个字段为显示名称" autocomplete="off" class="layui-input" value="">
                                        </div>
                                    </div>
                                    <p class="layui-form-mid layui-word-aux" id="forms_field_Help">辅助说明</p>
                                </div>

                                <div class="layui-form-item component" data-method="offset">
                                    <label class="layui-form-label title" id="select_field" data-type="select">单选</label>
                                    <span class="layui-form-mid required" style="display:none;">*</span>
                                    <div class="layui-input-inline">
                                        <select id="select_field$value" style="display: none" lay-filter="selone" maxlength="32">
                                            <option value="" selected>请选择...</option>
                                            <option value="1">选项一</option>
                                            <option value="2">选项二</option>
                                            <option value="3">选项三</option>
                                            <option value="4">选项四</option>
                                        </select>
                                        <div class="layui-form-mid layui-word-aux">数据格式：value;text</div>
                                    </div>
                                    <p class="layui-form-mid layui-word-aux" id="select_field$help">辅助说明</p>
                                </div>

                                <div class="layui-form-item component" data-method="offset">
                                    <label class="layui-form-label title" id="selects_field" data-type="selects">多选</label>
                                    <span class="layui-form-mid required" style="display:none;">*</span>
                                    <div class="layui-input-inline">
                                        <select id="selects_field$value" xm-select="selectId" xm-select-show-count="2" maxlength="256">
                                            <option value="1">选项一</option>
                                            <option value="2">选项二</option>
                                            <option value="3">选项三</option>
                                            <option value="4">选项四</option>
                                        </select>
                                        <div class="layui-form-mid layui-word-aux">数据格式：value;text</div>
                                    </div>
                                    <p class="layui-form-mid layui-word-aux" id="selects_field$help">辅助说明</p>
                                </div>

                                <div class="layui-form-item component" data-method="offset">
                                    <label class="layui-form-label title" id="date_field" data-type="date">日期选择</label>
                                    <span class="layui-form-mid required" style="display:none;">*</span>
                                    <div class="layui-input-inline">
                                        <input type="text" class="layui-input" id="date_field$value" maxlength="0"
                                               onfocus="laydate({elem:'#date_field$value',type: 'date',theme: '#fdd5004'})"
                                               placeholder="yyyy-MM-dd">
                                    </div>
                                    <p class="layui-form-mid layui-word-aux" id="date_field$help">辅助说明</p>
                                </div>

                                <div class="layui-form-item component" data-method="offset">
                                    <label class="layui-form-label title" id="datetime_field" data-type="datetime">日期时间选择</label>
                                    <span class="layui-form-mid required" style="display:none;">*</span>
                                    <div class="layui-input-inline">
                                        <input type="text" class="layui-input" id="datetime_field$value" maxlength="0"
                                               onfocus="laydate({elem:'#datetime_field$value',type: 'datetime',theme: '#fdd5004'})"
                                               placeholder="yyyy-MM-dd HH:mm:ss">
                                    </div>
                                    <p class="layui-form-mid layui-word-aux" id="datetime_field$help">辅助说明</p>
                                </div>

                                <div class="layui-form-item component" data-method="offset">
                                    <label class="layui-form-label title" id="int_field" data-type="int">整数</label>
                                    <span class="layui-form-mid required" style="display:none;">*</span>
                                    <div class="layui-input-inline">
                                        <input type="number" id="int_field$value" maxlength="20"
                                               required lay-verify="required" placeholder="" autocomplete="off"
                                               class="layui-input">
                                    </div>
                                    <p class="layui-form-mid layui-word-aux" id="int_field$help">辅助说明</p>
                                </div>

                                <div class="layui-form-item component" data-method="offset">
                                    <label class="layui-form-label title" id="decimal_field" data-type="decimal">小数</label>
                                    <span class="layui-form-mid required" style="display:none;">*</span>
                                    <div class="layui-input-inline">
                                        <input type="number" id="decimal_field$value" decimal="3" maxlength="20"
                                               required lay-verify="required" placeholder="" autocomplete="off"
                                               class="layui-input">
                                    </div>
                                    <p class="layui-form-mid layui-word-aux" id="decimal_field$help">辅助说明</p>
                                </div>

                                <div class="layui-upload upload component">
                                    <button type="button" id="upload_field" class="layui-btn title" data-type="upload">单文件上传</button>
                                    <span class="layui-form-mid required" style="display:none;">*</span>
                                    <div class="layui-upload-list">
                                        <input type="hidden" class="form-upload-ins" maxlength="256" value="">
                                        <p class="upload-text"></p>
                                    </div>
                                </div>

                                <div class="layui-upload uploads component">
                                    <button type="button" id="uploads_field" class="layui-btn layui-btn-normal uploadList title" data-type="uploads">多文件上传</button>
                                    <span class="layui-form-mid required" style="display:none;">*</span>
                                    <div class="layui-upload-list">
                                        <table class="layui-table">
                                            <thead>
                                            <tr><th>文件名</th>
                                                <th>大小</th>
                                                <th>状态</th>
                                                <th>操作</th>
                                            </tr></thead>
                                            <tbody class="uploadFildList"></tbody>
                                        </table>
                                        <input type="hidden" class="form-upload-ins" maxlength="1024" value="">
                                    </div>
                                </div>
                            </div>
                        </div>
                    </fieldset>
                </form>
            </div>
        </div>
    </div>

    <form action="form_custom/form_edit_save/${tab.formcmName}.do" method="post" id="edit_save">
        <input type="hidden" name="formcmName" value="${tab.formcmName}">
        <input type="hidden" name="field" value="">
    </form>
</div>
<jsp:include page="/common/js.jsp"></jsp:include>
<script src="/resources/js/system/system.js"></script>
<script src="/resources/js/system/fb.js"></script>
<script>
    var layTableSelect;
    layui.config({
        base: '/resources/js/admin/'
    }).extend({
        treeGrid: 'treeGrid',
        tableSelect: 'tableSelect'
    }).use(['form','code','laydate','tableSelect'], function () {
        layform = layui.form;
        laydate = layui.laydate;
        layTableSelect = layui.tableSelect;
        // 代码块
        layui.code({
            title: 'html',
            encode: true,
            about: false
        });
        layform.render();
        layform.on('select(multi)', function (data) {
            console.log(data.elem); //得到select原始DOM对象
            console.log(data.value); //得到被选中的值
            console.log(data.othis); //得到美化后的DOM对象
            console.log(data.current_value); //当前操作的值
        })
    });

    function editpage(value) {
        window.location.href = "/form_custom/formedit.do?formid=" + value;
    }
    function editback(){
        window.location.href="/form_custom/form_custom_made.do";
    }
    function editsave(formcmName){
        //判断是否是从编辑节点跳转
        if(formcmName.substring(0,9)=="flowtable"){
            //节点id
            var nodeid = "${nodeid}";
            //页面id
            var formid = "${formid}";
            var wkflwId = "${wkflwId}";
            //更新formId
            if(formid==""||formid!=formcmName){
                $.ajax({
                    type: "POST",
                    url: "/workflow/updateNodeFormId.do",
                    data: {'nodeID': nodeid,'formId': formcmName,'wkflwId':wkflwId},
                    contentType: "application/x-www-form-urlencoded; charset=utf-8",
                    dataType: "json",
                    async: false,
                    success: function (data) {
                    },
                    error: function (message) {
                    }
                });
            }
        }
        var $this = $("#formtitle");
        var $div = $this.children(".component");
        var tableId = $this.find('legend').attr('id');
        if('${tableId}'){
            tableId = '${tableId}';
        }
        var tableName = $this.find('legend').text();
        var json = {};
        var datas = []
        json.id=tableId;
        json.name=tableName;
        $div.each(function(){
            var getdata = getFieldValue($(this));
            datas.push(getdata);
        });
        json.data=datas;
        var jsons = JSON.stringify(json);
        $.ajax({
            type: "POST",
            url: "form_custom/form_edit_save/"+formcmName+".do",
            data: {"postData":jsons,"wkflwId":wkflwId},
            contentType: "application/x-www-form-urlencoded; charset=utf-8",
            dataType: "html",
            success: function (message) {
                if(formcmName.substring(0,9)=="flowtable"){
                    alert("保存成功！");
                }else{
                    editback();
                }

            },
            error: function (message) {
                $("#request-process-patent").html("提交数据失败！");
            }
        });
    }

    function selectfrom(){
        var dis = document.getElementById("selectform").style.display;
        if (dis == "none") {
            document.getElementById("selectform").style.display = "block";
        } else {
            document.getElementById("selectform").style.display = "none";
        }
    }
    function selectsfrom(){
        var dis = document.getElementById("selectsform").style.display;
        if (dis == "none") {
            document.getElementById("selectsform").style.display = "block";
        } else {
            document.getElementById("selectsform").style.display = "none";
        }
    }

    function updateinput(id, name) {
        var val = $("div[name='users_field']").val();
        if(val==""){
            var span = "<span value='" + id + "' style='background-color: #009688;color: #FFF;'> <label>" + name + "</label><i onClick='event.cancelBubble = true;removeval(this)' class='xm-iconfont icon-close remove'></i></span>";
            $("div[name='users_field']").html(span);
            val = val + id + ";";
        }else{
            layer.msg("所选人员已存在");
        }
    }

    function removeval(object){
        var obj = $(object).parent();
        var val = "";
        obj.remove();
        $("div[name='users_field']").find("span").each(function () {
            var tvalue = $(this).attr("value");
            if(val.indexOf(tvalue)<0){
                val += $(this).attr("value") + ";"
            }
        });

    }

    function usersselect(obj,val) {
        var id = $(obj).attr("id");
        layTableSelect.render({
            elem: '.users_select',
            searchKey: 'inputval',
            searchPlaceholder: '',
            table: {
                url: '/user/selectAll.do',
                cols: [[
                    {type: 'checkbox'},
                    {field: 'userName', title: 'ID', width: 100},
                    {field: 'name', title: '姓名', width: 300},
                ]]
            },
            done: function (elem, data) {
                var user = data.data;
                var values="";
                var span = "";
                for(var i=0;i<user.length;i++){
                    span += "<span value='" + user[i].userName + "' style='background-color: #009688;color: #FFF;'> <label>" + user[i].name + "</label><i onClick='event.cancelBubble = true;removeval(this)' class='xm-iconfont icon-close remove'></i></span>  ";
                    values+=user[i].userName+";";
                }
                $(obj).html(span);
                $(id+"_Value").val(values);
            }
        });
    }

</script>
</body>
</html>
