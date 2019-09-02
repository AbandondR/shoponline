/**
 * Created by Administrator on 2017/7/2/002.
 */
$(function () {
    var basePath = $("#basePath").val();

    /*(function checkRepeated() {
        let token = $('#token_order').val();
        if(token =='') {
            alert("您已提交订单");
            return;
        }
    })();*/
    $(".logout").on("click",function () {
        $.ajax({
            url:basePath + "/user/logout",
            type:"post",
            success:function () {
                window.location.href = basePath + "/index.jsp";
            }
        })
    })

    /**
     * 选中收货地址
     */
    $(".radio-container .radio-beauty").on("click", function() {

        //取消之前选中
        $(".radio-container").find(".radio-beauty.radio-checked").removeClass("radio-checked");
       // $(".radio-container").find("input:checked").prop("checked",false);
        $(this).addClass("radio-checked");
        //$(this).prev("#addr").prop("checked",true);
        var addressId = $(this).prev("#addr").val();
        $.ajax({
            url:"/address/getAddr?addressId="+addressId,
            type:"get",
            success:function (data) {
                $(".address-name").html("收货人："+data.userName+"&nbsp;"+data.telephone);
                $(".address-detail").text("寄送至： "+data.addressDetail);
                $(".address-detail").attr("data-addressId",data.id);
            }
        })
    });


    /*$(".c-address").hover(function () {
        $(this).find(".edit-address").show();
        $(this).find(".delete-address").show();
        $(this).find(".set-default-address").show();
    },function () {
        $(this).find(".edit-address").hide();
        $(this).find(".delete-address").hide();
        $(this).find(".set-default-address").hide();
    })
*/
    var submit_flag = false;
    //提交订单
    $(".foot-submit").on("click",function () {
        //重复提交
        if(submit_flag) {
            return;
        }
        submit_flag = true;
        var addressId = $(".address-detail").attr("data-addressId");
        var commodityIds = new Array();
        var cartItemIds = new Array();
        $.each($(".list-goods"),function (i,e) {
            var commodityId = $(this).attr("data-commodityId");
            var cartItem = $(this).attr("data-itemId");
            commodityIds.push(commodityId);
            cartItemIds.push(cartItem);
        });
        postToNewTabWithArrayByjQuery("/orders/createOrder", commodityIds, cartItemIds, addressId);
        /*$.ajax({
            url:"/orders/createOrder",
            type:"post",
            data:{"addressId":addressId,"skuIds":commodityIds, "cartItems": cartItemIds },
            success:function (data) {
                if(data == 'leak_stock') {
                    alert("库存不足!请重新下单");
                    submit_flag = false;
                    return;
                }
                if(data == 'repeated'){
                    alert("请勿重复下单");
                    return;
                }
                var orderId = data.split(",")[0];
                var orderDesc = data.split(",")[1];
                var totalAmount = data.split(",")[2];
                var orderName = data.split(",")[3];
               // window.location.href = "/order/orderPay?orderId="+orderId+"&orderDesc="+orderDesc+
                   // "&totalAmount="+totalAmount+"&orderName="+orderName;
            }
        })*/
    })
    function postToNewTabWithArrayByjQuery(url, array1, array2, addressId) {
        var $form = $('<form action="' + url + '" method="POST" target="_self" style="display:none;"></form>');
        for (var element in array1) {
            $form.append('<input type="hidden" name="skuIds[]" value="' + array1[element] + '" />');
        }
        for (var element in array2) {
            $form.append('<input type="hidden" name="cartItems[]" value="' + array2[element] + '" />');
        }
        $form.append('<input type="hidden" name="addressId" value="' + addressId + '" />');
        $("body").append($form);
        $form.submit();
        $form.remove();
    };
});