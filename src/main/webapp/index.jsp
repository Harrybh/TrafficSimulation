<html>

<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="initial-scale=1.0, user-scalable=no, width=device-width">
    <title>可视化</title>
    <link rel="stylesheet" href="https://cache.amap.com/lbs/static/main1119.css" />
    <script type="text/javascript"
            src="https://webapi.amap.com/maps?v=1.4.15&key=bee11f0b69f2ab082be9307b1538a1d4"></script>
            <script src="https://cdn.staticfile.net/jquery/1.10.2/jquery.min.js">
            </script>
    <style>
        html,
        body,
        #container {
            height: 100%;
            width: 100%;
        }

        .content-window-card {
            position: relative;
            box-shadow: none;
            bottom: 0;
            left: 0;
            width: auto;
            padding: 0;
        }

        .content-window-card p {
            height: 2rem;
        }

        .custom-info {
            border: solid 1px silver;
        }

        div.info-top {
            position: relative;
            background: none repeat scroll 0 0 #F9F9F9;
            border-bottom: 1px solid #CCC;
            border-radius: 5px 5px 0 0;
        }

        div.info-top div {
            display: inline-block;
            color: #333333;
            font-size: 14px;
            font-weight: bold;
            line-height: 31px;
            padding: 0 10px;
        }

        div.info-top img {
            position: absolute;
            top: 10px;
            right: 10px;
            transition-duration: 0.25s;
        }

        div.info-top img:hover {
            box-shadow: 0px 0px 5px #000;
        }

        div.info-middle {
            font-size: 12px;
            padding: 10px 6px;
            line-height: 20px;
        }

        div.info-bottom {
            height: 0px;
            width: 100%;
            clear: both;
            text-align: center;
        }

        div.info-bottom img {
            position: relative;
            z-index: 104;
        }

        span {
            margin-left: 5px;
            font-size: 11px;
        }

        .info-middle img {
            float: left;
            margin-right: 6px;
        }
    </style>
</head>

