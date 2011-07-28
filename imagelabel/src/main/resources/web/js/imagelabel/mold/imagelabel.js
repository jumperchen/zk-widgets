/**
* Here's the mold file , a mold means a HTML struct that the widget really presented.
* yep, we build html in Javascript , that make it more clear and powerful.
*/
function (out) {

	//Here you call the "this" means the widget instance. (@see ${component-xlass}.js)

	var zcls = this.getZclass(),
		uuid = this.uuid;

	out.push('<div id="', uuid, '" class="', zcls, '>',
			 '<span id="', uuid, '-image"><img src="', this._image, '" /></span>',
			 '<span id="', uuid, '-label" class="', zcls, '-label">', this._label, '</span>',
			 '</div>');

}