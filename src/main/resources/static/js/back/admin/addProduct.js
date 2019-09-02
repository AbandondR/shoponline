/**
 * Created by Administrator on 2017/6/24/024.
 */
$(function () {
    var bashPath = $("#basePath").val();

    //管理员退出
    $("#btn-logout").on("click", function () {
        $.ajax({
            url: "/admin/logout",
            type: "get",
            success: function () {
                window.location.href = "/admin/login";
            }
        })
    });

    var setting = {
        view: {
            dblClickExpand: false,//双击节点时，是否自动展开父节点的标识
            showLine: true,//是否显示节点之间的连线
            fontCss: {'color': 'black', 'font-weight': 'bold'},//字体样式函数
            selectedMulti: false //设置是否允许同时选中多个节点
        },
        check: {
            //chkboxType: { "Y": "ps", "N": "ps" },
            /*chkboxType: { "Y": "", "N": "" },*/
            chkStyle: "radio",//单选框类型
            enable: false, //每个节点上是否显示 CheckBox
            radioType: "all"
        },
        edit: {
            enable: true,
            editNameSelectAll: false,
            showRemoveBtn: false,
            showRenameBtn: false,
            removeTitle: "remove",
            renameTitle: "rename"
        },
        data: {
            key: {
                children: "childrens",
                isParent: "isParent",
                name: "cataName"
            },
            simpleData: {//简单数据模式
                enable: true,
                idKey: "id",
                pIdKey: "parentId",
                rootPId: null
            }
        },
        async: {
            enable: true,
            url: "/admin/kind/tree",
            type: "Get",
            autoParam: ["id"]
        },
        callback: {
            beforeExpand: zTreeBeforeExpand, // 用于捕获父节点展开之前的事件回调函数，并且根据返回值确定是否允许展开操作
            /*onCheck: function(event, treeId, treeNode) {
                if(treeNode.checked) {
                    $("#parentId").val(treeNode.id);
                }
                else {
                    $("#parentId").val("");
                }
                console.log($("#parentId").val())
            }*/
            onClick: treeNodeClick
        }
    };

    /**
     * 保证页面始终只打开一个分类
     * @param treeId
     * @param treeNode
     */
    function zTreeBeforeExpand(treeId, treeNode) {
        var treeObj = $.fn.zTree.getZTreeObj(treeId);
        //treeObj.
        treeObj.expandAll(false);
        return true;
    }

    /**
     * 节点点击事件
     */
    function treeNodeClick(event, treeId, treeNode) {
        //如果是叶子节点
        if (!treeNode.isParent) {
            $.ajax({
                type: "post",
                data: {"cataId": treeNode.id},
                url: "/product/proplist",
                success: function (data) {
                    initProductPanel(data);
                }
            })
        }
    }
    var zTreeObj = $.fn.zTree.init($("#CataZTree"), setting);
    //表格初始t
    function init(){
        var basePropContent = $(".base-prop-content");
        basePropContent.append("<div class='prop-cell clearfix'>"+
            "<div class='prop-name-text text'>"+
            "<span >商品名称：</span>"+
        "</div>"+
        "<div class='prop-value-text'>"+
            "<input type='text' class='input-prop' name='productName' style='width:inherit'>"+
            "</div>"+
            "</div>");
    }
    function initProductPanel(data) {

        var saleProps = data.salePropList;
        var otherProps = data.otherPropList;

        var basePropContent = $(".base-prop-content");
        var salePropContent = $(".sale-prop-content");
        var containerMiddle = $(".contianer-middle");
        var containerRight = $(".container-right");
        init();
        if (saleProps.length == 0 && otherProps.length == 0) {
            containerMiddle.attr("hidden", "hidden");
            basePropContent.empty();
            salePropContent.empty();

            containerRight.attr("hidden", "hidden");
            containerRight.empty();
            alert("该分类暂无属性");
            return;
        }
        $.each(otherProps, function (index, value) {
            var requiredFlag = false;
            if (value.isMust == '1') {
                requiredFlag = true;
            }
            var str1 =
                "<div class='prop-cell clearfix'>" +
                "<div class='prop-name-text text'>" +
                "<span >" + value.propName + "：</span>" +
                "</div>" +
                "<div class='prop-value-text'>";
            if (value.propValueList.length == 0) {
                str1 += "<input type='text' class='input-prop' style='width:inherit'></div></div>";
            }
            else {
                if (requiredFlag) {
                    str1 += "<select name='common_props' style='width:inherit' class='yjh-required'>";
                }
                else {
                    str1 += "<select name='common_props' style='width:inherit'>";
                }
                str1 += "<option value='0'>--请选择--</option>";
                var str2 = "";
                $.each(value.propValueList, function (index, val) {
                    str2 += "<option value='" + value.id + ":" + val.id + "'>" + val.propValue + "</option>";
                })
                str1 += str2;
                str1 += "</select></div></div>";
            }
            basePropContent.append(str1);
        })
        $.each(saleProps, function (index, value) {
            var sale_str_1 = "<div class='prop-cell clearfix'>" +
                "<div class=prop-name-text text>" +
                "<span >" + value.propName + ":</span>" +
                "</div>" +
                "<div class='prop-value-text sale-prop-value clearfix'>";
            var sale_str_2 = "";
            $.each(value.propValueList, function (index, val) {
                sale_str_2 += "<div class='sale-prop-cell'>" +
                    "<input class='cell' type='checkbox' name='saleProps' value='" + value.id + ":" + val.id + "'><label>" + val.propValue + "</label>" +
                    "</div>"
            })
            sale_str_1 += sale_str_2;
            sale_str_1 += "</div></div>";
            //sale_str_1 += "<button class='skuCreate'>确定</button>"
            salePropContent.append(sale_str_1);
        })
        salePropContent.append("<input class='skuCreate' type='button' value='确定'>");
        $(".skuCreate").on("click", function(){
            tableInit();
            $(".container-right").removeAttr("hidden");
        })
        containerMiddle.removeAttr("hidden");
        //containerMiddle.show();
    }

    function tableInit() {
        var saleObjs = $(".sale-prop-content .prop-cell");
        //保存数据
        var arr = new Array();
        for(var i=0; i<saleObjs.length; i++) {
            var arr1 = $(saleObjs[i]).find(".sale-prop-cell").find("input:checked");
            if(arr1.length==0) {
                return;
            }
            var temp = new Array();
            $.each(arr1, function(i, v){
                var obj = {
                    "data": $(v).val(),
                    "text": $(v).next("label").html()
                };
                temp.push(obj);
            });
            var str = $(saleObjs[i]).find(".prop-name-text").children("span").html();
            str = str.substr(0, str.length-1);
            var obj={
                "name":str,
                "values":temp
            }
            arr.push(obj);
        }
        console.log(arr);
        createTable(arr, $(".container-right"));
    }
    function createTable(arr, table) {

        var n_arr = new Array();
        for(var i=0; i < arr.length; i++) {
           n_arr.push(arr[i].values)
        }
        var str = "<table class='sku-table' border='1'><tr>";
        //表格头部
        $.each(arr, function(i ,v){
            str+="<th>"+v.name+"</th>";
        })
        str+="<th>价格</th><th>重量</th><th>库存</th><th>操作</th></tr>";

        var result = descartes(n_arr);
        var str1 = "";
        for(var i=0; i<result.length; i++) {
            var skuGroup = "";
            var str2 = "";
                for(var j=0; j<result[i].length; j++) {
                    str2 += "<td data-sku='"+result[i][j].data+"'>" + result[i][j].text+"</td>";
                    skuGroup+=result[i][j].data;
                    skuGroup += ",";
            }
            skuGroup = skuGroup.substr(0, skuGroup.length-1);
            str1 +="<tr data-skugroup='"+skuGroup+"'>";
            str1 += str2;
            str1 += "<td><input name='price' style='width:40px'></td><td ><input name='weight' style='width:40px'></td><td><input name='stock' style='width:40px'></td><td><button class='delete-sku' name='delete'>删除</button></td>";

        }
        str1 += "</table>";
        str += str1;
        table.append(str);
        $(".delete-sku").on("click", function(){
            var tr = $(this).parents("tr");
            tr.remove();
        })
    }

    $(".productPicture").fileinput({
        showUpload: false,
        // showCaption: false,
        language: 'zh', //设置语言
        maxFileCount: 4,//允许最大上传文件个数
        maxFileSize: 10240,//单位为kb，如果为0表示不限制文件大小
        dropZoneEnabled: false,//是否显示拖拽区域
        msgFilesTooMany: "选择上传的文件数量({n}) 超过允许的最大数值{m}！",
        browseClass: "btn btn-primary",
        allowedFileExtensions: ['jpg', 'png', 'gif'],//接收的文件后缀
        previewFileIcon: "<i class='glyphicon glyphicon-king'></i>"
    });

    function descartes(list)
    {
        //parent上一级索引;count指针计数
        var point  = {};
        var result = [];
        var pIndex = null;
        var tempCount = 0;
        var temp   = [];
        //根据参数列生成指针对象
        for(var index in list)
        {
            if(typeof list[index] == 'object')
            {
                point[index] = {'parent':pIndex,'count':0}
                pIndex = index;
            }
        }
        //单维度数据结构直接返回
        if(pIndex == null)
        {
            return list;
        }
        //动态生成笛卡尔积
        while(true)
        {
            for(var index in list)
            {
                tempCount = point[index]['count'];
                temp.push(list[index][tempCount]);
            }

            //压入结果数组
            result.push(temp);
            temp = [];

            //检查指针最大值问题
            while(true)
            {
                if(point[index]['count']+1 >= list[index].length)
                {
                    point[index]['count'] = 0;
                    pIndex = point[index]['parent'];
                    if(pIndex == null)
                    {
                        return result;
                    }

                    //赋值parent进行再次检查
                    index = pIndex;
                }
                else
                {
                    point[index]['count']++;
                    break;
                }
            }
        }
    }

});