<body>
<div id="container"></div>
<script type="text/javascript">
    let lastRequestTime = 0;//两次请求时间间隔
    let initTime;
    let datalist;//后端传来的位置信息
    var carMarker=[];//每一个car标记的记录
    //工厂点位信息
    var lnglats = [
        [104.138734, 30.841976, "新都区百居益卫浴家俬厂", "斑竹园街道林泉社区16组101号白螺加油站右转500米"],
        [104.271130, 30.846617, "成都市和乐门业有限公司", "同心大道1236号"],
        [104.017882, 30.803865, "卡宾尼全屋定制厂", "成都市郫都区"],
        [104.021985, 30.828372, "成都富坤门窗", "新繁街道瑞云小区普文街"],
        [103.881413, 30.823710, "索亚全屋定制衣柜厂", "高铁西站对面"],
        [103.911916, 30.656526, "万家兴门窗厂", "高五路"],
        [104.025766, 30.761265, "胜杰工艺礼品厂", "安靖镇正义路128号"],
        [103.943072, 30.596499, "欧罗巴鞋业", "金牛坝路四号3栋21号"],
        [104.022721, 30.705629, "科新液压气动", "东升镇接待寺现代商贸园区双安路33号"],
        [104.039982, 30.771500, "方源木门厂", "王桥四组306附近"],
        [103.970745, 30.812381, "先锋门业", "团结镇靖源上街318号"],
        [104.597264, 30.859898, "成都贝乐宠物用品厂", "三合碑社区9组21号"],
        [104.096618, 30.876984, "新都区尚武家具厂", "新民镇九堰村5组201号"],
        [104.032824, 30.747666, "成都市金贯货架有限公司", "安靖街道雍渡村民巷二楼展厅"],
        [104.132616, 30.843174, "森泰莱免洗布艺沙发厂", "白桥路南30米"],
        [104.060300, 30.664960, "飞燕有机玻璃制品厂(东城根中街9号院店)", "东城根中街9号附1号"],
        [104.031251, 30.936561, "琥龙家具", "黄龙村13组106号"],
        [104.069117, 30.649155, "江西景德镇陶瓷厂(成都直销部)", "新开街1号附8号(锦江宾馆地铁站C1口步行440米"],
        [103.715899, 30.617399, "四川一宇钢结构工程有限公司", "晨曦大道中段801号"]
    ];

    // var car = [
    //     [103.96295, 30.77282, "类型：货车（厢式/板车）", ["载重量：35t", "尺寸：6.2x2.0x2", "品牌：解放"]],
    //     [103.88857, 30.70288, "类型：货车（厢式/板车）", ["载重量：25t", "尺寸：9.6x2.3x2.7", "品牌：威铃"]],
    //     [103.78159, 30.79242, "类型：货车（冷藏车）", ["载重量：8t", "尺寸：7.2x2.3x2.7", "品牌：--"]],
    //     [103.86664, 30.59429, "类型：货车（板车）", ["载重量：40t", "尺寸：16x2.5x2.4", "品牌：斯太尔"]],
    //     [104.00419, 30.59257, "类型：货车（行李托运） 集装箱", ["载重量：8t", "尺寸：4.2x1.9x1.8", "品牌：东风"]],
    //     [104.03210, 30.64774, "类型：危险品车", ["载重量：10", "尺寸：9.6x2.3x2.4", "品牌：江铃"]],
    //     [104.07994, 30.62705, "类型：危险品车", ["载重量：10", "尺寸：9.6x2.3x2.4", "品牌：威铃"]],
    //     [104.14705, 30.67474, "类型：危险品车", ["载重量：10", "尺寸：9.6x2.3x2.4", "品牌：江铃"]],
    //     [104.16831, 30.81364, "类型：货车（板车）", ["载重量：40t", "尺寸：12.5x2.4x2.7", "品牌：东风"]],
    //     [104.29156, 30.91687, "类型：货车（厢式/板车）", ["载重量：25t", "尺寸：9.6x2.3x2.7", "品牌：威铃"]],
    //     [104.24054, 30.85474, "类型：货车（厢式/板车）", ["载重量：35t", "尺寸：6.2x2.0x2", "品牌：解放"]],
    //     [104.36060, 30.75801, "类型：货车（冷藏车）", ["载重量：8t", "尺寸：7.2x2.3x2.7", "品牌：--"]],
    //     [104.22753, 30.62052, "类型：货车（板车）", ["载重量：40t", "尺寸：16x2.5x2.4", "品牌：斯太尔"]],
    //     [104.25454, 30.61359, "类型：货车（行李托运） 集装箱", ["载重量：8t", "尺寸：4.2x1.9x1.8", "品牌：东风"]],
    //     [104.16450, 30.57811, "类型：危险品车", ["载重量：10", "尺寸：9.6x2.3x2.4", "品牌：江铃"]],
    //     [104.11747, 30.58070, "类型：危险品车", ["载重量：10", "尺寸：9.6x2.3x2.4", "品牌：威铃"]],
    //     [104.18651, 30.84869, "类型：危险品车", ["载重量：10", "尺寸：9.6x2.3x2.4", "品牌：江铃"]],
    //     [103.94339, 30.95137, "类型：危险品车", ["载重量：10", "尺寸：9.6x2.3x2.4", "品牌：江铃"]],
    //     [103.92438, 30.57205, "类型：危险品车", ["载重量：10", "尺寸：9.6x2.3x2.4", "品牌：威铃"]],
    //     [104.07745, 30.54780, "类型：危险品车", ["载重量：10", "尺寸：9.6x2.3x2.4", "品牌：江铃"]],
    //     [104.02343, 30.52182, "类型：货车（板车）", "载重量：40t", "尺寸：12.5x2.4x2.7", "品牌：东风"]
    // ]

    //初始化地图对象，加载地图
    var map = new AMap.Map("container", {
        resizeEnable: true,
        center: [104.065861, 30.657401],
        zoom: 11
    });
    //构建自定义信息窗体
    function createInfoWindow(title, content) {
        var info = document.createElement("div");
        info.className = "custom-info input-card content-window-card";

        //可以通过下面的方式修改自定义窗体的宽高
        //info.style.width = "400px";
        // 定义顶部标题
        var top = document.createElement("div");
        var titleD = document.createElement("div");
        var closeX = document.createElement("img");
        top.className = "info-top";
        titleD.innerHTML = title;
        closeX.src = "https://webapi.amap.com/images/close2.gif";
        closeX.onclick = closeInfoWindow;

        top.appendChild(titleD);
        top.appendChild(closeX);
        info.appendChild(top);

        // 定义中部内容
        var middle = document.createElement("div");
        middle.className = "info-middle";
        middle.style.backgroundColor = 'white';
        middle.innerHTML = content;
        info.appendChild(middle);

        // 定义底部内容
        var bottom = document.createElement("div");
        bottom.className = "info-bottom";
        bottom.style.position = 'relative';
        bottom.style.top = '0px';
        bottom.style.margin = '0 auto';
        var sharp = document.createElement("img");
        sharp.src = "https://webapi.amap.com/images/sharp.png";
        bottom.appendChild(sharp);
        info.appendChild(bottom);
        return info;
    }

    //添加marker标记函数
    function addMarker(a) {
        var marker = new AMap.Marker({
            // icon: "//a.amap.com/jsapi_demos/static/demo-center/icons/poi-marker-default.png",
            map: map,
            position: [a.x, a.y],
            // icon: "https://a.amap.com/jsapi_demos/static/demo-center/icons/poi-marker-red.png"
        });
        //鼠标点击marker弹出自定义的信息窗体
        AMap.event.addListener(marker, 'click', function () {
            var infoWindow = new AMap.InfoWindow({
                isCustom: true,  //使用自定义窗体
                content: createInfoWindow(a[2], a[3]),
                offset: new AMap.Pixel(16, -45)
            });
            infoWindow.open(map, marker.getPosition());
        });
    }

    //添加car标记函数
    function addCar(a,i) {
        carMarker[i] = new AMap.Marker({
            map: map,
            position: [a.longitude, a.latitude],
            icon: "https://a.amap.com/jsapi_demos/static/demo-center/icons/poi-marker-red.png"
        });
        console.log(carMarker[i]);
        //鼠标点击marker弹出自定义的信息窗体
        // AMap.event.addListener(marker, 'click', function () {
        //     var infoWindow = new AMap.InfoWindow({
        //         isCustom: true,  //使用自定义窗体
        //         content: createInfoWindow(a[2], a[3].join("<br/>")),
        //         offset: new AMap.Pixel(16, -45)
        //     });
        //     infoWindow.open(map, marker.getPosition());
        // });
    }

    //移除car标记函数
    function removeCar(i) {
        if (carMarker[i]) {
            carMarker[i].setMap(null);
            carMarker[i] = null;
        }
    }

    //计算时间间隔函数


    //获取后端信息函数
    function getData(){
        let now=new Date();
        let timeInterval = null;
        if (lastRequestTime !== 0) {
        timeInterval = (now.getTime() - initTime) / 1000;  // 计算时间间隔，单位为秒
        lastRequestTime = now;  // 更新上一次请求的时间
        } else{
            timeInterval=lastRequestTime;
            lastRequestTime=now;
            initTime=now.getTime();
        }
        console.log(timeInterval);
        $.ajax({
            url: '/MainController/getAjaxData',
            type: 'GET',
            data:{
                timeInterval:timeInterval
            },
            success: function(data) {
                datalist=data.message  // 输出整个 data 对象
                renderCar();
            }
        })
    }

    //渲染car点位函数
    const renderCar = async ()=>{
        console.log(datalist);
        for(var i=0;i<datalist.length;i++){
            console.log("我执行了");
            removeCar(i);
            addCar(datalist[i],i);
        }
    }

    //关闭信息窗体函数
    function closeInfoWindow() {
        map.clearInfoWindow();
    }

        function getProductData(){
            $.ajax({
                url: '/MainController/getAjaxData2',
                type: 'GET',
                success: function(data) {
                    lnglats=data.message  // 输出整个 data 对象
                    console.log("zheli",lnglats);
                    renderFactory();
                }
            })
        }

    //渲染工厂点位
    function renderFactory(){
        for (var i = 0, marker; i < lnglats.length; i++) {
            addMarker(lnglats[i]);
        }
    }
   getProductData();
    //定时发送请求，渲染车辆点位
    setInterval(getData, 1000);
</script>
</body>

</html>
