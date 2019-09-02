/**
 * Created by Administrator on 2017/6/30/030.
 */

$(function () {
    var basePath = $("#basePath").val();

    $('input').iCheck({
        checkboxClass: 'icheckbox_flat-red',
        radioClass: 'iradio_flat-red'
    });

    /*$(".slt-condition").on("change",function () {
       var value = $(this).val();
       if(value == 1){
           $(".box-search").show();
           $(".search-order-state").hide();
       } else {
           $(".box-search").hide();
           $(".search-order-state").show();
       }
    });*/

    //管理员退出
    $("#btn-logout").on("click",function () {
        $.ajax({
            url:"/admin/logout",
            type:"get",
            success:function () {
                window.location.href = "/admin/login";
            }
        })
    });

    //根据订单ID搜索
    $(".btn-search").on("click",function () {
        var condition = $(".input-condition").val();
        //var state = $("#state").val();
        window.location.href ="/admin/userList/1?condition=" + condition;
    })

    $(".btn-edit-order").on("click",function () {
        var id = $(this).find("span").attr("value");
        window.location.href =  "/admin/user/deleteUser/"+id;
    })

    $(".btn-del-user").on("click",function () {
        var id = $(this).find("span").attr("value");
        $(".alert-text").text("确定要删除此用户吗？");
        $("#btn-operate").off();
        $("#btn-operate").on("click",function () {
            $.ajax({
                url:"/admin/user/delete/"+id,
                type:"get",
                success:function () {
                    window.location.reload(true);
                }
            })
        });
        $("#alertModal").modal('show');
    })

});