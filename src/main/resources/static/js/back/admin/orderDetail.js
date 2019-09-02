/**
 * Created by Administrator on 2017/6/30/030.
 */
$(function () {
    var basePath = $("#basePath").val();
    /**
     * 给订单发货
     */
    $(".btn-deliver-goods").on("click",function () {
        var orderId = $(this).attr("data-orderId");
        $.ajax({
            url:"/admin/order/updateStatus",
            type:"post",
            data:{"orderNum":orderId,"state":"待收货"},
            success:function () {
                window.location.reload(true);
            }
        })
    })
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
});