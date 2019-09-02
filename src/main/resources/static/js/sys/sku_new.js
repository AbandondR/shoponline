var sku_data;
var default_price;

$(function(){
    //default_price = $('#');
    //此规格组合有3*3=12种情况
    $.getJSON('/product/sku-json?productId=1', function(data) {
        sku_data = data.data;
        var attr_prop=$(".tb-item-info-right .tb-skin .tb-sku-prop");
        var attr_prop_name = '';
        //遍历属性名
        $.each(sku_data.propNameList, function(index1, val1){
            //是否图片展示
            var propAttr = val1.isNeedImage=='1' ? true : false;
            var attr_prop_value = "";
            if(!propAttr){
                //attr_prop = $(".J_Prop .J_TMySizeProp .tb-prop .tb-clear .J_Prop_measurement");

                attr_prop_name = "<dl class='J_Prop J_TMySizeProp tb-prop tb-clear J_Prop_measurement'><dt class='tb-property-type' data-propNameId='"+val1.id+"'>"+val1.propName+"</dt>"+"<dd> <ul class='J_TSaleProp tb-clearfix'>";
                $.each(val1.propValueList, function(index2, val2){
                    attr_prop_value += "<li data-skuid='"+val1.id+":"+val2.id+"' class='sku'><a><span>"+val2.propValue+"</span></a></li>"
                });
            }
            else {
                //attr_prop = $(".J_Prop .tb-prop .tb-clear .J_Prop_Color");
                attr_prop_name = "<dl class='J_Prop tb-prop tb-clear J_Prop_Color'><dt class='tb-property-type'>"+val1.propName+"</dt>"+"<dd><ul data-property='"+val1.name+"' class='J_TSaleProp tb-img tb-clearfix'>";
                $.each(val.propValueList, function(index2, val2){
                    attr_prop_value +=
                        "<li data-skuid='"+val1.id+":"+val2.id+"' class='sku'><a herf='javascript:;' style='background:url('"+val2.imgLocation+"') center no-repeat;' title='"+val2.propValue+"' data-skuValId='"+val1.id+":"+val2.id+"'></a><span>"+val2.propValue+"</span></li>";
                });
            }
            attr_prop_name += attr_prop_value+"</ul></dd></dl>";
            attr_prop.append(attr_prop_name);
        });
        setSku();//初始化sku状态
        console.log(SKUResult, 'SKUResult');
        $(".tb-btn-add a").click(function(){
            var str=isSelectAll();
            if(str.length>0){
                alert("请选择："+str);
            }
            else{
                var confirmId = $("#confirm_id").val();
                var index = sku_data.productSkuList.findIndex(item => item.id==confirmId);
                var productSku = sku_data.productSkuList[index];
                var skuGroup = productSku.skuGorup;
                var data = $.param(productSku)+"&productNum="+$("#J_IptAmount").val();
                var propNameStr = '';
                var arr = skuGroup.split(",");
                for(var i=0; i<arr.length; i++) {
                    var index1 = sku_data.propNameList.findIndex(item => item.id==arr[i].split(":")[0]);
                    var index2 = sku_data.propNameList[index1].propValueList.findIndex(item => item.id==arr[i].split(":")[1]);
                    var name = sku_data.propNameList[index1].propName;
                    var value = sku_data.propNameList[index1].propValueList[index2].propValue;
                    propNameStr += name+":"+value+",";
                }
                data += "&skuGroupStr="+propNameStr.substr(0, propNameStr.length-1);
                //data += "&locationHref="+window.location.href;
                console.log(data);
                $.ajax({
                    url:"/shopcart/addProduct",
                    type:"post",
                    data:data,
                    success: function(data){
                        if(data=="false") {
                            return;
                        }
                        else if(data=='ok'){
                            alert('成功加入购物车！');
                            return;
                        }
                        else if(data=="redirect"){
                            window.location.href="/user/login";
                        }
                    }
                })

            }
        });
    });
});

