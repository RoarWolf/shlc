$(function(){
	var style=`
			@font-face {font-family: "ad-font";
			  src: url('//at.alicdn.com/t/font_2041105_xhmhury01y.eot?t=1599009976073'); /* IE9 */
			  src: url('//at.alicdn.com/t/font_2041105_xhmhury01y.eot?t=1599009976073#iefix') format('embedded-opentype'), /* IE6-IE8 */
			  url('data:application/x-font-woff2;charset=utf-8;base64,d09GMgABAAAAAALgAAsAAAAABsgAAAKSAAEAAAAAAAAAAAAAAAAAAAAAAAAAAAAAHEIGVgCCcAqBVIFRATYCJAMICwYABCAFhG0HMBv3BcgusG3Yk0ALqw4bZvbdsZYhIBwiqNb+7bmdj6gAUAJqDKhUZNh4RidY2KhYICPe/87lP2A1cpTnj+WpEaljW/hJ03LSdqAAhZuRswR6k5vbhJskt5zgGdKThaj85MJEoRZoUQMqLv7ziXunzfMukPkaOJzmWPypCzAOpDTaE6PICiQAT0W8E+kO8jGBZkPKEofLm13IS1gViFucKZAvJKRUyo1CfcPBFE+gTmN1N/mCx9H347+1yFOpK9B1cmVJh9lft6mq7P3jUkCCJEE4XoeCBSCJ842FIymhMFOaJdPGgJTpX6e1RZc46p8n6qG1FQzwimdSeqpHFa9AMppN4PKkSyitrRPK+6/+/R7Mz17/2GW6bizLT99fwzzfV30lvO+L4AWMrghfZtnTWVv1b33LycnktW+yjNdkXW3azWMmInnGYyQ0vPX9M7oZjBE6Hj9/5SeNQUfoBHv+1LAcZgFqZ+WvbP/W39h7NLklfTeWEnyPeBnFamcJw0Kg8eTB4CcqB/akrYazSlPZWKlsSt2crpnS1ACRbp9TLX13XAmN+u471GAwQ9Fogpz0BdRpsYF6jXbQbN7K8RY9hJTIJsw55hA6XaDS7gOKTq/kpH+gTr8v1OuMFJpdDBe2mApeXKASok7UHcI1z6XMHRbj9jayyFZEXh2x+igkT5PG0kq5vIIuiiW2yJg1w5ASKjyHLHPPoW17xBeeiVpYNcLQb9WWrzS9qap5DhQdEJAihHSEagfBaTwuGgwOFSufb0NMxKYQHYSWdB8SJD4+0VBVH0BeMbiDCI/yihRjmkIhiqAEj4NY5nzIpoKH8JvnmZAmVGVMyPlaakI/OtRQ3d7k/N0haAZrFTSrqL8aKa7KAQAAAA==') format('woff2'),
			  url('//at.alicdn.com/t/font_2041105_xhmhury01y.woff?t=1599009976073') format('woff'),
			  url('//at.alicdn.com/t/font_2041105_xhmhury01y.ttf?t=1599009976073') format('truetype'), /* chrome, firefox, opera, Safari, Android, iOS 4.2+ */
			  url('//at.alicdn.com/t/font_2041105_xhmhury01y.svg?t=1599009976073#iconfont') format('svg'); /* iOS 4.1- */
			}
			.ad-font {
			  font-family: "ad-font" !important;
			  font-size: 16px;
			  font-style: normal;
			  -webkit-font-smoothing: antialiased;
			  -moz-osx-font-smoothing: grayscale;
			}
	
			.icon-guanbi:before {
			  content: "\\e687";
			}

			.ad-content {
				position: fixed;
				left: 0;
				right: 0;
				top: 0;
				bottom: 0;
				z-index: 100000;
				background-color: rgba(0,0,0,.3);
				display: none;
			}
			.ad-content-box {
				position: absolute;
				left: 50%;
				top: 50%;
				width: 85%;
				transform: translate(-50%, -50%);
				-webkit-transform: translate(-50%, -50%);
				-moz-transform: translate(-50%, -50%);
				-ms-transform: translate(-50%, -50%);
				-o-transform: translate(-50%, -50%);
			}
			.ad-content-box .ad-link {
				text-decoration: none;
				display: block;
				width: 100%;
			}
			.ad-content-box .ad-link img {
				width: 100%;
				border-radius: 12px;
			}
			.ad-close {
				text-align: center;
				margin-top: 4vh;
			}
			.ad-close i {
				font-size: 36px;
				color: #fff;
			}
			`
	var str= `
		 <div class="ad-content-box">
			<a href="https://wallet.95516.com/s/wl/webV3/activity/vInvite/html/snsIndex.html?r=8cb9766c3e9adc200e04d5fa6024a40b&code=ctoc00000000089" class="ad-link">
				<div class="ad-img-box">
					
				</div>
				<div class="ad-desc"></div>
			</a>
			<div class="ad-close">
				<i class="ad-font icon-guanbi ad-close-i"></i>
			</div>
		</div>
	`
	var $style= $('<style>'+style+'</style>')
	$(document.head).append($style)
	var adContent= $('<div class="ad-content"></div>')
	adContent.html(str)
	$('body').append(adContent)
	var oImg= new Image()
	$(oImg).on('load',function(e){
		handleLoadFn()
	})
	oImg.src= '/images/云闪付宣传图.jpg'
	oImg.alt="云闪付展示图" 
	oImg.title="云闪付展示图"
	$('.ad-img-box').append($(oImg))

	function handleLoadFn(){
		console.log(adContent)
		adContent.fadeIn(300)
	}

	adContent.on('click',function(e){
		e= e || window.event
		var target=  e.target || e.srcElement
		if($(target).hasClass('ad-close-i')){
			adContent.fadeOut(300)
		}
	})

})
