/**
* Here's the mold file , a mold means a HTML struct that the widget really presented.
* yep, we build html in Javascript , that make it more clear and powerful.
*/
function (out) {
	var uuid = this.uuid,
		zcls = this.getZclass();
	out.push('<div id="', uuid, '" class="', zcls, '">' , this.domContent_(), '</div>');
}