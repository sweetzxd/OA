/*document.addEventListener('touchmove', function (e) {
    e.preventDefault();
}, false);*/
if(document.getElementById('pullDown')){
    document.addEventListener('DOMContentLoaded', function () {
        setTimeout(loaded, 200);
    }, false);
}
var page=1,
    myScroll,
    pullDownEl,
    pullDownOffset,
    pullUpEl,
    pullUpOffset;

function pullDownAction() {
    setTimeout(function () {
        getLoadTableHtml();
        page=1;
        document.getElementById("pullUp").style.display = "";
        myScroll.refresh();
    }, 1000);
}

function pullUpAction() {
    setTimeout(function () {
        getNextTableHtml(page);
        page++;
        myScroll.refresh();
    }, 1000);
}

function loaded() {
    pullDownEl = document.getElementById('pullDown');
    pullDownOffset = pullDownEl.offsetHeight;
    pullUpEl = document.getElementById('pullUp');
    pullUpOffset = 10;
    myScroll = new iScroll('wrapper', {
        useTransition: true,
        topOffset: pullDownOffset,
        onRefresh: function () {
            if (pullDownEl.className.match('loading')) {
                pullDownEl.className = '';
                pullDownEl.querySelector('.pullDownLabel').innerHTML = '下拉刷新...';
            }
            if (pullUpEl.className.match('loading')) {
                pullUpEl.className = '';
                pullUpEl.querySelector('.pullUpLabel').innerHTML = '加载下一页...';
            }

            document.getElementById("pullUp").style.display = "none";
            document.getElementById("show").innerHTML = "onRefresh: up[" + pullUpEl.className + "],down[" + pullDownEl.className + "],Y[" + this.y + "],maxScrollY[" + this.maxScrollY + "],minScrollY[" + this.minScrollY + "],scrollerH[" + this.scrollerH + "],wrapperH[" + this.wrapperH + "]";
        },
        onScrollMove: function () {
            $('#table-down').html("");
            document.getElementById("show").innerHTML = "onScrollMove: up[" + pullUpEl.className + "],down[" + pullDownEl.className + "],Y[" + this.y + "],maxScrollY[" + this.maxScrollY + "],minScrollY[" + this.minScrollY + "],scrollerH[" + this.scrollerH + "],wrapperH[" + this.wrapperH + "]";
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
            document.getElementById("show").innerHTML = "onScrollEnd: up[" + pullUpEl.className + "],down[" + pullDownEl.className + "],Y[" + this.y + "],maxScrollY[" + this.maxScrollY + "],minScrollY[" + this.minScrollY + "],scrollerH[" + this.scrollerH + "],wrapperH[" + this.wrapperH + "]";
            if (pullDownEl.className.match('flip')) {
                pullDownEl.className = 'loading';
                pullDownEl.querySelector('.pullDownLabel').innerHTML = '加载中...';
                pullDownAction();
            }
            if (pullUpEl.className.match('flip')) {
                pullUpEl.className = 'loading';
                pullUpEl.querySelector('.pullUpLabel').innerHTML = '加载中...';
                pullUpAction();
            }
        }
    });
}

