/**
 * Created by Administrator on 2017/5/24.
 */

$(function () {

    if(Number($(".list_amount .sum").val())>1 && $(".reduce").hasClass("reSty")){
        $(".reduce").removeClass("reSty");
    }
    //全局的checkbox选中和未选中的样式
    var $allCheckbox = $('input[type="checkbox"]'),     //全局的全部checkbox
        $wholeChexbox = $('.whole_check'),
        $cartBox = $('.cartBox'),                       //每个商铺盒子
        $shopCheckbox = $('.shopChoice'),               //每个商铺的checkbox
        $sonCheckBox = $('.son_check');                 //每个商铺下的商品的checkbox
    $allCheckbox.click(function () {
        if ($(this).is(':checked')) {
            $(this).next('label').addClass('mark');
        } else {
            $(this).next('label').removeClass('mark')
        }
    });

    //===============================================全局全选与单个商品的关系================================
    $wholeChexbox.click(function () {
        var $checkboxs = $cartBox.find('input[type="checkbox"]');
        if ($(this).is(':checked')) {
            $checkboxs.prop("checked", true);
            $checkboxs.next('label').addClass('mark');
        } else {
            $checkboxs.prop("checked", false);
            $checkboxs.next('label').removeClass('mark');
        }
        totalMoney();
    });


    $sonCheckBox.each(function () {
        $(this).click(function () {
            if ($(this).is(':checked')) {
                //判断：所有单个商品是否勾选
                var len = $sonCheckBox.length;
                var num = 0;
                $sonCheckBox.each(function () {
                    if ($(this).is(':checked')) {
                        num++;
                    }
                });
                if (num == len) {
                    $wholeChexbox.prop("checked", true);
                    $wholeChexbox.next('label').addClass('mark');
                }
            } else {
                //单个商品取消勾选，全局全选取消勾选
                $wholeChexbox.prop("checked", false);
                $wholeChexbox.next('label').removeClass('mark');
            }
        })
    })

    //=======================================每个店铺checkbox与全选checkbox的关系/每个店铺与其下商品样式的变化===================================================

    //店铺有一个未选中，全局全选按钮取消对勾，若店铺全选中，则全局全选按钮打对勾。
    $shopCheckbox.each(function () {
        $(this).click(function () {
            if ($(this).is(':checked')) {
                //判断：店铺全选中，则全局全选按钮打对勾。
                var len = $shopCheckbox.length;
                var num = 0;
                $shopCheckbox.each(function () {
                    if ($(this).is(':checked')) {
                        num++;
                    }
                });
                if (num == len) {
                    $wholeChexbox.prop("checked", true);
                    $wholeChexbox.next('label').addClass('mark');
                }

                //店铺下的checkbox选中状态
                $(this).parents('.cartBox').find('.son_check').prop("checked", true);
                $(this).parents('.cartBox').find('.son_check').next('label').addClass('mark');
            } else {
                //否则，全局全选按钮取消对勾
                $wholeChexbox.prop("checked", false);
                $wholeChexbox.next('label').removeClass('mark');

                //店铺下的checkbox选中状态
                $(this).parents('.cartBox').find('.son_check').prop("checked", false);
                $(this).parents('.cartBox').find('.son_check').next('label').removeClass('mark');
            }
            totalMoney();
        });
    });


    //========================================每个店铺checkbox与其下商品的checkbox的关系======================================================

    //店铺$sonChecks有一个未选中，店铺全选按钮取消选中，若全都选中，则全选打对勾
    $cartBox.each(function () {
        var $this = $(this);
        var $sonChecks = $this.find('.son_check');
        $sonChecks.each(function () {
            $(this).click(function () {
                if ($(this).is(':checked')) {
                    //判断：如果所有的$sonChecks都选中则店铺全选打对勾！
                    var len = $sonChecks.length;
                    var num = 0;
                    $sonChecks.each(function () {
                        if ($(this).is(':checked')) {
                            num++;
                        }
                    });
                    if (num == len) {
                        $(this).parents('.cartBox').find('.shopChoice').prop("checked", true);
                        $(this).parents('.cartBox').find('.shopChoice').next('label').addClass('mark');
                    }

                } else {
                    //否则，店铺全选取消
                    $(this).parents('.cartBox').find('.shopChoice').prop("checked", false);
                    $(this).parents('.cartBox').find('.shopChoice').next('label').removeClass('mark');
                }
                totalMoney();
            });
        });
    });


    //=================================================商品数量==============================================
    var $plus = $('.plus'),
        $reduce = $('.reduce'),
        $all_sum = $('.sum');
    $plus.click(function () {
        var $inputVal = $(this).prev('input'),
            $count = parseInt($inputVal.val())+1,
            $obj = $(this).parents('.amount_box').find('.reduce'),
            $priceTotalObj = $(this).parents('.order_lists').find('.sum_price');
            $price = $(this).parents('.order_lists').find('.price').html(),  //单价
            $priceTotal = $count*parseInt($price.substring(1));
        var $skuId = $(this).parent().attr("data-skuId");
        $.ajax({
            url:"/shopcart/updateItemCount",
            type:"post",
            data:{"skuId":$skuId, "itemCount":$count},
            success:function (data) {
                if(data == 'lack_stock'){
                    $(".modal-body").text("库存不足！！！");
                    $('#alertModal').modal('show');
                    return;
                }
                if(data == 'success') {
                    $inputVal.val($count);
                    $priceTotalObj.html('￥'+$priceTotal);
                    if($inputVal.val()>1 && $obj.hasClass('reSty')){
                        $obj.removeClass('reSty');
                    }
                    totalMoney();
                }
            }
        })



    });

    $reduce.click(function () {

        var $inputVal = $(this).next('input'),
            $count = parseInt($inputVal.val())-1,
            $priceTotalObj = $(this).parents('.order_lists').find('.sum_price'),
            $price = $(this).parents('.order_lists').find('.price').html(),  //单价
            $priceTotal = $count*parseInt($price.substring(1));
        var $this = $(this);
        var $skuId = $(this).parent().attr("data-skuId");
        if($inputVal.val()=='1'){
            if(!$(this).hasClass('reSty')) {
                $(this).addClass('reSty');
            }
            return;
        }
        $.ajax({
            url:"/shopcart/updateItemCount",
            type:"post",
            data:{"skuId":$skuId, "itemCount":$count},
            success:function (data) {
                if(data == 'lack_stock'){
                    $(".modal-body").text("库存不足！！！");
                    $('#alertModal').modal('show');
                    return;
                }
                if(data == 'success') {
                    $inputVal.val($count);
                    $priceTotalObj.html('￥'+$priceTotal);
                    if($inputVal.val()=='1' && !$this.hasClass('reSty')){
                        $this.addClass('reSty');
                    }
                    totalMoney();
                }
            }
        })



    });

    $all_sum.change(function () {
        var $count = 0,
            $priceTotalObj = $(this).parents('.order_lists').find('.sum_price'),
            $price = $(this).parents('.order_lists').find('.price').html(),  //单价
            $priceTotal = 0;
        var $skuId = $(this).parent().attr("data-skuId");
        var $that = $(this);
        var $reduce = $(this).next(".reduce");
        if($(this).val()=='' || parseInt($(this).val()) < 1){
            $(this).val('1');
        }
        $(this).val($(this).val().replace(/\D|^0/g,''));
        $count = $(this).val();
        $priceTotal = $count*parseInt($price.substring(1));
        $.ajax({
            url:"/shopcart/updateItemCount",
            type:"post",
            data:{"skuId":$skuId, "itemCount":$count},
            success:function (data) {
                if(data == 'lack_stock'){
                    $(".modal-body").text("库存不足！！！");
                    $('#alertModal').modal('show');
                    $count = 1;
                    $priceTotal=$count*parseInt($price.substring(1));
                    $(this).attr('value',$count);
                    $priceTotalObj.html('￥'+$priceTotal);
                    if($that.val()>1 && $reduce.hasClass('reSty')){
                        $obj.removeClass('reSty');
                    }
                    totalMoney();
                    return;
                }
                if(data == 'success') {
                    $(this).attr('value',$count);
                    $priceTotalObj.html('￥'+$priceTotal);
                    if($that.val()>1 && $reduce.hasClass('reSty')){
                        $obj.removeClass('reSty');
                    }
                    totalMoney();
                }
            }
        })

    })

    //======================================移除商品========================================

    /*var $order_lists = null;
    var $order_content = '';
    $('.delBtn').click(function () {
        $order_lists = $(this).parents('.order_lists');
        $order_content = $order_lists.parents('.order_content');
        $('.model_bg').fadeIn(300);
        $('.my_model').fadeIn(300);
    });*/

    $(".delBtn").on("click",function () {
        var skuId = $(this).attr("data-skuid");
        $(".modal-body").text("确定要从购物车中删除该商品吗？");
        $("#btn-dismiss").text("取消");
        $("#btn-operate").text("确定");
        $("#btn-operate").show();
        $('#alertModal').modal('show');
        $("#btn-operate").off();
        $("#btn-operate").on("click",function () {
            $.ajax({
                url:"/shopcart/deleteItem",
                type:"post",
                data:{"skuId":skuId},
                success:function () {
                    window.location.reload(true);
                }
            })
        })
    });
    //关闭模态框
    $('.closeModel').click(function () {
        closeM();
    });
    $('.dialog-close').click(function () {
        closeM();
    });
    function closeM() {
        $('.model_bg').fadeOut(300);
        $('.my_model').fadeOut(300);
    }
    //确定按钮，移除商品
    /*$('.dialog-sure').click(function () {
        $order_lists.remove();
        if($order_content.html().trim() == null || $order_content.html().trim().length == 0){
            $order_content.parents('.cartBox').remove();
        }
        closeM();
        $sonCheckBox = $('.son_check');
        totalMoney();
    })*/

    //======================================总计==========================================

    function totalMoney() {
        var total_money = 0;
        var total_count = 0;
        var calBtn = $('.calBtn a');
        $sonCheckBox.each(function () {
            if ($(this).is(':checked')) {
                var goods = parseInt($(this).parents('.order_lists').find('.sum_price').html().substring(1));
                var num =  parseInt($(this).parents('.order_lists').find('.sum').val());
                total_money += goods;
                total_count += num;
            }
        });
        $('.total_text').html('￥'+total_money);
        $('.piece_num').html(total_count);

        // console.log(total_money,total_count);

        if(total_money!=0 && total_count!=0){
            if(!calBtn.hasClass('btn_sty')){
                calBtn.addClass('btn_sty');
            }
        }else{
            if(calBtn.hasClass('btn_sty')){
                calBtn.removeClass('btn_sty');
            }
        }
    }
    /**
     * 订单结算
     */

    $(".calBtn a").on("click",function () {
        //var shopCartId = $("#shopCartId").val();
        var commodityIds = new Array();
        var cartItems = new Array();
        $(".order_content .order_lists").each(function (index, val) {
            var labelChild = $(this).find(".list_chk label");
            if(labelChild.hasClass("mark")) {
                commodityIds.push($(this).attr("data-skuId"));
                cartItems.push($(this).attr("data-itemId"));
            }
        })
        postToNewTabWithArrayByjQuery("/orders/orderConfirm", commodityIds, cartItems);
        //window.location.href = "/orders/orderConfirm?skuIds="+commodityIds;
    })
    //$(".calBtn a").css("pointer-events","none");
    function postToNewTabWithArrayByjQuery(url, array1, array2) {
        var $form = $('<form action="' + url + '" method="POST" target="_self" style="display:none;"></form>');
        for (var element in array1) {
            $form.append('<input type="hidden" name="skuIds[]" value="' + array1[element] + '" />');
        }
        for (var element in array2) {
            $form.append('<input type="hidden" name="cartItems[]" value="' + array2[element] + '" />');
        }
        $("body").append($form);
        $form.submit();
        $form.remove();
    };
});