/**
 * Created by Administrator on 2017/7/1/001.
 */
$(function () {
    var basePath = $("#basePath").val();

    /**
     * 立即收货
     */
   $(".a-take-goods").on("click",function () {
       var orderId = $(this).attr("data-orderId");
       var state = "待评价";
       var tradeId = $(this).attr("data-tradeId");
       $(".modal-body").text("确定要收货吗？");
       $("#btn-dismiss").text("取消");
       $("#btn-operate").text("确定");
       $('#alertModal').modal('show');
       $("#btn-operate").off();
       $("#btn-operate").on("click",function () {
           $.ajax({
               url:"/orders/updatestatus",
               type:"post",
               data:{"orderId":orderId,"state":state, "tradeId":tradeId},
               success:function () {
                   window.location.reload(true);
               }
           })
       })
   });

    /**
     * 取消订单
     */
    $(".p-order-a").on("click",function () {
        var orderId = $(this).attr("data-orderId");
        var state = "已取消";
        var tradeId = $(this).attr("data-tradeId");
        $(".modal-body").text("确定要取消订单吗？");
        $("#btn-dismiss").text("取消");
        $("#btn-operate").text("确定");
        $('#alertModal').modal('show');
        $("#btn-operate").off();
        $("#btn-operate").on("click",function () {
            $.ajax({
                url:"/orders/updatestatus",
                type:"post",
                data:{"orderId":orderId,"state":state, "tradeId":tradeId},
                success:function () {
                    window.location.reload(true);
                }
            })
        })
    })

    /**
     * 评价
     */
    $(".a-comment").on("click",function () {
        var orderId = $(this).attr("data-orderId");
        $.ajax({
            url:"/orders/istakegoods?orderId="+orderId,
            type:"get",
            success:function (data) {
                if(data == "success"){
                    window.open("/orders/addComment?orderId="+orderId);
                } else {
                    alert("订单状态错误，无法进行评论");
                }
            }
        })
    });


    /**
     * 跳转至付款页面
     */
    $(".a-pay").on("click",function () {
        var orderId = $(this).attr("data-orderId");
        postToNewTabWithArrayByjQuery("/orders/toOrderPay", orderId);
        /*$.ajax({
            url:"/orders/toOrderPay?orderId=" + orderId,
            type:"get",
            success:function (data) {
                var orderId = data.split(",")[0];
                var orderDesc = data.split(",")[1];
                var totalAmount = data.split(",")[2];
                var orderName = data.split(",")[3];
                window.open(basePath + "/order/orderPay?orderId="+orderId+"&orderDesc="+orderDesc+
                    "&totalAmount="+totalAmount+"&orderName="+orderName);
            }
        });*/
        // window.open(basePath + "/order/toOrderPay/" + orderId);
    });
    function postToNewTabWithArrayByjQuery(url, orderId) {
        var $form = $('<form action="' + url + '" method="get" target="_self" style="display:none;"></form>');
        $form.append('<input type="hidden" name="orderId" value="' + orderId+ '" />');
        $("body").append($form);
        $form.submit();
        $form.remove();
    };
});