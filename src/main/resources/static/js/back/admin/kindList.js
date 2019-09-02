/**
 * Created by Administrator on 2017/6/24/024.
 */
$(function () {
    var bashPath = $("#basePath").val();

    //管理员退出
    $("#btn-logout").on("click",function () {
        $.ajax({
            url:"/admin/logout",
            type:"get",
            success:function () {
                window.location.href =  "/admin/login";
            }
        })
    });

    //根据分类名称搜索
    $(".btn-search").on("click",function () {
        var condition = $(".input-condition").val();
        //var state = $("#state").val();
        window.location.href ="/admin/kindList/1?nameCondition=" + condition;
    })

    $(".btn-edit-kind").on("click",function () {
        var kind_id = $(this).find("span").attr("value");
        $("#input-kind-id").val(kind_id);
    });

    //修改商品品类的名称
    $("#btn-updateKind").on("click",function () {
        $("#modal-edit-kind").modal("hide");
        var id = $("#input-kind-id").val();
        var name = $("#input-kind-name").val();
        $.ajax({
            url:"/admin/kind/edit",
            type:"post",
            data:{"cataId":id,"cataName":name},
            success:function (data) {
                if(data=='repeat') {
                    $(".alert-text").text("该分类已经存在");
                    $("#alertModal").modal('show');
                    return;
                }
                if(data=='error') {
                    $(".alert-text").text("系统错误,修改失败");
                    $("#alertModal").modal('show');
                    return;
                }
                window.location.reload(true);
            }
        })
    })

    /**
     * 修改分类状态
     */
    $(".status").on("click", function() {
        var status = $(this).attr("data-status");
        var msgText;
        if(status == '1') {
            msgText = "确定禁用该分类？";
            status = '0';
        }
        else {
            msgText = "确定启用该分类？";
            status = '1';
        }
        var id = $(this).attr("data-id");
        $(".alert-text").text(msgText);
        $("#btn-operate").off();
        $("#btn-operate").on("click",function () {
            $.ajax({
                url:"/admin/kind/updateStatus",
                type:"POST",
                data:{"id":id, "status":status},
                success:function (data) {
                    if(data=='paramet_error') {
                        $(".alert-text").text("参数错误");
                        $("#alertModal").modal('show');
                        return;
                    }
                    if(data=='error') {
                        $(".alert-text").text("系统错误,修改失败");
                        $("#alertModal").modal('show');
                        return;
                    }
                    window.location.reload(true);
                }
            })
        });
        $("#alertModal").modal('show');
    })

    //添加商品品类
    $("#btn-addKind").on("click",function () {
       var name = $("#input-kindName").val();
       var status = $("input[name='status']:checked").val();
       var parentId = $("#parentId").val();
       $.ajax({
           url:"/admin/kind/add",
           type:"post",
           data:{"cataName":name, "parentId": parentId, "status":status},
           success:function (data) {
               if(data=='param_error') {
                   $(".alert-text").text("参数错误");
                   $("#alertModal").modal('show');
                   return;
               }
               if(data=='error') {
                   $(".alert-text").text("系统错误,修改失败");
                   $("#alertModal").modal('show');
                   return;
               }
               window.location.reload(true);
           }
       })
    });

    //删除商品品类
    $(".btn-del-kind").on("click",function () {
        var id = $(this).find("span").attr("value");
        $(".alert-text").text("确定要删除此商品分类吗？");
        $("#btn-operate").off();
        $("#btn-operate").on("click",function () {
            $.ajax({
                url:"/kind/"+id,
                type:"POST",
                success:function () {
                    window.location.reload(true);
                }
            })
        });
        $("#alertModal").modal('show');
    })
    
    //查看该商品品类的子品类
    $(".btn-edit-smallKind").on("click",function () {
        var id = $(this).find("span").attr("idCondition");
        var name = $(this).find("span").attr("nameCondition");
      if (!name && typeof(name)!="undefined" && name!=0)
          window.location.href = "/admin/kindList/1?idCondition=" + id + "&nameCondition=" + name;
      else
          window.location.href = "/admin/kindList/1?idCondition=" + id;
    })

    var setting = {
        view: {
            dblClickExpand: false,//双击节点时，是否自动展开父节点的标识
            showLine: true,//是否显示节点之间的连线
            fontCss:{'color':'black','font-weight':'bold'},//字体样式函数
            selectedMulti: false //设置是否允许同时选中多个节点
        },
        check:{
            //chkboxType: { "Y": "ps", "N": "ps" },
            /*chkboxType: { "Y": "", "N": "" },*/
            chkStyle: "radio",//单选框类型
            enable: true, //每个节点上是否显示 CheckBox
            radioType: "all"
        },
        edit:{
            enable: true,
            editNameSelectAll: false,
            showRemoveBtn : false,
            showRenameBtn : false,
            removeTitle : "remove",
            renameTitle : "rename"
        },
        data: {
            key: {
                children:"childrens",
                isParent:"isParent",
                name:"cataName"
            },
            simpleData: {//简单数据模式
                enable:true,
                idKey: "id",
                pIdKey: "parentId",
                rootPId: null
            }
        },
        async: {
            enable:true,
            url:"/admin/kind/tree",
            type:"Get",
            autoParam:["id"]
        },
        callback: {
            beforeExpand:zTreeBeforeExpand, // 用于捕获父节点展开之前的事件回调函数，并且根据返回值确定是否允许展开操作
            onCheck: function(event, treeId, treeNode) {
                if(treeNode.checked) {
                    $("#parentId").val(treeNode.id);
                }
                else {
                    $("#parentId").val("");
                }
                console.log($("#parentId").val())
            }
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

    zTreeObj = $.fn.zTree.init($("#CataZTree"), setting);
});