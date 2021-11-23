$(function(){
	$("#warp ul li").each(function() {
		$("#warp ul li").click(function() {
			$current = $(".content .contli").eq($(this).index()).offset().top;
//			console.log($current);
			$("html,body").animate({
				"scrollTop": $current-40
			}, 100);
			$("#warp ul li").eq($(this).index()).addClass("active").siblings().removeClass("active");
		})
	})

	$(window).scroll(function() {
		$_top = $(window).scrollTop();
//		console.log($_top);
		if($_top >= 0 && $_top < 200)$("#warp ul li").eq(0).stop().addClass("active").siblings().removeClass("active");
		if($_top >= 210 && $_top < 410) $("#warp ul li").eq(1).stop().addClass("active").siblings().removeClass("active");
		if($_top >= 420 && $_top < 620) $("#warp ul li").eq(2).stop().addClass("active").siblings().removeClass("active");
		if($_top >= 630 && $_top < 830) $("#warp ul li").eq(3).stop().addClass("active").siblings().removeClass("active");
		if($_top >= 840 && $_top < 1040) $("#warp ul li").eq(4).stop().addClass("active").siblings().removeClass("active");
		if($_top >= 1050 && $_top < 1250) $("#warp ul li").eq(5).stop().addClass("active").siblings().removeClass("active");
		if($_top >= 1260) {
			$("#box ul li ").eq(6).stop().addClass("active").siblings().removeClass("active");
		}
	})
	
	
})