var myScroll,
	pullDownEl,
	pullDownOffset,
	pullUpEl,
	pullUpOffset;

function loaded() {
	pullDownEl = document.getElementById('pullDown');
	pullUpEl = document.getElementById('pullUp');
	pullUpOffset = 10;
	myScroll = new iScroll('offCanvasContentScroll', {
		useTransition: true,
		onRefresh: function () {
            if (pullDownEl.className.match('loading')) {
                pullDownEl.className = '';
                pullDownEl.querySelector('.pullDownLabel').innerHTML = '下拉刷新...';
            }
			if (pullUpEl.className.match('loading')) {
				pullUpEl.className = '';
				pullUpEl.querySelector('.pullUpLabel').innerHTML = '下拉加载更多...';
			}

			document.getElementById("pullUp").style.display="none";
		},
		onScrollMove: function () {
            if (this.y > 0) {
                pullDownEl.className = 'flip';
                pullDownEl.querySelector('.pullDownLabel').innerHTML = '松开后刷新...';
                this.minScrollY = 0;
            }
            if (this.y < 0 && pullDownEl.className.match('flip')) {
                pullDownEl.className = '';
                pullDownEl.querySelector('.pullDownLabel').innerHTML = '下拉刷新...';
                this.minScrollY = -pullDownOffset;
            }

            if (this.scrollerH < this.wrapperH && this.y < (this.minScrollY - pullUpOffset) || this.scrollerH > this.wrapperH && this.y < (this.maxScrollY - pullUpOffset)) {
                document.getElementById("pullUp").style.display = "";
                pullUpEl.className = 'flip';
                pullUpEl.querySelector('.pullUpLabel').innerHTML = '松开后刷新...';
            }
            if (this.scrollerH < this.wrapperH && this.y > (this.minScrollY - pullUpOffset) && pullUpEl.className.match('flip') || this.scrollerH > this.wrapperH && this.y > (this.maxScrollY - pullUpOffset) && pullUpEl.className.match('flip')) {
                document.getElementById("pullUp").style.display = "none";
                pullUpEl.className = '';
                pullUpEl.querySelector('.pullUpLabel').innerHTML = '加载下一页...';
            }
		},
		onScrollEnd: function () {
            if (pullDownEl.className.match('flip')) {
                pullDownEl.className = 'loading';
                pullDownEl.querySelector('.pullDownLabel').innerHTML = '加载中...';
                new oA.before.load().pullDownAction();
            }
			 if (pullUpEl.className.match('flip')) {
				pullUpEl.className = 'loading';
				pullUpEl.querySelector('.pullUpLabel').innerHTML = '正中努力加载...';
				new oA.before.load().pullUpAction();
			}
		}
	});
}

document.addEventListener('touchmove', function (e) { e.preventDefault(); }, false);
document.addEventListener('DOMContentLoaded', function () { setTimeout(loaded, 200); }, false);