function setSku(){
    initSku();
    $('.sku').each(function() {
        var self = $(this);
        var attr_id = self.data('skuid');
        if(!SKUResult[attr_id]) {
            self.addClass('disabled');
        }
    }).click(function() {
        var self = $(this);
        //选中自己，兄弟节点取消选中
        self.toggleClass('selected').siblings().removeClass('selected');
        showInfo();
        //已经选择的节点
        var selectedObjs = $('.tb-sku-prop dl .selected');
        if(selectedObjs.length) {
            //获得组合key价格
            var selectedIds = [];
            selectedObjs.each(function() {
                selectedIds.push($(this).data('skuid'));
            });
            selectedIds.sort(function(value1, value2) {
                //从小到大排序
                var val1 = value1.split(":")[1];
                var val2 = value2.split(":")[1];
                return parseInt(val1) - parseInt(val2);
            });
            var len = selectedIds.length;
            var price = SKUResult[selectedIds.join(';')].price;
            var stock = SKUResult[selectedIds.join(';')].stock;
            if(price instanceof Array) {
                var maxPrice = Math.max.apply(Math, price);
                var minPrice = Math.min.apply(Math, price);
                if(price || stock) {
                    $('.tb-price-panel .tb-price-rmb-num').text(maxPrice > minPrice ? minPrice + "-" + maxPrice : maxPrice);
                }
            }
            else {
                $('.tb-price-panel .tb-price-rmb-num').text(price);
            }
            $('.tb-stock .tb-count').text(stock!=null?stock:'0');
            /*if(price || stock) {
                if(price instanceof Array) {
                    $('.tb-price-panel .tb-price-rmb-num').text(maxPrice > minPrice ? minPrice + "-" + maxPrice : maxPrice);
                }
                else {
                    $('.tb-price-panel .tb-price-rmb-num').text(price);
                }
                $('.tb-stock .tb-count').text(stock);
            }*/
            //用已选中的节点验证待测试节点 underTestObjs
            console.log($(".sku").not(selectedObjs).not(self))
            $(".sku").not(selectedObjs).not(self).each(function() {
                var siblingsSelectedObj = $(this).siblings('.selected');
                console.log(siblingsSelectedObj)
                var testAttrIds = [];//从选中节点中去掉选中的兄弟节点
                if(siblingsSelectedObj.length) {
                    var siblingsSelectedObjId = siblingsSelectedObj.data('skuid');
                    for(var i = 0; i < len; i++) {
                        (selectedIds[i] != siblingsSelectedObjId) && testAttrIds.push(selectedIds[i]);
                    }
                } else {
                    testAttrIds = selectedIds.concat();
                }
                testAttrIds = testAttrIds.concat($(this).data('skuid'));
                testAttrIds.sort(function(value1, value2) {
                    var val1 = value1.split(":")[1];
                    var val2 = value2.split(":")[1];
                    return parseInt(val1) - parseInt(val2);
                });
                if(!SKUResult[testAttrIds.join(';')]) {
                    $(this).addClass('disabled').removeClass('selected');
                } else {
                    $(this).removeClass('disabled');
                }
            });
        } else {
            //设置默认价格
            $('#price').text('--');
            //设置属性状态
            $('.sku').each(function() {
                SKUResult[$(this).data('skuid')] ? $(this).removeClass('disabled') : $(this).addClass('disabled').removeClass('selected');
            })
        }
    });
}
//根据规格值id拼接，确定一个sku
function confimSku(){
    var attr_col=$("#sku_id").text();
    $("#confirm_id").val('');
    $.each(sku_data.productSkuList,function(key,val){
        if(val.skuGorup===attr_col || val.skuGorup===attr_col.split(";").reverse().join(",")){
            $("#confirm_id").val(val.id);
            console.log('sku_id:' + val.id)
        }
        return;
    })
}
function showInfo(){
    var flag=isSelectAll();
    if(flag.length===0){
        $("#showInfo").show();
    }
    else{
        $("#showInfo").hide();
    }
}
//判断规格是否全部选中
function isSelectAll(){
    var tip_str=[];
    $(".tb-item-info-right .tb-skin .tb-sku-prop dl").each(function(key,val){
        var isSelected=$(this).find(".selected").length;
        if(isSelected===0){
            tip_str.push($(this).children('.tb-property-type').text());
        }
    });
    var len=tip_str.length;
    $("#sku_id").text(pingSku());
    confimSku();
    return tip_str.join(',');
}
//规格值id拼接
function pingSku(){
    var str=[];
    $(".tb-item-info-right .tb-skin .tb-sku-prop dl").each(function(key,val){
        var ele=$(this).find(".selected");
        if(ele.length>0){
            str.push($(ele).data('skuid'));
        }
    });
    str=str.join(';');
    return str;
}
function initSku(){
    var i,
        j,
        skuKeys = getObjKeys(sku_data.productSkuList);// 拿到数组的sku， 返回的是键数组["110;310", "110;311", "111;312", "112;311"]
    console.log(skuKeys, 'skuKeys')
    for(i = 0; i < skuKeys.length; i++) {
        var skuKey = skuKeys[i];// 一条SKU信息key， 如 110;210;311;414
        console.log(skuKey,'单个skuKey');
        let index = sku_data.productSkuList.findIndex(items => items.skuGorup == skuKey)
        var sku = index > -1 ? sku_data.productSkuList[index] : 0; // 一条SKU信息value,主要拿这个的price和库存
        console.log(sku, `第${i}条sku`)
        var skuKeyAttrs = skuKey.split(","); //将sku 分割SKU key;
        console.log(skuKeyAttrs, '分割单个skuKey');
        //对分割SKU key 从小到大排序
        skuKeyAttrs.sort(function(value1, value2) {
            var val1 = value1.split(":")[1];
            var val2 = value2.split(":")[1];
            return parseInt(val1) - parseInt(val2);
        });
        //对每个SKU信息key属性值进行拆分组合,形成一个新的数组
        var combArr = combInArray(skuKeyAttrs);
        for(j = 0; j < combArr.length; j++) {
            add2SKUResult(combArr[j], sku);

        }
        //结果集接放入SKUResult
        console.log(sku, 'sku')
        SKUResult[skuKeyAttrs.join(";")] = sku
    }
}
//获得对象的key(键)
function getObjKeys(obj) {
    if (obj !== Object(obj)){
        throw new TypeError('Invalid object');
    }
    var keys = [];
    var len=obj.length;
    for(var i=0;i<len;i++){
        if(obj[i]['skuGorup']!=''){
            keys[keys.length] =obj[i]['skuGorup'];
        }
    }
    console.log(keys);
    return keys;
}

