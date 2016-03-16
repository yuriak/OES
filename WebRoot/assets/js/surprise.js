/**
 * 
 */
$(function () {
	var count=0;
	$('#indexLogo').click(function(){
		count++;
		if(count==3){
			alert("再点打死你！");
			count=0;
		}
	});
	
});