/**
 * Created by Administrator on 2017/7/4/004.
 */
$(function () {

    //将某个地址设置为收货地址
    $("span.option-hover.t-setDefault").on("click",function () {
        var addressId = $(this).parent().attr("data-address");
        $.ajax({
            url:"/address/setDefaultAddr",
            type:"post",
            data:{"addressId":addressId},
            success:function () {
                window.location.reload(true);
            }
        })
    })

    /**
     * 地址非空检查
     */
    function addressCheckNull() {
        var province = $("#province").val();
        var city = $("#city").val();
        var area = $("#area").val();
        var street = $("#street").val();
        var detailAddr = $("#addressdetail").val();
        if(province.trim()==""||province.trim()=="-所在省-" || city.trim()=="" ||city.trim()=="-所在市-"|| area.trim()==""||area.trim()=="-所在区-" || street.trim()=="-选择街道-" ||street.trim()==""){
            var checselectkmsg = $("#checkSelectMsg");
            checselectkmsg.text("地址不能为空！");
            checselectkmsg.css('color', 'red');
            checselectkmsg.show();
            return true;
        }
        if(detailAddr.trim() == "") {

            var checkmsg = $("#checkmsg");
            checkmsg.text("详细地址不能为空！");
            checkmsg.css('color', 'red');
            checkmsg.show();
           /* $("#alertModal .modal-body").text("");
            $('#alertModal').modal('show');*/
            return true;
        }
        return false;
    }

    $("a.newaddress").on("click", function ()  {
        $("#myModalLabel2").html("添加收货地址");
        $("#btn-optionAddress").html("添加");
        $("#street-option-type").val("11");
        $("#type").val("add");
        $("#address-distpicker").distpicker('destroy');
        $("#address-distpicker").distpicker({
            province: '-所在省-',
            city: '-所在市-',
            district: '-所在区-'
        });
        $("#addressdetail").val("");
        $("#username").val("");
        $("#tel").val("");
        $("#postalcode").val("");

    })
    //选择街道
    function findStreet(data) {
        $("#street").empty();
        if(data != null)
        $.each(data, function(i, street) {
            $("#street").append("<option data-code='"+street.code+"' data-text='"+street.name+"' value='"+street.name+"'>" +street.name+"</option>")
        })
        $("#street").removeAttr("disabled");
    }
    function initStreet() {
        var street = $("#street").empty();
        street.append("<option data-code=\"\" data-text=\"\" value=\"-选择街道-\" >-选择街道-</option>")
    }

    $("#province").on("change", function() {
        initStreet();
        $("#street").attr("disabled",true);
    })
    $("#city").on("change", function() {
        initStreet();
        $("#street").attr("disabled",true);
    })
    //获取街道信息
    $("#area").on("change", function(){
        var areaCode = $("#area option:selected").attr("data-code");
        var streetType = $("#street-option-type").val();

        if(areaCode != "" && streetType=='11') {
            $.ajax(
                {
                type:"GET",
                url:"/address/getstreet?areaCode="+areaCode,
                dataType:"jsonp",
                jsonp:"callback",
                jsopCallBack:"findStreet",
                success: function f(data) {
                    findStreet(data);
                    return;
                }
            });
        }
        else if(areaCode != "" && streetType=='00'){
            //$("#street").append("<option value='"+data.street+"' selected>"+data.street+"</option>");
            return ;
        }
        return;
    });

    /*var provinceList;
    //查询所有省份
    function queryAllProvince() {
        $.ajax({
            url:basePath + "/address/findAllProvince",
            type:"get",
            success:function (data) {
                return data;
            }
        })
    }
    function findCityByProvince(index) {
        $("#city").empty();
        $.each(provinceList[index].cityList,function (i,city) {
            $("#city").append("<option value='"+city.id+"'>"+city.name+"</option>" +
                ""
            );
        });
    }*/
    /*function findCityByProvince2(index) {
        $("#city2").empty();
        $.each(provinceList[index].cityList,function (i,city) {
            $("#city2").append("<option value='"+city.id+"'>"+city.name+"</option>" +
                ""
            );
        });
    }*/
/*
    //新增地址
     $(".newaddress").on("click",function () {
         // alert(provinceList)
         $.ajax({
             url:"/address/findAllProvince",
             type:"get",
             success:function (data) {
                 provinceList = data;
                 $.each(provinceList,function (i,pro) {
                     $("#province").append("<option value='"+pro.id+"'>"+pro.name+"</option>" +
                         "");
                 });
                 $.each(provinceList,function (i,pro) {
                     if(pro.id == $("#province").val()){
                         findCityByProvince(i);
                     }
                 });
             }
       })
         /*
         //选择省份时加载其收货地区
         $("#province").on("change",function () {
             var index = $(this).find("option:selected").index();
             findCityByProvince(index);
         });
     });*/
    //提交收货地址表单
    $("#btn-optionAddress").on("click",function () {
        if(addressCheckNull()) {
            return;
        }
        var type = $("#type").val();
        if(type=="update") {
            $.ajax({
                url:"/address/updateAddr",
                type:"post",
                data:$("#form-optionAddress").serialize(),
                success:function () {
                    window.location.reload(true);
                }
            });
        }
        if(type == "add") {
            $.ajax({
                url: "/address/addAddress",
                type: "post",
                data: $("#form-optionAddress").serialize(),
                success: function () {
                    window.location.reload(true);
                }
            })
        }
    });
    //编辑收货地址
    $(".option-hover.t-change").on("click",function () {
        /*$("#province").unbind("change");
        $("#city").unbind("change");*/
        //$("#area").unbind("change");
        $("#type").val("update");
        $("#street-option-type").val("00");
        $("#myModalLabel2").html("修改收货地址");
        $("#btn-optionAddress").html("修改");
        var addressId = $(this).parent().attr("data-address");
        $("#addressId").val(addressId);
        $.ajax({
            url:"/address/getAddr?addressId="+addressId,
            type:"get",
            success:function (data) {
                if(data!=null) {
                    $("#addressdetail").val(data.addressDetail);
                    $("#username").val(data.userName);
                    $("#tel").val(data.telephone);
                    $("#postalcode").val(data.postalCode);
                    $("#address-distpicker").distpicker('destroy');
                    $("#address-distpicker").distpicker({
                        province: data.province,
                        city: data.city,
                        district: data.area
                    });
                    $("#street").append("<option value='"+data.street+"' selected>"+data.street+"</option>");
                    $("#street-option-type").val("11");
                }
            }
        })
    });

    //删除收货地址
    $(".option-hover.t-delete").on("click",function () {
        var addressId = $(this).parent().attr("data-address");
        $.ajax({
            url:"/address/deleteAddr?addressId="+addressId,
            type:"delete",
            success:function () {
                window.location.reload(true);
            }
        })
    });
});