/**
 * 从数组中生成指定长度的组合
 * 方法: 先生成[0,1...]形式的数组, 然后根据0,1从原数组取元素，得到组合数组
 */
function combInArray(aData) {
    if(!aData || !aData.length) {
        return [];
    }

    var len = aData.length;
    var aResult = [];

    for(var n = 1; n < len; n++) {
        var aaFlags = getCombFlags(len, n);
        while(aaFlags.length) {
            var aFlag = aaFlags.shift();
            var aComb = [];
            for(var i = 0; i < len; i++) {
                aFlag[i] && aComb.push(aData[i]);
            }
            aResult.push(aComb);
        }
    }
    return aResult;
}

/**
 * 得到从 m 元素中取 n 元素的所有组合
 * 结果为[0,1...]形式的数组, 1表示选中，0表示不选
 */
function getCombFlags(m, n) {
    if(!n || n < 1) {
        return [];
    }

    var aResult = [];
    var aFlag = [];
    var bNext = true;
    var i, j, iCnt1;

    for (i = 0; i < m; i++) {
        aFlag[i] = i < n ? 1 : 0;
    }

    aResult.push(aFlag.concat());

    while (bNext) {
        iCnt1 = 0;
        for (i = 0; i < m - 1; i++) {
            if (aFlag[i] == 1 && aFlag[i+1] == 0) {
                for(j = 0; j < i; j++) {
                    aFlag[j] = j < iCnt1 ? 1 : 0;
                }
                aFlag[i] = 0;
                aFlag[i+1] = 1;
                var aTmp = aFlag.concat();
                aResult.push(aTmp);
                if(aTmp.slice(-n).join("").indexOf('0') == -1) {
                    bNext = false;
                }
                break;
            }
            aFlag[i] == 1 && iCnt1++;
        }
    }
    return aResult;
}

//把组合的key放入结果集SKUResult
function add2SKUResult(combArrItem, sku) {
    var key = combArrItem.join(";");
    if(SKUResult[key]) {//SKU信息key属性·
        SKUResult[key].stock += parseInt(sku.stock);
        SKUResult[key].price.push(sku.price);
    } else {
        SKUResult[key] = {
            stock : parseInt(sku.stock),
            price : [sku.price]
        };
    }
}
//保存最后的组合结果信息
var SKUResult = {};